package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateActivityActionDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateActivityActionTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterOnboardActionReasonDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateActivityActionDetail;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateActivityActionDetail;
import com.vinsys.hrms.entity.MasterCandidateOnboardActionReason;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/candidateActivityAction")

public class CandidateActivityActionService {

	public static Logger logger = LoggerFactory.getLogger(CandidateActivityActionService.class);

	@Autowired
	IHRMSCandidateActivityActionDAO candidateActivityActionDAO;

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSMasterCandidateActivityActionTypeDAO masterCandidateActivityActionTypeDAO;

	@Autowired
	IHRMSMasterOnboardActionReasonDAO masterOnboardActionReasonDAO;

	@RequestMapping(value = "/submitCandActAction", method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String submitCandidateActivityActionDetails(
			@RequestBody VOCandidateActivityActionDetail voCandidateActivityAction) {
		CandidateActivityActionDetail candidateActivityActionEntity;
		Candidate candidateEntity;
		MasterCandidateOnboardActionReason masterCandidateOnboardActionReasonEntity;
		String resultMesage = "";
		try {
			if (!HRMSHelper.isNullOrEmpty(voCandidateActivityAction)
					&& !HRMSHelper.isNullOrEmpty(voCandidateActivityAction.getCandidate().getId()) && !HRMSHelper
							.isNullOrEmpty(voCandidateActivityAction.getMasterCandidateOnboardActionReason().getId())) {
				candidateActivityActionEntity = candidateActivityActionDAO.findById(voCandidateActivityAction.getId())
						.get();
				masterCandidateOnboardActionReasonEntity = masterOnboardActionReasonDAO
						.findById(voCandidateActivityAction.getMasterCandidateOnboardActionReason().getId()).get();
				if (!HRMSHelper.isNullOrEmpty(candidateActivityActionEntity)
						&& !HRMSHelper.isNullOrEmpty(masterCandidateOnboardActionReasonEntity)) {
					// update
					candidateActivityActionEntity = HRMSRequestTranslator.translateToCandidateActivityActionEntity(
							candidateActivityActionEntity, voCandidateActivityAction);
					candidateActivityActionEntity
							.setMasterCandidateOnboardActionReason(masterCandidateOnboardActionReasonEntity);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					// insert
					candidateEntity = new Candidate();
					candidateEntity = candidateDAO.findById(voCandidateActivityAction.getCandidate().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(candidateEntity)
							&& !HRMSHelper.isNullOrEmpty(masterCandidateOnboardActionReasonEntity)) {
						candidateActivityActionEntity = new CandidateActivityActionDetail();
						candidateActivityActionEntity.setCandidate(candidateEntity);
						candidateActivityActionEntity
								.setMasterCandidateOnboardActionReason(masterCandidateOnboardActionReasonEntity);
						candidateActivityActionEntity = HRMSRequestTranslator.translateToCandidateActivityActionEntity(
								candidateActivityActionEntity, voCandidateActivityAction);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					if (candidateActivityActionEntity.getTypeOfAction().equals(IHRMSConstants.OnboardActionType_CANCEL))
						resultMesage = IHRMSConstants.cancelledsuccessMessage;
					else
						resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				candidateActivityActionEntity = candidateActivityActionDAO.save(candidateActivityActionEntity);
				if (!HRMSHelper.isNullOrEmpty(candidateActivityActionEntity)
						&& !HRMSHelper.isNullOrEmpty(candidateActivityActionEntity.getTypeOfAction())) {
					if (candidateActivityActionEntity.getTypeOfAction()
							.equals(IHRMSConstants.OnboardActionType_CANCEL)) {
						// updating candidate status to CANCEL ONBOARD
						candidateEntity = candidateActivityActionEntity.getCandidate();
						candidateEntity.setCandidateStatus(IHRMSConstants.CandidateStatus_CANCEL_ONBOARD);
						candidateEntity.setUpdatedDate(new Date());
						candidateDAO.save(candidateEntity);
					} else if (candidateActivityActionEntity.getTypeOfAction()
							.equals(IHRMSConstants.OnboardActionType_ONBOARD)) {
						// updating candidate status to ONBOARD
						candidateEntity = candidateActivityActionEntity.getCandidate();
						candidateEntity.setCandidateStatus(IHRMSConstants.CandidateStatus_ONBOARD);
						candidateEntity.setUpdatedDate(new Date());
						candidateDAO.save(candidateEntity);
					}
				}
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

	@RequestMapping(method = RequestMethod.GET, value = "/{candidateId}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getCandidateActivityActionDetails(@PathVariable("candidateId") long candidateId) {

		List<CandidateActivityActionDetail> candidateActivityActionDetails = new ArrayList<>();
		List<Object> voMasterCandidateActivityActionDetailList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			if (HRMSHelper.isNullOrEmpty(candidateId)) {
				logger.info("in getCandidateActivityActionDetails() : Candidate ID not sent");
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			} else {
				logger.info("in getCandidateActivityActionDetails() : Find by query block");
				candidateActivityActionDetails = candidateActivityActionDAO
						.findActivityActionDetailByCandidate(candidateId);
				if (!HRMSHelper.isNullOrEmpty(candidateActivityActionDetails)) {
					voMasterCandidateActivityActionDetailList = HRMSResponseTranslator
							.translateToCandidateActivityActionDetailVO(candidateActivityActionDetails,
									voMasterCandidateActivityActionDetailList);
					hrmsListResponseObject.setListResponse((List<Object>) voMasterCandidateActivityActionDetailList);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
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
