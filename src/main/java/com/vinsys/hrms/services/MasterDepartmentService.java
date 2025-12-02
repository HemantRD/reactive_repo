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

import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterDepartment;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterDepartment")

public class MasterDepartmentService {

	@Autowired
	IHRMSMasterDepartmentDAO masterDepartmentDAO;

	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createMasterDepartmentActivity(@RequestBody VOMasterDepartment voMasterDepartment) {

		MasterDepartment masterDepartmentEntity;
		Organization organizationEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterDepartment)
					&& !HRMSHelper.isNullOrEmpty(voMasterDepartment.getOrganization().getId())) {

				masterDepartmentEntity = masterDepartmentDAO.findById(voMasterDepartment.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(masterDepartmentEntity)) {
					/* update */
					// masterDepartmentEntity =
					// HRMSRequestTranslator.translateToMasterCandidateActivityEntity(masterDepartmentEntity,voMasterDepartment);
					masterDepartmentEntity = HRMSRequestTranslator
							.translateToMasterDepartmentEntity(masterDepartmentEntity, voMasterDepartment);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					masterDepartmentEntity = new MasterDepartment();

					organizationEntity = organizationDAO.findById(voMasterDepartment.getOrganization().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(organizationEntity)) {
						masterDepartmentEntity.setOrganization(organizationEntity);
						masterDepartmentEntity = HRMSRequestTranslator
								.translateToMasterDepartmentEntity(masterDepartmentEntity, voMasterDepartment);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				masterDepartmentDAO.save(masterDepartmentEntity);
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
	public String getAllMasterDepartmentByOrgId(@PathVariable("id") long orgId) {

		List<MasterDepartment> masterDepartmentEntityList = new ArrayList<>();
		List<Object> voMasterDepartmentList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			// masterBranchEntityList = masterBranchDAO.findAll();
			masterDepartmentEntityList = masterDepartmentDAO.findAllMasterDepartmentByOrgIdCustomQuery(orgId,
					IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(masterDepartmentEntityList)) {
				// voMasterDepartmentList =
				// HRMSResponseTranslator.transalteToMasterCandidateActivityVO(masterCandidateActivityEntityList,voMasterCandidateActivityList);
				voMasterDepartmentList = HRMSResponseTranslator
						.transalteToMasterDepartmentVO(masterDepartmentEntityList, voMasterDepartmentList);
				hrmsListResponseObject.setListResponse((List<Object>) voMasterDepartmentList);
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

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteMasterDepartment(@PathVariable("id") long branchId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(branchId)) {
				masterDepartmentDAO.deleteById(branchId);
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
