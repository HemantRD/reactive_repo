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
import com.vinsys.hrms.dao.IHRMSCandidatePreviousEmploymentDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidatePreviousEmployment;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidatePreviousEmployment;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/candidatePreviousEmployment")

public class CandidatePreviousEmploymentService {

	private static final Logger logger = LoggerFactory.getLogger(CandidatePreviousEmploymentService.class);

	@Autowired
	IHRMSCandidatePreviousEmploymentDAO previousEmploymentDAO;
	@Autowired
	IHRMSProfessionalDetailsDAO professionalDAO;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSMasterCountryDAO countryDAO;
	@Autowired
	IHRMSMasterStateDAO stateDAO;
	@Autowired
	IHRMSMasterCityDAO cityDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String addCandidatePreviousEmployment(
			@RequestBody VOCandidatePreviousEmployment voCandidatePreviousEmployment)
			throws JsonGenerationException, JsonMappingException, IOException {

		try {

			if (voCandidatePreviousEmployment != null) {
				
				//monika
				if(!HRMSHelper.isNullOrEmpty(voCandidatePreviousEmployment.getCompanyName())&&
						(!HRMSHelper.isNullOrEmpty(voCandidatePreviousEmployment.getCompanyAddress()))&&
						//(HRMSHelper.isNullOrEmpty(voCandidatePreviousEmployment.getCountry()))&&
						//(HRMSHelper.isNullOrEmpty(voCandidatePreviousEmployment.getCity()))&&
						//(HRMSHelper.isNullOrEmpty(voCandidatePreviousEmployment.getState()))&&
						!HRMSHelper.isNullOrEmpty(voCandidatePreviousEmployment.getFromDate())&&
						!HRMSHelper.isNullOrEmpty(voCandidatePreviousEmployment.getToDate())&&
						!HRMSHelper.isNullOrEmpty(voCandidatePreviousEmployment.getExperience())
						
						) {

				if (voCandidatePreviousEmployment.getId() > 0) {
					CandidatePreviousEmployment prevEmpEntity = previousEmploymentDAO
							.findById(voCandidatePreviousEmployment.getId()).get();
					logger.info(" == >> UPDATING CANDIDATE PREVIOUS EMPLOYMENT << == ");

					if (prevEmpEntity != null) {
						prevEmpEntity = HRMSRequestTranslator.translateToAddPreviousEmploymentEntity(prevEmpEntity,
								voCandidatePreviousEmployment);
						prevEmpEntity.setState(stateDAO.findById(voCandidatePreviousEmployment.getState().getId()).get());
						prevEmpEntity
								.setCountry(countryDAO.findById(voCandidatePreviousEmployment.getCountry().getId()).get());
						prevEmpEntity.setCity(cityDAO.findById(voCandidatePreviousEmployment.getCity().getId()).get());
						prevEmpEntity.setPreviousManagerEmail(voCandidatePreviousEmployment.getPreviousManagerEmail());
						CandidatePreviousEmployment candidatePreviousEmploymentupdate=previousEmploymentDAO.save(prevEmpEntity);
						//return HRMSHelper.sendSuccessResponse(IHRMSConstants.updateSuccesMessage,IHRMSConstants.successCode);
						
						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> objectList = new ArrayList<Object>();
						VOCandidatePreviousEmployment model = HRMSEntityToModelMapper
								.convertToCandidatePreviousEmploymentModel(candidatePreviousEmploymentupdate);
						objectList.add(model);
						response.setListResponse(objectList);
						response.setResponseCode(IHRMSConstants.successCode);
						response.setResponseMessage(IHRMSConstants.updateSuccesMessage);
						return HRMSHelper.createJsonString(response);
						
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				} else {

					if (voCandidatePreviousEmployment != null
							&& voCandidatePreviousEmployment.getCandidateProfessionalDetail() != null
							&& voCandidatePreviousEmployment.getCandidateProfessionalDetail().getCandidate() != null) {
						
						CandidatePreviousEmployment candidatePreviousEmploymentEntity = new CandidatePreviousEmployment();
						candidatePreviousEmploymentEntity = HRMSRequestTranslator
								.translateToAddPreviousEmploymentEntity(candidatePreviousEmploymentEntity,
										voCandidatePreviousEmployment);
						Candidate candidate = candidateDAO.findById(candidatePreviousEmploymentEntity.getCandidateProfessionalDetail().getCandidate().getId()).get();
						
						if(candidate== null) {
							throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,IHRMSConstants.CandidateDoesnotExistMessage);
						}
						logger.info(" == >> CREATING CANDIDATE PREVIOUS EMPLOYMENT << == ");
						

						candidatePreviousEmploymentEntity
								.setState(stateDAO.findById(voCandidatePreviousEmployment.getState().getId()).get());
						candidatePreviousEmploymentEntity
								.setCountry(countryDAO.findById(voCandidatePreviousEmployment.getCountry().getId()).get());
						candidatePreviousEmploymentEntity
								.setCity(cityDAO.findById(voCandidatePreviousEmployment.getCity().getId()).get());

						
						CandidateProfessionalDetail prefessionalDetailsEntity = professionalDAO.findBycandidate(candidate);
						if(prefessionalDetailsEntity == null) {
							prefessionalDetailsEntity = new CandidateProfessionalDetail();
							prefessionalDetailsEntity.setCandidate(candidate);
							prefessionalDetailsEntity = professionalDAO.save(prefessionalDetailsEntity);
						}
						
						candidatePreviousEmploymentEntity.setCandidateProfessionalDetail(prefessionalDetailsEntity);
						CandidatePreviousEmployment candidatePreviousEmploymentResp=previousEmploymentDAO.save(candidatePreviousEmploymentEntity);

						//return HRMSHelper.sendSuccessResponse(IHRMSConstants.successMessage,IHRMSConstants.successCode);
						
						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> objectList = new ArrayList<Object>();
						VOCandidatePreviousEmployment model = HRMSEntityToModelMapper
								.convertToCandidatePreviousEmploymentModel(candidatePreviousEmploymentResp);
						objectList.add(model);
						response.setListResponse(objectList);
						response.setResponseCode(IHRMSConstants.successCode);
						response.setResponseMessage(IHRMSConstants.addedsuccessMessage);
						return HRMSHelper.createJsonString(response);
						
					} else {

						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}

				}
				//monika
				}else {
					throw new HRMSException(IHRMSConstants.NotValidDateCode,IHRMSConstants.InvalidInput);
					}
				
			}//end-if 
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
		return null;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getCandidatePreviousEmploymentDetails(@PathVariable("id") long id) {

		try {

			logger.info(" == >> GET CANDIDATE PREVIOUS EMPLOYMENT << == ");

			HRMSListResponseObject response = null;
			Candidate candidate = candidateDAO.findById(id).get();
			if (candidate != null && candidate.getCandidateProfessionalDetail() != null) {

				response = new HRMSListResponseObject();
				Set<CandidatePreviousEmployment> prevEmploymentEntitySet = candidate.getCandidateProfessionalDetail()
						.getCandidatePreviousEmployments();
				List<Object> prevEmploymentModelList = new ArrayList<Object>();

				for (CandidatePreviousEmployment prevEmploymentEntity : prevEmploymentEntitySet) {
					VOCandidatePreviousEmployment model = HRMSEntityToModelMapper
							.convertToCandidatePreviousEmploymentModel(prevEmploymentEntity);
					prevEmploymentModelList.add(model);

				}

				response.setListResponse(prevEmploymentModelList);
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
	public String deleteCandidatePreviousEmploymentDetail(@PathVariable("id") long previousEmploymentId) {

		try {
			if (previousEmploymentId != 0) {

				logger.info(" == >> DELETE CANDIDATE PREVIOUS EMPLOYMENT << == ");

				CandidatePreviousEmployment prevEmploymentEntity = previousEmploymentDAO.findById(previousEmploymentId).get();

				if (prevEmploymentEntity != null) {
					prevEmploymentEntity.getCandidateProfessionalDetail();
					previousEmploymentDAO.delete(prevEmploymentEntity);
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
