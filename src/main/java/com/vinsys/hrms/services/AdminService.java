package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateAddressDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateLetterDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
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
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
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
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;
@Hidden @RestController
@RequestMapping(path = "/admin")

public class AdminService {

	private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

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

	/**
	 * This service is to create admin,with basic information,basic professional
	 * information,basic personal information,candidate address.
	 * 
	 * This services doesn't have any screen
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createAdmin(@RequestBody VOCandidate candidateModel) {
		try {

			logger.info(" == >> CREATING ADMIN << ==");

			Candidate emailCheck = null;
			if (candidateModel != null && !HRMSHelper.isNullOrEmpty(candidateModel.getEmailId())) {

				emailCheck = candidateDAO.findByemailId(candidateModel.getEmailId());
				if (emailCheck == null) {

					if (candidateModel.getLoginEntity() != null
							&& candidateModel.getLoginEntity().getOrganization() != null) {

						Candidate candidateEntity = HRMSRequestTranslator
								.translateToCandidateEntityToPersist(candidateModel);
						candidateEntity.setCandidateStatus(IHRMSConstants.AdminStatus);

						/**
						 * Assigning Organization To admin
						 */
						Organization organization = organizationDAO
								.findById(candidateModel.getLoginEntity().getOrganization().getId()).get();
						candidateEntity.getLoginEntity().setOrganization(organization);

						/**
						 * Finding And Assigning The Masters To admin
						 */
						MasterBranch masterBranch = branchDAO
								.findById(candidateModel.getCandidateProfessionalDetail().getBranch().getId()).get();
						MasterDivision masterDivision = divisionDAO
								.findById(candidateModel.getCandidateProfessionalDetail().getDivision().getId()).get();
						MasterDesignation masterDesignation = designationDAO
								.findById(candidateModel.getCandidateProfessionalDetail().getDesignation().getId()).get();
						MasterDepartment masterDepartment = departmenDAO
								.findById(candidateModel.getCandidateProfessionalDetail().getDepartment().getId()).get();
						MasterCity masterCity = masterCityDAO
								.findById(candidateModel.getCandidateProfessionalDetail().getCity().getId()).get();
						MasterCountry masterCountry = masterCountryDAO
								.findById(candidateModel.getCandidateProfessionalDetail().getCountry().getId()).get();
						MasterState masterState = masterStateDAO
								.findById(candidateModel.getCandidateProfessionalDetail().getState().getId()).get();

						/**
						 * Persisting admin Login Entity and Login Entity Type Details
						 */
						LoginEntity loginEntity = null;
						LoginEntityType loginEntityType = null;
						if (!HRMSHelper.isNullOrEmpty(organization)) {
							loginEntity = candidateEntity.getLoginEntity();
							loginEntityType = loginEntityTypeDAO.findByRoleName(candidateModel.getRoleToBeCreated(),
									organization.getId());
						}

						if (loginEntityType == null) {
							logger.info("Role Not Found");
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.RoleDoesnotExistMessage);
						}

						/*
						 * changed by ssw on 01jan2018
						 * for : manyToMany for login entity type
						 */
						//loginEntity.setLoginEntityType(loginEntityType);
						Set<LoginEntityType> loginEntityTypes = new HashSet<LoginEntityType>();
						loginEntityTypes.add(loginEntityType);
						loginEntity.setLoginEntityTypes(loginEntityTypes);
						/*
						 * up to this modified by ssw
						 */
						candidateEntity.setLoginEntity(loginEntity);

						Candidate candidateNew = candidateDAO.save(candidateEntity);

						/**
						 * Persisting admin Personal Details
						 */
						CandidatePersonalDetail candidatePersonalDetail = new CandidatePersonalDetail();
						candidatePersonalDetail
								.setMaritalStatus(candidateModel.getCandidatePersonalDetail().getMaritalStatus());
						candidatePersonalDetail.setCandidate(candidateNew);
						candidatePersonalDetail.setCreatedBy(candidateModel.getCreatedBy());
						candidatePersonalDetail.setCreatedDate(new Date());
						candidatePersonalDetail = personalDetailsDAO.save(candidatePersonalDetail);

						/**
						 * Persisting admin Professional Details
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

						
						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> listResponse = new ArrayList<Object>();
						Candidate candidateRetrived = candidateDAO.findById(candidateNew.getId()).get();
						candidateRetrived.setCandidatePersonalDetail(candidatePersonalDetail);
						// candidateRetrived.setCandidateAddress(newAddressSet);
						Object candidateVo = HRMSResponseTranslator.translateToCandidateModle(candidateRetrived);
						listResponse.add(candidateVo);
						response.setListResponse(listResponse);
						response.setTotalCount(listResponse.size());

						/**
						 * To send email notification to admin
						 */
						Map<String, String> mailContentMap = new HashMap<String, String>();
						mailContentMap.put("{employeeFirstName}", candidateRetrived.getFirstName());
						mailContentMap.put("{employeeMiddleName}", candidateRetrived.getMiddleName());
						mailContentMap.put("{employeeLastName}", candidateRetrived.getLastName());
						mailContentMap.put("{employeeOfficialEmail}", candidateRetrived.getEmailId());
						mailContentMap.put("{employeePassword}", candidateRetrived.getLoginEntity().getPassword());

						String mailContent = HRMSHelper.replaceString(mailContentMap,
								IHRMSEmailTemplateConstants.Template_Employee_Change_Password);
						/*EmailSender.toSendEmail(candidateRetrived.getEmailId(), null, mailContent,
								IHRMSConstants.AdminPasswordSubject);*/
						return HRMSHelper.createJsonString(response);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
				} else {
					throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.emailAlreadyExistMessage);
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
