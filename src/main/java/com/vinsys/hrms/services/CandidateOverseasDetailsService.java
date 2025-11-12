package com.vinsys.hrms.services;

import java.io.IOException;
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateOverseasExpDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateOverseasExperience;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateOverseasExperience;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/candidateOverseasExperience")

public class CandidateOverseasDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(CandidateOverseasDetailsService.class);

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSProfessionalDetailsDAO professionalDtlsDAO;

	@Autowired
	IHRMSCandidateOverseasExpDAO candOverseaExpDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addCandidateOverseasDetails(@RequestBody VOCandidateOverseasExperience vocandidateOverseasExperience)
			throws JsonGenerationException, JsonMappingException, IOException {
		try {

			if (vocandidateOverseasExperience != null) {

				// monika
				if (!HRMSHelper.isNullOrEmpty(vocandidateOverseasExperience.getCompany())
						&& (!HRMSHelper.isNullOrEmpty(vocandidateOverseasExperience.getFromDate()))
						&& (!HRMSHelper.isNullOrEmpty(vocandidateOverseasExperience.getToDate()))) {

					if (vocandidateOverseasExperience.getId() > 0) {

						logger.info("UPDATING CANDIDATE OVERSEAS RECORD");

						CandidateOverseasExperience overSeasExpirenceEntity = candOverseaExpDAO
								.findById(vocandidateOverseasExperience.getId()).get();
						if (overSeasExpirenceEntity != null) {

							overSeasExpirenceEntity = HRMSRequestTranslator.translateToCandidateOverseasExpReq(
									overSeasExpirenceEntity, vocandidateOverseasExperience);
							overSeasExpirenceEntity = candOverseaExpDAO.save(overSeasExpirenceEntity);
							return HRMSHelper.sendSuccessResponse(IHRMSConstants.updateSuccesMessage,
									IHRMSConstants.successCode);

						} else {

							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.DataNotFoundMessage);
						}
					} else {

						logger.info("CREATING CANDIDATE OVERSEAS RECORD");
						CandidateOverseasExperience candidateOverseasExperienceEntity = new CandidateOverseasExperience();
						candidateOverseasExperienceEntity = HRMSRequestTranslator.translateToCandidateOverseasExpReq(
								candidateOverseasExperienceEntity, vocandidateOverseasExperience);

						if (candidateOverseasExperienceEntity != null
								&& candidateOverseasExperienceEntity.getCandidateProfessionalDetail() != null
								&& candidateOverseasExperienceEntity.getCandidateProfessionalDetail()
										.getCandidate() != null) {

							Candidate candidate = candidateDAO.findById(candidateOverseasExperienceEntity
									.getCandidateProfessionalDetail().getCandidate().getId()).get();

							if (candidate != null) {
								CandidateProfessionalDetail professionalDetail = professionalDtlsDAO
										.findBycandidate(candidate);

								if (professionalDetail == null) {
									professionalDetail = new CandidateProfessionalDetail();
									professionalDetail.setCandidate(candidate);
									professionalDetail = professionalDtlsDAO.save(professionalDetail);
								}

								if (professionalDetail != null) {
									professionalDetail.setCandidate(candidate);
									candidateOverseasExperienceEntity
											.setCandidateProfessionalDetail(professionalDetail);
									candidateOverseasExperienceEntity = candOverseaExpDAO
											.save(candidateOverseasExperienceEntity);

									// HRMSBaseResponse response = new HRMSBaseResponse();
									// response.setResponseCode(IHRMSConstants.successCode);
									// response.setResponseMessage(IHRMSConstants.successMessage);
									// return HRMSHelper.createJsonString(response);
									BaseId response = new BaseId();
									response.setId(candidateOverseasExperienceEntity.getId());
									response.setResponseCode(IHRMSConstants.successCode);
									response.setResponseMessage(IHRMSConstants.addedsuccessMessage);
									return HRMSHelper.createJsonString(response);
								} else {
									throw new HRMSException(IHRMSConstants.ParentDataNotFilledCode,
											IHRMSConstants.ChildProfessionalDataNotFoundMessage);
								}

							} else {
								throw new HRMSException(IHRMSConstants.UserNotFoundCode,
										IHRMSConstants.UserNotFoundMessage);
							}

						} else {

							throw new HRMSException(IHRMSConstants.InsufficientDataCode,
									IHRMSConstants.InsufficientDataMessage);
						}
					}
					// monika
				} else {
					throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.InvalidInput);
				}

			} // end-if
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
		return IHRMSConstants.UnknowErrorMessage;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getCandidateOverseasDetails(@PathVariable("id") long id) {

		try {
			HRMSListResponseObject response = null;
			Candidate candidate = candidateDAO.findById(id).get();
			if (candidate != null && candidate.getCandidateProfessionalDetail() != null) {

				response = new HRMSListResponseObject();
				Set<CandidateOverseasExperience> overseasExpEntitySet = candidate.getCandidateProfessionalDetail()
						.getCandidateOverseasExperiences();
				List<Object> overseasExpListModel = new ArrayList<Object>();

				for (CandidateOverseasExperience overseasExpEntity : overseasExpEntitySet) {
					VOCandidateOverseasExperience model = HRMSEntityToModelMapper
							.convertToCandidateOverseasDetailModel(overseasExpEntity);
					overseasExpListModel.add(model);

				}

				response.setListResponse(overseasExpListModel);
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

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
	@ResponseBody
	public String deleteCandidateOverseasDetail(@PathVariable("id") long overseasDetailsId) {

		try {
			if (overseasDetailsId > 0) {

				CandidateOverseasExperience overseasExpEntity = candOverseaExpDAO.findById(overseasDetailsId).get();

				if (overseasExpEntity != null) {
					overseasExpEntity.getCandidateProfessionalDetail();
					candOverseaExpDAO.delete(overseasExpEntity);
					return HRMSHelper.sendSuccessResponse(IHRMSConstants.deletedsuccessMessage,
							IHRMSConstants.successCode);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}

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
}
