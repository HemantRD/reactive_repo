package com.vinsys.hrms.idp.t_types.service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.t_types.vo.TTypesStatus;
import com.vinsys.hrms.idp.t_types.vo.TTypesVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TTypesService {

    HRMSBaseResponse<String> addUpdateTrainingType(TTypesVo tTypesVo) throws HRMSException;

    HRMSBaseResponse<String> updateTrainingTypeStatus(TTypesStatus tTypesStatus) throws HRMSException;

    HRMSBaseResponse<TTypesVo> getById(Long id) throws HRMSException;

    HRMSBaseResponse<List<TTypesVo>> getAllTrainingTypes(String keyword, Pageable pageable) throws HRMSException;

    byte[] getAllTrainingTypesExcel(String keyword) throws HRMSException;

}
