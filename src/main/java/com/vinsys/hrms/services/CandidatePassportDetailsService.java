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
import com.vinsys.hrms.dao.IHRMSCandidatePassportDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidatePassportDetail;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidatePassportDetail;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping("/candidatePassport")

public class CandidatePassportDetailsService {

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO;
	@Autowired
	IHRMSCandidatePassportDetailsDAO candidatePassportDetailsDAO;

	BaseId baseresponse = new BaseId();

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addCandidatePassportDetails(@RequestBody VOCandidatePassportDetail vocandidatePassportDetail) {
		try {

			if (!HRMSHelper.isNullOrEmpty(vocandidatePassportDetail)
					&& !HRMSHelper.isNullOrEmpty(vocandidatePassportDetail.getCandidatePersonalDetail()) && !HRMSHelper
							.isNullOrEmpty(vocandidatePassportDetail.getCandidatePersonalDetail().getCandidate())) {
				//monika
				if(!HRMSHelper.isNullOrEmpty(vocandidatePassportDetail.getPassportFirstName())&&
						(!HRMSHelper.isNullOrEmpty(vocandidatePassportDetail.getPassportLastName()))&&
						(!HRMSHelper.isNullOrEmpty(vocandidatePassportDetail.getDateOfExpiry()))&&
						(!HRMSHelper.isNullOrEmpty(vocandidatePassportDetail.getDateOfIssue()))&&
						(!HRMSHelper.isNullOrEmpty(vocandidatePassportDetail.getPassportNumber()))&&
						(!HRMSHelper.isNullOrEmpty(vocandidatePassportDetail.getPlaceOfIssue()))
						
						) {
				
				
				Candidate candidateEntity = candidateDAO
						.getOne(vocandidatePassportDetail.getCandidatePersonalDetail().getCandidate().getId());
				if (!HRMSHelper.isNullOrEmpty(candidateEntity)) {

					CandidatePersonalDetail candidatePersonalDetailEntity = candidatePersonalDetailDAO
							.findBycandidate(candidateEntity);
					
					if(candidatePersonalDetailEntity == null) {
						candidatePersonalDetailEntity = new CandidatePersonalDetail();
						candidatePersonalDetailEntity.setCandidate(candidateEntity);
						candidatePersonalDetailEntity = candidatePersonalDetailDAO.save(candidatePersonalDetailEntity);
					}
					
					if (!HRMSHelper.isNullOrEmpty(candidatePersonalDetailEntity)) {

						candidatePersonalDetailEntity.setCandidate(candidateEntity);
						if (vocandidatePassportDetail.getId() > 0) {
							CandidatePassportDetail candidatePassportDetailEntity = candidatePassportDetailsDAO
									.findById(vocandidatePassportDetail.getId()).get();
							CandidatePassportDetail candidatePassportDetailEntityResp = HRMSRequestTranslator
									.convertToUpdateCandidatePassportDetailEntity(candidatePassportDetailEntity,
											vocandidatePassportDetail);
							candidatePassportDetailEntityResp.setCandidatePersonalDetail(candidatePersonalDetailEntity);
							candidatePassportDetailsDAO.save(candidatePassportDetailEntityResp);
							if (candidatePassportDetailEntityResp != null) {
								baseresponse.setId(candidatePassportDetailEntityResp.getId());
								baseresponse.setResponseCode(IHRMSConstants.successCode);
								baseresponse.setResponseMessage(IHRMSConstants.updatedsuccessMessage);
								return HRMSHelper.createJsonString(baseresponse);
							}

						} else {
							CandidatePassportDetail tocheckCandidatePassportDetailExist = candidatePassportDetailsDAO
									.findBycandidatePersonalDetail(candidatePersonalDetailEntity);
							if (tocheckCandidatePassportDetailExist != null) {
								throw new HRMSException(IHRMSConstants.CandidateDataAlreadyExistCode,
										IHRMSConstants.CandidateDataAlreadyExistMessage);
							} else {

								CandidatePassportDetail candidatePassportDetailEntity = HRMSRequestTranslator
										.convertoCandidatePassportDetailEntity(vocandidatePassportDetail);

								candidatePassportDetailEntity.setCandidatePersonalDetail(candidatePersonalDetailEntity);
								//candidatePassportDetailEntity.setIsActive(IHRMSConstants.isActive);

								CandidatePassportDetail candidatePassportDetailresp = candidatePassportDetailsDAO
										.save(candidatePassportDetailEntity);
								if (candidatePassportDetailresp != null) {
									baseresponse.setId(candidatePassportDetailresp.getId());
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
				}
				//monika
				else {
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

	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String deleteCandidatePassportdetails(@PathVariable("id") long candidatePassportId) {
		try {
			if (candidatePassportId > 0) {

				candidatePassportDetailsDAO.deleteById(candidatePassportId);
				baseresponse.setResponseCode(IHRMSConstants.successCode);
				baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage);
				return HRMSHelper.createJsonString(baseresponse);
				/*
				 * CandidatePassportDetail candidatePassportDetailrequest=
				 * candidatePassportDetailsDAO.getOne(request.getId());
				 * candidatePassportDetailrequest.setIsActive(IHRMSConstants.isNotActive);
				 * 
				 * 
				 * CandidatePassportDetail candidatePassportDetail=
				 * candidatePassportDetailsDAO.save(candidatePassportDetailrequest);
				 * 
				 * if(candidatePassportDetail!=null) {
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
	 * @ResponseBody public String findAllCandidatePassportdetails() { try {
	 * List<CandidatePassportDetail>
	 * candidatePassportDetailEntityList=candidatePassportDetailsDAO.
	 * findallCandidatePassportDetails(IHRMSConstants.isActive);
	 * //List<VOCandidatePassportDetail>vocanidatePassportDetailList=
	 * HRMSResponseTranslator.convertToVoCandidatePassportDetailList(
	 * candidatePassportDetailEntityList); //return
	 * HRMSHelper.createJsonString(vocanidatePassportDetailList);
	 * HRMSListResponseObject response =new HRMSListResponseObject(); List <Object>
	 * objectList = new ArrayList<Object>(); for (CandidatePassportDetail
	 * candidatePassportDetailEntity : candidatePassportDetailEntityList) {
	 * VOCandidatePassportDetail voCandidateStatutoryNomination=
	 * HRMSEntityToModelMapper.convertToPassportDetailModel(
	 * candidatePassportDetailEntity);
	 * objectList.add(voCandidateStatutoryNomination); }
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
	public String findAllCandidatePassportdetailsbyId(@PathVariable("id") long candidateId) {
		try {
			if (candidateId > 0) {
				Candidate candidate = candidateDAO.findById(candidateId).get();
				if (!HRMSHelper.isNullOrEmpty(candidate)) {

					// CandidateFamilyDetail
					// candidateFamilyDetailEntity=HRMSRequestTranslator.convertTOCandidateFamilyDetailEntity(candidateFamilyDetail);
					CandidatePersonalDetail candidatePersonalDetail = candidatePersonalDetailDAO
							.findBycandidate(candidate);
					if (candidatePersonalDetail != null) {
						List<CandidatePassportDetail> candidatePassportDetailEntityList = candidatePassportDetailsDAO
								.findallCandidatePassportDetailsbyId(IHRMSConstants.isActive,
										candidatePersonalDetail.getId());
						// List<VOCandidatePassportDetail>vocanidatePassportDetailList=HRMSResponseTranslator.convertToVoCandidatePassportDetailList(candidatePassportDetailEntityList);
						// return HRMSHelper.createJsonString(vocanidatePassportDetailList);

						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> objectList = new ArrayList<Object>();
						for (CandidatePassportDetail candidatePassportDetailEntity : candidatePassportDetailEntityList) {
							VOCandidatePassportDetail voCandidateStatutoryNomination = HRMSEntityToModelMapper
									.convertToPassportDetailModel(candidatePassportDetailEntity);
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
