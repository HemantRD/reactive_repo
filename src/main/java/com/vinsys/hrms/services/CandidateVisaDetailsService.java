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
import com.vinsys.hrms.dao.IHRMSCandidateVisaDetailsDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateVisaDetail;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidateVisaDetail;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping("/candidateVisa")

public class CandidateVisaDetailsService {

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO;
	@Autowired
	IHRMSCandidateVisaDetailsDAO candidateVisaDetailsDAO;

	BaseId baseresponse = new BaseId();

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addCandidateVisaDetails(@RequestBody VOCandidateVisaDetail vocandidateVisaDetail) {
		try {

			if (!HRMSHelper.isNullOrEmpty(vocandidateVisaDetail)
					&& !HRMSHelper.isNullOrEmpty(vocandidateVisaDetail.getCandidatePersonalDetail())
					&& !HRMSHelper.isNullOrEmpty(vocandidateVisaDetail.getCandidatePersonalDetail().getCandidate())) {
				//Monika
				if(!HRMSHelper.isNullOrEmpty(vocandidateVisaDetail.getVisaType())&&
						(!HRMSHelper.isNullOrEmpty(vocandidateVisaDetail.getVisaNumber()))&&
						(!HRMSHelper.isNullOrEmpty(vocandidateVisaDetail.getCountry()))&&
						(!HRMSHelper.isNullOrEmpty(vocandidateVisaDetail.getDateOfIssue()))&&
						(!HRMSHelper.isNullOrEmpty(vocandidateVisaDetail.getDateOfExpiry()))
						) {
				
				Candidate candidateEntity = candidateDAO
						.getOne(vocandidateVisaDetail.getCandidatePersonalDetail().getCandidate().getId());
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

						if (vocandidateVisaDetail.getId() > 0) {

							CandidateVisaDetail candidateVisaDetailEntity = candidateVisaDetailsDAO
									.findById(vocandidateVisaDetail.getId()).get();
							CandidateVisaDetail candidateVisaDetailEntityResp = HRMSRequestTranslator
									.convertToUpdateCandidateVisaDetailEntity(candidateVisaDetailEntity,
											vocandidateVisaDetail);
							candidateVisaDetailEntityResp.setCandidatePersonalDetail(candidatePersonalDetailEntity);
							candidateVisaDetailsDAO.save(candidateVisaDetailEntityResp);
							if (candidateVisaDetailEntityResp != null) {
								baseresponse.setId(candidateVisaDetailEntityResp.getId());
								baseresponse.setResponseCode(IHRMSConstants.successCode);
								baseresponse.setResponseMessage(IHRMSConstants.updatedsuccessMessage);
								return HRMSHelper.createJsonString(baseresponse);
							}
						} else {
							CandidateVisaDetail candidateVisaDetailEntity = HRMSRequestTranslator
									.convertToCandidateVisaDetailEntity(vocandidateVisaDetail);

							candidateVisaDetailEntity.setCandidatePersonalDetail(candidatePersonalDetailEntity);
							//candidateVisaDetailEntity.setIsActive(IHRMSConstants.isActive);
							CandidateVisaDetail candidateVisaDetailresp = candidateVisaDetailsDAO
									.save(candidateVisaDetailEntity);
							if (candidateVisaDetailresp != null) {
								baseresponse.setId(candidateVisaDetailresp.getId());
								baseresponse.setResponseCode(IHRMSConstants.successCode);
								baseresponse.setResponseMessage(IHRMSConstants.addedsuccessMessage);
								return HRMSHelper.createJsonString(baseresponse);
							} else {
								throw new HRMSException(IHRMSConstants.errorWhileAddingDataCode,
										IHRMSConstants.errorWhileAddingDataMessage);

							}
						}
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}

				} else {
					throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
							IHRMSConstants.CandidateDoesnotExistMessage);
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

	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String deleteCandidateVisadetails(@PathVariable("id") long candidateVisaId) {
		try {
			if (candidateVisaId > 0) {

				candidateVisaDetailsDAO.deleteById(candidateVisaId);  
				baseresponse.setResponseCode(IHRMSConstants.successCode);
				baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage);
				return HRMSHelper.createJsonString(baseresponse);

				/*
				 * CandidateVisaDetail candidateVisaDetailrequest=
				 * candidateVisaDetailsDAO.getOne(request.getId());
				 * candidateVisaDetailrequest.setIsActive(IHRMSConstants.isNotActive);
				 * 
				 * 
				 * CandidateVisaDetail candidateVisaDetail=
				 * candidateVisaDetailsDAO.save(candidateVisaDetailrequest);
				 * 
				 * if(candidateVisaDetail!=null) {
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
	 * @ResponseBody public String findAllCandidateVisaDetails() { try {
	 * List<CandidateVisaDetail>
	 * candidateVisaDetailEntityList=candidateVisaDetailsDAO.
	 * findallCandidateVisaDetails(IHRMSConstants.isActive);
	 * //List<VOCandidateVisaDetail>vocanidateVisaDetailList=HRMSResponseTranslator.
	 * convertToVoCandidateVisaDetailList(candidateVisaDetailEntityList); //return
	 * HRMSHelper.createJsonString(vocanidateVisaDetailList); HRMSListResponseObject
	 * response =new HRMSListResponseObject(); List <Object> objectList = new
	 * ArrayList<Object>(); for (CandidateVisaDetail candidateVisaDetailEntity :
	 * candidateVisaDetailEntityList) { VOCandidateVisaDetail voCandidateVisaDetail=
	 * HRMSEntityToModelMapper.convertToCandidateVisaDetailModel(
	 * candidateVisaDetailEntity); objectList.add(voCandidateVisaDetail); }
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
	public String findAllCandidateVisaDetailsbyId(@PathVariable("id") long candidateId) {
		try {
			if (candidateId > 0) {
				Candidate candidate = candidateDAO.findById(candidateId).get();
				if (!HRMSHelper.isNullOrEmpty(candidate)) {

					// CandidateFamilyDetail
					// candidateFamilyDetailEntity=HRMSRequestTranslator.convertTOCandidateFamilyDetailEntity(candidateFamilyDetail);
					CandidatePersonalDetail candidatePersonalDetail = candidatePersonalDetailDAO
							.findBycandidate(candidate);
					if (candidatePersonalDetail != null) {
						List<CandidateVisaDetail> candidateVisaDetailEntityList = candidateVisaDetailsDAO
								.findallCandidateVisaDetailsbyId(IHRMSConstants.isActive,
										candidatePersonalDetail.getId());
						// List<VOCandidateVisaDetail>vocanidateVisaDetailList=HRMSResponseTranslator.convertToVoCandidateVisaDetailList(candidateVisaDetailEntityList);
						// return HRMSHelper.createJsonString(vocanidateVisaDetailList);

						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> objectList = new ArrayList<Object>();
						for (CandidateVisaDetail candidateVisaDetailEntity : candidateVisaDetailEntityList) {
							VOCandidateVisaDetail voCandidateVisaDetail = HRMSEntityToModelMapper
									.convertToCandidateVisaDetailModel(candidateVisaDetailEntity);
							objectList.add(voCandidateVisaDetail);
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
