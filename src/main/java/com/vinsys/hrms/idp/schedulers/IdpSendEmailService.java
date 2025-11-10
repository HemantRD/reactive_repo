package com.vinsys.hrms.idp.schedulers;

import com.vinsys.hrms.dao.IHRMSEmailTemplateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.idp.IdpEmailLogDAO;
import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.idp.entity.IdpEmailLogEntity;

import static com.vinsys.hrms.idp.utils.IdpEnums.Status.*;

import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IdpSendEmailService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final EmailSender emailsender;
    private final IHRMSEmailTemplateDAO emailTemplateDAO;
    private final IHRMSEmployeeDAO employeeDAO;
    private final IdpEmailLogDAO idpEmailLogDAO;


    public IdpSendEmailService(final EmailSender emailsender, final IHRMSEmailTemplateDAO emailTemplateDAO,
                               final IHRMSEmployeeDAO employeeDAO, final IdpEmailLogDAO idpEmailLogDAO) {
        this.emailsender = emailsender;
        this.emailTemplateDAO = emailTemplateDAO;
        this.employeeDAO = employeeDAO;
        this.idpEmailLogDAO = idpEmailLogDAO;
    }

    public void sendEmailToIdpInformationFollowup() {
        log.info("Sending Idp email to Employee :::");

        List<IdpEmailLogEntity> pendingList = idpEmailLogDAO.findByStatus(PENDING.getKey());

        if (CollectionUtils.isEmpty(pendingList)) {
            return;
        }

        for (IdpEmailLogEntity entity : pendingList) {
            try {
                Employee employee = employeeDAO.findActiveEmployeeById(entity.getIdpFlow().getEmployeeId(),
                        IHRMSConstants.isActive);
                String empMailId = employee.getOfficialEmailId();
                String templateName = getTemplateCodeByRole(entity);
                EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
                        templateName, IHRMSConstants.isActive, employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
                        employee.getCandidate().getLoginEntity().getOrganization());
                if (!HRMSHelper.isNullOrEmpty(template)) {
                    String mailBody = getEmailBody(templateName, template.getTemplate(), entity);
                    emailsender.toPersistEmail(empMailId, null, mailBody, template.getEmailSubject(),
                            employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
                            employee.getCandidate().getLoginEntity().getOrganization().getId());
                    entity.setStatus("Sent");
                    log.info("Email sent successfully to Employee :::");
                } else {
                    entity.setStatus("Error");
                    entity.setLogMessage("Email template not found for Reminder mail to Employee .");
                    log.error("Email template not found for Reminder mail to Employee .");
                }
            } catch (Exception e) {
                entity.setStatus("Error");
                entity.setLogMessage(e.getMessage());
                log.error("Unable to queue email for IdpEmailLogEntity Id: {},  Error is: {}", entity.getId(), e.getMessage());
            }
            idpEmailLogDAO.save(entity);
        }
    }


    private String getTemplateCodeByRole(IdpEmailLogEntity entity) {
        String templateName = "EMAIL_TEMPLATE_";
        if (entity.getActionType().equalsIgnoreCase("Information")) {
            templateName += "INFO_";
        } else {
            templateName += "FU_";
        }
        if (entity.getIdpFlow().getEmployeeRole().equalsIgnoreCase(ERole.EMPLOYEE.name())) {
            templateName += "TM";
            return templateName;
        }
        if (entity.getIdpFlow().getEmployeeRole().equalsIgnoreCase(ERole.MANAGER.name())) {
            templateName += "LM";
            return templateName;
        }
        if (entity.getIdpFlow().getEmployeeRole().equalsIgnoreCase(ERole.TDHEAD.name())) {
            templateName += "TDHEAD";
            return templateName;
        }
        throw new IllegalArgumentException("Invalid Role :" + entity.getIdpFlow().getEmployeeRole());
    }

    private String getEmailBody(String templateName, String rawEmailBody, IdpEmailLogEntity entity) {
        Map<String, String> placeHolderMap = new HashMap<>();
        switch (templateName) {
            case "EMAIL_TEMPLATE_INFO_TM":
                placeHolderMap.put("{{IDP_DETAILS}}", "TBD");
                break;
            case "EMAIL_TEMPLATE_INFO_LM":
                placeHolderMap.put("{{NAME}}", "TBD");
                break;
            case "EMAIL_TEMPLATE_INFO_TDHEAD":
                placeHolderMap.put("{{LINE_MANAGER_NAME}}", "TBD");
                break;
            case "EMAIL_TEMPLATE_FU_TM":
                break;
            case "EMAIL_TEMPLATE_FU_LM":
                placeHolderMap.put("{{NAME}}", "TBD");
                break;
            case "EMAIL_TEMPLATE_FU_TDHEAD":
                placeHolderMap.put("{{LINE_MANAGER_NAME}}", "TBD");
                break;
        }
        return HRMSHelper.replaceString(placeHolderMap, rawEmailBody);
    }
}
