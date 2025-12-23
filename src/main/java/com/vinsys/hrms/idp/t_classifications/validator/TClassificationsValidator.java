package com.vinsys.hrms.idp.t_classifications.validator;

import com.vinsys.hrms.dao.idp.TClassificationsDAO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.t_classifications.vo.TClassificationsStatus;
import com.vinsys.hrms.idp.t_classifications.vo.TClassificationsVo;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogListingReq;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;
import org.springframework.stereotype.Component;

@Component
public class TClassificationsValidator {

    private final TClassificationsDAO tClassificationsDAO;

    public TClassificationsValidator(final TClassificationsDAO tClassificationsDAO) {
        this.tClassificationsDAO = tClassificationsDAO;
    }

    public void validateAddUpdate(TClassificationsVo tClassificationsVo) throws HRMSException {
        if (tClassificationsVo.getId() != null && !tClassificationsDAO.existsById(tClassificationsVo.getId())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id");
        }
        if (HRMSHelper.isNullOrEmpty(tClassificationsVo.getName())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Name");
        }
        if (tClassificationsDAO.nameAlreadyExist(tClassificationsVo.getName(), tClassificationsVo.getId())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1546) + " For Name");
        }
    }

    public void validateGetById(Long id) throws HRMSException {
        if (!tClassificationsDAO.existsById(id)) {
            throw new HRMSException(1501, "Not Found");
        }
    }

    public void updateTrainingClassificationStatus(TClassificationsStatus tClassificationsStatus) throws HRMSException {
        if (!tClassificationsDAO.existsById(tClassificationsStatus.getId())) {
            throw new HRMSException(1501, "Not Found");
        }
        if (HRMSHelper.isNullOrEmpty(tClassificationsStatus.getStatus())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " status");
        }
    }
}
