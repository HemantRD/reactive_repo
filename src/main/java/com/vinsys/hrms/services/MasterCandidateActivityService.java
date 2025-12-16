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

import com.vinsys.hrms.dao.IHRMSMasterCandidateActivityDAO;
//import com.vinsys.hrms.dao.IHRMSMasterBranchDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterCandidateActivity;
import com.vinsys.hrms.entity.MasterCandidateActivity;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
//import com.vinsys.hrms.datamodel.HRMSListResponseObject;
//import com.vinsys.hrms.datamodel.VOMasterBranch;
//import com.vinsys.hrms.entity.MasterBranch;
//import com.vinsys.hrms.entity.Organization;
//import com.vinsys.hrms.exception.HRMSException;
//import com.vinsys.hrms.translator.HRMSRequestTranslator;
//import com.vinsys.hrms.translator.HRMSResponseTranslator;
//import com.vinsys.hrms.util.HRMSHelper;
//import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterCandidateActivity")

public class MasterCandidateActivityService {

	@Autowired
	// IHRMSMasterBranchDAO masterBranchDAO;
	IHRMSMasterCandidateActivityDAO candidateActivityDAO;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createMasterCandidateActivity(@RequestBody VOMasterCandidateActivity voMasterCandidateActivity) {

		// MasterBranch masterBranchEntity;
		MasterCandidateActivity masterCandidateActivityEntity;
		Organization organizationEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterCandidateActivity)
					&& !HRMSHelper.isNullOrEmpty(voMasterCandidateActivity.getOrganization().getId())) {

				masterCandidateActivityEntity = candidateActivityDAO.findById(voMasterCandidateActivity.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(masterCandidateActivityEntity)) {
					/* update */
					masterCandidateActivityEntity = HRMSRequestTranslator.translateToMasterCandidateActivityEntity(
							masterCandidateActivityEntity, voMasterCandidateActivity);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					masterCandidateActivityEntity = new MasterCandidateActivity();
					organizationEntity = organizationDAO.findById(voMasterCandidateActivity.getOrganization().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(organizationEntity)) {
						masterCandidateActivityEntity.setOrgId(organizationEntity.getId());
						masterCandidateActivityEntity = HRMSRequestTranslator.translateToMasterCandidateActivityEntity(
								masterCandidateActivityEntity, voMasterCandidateActivity);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				candidateActivityDAO.save(masterCandidateActivityEntity);
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

	@RequestMapping(method = RequestMethod.GET, value = "/{orgId}/{divisionId}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllMasterCandidateActivityByOrgId(@PathVariable("orgId") long orgId,
			@PathVariable("divisionId") long divisionId) {

		List<MasterCandidateActivity> masterCandidateActivityEntityList = new ArrayList<>();
		List<Object> voMasterCandidateActivityList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(divisionId)) {
				// masterBranchEntityList = masterBranchDAO.findAll();
				masterCandidateActivityEntityList = candidateActivityDAO
						.findAllMasterCandidateActivityByOrgIdCustomQuery(orgId, divisionId, IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(masterCandidateActivityEntityList)) {
					voMasterCandidateActivityList = HRMSResponseTranslator.transalteToMasterCandidateActivityVO(
							masterCandidateActivityEntityList, voMasterCandidateActivityList);
					hrmsListResponseObject.setListResponse((List<Object>) voMasterCandidateActivityList);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					// return HRMSHelper.createJsonString(voMasterBranchList);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else {
					return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
							IHRMSConstants.DataNotFoundCode);
				}
			} else {
				// insufficient data
				return HRMSHelper.sendErrorResponse(IHRMSConstants.InsufficientDataMessage,
						IHRMSConstants.InsufficientDataCode);
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

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteMasterCandidateActivity(@PathVariable("id") long branchId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(branchId)) {
				candidateActivityDAO.deleteById(branchId);
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.deletedsuccessMessage, IHRMSConstants.successCode);
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
