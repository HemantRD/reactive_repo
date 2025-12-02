package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.dao.IHRMSCandidateStatutoryNominationDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateStatutoryNomination;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidateStatutoryNomination;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping("/candidateStatutoryNomination")

public class CandidateStatutoryNominationService {

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO;
	@Autowired
	IHRMSCandidateStatutoryNominationDAO candidateStatutoryNominationDAO;

	BaseId baseresponse = new BaseId();

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addCandidateNominationDetails(
			@RequestBody VOCandidateStatutoryNomination vocandidateStatutoryNomination) {
		try {

			if (!HRMSHelper.isNullOrEmpty(vocandidateStatutoryNomination)
					&& !HRMSHelper.isNullOrEmpty(vocandidateStatutoryNomination.getCandidatePersonalDetail())
					&& !HRMSHelper.isNullOrEmpty(
							vocandidateStatutoryNomination.getCandidatePersonalDetail().getCandidate())) {
				Candidate candidateEntity = candidateDAO
						.getOne(vocandidateStatutoryNomination.getCandidatePersonalDetail().getCandidate().getId());
				if (!HRMSHelper.isNullOrEmpty(candidateEntity)) {
					CandidatePersonalDetail candidatePersonalDetailEntity = candidatePersonalDetailDAO
							.findBycandidate(candidateEntity);

					if (candidatePersonalDetailEntity == null) {
						candidatePersonalDetailEntity = new CandidatePersonalDetail();
						candidatePersonalDetailEntity.setCandidate(candidateEntity);
						candidatePersonalDetailEntity = candidatePersonalDetailDAO.save(candidatePersonalDetailEntity);
					}

					if (!HRMSHelper.isNullOrEmpty(candidatePersonalDetailEntity)) {
						candidatePersonalDetailEntity.setCandidate(candidateEntity);

						if (vocandidateStatutoryNomination.getId() > 0) {

							CandidateStatutoryNomination candidateStatutoryNominationEntity = candidateStatutoryNominationDAO
									.findById(vocandidateStatutoryNomination.getId()).get();
							CandidateStatutoryNomination candidateStatutoryNominationEntityResp = HRMSRequestTranslator
									.convertToUpdateCandidateStatutoryNominationEntity(
											candidateStatutoryNominationEntity, vocandidateStatutoryNomination);
							candidateStatutoryNominationEntityResp
									.setCandidatePersonalDetail(candidatePersonalDetailEntity);
							candidateStatutoryNominationDAO.save(candidateStatutoryNominationEntityResp);
							if (candidateStatutoryNominationEntityResp != null) {
								baseresponse.setId(candidateStatutoryNominationEntityResp.getId());
								baseresponse.setResponseCode(IHRMSConstants.successCode);
								baseresponse.setResponseMessage(IHRMSConstants.updatedsuccessMessage);
								return HRMSHelper.createJsonString(baseresponse);
							}
						} else {
							CandidateStatutoryNomination toCheckCandidateStatutoryNominationExist = candidateStatutoryNominationDAO
									.findBycandidatePersonalDetail(candidatePersonalDetailEntity);
							if (!HRMSHelper.isNullOrEmpty(toCheckCandidateStatutoryNominationExist)) {
								throw new HRMSException(IHRMSConstants.CandidateDataAlreadyExistCode,
										IHRMSConstants.CandidateDataAlreadyExistMessage);
							} else {
								CandidateStatutoryNomination candidateStatutoryNominationEntity = HRMSRequestTranslator
										.convertToCandidateStatutoryNominationEntity(vocandidateStatutoryNomination);

								candidateStatutoryNominationEntity
										.setCandidatePersonalDetail(candidatePersonalDetailEntity);
								candidateStatutoryNominationEntity.setIsActive(IHRMSConstants.isActive);
								CandidateStatutoryNomination candidateStatutoryNominationEntityResp = candidateStatutoryNominationDAO
										.save(candidateStatutoryNominationEntity);
								if (candidateStatutoryNominationEntityResp != null) {
									baseresponse.setId(candidateStatutoryNominationEntityResp.getId());
									baseresponse.setResponseCode(IHRMSConstants.successCode);
									baseresponse.setResponseMessage(IHRMSConstants.addedsuccessMessage);
									return HRMSHelper.createJsonString(baseresponse);
								} else {
									throw new HRMSException(IHRMSConstants.errorWhileAddingDataCode,
											IHRMSConstants.errorWhileAddingDataMessage);

								}
							}
						}
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}

				} else {
					throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
							IHRMSConstants.CandidateDoesnotExistMessage);
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

	@RequestMapping(method = { RequestMethod.DELETE,
			RequestMethod.PUT }, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String deleteCandidateNominationdetails(@PathVariable("id") long candidateStatutoryNominationId) {
		try {
			if (candidateStatutoryNominationId > 0) {

				candidateStatutoryNominationDAO.deleteById(candidateStatutoryNominationId);
				baseresponse.setResponseCode(IHRMSConstants.successCode);
				baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage);
				return HRMSHelper.createJsonString(baseresponse);
				/*
				 * CandidateStatutoryNomination candidateStatutoryNominationrequest=
				 * candidateStatutoryNominationDAO.getOne(request.getId());
				 * candidateStatutoryNominationrequest.setIsActive(IHRMSConstants.isNotActive);
				 * 
				 * 
				 * CandidateStatutoryNomination candidateStatutoryNomination=
				 * candidateStatutoryNominationDAO.save(candidateStatutoryNominationrequest);
				 * 
				 * if(candidateStatutoryNomination!=null) {
				 * baseresponse.setResponseCode(IHRMSConstants.successCode);
				 * baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage); return
				 * HRMSHelper.createJsonString(baseresponse); } else { return
				 * HRMSHelper.sendErrorResponse(IHRMSConstants.errorWhileDeletingDataMessage,
				 * IHRMSConstants.errorWhileDeletingDataCode); }
				 */

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

	/*
	 * @RequestMapping(method= RequestMethod.GET,produces="application/json")
	 * 
	 * @ResponseBody public String findAllCandidateNominationDetails() { try {
	 * List<CandidateStatutoryNomination>
	 * candidateStatutoryNominationEntityList=candidateStatutoryNominationDAO.
	 * finadallCandidateStatutoryNominationDetails(IHRMSConstants.isActive);
	 * //List<VOCandidateStatutoryNomination>vocanidateStatutoryNominationList=
	 * HRMSResponseTranslator.convertToVoCandidateStatutoryNominationList(
	 * candidateStatutoryNominationEntityList); //return
	 * HRMSHelper.createJsonString(vocanidateStatutoryNominationList);
	 * 
	 * HRMSListResponseObject response =new HRMSListResponseObject(); List <Object>
	 * objectList = new ArrayList<Object>(); for (CandidateStatutoryNomination
	 * candidateStatutoryNominationEntity : candidateStatutoryNominationEntityList)
	 * { VOCandidateStatutoryNomination voCandidateStatutoryNomination=
	 * HRMSEntityToModelMapper.convertToCandidateStatutoryNominationDetailModel(
	 * candidateStatutoryNominationEntity);
	 * objectList.add(voCandidateStatutoryNomination); }
	 * response.setListResponse(objectList);
	 * response.setResponseCode(IHRMSConstants.successCode);
	 * response.setResponseMessage(IHRMSConstants.successMessage);
	 * 
	 * return HRMSHelper.createJsonString(response);
	 * 
	 * 
	 * }catch(Exception e) { e.printStackTrace(); try { return
	 * HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage,
	 * IHRMSConstants.UnknowErrorCode); } catch (IOException e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace(); }; } return null; }
	 */

	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String findAllCandidateNominationDetailsbyId(@PathVariable("id") long candidateId) {
		try {
			if (candidateId > 0) {
				Candidate candidate = candidateDAO.findById(candidateId).get();
				if (!HRMSHelper.isNullOrEmpty(candidate)) {

					// CandidateFamilyDetail
					// candidateFamilyDetailEntity=HRMSRequestTranslator.convertTOCandidateFamilyDetailEntity(candidateFamilyDetail);
					CandidatePersonalDetail candidatePersonalDetail = candidatePersonalDetailDAO
							.findBycandidate(candidate);
					if (candidatePersonalDetail != null) {
						List<CandidateStatutoryNomination> candidateStatutoryNominationEntityList = candidateStatutoryNominationDAO
								.finadallCandidateStatutoryNominationDetailsbyId(IHRMSConstants.isActive,
										candidatePersonalDetail.getId());
						// List<VOCandidateStatutoryNomination>vocanidateStatutoryNominationList=HRMSResponseTranslator.convertToVoCandidateStatutoryNominationList(candidateStatutoryNominationEntityList);
						// return HRMSHelper.createJsonString(vocanidateStatutoryNominationList);
						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> objectList = new ArrayList<Object>();
						for (CandidateStatutoryNomination candidateStatutoryNominationEntity : candidateStatutoryNominationEntityList) {
							VOCandidateStatutoryNomination voCandidateStatutoryNomination = HRMSEntityToModelMapper
									.convertToCandidateStatutoryNominationDetailModel(
											candidateStatutoryNominationEntity);
							objectList.add(voCandidateStatutoryNomination);
						}
						response.setListResponse(objectList);
						response.setResponseCode(IHRMSConstants.successCode);
						response.setResponseMessage(IHRMSConstants.successMessage);

						return HRMSHelper.createJsonString(response);
					}
				} else {
					throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
							IHRMSConstants.CandidateDoesnotExistMessage);
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