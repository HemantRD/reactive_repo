package com.vinsys.hrms.idp.reports.validator;

import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.exception.HRMSException;
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
}
