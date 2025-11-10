package com.vinsys.hrms.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.IHRMSMasterLanguageDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterLanguage;
import com.vinsys.hrms.entity.MasterLanguage;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterLanguage")

public class MasterLanguageService {

	@Autowired
	IHRMSMasterLanguageDAO languageDAO;

	@RequestMapping(method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllLanguageList() throws JsonGenerationException, JsonMappingException, IOException {

		try {
			List<MasterLanguage> listOfLanguage = languageDAO.findByisActive(IHRMSConstants.isActive,
					Sort.by(Sort.Direction.ASC, "languageName"));
			if (listOfLanguage != null && !listOfLanguage.isEmpty()) {
				List<Object> responseObject = new ArrayList<Object>();
				for (MasterLanguage entity : listOfLanguage) {
					VOMasterLanguage model = HRMSEntityToModelMapper.convertToLanguageMasterVO(entity);
					responseObject.add(model);
				}

				HRMSListResponseObject listResponse = new HRMSListResponseObject();
				listResponse.setListResponse(responseObject);
				listResponse.setResponseCode(IHRMSConstants.successCode);
				listResponse.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(listResponse);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.NO_LANGUAGE_FOUND);
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
