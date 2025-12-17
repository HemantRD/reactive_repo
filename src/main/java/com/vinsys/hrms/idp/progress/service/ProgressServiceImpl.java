package com.vinsys.hrms.idp.progress.service;

import com.vinsys.hrms.constants.EResponse;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.idp.IdpProgressHistoryDAO;
import com.vinsys.hrms.dao.idp.IdpTrainingDetailsDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.entity.IdpDetails;
import com.vinsys.hrms.idp.entity.IdpProgressHistory;
import com.vinsys.hrms.idp.progress.validator.ProgressValidator;
import com.vinsys.hrms.idp.progress.vo.BulkExcelProgressVo;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ResponseCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ProgressServiceImpl implements IProgressService {

    private final ProgressValidator validator;
    private final TrainingCatalogDAO trainingCatalogDAO;
    private final IHRMSEmployeeDAO employeeDAO;
    private final IdpProgressHistoryDAO idpProgressHistoryDAO;
    private final IdpTrainingDetailsDAO idpTrainingDetailsDAO;


    public ProgressServiceImpl(final ProgressValidator validator, final TrainingCatalogDAO trainingCatalogDAO,
                               final IHRMSEmployeeDAO employeeDAO, final IdpProgressHistoryDAO idpProgressHistoryDAO,
                               final IdpTrainingDetailsDAO idpTrainingDetailsDAO) {
        this.validator = validator;
        this.trainingCatalogDAO = trainingCatalogDAO;
        this.employeeDAO = employeeDAO;
        this.idpProgressHistoryDAO = idpProgressHistoryDAO;
        this.idpTrainingDetailsDAO = idpTrainingDetailsDAO;
    }

    @Transactional
    public HRMSBaseResponse<String> uploadBulkProgressExcel(MultipartFile excelFile) throws HRMSException, Exception {
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        List<BulkExcelProgressVo> bulkExcelProgressVoList = validator.validateExcelFileDataAndGetList(excelFile);

        List<IdpProgressHistory> progressHistoryEntities = new ArrayList<>();
        for (BulkExcelProgressVo data : bulkExcelProgressVoList) {
            Long trainingId = trainingCatalogDAO.getIdByTrainingCode(data.getTrainingCode());
            Employee employee = employeeDAO.findByofficialEmailId(data.getMemberEmail());
            IdpDetails IdpDetails = idpTrainingDetailsDAO.getIdpDetailsData(trainingId, employee.getId());
            Long idpDetailId = IdpDetails.getIdp().getDetails()
                    .stream().filter(r -> Objects.equals(r.getTraining().getId(), trainingId)).findFirst().get().getId();
            System.out.println("IdpDetails.getId()" + IdpDetails.getId() +
                    " IdpDetails.getIdp().getId() " + IdpDetails.getIdp().getId() +
                    " employee.getId() " + employee.getId());
            idpProgressHistoryDAO.markOldRecordsInActive(IdpDetails.getIdp().getId(), idpDetailId, employee.getId());
            progressHistoryEntities.add(populateProgressHistory(IdpDetails, data, trainingId, employee));
        }
        idpProgressHistoryDAO.saveAll(progressHistoryEntities);
        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
        response.setResponseCode(EResponse.SUCCESS.getCode());
        response.setResponseMessage(EResponse.SUCCESS.getMessage());
        return response;
    }

    private IdpProgressHistory populateProgressHistory(IdpDetails IdpDetails,
                                                             BulkExcelProgressVo data, Long trainingId, Employee employee) {
        Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
        IdpProgressHistory progressHistory = new IdpProgressHistory();
        progressHistory.setIdp(IdpDetails.getIdp());
        progressHistory.setIdpDetail(IdpDetails.getIdp().getDetails()
                .stream().filter(r -> Objects.equals(r.getTraining().getId(), trainingId)).findFirst().get());
        progressHistory.setEmployeeId(employee.getId());
        progressHistory.setLogDate(data.getProgressDate());
        progressHistory.setProgressValue(data.getProgressValue());
        progressHistory.setProgressUnit(data.getProgressUnit());
        progressHistory.setStatus(data.getStatus());
        progressHistory.setCreatedBy(employeeId.toString());
        progressHistory.setCreatedDate(new Date());
        progressHistory.setUpdatedBy(employeeId.toString());
        progressHistory.setUpdatedDate(new Date());
        progressHistory.setIsActive("Y");
        progressHistory.setRemark(data.getRemark());
        progressHistory.setOrgId(orgId);
        return progressHistory;
    }
}
