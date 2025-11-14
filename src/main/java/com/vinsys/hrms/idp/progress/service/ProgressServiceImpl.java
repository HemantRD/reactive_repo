package com.vinsys.hrms.idp.progress.service;

import com.vinsys.hrms.constants.EResponse;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.idp.IdpProgressHistoryDAO;
import com.vinsys.hrms.dao.idp.IdpTrainingDetailsDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.progress.data.DataIdpIdpDetails;
import com.vinsys.hrms.idp.progress.validator.ProgressValidator;
import com.vinsys.hrms.idp.progress.vo.BulkExcelProgressVo;
import com.vinsys.hrms.util.ResponseCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    public HRMSBaseResponse<String> uploadBulkProgressExcel(MultipartFile excelFile) throws HRMSException, Exception {
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        List<BulkExcelProgressVo> bulkExcelProgressVoList = validator.validateExcelFileDataAndGetList(excelFile);

        for (BulkExcelProgressVo data : bulkExcelProgressVoList) {
            Long trainingId = trainingCatalogDAO.getIdByTrainingCode(data.getTrainingCode());
            Employee employee = employeeDAO.findByofficialEmailId(data.getMemberEmail());
            DataIdpIdpDetails dataIdpIdpDetails = idpTrainingDetailsDAO.getIdpDetailsData(trainingId, employee.getId());
            idpProgressHistoryDAO.markOldRecordsInActive(dataIdpIdpDetails.getIdpId(), dataIdpIdpDetails.getIdpDetailsId(), employee.getId());
        }
        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
        response.setResponseCode(EResponse.SUCCESS.getCode());
        response.setResponseMessage(EResponse.SUCCESS.getMessage());
        return response;
    }
}
