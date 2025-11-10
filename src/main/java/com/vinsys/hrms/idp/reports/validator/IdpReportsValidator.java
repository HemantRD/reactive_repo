package com.vinsys.hrms.idp.reports.validator;

import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.utils.IdpEnums;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;
import org.springframework.stereotype.Component;

@Component
public class IdpReportsValidator {

    private final TrainingCatalogDAO trainingCatalogDAO;

    public IdpReportsValidator(final TrainingCatalogDAO trainingCatalogDAO) {
        this.trainingCatalogDAO = trainingCatalogDAO;
    }

    public void validateTopTrainingCoursesExcel(String viewType) throws HRMSException {
        if (HRMSHelper.isNullOrEmpty(viewType) || !viewType.equals("excel")) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " viewType");
        }
    }

    public void validateCostReport(String viewType, String reportType) throws HRMSException {
        if (!HRMSHelper.isNullOrEmpty(viewType) && !viewType.equals("excel")) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " viewType");
        }
        if (HRMSHelper.isNullOrEmpty(reportType) ||
                (!reportType.equals(IdpEnums.ReportType.SUMMARY.getKey()) &&
                        (!reportType.equals(IdpEnums.ReportType.DETAILED.getKey())))) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " reportType");
        }
    }
}
