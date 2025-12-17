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
import com.vinsys.hrms.dao.IHRMSCandidateFamilyAddressDAO;
import com.vinsys.hrms.dao.IHRMSCandidateFamilyDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateFamilyDetail;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateFamilyAddress;
import com.vinsys.hrms.entity.CandidateFamilyDetail;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping("/candidateFamilyDetails")

public class CandidateFamilyDetailsService {

	@Autowired
	IHRMSCandidateFamilyDetailsDAO addFamilyDetailsDAO;

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO;
	@Autowired
	IHRMSCandidateFamilyAddressDAO candidateFamilyAddressDAO;

	@Autowired
	IHRMSMasterCityDAO masterCityDAO;

	@Autowired
	IHRMSMasterStateDAO masterStateDAO;

	@Autowired
	IHRMSMasterCountryDAO masterCountryDAO;

	BaseId baseresponse = new BaseId();

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String addCandidateFamilydetails(@RequestBody VOCandidateFamilyDetail vocandidateFamilyDetail) {
		try {
			if (!HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail)
					&& !HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getCandidatePersonalDetail())
					&& !HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getCandidatePersonalDetail().getCandidate())) {

				// Added By Monika.
				if (!HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getFirst_name())
//						&& !HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getMiddle_name())
						//&& !HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getLast_name())
						&& !HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getGender()) && !HRMSHelper.isNullOrEmpty(
								vocandidateFamilyDetail.getRelationship()) /*
																			 * && !HRMSHelper
																			 * .isNullOrEmpty(vocandidateFamilyDetail.
																			 * getCandidateFamilyAddress(
																			 * ).getAddressLine1()) && //
																			 * !HRMSHelper.isNullOrEmpty(
																			 * vocandidateFamilyDetail.
																			 * getCandidateFamilyAddress().getCountry())
																			 * && !HRMSHelper.isNullOrEmpty(
																			 * vocandidateFamilyDetail.
																			 * getCandidateFamilyAddress().getPincode())
																			 */
						&& !HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getContactNo1())
						&& !HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getDependent())
						&& !HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getOccupation())
						&& HRMSHelper.regexMatcher(vocandidateFamilyDetail.getContactNo1(),
								"^\\d{10,15}$")
				// !HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getCandidateFamilyAddress().getCity())&&
				// !HRMSHelper.isNullOrEmpty(vocandidateFamilyDetail.getCandidateFamilyAddress().getState())

				) {

					Candidate candidateEntity = candidateDAO
							.findById(vocandidateFamilyDetail.getCandidatePersonalDetail().getCandidate().getId()).get();
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
							candidatePersonalDetailEntity.setCandidate(candidateEntity);
							CandidateFamilyDetail candidateFamilyDetailEntity = new CandidateFamilyDetail();
							CandidateFamilyDetail candidateFamilyDetailEntityresp = new CandidateFamilyDetail();
							if (vocandidateFamilyDetail.getId() > 0) {
								candidateFamilyDetailEntity = addFamilyDetailsDAO
										.findById(vocandidateFamilyDetail.getId()).get();
								MasterCity masterCity = masterCityDAO
										.findById(vocandidateFamilyDetail.getCandidateFamilyAddress().getCity().getId()).get();
								MasterState masterState = masterStateDAO.findById(
										vocandidateFamilyDetail.getCandidateFamilyAddress().getState().getId()).get();
								MasterCountry masterCountry = masterCountryDAO.findById(
										vocandidateFamilyDetail.getCandidateFamilyAddress().getCountry().getId()).get();

								candidateFamilyDetailEntityresp = HRMSRequestTranslator
										.convertToUpdateCandidateFamilyDetailEntity(candidateFamilyDetailEntity,
												vocandidateFamilyDetail);
								candidateFamilyDetailEntityresp.getCandidateFamilyAddress().setCity(masterCity);
								candidateFamilyDetailEntityresp.getCandidateFamilyAddress().setState(masterState);
								candidateFamilyDetailEntityresp.getCandidateFamilyAddress().setCountry(masterCountry);
								candidateFamilyDetailEntityresp
										.setCandidatePersonalDetail(candidatePersonalDetailEntity);

								List<CandidateFamilyDetail> candidateFamily = addFamilyDetailsDAO.findAll();
								boolean hasEmergencyContact = candidateFamily.stream()
										.anyMatch(detail -> !HRMSHelper.isNullOrEmpty(detail.getIsEmergencyContact()));

								if (vocandidateFamilyDetail.getIsEmergencyContact()
										.equalsIgnoreCase(IHRMSConstants.isEmergencyContact)) {

									if (hasEmergencyContact) {
										throw new HRMSException("Emergency Contact Already Exists!");
									} else {

										candidateFamilyDetailEntity
												.setIsEmergencyContact(vocandidateFamilyDetail.getIsEmergencyContact());
									}
								} /*
									 * else { candidateFamilyDetailEntity
									 * .setIsEmergencyContact(IHRMSConstants.isNotEmergencyContact); }
									 */

								addFamilyDetailsDAO.save(candidateFamilyDetailEntityresp);
								if (candidateFamilyDetailEntityresp != null) {
									/*
									 * baseresponse.setId(candidateFamilyDetailEntityresp.getId());
									 * baseresponse.setResponseCode(IHRMSConstants.successCode);
									 * baseresponse.setResponseMessage(IHRMSConstants.updatedsuccessMessage); return
									 * HRMSHelper.createJsonString(baseresponse);
									 */
									HRMSListResponseObject response = new HRMSListResponseObject();
									List<Object> objectList = new ArrayList<Object>();
									VOCandidateFamilyDetail voCandidateFamilyDetail = HRMSEntityToModelMapper
											.convertToFamilyDetailModel(candidateFamilyDetailEntityresp);
									objectList.add(voCandidateFamilyDetail);
									response.setListResponse(objectList);
									response.setResponseCode(IHRMSConstants.successCode);
									response.setResponseMessage(IHRMSConstants.updateSuccesMessage);
									return HRMSHelper.createJsonString(response);
								}

							} else {

								MasterCity cityEntity = masterCityDAO
										.findById(vocandidateFamilyDetail.getCandidateFamilyAddress().getCity().getId()).get();
								MasterState stateEntity = masterStateDAO.findById(
										vocandidateFamilyDetail.getCandidateFamilyAddress().getState().getId()).get();
								MasterCountry countryEntity = masterCountryDAO.findById(
										vocandidateFamilyDetail.getCandidateFamilyAddress().getCountry().getId()).get();
								candidateFamilyDetailEntity = HRMSRequestTranslator
										.convertTOCandidateFamilyDetailEntity(vocandidateFamilyDetail);

								candidateFamilyDetailEntity.getCandidateFamilyAddress().setCity(cityEntity);
								candidateFamilyDetailEntity.getCandidateFamilyAddress().setState(stateEntity);
								candidateFamilyDetailEntity.getCandidateFamilyAddress().setCountry(countryEntity);
								candidateFamilyDetailEntity.setCandidatePersonalDetail(candidatePersonalDetailEntity);

								// candidateFamilyDetailEntity.setIsActive(IHRMSConstants.isActive);

								List<CandidateFamilyDetail> candidateFamily = addFamilyDetailsDAO.findCandidateFamilyDetailsbyId(candidatePersonalDetailEntity.getId());
								boolean hasEmergencyContact = candidateFamily.stream()
										.anyMatch(detail -> !HRMSHelper.isNullOrEmpty(detail.getIsEmergencyContact()));

								if (vocandidateFamilyDetail.getIsEmergencyContact()
										.equalsIgnoreCase(IHRMSConstants.isEmergencyContact)) {

									if (hasEmergencyContact) {
										throw new HRMSException("Emergency Contact Already Exists!");
									} else {

										candidateFamilyDetailEntity
												.setIsEmergencyContact(vocandidateFamilyDetail.getIsEmergencyContact());
									}
								}/* else {
									candidateFamilyDetailEntity
											.setIsEmergencyContact(IHRMSConstants.isNotEmergencyContact);
								}*/

								candidateFamilyDetailEntityresp = addFamilyDetailsDAO.save(candidateFamilyDetailEntity);

								CandidateFamilyAddress addressEntity = candidateFamilyDetailEntity
										.getCandidateFamilyAddress();
								addressEntity.setCandidateFamilyDetail(candidateFamilyDetailEntityresp);
								candidateFamilyAddressDAO.save(addressEntity);

								if (candidateFamilyDetailEntityresp != null) {
									/*
									 * baseresponse.setId(candidateFamilyDetailEntityresp.getId());
									 * baseresponse.setResponseCode(IHRMSConstants.successCode);
									 * baseresponse.setResponseMessage(IHRMSConstants.addedsuccessMessage);
									 */
									HRMSListResponseObject response = new HRMSListResponseObject();
									List<Object> objectList = new ArrayList<Object>();
									VOCandidateFamilyDetail voCandidateFamilyDetail = HRMSEntityToModelMapper
											.convertToFamilyDetailModel(candidateFamilyDetailEntityresp);
									objectList.add(voCandidateFamilyDetail);
									response.setListResponse(objectList);
									response.setResponseCode(IHRMSConstants.successCode);
									response.setResponseMessage(IHRMSConstants.addedsuccessMessage);
									return HRMSHelper.createJsonString(response);
								} else {
									throw new HRMSException(IHRMSConstants.errorWhileAddingDataCode,
											IHRMSConstants.errorWhileAddingDataMessage);
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
					// monika
				} else {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
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
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String deleteCandidateFamilydetails(@PathVariable("id") long candidateFamilyDetailId) {
		try {
			if (candidateFamilyDetailId > 0) {
				CandidateFamilyDetail candidateFamilyDetailrequest = addFamilyDetailsDAO
						.getOne(candidateFamilyDetailId);

				CandidateFamilyAddress candidateFamilyAddressEntity = candidateFamilyAddressDAO
						.findBycandidateFamilyDetail(candidateFamilyDetailrequest);
				candidateFamilyAddressDAO.delete(candidateFamilyAddressEntity);

				addFamilyDetailsDAO.delete(candidateFamilyDetailrequest);
				baseresponse.setResponseCode(IHRMSConstants.successCode);
				baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage);
				return HRMSHelper.createJsonString(baseresponse);

				/*
				 * if(candidateFamilyDetailrequest!=null) { CandidateFamilyAddress
				 * candidateFamilyAddressEntity=candidateFamilyAddressDAO.
				 * findBycandidateFamilyDetail(candidateFamilyDetailrequest);
				 * candidateFamilyAddressEntity.setIsActive(IHRMSConstants.isNotActive);
				 * candidateFamilyAddressDAO.save(candidateFamilyAddressEntity);
				 * 
				 * candidateFamilyDetailrequest.setIsActive(IHRMSConstants.isNotActive);
				 * candidateFamilyDetailrequest.setCandidateFamilyAddress(
				 * candidateFamilyAddressEntity); CandidateFamilyDetail candidateFamilyDetail=
				 * addFamilyDetailsDAO.save(candidateFamilyDetailrequest);
				 * //candidateKnownLanguageDAO.delete(request.getId());
				 * if(candidateFamilyDetail!=null) {
				 * baseresponse.setResponseCode(IHRMSConstants.successCode);
				 * baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage); return
				 * HRMSHelper.createJsonString(baseresponse); } else { return
				 * HRMSHelper.sendErrorResponse(IHRMSConstants.errorWhileDeletingDataMessage,
				 * IHRMSConstants.errorWhileDeletingDataCode); } } else { return
				 * HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
				 * IHRMSConstants.DataNotFoundCode); }
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
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/*
	 * @RequestMapping(method= RequestMethod.GET,produces="application/json")
	 * 
	 * @ResponseBody public String findAllCandidateFamilydetails() { try {
	 * List<CandidateFamilyDetail>
	 * candidateFamilyDetailsEntityList=addFamilyDetailsDAO.
	 * findallCandidateFamilyDetails();
	 * //List<VOCandidateFamilyDetail>vocanidateFamilyList=HRMSResponseTranslator.
	 * convertToVoCandidateFamilyDetailList(candidateFamilyDetailsEntityList);
	 * //return HRMSHelper.createJsonString(vocanidateFamilyList);
	 * HRMSListResponseObject response =new HRMSListResponseObject(); List <Object>
	 * objectList = new ArrayList<Object>(); for (CandidateFamilyDetail
	 * candidateFamilyDetailEntity : candidateFamilyDetailsEntityList) {
	 * VOCandidateFamilyDetail voCandidateFamilyDetail=
	 * HRMSEntityToModelMapper.convertToFamilyDetailModel(
	 * candidateFamilyDetailEntity); objectList.add(voCandidateFamilyDetail); }
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
	public String findAllCandidateFamilydetailsbyId(@PathVariable("id") long candidateId) {
		try {
			if (candidateId > 0) {
				Candidate candidate = candidateDAO.findById(candidateId).get();
				if (!HRMSHelper.isNullOrEmpty(candidate)
						&& !HRMSHelper.isNullOrEmpty(candidate.getCandidatePersonalDetail())) {

					// CandidateFamilyDetail
					// candidateFamilyDetailEntity=HRMSRequestTranslator.convertTOCandidateFamilyDetailEntity(candidateFamilyDetail);
					CandidatePersonalDetail candidatePersonalDetail = candidatePersonalDetailDAO
							.findBycandidate(candidate);
					if (candidatePersonalDetail != null) {
						List<CandidateFamilyDetail> candidateFamilyDetailsEntityList = addFamilyDetailsDAO
								.findCandidateFamilyDetailsbyId(candidatePersonalDetail.getId());
						// List<VOCandidateFamilyDetail>vocanidateFamilyList=HRMSResponseTranslator.convertToVoCandidateFamilyDetailList(candidateFamilyDetailsEntityList);
						// return HRMSHelper.createJsonString(vocanidateFamilyList);
						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> objectList = new ArrayList<Object>();
						for (CandidateFamilyDetail candidateFamilyDetailEntity : candidateFamilyDetailsEntityList) {
							VOCandidateFamilyDetail voCandidateFamilyDetail = HRMSEntityToModelMapper
									.convertToFamilyDetailModel(candidateFamilyDetailEntity);
							objectList.add(voCandidateFamilyDetail);
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
