package com.vinsys.hrms.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSMasterBranchDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterBranch;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterBranch")

public class MasterBranchService {

	@Autowired
	IHRMSMasterBranchDAO masterBranchDAO;

	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createMasterBranch(@RequestBody VOMasterBranch voMasterBranch) {

		MasterBranch masterBranchEntity;
		Organization organizationEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterBranch)
					&& !HRMSHelper.isNullOrEmpty(voMasterBranch.getOrganization().getId())) {

				masterBranchEntity = masterBranchDAO.findById(voMasterBranch.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(masterBranchEntity)) {
					/* update */
					masterBranchEntity = HRMSRequestTranslator.translateToMasterBranchEntity(masterBranchEntity,
							voMasterBranch);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					masterBranchEntity = new MasterBranch();
					organizationEntity = organizationDAO.findById(voMasterBranch.getOrganization().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(organizationEntity)) {
						masterBranchEntity.setOrgId(organizationEntity.getId());
						masterBranchEntity = HRMSRequestTranslator.translateToMasterBranchEntity(masterBranchEntity,
								voMasterBranch);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				masterBranchDAO.save(masterBranchEntity);
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
	public String getAllMasterBranch(@PathVariable("id") long orgId) {

		List<MasterBranch> masterBranchEntityList = new ArrayList<>();
		List<Object> voMasterBranchList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			// masterBranchEntityList = masterBranchDAO.findAll();
			masterBranchEntityList = masterBranchDAO.findAllMasterBranchByOrgIdCustomQuery(orgId, IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(masterBranchEntityList)) {
				voMasterBranchList = HRMSResponseTranslator.transalteToMasterBranchVO(masterBranchEntityList,
						voMasterBranchList);
				hrmsListResponseObject.setListResponse((List<Object>) voMasterBranchList);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				// return HRMSHelper.createJsonString(voMasterBranchList);
				return HRMSHelper.createJsonString(hrmsListResponseObject);
			} else {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
						IHRMSConstants.DataNotFoundCode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteMasterBranch(@PathVariable("id") long branchId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(branchId)) {
				masterBranchDAO.deleteById(branchId);
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