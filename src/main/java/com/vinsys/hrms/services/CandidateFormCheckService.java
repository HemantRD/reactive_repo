package com.vinsys.hrms.services;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateEmergencyContact;
import com.vinsys.hrms.entity.CandidateFamilyDetail;
import com.vinsys.hrms.entity.CandidateHealthReport;
import com.vinsys.hrms.entity.CandidateLanguage;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.CandidateQualification;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/candidateFormCheck")

public class CandidateFormCheckService {

	private static final Logger logger = LoggerFactory.getLogger(CandidateFormCheckService.class);

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@RequestMapping(value = "/{candidateId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String candidateFormCheckService(@PathVariable("candidateId") long candidateId) {
		Candidate candidate = candidateDAO.findById(candidateId).get();
		try {
			if (candidate != null) {

				HRMSListResponseObject response = new HRMSListResponseObject();
				boolean formCheck = checkForms(candidate);
				response.setCandidateFormSubmitted(formCheck);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(response);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.CandidateDoesnotExistMessage);
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

	private boolean checkForms(Candidate candidate) {

		CandidatePersonalDetail personalDetails = candidate.getCandidatePersonalDetail();
		boolean flag = true;

		if (personalDetails != null) {

			Set<CandidateEmergencyContact> candidatePersonalDetailsSet = personalDetails
					.getCandidateEmergencyContacts();
			if (candidatePersonalDetailsSet == null || candidatePersonalDetailsSet.isEmpty()) {
				logger.info(" : in IF CandidateEmergencyContact : ");
				return flag = false;
			}
			Set<CandidateFamilyDetail> candidateFamilyDetailsSet = personalDetails.getCandidateFamilyDetails();

			if (candidateFamilyDetailsSet == null || candidateFamilyDetailsSet.isEmpty()) {
				logger.info(" : in IF candidateFamilyDetails : ");
				return flag = false;
			}

			CandidateHealthReport candidateHealthReport = personalDetails.getCandidateHealthReport();
			if (candidateHealthReport == null) {
				logger.info(" : in IF CandidateHealthReport : ");
				return flag = false;
			}

			/*
			 * CandidateStatutoryNomination candidateStatutoryNomination = personalDetails
			 * .getCandidateStatutoryNomination(); if (candidateStatutoryNomination == null)
			 * { logger.info(" : in IF CandidateStatutoryNomination : "); return false; }
			 */

			Set<CandidateLanguage> candidateLanguageDetails = personalDetails.getCandidateLanguages();
			if (candidateLanguageDetails == null || candidateLanguageDetails.isEmpty()) {
				logger.info(" : in IF CandidateLanguage : ");
				return flag = false;
			}

			/*
			 * CandidatePassportDetail candidatePassportDetails =
			 * personalDetails.getCandidatePassportDetail(); if (candidatePassportDetails ==
			 * null) { logger.info(" : in IF CandidatePassportDetail : "); return false; }
			 */

			/*
			 * CandidatePolicyDetail candidatePolicyDetails =
			 * personalDetails.getCandidatePolicyDetail(); if (candidatePolicyDetails ==
			 * null) { logger.info(" : in IF CandidatePolicyDetail : "); return false; }
			 */

			/*
			 * Set<CandidateVisaDetail> candidateVisaDetails =
			 * personalDetails.getCandidateVisaDetails(); if (candidateVisaDetails == null
			 * || candidateVisaDetails.isEmpty()) {
			 * logger.info(" : in IF CandidateVisaDetail : "); return false; }
			 */
		} else {
			return flag = false;
		}

		CandidateProfessionalDetail professionalDetails = candidate.getCandidateProfessionalDetail();

		if (professionalDetails != null) {

			/*
			 * Set<CandidateCertification> candidateCertificationset =
			 * professionalDetails.getCandidateCertifications(); if
			 * (candidateCertificationset == null || candidateCertificationset.isEmpty()) {
			 * logger.info(" : in IF CandidateCertification : "); return false; }
			 */

			/*
			 * Set<CandidateChecklist> candidateChecklistset =
			 * professionalDetails.getCandidateChecklists(); if (candidateChecklistset ==
			 * null || candidateChecklistset.isEmpty()) {
			 * logger.info(" : in IF CandidateChecklist : "); return false; }
			 */

			/*
			 * Set<CandidateOverseasExperience> candidateOverseasExpirenceset =
			 * professionalDetails .getCandidateOverseasExperiences(); if
			 * (candidateOverseasExpirenceset == null ||
			 * candidateOverseasExpirenceset.isEmpty()) {
			 * logger.info(" : in IF CandidateOverseasExperience : "); return false; }
			 */

			/*
			 * Set<CandidatePreviousEmployment> candidatePreviousEmploymentSet =
			 * professionalDetails .getCandidatePreviousEmployments(); if
			 * (candidatePreviousEmploymentSet == null ||
			 * candidatePreviousEmploymentSet.isEmpty()) {
			 * logger.info(" : in IF CandidatePreviousEmployment : "); return false; }
			 */
			Set<CandidateQualification> candidateQualificationSet = professionalDetails.getCandidateQualifications();
			if (candidateQualificationSet == null || candidateQualificationSet.isEmpty()) {
				logger.info(" : in IF CandidateQualification : ");
				return flag = false;
			}
		} else {
			logger.info(" : PREVIOUS EMPLOYMENT : ");
			return flag = false;
		}

		/*
		 * If all the above condition passed successfully,which mean all the forms has
		 * been is not null,thus the candidate activity status will get changed to form
		 * filled
		 */
		if (flag) {
			logger.info("Mandarory Forms  Filled");
			candidate.setCandidateActivityStatus(IHRMSConstants.CANDIDATE_ACTIVITY_MANDATORY_FORM_FILLED);
		} else {
			logger.info("Mandarory Forms Not Filled");
			candidate.setCandidateActivityStatus(IHRMSConstants.CANDIDATE_ACTIVITY_MANDATORY_FORM_NOT_FILLED);
		}
		candidateDAO.save(candidate);
		return flag;
	}

}
