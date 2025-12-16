package com.vinsys.hrms.idp.schedulers;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.idp.IdpEmailLogDAO;
import com.vinsys.hrms.email.service.impl.EmailTransactionServiceImpl;
import com.vinsys.hrms.email.utils.EventsConstants;
import com.vinsys.hrms.email.vo.EmailRequestVO;
import com.vinsys.hrms.email.vo.PlaceHolderVO;
import com.vinsys.hrms.email.vo.TemplateVO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.idp.entity.IdpEmailLog;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.IHRMSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.vinsys.hrms.idp.utils.IdpEnums.Status.PENDING;

@Service
public class IdpSendEmailService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final IHRMSEmployeeDAO employeeDAO;
    private final IdpEmailLogDAO idpEmailLogDAO;
    private final EmailTransactionServiceImpl emailService;


    public IdpSendEmailService(final IHRMSEmployeeDAO employeeDAO, final IdpEmailLogDAO idpEmailLogDAO,
                               final EmailTransactionServiceImpl emailService) {
        this.employeeDAO = employeeDAO;
        this.idpEmailLogDAO = idpEmailLogDAO;
        this.emailService = emailService;
    }

    public void sendEmailToIdpInformationFollowup() {

        log.info("Sending Idp email to Employee :::");
        List<IdpEmailLog> pendingList = idpEmailLogDAO.findByStatus(PENDING.getKey());

        if (CollectionUtils.isEmpty(pendingList)) {
            return;
        }

        for (IdpEmailLog entity : pendingList) {
            try {
                Employee employee = employeeDAO.findActiveEmployeeById(entity.getIdpFlow().getEmployeeId(),
                        IHRMSConstants.isActive);
                EmailRequestVO emailRequestVO = new EmailRequestVO();
                emailRequestVO.setEmailAddress(employee.getOfficialEmailId());
                emailRequestVO.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

                TemplateVO managerTemplate = new TemplateVO();
                managerTemplate.setTemplateName(getTemplateCodeByRole(entity));

                List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
//                PlaceHolderVO placeHolderVo = new PlaceHolderVO();
//                placeHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
//                placeHolderVo.setExpressionVariableValue(
//                        employee.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName() + " "
//                                + employee.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());
//                PlaceHolderVO subPlaceHolderVo = new PlaceHolderVO();
//                subPlaceHolderVo.setExpressionVariableName(IHRMSConstants.SUBORDINATE_KEY);
//                subPlaceHolderVo.setExpressionVariableValue(
//                        employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
//                placeHolders.add(placeHolderVo);
//                placeHolders.add(subPlaceHolderVo);
                managerTemplate.setPlaceHolders(placeHolders);
                emailRequestVO.setTemplateVo(managerTemplate);
                emailService.insertInEmailTxnTable(emailRequestVO);
                log.info("Email sent successfully to Employee :::");
                entity.setStatus("Sent");
            } catch (Exception e) {
                entity.setStatus("Error");
                entity.setLogMessage(e.getMessage());
                log.error("Unable to queue email for IdpEmailLog Id: {},  Error is: {}", entity.getId(), e.getMessage());
            }
            idpEmailLogDAO.save(entity);
        }
    }


    private String getTemplateCodeByRole(IdpEmailLog entity) {
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
        if (entity.getIdpFlow().getEmployeeRole().equalsIgnoreCase(ERole.DIVISIONHEAD.name())) {
            templateName += "DIVISIONHEAD";
            return templateName;
        }
        throw new IllegalArgumentException("Invalid Role :" + entity.getIdpFlow().getEmployeeRole());
    }
}
