package com.vinsys.hrms.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateAddressDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateLetterDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.dao.IHRMSEmailTemplateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
import com.vinsys.hrms.dao.IHRMSMapOrgDivHrDAO;
import com.vinsys.hrms.dao.IHRMSMasterBranchDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSMasterDesignationDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.dao.IHRMSSubscriptionDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidate;
import com.vinsys.hrms.datamodel.VOCandidateAddress;
import com.vinsys.hrms.datamodel.VOCandidateProfessionalDetail;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOEmployeeCurrentDetail;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateAddress;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.JWTTokenHelper;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/candidate")
//
public class CandidateService {

	private static final Logger logger = LoggerFactory.getLogger(CandidateService.class);

	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSCandidateAddressDAO candidateAddressDAO;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	IHRMSSubscriptionDAO subscriptionDAO;
	@Autowired
	IHRMSProfessionalDetailsDAO professionalDAO;
	@Autowired
	IHRMSLoginEntityTypeDAO loginEntityTypeDAO;
	@Autowired
	IHRMSMasterBranchDAO branchDAO;
	@Autowired
	IHRMSMasterDesignationDAO designationDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;
	@Autowired
	IHRMSMasterDepartmentDAO departmenDAO;
	@Autowired
	IHRMSCandidatePersonalDetailDAO personalDetailsDAO;
	@Autowired
	IHRMSMasterCityDAO masterCityDAO;
	@Autowired
	IHRMSMasterStateDAO masterStateDAO;
	@Autowired
	IHRMSMasterCountryDAO masterCountryDAO;
	@Autowired
	IHRMSCandidateLetterDAO candidateLetterDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSMapOrgDivHrDAO orgDivHRDAO;

	@Value("${retirment_remainder_before_90_days}")
	private Integer retirmentReminderNoofDays;

	@Value("${retirment_remainder_before_7_days}")
	private Integer retirmentReminderNoofDays1;

	@Value("${PROBATION_REMINDER_NO_OF_DAYS}")
	private int probationReminderNoofDays;

	@Autowired
	EmailSender emailsender;

	@Autowired
	IHRMSEmailTemplateDAO emailTemplateDAO;

	/**
	 * This service is to create candidate,with basic information,basic professional
	 * information,basic personal information,candidate address.
	 * 
	 * Screen Manage candidate screen
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createCandidate(@RequestBody VOCandidate candidateModel, HttpServletRequest httpRequest) {
		try {

			String token = httpRequest.getHeader("Authorization");
			token = token.substring(7, token.length());
			Claims claims = JWTTokenHelper.getLoggedInEmpDetail(token);
			String logedInUser = claims.get("username").toString();
			String logedInUserRole = claims.get("hasRole").toString();

			if (!HRMSHelper.isNullOrEmpty(logedInUserRole) && logedInUserRole.equals("HR")) {

				logger.info(" == >> CREATING CANDIDATE << ==");

				Candidate emailCheck = null;
				if (candidateModel != null && !HRMSHelper.isNullOrEmpty(candidateModel.getEmailId())) {

					emailCheck = candidateDAO.findByemailId(candidateModel.getEmailId());
					if (emailCheck == null) {

						/**************
						 * added by akanksha for add candidate server side validation
						 ************************/

