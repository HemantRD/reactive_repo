package com.vinsys.hrms.services;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateAddressDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCurrentOrganizationDetailDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterBranchDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSMasterDesignationDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.datamodel.VOCandidate;
import com.vinsys.hrms.datamodel.VOCandidateAddress;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateAddress;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCurrentDetail;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/candidatePersonalDetail")

public class CandidatePersonalDetailService {

	private static final Logger logger = LoggerFactory.getLogger(CandidatePersonalDetailService.class);

	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSCandidatePersonalDetailDAO personalDetailsDAO;
	@Autowired
	IHRMSMasterCityDAO masterCityDAO;
	@Autowired
	IHRMSMasterStateDAO masterStateDAO;
	@Autowired
	IHRMSMasterCountryDAO masterCountryDAO;
	@Autowired
	IHRMSProfessionalDetailsDAO professionalDetailDAO;
	@Autowired
	IHRMSCandidateAddressDAO addressDAO;
	@Autowired
	IHRMSMasterBranchDAO branchDAO;
	@Autowired
	IHRMSMasterDesignationDAO designationDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;
	@Autowired
	IHRMSMasterDepartmentDAO departmenDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSEmployeeCurrentOrganizationDetailDAO employeeCurrentOrganizationDetailDAO;

	/**
	 * This service is to update the candidate general information i.e candidate
	 * basic information,candidate address,candidate personal details entity will
	 * get updated
	 * 
	 * Screen Candidate general info
	 * 
	 */
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String updateCandidateGeneralInfo(@RequestBody VOCandidate candidateModelReq) {

		try {

			logger.info(" == >> UPDATING CANDIDATE PERSONAL (GENERAL INFO) DETAILS  << == ");

			if (!HRMSHelper.isNullOrEmpty(candidateModelReq)) {
				Candidate candidate = candidateDAO.findById(candidateModelReq.getId()).get();
				
				if (!HRMSHelper.isNullOrEmpty(candidate)) {
					logger.info("Update Candidate :" + candidate.getId() + " : " + candidate.getFirstName() + " "
							+ candidate.getLastName());
					candidate = HRMSRequestTranslator.translateToCandidateGeneralInfo(candidate, candidateModelReq);

					// candidate
					// Added By Monika.
					if (!HRMSHelper.isNullOrEmpty(candidateModelReq.getFirstName())
							&& !HRMSHelper.isNullOrEmpty(candidateModelReq.getLastName())
							&& !HRMSHelper.isNullOrEmpty(candidateModelReq.getGender())
							&& !HRMSHelper.isNullOrEmpty(candidateModelReq.getEmailId())
							&& !HRMSHelper.isNullOrEmpty(candidateModelReq.getMobileNumber())
							&& !HRMSHelper.isNullOrEmpty(candidateModelReq.getDateOfBirth()) && !HRMSHelper
									.isNullOrEmpty(candidateModelReq.getCandidatePersonalDetail().getMaritalStatus())
							&&

							!HRMSHelper.isNullOrEmpty(candidateModelReq.getCandidateStatus())
							&& HRMSHelper.regexMatcher(candidateModelReq.getEmailId(),
									"[a-zA-z0-9][a-zA-Z0-9_.]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")
							&&
							// HRMSHelper.regexMatcher(Long.toString(candidateModelReq.getMobileNumber()),"^\\d{10}$")
							HRMSHelper.regexMatcher(Long.toString(candidateModelReq.getMobileNumber()),
									"^(\\d{10}|\\d{12})$")

					// HRMSHelper.regexMatcher(candidateModelReq.getFirstName(), "[a-zA-Z]+") &&
					// HRMSHelper.regexMatcher(candidateModelReq.getLastName(), "[a-zA-Z]+") &&
					// HRMSHelper.regexMatcher(candidateModelReq.getMiddleName(), "[a-zA-Z]+")&&

					) {

						/*
						 * SETTING CANDIDATE ADDRESS TO UPDATE
						 */
						Set<CandidateAddress> newCandidateAddressSet = new HashSet<CandidateAddress>();
						Set<VOCandidateAddress> candidateAddressModelSet = candidateModelReq.getCandidateAddresses();
						if (!HRMSHelper.isNullOrEmpty(candidateAddressModelSet)) {
							for (VOCandidateAddress voCandidateAddress : candidateAddressModelSet) {
								// Address
								if (!HRMSHelper.isNullOrEmpty(voCandidateAddress.getCity().getCityName())
										&& !HRMSHelper.isNullOrEmpty(voCandidateAddress.getCountry().getCountryName())
										&& !HRMSHelper.isNullOrEmpty(voCandidateAddress.getState().getStateName())
										&& !HRMSHelper.isNullOrEmpty(voCandidateAddress.getAddressLine1())
								// !HRMSHelper.pincodeCheck(voCandidateAddress.getPincode())
								) {
									logger.info("Update Candidate : " + candidate.getId() + "City name : "
											+ voCandidateAddress.getCity().getCityName() + "Country name : "
											+ voCandidateAddress.getCountry().getCountryName() + " State Name : "
											+ voCandidateAddress.getState().getStateName());

									CandidateAddress candidateAddress = addressDAO.findById(voCandidateAddress.getId()).get();
									if (candidateAddress == null) {
										candidateAddress = new CandidateAddress();
									}
									candidateAddress = HRMSRequestTranslator
											.translateCandidateAddressEntity(voCandidateAddress, candidateAddress);
									MasterCity masterCityAddress = masterCityDAO
											.findById(voCandidateAddress.getCity().getId()).get();
									MasterCountry masterCountryAddress = masterCountryDAO
											.findById(voCandidateAddress.getCountry().getId()).get();
									MasterState masterStateAddress = masterStateDAO
											.findById(voCandidateAddress.getState().getId()).get();
									candidateAddress.setCity(masterCityAddress);
									candidateAddress.setState(masterStateAddress);
									candidateAddress.setCountry(masterCountryAddress);
									candidateAddress.setCandidate(candidate);
									newCandidateAddressSet.add(candidateAddress);
								} else {
									throw new HRMSException(IHRMSConstants.NotValidDateCode,
											IHRMSConstants.InvalidInput);
								}
							}
						}
						MasterBranch masterBranch = branchDAO
								.findById(candidateModelReq.getCandidateProfessionalDetail().getBranch().getId()).get();
						MasterDivision masterDivision = divisionDAO
								.findById(candidateModelReq.getCandidateProfessionalDetail().getDivision().getId()).get();
						MasterDesignation masterDesignation = designationDAO
								.findById(candidateModelReq.getCandidateProfessionalDetail().getDesignation().getId()).get();
						MasterDepartment masterDepartment = departmenDAO
								.findById(candidateModelReq.getCandidateProfessionalDetail().getDepartment().getId()).get();
						MasterCity masterCity = masterCityDAO
								.findById(candidateModelReq.getCandidateProfessionalDetail().getCity().getId()).get();
						MasterCountry masterCountry = masterCountryDAO
								.findById(candidateModelReq.getCandidateProfessionalDetail().getCountry().getId()).get();
						MasterState masterState = masterStateDAO
								.findById(candidateModelReq.getCandidateProfessionalDetail().getState().getId()).get();
						logger.info("Candidate :" + candidate.getId() + " Candidate Branch :"
								+ masterBranch.getBranchName() + " Candidtate Division :"
								+ masterDivision.getDivisionName() + " Candidate Department :"
								+ masterDepartment.getDepartmentName() + " Candidate City :" + masterCity.getCityName()
								+ " Candidate Country" + masterCountry.getCountryName() + "Candidate State :"
								+ masterState.getStateName());
						candidate.setCandidateAddress(newCandidateAddressSet);
						candidate = candidateDAO.save(candidate);
						candidate.getCandidateProfessionalDetail().setBranch(masterBranch);
						candidate.getCandidateProfessionalDetail().setDivision(masterDivision);
						candidate.getCandidateProfessionalDetail().setDesignation(masterDesignation);
						candidate.getCandidateProfessionalDetail().setDepartment(masterDepartment);
						candidate.getCandidateProfessionalDetail().setCity(masterCity);
						candidate.getCandidateProfessionalDetail().setCountry(masterCountry);
						candidate.getCandidateProfessionalDetail().setState(masterState);

						candidate.getCandidatePersonalDetail().setCandidate(candidate);
						candidate.getCandidateProfessionalDetail().setCandidate(candidate);
						personalDetailsDAO.save(candidate.getCandidatePersonalDetail());
						professionalDetailDAO.save(candidate.getCandidateProfessionalDetail());

//						logger.info("Candidate Personal Details : "+new ObjectMapper().writeValueAsString(candidate.getCandidatePersonalDetail()));
//						logger.info("Candidate Professional Details : "+new ObjectMapper().writeValueAsString(candidate.getCandidateProfessionalDetail()));
						// updating retirement date if it is an employee
						Employee employee = employeeDAO.getEmployeeDetailsByCandidateId(candidate.getId(),
								IHRMSConstants.isActive, IHRMSConstants.isActive);
						if (!HRMSHelper.isNullOrEmpty(employee) && !HRMSHelper.isLongZero(employee.getId())) {
							Calendar calCandRet = Calendar.getInstance();
							calCandRet.setTime(employee.getCandidate().getDateOfBirth());
							calCandRet.add(Calendar.YEAR, 58);
							calCandRet.add(Calendar.DATE, -1);
							EmployeeCurrentDetail employeeCurrentDetail = employeeCurrentOrganizationDetailDAO
									.findEmployeeCurrentDetailEmployeeWise(employee.getId());
							employeeCurrentDetail.setRetirementDate(calCandRet.getTime());
							employeeCurrentOrganizationDetailDAO.save(employeeCurrentDetail);
						}

					} // monika if candidate
					else {
						throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.InvalidInput);
					}
					return HRMSHelper.sendSuccessResponse(IHRMSConstants.GENERAL_INFO_UPDATED_SUCCESFULL,
							IHRMSConstants.successCode);
				} // if candidate
				else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode,
							IHRMSConstants.CandidateDoesnotExistMessage);
				}
			} // if candidateVO
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

}
