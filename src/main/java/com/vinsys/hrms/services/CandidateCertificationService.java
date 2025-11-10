package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateCertificationDetalDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateCertification;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateCertification;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/candidateCertification")

public class CandidateCertificationService {

	private static final Logger logger = LoggerFactory.getLogger(CandidateCertificationService.class);

	@Autowired
	IHRMSProfessionalDetailsDAO professionalDetailsDAO;

	@Autowired
	IHRMSCandidateCertificationDetalDAO certificationDetailsDAO;

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	/**
	 * This REST Service will Create candidate certification details ,
	 * 
	 * @param VOCandidateCertification
	 * @return Success or Error JSON
	 * @author shome.nitin
	 */
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String addCandidateCertificationDetails(@RequestBody VOCandidateCertification voCandidateCertification) {

		try {
			if (!HRMSHelper.isNullOrEmpty(voCandidateCertification)) {
				// monika
				if (!HRMSHelper.isNullOrEmpty(voCandidateCertification.getCertificationName())
						&& !HRMSHelper.isNullOrEmpty(voCandidateCertification.getCertificationDate())
						&& !HRMSHelper.isNullOrEmpty(voCandidateCertification.getCertificationValidityDate())) {
					if (voCandidateCertification.getId() > 0) {

						logger.info("UPDATING CANDIDATE CERTIFICATION RECORD");

						CandidateCertification certificationEntity = certificationDetailsDAO
								.findById(voCandidateCertification.getId()).get();

						if (certificationEntity != null) {

							certificationEntity = HRMSRequestTranslator.translateToCandidateCertificationEntity(
									certificationEntity, voCandidateCertification);
							certificationEntity = certificationDetailsDAO.save(certificationEntity);

							// BaseId response = new BaseId();
							// response.setId(certificationEntity.getId());
							// response.setResponseCode(IHRMSConstants.successCode);
							// response.setResponseMessage(IHRMSConstants.updatedsuccessMessage);
							return HRMSHelper.sendSuccessResponse(IHRMSConstants.updatedsuccessMessage,
									IHRMSConstants.successCode);
							// return HRMSHelper.createJsonString(response);
							// return
							// HRMSHelper.sendSuccessResponse(IHRMSConstants.updateSuccesMessage,IHRMSConstants.successCode);
						} else {

							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.DataNotFoundMessage);
						}
					} else {

						if (!HRMSHelper.isNullOrEmpty(voCandidateCertification)
								&& !HRMSHelper.isNullOrEmpty(voCandidateCertification.getCandidateProfessionalDetail())
								&& !HRMSHelper.isNullOrEmpty(
										voCandidateCertification.getCandidateProfessionalDetail().getCandidate())) {

							logger.info("CREATING CANDIDATE CERTIFICATION RECORD");
							Candidate candidate = candidateDAO.findById(
									voCandidateCertification.getCandidateProfessionalDetail().getCandidate().getId())
									.get();
							if (candidate == null) {
								throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
										IHRMSConstants.CandidateDoesnotExistMessage);
							}

							CandidateCertification entityCandidateCertification = new CandidateCertification();
							entityCandidateCertification = HRMSRequestTranslator
									.translateToCandidateCertificationEntity(entityCandidateCertification,
											voCandidateCertification);
							CandidateProfessionalDetail entityProfessionalDetails = professionalDetailsDAO
									.findBycandidate(candidate);

							if (entityProfessionalDetails == null) {
								entityProfessionalDetails = new CandidateProfessionalDetail();
								entityProfessionalDetails.setCandidate(candidate);
								entityProfessionalDetails = professionalDetailsDAO.save(entityProfessionalDetails);
							}

							entityCandidateCertification.setCandidateProfessionalDetail(entityProfessionalDetails);
							entityCandidateCertification = certificationDetailsDAO.save(entityCandidateCertification);
							BaseId response = new BaseId();
							response.setId(entityCandidateCertification.getId());
							response.setResponseCode(IHRMSConstants.successCode);
							response.setResponseMessage(IHRMSConstants.addedsuccessMessage);
							return HRMSHelper.createJsonString(response);

						} else {
							throw new HRMSException(IHRMSConstants.InsufficientDataCode,
									IHRMSConstants.InsufficientDataMessage);
						}
					}
				} // monika
				else {
					throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.InvalidInput);
				}

			} // end if
			else {
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
		// return null
		return null;
	}

	/**
	 * This REST Service will get all the candidate certification details of a
	 * Candidate, ID cannot be null
	 * 
	 * @param id Long
	 * @return List Of Candidate Certificates
	 * @author shome.nitin
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getCandidateCertification(@PathVariable("id") long id) {

		try {
			HRMSListResponseObject response = null;
			Candidate candidate = candidateDAO.findById(id).get();
			if (candidate != null && candidate.getCandidateProfessionalDetail() != null) {

				response = new HRMSListResponseObject();
				Set<CandidateCertification> certificationList = candidate.getCandidateProfessionalDetail()
						.getCandidateCertifications();
				List<Object> certificationListMiodel = new ArrayList<Object>();

				for (CandidateCertification certification : certificationList) {
					VOCandidateCertification model = HRMSEntityToModelMapper
							.convertToCandidateCertificateDetailsModel(certification);
					certificationListMiodel.add(model);

				}

				response.setListResponse(certificationListMiodel);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
			} else {

				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}

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

	/**
	 * This REST Service will delete the candidate certification details by the
	 * provided certification id, ID cannot be null
	 * 
	 * @param certificateId
	 * @return Success or Error JSON
	 * @author shome.nitin
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
	@ResponseBody
	public String deleteCandidateCertification(@PathVariable("id") long certificateId) {

		try {
			if (!HRMSHelper.isLongZero(certificateId)) {

				CandidateCertification certificationEntity = certificationDetailsDAO.findById(certificateId).get();

				if (certificationEntity != null) {
					certificationEntity.getCandidateProfessionalDetail();
					certificationDetailsDAO.delete(certificationEntity);
					return HRMSHelper.sendSuccessResponse(IHRMSConstants.deletedsuccessMessage,
							IHRMSConstants.successCode);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
