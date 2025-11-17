package com.vinsys.hrms.idp.trainingcatalog.validator;

import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.trainingcatalog.vo.SearchTopicReq;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogVo;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TrainingCatalogValidator {

    private final TrainingCatalogDAO trainingCatalogDAO;

    public TrainingCatalogValidator(final TrainingCatalogDAO trainingCatalogDAO) {
        this.trainingCatalogDAO = trainingCatalogDAO;
    }

    public void validateAddUpdate(TrainingCatalogVo trainingCatalogVo) throws HRMSException {
        if (trainingCatalogVo.getId() != null && !trainingCatalogDAO.existsById(trainingCatalogVo.getId())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id");
        }
        if (HRMSHelper.isNullOrEmpty(trainingCatalogVo.getTrainingCode())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Training Code");
        }
        if (trainingCatalogDAO.trainingCodeAlreadyExist(trainingCatalogVo.getTrainingCode(), trainingCatalogVo.getId())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1546) + " For Training Code");
        }
        if (HRMSHelper.isNullOrEmpty(trainingCatalogVo.getTopicName())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Topic Name");
        }
        if (trainingCatalogVo.getCompetencyTypeId() == null || trainingCatalogVo.getCompetencyTypeId() < 0) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " competencyTypeId");
        }
        if (trainingCatalogVo.getCompetencySubTypeId() == null || trainingCatalogVo.getCompetencySubTypeId() < 0) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " competencySubTypeId");
        }
        if (HRMSHelper.isNullOrEmpty(trainingCatalogVo.getInternal())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " isInternal");
        }
        if (trainingCatalogVo.getCostPerPersonIndividual() == null || trainingCatalogVo.getCostPerPersonIndividual() < 0) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " costPerPersonIndividual");
        }
        if (trainingCatalogVo.getCostPerPersonGroup() == null || trainingCatalogVo.getCostPerPersonGroup() < 0) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " costPerPersonGroup");
        }
        if (trainingCatalogVo.getCostPerGroup() == null || trainingCatalogVo.getCostPerGroup() < 0) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " costPerGroup");
        }
        if (trainingCatalogVo.getMinPersonInGroup() == null || trainingCatalogVo.getMinPersonInGroup() < 0) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " minPersonInGroup");
        }
        if (trainingCatalogVo.getMaxPersonInGroup() == null || trainingCatalogVo.getMaxPersonInGroup() < 0) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " maxPersonInGroup");
        }
        if (HRMSHelper.isNullOrEmpty(trainingCatalogVo.getCertificationCourse())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " isCertificationCourse");
        }
        if (trainingCatalogVo.getPriority() == null || trainingCatalogVo.getPriority() < 0) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " priority");
        }
        if (trainingCatalogVo.getStatus() == null) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " status");
        }
        if (CollectionUtils.isEmpty(trainingCatalogVo.getKeywords())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " keywords");
        }

    }

    public void validateGetById(Long id) throws HRMSException {
        if (!trainingCatalogDAO.existsById(id)) {
            throw new HRMSException(1501, "Not Found");
        }
    }

    public void searchTopics(SearchTopicReq request) throws HRMSException {
        if (HRMSHelper.isNullOrEmpty(request.getLine())) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " criteria");
        }
    }
}
