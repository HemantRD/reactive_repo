package com.vinsys.hrms.idp.t_classifications.service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.t_classifications.vo.TClassificationsStatus;
import com.vinsys.hrms.idp.t_classifications.vo.TClassificationsVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TClassificationsService {

    HRMSBaseResponse<String> addUpdateTrainingClassification(TClassificationsVo tClassificationsVo) throws HRMSException;

    HRMSBaseResponse<String> updateTrainingClassificationStatus(TClassificationsStatus tClassificationsStatus) throws HRMSException;

    HRMSBaseResponse<TClassificationsVo> getById(Long id) throws HRMSException;

    HRMSBaseResponse<List<TClassificationsVo>> getAllTrainingClassifications(String keyword, Pageable pageable) throws HRMSException;

    byte[] getAllTrainingClassificationsExcel(String keyword) throws HRMSException;

}
