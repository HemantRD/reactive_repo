package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateQualificationDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateQualification;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.CandidateQualification;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/candidateQualification")

public class CandidateQualificationService {

	@Autowired
	IHRMSProfessionalDetailsDAO professionalDetailsDAO;

	@Autowired
	IHRMSCandidateQualificationDAO qualificationDAO;

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	/*
	 * TO CREATE CANDIDATE QUALIFICATION
	 * 
	 * @parameter VOCandidateQualification
	 * 
	 * @method POST
	 * 
	 */
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String addCandidateQualification(@RequestBody VOCandidateQualification voCandidateQualification) {

		try {
			if (voCandidateQualification != null) {

				if(!HRMSHelper.isNullOrEmpty(voCandidateQualification.getInstituteName())&&
				(!HRMSHelper.isNullOrEmpty(voCandidateQualification.getModeOfEducation()))&&
				(!HRMSHelper.isNullOrEmpty(voCandidateQualification.getPassingYearMonth())))
				
				{
				
				if (voCandidateQualification.getId() > 0) {

					/*
					 * TO UPDATE QUALIFICATION DETAILS
					 */
					CandidateQualification qualificationEntity =  qualificationDAO.findById(voCandidateQualification.getId()).get();
					if(qualificationEntity!=null) {
						
						qualificationEntity = HRMSRequestTranslator.translateToPreviousQualificationEntity(qualificationEntity, voCandidateQualification);
						qualificationDAO.save(qualificationEntity);
						return HRMSHelper.sendSuccessResponse(IHRMSConstants.updateSuccesMessage,
								IHRMSConstants.successCode);
					}else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
						
					}
					
				} else {
					/*
					 * TO CREATE QUALIFCATION
					 */
					if (voCandidateQualification != null && voCandidateQualification.getCandidateProfessionalDetail() != null
							&& voCandidateQualification.getCandidateProfessionalDetail().getCandidate() != null) {
						
						
						
					CandidateQualification qualificationEntity = new CandidateQualification();
					CandidateQualification candidateQualificationEntity = HRMSRequestTranslator
							.translateToPreviousQualificationEntity(qualificationEntity, voCandidateQualification);
					
					Candidate candidate = candidateDAO.findById(candidateQualificationEntity.getCandidateProfessionalDetail().getCandidate().getId()).get();
					if(candidate== null) {
						throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,IHRMSConstants.CandidateDoesnotExistMessage);
					}
					CandidateProfessionalDetail profDetailsEntity = professionalDetailsDAO.findBycandidate(candidate);
					
					if(profDetailsEntity == null) {
						profDetailsEntity = new CandidateProfessionalDetail();
						profDetailsEntity.setCandidate(candidate);
						profDetailsEntity = professionalDetailsDAO.save(profDetailsEntity);
					}
					
					candidateQualificationEntity.setCandidateProfessionalDetail(profDetailsEntity);

					candidateQualificationEntity = qualificationDAO.save(candidateQualificationEntity);
					BaseId response = new BaseId();
					response.setId(candidateQualificationEntity.getId());
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(response);
					
					}else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
					}
				}
				
					
				}
				//monika
				else {
					throw new HRMSException(IHRMSConstants.NotValidDateCode,IHRMSConstants.InvalidInput);
				}
			}//end if
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
	public String getCandidateQualification(@PathVariable("id") long id) {

		try {
			HRMSListResponseObject response = null;
			Candidate candidate = candidateDAO.findById(id).get();
			if (candidate != null && candidate.getCandidateProfessionalDetail() != null) {

				response = new HRMSListResponseObject();
				Set<CandidateQualification> qualificationEntitySet = candidate.getCandidateProfessionalDetail()
						.getCandidateQualifications();
				List<Object> qualificationtModel = new ArrayList<Object>();

				for (CandidateQualification qualificationEntity : qualificationEntitySet) {
					VOCandidateQualification model = HRMSEntityToModelMapper
							.convertToCandidateQualificationDetailModel(qualificationEntity);
					qualificationtModel.add(model);

				}

				response.setListResponse(qualificationtModel);
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
	public String deleteCandidateQualification(@PathVariable("id") long candidateQualificationId) {

		try {
			if (candidateQualificationId > 0) {

				CandidateQualification qulificationEntity = qualificationDAO.findById(candidateQualificationId).get();

				if (qulificationEntity != null) {

					qualificationDAO.delete(qulificationEntity);
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
