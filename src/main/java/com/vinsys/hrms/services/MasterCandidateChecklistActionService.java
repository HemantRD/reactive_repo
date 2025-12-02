package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSMasterCandidateChecklistActionDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.entity.MasterCandidateChecklistAction;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterCandidateChecklistAction")

public class MasterCandidateChecklistActionService {

	private static final Logger logger = LoggerFactory.getLogger(MasterCandidateChecklistActionService.class);

	@Autowired
	IHRMSMasterCandidateChecklistActionDAO candidateChecklistActionDAO;

	@RequestMapping(value = "/{organizationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getCandidateChecklistActionsOrgwise(@PathVariable("organizationId") long organizationId) {

		logger.info("Get Candidate checklist action organization wiser ");
		List<MasterCandidateChecklistAction> candidateChecklistActionEntityList = null;
		try {
			if (!HRMSHelper.isLongZero(organizationId)) {
				candidateChecklistActionEntityList = candidateChecklistActionDAO
						.findAllCandidateChecklistActionOrgwise(organizationId, IHRMSConstants.isActive);
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> listResponse = new ArrayList<Object>();
			if (HRMSHelper.isNullOrEmpty(candidateChecklistActionEntityList)) {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
			listResponse = HRMSResponseTranslator.translateListOfMasterCandidateChecklistActionToVO(
					candidateChecklistActionEntityList, listResponse);
			response.setListResponse(listResponse);
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			return HRMSHelper.createJsonString(response);
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
