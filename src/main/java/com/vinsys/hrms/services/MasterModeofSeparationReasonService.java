package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vinsys.hrms.dao.IHRMSMasterModeofSeparationDAO;
import com.vinsys.hrms.dao.IHRMSMasterModeofSeparationReasonDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterModeofSeparationReason;
import com.vinsys.hrms.entity.MasterModeofSeparation;
import com.vinsys.hrms.entity.MasterModeofSeparationReason;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

@RequestMapping(path = "/masterModeofSeparationReason")

public class MasterModeofSeparationReasonService {

	@Autowired
	IHRMSMasterModeofSeparationReasonDAO modeofSepoarationReasonDAO;
	@Autowired
	IHRMSMasterModeofSeparationDAO modeofSeparationDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String AddModeofSeparationReason(@RequestBody VOMasterModeofSeparationReason vomstModeofSeparationReason) {
		MasterModeofSeparation masterModeofSeparationEntity;
		MasterModeofSeparationReason masterModeofSeparationReasonEntity;
		String resultMessage = "";
		try {
			if (!HRMSHelper.isNullOrEmpty(vomstModeofSeparationReason)) {
				masterModeofSeparationReasonEntity = modeofSepoarationReasonDAO
						.findById(vomstModeofSeparationReason.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(masterModeofSeparationReasonEntity)) {
					/* update */
					masterModeofSeparationReasonEntity = HRMSRequestTranslator.translateToMasterModeofSeparationReason(
							masterModeofSeparationReasonEntity, vomstModeofSeparationReason);
					resultMessage = IHRMSConstants.updatedsuccessMessage;
				} else {

					/* insert */
					masterModeofSeparationReasonEntity = new MasterModeofSeparationReason();
					masterModeofSeparationEntity = modeofSeparationDAO
							.findById(vomstModeofSeparationReason.getMasterModeofSeparation().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(masterModeofSeparationEntity)) {
						masterModeofSeparationReasonEntity.setMasterModeofSeparation(masterModeofSeparationEntity);
						masterModeofSeparationReasonEntity = HRMSRequestTranslator
								.translateToMasterModeofSeparationReason(masterModeofSeparationReasonEntity,
										vomstModeofSeparationReason);
						resultMessage = IHRMSConstants.addedsuccessMessage;
					} else
						throw new HRMSException(IHRMSConstants.MODEOFSEPARTIONCODE,
								IHRMSConstants.MODEOFSEPARTIONCODEMESSAGE);
				}
				modeofSepoarationReasonDAO.save(masterModeofSeparationReasonEntity);
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
			unknown.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
	@ResponseBody
	public String findAllModeofSeparationReason(@PathVariable("id") long modeofSeparationId) {
		try {
			if (!HRMSHelper.isNullOrEmpty(modeofSeparationId)) {
				List<MasterModeofSeparationReason> modeofSeparationReasonList = modeofSepoarationReasonDAO
						.findAllMasterModeofSeparationReasonCustomQuery(modeofSeparationId);

				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> objectList = new ArrayList<Object>();
				for (MasterModeofSeparationReason modeofSeparationReasonEntity : modeofSeparationReasonList) {
					VOMasterModeofSeparationReason voMasterModeofSeparationReason = HRMSEntityToModelMapper
							.convertToMstModeofSeparationReasonVO(modeofSeparationReasonEntity);
					objectList.add(voMasterModeofSeparationReason);
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

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
	@ResponseBody
	public String deleteModeofSeparationReason(@PathVariable("id") long modeofSeparationId) {
		try {
			if (!HRMSHelper.isNullOrEmpty(modeofSeparationId)) {
				// modeofSepoarationDAO.delete(modeofSeparationId);
				MasterModeofSeparationReason modeofSeparationReasonEntity = modeofSepoarationReasonDAO
						.findById(modeofSeparationId).get();
				modeofSeparationReasonEntity.setIsActive(IHRMSConstants.isNotActive);
				modeofSepoarationReasonDAO.save(modeofSeparationReasonEntity);
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.deletedsuccessMessage, IHRMSConstants.successCode);
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
}