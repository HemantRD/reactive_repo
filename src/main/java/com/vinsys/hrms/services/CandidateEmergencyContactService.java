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
import com.vinsys.hrms.dao.IHRMSCandidateEmergencyContactAddressDAO;
import com.vinsys.hrms.dao.IHRMSCandidateEmergencyContactDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateEmergencyContact;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateEmergencyContact;
import com.vinsys.hrms.entity.CandidateEmergencyContactAddress;
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
@RequestMapping(path = "/candidateEmergencyContact")

public class CandidateEmergencyContactService {

	@Autowired
	IHRMSCandidateEmergencyContactDAO candidateEmergencyContactDAO;
	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO;
	@Autowired
	IHRMSCandidateEmergencyContactAddressDAO candidateEmergencyContactAddressDAO;

	@Autowired
	IHRMSMasterCityDAO masterCityDAO;

	@Autowired
	IHRMSMasterStateDAO masterStateDAO;

	@Autowired
	IHRMSMasterCountryDAO mastercountryDAO;

	BaseId baseresponse = new BaseId();

	/**
	 * This service is use for Adding Candidate Emergency contact details
	 */
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addCandidateEmergencyContactdetails(
			@RequestBody VOCandidateEmergencyContact vocandidateEmergencyContact) {
		try {
			if (!HRMSHelper.isNullOrEmpty(vocandidateEmergencyContact)
					&& !HRMSHelper.isNullOrEmpty(vocandidateEmergencyContact.getCandidatePersonalDetail())
					&& !HRMSHelper
							.isNullOrEmpty(vocandidateEmergencyContact.getCandidatePersonalDetail().getCandidate())) {
				// Added By Monika.
				if (!HRMSHelper.isNullOrEmpty(vocandidateEmergencyContact.getFirstname())
						&& !HRMSHelper.isNullOrEmpty(vocandidateEmergencyContact.getMiddlename())
						&& !HRMSHelper.isNullOrEmpty(vocandidateEmergencyContact.getLastname())
						&& !HRMSHelper.isNullOrEmpty(
								vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getAddressLine1())
						&& !HRMSHelper.isNullOrEmpty(vocandidateEmergencyContact.getMobileNumber())
						&& !HRMSHelper.isNullOrEmpty(vocandidateEmergencyContact.getRelationship()) &&
						// !HRMSHelper.isNullOrEmpty(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getCountry().getId())&&
						// !HRMSHelper.isNullOrEmpty(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getCity().getId())&&
						// !HRMSHelper.isNullOrEmpty(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getState().getId())&&
						!HRMSHelper.isNullOrEmpty(
								vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getPincode())
						&&

						HRMSHelper.regexMatcher(vocandidateEmergencyContact.getMobileNumber(), "^(\\d{10}|\\d{12})$")) {

					Candidate candidateEntity = candidateDAO
							.getOne(vocandidateEmergencyContact.getCandidatePersonalDetail().getCandidate().getId());
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
							CandidateEmergencyContact candidateEmergencyContactEntity = new CandidateEmergencyContact();
							CandidateEmergencyContact candidateEmergencyContactEntityresp = new CandidateEmergencyContact();

							if (vocandidateEmergencyContact.getId() > 0) {

								candidateEmergencyContactEntity = candidateEmergencyContactDAO
										.findById(vocandidateEmergencyContact.getId()).get();
								MasterCity masterCity = masterCityDAO.findById(vocandidateEmergencyContact
										.getCandidateEmergencyContactAddress().getCity().getId()).get();
								MasterState masterState = masterStateDAO.findById(vocandidateEmergencyContact
										.getCandidateEmergencyContactAddress().getState().getId()).get();
								MasterCountry masterCountry = mastercountryDAO.findById(vocandidateEmergencyContact
										.getCandidateEmergencyContactAddress().getCountry().getId()).get();
								candidateEmergencyContactEntityresp = HRMSRequestTranslator
										.convertToUpdateCandidateEmergencyContactEntity(candidateEmergencyContactEntity,
												vocandidateEmergencyContact);
								candidateEmergencyContactEntityresp.getCandidateEmergencyContactAddress()
										.setCity(masterCity);
								candidateEmergencyContactEntityresp.getCandidateEmergencyContactAddress()
										.setState(masterState);
								candidateEmergencyContactEntityresp.getCandidateEmergencyContactAddress()
										.setCountry(masterCountry);
								candidateEmergencyContactEntityresp
										.setCandidatePersonalDetail(candidatePersonalDetailEntity);

								candidateEmergencyContactEntityresp = candidateEmergencyContactDAO
										.save(candidateEmergencyContactEntityresp);
								if (candidateEmergencyContactEntityresp != null) {
									// baseresponse.setId(candidateEmergencyContactEntityresp.getId());
									// baseresponse.setResponseCode(IHRMSConstants.successCode);
									// baseresponse.setResponseMessage(IHRMSConstants.updatedsuccessMessage);
									// return HRMSHelper.createJsonString(baseresponse);

									List<Object> list = new ArrayList<>();
									list.add(HRMSEntityToModelMapper
											.convertToEmergencyContactModel(candidateEmergencyContactEntityresp));
									HRMSListResponseObject responseObject = new HRMSListResponseObject();
									responseObject.setListResponse(list);
									responseObject.setResponseCode(IHRMSConstants.successCode);
									responseObject.setResponseMessage(IHRMSConstants.updatedsuccessMessage);
									return HRMSHelper.createJsonString(responseObject);
								}

							} else {

								// CandidateEmergencyContact candidateEmergencyContactEntity =
								// HRMSJMapperUtils.map(vocandidateEmergencyContact,
								// CandidateEmergencyContact.class);
								MasterCity city = masterCityDAO.findById(vocandidateEmergencyContact
										.getCandidateEmergencyContactAddress().getCity().getId()).get();
								MasterState state = masterStateDAO.findById(vocandidateEmergencyContact
										.getCandidateEmergencyContactAddress().getState().getId()).get();
								MasterCountry country = mastercountryDAO.findById(vocandidateEmergencyContact
										.getCandidateEmergencyContactAddress().getCountry().getId()).get();
								candidateEmergencyContactEntity = HRMSRequestTranslator
										.convertToCandidateEmergencyContactEntity(vocandidateEmergencyContact);
								candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().setCity(city);
								candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().setState(state);
								candidateEmergencyContactEntity.getCandidateEmergencyContactAddress()
										.setCountry(country);
								candidateEmergencyContactEntity
										.setCandidatePersonalDetail(candidatePersonalDetailEntity);
								// candidateEmergencyContactEntity.setIsActive(IHRMSConstants.isActive);
								candidateEmergencyContactEntityresp = candidateEmergencyContactDAO
										.save(candidateEmergencyContactEntity);

								CandidateEmergencyContactAddress candidateEmergencyContactAddressEntity = candidateEmergencyContactEntity
										.getCandidateEmergencyContactAddress();
								candidateEmergencyContactAddressEntity
										.setCandidateEmergencyContact(candidateEmergencyContactEntity);
								// candidateEmergencyContactAddressEntity.setIsActive(IHRMSConstants.isActive);
								candidateEmergencyContactAddressEntity = candidateEmergencyContactAddressDAO
										.save(candidateEmergencyContactAddressEntity);

								if (candidateEmergencyContactEntityresp != null) {
									// baseresponse.setId(candidateEmergencyContactEntityresp.getId());
									// baseresponse.setResponseCode(IHRMSConstants.successCode);
									// baseresponse.setResponseMessage(IHRMSConstants.addedsuccessMessage);
									// return HRMSHelper.createJsonString(baseresponse);

									List<Object> list = new ArrayList<>();
									list.add(HRMSEntityToModelMapper
											.convertToEmergencyContactModel(candidateEmergencyContactEntityresp));
									HRMSListResponseObject responseObject = new HRMSListResponseObject();
									responseObject.setListResponse(list);
									responseObject.setResponseCode(IHRMSConstants.successCode);
									responseObject.setResponseMessage(IHRMSConstants.addedsuccessMessage);
									return HRMSHelper.createJsonString(responseObject);
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
					} // monika.
				} else {
					throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.InvalidInput);
				}
			} // end if
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
	 * This service is use for Deleting Candidate Emergency contact details
	 * 
	 * 
	 */

	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String deleteCandidateEmergencyContactdetails(@PathVariable("id") long candidateEmergencyContactDetailId) {
		try {
			if (candidateEmergencyContactDetailId > 0) {

				CandidateEmergencyContact candidateEmergencyContactrequest = candidateEmergencyContactDAO
						.getOne(candidateEmergencyContactDetailId);

				CandidateEmergencyContactAddress candidateEmergencyContactAddressEntity = candidateEmergencyContactAddressDAO
						.findBycandidateEmergencyContact(candidateEmergencyContactrequest);

				candidateEmergencyContactAddressDAO.delete(candidateEmergencyContactAddressEntity);

				candidateEmergencyContactDAO.delete(candidateEmergencyContactrequest);
				baseresponse.setResponseCode(IHRMSConstants.successCode);
				baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage);
				return HRMSHelper.createJsonString(baseresponse);
				/*
				 * candidateEmergencyContactrequest.setIsActive(IHRMSConstants.isNotActive);
				 * candidateEmergencyContactrequest.getCandidateEmergencyContactAddress().
				 * setIsActive(IHRMSConstants.isNotActive); CandidateEmergencyContact
				 * candidateEmergencyContact=candidateEmergencyContactDAO.save(
				 * candidateEmergencyContactrequest);
				 * 
				 * if(candidateEmergencyContact!=null) {
				 * baseresponse.setResponseCode(IHRMSConstants.successCode);
				 * baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage); return
				 * HRMSHelper.createJsonString(baseresponse); }else {
				 * 
				 * throw new HRMSException(IHRMSConstants.errorWhileAddingDataCode,
				 * IHRMSConstants.errorWhileAddingDataMessage); }
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
	 * @ResponseBody public String findAllCandidateEmergencyContactdetails() { try {
	 * List<CandidateEmergencyContact>
	 * CandidateEmergencyContactEntityList=candidateEmergencyContactDAO.
	 * finadallCandidateEmergencyContactDetails();
	 * //List<VOCandidateEmergencyContact>vocanidateEmergencyList=
	 * HRMSResponseTranslator.convertToVoCandidateEmergencyContactDetailList(
	 * CandidateEmergencyContactEntityList); //return
	 * HRMSHelper.createJsonString(vocanidateEmergencyList);
	 * 
	 * HRMSListResponseObject response =new HRMSListResponseObject(); List <Object>
	 * objectList = new ArrayList<Object>(); for (CandidateEmergencyContact
	 * candidateEmergencyContact : CandidateEmergencyContactEntityList) {
	 * VOCandidateEmergencyContact voCandidateEmergencyContact=
	 * HRMSEntityToModelMapper.convertToEmergencyContactModel(
	 * candidateEmergencyContact); objectList.add(voCandidateEmergencyContact); }
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
	public String findAllCandidateEmergencyContactdetailsbyId(@PathVariable("id") long candidateId) {
		try {
			if (candidateId > 0) {
				Candidate candidate = candidateDAO.findById(candidateId).get();
				if (!HRMSHelper.isNullOrEmpty(candidate)) {

					// CandidateFamilyDetail
					// candidateFamilyDetailEntity=HRMSRequestTranslator.convertTOCandidateFamilyDetailEntity(candidateFamilyDetail);
					CandidatePersonalDetail candidatePersonalDetail = candidatePersonalDetailDAO
							.findBycandidate(candidate);
					if (candidatePersonalDetail != null) {
						List<CandidateEmergencyContact> CandidateEmergencyContactEntityList = candidateEmergencyContactDAO
								.findCandidateEmergencyDetailsbyId(candidatePersonalDetail.getId());
						// List<VOCandidateEmergencyContact>vocanidateEmergencyList=HRMSResponseTranslator.convertToVoCandidateEmergencyContactDetailList(CandidateEmergencyContactEntityList);
						// return HRMSHelper.createJsonString(vocanidateEmergencyList);
						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> objectList = new ArrayList<Object>();
						for (CandidateEmergencyContact candidateEmergencyContact : CandidateEmergencyContactEntityList) {
							VOCandidateEmergencyContact voCandidateEmergencyContact = HRMSEntityToModelMapper
									.convertToEmergencyContactModel(candidateEmergencyContact);
							objectList.add(voCandidateEmergencyContact);
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
