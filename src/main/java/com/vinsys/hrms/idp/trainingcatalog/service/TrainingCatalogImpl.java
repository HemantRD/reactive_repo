package com.vinsys.hrms.idp.trainingcatalog.service;

import com.vinsys.hrms.dao.idp.CompetencySubTypesDAO;
import com.vinsys.hrms.dao.idp.CompetencyTypesDAO;
import com.vinsys.hrms.dao.idp.TrainingBudgetDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.idp.TrainingBudget;
import com.vinsys.hrms.entity.idp.TrainingCatalog;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.trainingcatalog.util.TrainingCatalogHelper;
import com.vinsys.hrms.idp.trainingcatalog.validator.TrainingCatalogValidator;
import com.vinsys.hrms.idp.trainingcatalog.vo.*;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.ResponseCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingCatalogImpl implements ITrainingCatalogService {

    private final BackendProperties props;
    private final TrainingCatalogValidator validator;
    private final TrainingCatalogDAO trainingCatalogDAO;
    private final CompetencyTypesDAO competencyTypesDAO;
    private final CompetencySubTypesDAO competencySubTypesDAO;
    private final TrainingBudgetDAO trainingBudgetDAO;

    private final TrainingCatalogHelper helper;


    public TrainingCatalogImpl(final BackendProperties props, final TrainingCatalogValidator validator,
                               final TrainingCatalogDAO trainingCatalogDAO, final CompetencyTypesDAO competencyTypesDAO,
                               final CompetencySubTypesDAO competencySubTypesDAO, final TrainingBudgetDAO trainingBudgetDAO,
                               final TrainingCatalogHelper helper) {
        this.props = props;
        this.validator = validator;
        this.trainingCatalogDAO = trainingCatalogDAO;
        this.competencyTypesDAO = competencyTypesDAO;
        this.competencySubTypesDAO = competencySubTypesDAO;
        this.trainingBudgetDAO = trainingBudgetDAO;
        this.helper = helper;
    }

    @Transactional
    public HRMSBaseResponse<String> addUpdateTrainingCatalog(TrainingCatalogVo trainingCatalogVo) throws HRMSException {
        validator.validateAddUpdate(trainingCatalogVo);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        TrainingCatalog catalog;
        if (trainingCatalogVo.getId() == null) {
            catalog = trainingCatalogDAO.save(helper.populateTrainingCatalog(trainingCatalogVo, null));
        } else {
            TrainingCatalog dbCatalog = trainingCatalogDAO.getById(trainingCatalogVo.getId());
            catalog = trainingCatalogDAO.saveAndFlush(helper.populateTrainingCatalog(trainingCatalogVo, dbCatalog));
        }
        helper.saveKeywords(trainingCatalogVo.getKeywords(), catalog.getId());
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<TrainingCatalogVo> getById(Long id) throws HRMSException {
        validator.validateGetById(id);
        HRMSBaseResponse<TrainingCatalogVo> response = new HRMSBaseResponse<>();
        TrainingCatalog dbCatalog = trainingCatalogDAO.getById(id);
        response.setResponseBody(helper.populateTrainingCatalogVo(dbCatalog));
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<List<TrainingCatalogVo>> getAllTrainingCatalogs(String keyword, Pageable pageable) throws HRMSException {
        Page<TrainingCatalogVo> trainingCatalogList = trainingCatalogDAO.findTrainingCatalogsByPage(keyword, pageable);
        return helper.getResponse(trainingCatalogList.getContent(), trainingCatalogList.getTotalElements());
    }

    @Override
    public HRMSBaseResponse<List<CompetencyDDLVo>> getCompetencyTypes() {
        HRMSBaseResponse<List<CompetencyDDLVo>> response = new HRMSBaseResponse<>();
        response.setResponseBody(competencyTypesDAO.getCompetencyTypes());
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<List<CompetencyDDLVo>> getCompetencySubTypes(Integer id) {
        HRMSBaseResponse<List<CompetencyDDLVo>> response = new HRMSBaseResponse<>();
        response.setResponseBody(competencySubTypesDAO.getCompetencyTypes(id));
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<List<SearchTopicsVo>> searchTopics(SearchTopicReq request, String keyword, Pageable pageable) throws HRMSException {
        validator.searchTopics(request);
        List<String> allKeywords = trainingCatalogDAO.getDistinctKeywords();
        String linePayload = request.getLine().toLowerCase();
        List<String> dbWords = new ArrayList<>();
        for (String word : allKeywords) {
            if (linePayload.contains(word)) {
                dbWords.add(word);
            }
        }
        Page<SearchTopicsVo> trainingCatalogList = trainingCatalogDAO.getSearchTrainingTopics(keyword,
                pageable, dbWords);
        HRMSBaseResponse<List<SearchTopicsVo>> response = new HRMSBaseResponse<>();
        response.setResponseBody(trainingCatalogList.getContent().stream()
                .sorted(Comparator.comparingLong(SearchTopicsVo::getTotalCount).reversed()).collect(Collectors.toList()));
        response.setResponseCode(1200);
        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
        response.setApplicationVersion(props.getApp_version());
        response.setTotalRecord(trainingCatalogList.getTotalElements());
        return response;
    }

    @Transactional
    public HRMSBaseResponse<String> addUpdateTrainingBudget(TrainingBudgetVo trainingBudgetVo) throws HRMSException {
        validator.validateAddUpdateBudget(trainingBudgetVo);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        if (trainingBudgetVo.getId() == null) {
            trainingBudgetDAO.save(helper.populateTrainingBudget(trainingBudgetVo, null));
        } else {
            TrainingBudget dbBudget = trainingBudgetDAO.getById(trainingBudgetVo.getId());
            trainingBudgetDAO.saveAndFlush(helper.populateTrainingBudget(trainingBudgetVo, dbBudget));
        }
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    public HRMSBaseResponse<List<TrainingBudgetVo>> getAllTrainingBudgets(Integer year, Pageable pageable) {
        Page<TrainingBudgetVo> trainingCatalogList = trainingBudgetDAO.findTrainingBudgetsByPage(year, pageable);
        return helper.getResponse(trainingCatalogList.getContent(), trainingCatalogList.getTotalElements());
    }
}
