package com.vinsys.hrms.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateAddressDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateQualificationDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeAdditionalRolesDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationDAO;
import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterBranchDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSMasterDesignationDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.dao.IHRMSRoleMasterDAO;
import com.vinsys.hrms.dao.IHRMSWorkshiftDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidate;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOEmployeeCurrentDetail;
import com.vinsys.hrms.datamodel.VOLoginEntityType;
import com.vinsys.hrms.datamodel.VOMasterWorkshift;
import com.vinsys.hrms.datamodel.employeedetails.VOEmployeeCurrentOrganisationDetails;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCurrentDetail;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.entity.MasterWorkshift;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/employeeCurrentOrganization")

public class EmployeeCurrentOrganizationDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeCurrentOrganizationDetailsService.class);

	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSLoginEntityTypeDAO loginEntityTypeDAO;
	@Autowired
	IHRMSMasterBranchDAO branchDAO;
	@Autowired
	IHRMSMasterDepartmentDAO deptDAO;
	@Autowired
	IHRMSMasterDesignationDAO designationDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;
	@Autowired
	IHRMSMasterEmployementTypeDAO employmentTypeDAO;
	@Autowired
	IHRMSEmployeeAdditionalRolesDAO additionalRolesDAO;
	@Autowired
	IHRMSWorkshiftDAO workshiftDAO;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSRoleMasterDAO roleMasterDAO;
	@Autowired
	IHRMSMasterCityDAO masterCityDAO;
	@Autowired
	IHRMSMasterCountryDAO masterCountryDAO;
	@Autowired
	IHRMSMasterStateDAO masterStateDAO;

	@Autowired
	IHRMSEmployeeAcnDAO employeeACN;

	@Autowired
	IHRMSEmployeeSeparationDAO employeeSeparationDAO;
	@Autowired
	IHRMSCandidateAddressDAO candidateAddressDAO;
	@Autowired
	IHRMSCandidateQualificationDAO candidateQualification;
	@Autowired
	IHRMSProfessionalDetailsDAO candidateProfessionalDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createCandidateCurrentOrganizationDetails(@RequestBody VOEmployee employeeObject) {
		logger.info("Inside createCandidateCurrentOrganizationDetails Method");
		try {

			/**
			 * This Service Will Get Changed As Per New Structure 06/01/2018 Saurabh & Shome
			 * is working here UNDER CONSTRUCTION
			 */
			if (!HRMSHelper.isNullOrEmpty(employeeObject) && !HRMSHelper.isNullOrEmpty(employeeObject.getCandidate())
					&& !HRMSHelper.isNullOrEmpty(employeeObject.getCandidate().getLoginEntity())
					&& !HRMSHelper.isNullOrEmpty(employeeObject.getCandidate().getEmploymentType())
					&& !HRMSHelper.isNullOrEmpty(employeeObject.getCandidate().getCandidateProfessionalDetail())
					&& !HRMSHelper.isNullOrEmpty(
							employeeObject.getCandidate().getCandidateProfessionalDetail().getDepartment())
					&& !HRMSHelper.isNullOrEmpty(
							employeeObject.getCandidate().getCandidateProfessionalDetail().getDesignation())
					&& !HRMSHelper
							.isNullOrEmpty(employeeObject.getCandidate().getCandidateProfessionalDetail().getDivision())
					&& !HRMSHelper
							.isNullOrEmpty(employeeObject.getCandidate().getCandidateProfessionalDetail().getBranch())
					&& !HRMSHelper
							.isNullOrEmpty(employeeObject.getCandidate().getCandidateProfessionalDetail().getCountry())
					&& !HRMSHelper
							.isNullOrEmpty(employeeObject.getCandidate().getCandidateProfessionalDetail().getState())
					&& !HRMSHelper
							.isNullOrEmpty(employeeObject.getCandidate().getCandidateProfessionalDetail().getCity())
					&& !HRMSHelper.isNullOrEmpty(
							employeeObject.getCandidate().getCandidateProfessionalDetail().getDateOfJoining())
					&& !HRMSHelper.isNullOrEmpty(employeeObject.getCandidate().getEmploymentType())
					&& !HRMSHelper.isNullOrEmpty(employeeObject.getOfficialEmailId())) {

				Employee employee = employeeDAO.findById(employeeObject.getId()).get();
				logger.info(
						"Add Organization Details For Employee Id :" + employee.getCandidate().getEmployee().getId());

				if (employee != null) {

					VOCandidate candidateModel = employeeObject.getCandidate();
					Set<VOLoginEntityType> roles = employeeObject.getCandidate().getLoginEntity().getLoginEntityTypes();
					Set<LoginEntityType> rolesEntity = employee.getCandidate().getLoginEntity().getLoginEntityTypes();
					rolesEntity = new HashSet<LoginEntityType>();

					for (VOLoginEntityType loginEntityType : roles) {
						LoginEntityType newRolesLoginEntity = loginEntityTypeDAO.findById(loginEntityType.getId())
								.get();
						rolesEntity.add(newRolesLoginEntity);
					}
					employee.getCandidate().getLoginEntity().setLoginEntityTypes(rolesEntity);
					logger.info("Assign Role To Employee : "
							+ employee.getCandidate().getLoginEntity().getLoginEntityName());

					MasterBranch masterBranch = branchDAO
							.findById(candidateModel.getCandidateProfessionalDetail().getBranch().getId()).get();

					if (masterBranch != null) {
						employee.getCandidate().getCandidateProfessionalDetail().setBranch(masterBranch);
						logger.info("Assign Branch To Employee :" + masterBranch.getBranchName());
					}

					/*
					 * EmployeeBranch employeeBranch = employee.getEmployeeBranch(); if
					 * (employeeBranch != null) {
					 * logger.info(" == >> UPDATING EMPLOYEE BRANCH << == "); employeeBranch =
					 * HRMSRequestTranslator.translateToEmployeBranchEntity(employeeBranch,
					 * employeeCurrentOrganizationRequest); } else {
					 * logger.info(" == >> CREATING EMPLOYEE BRANCH << == "); employeeBranch = new
					 * EmployeeBranch(); employeeBranch =
					 * HRMSRequestTranslator.translateToEmployeBranchEntity(employeeBranch,
					 * employeeCurrentOrganizationRequest); }
					 * 
					 * employeeBranch.setBranch(masterBranch); employeeBranch.setEmployee(employee);
					 * employee.setEmployeeBranch(employeeBranch);
					 */

					MasterDepartment masterDepartment = deptDAO
							.findById(candidateModel.getCandidateProfessionalDetail().getDepartment().getId()).get();
					if (masterDepartment != null) {
						employee.getCandidate().getCandidateProfessionalDetail().setDepartment(masterDepartment);
						logger.info("Assign Department To Employee :" + masterDepartment.getDepartmentName());
					}

					/*
					 * EmployeeDepartment employeeDepartment = employee.getEmployeeDepartment(); if
					 * (employeeDepartment != null) {
					 * logger.info(" == >> UPDATING EMPLOYEE DEPARTMENT << == "); employeeDepartment
					 * = HRMSRequestTranslator.translateToEmployeeDepartmentEntity(
					 * employeeDepartment, employeeCurrentOrganizationRequest); } else {
					 * logger.info(" == >> CREATING EMPLOYEE DEPARTMENT << == "); employeeDepartment
					 * = new EmployeeDepartment(); employeeDepartment =
					 * HRMSRequestTranslator.translateToEmployeeDepartmentEntity(
					 * employeeDepartment, employeeCurrentOrganizationRequest); }
					 * 
					 * employeeDepartment.setEmployee(employee);
					 * employeeDepartment.setDepartment(masterDepartment);
					 * employee.setEmployeeDepartment(employeeDepartment);
					 */

					MasterDesignation masterDesignation = designationDAO
							.findById(candidateModel.getCandidateProfessionalDetail().getDesignation().getId()).get();
					if (masterDesignation != null) {
						employee.getCandidate().getCandidateProfessionalDetail().setDesignation(masterDesignation);
						logger.info("Assign Designation To Employee :" + masterDesignation.getDesignationName());
					}

					/*
					 * EmployeeDesignation employeeDesignation = employee.getEmployeeDesignation();
					 * if (employeeDesignation != null) {
					 * 
					 * logger.info(" == >> UPDATING EMPLOYEE DESIGNATION << == ");
					 * employeeDesignation =
					 * HRMSRequestTranslator.translateToEmployeeDesignationEntity(
					 * employeeDesignation, employeeCurrentOrganizationRequest); } else {
					 * logger.info(" == >> CREATING EMPLOYEE DESIGNATION << == ");
					 * employeeDesignation = new EmployeeDesignation(); employeeDesignation =
					 * HRMSRequestTranslator.translateToEmployeeDesignationEntity(
					 * employeeDesignation, employeeCurrentOrganizationRequest); }
					 * 
					 * employeeDesignation.setEmployee(employee);
					 * employeeDesignation.setDesignation(masterDesignation);
					 * employee.setEmployeeDesignation(employeeDesignation);
					 */

					MasterDivision masterDivision = divisionDAO
							.findById(candidateModel.getCandidateProfessionalDetail().getDivision().getId()).get();
					if (masterDivision != null) {
						employee.getCandidate().getCandidateProfessionalDetail().setDivision(masterDivision);
						logger.info("Assign Division To Employee :" + masterDivision.getDivisionName());
					}

					/*
					 * EmployeeDivision employeeDivision = employee.getEmployeeDivision(); if
					 * (employeeDivision != null) {
					 * logger.info(" == >> UPDATING EMPLOYEE DIVISION << == ");
					 * HRMSRequestTranslator.translateToEmployeeDivisionEntity(employeeDivision,
					 * employeeCurrentOrganizationRequest); } else {
					 * logger.info(" == >> CREATING EMPLOYEE DESIGNATION << == "); employeeDivision
					 * = new EmployeeDivision(); employeeDivision =
					 * HRMSRequestTranslator.translateToEmployeeDivisionEntity(employeeDivision,
					 * employeeCurrentOrganizationRequest); }
					 * 
					 * employeeDivision.setEmployee(employee);
					 * employeeDivision.setDivision(masterDivision);
					 * employee.setEmployeeDivision(employeeDivision);
					 */

					MasterEmploymentType masterEmploymentType = employmentTypeDAO
							.findById(candidateModel.getEmploymentType().getId()).get();
					if (masterEmploymentType != null) {
						employee.getCandidate().setEmploymentType(masterEmploymentType);
						logger.info("Assign Employee Type Name:" + masterEmploymentType.getEmploymentTypeName());
					}

					/*
					 * EmployeeEmploymentType employeeEmploymentType =
					 * employee.getEmployeeEmploymentType();
					 * 
					 * if (employeeEmploymentType != null) {
					 * logger.info(" == >> UPDATING EMPLOYMENT TYPE << == ");
					 * HRMSRequestTranslator.translateToEmployementTypeEntity(
					 * employeeEmploymentType, employeeObject);
					 * 
					 * } else { logger.info(" == >> CREATING EMPLOYMENT TYPE << == ");
					 * employeeEmploymentType = new EmployeeEmploymentType();
					 * HRMSRequestTranslator.translateToEmployementTypeEntity(
					 * employeeEmploymentType, employeeObject); }
					 * 
					 * employeeEmploymentType.setEmployee(employee);
					 * employeeEmploymentType.setEmploymentType(masterEmploymentType);
					 * employee.setEmployeeEmploymentType(employeeEmploymentType);
					 */

					EmployeeReportingManager employeeReportingManager = employee.getEmployeeReportingManager();
					Employee reportingManager = null;

					if (employeeReportingManager != null) {

						logger.info(" Updating Reporting Manager ");
						reportingManager = employeeDAO
								.findById(employeeObject.getEmployeeReportingManager().getReportingManager().getId()).get();
						if (reportingManager != null) {
							employeeReportingManager.setReporingManager(reportingManager);
							employeeReportingManager.setUpdatedBy(employeeObject.getUpdatedBy());
							employeeReportingManager.setUpdatedDate(new Date());
							logger.info("***********Updating Reporting Manager Of Employee****************");
							logger.info(
									"Updating Reporting Manager Of Employee :" + reportingManager.getId().toString());
						}
					} else {
						logger.info(" Creating Reporting Manager ");
						employeeReportingManager = new EmployeeReportingManager();
						reportingManager = employeeDAO
								.findById(employeeObject.getEmployeeReportingManager().getReportingManager().getId())
								.get();
						employeeReportingManager.setReporingManager(reportingManager);
						employeeReportingManager.setCreatedBy(employeeObject.getCreatedBy());
						employeeReportingManager.setCreatedDate(new Date());
						logger.info("Assign Reporting Manager Of Employee :" + reportingManager.getId().toString());
					}

					employeeReportingManager.setEmployee(employee);
					employeeReportingManager.setIsActive(IHRMSConstants.isActive);
					employeeReportingManager.setRemark(employeeObject.getRemark());
					employee.setEmployeeReportingManager(employeeReportingManager);

					String responseMessage = IHRMSConstants.successMessage;

					EmployeeCurrentDetail employeeCurrentDetail = employee.getEmployeeCurrentDetail();
					MasterWorkshift masterWorkshift = null;
					if (employeeCurrentDetail != null) {

						logger.info(": Updating Current Organization Details : ");
						employeeCurrentDetail = HRMSRequestTranslator
								.translateToEmployeeCurrentDetailEntity(employeeCurrentDetail, employeeObject);
						masterWorkshift = workshiftDAO
								.findById(employeeObject.getEmployeeCurrentDetail().getWorkshift().getId()).get();
						if (masterWorkshift != null) {
							employeeCurrentDetail.setWorkshift(masterWorkshift);
							logger.info("Assign WorkShift To Employee :" + masterWorkshift.getWorkshiftName());
						}

						responseMessage = IHRMSConstants.updatedsuccessMessage;
					} else {

						logger.info(" == >> CREATING CURRENT ORGANIZATION DETAILS << == ");
						employeeCurrentDetail = new EmployeeCurrentDetail();
						masterWorkshift = workshiftDAO
								.findById(employeeObject.getEmployeeCurrentDetail().getWorkshift().getId()).get();
						employeeCurrentDetail.setWorkshift(masterWorkshift);
						employeeCurrentDetail = HRMSRequestTranslator
								.translateToEmployeeCurrentDetailEntity(employeeCurrentDetail, employeeObject);
						logger.info("Assign WorkShift To Employee :" + masterWorkshift.getWorkshiftName());

					}

					/**
					 * Setting candidate professional details CITY ,STATE,COUNTRY
					 */
					MasterCity masterCity = masterCityDAO
							.findById(candidateModel.getCandidateProfessionalDetail().getCity().getId()).get();
					logger.info("Assign City To Employee :" + masterCity.getCityName());
					MasterCountry masterCountry = masterCountryDAO
							.findById(candidateModel.getCandidateProfessionalDetail().getCountry().getId()).get();
					logger.info("Assign Country To Employee :" + masterCountry.getCountryName());
					MasterState masterState = masterStateDAO
							.findById(candidateModel.getCandidateProfessionalDetail().getState().getId()).get();
					logger.info("Assign State To Employee :" + masterState.getStateName());

					employee.getCandidate().getCandidateProfessionalDetail().setCity(masterCity);
					employee.getCandidate().getCandidateProfessionalDetail().setCountry(masterCountry);
					employee.getCandidate().getCandidateProfessionalDetail().setState(masterState);
					employeeCurrentDetail.setEmployee(employee);
					employee.setEmployeeCurrentDetail(employeeCurrentDetail);

					employee.getCandidate().getCandidateProfessionalDetail()
							.setDateOfJoining(candidateModel.getCandidateProfessionalDetail().getDateOfJoining());
					employee.setOfficialEmailId(employeeObject.getOfficialEmailId());
					employee.setProbationPeriod(employeeObject.getEmployeeProbationPeriod());
					employee = employeeDAO.save(employee);
					logger.info("Employee Add Information SuccessFully");

					if (!HRMSHelper.isNullOrEmpty(employeeObject.getEmpACN())) {
						EmployeeACN employeeACNEntity = employee.getEmployeeACN();
						if (HRMSHelper.isNullOrEmpty(employee.getEmployeeACN())) {
							employeeACNEntity = new EmployeeACN();
							employeeACNEntity.setIsActive(IHRMSConstants.isActive);
							employeeACNEntity.setEmployee(employee);
						}
						employeeACNEntity.setEmpACN(Long.parseLong(employeeObject.getEmpACN()));
						employeeACNEntity.setUpdatedDate(new Date());
						logger.info("Updating Employee Access Card No.(ACN):" + employeeObject.getEmpACN() + " EMP ID:"
								+ employee.getId());

						employeeACN.save(employeeACNEntity);
					}

					BaseId response = new BaseId();
					response.setId(employee.getId());
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(responseMessage);
					return HRMSHelper.createJsonString(response);

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode,
							IHRMSConstants.EmployeeDoesnotExistMessage);
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

	@RequestMapping(method = RequestMethod.GET, value = "/{employeeId}", produces = "application/json")
	@ResponseBody
	public String getEmployeeCurrentOrganizationDetails(@PathVariable("employeeId") long employeeId) {
		try {

			Employee employee = employeeDAO.findById(employeeId).get();
			if (employee != null) {

				VOEmployeeCurrentDetail employeeCurrentDetailModel = HRMSEntityToModelMapper
						.convertToEmployeCurrentDetailModel(employee.getEmployeeCurrentDetail());
				if (employee.getEmployeeCurrentDetail() != null) {
					VOMasterWorkshift workshift = HRMSEntityToModelMapper
							.convertToWorkshiftModel(employee.getEmployeeCurrentDetail().getWorkshift());
					employeeCurrentDetailModel.setWorkshift(workshift);
				} else {
					employeeCurrentDetailModel = new VOEmployeeCurrentDetail();
					employeeCurrentDetailModel.setWorkshift(new VOMasterWorkshift());
				}

				VOEmployee employeeModel = HRMSEntityToModelMapper.convertToEmployeeModel(employee);
				employeeModel.getCandidate().setCandidateProfessionalDetail(
						HRMSEntityToModelMapper.convertToCandidateProfessionalDetailsDetailModel(
								employee.getCandidate().getCandidateProfessionalDetail()));
				// VOCandidate candidate =
				// HRMSResponseTranslator.translateToCandidateModle(employee.getCandidate());
				employeeModel.setEmployeeCurrentDetail(employeeCurrentDetailModel);

				List<Object> listResponse = new ArrayList<Object>();
				listResponse.add(employeeModel);
				HRMSListResponseObject resp = new HRMSListResponseObject();
				resp.setListResponse(listResponse);
				resp.setResponseCode(IHRMSConstants.successCode);
				resp.setResponseMessage(IHRMSConstants.successMessage);

				return HRMSHelper.createJsonString(resp);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EmployeeDoesnotExistMessage);
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
	 * 
	 * @Author: Monika Gargote.
	 * @description: for getting all employee details in one excel sheet.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/employeeDetails", produces = "application/json")
	@ResponseBody
	public void getEmployeeCurrentDetails(HttpServletResponse res) throws ParseException {
		try {
			List<VOEmployeeCurrentOrganisationDetails> employeeList = new ArrayList<>();
			List<Object[]> empdetails = candidateDAO.findEmployeeDetails();
			for (Object[] objects : empdetails) {
				VOEmployeeCurrentOrganisationDetails employeeResponse = new VOEmployeeCurrentOrganisationDetails();

				String employeeId = objects[0] == null ? null : objects[0].toString();
				employeeResponse.setEmployee_id(Long.parseLong(employeeId));
				logger.info("employee Id :" + objects[0]);

				String officalMailId = objects[1] == null ? null : objects[1].toString();
				employeeResponse.setEmployeeofficialnMailId(officalMailId);
				logger.info("Employee OfficialMaild :" + objects[1]);

				String isActive = objects[2] == null ? null : objects[2].toString();
				String empStatus = "";
				if (isActive.toUpperCase().equals("Y")) {
					empStatus = "Active";
				}
				if (isActive.toUpperCase().equals("N")) {
					empStatus = "InActive";
				}
				employeeResponse.setIsActive(empStatus);
				logger.info("Is Active :" + objects[2]);

				String dateOfBirth = objects[3] == null ? null : objects[3].toString();
				employeeResponse.setDateOfBirth(dateOfBirth);
				logger.info("date of Birth:" + objects[3]);

				String personalMailId = objects[4] == null ? null : objects[4].toString();
				employeeResponse.setEmployeePersonalMailId(personalMailId);
				logger.info("Employee Personal mail id" + objects[4]);

				String firstName = objects[5] == null ? null : objects[5].toString();
				String middleName = objects[6] == null ? null : objects[6].toString();
				String lastName = objects[7] == null ? null : objects[7].toString();
				if (middleName == null) {
					middleName = "";
					employeeResponse.setEmployee_name(firstName + " " + middleName + " " + lastName);
				} else {
					employeeResponse.setEmployee_name(firstName + " " + middleName + " " + lastName);
				}
				logger.info("Employee name :" + firstName + " " + lastName);

				String contactNumber = objects[8] == null ? null : objects[8].toString();
				employeeResponse.setContactNumber(contactNumber);
				logger.info("Contact Number :" + objects[8]);

				String gender = objects[9] == null ? null : objects[9].toString();
				employeeResponse.setGender(gender);
				logger.info("Gender :" + objects[9]);

				String adharCardNo = objects[10] == null ? null : objects[10].toString();
				employeeResponse.setAddharCardNo(adharCardNo);
				logger.info("AddharCardNo :" + objects[10]);

				String dateOfJoining = objects[11] == null ? null : objects[11].toString();
				employeeResponse.setDateOfjoining(dateOfJoining);
				logger.info("Employee Date of Joining :" + objects[11]);

				String pancard = objects[12] == null ? null : objects[12].toString();
				employeeResponse.setPancardNo(pancard);
				logger.info("employee pancard :" + pancard);

				String branchId = objects[13] == null ? null : objects[13].toString();
				MasterBranch masterBranch = branchDAO.findById(Long.valueOf(branchId)).get();
				employeeResponse.setBranch(masterBranch.getBranchName());

				String divisionId = objects[14] == null ? null : objects[14].toString();
				MasterDivision masterDivision = divisionDAO.findById(Long.valueOf(divisionId)).get();
				employeeResponse.setDivision(masterDivision.getDivisionName());

				String department = objects[15] == null ? null : objects[15].toString();
				MasterDepartment masterDepartment = deptDAO.findById(Long.valueOf(department)).get();
				String departmentName = masterDepartment.getDepartmentName() == null ? null
						: masterDepartment.getDepartmentName();
				employeeResponse.setDepartment(departmentName);

				String maritalStatus = objects[16] == null ? null : objects[16].toString();
				employeeResponse.setMarital_status(maritalStatus);
				logger.info("Employee maritalStatus :" + maritalStatus);

				String reportingMangerId = objects[17] == null ? null : objects[17].toString();

				Employee employee = null;
				Candidate candidate = null;
				if (reportingMangerId == null) {
					employeeResponse.setROmailid(null);
					employeeResponse.setReportingManagername(null);
				} else {
					employee = employeeDAO.findById(Long.valueOf(reportingMangerId)).get();

					String reportingOfficalMailId = employee.getOfficialEmailId() == null ? null
							: employee.getOfficialEmailId();
					employeeResponse.setROmailid(reportingOfficalMailId);

					candidate = candidateDAO.findById(employee.getCandidate().getId()).get();
					String reportingManagerFirstName = candidate.getFirstName() == null ? null
							: candidate.getFirstName();

					String reportingManagerLastName = candidate.getLastName() == null ? null : candidate.getLastName();

					employeeResponse
							.setReportingManagername(reportingManagerFirstName + " " + reportingManagerLastName);
				}

				Object[][] empSeparation = employeeSeparationDAO.findByEmployeeDetails(Long.valueOf(employeeId));
				if (empSeparation.length == 0) {
					employeeResponse.setDateofResignation(null);
					employeeResponse.setLastWorkingdate(null);
				} else {
					for (Object[] empSep : empSeparation) {
						String dateOfResignation = empSep[1] == null ? null : empSep[1].toString();
						employeeResponse.setDateofResignation(dateOfResignation);
						logger.info("Employee of date of Resignation :" + dateOfResignation);

						String lastWorkingDate = empSep[2] == null ? null : empSep[2].toString();
						employeeResponse.setLastWorkingdate(lastWorkingDate);
						logger.info("Employee lastWorkingDate :" + lastWorkingDate);
					}
				}

				Employee employeeAddress = employeeDAO.findById(Long.valueOf(employeeId)).get();
				Object[][] canAddress = candidateAddressDAO
						.findByAddressByCandidate(employeeAddress.getCandidate().getId());
				if (canAddress.length == 0) {
					employeeResponse.setPresentAddress(null);
					employeeResponse.setPermanantAddress(null);
				} else {

					for (Object[] canAdd : canAddress) {
						String persentAddress = canAdd[1] == null ? null : canAdd[1].toString();
						employeeResponse.setPresentAddress(persentAddress);
						logger.info("Employee present Address :" + persentAddress);

						String permantAddress = canAdd[2] == null ? null : canAdd[2].toString();
						employeeResponse.setPermanantAddress(permantAddress);
						logger.info("Employee Permant Address :" + permantAddress);
					}
				}

				CandidateProfessionalDetail candidateProfessional = candidateProfessionalDAO
						.findByCandidateId(employeeAddress.getCandidate().getId());
				Object[][] canQualification = candidateQualification
						.findByQualificationByProfessionalId(candidateProfessional.getId());

				if (canQualification.length == 0) {
					employeeResponse.setQualification(null);
				} else {
					for (Object[] canQuali : canQualification) {
						String qualification = canQuali[1] == null ? null : canQuali[1].toString();
						employeeResponse.setQualification(qualification);
						logger.info("Employee qualification :" + qualification);
					}
				}

				employeeList.add(employeeResponse);
			}
			res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			res.setHeader("Content-Disposition", "attachment; filename=FinalAttendanceReport.xlsx");
			String sheetName = "FinalAttendanceReport";// name of sheet

			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet(sheetName);

			Font detailsFont = wb.createFont();
			detailsFont.setBold(true);
			detailsFont.setFontHeightInPoints((short) 11);

			CellStyle detailsStyle = wb.createCellStyle();
			detailsStyle.setAlignment(HorizontalAlignment.LEFT);
			detailsStyle.setFont(detailsFont);

			Font font = wb.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			font.setFontHeightInPoints((short) 11);
			CellStyle headerStyle = wb.createCellStyle();
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setFillForegroundColor(IndexedColors.MAROON.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setFont(font);

			Font cellFont = wb.createFont();
			cellFont.setFontHeightInPoints((short) 10);
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(HorizontalAlignment.LEFT);
			cellStyle.setFont(cellFont);
			cellStyle.setBorderBottom(BorderStyle.THIN);
			cellStyle.setBorderTop(BorderStyle.THIN);
			cellStyle.setBorderLeft(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);

			CellStyle cellRedStyle = wb.createCellStyle();
			cellRedStyle.setAlignment(HorizontalAlignment.LEFT);
			cellRedStyle.setFont(cellFont);
			cellRedStyle.setBorderBottom(BorderStyle.THIN);
			cellRedStyle.setBorderTop(BorderStyle.THIN);
			cellRedStyle.setBorderLeft(BorderStyle.THIN);
			cellRedStyle.setBorderRight(BorderStyle.THIN);
			cellRedStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
			cellRedStyle.setFillPattern(FillPatternType.FINE_DOTS);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));

			XSSFRow row = sheet.createRow(0);
			Cell cell1 = row.createCell(0);
			cell1.setCellValue("Vinsys IT Services India Limited.");
			cell1.setCellStyle(detailsStyle);

			row = sheet.createRow(1);
			cell1 = row.createCell(0);
			cell1.setCellValue("Employee Details");
			cell1.setCellStyle(detailsStyle);

			row = sheet.createRow(2);
			cell1 = row.createCell(0);
			cell1.setCellValue("Report generated date : "
					+ HRMSDateUtil.format(HRMSDateUtil.getToday(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			cell1.setCellStyle(detailsStyle);

			row = sheet.createRow(4);

			Cell cell21 = row.createCell(1);
			cell21.setCellValue("Sr.No.");
			cell21.setCellStyle(headerStyle);

			Cell cell22 = row.createCell(2);
			cell22.setCellValue("EmpCode");
			cell22.setCellStyle(headerStyle);

			Cell cell23 = row.createCell(3);
			cell23.setCellValue("EmployeeName");
			cell23.setCellStyle(headerStyle);

			Cell cell24 = row.createCell(4);
			cell24.setCellValue("EmployeeOfficialMaild");
			cell24.setCellStyle(headerStyle);

			Cell cell25 = row.createCell(5);
			cell25.setCellValue("EmployeeStatus");
			cell25.setCellStyle(headerStyle);

			Cell cell26 = row.createCell(6);
			cell26.setCellValue("DateOfBirth");
			cell26.setCellStyle(headerStyle);

			Cell cell27 = row.createCell(7);
			cell27.setCellValue("EmployeePersonalMailID");
			cell27.setCellStyle(headerStyle);

			Cell cell28 = row.createCell(8);
			cell28.setCellValue("ContactNumber");
			cell28.setCellStyle(headerStyle);

			Cell cell29 = row.createCell(9);
			cell29.setCellValue("Gender");
			cell29.setCellStyle(headerStyle);

			Cell cell30 = row.createCell(10);
			cell30.setCellValue("AddharCardNo");
			cell30.setCellStyle(headerStyle);

			Cell cell31 = row.createCell(11);
			cell31.setCellValue("DateOfJoining");
			cell31.setCellStyle(headerStyle);

			Cell cell32 = row.createCell(12);
			cell32.setCellValue("PanCard");
			cell32.setCellStyle(headerStyle);

			Cell cell38 = row.createCell(13);
			cell38.setCellValue("MaritalStatus");
			cell38.setCellStyle(headerStyle);

			Cell cell39 = row.createCell(14);
			cell39.setCellValue("DivisionName");
			cell39.setCellStyle(headerStyle);

			Cell cell40 = row.createCell(15);
			cell40.setCellValue("BranchName");
			cell40.setCellStyle(headerStyle);

			Cell cell41 = row.createCell(16);
			cell41.setCellValue("ReportingManagerMailId");
			cell41.setCellStyle(headerStyle);

			Cell cell42 = row.createCell(17);
			cell42.setCellValue("ReportingManangerName");
			cell42.setCellStyle(headerStyle);

			Cell cell43 = row.createCell(18);
			cell43.setCellValue("DepartmentName");
			cell43.setCellStyle(headerStyle);

			Cell cell33 = row.createCell(19);
			cell33.setCellValue("PresentAddress");
			cell33.setCellStyle(headerStyle);

			Cell cell34 = row.createCell(20);
			cell34.setCellValue("PermantAddres");
			cell34.setCellStyle(headerStyle);

			Cell cell36 = row.createCell(21);
			cell36.setCellValue("DateOfResignation");
			cell36.setCellStyle(headerStyle);

			Cell cell37 = row.createCell(22);
			cell37.setCellValue("LastWorkingDate");
			cell37.setCellStyle(headerStyle);

			Cell cell35 = row.createCell(23);
			cell35.setCellValue("Qualification");
			cell35.setCellStyle(headerStyle);

			int rowNo = 4;
			for (int r = 0; r < employeeList.size(); r++) {
				++rowNo;
				row = sheet.createRow(rowNo);
				int cellNo = 1;

				Cell cell = row.createCell(cellNo++);
				cell.setCellValue(r + 1);
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getEmployee_id());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getEmployee_name());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getEmployeeofficialnMailId());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getIsActive());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getDateOfBirth());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getEmployeePersonalMailId());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getContactNumber());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getGender());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getAddharCardNo());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getDateOfjoining());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getPancardNo());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getMarital_status());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getDivision());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getBranch());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getROmailid());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getReportingManagername());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getDepartment());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getPresentAddress());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getPermanantAddress());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getDateofResignation());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getLastWorkingdate());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(cellNo++);
				cell.setCellValue(employeeList.get(r).getQualification());
				cell.setCellStyle(cellStyle);
			}

			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);
			sheet.autoSizeColumn(11);
			sheet.autoSizeColumn(12);
			sheet.autoSizeColumn(13);
			sheet.autoSizeColumn(14);
			sheet.autoSizeColumn(15);
			sheet.autoSizeColumn(16);
			sheet.autoSizeColumn(17);
			sheet.autoSizeColumn(18);
			sheet.autoSizeColumn(19);
			sheet.autoSizeColumn(20);
			sheet.autoSizeColumn(21);
			sheet.autoSizeColumn(22);
			sheet.autoSizeColumn(23);
			wb.write(res.getOutputStream());
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
