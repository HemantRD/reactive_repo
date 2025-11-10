package com.vinsys.hrms.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOLoginEntityType;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;
@Hidden @RestController
@RequestMapping(path = "/loginEntityType")

public class LoginEntityTypeService {

	@Autowired
	IHRMSLoginEntityTypeDAO loginEntityTypeDAO;

	@RequestMapping(value = "/{orgId}/{isCandInclude}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getLoginEntityTypes(@PathVariable("orgId") long orgId,
			@PathVariable("isCandInclude") String isCandInclude)
			throws JsonGenerationException, JsonMappingException, IOException {

		List<LoginEntityType> loginEntityTypeList = loginEntityTypeDAO.findByOrganizationId(orgId,
				IHRMSConstants.isActive);
		try {
			if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isNullOrEmpty(isCandInclude)) {

				List<Object> listResponse = new ArrayList<Object>();
				HRMSListResponseObject response = new HRMSListResponseObject();
				for (LoginEntityType loginEntityType : loginEntityTypeList) {

					VOLoginEntityType model = HRMSRequestTranslator.translateToLoginEntityTypeResponse(loginEntityType);
					if (loginEntityType.getLoginEntityTypeName().equals("Candidate")) {
						if (isCandInclude.equals("Y")) {
							listResponse.add(model);
						}
					} else {
						listResponse.add(model);
					}
				}
				response.setListResponse(listResponse);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(response);
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
}
