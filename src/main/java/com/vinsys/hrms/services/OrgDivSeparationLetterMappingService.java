package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSOrgDivSeparationLetterMappingDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.entity.MapOrgDivSeparationLetter;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/orgDivSeperationLetterMapping")

public class OrgDivSeparationLetterMappingService {

	private static final Logger logger = LoggerFactory.getLogger(OrgDivSeparationLetterMappingService.class);

	@Autowired
	IHRMSOrgDivSeparationLetterMappingDAO seperationLetterMappingDAO;

	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/{orgId}/{divId}")
	@ResponseBody
	public String findAllCandidatePolicyDetailsbyId(@PathVariable("orgId") long orgId,
			@PathVariable("divId") long divId) {
		try {
			if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(divId)) {
				List<MapOrgDivSeparationLetter> orgDivSeperationLetters = new ArrayList<MapOrgDivSeparationLetter>();
				List<Object> voOrgDivSeperationLettersyList = new ArrayList<>();
				HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
				Organization organization = organizationDAO.findById(orgId).get();
				MasterDivision masterDivision = divisionDAO.findById(divId).get();
				if (!HRMSHelper.isNullOrEmpty(organization) && !HRMSHelper.isNullOrEmpty(masterDivision)) {
					orgDivSeperationLetters = seperationLetterMappingDAO.getSeparationLetterByOrgDiv(orgId, divId,
							IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(orgDivSeperationLetters)) {
						voOrgDivSeperationLettersyList = HRMSResponseTranslator.translateToOrgDivSeperationLetterVo(
								orgDivSeperationLetters, voOrgDivSeperationLettersyList);
						
						hrmsListResponseObject.setListResponse((List<Object>) voOrgDivSeperationLettersyList);
						hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
						hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
						return HRMSHelper.createJsonString(hrmsListResponseObject);
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				} else {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}

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
