package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterEmploymentType;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterEmploymentType")

public class MasterEmploymentTypeService {

	@Autowired
	IHRMSMasterEmployementTypeDAO masterEmployementTypeDAO;

	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createMasterEmploymentType(@RequestBody VOMasterEmploymentType voMasterEmploymentType) {

		MasterEmploymentType masterEmploymentTypeEntity;
		Organization organizationEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterEmploymentType)
					&& !HRMSHelper.isNullOrEmpty(voMasterEmploymentType.getOrganization().getId())) {

				masterEmploymentTypeEntity = masterEmployementTypeDAO.findById(voMasterEmploymentType.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(masterEmploymentTypeEntity)) {
					/* update */
					// masterDesignationEntity =
					masterEmploymentTypeEntity = HRMSRequestTranslator
							.translateToMasterEmploymentTypeEntity(masterEmploymentTypeEntity, voMasterEmploymentType);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					masterEmploymentTypeEntity = new MasterEmploymentType();
					organizationEntity = organizationDAO.findById(voMasterEmploymentType.getOrganization().getId())
							.get();
					if (!HRMSHelper.isNullOrEmpty(organizationEntity)) {
						masterEmploymentTypeEntity.setOrganization(organizationEntity);
						// masterDesignationEntity =
						// HRMSRequestTranslator.translateToMasterBranchEntity(masterDesignationEntity,voMasterDesignation);
						masterEmploymentTypeEntity = HRMSRequestTranslator.translateToMasterEmploymentTypeEntity(
								masterEmploymentTypeEntity, voMasterEmploymentType);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				masterEmployementTypeDAO.save(masterEmploymentTypeEntity);
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
	public String getAllMasterEmploymentType(@PathVariable("id") long employmentTypeId) {

		List<MasterEmploymentType> masterEmploymentTypeEntityList = new ArrayList<>();
		List<Object> voMasterEmploymentTypeList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			masterEmploymentTypeEntityList = masterEmployementTypeDAO
					.findAllMasterEmploymentTypeByOrgIdCustomQuery(employmentTypeId, IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(masterEmploymentTypeEntityList)) {
				voMasterEmploymentTypeList = HRMSResponseTranslator
						.translateToMasterEmploymentTypeVO(masterEmploymentTypeEntityList, voMasterEmploymentTypeList);
				hrmsListResponseObject.setListResponse((List<Object>) voMasterEmploymentTypeList);
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

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteMasterEmploymentType(@PathVariable("id") long employmentTypeId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(employmentTypeId)) {
				masterEmployementTypeDAO.deleteById(employmentTypeId);
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
		} catch (EmptyResultDataAccessException exception) {
			try {
				exception.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
						IHRMSConstants.DataNotFoundCode);
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
