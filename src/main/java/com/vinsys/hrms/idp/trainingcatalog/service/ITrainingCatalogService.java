package com.vinsys.hrms.idp.trainingcatalog.service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.trainingcatalog.vo.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITrainingCatalogService {

    HRMSBaseResponse<String> addUpdateTrainingCatalog(TrainingCatalogVo trainingCatalogVo) throws HRMSException;

    HRMSBaseResponse<TrainingCatalogVo> getById(Long id) throws HRMSException;

    HRMSBaseResponse<List<TrainingCatalogVo>> getAllTrainingCatalogs(TrainingCatalogListingReq request, Pageable pageable) throws HRMSException;

    byte[] getAllTrainingCatalogsExcel(TrainingCatalogListingReq request, Pageable pageable) throws HRMSException;

    HRMSBaseResponse<List<CompetencyDDLVo>> getCompetencyTypes();

    HRMSBaseResponse<List<CompetencyDDLVo>> getCompetencySubTypes(Integer id);

    HRMSBaseResponse<List<SearchTopicsVo>> searchTopics(SearchTopicReq request, String keyword, Pageable pageable) throws HRMSException;

    HRMSBaseResponse<String> addUpdateTrainingBudget(TrainingBudgetVo trainingBudgetVo) throws HRMSException;

    HRMSBaseResponse<List<TrainingBudgetVo>> getAllTrainingBudgets(Integer year, Pageable pageable) throws HRMSException;

    HRMSBaseResponse<String> updateTrainingStatus(TrainingCatalogStatus trainingCatalogStatus) throws HRMSException;

}
