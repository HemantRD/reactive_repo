package com.vinsys.hrms.idp.trainingcatalog.service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.trainingcatalog.vo.CompetencyDDLVo;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITrainingCatalogService {

    HRMSBaseResponse<String> addUpdateTrainingCatalog(TrainingCatalogVo trainingCatalogVo) throws HRMSException;

    HRMSBaseResponse<TrainingCatalogVo> getById(Long id) throws HRMSException;

    HRMSBaseResponse<List<TrainingCatalogVo>> getAllTrainingCatalogs(String keyword, Pageable pageable) throws HRMSException;

    HRMSBaseResponse<List<CompetencyDDLVo>> getCompetencyTypes();

    HRMSBaseResponse<List<CompetencyDDLVo>> getCompetencySubTypes(Integer id);

}
