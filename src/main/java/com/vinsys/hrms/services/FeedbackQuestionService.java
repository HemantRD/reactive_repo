package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSFeedbackQuestionDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOFeedbackQuestion;
import com.vinsys.hrms.entity.FeedbackQuestion;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/feedBackQuestion")

public class FeedbackQuestionService {

	@Autowired
	IHRMSFeedbackQuestionDAO feedbackQuestionDAO;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createFeedBackQuestion(@RequestBody VOFeedbackQuestion voFeedbackQuestion) {

		FeedbackQuestion feedbackQuestionEntity;
		Organization organizationEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voFeedbackQuestion)
					&& !HRMSHelper.isNullOrEmpty(voFeedbackQuestion.getOrganization().getId())) {

				feedbackQuestionEntity = feedbackQuestionDAO.findById(voFeedbackQuestion.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(feedbackQuestionEntity)) {
					/* update */
					feedbackQuestionEntity = HRMSRequestTranslator
							.translateToFeedbackQuestionEntity(feedbackQuestionEntity, voFeedbackQuestion);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					feedbackQuestionEntity = new FeedbackQuestion();
					organizationEntity = organizationDAO.findById(voFeedbackQuestion.getOrganization().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(organizationEntity)) {
						feedbackQuestionEntity.setOrgId(organizationEntity.getId());
						feedbackQuestionEntity = HRMSRequestTranslator
								.translateToFeedbackQuestionEntity(feedbackQuestionEntity, voFeedbackQuestion);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				feedbackQuestionDAO.save(feedbackQuestionEntity);
				return HRMSHelper.sendSuccessResponse(resultMesage, IHRMSConstants.successCode);
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllFeedbackQuestionsAndOptions(@PathVariable("id") long orgId) {

		List<FeedbackQuestion> feedbackQuestionEntityList = new ArrayList<>();
		List<Object> voFeedbackQuestionList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			// masterBranchEntityList = masterBranchDAO.findAll();
			feedbackQuestionEntityList = feedbackQuestionDAO.findAllFeedbackQuestionAndOptionByOrgIdCustomQuery(orgId,
					IHRMSConstants.isActive/* , IHRMSConstants.isActive */);
			if (!HRMSHelper.isNullOrEmpty(feedbackQuestionEntityList)) {
				voFeedbackQuestionList = HRMSResponseTranslator
						.transalteToFeedbackQuestionVO(feedbackQuestionEntityList, voFeedbackQuestionList);
				hrmsListResponseObject.setListResponse((List<Object>) voFeedbackQuestionList);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				// return HRMSHelper.createJsonString(voMasterBranchList);
				return HRMSHelper.createJsonString(hrmsListResponseObject);
			} else {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
						IHRMSConstants.DataNotFoundCode);
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteFeedbackQuestion(@RequestBody VOFeedbackQuestion voFeedbackQuestion) {

		try {
			FeedbackQuestion feedbackQuestionEntity = new FeedbackQuestion();
			if (!HRMSHelper.isNullOrEmpty(voFeedbackQuestion)) {
				// masterBranchDAO.delete(branchId);
				feedbackQuestionEntity = feedbackQuestionDAO.findById(voFeedbackQuestion.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(feedbackQuestionEntity)) {
					feedbackQuestionEntity.setIsActive(IHRMSConstants.isNotActive);
					feedbackQuestionEntity.setUpdatedBy(voFeedbackQuestion.getUpdatedBy());
					feedbackQuestionDAO.save(feedbackQuestionEntity);
				} else {
					return HRMSHelper.sendSuccessResponse(IHRMSConstants.deletedsuccessMessage,
							IHRMSConstants.successCode);
				}

			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

}
