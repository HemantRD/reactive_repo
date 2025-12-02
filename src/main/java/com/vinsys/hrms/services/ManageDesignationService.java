package com.vinsys.hrms.services;

/*@author akanksha gaikwad
 * description: service created for manage designation.
 * 
 */
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSMasterDesignationDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterDesignation;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/designation")

public class ManageDesignationService {
	private static final Logger logger = LoggerFactory.getLogger(ManageDesignationService.class);

	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@Autowired
	IHRMSMasterDesignationDAO designationDAO;

	@Autowired
	IHRMSMasterDesignationDAO masterDesignationDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createMasterDesignation(@RequestBody VOMasterDesignation voMasterDesignation) {

		MasterDesignation masterDesignationEntity;
		Organization organizationEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterDesignation)) {

				masterDesignationEntity = masterDesignationDAO.findById(voMasterDesignation.getId()).get();

				List<MasterDesignation> allMasterDesgnation = null;

				if (Boolean.TRUE.equals(masterDesignationDAO
						.existsByDesignationNameIgnoreCase(voMasterDesignation.getDesignationName()))) {
					throw new HRMSException(IHRMSConstants.DesignationAlredyExist);
				}

				if (!HRMSHelper.isNullOrEmpty(masterDesignationEntity)) {
					/* update */

					masterDesignationEntity = HRMSRequestTranslator
							.translateToMasterDesignationEntity(masterDesignationEntity, voMasterDesignation);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					masterDesignationEntity = new MasterDesignation();
					organizationEntity = organizationDAO.findById(voMasterDesignation.getOrganization().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(organizationEntity)) {
						masterDesignationEntity.setOrgId(organizationEntity.getId());

						masterDesignationEntity = HRMSRequestTranslator
								.translateToMasterDesignationEntity(masterDesignationEntity, voMasterDesignation);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				masterDesignationDAO.save(masterDesignationEntity);
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

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/{page}/{size}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllMasterdesignation(@PathVariable("id") long orgId, @PathVariable("page") int page,
			@PathVariable("size") int size) {

		logger.info("Page No : " + page);
		logger.info("Page No : " + size);
		if (size <= 0) {
			size = 10;
		}

		int totalCount = 0;
		List<MasterDesignation> masterDesignationEntityList = new ArrayList<>();
		List<Object> voMasterDesignationList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			masterDesignationEntityList = masterDesignationDAO.findAllMasterDesignationByOrgIdCustomQuery(orgId,
					IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(masterDesignationEntityList)) {
				voMasterDesignationList = HRMSResponseTranslator
						.transalteToMasterDesignationVO(masterDesignationEntityList, voMasterDesignationList);
				hrmsListResponseObject.setListResponse((List<Object>) voMasterDesignationList);
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
	@Transactional
	public String deleteMasterBranch(@PathVariable("id") long desigationId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(desigationId)) {
				masterDesignationDAO.deleteDesignation(desigationId);
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
