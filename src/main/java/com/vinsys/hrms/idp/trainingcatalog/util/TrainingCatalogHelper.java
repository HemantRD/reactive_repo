package com.vinsys.hrms.idp.trainingcatalog.util;

import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogKeywordsDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.idp.TrainingBudget;
import com.vinsys.hrms.entity.idp.TrainingCatalog;
import com.vinsys.hrms.entity.idp.TrainingCatalogKeywords;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingBudgetVo;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogListingReq;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogVo;
import com.vinsys.hrms.master.dao.IMasterYearDAO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.ResponseCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TrainingCatalogHelper {

    private final BackendProperties props;
    private final TrainingCatalogKeywordsDAO trainingCatalogKeywordsDAO;
    private final IMasterYearDAO iMasterYearDAO;
    private final TrainingCatalogDAO trainingCatalogDAO;


    public TrainingCatalogHelper(final BackendProperties props,
                                 final TrainingCatalogKeywordsDAO trainingCatalogKeywordsDAO,
                                 final IMasterYearDAO iMasterYearDAO, final TrainingCatalogDAO trainingCatalogDAO) {
        this.props = props;
        this.trainingCatalogKeywordsDAO = trainingCatalogKeywordsDAO;
        this.iMasterYearDAO = iMasterYearDAO;
        this.trainingCatalogDAO = trainingCatalogDAO;
    }

    public TrainingCatalogVo populateTrainingCatalogVo(TrainingCatalog dbCatalog) {
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
        trainingCatalogVo.setDurationInHours(dbCatalog.getDurationInHours());
        trainingCatalogVo.setStatus(dbCatalog.getIsActive().equalsIgnoreCase("Y"));
        trainingCatalogVo.setRemark(dbCatalog.getRemark());
        trainingCatalogVo.setKeywords(trainingCatalogKeywordsDAO.findAllByTrainingId(dbCatalog.getId()));
        return trainingCatalogVo;
    }

    public <T> HRMSBaseResponse<List<T>> getResponse(List<T> traniningCatalogList,
                                                     long totalRecord) {
        HRMSBaseResponse<List<T>> response = new HRMSBaseResponse<>();
        response.setResponseBody(traniningCatalogList);
        response.setResponseCode(1200);
        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
        response.setApplicationVersion(props.getApp_version());
        response.setTotalRecord(totalRecord);
        return response;
    }

    public TrainingCatalog populateTrainingCatalog(TrainingCatalogVo trainingCatalogVo, TrainingCatalog dbCatalog) {
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
        catalog.setDurationInHours(trainingCatalogVo.getDurationInHours());
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
            trainingCatalogKeywords.setKeyword(keyword.toLowerCase());
            trainingCatalogKeywords.setRecordStatus(false);
            list.add(trainingCatalogKeywords);
        }
        trainingCatalogKeywordsDAO.saveAll(list);
    }

    public TrainingBudget populateTrainingBudget(TrainingBudgetVo trainingBudgetVo, TrainingBudget dbBudget) {
        TrainingBudget budget = dbBudget;
        Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
        if (budget == null) {
            budget = new TrainingBudget();
            budget.setCreatedBy(employeeId.toString());
            budget.setCreatedDate(new Date());
        }
        budget.setBudgetAmount(trainingBudgetVo.getBudgetAmount());
        budget.setYear(iMasterYearDAO.findByYear(trainingBudgetVo.getYear()));
        budget.setCurrencySymbol(trainingBudgetVo.getCurrencySymbol());
        budget.setUpdatedBy(employeeId.toString());
        budget.setUpdatedDate(new Date());
        budget.setIsActive("Y");
        budget.setRemark(trainingBudgetVo.getRemark());
        budget.setOrgId(orgId);
        return budget;
    }

}
