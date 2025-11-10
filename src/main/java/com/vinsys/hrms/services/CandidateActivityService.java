package com.vinsys.hrms.services;

import java.text.SimpleDateFormat;
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
import com.vinsys.hrms.dao.IHRMSCandidateActivityDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateActivityActionTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateActivityDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateActivity;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateActivity;
import com.vinsys.hrms.entity.CandidateActivityActionDetail;
import com.vinsys.hrms.entity.MasterCandidateActivity;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/candidateActivity")

public class CandidateActivityService {

	public static Logger logger = LoggerFactory.getLogger(CandidateActivityService.class);

	@Autowired
	IHRMSMasterCandidateActivityDAO masterCandidateActivityDAO;
	@Autowired
	IHRMSCandidateActivityDAO candidateActivityDAO;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSMasterCandidateActivityActionTypeDAO masterCandidateActivityActionTypeDAO;
	@Autowired
	IHRMSCandidateActivityActionDAO candidateActivityActionDAO;

	@RequestMapping(method = RequestMethod.GET, value = "/{employeeId}/{organizationId}/{activityId}/{designationId}/{departmentId}/"
			+ "{candidateId}/{emailId}/{divisionId}/{tab}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getCandidateActivityDetails(@PathVariable("employeeId") long employeeId,
			@PathVariable("organizationId") long organizationId, @PathVariable("activityId") long activityId,
			@PathVariable("designationId") long designationId, @PathVariable("departmentId") long departmentId,
			@PathVariable("candidateId") long candidateId, @PathVariable("emailId") String emailIdVar,
			@PathVariable("divisionId") long divisionId, @PathVariable("tab") String tab) {

		// List<CandidateActivity> candidateActivities = new ArrayList<>();
		List<Candidate> candidates = new ArrayList<Candidate>();
		List<Object> voCandidateActivityList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			if (HRMSHelper.isLongZero(employeeId) && HRMSHelper.isLongZero(departmentId)
					&& HRMSHelper.isLongZero(activityId) && HRMSHelper.isLongZero(designationId)
					&& HRMSHelper.isLongZero(candidateId) && HRMSHelper.isNullOrEmpty(emailIdVar)
					&& !HRMSHelper.isNullOrEmpty(tab)) {
				logger.info("in getCandidateActivityDetails() : Find activity details of candidate of an organization");
				/*
				 * next if gets candidate for HRWorkspace tab
				 */
				if (tab.equalsIgnoreCase("HRW")) {
					candidates = candidateActivityDAO.findAllPendingCandidateActivity(organizationId,
							IHRMSConstants.CandidateStatus_INITIATED, divisionId, IHRMSConstants.isActive,
							IHRMSConstants.CandidateStatus_INITIATED);
				}
				/*
				 * next if gets candidate for verify candidate document tab
				 */
				else if (tab.equalsIgnoreCase("VCD")) {
					candidates = candidateActivityDAO.findAllPendingCandidateActivity(organizationId,
							IHRMSConstants.CandidateStatus_INITIATED, divisionId, IHRMSConstants.isActive,
							IHRMSConstants.CandidateStatus_ONBOARD);
				} else {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}

			} else {
				logger.info("in getCandidateActivityDetails() : Find activity detail by filter");
				String emailId = "%" + emailIdVar + "%";
				candidates = candidateActivityDAO.findCandidateActivityWithFilter(organizationId, activityId,
						designationId, departmentId, candidateId, emailId, divisionId, IHRMSConstants.isActive);
			}

			if (!HRMSHelper.isNullOrEmpty(candidates)) {
				voCandidateActivityList = HRMSResponseTranslator.translateToCandidateWithActivityDetailsVo(candidates,
						voCandidateActivityList);
				hrmsListResponseObject.setListResponse((List<Object>) voCandidateActivityList);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(hrmsListResponseObject);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String submitCandidateActivityDetails(@RequestBody VOCandidateActivity voCandidateActivity) {

		CandidateActivity candidateActivityEntity;
		Candidate candidateEntity;
		MasterCandidateActivity masterCandidateActivityEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voCandidateActivity)
					&& !HRMSHelper.isNullOrEmpty(voCandidateActivity.getCandidate().getId())
					&& !HRMSHelper.isNullOrEmpty(voCandidateActivity.getMasterCandidateActivity().getId())) {

				candidateActivityEntity = candidateActivityDAO.findById(voCandidateActivity.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(candidateActivityEntity)) {
					/* update */
					candidateActivityEntity = HRMSRequestTranslator
							.translateToCandidateActivityEntity(candidateActivityEntity, voCandidateActivity);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					candidateActivityEntity = new CandidateActivity();
					candidateEntity = candidateDAO.findById(voCandidateActivity.getCandidate().getId()).get();
					masterCandidateActivityEntity = masterCandidateActivityDAO
							.findById(voCandidateActivity.getMasterCandidateActivity().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(candidateEntity)
							&& !HRMSHelper.isNullOrEmpty(masterCandidateActivityEntity)) {
						candidateActivityEntity.setCandidate(candidateEntity);
						candidateActivityEntity.setMasterCandidateActivity(masterCandidateActivityEntity);
						candidateActivityEntity = HRMSRequestTranslator
								.translateToCandidateActivityEntity(candidateActivityEntity, voCandidateActivity);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				candidateActivityDAO.save(candidateActivityEntity);
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

	@RequestMapping(method = RequestMethod.GET, value = "getOnboardCandidate/{organizationId}/{candidateId}/{fromDate}/{toDate}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getOnboardCandidateList(@PathVariable("organizationId") long organizationId,
			@PathVariable("candidateId") long candidateId, @PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate) {

		// List<CandidateActivity> candidateActivities = new ArrayList<>();
		List<CandidateActivityActionDetail> candidateActivityActionDetailList = new ArrayList<CandidateActivityActionDetail>();
		List<Candidate> candidates = new ArrayList<Candidate>();
		List<Object> voCandidateOnboardList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			if (HRMSHelper.isLongZero(candidateId) && HRMSHelper.isNullOrEmpty(fromDate)
					&& HRMSHelper.isNullOrEmpty(toDate)) {
				logger.info("in getOnboardCandidateList() : Find onboard candidate list");
				// candidates =
				// candidateActivityDAO.findAllOnboardCandidateList(organizationId);
				candidateActivityActionDetailList = candidateActivityActionDAO.findByTypeOfAction("ONBOARD",
						organizationId);
			} else {
				logger.info("in getOnboardCandidateList() : Find onboard candidate list by filter");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(fromDate)) {
					Date fromDateObj = sdf.parse(fromDate);
					Date toDateObj = sdf.parse(toDate);
					candidates = candidateActivityDAO.findOnboardCandidateListWithFilter(organizationId, candidateId,
							fromDateObj, toDateObj);
					candidateActivityActionDetailList = candidateActivityActionDAO.findByTypeOfActionWithFilter(
							"ONBOARD", organizationId, candidateId, fromDateObj, toDateObj);
				}
			}

			if (!HRMSHelper.isNullOrEmpty(candidateActivityActionDetailList)) {
				voCandidateOnboardList = HRMSResponseTranslator.translateListOfCandidateActivityActionDetailToVO(
						candidateActivityActionDetailList, voCandidateOnboardList);
				hrmsListResponseObject.setListResponse((List<Object>) voCandidateOnboardList);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(hrmsListResponseObject);
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
