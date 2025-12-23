package com.vinsys.hrms.idp.t_types.validator;

import com.vinsys.hrms.dao.idp.TTypesDAO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.t_types.vo.TTypesStatus;
import com.vinsys.hrms.idp.t_types.vo.TTypesVo;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;
import org.springframework.stereotype.Component;

@Component
public class TTypesValidator {

    private final TTypesDAO tTypesDAO;

    public TTypesValidator(final TTypesDAO tTypesDAO) {
        this.tTypesDAO = tTypesDAO;
    }

    public void validateAddUpdate(TTypesVo tClassificationsVo) throws HRMSException {
        if (tClassificationsVo.getId() != null && !tTypesDAO.existsById(tClassificationsVo.getId())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id");
        }
        if (HRMSHelper.isNullOrEmpty(tClassificationsVo.getName())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Name");
        }
        if (tTypesDAO.nameAlreadyExist(tClassificationsVo.getName(), tClassificationsVo.getId())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1546) + " For Name");
        }
    }

    public void validateGetById(Long id) throws HRMSException {
        if (!tTypesDAO.existsById(id)) {
            throw new HRMSException(1501, "Not Found");
        }
    }

    public void updateTrainingTypeStatus(TTypesStatus tTypesStatus) throws HRMSException {
        if (!tTypesDAO.existsById(tTypesStatus.getId())) {
            throw new HRMSException(1501, "Not Found");
        }
        if (HRMSHelper.isNullOrEmpty(tTypesStatus.getStatus())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " status");
        }
    }
}
