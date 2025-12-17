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

import com.vinsys.hrms.dao.IHRMSMasterOnboardActionReasonDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterOnboardActionReason;
import com.vinsys.hrms.entity.MasterCandidateOnboardActionReason;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * @author waikar.saurabh
 * 
 *         This class for service of on board action reason
 *
 */
@Hidden
@RestController
@RequestMapping(path = "/masterOnboardActionReason")

public class MasterCandidateOnboardActionReasonService {

	@Autowired
	IHRMSMasterOnboardActionReasonDAO actionReasonDAO;

	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/{typeOfAction}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllMasterOnboardActionReason(@PathVariable("id") long orgId,
			@PathVariable("typeOfAction") String typeOfAction) {

		List<MasterCandidateOnboardActionReason> onboardActionReasons = new ArrayList<>();
		List<Object> voMasterOnboardActionReasonList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			if (!HRMSHelper.isNullOrEmpty(orgId) && !HRMSHelper.isNullOrEmpty(typeOfAction)) {
				onboardActionReasons = actionReasonDAO.findAllMasterOnboardActionReasonByOrgIdCustomQuery(orgId,
						typeOfAction, IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(onboardActionReasons)) {
					voMasterOnboardActionReasonList = HRMSResponseTranslator
							.translateToMasterCandidateOnboardActionReasonVO(onboardActionReasons,
									voMasterOnboardActionReasonList);
					hrmsListResponseObject.setListResponse((List<Object>) voMasterOnboardActionReasonList);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
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

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createMasterOnboardActionReason(
			@RequestBody VOMasterOnboardActionReason voMasterOnboardActionReason) {

		MasterCandidateOnboardActionReason onboardActionReasonEntity;
		Organization organizationEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterOnboardActionReason)
					&& !HRMSHelper.isNullOrEmpty(voMasterOnboardActionReason.getOrganization().getId())) {

				onboardActionReasonEntity = actionReasonDAO.findById(voMasterOnboardActionReason.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(onboardActionReasonEntity)) {
					/* update */
					onboardActionReasonEntity = HRMSRequestTranslator.translateToMasterOnboardActionReasonEntity(
							onboardActionReasonEntity, voMasterOnboardActionReason);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					onboardActionReasonEntity = new MasterCandidateOnboardActionReason();
					organizationEntity = organizationDAO.findById(voMasterOnboardActionReason.getOrganization().getId())
							.get();
					if (!HRMSHelper.isNullOrEmpty(organizationEntity)) {
						onboardActionReasonEntity.setOrgId(organizationEntity.getId());
						onboardActionReasonEntity = HRMSRequestTranslator.translateToMasterOnboardActionReasonEntity(
								onboardActionReasonEntity, voMasterOnboardActionReason);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				actionReasonDAO.save(onboardActionReasonEntity);
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
}
