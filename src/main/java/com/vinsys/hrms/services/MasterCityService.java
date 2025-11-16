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

import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterCity;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterCity")

public class MasterCityService {

	@Autowired
	IHRMSMasterCityDAO masterCityDAO;

	@Autowired
	IHRMSMasterStateDAO masterStateDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createMasterCity(@RequestBody VOMasterCity voMasterCity) {

		MasterCity masterCityEntity;
		MasterState masterStateEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterCity) && !HRMSHelper.isNullOrEmpty(voMasterCity.getState().getId())) {

				masterCityEntity = masterCityDAO.findById(voMasterCity.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(masterCityEntity)) {
					/* update */
					// masterDesignationEntity =
					// HRMSRequestTranslator.translateToMasterBranchEntity(masterDesignationEntity,voMasterDesignation);
					masterCityEntity = HRMSRequestTranslator.translateToMasterCityEntity(masterCityEntity,
							voMasterCity);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					masterCityEntity = new MasterCity();
					masterStateEntity = masterStateDAO.findById(voMasterCity.getState().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(masterStateEntity)) {
						masterCityEntity.setMasterState(masterStateEntity);
						// masterDesignationEntity =
						// HRMSRequestTranslator.translateToMasterBranchEntity(masterDesignationEntity,voMasterDesignation);
						masterCityEntity = HRMSRequestTranslator.translateToMasterCityEntity(masterCityEntity,
								voMasterCity);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				masterCityDAO.save(masterCityEntity);
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
	public String getAllMasterCity(@PathVariable("id") long stateId) {

		List<MasterCity> masterCityEntityList = new ArrayList<>();
		List<Object> voMasterCityList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			masterCityEntityList = masterCityDAO.findAllMasterCityByStateIdCustomQuery(stateId,
					IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(masterCityEntityList)) {
				voMasterCityList = HRMSResponseTranslator.transalteToMasterCityVO(masterCityEntityList,
						voMasterCityList);
				hrmsListResponseObject.setListResponse((List<Object>) voMasterCityList);
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
	public String deleteMasterCity(@PathVariable("id") long CityId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(CityId)) {
				masterCityDAO.deleteById(CityId);
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
