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
import com.vinsys.hrms.dao.IHRMSCandidateLanguageDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.dao.IHRMSMasterLanguageDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateLanguage;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateLanguage;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.MasterLanguage;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping("/language")

public class CandidateKnownLanguageService {

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO;

	@Autowired
	IHRMSCandidateLanguageDAO candidateKnownLanguageDAO;
	BaseId baseresponse = new BaseId();

	@Autowired
	IHRMSMasterLanguageDAO masterLanguageDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addCandidateLanguagedetails(@RequestBody VOCandidateLanguage vocandidateLanguage) {
		try {
			if (!HRMSHelper.isNullOrEmpty(vocandidateLanguage)
					&& !HRMSHelper.isNullOrEmpty(vocandidateLanguage.getCandidatePersonalDetail())
					&& !HRMSHelper.isNullOrEmpty(vocandidateLanguage.getCandidatePersonalDetail().getCandidate())) {
				MasterLanguage mstLangEntity = masterLanguageDAO.findById(vocandidateLanguage.getLanguage().getId())
						.get();
				if (mstLangEntity != null) {
					Candidate candidateEntity = candidateDAO
							.getOne(vocandidateLanguage.getCandidatePersonalDetail().getCandidate().getId());
					if (!HRMSHelper.isNullOrEmpty(candidateEntity)) {
						CandidatePersonalDetail candidatePersonalDetailEntity = candidatePersonalDetailDAO
								.findBycandidate(candidateEntity);

						if (candidatePersonalDetailEntity == null) {
							candidatePersonalDetailEntity = new CandidatePersonalDetail();
							candidatePersonalDetailEntity.setCandidate(candidateEntity);
							candidatePersonalDetailEntity = candidatePersonalDetailDAO
									.save(candidatePersonalDetailEntity);
						}

						if (!HRMSHelper.isNullOrEmpty(candidatePersonalDetailEntity)) {
							int countMotherTongue = 0;
							boolean flag = false;
							if (vocandidateLanguage.getMotherTongue()
									.equalsIgnoreCase(IHRMSConstants.CANDIDATE_LANGUAGE_CONST_YES)) {
								countMotherTongue = getCountMotherTongueCandPersDetailswise(
										candidatePersonalDetailEntity.getId());
								if (countMotherTongue == 0) {
									flag = true;
								} else {
									flag = false;
									throw new HRMSException(IHRMSConstants.CANDIDATE_UNIQUE_MOTHER_TONGUE_ERROR_CODE,
											IHRMSConstants.CANDIDATE_UNIQUE_MOTHER_TONGUE_ERROR_MESSAGE);
								}
							} else {
								flag = true;
							}

							if (flag) {
								candidatePersonalDetailEntity.setCandidate(candidateEntity);
								MasterLanguage masterLanguageEntity = masterLanguageDAO
										.findById(vocandidateLanguage.getLanguage().getId()).get();
								if (vocandidateLanguage.getId() > 0) {
									CandidateLanguage candidateLanguageEntity = candidateKnownLanguageDAO
											.findById(vocandidateLanguage.getId()).get();
									CandidateLanguage candidateLanguageEntityResp = HRMSRequestTranslator
											.convertToUpdateCandidateLanguageEntity(candidateLanguageEntity,
													vocandidateLanguage);
									candidateLanguageEntityResp
											.setCandidatePersonalDetail(candidatePersonalDetailEntity);
									candidateLanguageEntityResp.setLanguage(masterLanguageEntity);
									candidateKnownLanguageDAO.save(candidateLanguageEntityResp);
									if (candidateLanguageEntityResp != null) {
										baseresponse.setId(candidateLanguageEntityResp.getId());
										baseresponse.setResponseCode(IHRMSConstants.successCode);
										baseresponse.setResponseMessage(IHRMSConstants.updatedsuccessMessage);
										return HRMSHelper.createJsonString(baseresponse);
									}
								} else {
									CandidateLanguage candidateLanguageEntity = HRMSRequestTranslator
											.convertToCandidateLanguageEntity(vocandidateLanguage);

									candidateLanguageEntity.setCandidatePersonalDetail(candidatePersonalDetailEntity);
									// candidateLanguageEntity.setIsActive(IHRMSConstants.isActive);
									candidateLanguageEntity.setLanguage(masterLanguageEntity);
									CandidateLanguage candidateLanguageresp = candidateKnownLanguageDAO
											.save(candidateLanguageEntity);
									if (candidateLanguageresp != null) {
										baseresponse.setId(candidateLanguageresp.getId());
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
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.DataNotFoundMessage);
						}

					} else {
						throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
								IHRMSConstants.CandidateDoesnotExistMessage);
					}
				} else {
					throw new HRMSException(IHRMSConstants.InvalidSelectedLanguageErrorCode,
							IHRMSConstants.InvalidSelectedLanguageErrorMessage);
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

	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String deleteCandidateKnownLanguagesdetails(@PathVariable("id") long candidateLanguageId) {
		try {
			if (candidateLanguageId > 0) {

				candidateKnownLanguageDAO.deleteById(candidateLanguageId);
				baseresponse.setResponseCode(IHRMSConstants.successCode);
				baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage);
				return HRMSHelper.createJsonString(baseresponse);
				// CandidateLanguage candidateLanguagerequest=
				// candidateKnownLanguageDAO.getOne(request.getId());
				// candidateLanguagerequest.setIsActive(IHRMSConstants.isNotActive);

				// CandidateLanguage candidateLanguage=
				// candidateKnownLanguageDAO.save(candidateLanguagerequest);
				// candidateKnownLanguageDAO.delete(request.getId());
				/*
				 * if(candidateLanguage!=null) {
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
	 * @ResponseBody public String findAllCandidateLanguagedetails() { try {
	 * List<CandidateLanguage>
	 * candidateLangugaeEntityList=candidateKnownLanguageDAO.
	 * findallCandidateLanguage(IHRMSConstants.isActive);
	 * //List<VOCandidateLanguage>vocanidateLanguageList=HRMSResponseTranslator.
	 * convertToVoCandidateLanguageList(candidateLangugaeEntityList); //return
	 * HRMSHelper.createJsonString(vocanidateLanguageList);
	 * 
	 * HRMSListResponseObject response =new HRMSListResponseObject(); List <Object>
	 * objectList = new ArrayList<Object>(); for (CandidateLanguage
	 * candidateLanguageEntity : candidateLangugaeEntityList) { VOCandidateLanguage
	 * voCandidateLanguage= HRMSEntityToModelMapper.convertToCandidateLanguageModel(
	 * candidateLanguageEntity); objectList.add(voCandidateLanguage); }
	 * 
	 * //List<VOCandidatePolicyDetail>vocanidatePolicyDetailList=
	 * HRMSResponseTranslator.convertToVoCandidatePolicyDetailList(
	 * candidatePolicyDetailEntityList); //return
	 * HRMSHelper.createJsonString(vocanidatePolicyDetailList);
	 * response.setListResponse(objectList);
	 * response.setResponseCode(IHRMSConstants.successCode);
	 * response.setResponseMessage(IHRMSConstants.successMessage);
	 * 
	 * return HRMSHelper.createJsonString(response);
	 * 
	 * }catch(Exception e) { e.printStackTrace(); try { return
	 * HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage,
	 * IHRMSConstants.UnknowErrorCode); } catch (IOException e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace(); }; } return null; }
	 */

	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String findAllCandidateLanguagedetailsId(@PathVariable("id") long candidateId) {
		try {
			if (candidateId > 0) {
				Candidate candidate = candidateDAO.findById(candidateId).get();
				if (!HRMSHelper.isNullOrEmpty(candidate)) {

					// CandidateFamilyDetail
					// candidateFamilyDetailEntity=HRMSRequestTranslator.convertTOCandidateFamilyDetailEntity(candidateFamilyDetail);
					CandidatePersonalDetail candidatePersonalDetail = candidatePersonalDetailDAO
							.findBycandidate(candidate);
					if (candidatePersonalDetail != null) {
						List<CandidateLanguage> candidateLangugaeEntityList = candidateKnownLanguageDAO
								.findallCandidateLanguagebyId(IHRMSConstants.isActive, candidatePersonalDetail.getId(),
										IHRMSConstants.isActive);
						// List<VOCandidateLanguage>vocanidateLanguageList=HRMSResponseTranslator.convertToVoCandidateLanguageList(candidateLangugaeEntityList);
						// return HRMSHelper.createJsonString(vocanidateLanguageList);

						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> objectList = new ArrayList<Object>();
						for (CandidateLanguage candidateLanguageEntity : candidateLangugaeEntityList) {
							VOCandidateLanguage voCandidateLanguage = HRMSEntityToModelMapper
									.convertToCandidateLanguageModel(candidateLanguageEntity);
							objectList.add(voCandidateLanguage);
						}

						// List<VOCandidatePolicyDetail>vocanidatePolicyDetailList=HRMSResponseTranslator.convertToVoCandidatePolicyDetailList(candidatePolicyDetailEntityList);
						// return HRMSHelper.createJsonString(vocanidatePolicyDetailList);
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

	private int getCountMotherTongueCandPersDetailswise(long candPersonalId) {
		int result = 0;
		if (!HRMSHelper.isLongZero(candPersonalId)) {
			result = candidateKnownLanguageDAO.getCountMotherTongueCandPersDetailswise(IHRMSConstants.isActive,
					candPersonalId, IHRMSConstants.CANDIDATE_LANGUAGE_CONST_YES);
		}
		return result;
	}
}
