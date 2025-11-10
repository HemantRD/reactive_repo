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
import com.vinsys.hrms.dao.IHRMSCandidatePolicyDetailsDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidatePolicyDetail;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidatePolicyDetail;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping("/candidatePolicy")

public class CandidatePolicyDetailsService {

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO;
	@Autowired
	IHRMSCandidatePolicyDetailsDAO candidatePolicyDetailsDAO;

	BaseId baseresponse = new BaseId();

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addCandidatePolicyDetails(@RequestBody VOCandidatePolicyDetail vocandidatePolicyDetail) {
		try {

			if (!HRMSHelper.isNullOrEmpty(vocandidatePolicyDetail)
					&& !HRMSHelper.isNullOrEmpty(vocandidatePolicyDetail.getCandidatePersonalDetail())
					&& !HRMSHelper.isNullOrEmpty(vocandidatePolicyDetail.getCandidatePersonalDetail().getCandidate())) {
				Candidate candidateEntity = candidateDAO
						.getOne(vocandidatePolicyDetail.getCandidatePersonalDetail().getCandidate().getId());
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

						if (vocandidatePolicyDetail.getId() > 0) {

							CandidatePolicyDetail candidatePolicyDetailEntity = candidatePolicyDetailsDAO
									.findById(vocandidatePolicyDetail.getId()).get();
							CandidatePolicyDetail candidatePolicyDetailEntityResp = HRMSRequestTranslator
									.convertToUpdateCandidatePolicyDetailEntity(candidatePolicyDetailEntity,
											vocandidatePolicyDetail);
							candidatePolicyDetailEntityResp.setCandidatePersonalDetail(candidatePersonalDetailEntity);
							candidatePolicyDetailsDAO.save(candidatePolicyDetailEntityResp);
							if (candidatePolicyDetailEntityResp != null) {
								baseresponse.setId(candidatePolicyDetailEntityResp.getId());
								baseresponse.setResponseCode(IHRMSConstants.successCode);
								baseresponse.setResponseMessage(IHRMSConstants.updatedsuccessMessage);
								return HRMSHelper.createJsonString(baseresponse);
							}
						} else {
							CandidatePolicyDetail tocheckCandidatePolicyExist = candidatePolicyDetailsDAO
									.findBycandidatePersonalDetail(candidatePersonalDetailEntity);
							if (!HRMSHelper.isNullOrEmpty(tocheckCandidatePolicyExist)) {
								throw new HRMSException(IHRMSConstants.CandidateDataAlreadyExistCode,
										IHRMSConstants.CandidateDataAlreadyExistMessage);
							} else {
								CandidatePolicyDetail candidatePolicyDetailEntity = HRMSRequestTranslator
										.convertToCandidatePolicyDetailEntity(vocandidatePolicyDetail);

								candidatePolicyDetailEntity.setCandidatePersonalDetail(candidatePersonalDetailEntity);
								//candidatePolicyDetailEntity.setIsActive(IHRMSConstants.isActive);
								CandidatePolicyDetail candidatePolicyDetailresp = candidatePolicyDetailsDAO
										.save(candidatePolicyDetailEntity);
								if (candidatePolicyDetailresp != null) {
									baseresponse.setId(candidatePolicyDetailresp.getId());
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

	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String deleteCandidatePolicydetails(@PathVariable("id") long candidatePolicyId) {
		try {
			if (candidatePolicyId > 0) {
				candidatePolicyDetailsDAO.deleteById(candidatePolicyId);
				baseresponse.setResponseCode(IHRMSConstants.successCode);
				baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage);
				return HRMSHelper.createJsonString(baseresponse);

				/*
				 * CandidatePolicyDetail candidatePolicyDetailrequest=
				 * candidatePolicyDetailsDAO.getOne(request.getId());
				 * candidatePolicyDetailrequest.setIsActive(IHRMSConstants.isNotActive);
				 * 
				 * 
				 * CandidatePolicyDetail candidatePolicyDetail=
				 * candidatePolicyDetailsDAO.save(candidatePolicyDetailrequest);
				 * 
				 * if(candidatePolicyDetail!=null) {
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
	 * @ResponseBody public String findAllCandidatePolicyDetails() { try {
	 * 
	 * List<CandidatePolicyDetail>
	 * candidatePolicyDetailEntityList=candidatePolicyDetailsDAO.
	 * finadallCandidatePolicyDetails(IHRMSConstants.isActive);
	 * HRMSListResponseObject response =new HRMSListResponseObject(); List <Object>
	 * objectList = new ArrayList<Object>(); for (CandidatePolicyDetail
	 * CandidatePolicyDetailEntity : candidatePolicyDetailEntityList) {
	 * VOCandidatePolicyDetail voCandidatePolicyDetail=
	 * HRMSEntityToModelMapper.convertToCandidatePolicyDetailModel(
	 * CandidatePolicyDetailEntity); objectList.add(voCandidatePolicyDetail); }
	 * //List<Object>vocanidatePolicyDetailList=
	 * HRMSResponseTranslator.convertToVoCandidatePolicyDetailList(
	 * candidatePolicyDetailEntityList); response.setListResponse(objectList);
	 * response.setResponseCode(IHRMSConstants.successCode);
	 * response.setResponseMessage(IHRMSConstants.successMessage);
	 * 
	 * return HRMSHelper.createJsonString(response);
	 * 
	 * 
	 * 
	 * }catch(Exception e) { e.printStackTrace(); try { return
	 * HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage,
	 * IHRMSConstants.UnknowErrorCode); } catch (IOException e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace(); }; } return null; }
	 */

	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String findAllCandidatePolicyDetailsbyId(@PathVariable("id") long candidateId) {
		try {
			if (candidateId > 0) {
				Candidate candidate = candidateDAO.findById(candidateId).get();
				if (!HRMSHelper.isNullOrEmpty(candidate)) {

					// CandidateFamilyDetail
					// candidateFamilyDetailEntity=HRMSRequestTranslator.convertTOCandidateFamilyDetailEntity(candidateFamilyDetail);
					CandidatePersonalDetail candidatePersonalDetail = candidatePersonalDetailDAO
							.findBycandidate(candidate);
					if (candidatePersonalDetail != null) {
						List<CandidatePolicyDetail> candidatePolicyDetailEntityList = candidatePolicyDetailsDAO
								.findallCandidatePolicyDetailsbyId(IHRMSConstants.isActive,
										candidatePersonalDetail.getId());
						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> objectList = new ArrayList<Object>();
						for (CandidatePolicyDetail CandidatePolicyDetailEntity : candidatePolicyDetailEntityList) {
							VOCandidatePolicyDetail voCandidatePolicyDetail = HRMSEntityToModelMapper
									.convertToCandidatePolicyDetailModel(CandidatePolicyDetailEntity);
							objectList.add(voCandidatePolicyDetail);
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
}