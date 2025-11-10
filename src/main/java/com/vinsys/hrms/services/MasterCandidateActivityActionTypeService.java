package com.vinsys.hrms.services;

import java.util.ArrayList;
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

import com.vinsys.hrms.dao.IHRMSMasterCandidateActivityActionTypeDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterCandidateActivityActionType;
import com.vinsys.hrms.entity.MasterCandidateActivityActionType;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterCandidateActivityActionType")

public class MasterCandidateActivityActionTypeService {
	
	public static Logger logger = LoggerFactory.getLogger(MasterCandidateActivityActionType.class);
	
	@Autowired
	IHRMSMasterCandidateActivityActionTypeDAO masterCandidateActivityActionTypeDAO;
	
	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String submitMasterCandidateActivityActionType(@RequestBody VOMasterCandidateActivityActionType voMasterCandidateActivityActionType) {

		MasterCandidateActivityActionType masterCandidateActivityActionTypeEntity;
		Organization organizationEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterCandidateActivityActionType)
					&& !HRMSHelper.isNullOrEmpty(voMasterCandidateActivityActionType.getOrganization().getId())) {

				masterCandidateActivityActionTypeEntity = masterCandidateActivityActionTypeDAO.findById(voMasterCandidateActivityActionType.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(masterCandidateActivityActionTypeEntity)) {
					// update 
					masterCandidateActivityActionTypeEntity = HRMSRequestTranslator
							.translateToMasterCandidateActivityActionTypeEntity(masterCandidateActivityActionTypeEntity, voMasterCandidateActivityActionType);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					// insert 
					masterCandidateActivityActionTypeEntity = new MasterCandidateActivityActionType();
					organizationEntity = new Organization();
					organizationEntity = organizationDAO.findById(voMasterCandidateActivityActionType.getOrganization().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(organizationEntity)) {
						masterCandidateActivityActionTypeEntity.setOrgId(organizationEntity.getId());
						masterCandidateActivityActionTypeEntity = HRMSRequestTranslator
								.translateToMasterCandidateActivityActionTypeEntity(masterCandidateActivityActionTypeEntity, voMasterCandidateActivityActionType);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				masterCandidateActivityActionTypeDAO.save(masterCandidateActivityActionTypeEntity);
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/{organizationId}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getCandidateActivityActionTypeByOrganization(@PathVariable("organizationId") long organizationId) {

		List<MasterCandidateActivityActionType> masterCandidateActivityActionTypes = new ArrayList<>();
		List<Object> voMasterCandidateActivityActionTypesList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			if (HRMSHelper.isNullOrEmpty(organizationId)) {
				logger.info("in getCandidateActivityActionTypeOrganization() : Find all block");
			}else {
				logger.info("in getCandidateActivityActionTypeOrganization() : Find by query block");
				masterCandidateActivityActionTypes = masterCandidateActivityActionTypeDAO.findAllCandidateActivityTypeByOrganization(organizationId, IHRMSConstants.isActive);
				if(!HRMSHelper.isNullOrEmpty(masterCandidateActivityActionTypes)) {
					voMasterCandidateActivityActionTypesList = HRMSResponseTranslator.translateToMasterCandidateActivityActionTypesVO(masterCandidateActivityActionTypes, voMasterCandidateActivityActionTypesList);
					hrmsListResponseObject.setListResponse((List<Object>) voMasterCandidateActivityActionTypesList);
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
