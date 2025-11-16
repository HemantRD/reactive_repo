package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterCountry;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterCountry")


public class MasterCountryService {

	@Autowired
	IHRMSMasterCountryDAO masterCountryDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createMasterCountry(@RequestBody VOMasterCountry voMasterCountry) {

		MasterCountry masterCountryEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterCountry) && !HRMSHelper.isNullOrEmpty(voMasterCountry.getId())) {

				masterCountryEntity = masterCountryDAO.findById(voMasterCountry.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(masterCountryEntity)) {
					/* update */
					masterCountryEntity = HRMSRequestTranslator.translateToMasterCountryEntity(masterCountryEntity,
							voMasterCountry);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					masterCountryEntity = new MasterCountry();
					masterCountryEntity = HRMSRequestTranslator.translateToMasterCountryEntity(masterCountryEntity,
							voMasterCountry);
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				masterCountryDAO.save(masterCountryEntity);
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

	@RequestMapping(method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllMasterCountryDetails() {

		List<MasterCountry> masterCountryEntityList = new ArrayList<>();
		List<Object> voMasterCountryList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			// masterCountryEntityList =
			// masterCountryDAO.findAllMasterCountryByOrgIdCustomQuery(orgId);
			masterCountryEntityList = masterCountryDAO.findAll(Sort.by(Sort.Direction.ASC, "countryName"));
			if (!HRMSHelper.isNullOrEmpty(masterCountryEntityList)) {
				voMasterCountryList = HRMSResponseTranslator.transalteToMasterCountryVO(masterCountryEntityList,
						voMasterCountryList);
				hrmsListResponseObject.setListResponse((List<Object>) voMasterCountryList);
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

	@RequestMapping(method = RequestMethod.GET,value = "/findAll", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllMasterCountry() {

		List<MasterCountry> masterCountryEntityList = new ArrayList<>();
		List<Object> voMasterCountryList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();;
		try {
			// masterCountryEntityList =
			// masterCountryDAO.findAllMasterCountryByOrgIdCustomQuery(orgId);
			masterCountryEntityList = masterCountryDAO.findAllMasterCountryCustomQuery(IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(masterCountryEntityList)) {
				//voMasterCountryList = HRMSResponseTranslator.transalteToMasterCountryVO(masterCountryEntityList,voMasterCountryList);
				voMasterCountryList = HRMSResponseTranslator.transalteToMstCountryVO(masterCountryEntityList, voMasterCountryList);
				hrmsListResponseObject.setListResponse((List<Object>) voMasterCountryList);
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
	public String deleteMasterDivision(@PathVariable("id") long countryId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(countryId)) {
				masterCountryDAO.deleteById(countryId);
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
