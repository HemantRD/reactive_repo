package com.vinsys.hrms.idp.trainingcatalog.service;

import com.vinsys.hrms.dao.idp.CompetencySubTypesDAO;
import com.vinsys.hrms.dao.idp.CompetencyTypesDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogKeywordsDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.idp.TrainingCatalog;
import com.vinsys.hrms.entity.idp.TrainingCatalogKeywords;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.trainingcatalog.validator.TrainingCatalogValidator;
import com.vinsys.hrms.idp.trainingcatalog.vo.CompetencyDDLVo;
import com.vinsys.hrms.idp.trainingcatalog.vo.SearchTopicReq;
import com.vinsys.hrms.idp.trainingcatalog.vo.SearchTopicsVo;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogVo;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.ResponseCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TrainingCatalogImpl implements ITrainingCatalogService {

    private final BackendProperties props;
    private final TrainingCatalogValidator validator;
    private final TrainingCatalogDAO trainingCatalogDAO;
    private final TrainingCatalogKeywordsDAO trainingCatalogKeywordsDAO;
    private final CompetencyTypesDAO competencyTypesDAO;
    private final CompetencySubTypesDAO competencySubTypesDAO;


    public TrainingCatalogImpl(final BackendProperties props, final TrainingCatalogValidator validator,
                               final TrainingCatalogDAO trainingCatalogDAO, final TrainingCatalogKeywordsDAO trainingCatalogKeywordsDAO,
                               final CompetencyTypesDAO competencyTypesDAO, final CompetencySubTypesDAO competencySubTypesDAO) {
        this.props = props;
        this.validator = validator;
        this.trainingCatalogDAO = trainingCatalogDAO;
        this.trainingCatalogKeywordsDAO = trainingCatalogKeywordsDAO;
        this.competencyTypesDAO = competencyTypesDAO;
        this.competencySubTypesDAO = competencySubTypesDAO;
    }

    @Transactional
    public HRMSBaseResponse<String> addUpdateTrainingCatalog(TrainingCatalogVo trainingCatalogVo) throws HRMSException {
        validator.validateAddUpdate(trainingCatalogVo);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        TrainingCatalog catalog;
        if (trainingCatalogVo.getId() == null) {
            catalog = trainingCatalogDAO.save(populateTrainingCatalog(trainingCatalogVo, null));
        } else {
            TrainingCatalog dbCatalog = trainingCatalogDAO.getById(trainingCatalogVo.getId());
            catalog = trainingCatalogDAO.saveAndFlush(populateTrainingCatalog(trainingCatalogVo, dbCatalog));
        }
        saveKeywords(trainingCatalogVo.getKeywords(), catalog.getId());
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    public HRMSBaseResponse<TrainingCatalogVo> getById(Long id) throws HRMSException {
        validator.validateGetById(id);
        HRMSBaseResponse<TrainingCatalogVo> response = new HRMSBaseResponse<>();
        TrainingCatalog dbCatalog = trainingCatalogDAO.getById(id);
        response.setResponseBody(populateTrainingCatalogVo(dbCatalog));
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    public HRMSBaseResponse<List<TrainingCatalogVo>> getAllTrainingCatalogs(String keyword, Pageable pageable) throws HRMSException {
        Page<TrainingCatalogVo> trainingCatalogList = trainingCatalogDAO.findTrainingCatalogsByPage(keyword, pageable);
        return getResponse(trainingCatalogList.getContent(), trainingCatalogList.getTotalElements());
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
        String[] tokens = request.getLine().split(" ");
        Pageable pageableQuery = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("cnt").descending());
        Page<SearchTopicsVo> trainingCatalogList = trainingCatalogDAO.getSearchTrainingTopics(keyword,
                pageableQuery, Arrays.asList(tokens));
        HRMSBaseResponse<List<SearchTopicsVo>> response = new HRMSBaseResponse<>();
        response.setResponseBody(trainingCatalogList.getContent());
        response.setResponseCode(1200);
        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
        response.setApplicationVersion(props.getApp_version());
        response.setTotalRecord(trainingCatalogList.getTotalElements());
        return response;
    }

    private TrainingCatalog populateTrainingCatalog(TrainingCatalogVo trainingCatalogVo, TrainingCatalog dbCatalog) {
        TrainingCatalog catalog = dbCatalog;
        Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
        if (catalog == null) {
            catalog = new TrainingCatalog();
            catalog.setCreatedBy(employeeId.toString());
            catalog.setCreatedDate(new Date());
        }
        catalog.setTrainingCode(trainingCatalogVo.getTrainingCode());
        catalog.setTopicName(trainingCatalogVo.getTopicName());
        catalog.setCompetencyTypeId(trainingCatalogVo.getCompetencyTypeId());
        catalog.setCompetencySubTypeId(trainingCatalogVo.getCompetencySubTypeId());
        catalog.setInternal(trainingCatalogVo.getInternal());
        catalog.setCostPerPersonIndividual(trainingCatalogVo.getCostPerPersonIndividual());
        catalog.setCostPerPersonGroup(trainingCatalogVo.getCostPerPersonGroup());
        catalog.setCostPerGroup(trainingCatalogVo.getCostPerGroup());
        catalog.setMinPersonInGroup(trainingCatalogVo.getMinPersonInGroup());
        catalog.setMaxPersonInGroup(trainingCatalogVo.getMaxPersonInGroup());
        catalog.setCertificationCourse(trainingCatalogVo.getCertificationCourse());
        catalog.setPriority(trainingCatalogVo.getPriority());
        catalog.setUpdatedBy(employeeId.toString());
        catalog.setUpdatedDate(new Date());
        catalog.setIsActive(trainingCatalogVo.getStatus() ? "Y" : "N");
        catalog.setRemark(trainingCatalogVo.getRemark());
        catalog.setOrgId(orgId);
        return catalog;
    }

    public void saveKeywords(List<String> keywords, Long trainingId) {
        trainingCatalogKeywordsDAO.deleteByTrainingId(trainingId);
        List<TrainingCatalogKeywords> list = new ArrayList<>();
        for (String keyword : keywords) {
            TrainingCatalogKeywords trainingCatalogKeywords = new TrainingCatalogKeywords();
            trainingCatalogKeywords.setTrainingId(trainingId);
            trainingCatalogKeywords.setKeyword(keyword);
            trainingCatalogKeywords.setRecordStatus(false);
            list.add(trainingCatalogKeywords);
        }
        trainingCatalogKeywordsDAO.saveAll(list);
    }

    private TrainingCatalogVo populateTrainingCatalogVo(TrainingCatalog dbCatalog) {
        TrainingCatalogVo trainingCatalogVo = new TrainingCatalogVo();
        trainingCatalogVo.setId(dbCatalog.getId());
        trainingCatalogVo.setTrainingCode(dbCatalog.getTrainingCode());
        trainingCatalogVo.setTopicName(dbCatalog.getTopicName());
        trainingCatalogVo.setCompetencyTypeId(dbCatalog.getCompetencyTypeId());
        trainingCatalogVo.setCompetencySubTypeId(dbCatalog.getCompetencySubTypeId());
        trainingCatalogVo.setInternal(dbCatalog.getInternal());
        trainingCatalogVo.setCostPerPersonIndividual(dbCatalog.getCostPerPersonIndividual());
        trainingCatalogVo.setCostPerPersonGroup(dbCatalog.getCostPerPersonGroup());
        trainingCatalogVo.setCostPerGroup(dbCatalog.getCostPerGroup());
        trainingCatalogVo.setMinPersonInGroup(dbCatalog.getMinPersonInGroup());
        trainingCatalogVo.setMaxPersonInGroup(dbCatalog.getMaxPersonInGroup());
        trainingCatalogVo.setCertificationCourse(dbCatalog.getCertificationCourse());
        trainingCatalogVo.setPriority(dbCatalog.getPriority());
        trainingCatalogVo.setStatus(dbCatalog.getIsActive().equalsIgnoreCase("Y"));
        trainingCatalogVo.setRemark(dbCatalog.getRemark());
        trainingCatalogVo.setKeywords(trainingCatalogKeywordsDAO.findAllByTrainingId(dbCatalog.getId()));
        return trainingCatalogVo;
    }

    private HRMSBaseResponse<List<TrainingCatalogVo>> getResponse(List<TrainingCatalogVo> traniningCatalogList,
                                                                  long totalRecord) {
        HRMSBaseResponse<List<TrainingCatalogVo>> response = new HRMSBaseResponse<>();
        response.setResponseBody(traniningCatalogList);
        response.setResponseCode(1200);
        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
        response.setApplicationVersion(props.getApp_version());
        response.setTotalRecord(totalRecord);
        return response;
    }

}