						if (!HRMSHelper.isNullOrEmpty(candidateModel.getFirstName())
								&& !HRMSHelper.isNullOrEmpty(candidateModel.getLastName())
								&& !HRMSHelper.isNullOrEmpty(candidateModel.getGender())
								&& !HRMSHelper.isNullOrEmpty(candidateModel.getEmailId())
								&& !HRMSHelper.isNullOrEmpty(candidateModel.getTitle())
								&& !HRMSHelper.isLongZero(candidateModel.getMobileNumber())
								&& !HRMSHelper.isNullOrEmpty(candidateModel.getDateOfBirth())
								&& !HRMSHelper
										.isNullOrEmpty(candidateModel.getCandidatePersonalDetail().getMaritalStatus())
								&& !HRMSHelper.isNullOrEmpty(
										candidateModel.getCandidateProfessionalDetail().getDateOfJoining())
								&& !HRMSHelper.isLongZero(
										candidateModel.getCandidateProfessionalDetail().getDepartment().getId())
								&& !HRMSHelper.isLongZero(
										candidateModel.getCandidateProfessionalDetail().getDesignation().getId())
								&& !HRMSHelper.isLongZero(
										candidateModel.getCandidateProfessionalDetail().getDivision().getId())
								&& !HRMSHelper.isNullOrEmpty(candidateModel.getNoticePeriod())

						) {

							boolean checkFirstName = HRMSHelper.regexMatcher(candidateModel.getFirstName(),
									"[a-zA-Z ]+");
							boolean checkLastName = HRMSHelper.regexMatcher(candidateModel.getLastName(), "[a-zA-Z ]+");
							boolean checkMobileNo = HRMSHelper.regexMatcher(
									Long.toString(candidateModel.getMobileNumber()), "^(\\d{10}|\\d{12})$");
							boolean checkEmail = HRMSHelper.regexMatcher(candidateModel.getEmailId(),
									"^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
							boolean checkNoticePeriod = HRMSHelper
									.regexMatcher(Integer.toString(candidateModel.getNoticePeriod()), "[0-9]{1,3}");

							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							String s = formatter.format(candidateModel.getDateOfBirth());

							boolean checkBirthDate = HRMSHelper.isDatePast(s, "yyyy-MM-dd");

							if (checkFirstName && checkLastName && checkEmail && checkMobileNo && checkNoticePeriod
									&& checkBirthDate) {

								if (candidateModel.getLoginEntity() != null
										&& candidateModel.getLoginEntity().getOrganization() != null) {

									Candidate candidateEntity = HRMSRequestTranslator
											.translateToCandidateEntityToPersist(candidateModel);
									candidateEntity.setCandidateStatus(IHRMSConstants.CandidateStatus_CREATED);

									/**
									 * Assigning Organization To Candidate
									 */
									Organization organization = organizationDAO
											.findById(candidateModel.getLoginEntity().getOrganization().getId()).get();
									candidateEntity.getLoginEntity().setOrganization(organization);

									/**
									 * Finding And Assigning The Masters To Candidate
									 */
									MasterBranch masterBranch = branchDAO
											.findById(
													candidateModel.getCandidateProfessionalDetail().getBranch().getId())
											.get();
									MasterDivision masterDivision = divisionDAO.findById(
											candidateModel.getCandidateProfessionalDetail().getDivision().getId())
											.get();
									MasterDesignation masterDesignation = designationDAO.findById(
											candidateModel.getCandidateProfessionalDetail().getDesignation().getId())
											.get();
									MasterDepartment masterDepartment = departmenDAO.findById(
											candidateModel.getCandidateProfessionalDetail().getDepartment().getId())
											.get();
									MasterCity masterCity = masterCityDAO
											.findById(candidateModel.getCandidateProfessionalDetail().getCity().getId())
											.get();
									MasterCountry masterCountry = masterCountryDAO.findById(
											candidateModel.getCandidateProfessionalDetail().getCountry().getId()).get();
									MasterState masterState = masterStateDAO
											.findById(
													candidateModel.getCandidateProfessionalDetail().getState().getId())
											.get();

									// finding employee entity of recruiter
									// change on 25 dec eve
									Employee recruiter = employeeDAO.findById(
											candidateModel.getCandidateProfessionalDetail().getRecruiter().getId())
											.get();
									candidateEntity.getCandidateProfessionalDetail().setRecruiter(recruiter);
									// upto this for recruiter

									/**
									 * Persisting Candidate Login Entity and Login Entity Type Details
									 */
									LoginEntity loginEntity = candidateEntity.getLoginEntity();
									LoginEntityType loginEntityType = loginEntityTypeDAO
											.findByRoleName(IHRMSConstants.RoleCandidate, organization.getId());
									/*
									 * changed by ssw on 01jan2018 for : manyToMany loginEntityType
									 */
									// loginEntity.setLoginEntityType(loginEntityType);
									Set<LoginEntityType> loginEntityTypes = new HashSet<LoginEntityType>();
									loginEntityTypes.add(loginEntityType);
									loginEntity.setLoginEntityTypes(loginEntityTypes);
									/*
									 * upto this changed
									 */
									loginEntity.setIsActive(IHRMSConstants.isActive);
									candidateEntity.setLoginEntity(loginEntity);

									Candidate candidateNew = candidateDAO.save(candidateEntity);

									/**
									 * Persisting Candidate Personal Details
									 */
									CandidatePersonalDetail candidatePersonalDetail = new CandidatePersonalDetail();
									candidatePersonalDetail.setMaritalStatus(
											candidateModel.getCandidatePersonalDetail().getMaritalStatus());
									candidatePersonalDetail.setCandidate(candidateNew);
									candidatePersonalDetail.setIsActive(IHRMSConstants.isActive);
									candidatePersonalDetail.setCreatedBy(candidateModel.getCreatedBy());
									candidatePersonalDetail.setCreatedDate(new Date());
									candidatePersonalDetail = personalDetailsDAO.save(candidatePersonalDetail);

									/**
									 * Persisting Candidate Professional Details
									 */
									CandidateProfessionalDetail candidateProfessionalDetails = candidateEntity
											.getCandidateProfessionalDetail();
									candidateProfessionalDetails.setBranch(masterBranch);
									candidateProfessionalDetails.setDivision(masterDivision);
									candidateProfessionalDetails.setDesignation(masterDesignation);
									candidateProfessionalDetails.setDepartment(masterDepartment);
									candidateProfessionalDetails.setCandidate(candidateNew);
									candidateProfessionalDetails.setCity(masterCity);
									candidateProfessionalDetails.setState(masterState);
									candidateProfessionalDetails.setCountry(masterCountry);
									professionalDAO.save(candidateProfessionalDetails);

									/**
									 * Persisting Candidate Present & Permanent Address
									 */
									Set<VOCandidateAddress> candidateAddressSetModel = candidateModel
											.getCandidateAddresses();
									Set<CandidateAddress> candidateAddressesEntity = new HashSet<CandidateAddress>();

									if (!HRMSHelper.isNullOrEmpty(candidateAddressSetModel)) {

										for (VOCandidateAddress candidateAddressModel : candidateAddressSetModel) {

											CandidateAddress candidateAddress = new CandidateAddress();
											candidateAddress.setAddressLine1(candidateAddressModel.getAddressLine1());
											candidateAddress.setAddressLine2(candidateAddressModel.getAddressLine2());
											candidateAddress.setAddressType(candidateAddressModel.getAddressType());
											candidateAddress.setPhone1(candidateAddressModel.getPhone1());
											candidateAddress.setPhone2(candidateAddressModel.getPhone2());
											candidateAddress.setCandidate(candidateNew);
											candidateAddress.setPincode(candidateAddressModel.getPincode());
											candidateAddress.setIsActive(IHRMSConstants.isActive);

											MasterCity masterCityAddress = masterCityDAO
													.findById(candidateAddressModel.getCity().getId()).get();
											MasterCountry masterCountryAddress = masterCountryDAO
													.findById(candidateAddressModel.getCountry().getId()).get();
											MasterState masterStateAddress = masterStateDAO
													.findById(candidateAddressModel.getState().getId()).get();

											candidateAddress.setCity(masterCityAddress);
											candidateAddress.setState(masterStateAddress);
											candidateAddress.setCountry(masterCountryAddress);

											candidateAddress.setCreatedBy(candidateAddressModel.getCreatedBy());
											candidateAddress.setCreatedDate(new Date());
											candidateAddressesEntity.add(candidateAddress);
										}
									}
									List<CandidateAddress> candidateAddressList = candidateAddressDAO
											.saveAll(candidateAddressesEntity);
									HashSet<CandidateAddress> newAddressSet = new HashSet<CandidateAddress>();
									for (CandidateAddress addressEntity : candidateAddressList) {
										newAddressSet.add(addressEntity);
									}

									HRMSListResponseObject response = new HRMSListResponseObject();
									List<Object> listResponse = new ArrayList<Object>();
									Candidate candidateRetrived = candidateDAO.findById(candidateNew.getId()).get();
									candidateRetrived.setCandidatePersonalDetail(candidatePersonalDetail);
									candidateRetrived.setCandidateAddress(newAddressSet);
									Object candidateVo = HRMSResponseTranslator
											.translateToCandidateModle(candidateRetrived);
									listResponse.add(candidateVo);
									response.setListResponse(listResponse);
									response.setTotalCount(listResponse.size());
									response.setResponseCode(IHRMSConstants.successCode);
									response.setResponseMessage(IHRMSConstants.CANDIDATE_CREATED_SUCCESFULLY);
									return HRMSHelper.createJsonString(response);

								} else {
									throw new HRMSException(IHRMSConstants.InsufficientDataCode,
											IHRMSConstants.InsufficientDataMessage);
								}
							} else {
								// Else part added by akanksha for validating regex patterns

								if (!checkFirstName)
									throw new HRMSException(IHRMSConstants.InsufficientDataCode, "First Name Invalid");

								if (!checkLastName)
									throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Last Name Invalid");

								if (!checkMobileNo)
									throw new HRMSException(IHRMSConstants.InsufficientDataCode,
											"Mobile number Invalid");

								if (!checkEmail)
									throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Email ID Invalid");

								if (!checkNoticePeriod)
									throw new HRMSException(IHRMSConstants.InsufficientDataCode,
											"Notice Period Invalid");

								if (!checkBirthDate)
									throw new HRMSException(IHRMSConstants.InsufficientDataCode, "birth date Invalid");

							}
						} else {
							// Else part added by akanksha to check null or empty fields

							throw new HRMSException(IHRMSConstants.InsufficientDataCode,
									IHRMSConstants.InsufficientDataMessage);
						}
					} else {
						throw new HRMSException(IHRMSConstants.DataAlreadyExist,
								IHRMSConstants.emailAlreadyExistMessage);
					}
				} else {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}

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

	/**
	 * This service is to update candidate, Screen manager candidate screen update.
	 */
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updateCandidate(@RequestBody VOCandidate candidateModel) {
		try {

			if (candidateModel != null) {

				logger.info(" ==== >> UPDATING CANDIDATE  << ==== ");

				Candidate candidteEntity = candidateDAO.findById(candidateModel.getId()).get();
				if (candidteEntity != null) {

					/*************
					 * added by akanksha for update candidate server side validation
					 *******************/

					if (!HRMSHelper.isNullOrEmpty(candidateModel.getFirstName())
							&& !HRMSHelper.isNullOrEmpty(candidateModel.getLastName())
							&& !HRMSHelper.isNullOrEmpty(candidateModel.getGender())
							&& !HRMSHelper.isNullOrEmpty(candidateModel.getEmailId())
							&& !HRMSHelper.isNullOrEmpty(candidateModel.getTitle())
							&& !HRMSHelper.isLongZero(candidateModel.getMobileNumber())
							&& !HRMSHelper.isNullOrEmpty(candidateModel.getDateOfBirth())
							&& !HRMSHelper.isNullOrEmpty(candidateModel.getCandidatePersonalDetail().getMaritalStatus())
							&& !HRMSHelper
									.isNullOrEmpty(candidateModel.getCandidateProfessionalDetail().getDateOfJoining())
							&& !HRMSHelper
									.isLongZero(candidateModel.getCandidateProfessionalDetail().getDepartment().getId())
							&& !HRMSHelper.isLongZero(
									candidateModel.getCandidateProfessionalDetail().getDesignation().getId())
							&& !HRMSHelper
									.isLongZero(candidateModel.getCandidateProfessionalDetail().getDivision().getId())
							&& !HRMSHelper.isNullOrEmpty(candidateModel.getNoticePeriod())

					) {

						// regex added by akanksha for add candidate server side validation

						boolean checkFirstName = HRMSHelper.regexMatcher(candidateModel.getFirstName(), "[a-zA-Z]+");
						boolean checkLastName = HRMSHelper.regexMatcher(candidateModel.getLastName(), "[a-zA-Z]+");
						boolean checkMobileNo = HRMSHelper.regexMatcher(Long.toString(candidateModel.getMobileNumber()),
								"^(\\d{10}|\\d{12})$");
						boolean checkEmail = HRMSHelper.regexMatcher(candidateModel.getEmailId(),
								"^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
						boolean checkNoticePeriod = HRMSHelper
								.regexMatcher(Integer.toString(candidateModel.getNoticePeriod()), "[0-9]{1,3}");

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						String s = formatter.format(candidateModel.getDateOfBirth());

						boolean checkBirthDate = HRMSHelper.isDatePast(s, "dd-MM-yyyy");

						if (checkFirstName && checkLastName && checkEmail && checkMobileNo && checkNoticePeriod
								&& checkBirthDate) {

							Candidate candidateEntity = HRMSRequestTranslator.updateCandidateEntity(candidteEntity,
									candidateModel);

							Set<CandidateAddress> candidateAddressEntitySet = candidateEntity.getCandidateAddress();

							/**
							 * SETTING CANDIDATE ADDRESS TO UPDATE
							 */
							if (!HRMSHelper.isNullOrEmpty(candidateModel.getCandidateAddresses())) {

								for (VOCandidateAddress voCandidateAddress : candidateModel.getCandidateAddresses()) {
									for (CandidateAddress candidateAddress : candidateAddressEntitySet) {
										if (candidateAddress.getId() == voCandidateAddress.getId()) {
											candidateAddress = HRMSRequestTranslator.translateCandidateAddressEntity(
													voCandidateAddress, candidateAddress);
											MasterCity masterCityAddress = masterCityDAO
													.findById(voCandidateAddress.getCity().getId()).get();
											MasterCountry masterCountryAddress = masterCountryDAO
													.findById(voCandidateAddress.getCountry().getId()).get();
											MasterState masterStateAddress = masterStateDAO
													.findById(voCandidateAddress.getState().getId()).get();

											candidateAddress.setCity(masterCityAddress);
											candidateAddress.setState(masterStateAddress);
											candidateAddress.setCountry(masterCountryAddress);
											candidateAddress.setUpdatedBy(candidateModel.getUpdatedBy());
											candidateAddressEntitySet.add(candidateAddress);
										}
									}
								}
							}

							/**
							 * SETTING MASTERS FOR CANDIDATE PROFESSIONAL DETAILS
							 */
							MasterBranch branch = branchDAO
									.findById(candidateModel.getCandidateProfessionalDetail().getBranch().getId())
									.get();
							MasterDivision divisionEntity = divisionDAO
									.findById(candidateModel.getCandidateProfessionalDetail().getDivision().getId())
									.get();
							MasterDesignation designationEntity = designationDAO
									.findById(candidateModel.getCandidateProfessionalDetail().getDesignation().getId())
									.get();
							MasterDepartment departmentEntity = departmenDAO
									.findById(candidateModel.getCandidateProfessionalDetail().getDepartment().getId())
									.get();
							MasterCity masterCity = masterCityDAO
									.findById(candidateModel.getCandidateProfessionalDetail().getCity().getId()).get();
							MasterCountry masterCountry = masterCountryDAO
									.findById(candidateModel.getCandidateProfessionalDetail().getCountry().getId())
									.get();
							MasterState masterState = masterStateDAO
									.findById(candidateModel.getCandidateProfessionalDetail().getState().getId()).get();

							// finding employee entity of recruiter
							// change on 25 dec eve
							Employee recruiter = employeeDAO
									.findById(candidateModel.getCandidateProfessionalDetail().getRecruiter().getId())
									.get();
							candidateEntity.getCandidateProfessionalDetail().setRecruiter(recruiter);
							// upto this for recruiter

							candidateEntity.getCandidateProfessionalDetail().setBranch(branch);
							candidateEntity.getCandidateProfessionalDetail().setDivision(divisionEntity);
							candidateEntity.getCandidateProfessionalDetail().setDesignation(designationEntity);
							candidateEntity.getCandidateProfessionalDetail().setDepartment(departmentEntity);
							candidateEntity.getCandidateProfessionalDetail().setCity(masterCity);
							candidateEntity.getCandidateProfessionalDetail().setState(masterState);
							candidateEntity.getCandidateProfessionalDetail().setCountry(masterCountry);
							candidateDAO.save(candidateEntity);
							return HRMSHelper.sendSuccessResponse(IHRMSConstants.updatedsuccessMessage,
									IHRMSConstants.successCode);

						} else {
							// code added by akanksha for validating regex patterns

							if (!checkFirstName)
								throw new HRMSException(IHRMSConstants.InsufficientDataCode, "First Name Invalid");

							if (!checkLastName)
								throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Last Name Invalid");

							if (!checkMobileNo)
								throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Mobile number Invalid");

							if (!checkEmail)
								throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Email ID Invalid");

							if (!checkNoticePeriod)
								throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Notice Period Invalid");

							if (!checkBirthDate)
								throw new HRMSException(IHRMSConstants.InsufficientDataCode,
										"Cannot Delect Future Date");

						}
					} else {
						// code added by akanksha to check null or empty fields
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}

				} else {

					throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
							IHRMSConstants.CandidateDoesnotExistMessage);
				}

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
		// return null;
		return null;
	}

	/**
	 * This service is to get list of candidates,based on id,name,emailID and
	 * AADHAR,and if no data the call the candidate.
	 * 
	 * Screen Manage candidate search
	 */
	@RequestMapping(value = "/{orgId}/{id}/{name}/{emailId}/{aadharCard}/{page}/{size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getCandidateDetails(@PathVariable("orgId") long orgId, @PathVariable("id") long candidateId,
			@PathVariable("name") String name, @PathVariable("emailId") String emailId,
			@PathVariable("aadharCard") String aadharCardNo, @PathVariable("page") int page,
			@PathVariable("size") int size) {

		logger.info(" == >> FINDING CANDIDATE  << ==");
		if (size <= 0) {
			size = IHRMSConstants.DefaultPageSize;
		}

		int count = 0;
		try {
			List<Candidate> candidateSetResult = null;
			Candidate candidate = null;
			PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");
			if (!HRMSHelper.isNullOrEmpty(name) || !HRMSHelper.isNullOrEmpty(emailId)
					|| !HRMSHelper.isNullOrEmpty(aadharCardNo)) {

				pageRequest = PageRequest.of(page, size);
				candidateSetResult = new ArrayList<Candidate>();
				logger.info("==>> FIND BY QUERY << ==");
				candidateSetResult = candidateDAO.findByCustomQuery(name, emailId, aadharCardNo, orgId, pageRequest);
				count = candidateDAO.findByCustomQuery(name, emailId, aadharCardNo, orgId, IHRMSConstants.isActive);
				logger.info("Count Check -- >> ");

			} else if (candidateId > 0) {
				logger.info("==>> FIND BY ID << ==");
				candidateSetResult = new ArrayList<Candidate>();
				candidate = candidateDAO.findById(candidateId).get();

				if (candidate != null) {
					count = 1;
					if (!HRMSHelper.isNullOrEmpty(candidate.getIsActive())
							&& candidate.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {
						candidateSetResult.add(candidate);
					} else {
						throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
								IHRMSConstants.CandidateDoesnotExistMessage);
					}

				} else {
					throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
							IHRMSConstants.CandidateDoesnotExistMessage);
				}

			} else {
				logger.info("==>>FIND ALL << ==");
				candidateSetResult = new ArrayList<Candidate>();
				count = candidateDAO.countByisActive(IHRMSConstants.isActive);
				Page<Candidate> pageCandidate = candidateDAO.findAll(pageRequest);
				candidateSetResult = pageCandidate.getContent();

			}

			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> listResponse = new ArrayList<Object>();
			for (Candidate candidateEntity : candidateSetResult) {
				if (!HRMSHelper.isNullOrEmpty(candidateEntity.getIsActive())
						&& candidateEntity.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {
					Object candidateModel = HRMSResponseTranslator.translateToCandidateModle(candidateEntity);
					listResponse.add(candidateModel);
				}

			}
			logger.info("is Active Count ==== >> " + listResponse.size());
			response.setListResponse(listResponse);
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			response.setTotalCount(count);
			response.setPageNo(page);
			response.setPageSize(size);
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
	public String deleteCandidate(@PathVariable("id") long candidateId) {
		try {

			logger.info(" ==>> DELETING CANDIDATE << ==");
			if (candidateId > 0) {
				Candidate candidteEntity = candidateDAO.findById(candidateId).get();

				if (candidteEntity != null && candidteEntity.getIsActive() != null
						&& !candidteEntity.getIsActive().equalsIgnoreCase(IHRMSConstants.isNotActive)) {
					candidteEntity.setIsActive(IHRMSConstants.isNotActive);
					candidteEntity.setUpdatedDate(new Date());
					candidateDAO.save(candidteEntity);
					return HRMSHelper.sendSuccessResponse(IHRMSConstants.deletedsuccessMessage,
							IHRMSConstants.successCode);
				} else {
					throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
							IHRMSConstants.CandidateDoesnotExistMessage);
				}

			} else {
				throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
						IHRMSConstants.CandidateDoesnotExistMessage);
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

	/**
	 * This service is to get list of candidates,based on id,name,emailID and
	 * aadhar,and if no data the call the candidate.
	 * 
	 * Screen Manage candidate search
	 */
	@RequestMapping(value = "manageCandidateList/{orgId}/{candidateStatus}/{candidateFName}/{candidateLName}/{candidateEmail}/"
			+ "{aadhaarCard}/{candidateId}/{divisionId}/{page}/"
			+ "{size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getManageCandidateList(@PathVariable("orgId") long organizationId,
			@PathVariable("candidateStatus") String candidateStatus,
			@PathVariable("candidateFName") String candidateFName,
			@PathVariable("candidateLName") String candidateLName,
			@PathVariable("candidateEmail") String candidateEmail, @PathVariable("aadhaarCard") String aadhaarCard,
			@PathVariable("candidateId") long candidateId, @PathVariable("divisionId") long divisionId,
			@PathVariable("page") int page, @PathVariable("size") int size) {

		logger.info("Page No : " + page);
		logger.info("Page No : " + size);
		if (size <= 0) {
			size = 10;
		}

		int totalCount = 0;
		try {
			List<Candidate> candidateSetResult = null;
			PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");
			if (!HRMSHelper.isNullOrEmpty(candidateStatus) && candidateStatus.equalsIgnoreCase("CREATED")) {
				/*
				 * this condition is for fetch candidate list with filter because :: with search
				 * we need to show candidate with statuses : i) created ii) initiated
				 */
				if (!HRMSHelper.isNullOrEmpty(candidateFName) || !HRMSHelper.isNullOrEmpty(candidateLName)
						|| !HRMSHelper.isNullOrEmpty(candidateEmail) || !HRMSHelper.isNullOrEmpty(aadhaarCard)) {
					logger.info(
							" com.vinsys.dao.CandidateService :: getManageCandidateList() " + " :: find by filter ");
					if (HRMSHelper.isNullOrEmpty(candidateFName)) {
						candidateFName = "";
					} else {
						candidateFName = candidateFName.toUpperCase();
					}
					if (HRMSHelper.isNullOrEmpty(candidateLName)) {
						candidateLName = "";
					} else {
						candidateLName = candidateLName.toUpperCase();
					}
					if (HRMSHelper.isNullOrEmpty(candidateEmail)) {
						candidateEmail = "";
					} else {
						candidateEmail = candidateEmail.toUpperCase();
					}
					if (HRMSHelper.isNullOrEmpty(aadhaarCard)) {
						aadhaarCard = "";
					} else {
						aadhaarCard = aadhaarCard.toUpperCase();
					}
					candidateSetResult = candidateDAO.findManageCandidateWithFilter(organizationId,
							IHRMSConstants.CandidateStatus_CREATED, IHRMSConstants.CandidateStatus_INITIATED,
							IHRMSConstants.isActive, candidateFName, candidateLName, candidateEmail, aadhaarCard,
							divisionId, pageRequest);

					totalCount = candidateDAO.findManageCandidateWithFilter(organizationId,
							IHRMSConstants.CandidateStatus_CREATED, IHRMSConstants.CandidateStatus_INITIATED,
							IHRMSConstants.isActive, candidateFName, candidateLName, candidateEmail, aadhaarCard,
							divisionId);
					/*
					 * this condition is for fetch candidate list on load manage candidate page ::
					 * with search we need to show candidate with statuses : i) created
					 */
				} else {
					logger.info(" com.vinsys.dao.CandidateService :: getManageCandidateList() " + " :: findAll ");
					candidateSetResult = candidateDAO.findManageCandidate(organizationId,
							IHRMSConstants.CandidateStatus_CREATED, IHRMSConstants.isActive, divisionId, pageRequest);
					totalCount = candidateDAO.findManageCandidate(organizationId,
							IHRMSConstants.CandidateStatus_CREATED, IHRMSConstants.isActive, divisionId);

				}
			} else if (!HRMSHelper.isNullOrEmpty(candidateStatus) && candidateStatus.equalsIgnoreCase("ONBOARD")) {
				if (!HRMSHelper.isLongZero(candidateId)) {
					logger.info(
							" com.vinsys.dao.CandidateService :: getManageCandidateList() " + " :: find by filter ");
					candidateSetResult = candidateDAO.findHRWorkspaceCandidateWithFilter(organizationId,
							IHRMSConstants.CandidateStatus_ONBOARD, IHRMSConstants.CandidateStatus_ONBOARD,
							IHRMSConstants.isActive, candidateId, pageRequest);

					totalCount = candidateDAO.findHRWorkspaceCandidateWithFilter(organizationId,
							IHRMSConstants.CandidateStatus_ONBOARD, IHRMSConstants.CandidateStatus_ONBOARD,
							IHRMSConstants.isActive, candidateId);

				} else {
					logger.info(" com.vinsys.dao.CandidateService :: getManageCandidateList() " + " :: findAll ");
					candidateSetResult = candidateDAO.findManageCandidate(organizationId,
							IHRMSConstants.CandidateStatus_ONBOARD, IHRMSConstants.isActive, divisionId, pageRequest);
					totalCount = candidateDAO.findManageCandidate(organizationId,
							IHRMSConstants.CandidateStatus_ONBOARD, IHRMSConstants.isActive, divisionId);
				}
			}

			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> listResponse = new ArrayList<Object>();
			logger.info("No Of Offset == >> " + pageRequest.getOffset());

			if (HRMSHelper.isNullOrEmpty(candidateSetResult)) {
				throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
						IHRMSConstants.CandidateDoesnotExistMessage);
			}
			for (Candidate candidateEntity : candidateSetResult) {
				Object candidateModel = HRMSResponseTranslator.translateToCandidateModle(candidateEntity);
				listResponse.add(candidateModel);
			}
			response.setListResponse(listResponse);
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			response.setTotalCount(totalCount);
			response.setPageNo(page);
			response.setPageSize(size);
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

	@RequestMapping(value = "updateCandidateStatus/{candidateStatus}/{candidateId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updateCandidateStatus(@PathVariable("candidateStatus") String candidateStatus,
			@PathVariable("candidateId") long candidateId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(candidateStatus) && !HRMSHelper.isLongZero(candidateId)) {
				logger.info(" com.vinsys.dao.CandidateService :: updateCandidateStatus() ");
				Candidate candidate = candidateDAO.findById(candidateId).get();
				candidate.setCandidateStatus(candidateStatus);
				candidate.setUpdatedDate(new Date());
				candidateDAO.save(candidate);
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.CANDIDATE_PROCESSED_SUCCESFULLY,
						IHRMSConstants.successCode);
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

	/**
	 * This service is to get list of candidates,based on id,name, organization and
	 * division Screen Candidate . No pagination to apply
	 */
	@RequestMapping(value = "allCandidateNameIdList/{orgId}/"
			+ "{divisionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllCandidateNameIdList(@PathVariable("orgId") long organizationId,
			@PathVariable("divisionId") long divisionId) {

		try {
			List<Candidate> candidateSetResult = null;
			if (!HRMSHelper.isLongZero(organizationId) && !HRMSHelper.isLongZero(divisionId)) {
				candidateSetResult = candidateDAO.getAllCandidateNameIdListDAO(organizationId, divisionId,
						IHRMSConstants.isActive);
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}

			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> listResponse = new ArrayList<Object>();

			if (HRMSHelper.isNullOrEmpty(candidateSetResult)) {
				throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
						IHRMSConstants.CandidateDoesnotExistMessage);
			}

			listResponse = HRMSResponseTranslator.translateAllCandNameIdToVO(candidateSetResult, listResponse);

			response.setListResponse(listResponse);
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
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

	@RequestMapping(value = "onboardCandidateList/{orgId}/{divisionId}/{candidateName}/{fromDOJ}/{toDOJ}/{page}/"
			+ "{size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getOnboardCandidateList(@PathVariable("orgId") long organizationId,
			@PathVariable("divisionId") long divisionId, @PathVariable("candidateName") String candidateName,
			// @PathVariable("fromDOJ") @DateTimeFormat(pattern = "yyyy-MM-dd") Date
			// fromDOJNew,
			// @PathVariable("toDOJ") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDOJNew,
			@PathVariable("fromDOJ") String fromDOJ, @PathVariable("toDOJ") String toDOJ,
			@PathVariable("page") int page, @PathVariable("size") int size) {

		logger.info("Page No : " + page);
		logger.info("Page No : " + size);
		if (size <= 0) {
			size = 10;
		}

		// String fromDOJ = "";
		// String toDOJ ="";
		List<Candidate> candidateSetResult = null;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");
		String candidateStatus = IHRMSConstants.CandidateStatus_ONBOARD;
		int totalCount = 0;
		try {
			/*
			 * null check org and divisionId
			 */
			if (!HRMSHelper.isLongZero(organizationId) && !HRMSHelper.isLongZero(divisionId)) {
				if (!HRMSHelper.isNullOrEmpty(candidateName)
						|| !(HRMSHelper.isNullOrEmpty(fromDOJ) || HRMSHelper.isNullOrEmpty(toDOJ))) {
					/*
					 * throwing insufficient data for candidate name is null and only one of date
					 * filter is filled
					 */
					if ((HRMSHelper.isNullOrEmpty(fromDOJ) || HRMSHelper.isNullOrEmpty(toDOJ))
							&& HRMSHelper.isNullOrEmpty(candidateName)) {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					if (HRMSHelper.isNullOrEmpty(candidateName)) {
						candidateName = "";
					} else {
						candidateName = candidateName.toUpperCase();
					}
					if (HRMSHelper.isNullOrEmpty(fromDOJ) || HRMSHelper.isNullOrEmpty(toDOJ)) {
						fromDOJ = "";
						toDOJ = "";
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					// HRMSDateUtil.parse(date, format)
					Date fromDateObj = sdf.parse(fromDOJ);
					Date toDateObj = sdf.parse(toDOJ);
					Calendar c1 = Calendar.getInstance();
					Calendar c2 = Calendar.getInstance();
					c1.setTime(fromDateObj);
					c2.setTime(toDateObj);
					Date fromDateNew = c1.getTime();
					Date toDateNew = c2.getTime();
					candidateSetResult = candidateDAO.findOnboardCandidateWithFilterNameDOJ(organizationId, divisionId,
							candidateStatus, IHRMSConstants.isActive, candidateName, fromDOJ, toDOJ, pageRequest);
					totalCount = candidateDAO.findOnboardCandidateWithFilterNameDOJ(organizationId, divisionId,
							candidateStatus, IHRMSConstants.isActive, candidateName, fromDOJ, toDOJ);
				} else {
					candidateSetResult = candidateDAO.findOnboardCandidate(organizationId, divisionId, candidateStatus,
							IHRMSConstants.isActive, pageRequest);

					totalCount = candidateDAO.findOnboardCandidate(organizationId, divisionId, candidateStatus,
							IHRMSConstants.isActive);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> listResponse = new ArrayList<Object>();
			logger.info("No Of Offset == >> " + pageRequest.getOffset());

			if (HRMSHelper.isNullOrEmpty(candidateSetResult)) {
				throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
						IHRMSConstants.CandidateDoesnotExistMessage);
			}
			for (Candidate candidateEntity : candidateSetResult) {
				Object candidateModel = HRMSResponseTranslator.translateToCandidateModle(candidateEntity);
				listResponse.add(candidateModel);
			}
			response.setListResponse(listResponse);
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			response.setTotalCount(totalCount);
			response.setPageNo(page);
			response.setPageSize(size);
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

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getCandidateDetails(@PathVariable("id") long candidateId) {

		try {
			Candidate cand = candidateDAO.findById(candidateId).get();
			HRMSListResponseObject response = new HRMSListResponseObject();
			if (!HRMSHelper.isNullOrEmpty(cand)) {
				List<Object> listResponse = new ArrayList<Object>();
				Object candidateModel = HRMSResponseTranslator.translateToCandidateModle(cand);
				listResponse.add(candidateModel);

				logger.info("is Active Count ==== >> " + listResponse.size());
				response.setListResponse(listResponse);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(response);
			} else
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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

	@RequestMapping(value = "/testProbationNotification", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getCandidateForProbationNotification() {

		try {

			List<Object> listResponse = new ArrayList<Object>();
			List<Object[]> probationaryEmployeeList = candidateDAO.findEmployeeForProbationNotification(
					IHRMSConstants.isActive, IHRMSConstants.PROBATIONARYDESCRIPTION, probationReminderNoofDays);

			HRMSListResponseObject response = new HRMSListResponseObject();

			if (!HRMSHelper.isNullOrEmpty(probationaryEmployeeList)) {

				for (Object[] objects : probationaryEmployeeList) {
					VOEmployee empObj = new VOEmployee();

					VOCandidate candObj = new VOCandidate();
					candObj.setId(Long.parseLong(objects[1].toString()));
					candObj.setFirstName(objects[2].toString());
					candObj.setLastName(objects[3].toString());

					Date probationCompletionDate = null;
					VOCandidateProfessionalDetail voCandidateProfessionalDetail = new VOCandidateProfessionalDetail();
					voCandidateProfessionalDetail.setDateOfJoining(
							HRMSDateUtil.parse(objects[7].toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
					if (!HRMSHelper.isNullOrEmpty(voCandidateProfessionalDetail.getDateOfJoining())) {
						probationCompletionDate = HRMSDateUtil.incByMonth(
								voCandidateProfessionalDetail.getDateOfJoining(),
								Integer.parseInt(objects[8].toString()));
					}

					empObj.setId(Long.parseLong(objects[0].toString()));
					empObj.setOfficialEmailId(objects[4].toString());

					candObj.setEmployee(empObj);
					// candList.add(candObj);

					listResponse.add(candObj);

					long divisionId = Long.parseLong(objects[5].toString());
					long orgId = Long.parseLong(objects[6].toString());

					MasterDivision divisionEntity = divisionDAO.findById(divisionId).get();
					Organization organizationEntity = organizationDAO.findById(orgId).get();
					/**
					 * Code added by Ritesh Kolhe on 02-Jun-2021 to send P2C notification to HR as
					 * well.
					 */

					Employee tempEmpEntity = employeeDAO.findActiveEmployeeById(empObj.getId(),
							IHRMSConstants.isActive);
					logger.info(tempEmpEntity.getId() + " " + tempEmpEntity.getCandidate().getFirstName() + " "
							+ tempEmpEntity.getCandidate().getLastName());
					Organization organizationObj = tempEmpEntity.getCandidate().getLoginEntity().getOrganization();
					List<Object[]> empresult = employeeDAO.findHREmailId(organizationObj.getId(),
							IHRMSConstants.isActive);

					List<Employee> emplist = new ArrayList<>();

					if (empresult != null && !empresult.isEmpty()) {

						for (Object[] resultSet : empresult) {
							Employee employee = new Employee();
							employee.setId(Long.parseLong(String.valueOf(resultSet[0])));
							employee.setOfficialEmailId(String.valueOf(resultSet[3]));
							emplist.add(employee);
						}
					}
					String ccEmailIds = "";
					for (Employee hrobj : emplist) {
						ccEmailIds = ccEmailIds + ";" + hrobj.getOfficialEmailId();
					}

					// existing code starts
					EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
							IHRMSConstants.PRIOR_PROBATION_NOTIFICATION_TEMPLATE, IHRMSConstants.isActive,
							divisionEntity, organizationEntity);
					if (!HRMSHelper.isNullOrEmpty(template)) {
						Map<String, String> placeHolderMap = new HashMap<String, String>();
						placeHolderMap.put("{employeeName}", candObj.getFirstName() + " " + candObj.getLastName());
						placeHolderMap.put("{divisionName}", divisionEntity.getDivisionName());
						placeHolderMap.put("{probationCompletionDate}",
								HRMSDateUtil.format(probationCompletionDate, IHRMSConstants.FRONT_END_DATE_FORMAT));
						placeHolderMap.put("{probationPeriod}", objects[8].toString());
						String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

						// emailsender.toPersistEmail(empObj.getOfficialEmailId(), null,
						// mailBody,template.getEmailSubject(),divisionId,orgId);
						emailsender.toPersistEmail(empObj.getOfficialEmailId(), ccEmailIds, mailBody,
								template.getEmailSubject(), divisionId, orgId); // New Line tO pERSIST
					} else {
						throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
								IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
					}

				}
				if (!HRMSHelper.isNullOrEmpty(listResponse)) {

					logger.info("is Active Count ==== >> " + listResponse.size());
					response.setListResponse(listResponse);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(response);
				} else
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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

	// ************************************Retirement Remainder Schedular
	// Added*****************************
	// Author : Monika

	@RequestMapping(value = "/testRetiementNotificatiom", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getCandidateforRetirementNotification() {

		try {
			int retirmentDays = 0;

			List<Object> listResponse = new ArrayList<Object>();
			List<Object[]> retirementEmployeeList = candidateDAO.findEmployeeForRetirement(IHRMSConstants.isActive);

			HRMSListResponseObject response = new HRMSListResponseObject();

			if (!HRMSHelper.isNullOrEmpty(retirementEmployeeList)) {

				for (Object[] objects : retirementEmployeeList) {

					VOEmployee empObj = new VOEmployee();

					VOCandidate candObj = new VOCandidate();
					candObj.setId(Long.parseLong(objects[1].toString()));
					candObj.setFirstName(objects[2].toString());
					candObj.setLastName(objects[3].toString());

					Date retirementDate = null;
					VOEmployeeCurrentDetail voEmployeeurrentDetail = new VOEmployeeCurrentDetail();
					voEmployeeurrentDetail.setRetirementDate(
							HRMSDateUtil.parse(objects[7].toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
					logger.info("Retirement Date :" + objects[7].toString());
					// ================
//					if(!HRMSHelper.isNullOrEmpty(voEmployeeurrentDetail.getRetirementDate())) {
//						retirementDate = HRMSDateUtil.incByMonth(voEmployeeurrentDetail.getRetirementDate(), Integer.parseInt(objects[7].toString()));
//						logger.info("Integer RetirementDate :"+Integer.parseInt(objects[7].toString()));
//					}
					retirementDate = new SimpleDateFormat("yyyy-MM-dd").parse(objects[7].toString());

					// =============
					empObj.setId(Long.parseLong(objects[0].toString()));
					empObj.setOfficialEmailId(objects[4].toString());

					candObj.setEmployee(empObj);
					logger.info("Employee Object :" + empObj.getOfficialEmailId() + " " + empObj.getId());
					// candList.add(candObj);

					listResponse.add(candObj);
					logger.info("Candidate Object :" + candObj.getFirstName() + " " + candObj.getLastName());
					long divisionId = Long.parseLong(objects[5].toString());
					long orgId = Long.parseLong(objects[6].toString());

					MasterDivision divisionEntity = divisionDAO.findById(divisionId).get();
					logger.info("divisionEntity :" + divisionEntity.getId());
					Organization organizationEntity = organizationDAO.findById(orgId).get();
					logger.info("organizationEntity :" + organizationEntity.getId());

					Employee tempEmpEntity = employeeDAO.findActiveEmployeeById(empObj.getId(),
							IHRMSConstants.isActive);

					Organization organizationObj = tempEmpEntity.getCandidate().getLoginEntity().getOrganization();
					List<Object[]> empresult = employeeDAO.findHREmailId(organizationObj.getId(),
							IHRMSConstants.isActive);

					List<Employee> emplist = new ArrayList<>();

					if (empresult != null && !empresult.isEmpty()) {

						for (Object[] resultSet : empresult) {
							Employee employee = new Employee();
							employee.setId(Long.parseLong(String.valueOf(resultSet[0])));
							employee.setOfficialEmailId(String.valueOf(resultSet[3]));
							emplist.add(employee);
						}
					}
					String ccEmailIds = "";
					for (Employee hrobj : emplist) {
						ccEmailIds = ccEmailIds + ";" + hrobj.getOfficialEmailId();
					}

					EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
							IHRMSConstants.PRIOR_RETIREMENT_NOTIFICATION_TEMPLATE, IHRMSConstants.isActive,
							divisionEntity, organizationEntity);
					if (!HRMSHelper.isNullOrEmpty(template)) {
						Map<String, String> placeHolderMap = new HashMap<String, String>();
						placeHolderMap.put("{employeeName}", candObj.getFirstName() + " " + candObj.getLastName());
						placeHolderMap.put("{divisionName}", divisionEntity.getDivisionName());
						placeHolderMap.put("{RetirementDate}",
								HRMSDateUtil.format(retirementDate, IHRMSConstants.FRONT_END_DATE_FORMAT));
						// placeHolderMap.put("{RetirementDate}",objects[7].toString());
						String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());
						// logger.info(""+template.getId()+" "+template.getEmailSubject()+"
						// "+template.getTemplate()+""+template.getTemplateName()+""+template.getOrganization().getId());
						// emailsender.toPersistEmail(empObj.getOfficialEmailId(), null,
						// mailBody,template.getEmailSubject(),divisionId,orgId);
						emailsender.toPersistEmail(empObj.getOfficialEmailId(), ccEmailIds, mailBody,
								template.getEmailSubject(), divisionId, orgId); // New Line tO pERSIST
					} else {
						throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
								IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
					}

				} // for end
				if (!HRMSHelper.isNullOrEmpty(listResponse)) {

					logger.info("is Active Count ==== >> " + listResponse.size());
					response.setListResponse(listResponse);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(response);
				} else
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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

	// *****************************Retirement Remainderbefore 7Days Schedular
	// Added*****************************
	// Author : Monika

	@RequestMapping(value = "/testRetiementCompelitionAlert", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getCandidateforRetirementCompletion() {

		try {
			int retirmentDays = 0;

			List<Object> listResponse = new ArrayList<Object>();

			List<Object[]> retirementEmployeeList = candidateDAO
					.findEmployeeForRetirementCompletion(IHRMSConstants.isActive);

			HRMSListResponseObject response = new HRMSListResponseObject();

			if (!HRMSHelper.isNullOrEmpty(retirementEmployeeList)) {

				for (Object[] objects : retirementEmployeeList) {

					VOEmployee empObj = new VOEmployee();

					VOCandidate candObj = new VOCandidate();
					candObj.setId(Long.parseLong(objects[1].toString()));
					candObj.setFirstName(objects[2].toString());
					candObj.setLastName(objects[3].toString());

					Date retirementDate = null;
					VOEmployeeCurrentDetail voEmployeeurrentDetail = new VOEmployeeCurrentDetail();
					voEmployeeurrentDetail.setRetirementDate(
							HRMSDateUtil.parse(objects[7].toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
					logger.info("Retirement Date :" + objects[7].toString());
					// ================
//					if(!HRMSHelper.isNullOrEmpty(voEmployeeurrentDetail.getRetirementDate())) {
//						retirementDate = HRMSDateUtil.incByMonth(voEmployeeurrentDetail.getRetirementDate(), Integer.parseInt(objects[7].toString()));
//						logger.info("Integer RetirementDate :"+Integer.parseInt(objects[7].toString()));
//					}
					retirementDate = new SimpleDateFormat("yyyy-MM-dd").parse(objects[7].toString());

					// =============
					empObj.setId(Long.parseLong(objects[0].toString()));
					empObj.setOfficialEmailId(objects[4].toString());

					candObj.setEmployee(empObj);
					logger.info("Employee Object :" + empObj.getOfficialEmailId() + " " + empObj.getId());
					// candList.add(candObj);

					listResponse.add(candObj);
					logger.info("Candidate Object :" + candObj.getFirstName() + " " + candObj.getLastName());
					long divisionId = Long.parseLong(objects[5].toString());
					long orgId = Long.parseLong(objects[6].toString());

					MasterDivision divisionEntity = divisionDAO.findById(divisionId).get();
					logger.info("divisionEntity :" + divisionEntity.getId());
					Organization organizationEntity = organizationDAO.findById(orgId).get();
					logger.info("organizationEntity :" + organizationEntity.getId());

					Employee tempEmpEntity = employeeDAO.findActiveEmployeeById(empObj.getId(),
							IHRMSConstants.isActive);

					Organization organizationObj = tempEmpEntity.getCandidate().getLoginEntity().getOrganization();
					List<Object[]> empresult = employeeDAO.findHREmailId(organizationObj.getId(),
							IHRMSConstants.isActive);

					List<Employee> emplist = new ArrayList<>();

					if (empresult != null && !empresult.isEmpty()) {

						for (Object[] resultSet : empresult) {
							Employee employee = new Employee();
							employee.setId(Long.parseLong(String.valueOf(resultSet[0])));
							employee.setOfficialEmailId(String.valueOf(resultSet[3]));
							emplist.add(employee);
						}
					}
					String ccEmailIds = "";
					for (Employee hrobj : emplist) {
						ccEmailIds = ccEmailIds + ";" + hrobj.getOfficialEmailId();
					}

					EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
							IHRMSConstants.PRIOR_RETIREMENT_NOTIFICATION2_TEMPLATE, IHRMSConstants.isActive,
							divisionEntity, organizationEntity);
					if (!HRMSHelper.isNullOrEmpty(template)) {
						Map<String, String> placeHolderMap = new HashMap<String, String>();
						placeHolderMap.put("{employeeName}", candObj.getFirstName() + " " + candObj.getLastName());
						placeHolderMap.put("{divisionName}", divisionEntity.getDivisionName());
						placeHolderMap.put("{RetirementDate}",
								HRMSDateUtil.format(retirementDate, IHRMSConstants.FRONT_END_DATE_FORMAT));
						// placeHolderMap.put("{RetirementDate}",objects[7].toString());
						String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());
						// logger.info(""+template.getId()+" "+template.getEmailSubject()+"
						// "+template.getTemplate()+""+template.getTemplateName()+""+template.getOrganization().getId());
						// emailsender.toPersistEmail(empObj.getOfficialEmailId(), null,
						// mailBody,template.getEmailSubject(),divisionId,orgId);
						emailsender.toPersistEmail(empObj.getOfficialEmailId(), ccEmailIds, mailBody,
								template.getEmailSubject(), divisionId, orgId); // New Line tO pERSIST
					} else {
						throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
								IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
					}

				} // for end
				if (!HRMSHelper.isNullOrEmpty(listResponse)) {

					logger.info("is Active Count ==== >> " + listResponse.size());
					response.setListResponse(listResponse);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(response);
				} else
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
