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

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterModeofSeparationDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterModeofSeparation;
import com.vinsys.hrms.entity.MasterModeofSeparation;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

@RequestMapping(path = "/masterModeofSeparation")

public class MasterModeOfSeparationService {

	@Autowired
	IHRMSMasterModeofSeparationDAO modeofSepoarationDAO;

	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String AddModeofSeparation(@RequestBody VOMasterModeofSeparation voMasterModeofSeparation) {
		Organization orgEntity;
		MasterModeofSeparation masterModeofSeparationEntity;
		String resultMessage = "";
		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterModeofSeparation)
					&& !HRMSHelper.isNullOrEmpty(voMasterModeofSeparation.getOrganization().getId())) {
				masterModeofSeparationEntity = modeofSepoarationDAO.findById(voMasterModeofSeparation.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(masterModeofSeparationEntity)) {
					/* Update */
					masterModeofSeparationEntity = HRMSRequestTranslator.translateToMasterModeofSeparationEntity(
							masterModeofSeparationEntity, voMasterModeofSeparation);
					resultMessage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					masterModeofSeparationEntity = new MasterModeofSeparation();
					orgEntity = organizationDAO.findById(voMasterModeofSeparation.getOrganization().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(orgEntity)) {
						masterModeofSeparationEntity.setOrgId(orgEntity.getId());
						masterModeofSeparationEntity = HRMSRequestTranslator.translateToMasterModeofSeparationEntity(
								masterModeofSeparationEntity, voMasterModeofSeparation);
						resultMessage = IHRMSConstants.addedsuccessMessage;
					} else
						throw new HRMSException(IHRMSConstants.OrganizationDoesnotExistCode,
								IHRMSConstants.OrganizationDoesnotExistMessage);

				}
				modeofSepoarationDAO.save(masterModeofSeparationEntity);
				return HRMSHelper.sendSuccessResponse(resultMessage, IHRMSConstants.successCode);
			} else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
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

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/{isMyselfScreen}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findAllModeofSeparation(@PathVariable("id") long orgId,
			@PathVariable("isMyselfScreen") boolean isMyselfScreen) {
		try {
			if (!HRMSHelper.isNullOrEmpty(orgId) && !HRMSHelper.isNullOrEmpty(isMyselfScreen)) {
				List<MasterModeofSeparation> modeofSeparationList;
				if (isMyselfScreen) {
					modeofSeparationList = modeofSepoarationDAO.findMasterModeofSeparationForEmpCustomQuery(orgId,
							IHRMSConstants.MODEOFSEPARTIONFOREMP);
				} else {
					modeofSeparationList = modeofSepoarationDAO.findAllMasterModeofSeparationCustomQuery(orgId);
				}

				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> objectList = new ArrayList<Object>();
				for (MasterModeofSeparation modeofSeparationEntity : modeofSeparationList) {
					VOMasterModeofSeparation voMasterModeofSeparation = HRMSEntityToModelMapper
							.convertToMstModeofSeparationVO(modeofSeparationEntity);
					objectList.add(voMasterModeofSeparation);
				}
				response.setListResponse(objectList);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);

				return HRMSHelper.createJsonString(response);
			} else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);

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
	public String deleteMasterCity(@PathVariable("id") long modeofSeparationId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(modeofSeparationId)) {
				// modeofSepoarationDAO.delete(modeofSeparationId);
				MasterModeofSeparation modeofSeparationEntity = modeofSepoarationDAO.findById(modeofSeparationId).get();
				modeofSeparationEntity.setIsActive(IHRMSConstants.isNotActive);
				modeofSepoarationDAO.save(modeofSeparationEntity);
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.deletedsuccessMessage, IHRMSConstants.successCode);
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