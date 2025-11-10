package com.vinsys.hrms.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCurrentOrganizationDetailDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationDAO;
import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.IHRMSWorkshiftDAO;
import com.vinsys.hrms.datamodel.ForgetPasswordRequest;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCreateEmployeeRequest;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOLoginEntityType;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeCurrentDetail;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.EmployeeSeparationDetails;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.MasterOrganizationEmailConfig;
import com.vinsys.hrms.entity.MasterWorkshift;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.EventsEmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/employee")

//@PropertySource(value="${HRMSCONFIG}")
public class EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	@Value("${base.url}")
	private String baseURL;
	@Value("${rootDirectory}")
	private String rootDirectory;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSMasterEmployementTypeDAO employmentTypeDAO;
	@Autowired
	IHRMSLoginEntityTypeDAO loginEntityTypeDAO;
	@Autowired
	IHRMSEmployeeReportingManager reportingManagerDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;
	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO configDAO;
	@Autowired
	EmailSender emailSender;
	@Autowired
	EventsEmailSender eventEmailSender;
	@Autowired
	IHRMSEmployeeCurrentOrganizationDetailDAO employeeCurrentOrganizationDetailDAO;

	@Autowired
	IHRMSEmployeeSeparationDAO employeeSeparationDAO;

	/*
	 * @Autowired IHRMSServiceCompletionDAO serviceCompletionDAO;
	 */
	@Autowired
	IHRMSWorkshiftDAO workshiftDAO;

	@Autowired
	IHRMSEmployeeAcnDAO employeeACNDAO;

	@Autowired
	IHRMSMasterLeaveTypeDAO masterLeaveTypeDAO;

	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailDAO;

	@Autowired
	IHRMSEmployeeCreditLeaveDAO employeeCreditLeaveDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addEmployee(@RequestBody VOCreateEmployeeRequest employeeRequest) {

		try {
			if (!HRMSHelper.isNullOrEmpty(employeeRequest) && !HRMSHelper.isNullOrEmpty(employeeRequest.getEmployee())
					&& !HRMSHelper.isNullOrEmpty(employeeRequest.getLoginEntityTypes())
					&& !HRMSHelper.isNullOrEmpty(employeeRequest.getEmployee().getCandidate())
					&& !HRMSHelper.isNullOrEmpty(employeeRequest.getEmployee().getEmployeeEmploymentType())
					&& !HRMSHelper.isNullOrEmpty(employeeRequest.getEmployee().getEmployeeReportingManager())) {

				Employee employeeIdCheck = employeeDAO.findById(employeeRequest.getEmployee().getId()).get();

				if (HRMSHelper.isNullOrEmpty(employeeIdCheck)) {

					Candidate candidate = candidateDAO.findById(employeeRequest.getEmployee().getCandidate().getId())
							.get();

					if (candidate != null) {

						if (candidate.getEmployee() != null) {
							throw new HRMSException(IHRMSConstants.CandidateAlreadyProcessedCode,
									IHRMSConstants.CandidateAlreadyProcessedMessage);
						}

						Employee employeeOfficialEmailIdCheck = employeeDAO
								.findByofficialEmailId(employeeRequest.getEmployee().getOfficialEmailId());

						if (employeeOfficialEmailIdCheck != null) {

							throw new HRMSException(IHRMSConstants.DataAlreadyExist,
									IHRMSConstants.EMPLOYEE_EMAIL_ALREADY_EXIST);
						}

						/*
						 * changed by ssw on 02jan2018
						 */
						Set<LoginEntityType> loginEntityTypes = new HashSet<LoginEntityType>();
						for (VOLoginEntityType voLoginEntityType : employeeRequest.getLoginEntityTypes()) {
							if (!HRMSHelper.isLongZero(voLoginEntityType.getId())) {
								LoginEntityType loginEntityType = loginEntityTypeDAO.findById(voLoginEntityType.getId())
										.get();
								loginEntityTypes.add(loginEntityType);
							}
						}

						LoginEntity loginEntity = candidate.getLoginEntity();

						/*
						 * Setting up new credential for employee
						 */
						loginEntity.setUsername(employeeRequest.getEmployee().getOfficialEmailId());
						String randomPwdStr = HRMSHelper.randomString();
						String encryptedPwdStr = HRMSHelper.encryptToSHA256(randomPwdStr);
						loginEntity.setPassword(encryptedPwdStr);
						/*
						 * change by ssw on 01jan2018
						 */
						// loginEntity.setLoginEntityType(loginEntityType);

						loginEntity.setLoginEntityTypes(loginEntityTypes);
						/*
						 * upto this added by ssw
						 */
						loginEntity.setIsFirstLogin(IHRMSConstants.IsFirstLogin);
						candidate.setLoginEntity(loginEntity);
						candidate.setCandidateStatus(IHRMSConstants.CandidateStatus_ISEMPLOYEE);

						MasterEmploymentType masterEmploymentType = employmentTypeDAO.findById(
								employeeRequest.getEmployee().getEmployeeEmploymentType().getEmploymentType().getId())
								.get();

						candidate.setEmploymentType(masterEmploymentType);
						candidateDAO.save(candidate);
						// EmployeeEmploymentType employementTypeEntity = new EmployeeEmploymentType();
						// employementTypeEntity.setEmploymentType(masterEmploymentType);

						/*
						 * Setting Employee
						 * 
						 */
						Employee employeeEntity = new Employee();
						employeeEntity = HRMSRequestTranslator.createEmployeeEntity(employeeEntity, employeeRequest);
						employeeEntity.setCandidate(candidate);
						// employeeEntity.setEmployeeEmploymentType(employementTypeEntity);

						employeeEntity = employeeDAO.save(employeeEntity);

						EmployeeACN empACN = new EmployeeACN();
						if (!HRMSHelper.isNullOrEmpty(employeeRequest.getEmployee().getEmpACN()))
							empACN.setEmpACN(Long.parseLong(employeeRequest.getEmployee().getEmpACN()));
						else
							empACN.setEmpACN(0);
						empACN.setEmployee(employeeEntity);
						empACN.setIsActive(IHRMSConstants.isActive);
						empACN.setIsFlexible(false);
						empACN.setIsManagement(false);
						employeeACNDAO.save(empACN);

						/*
						 * Setting Employee Reporting manager
						 */
						EmployeeReportingManager employeeReportingMgr = new EmployeeReportingManager();
						Employee reportingManager = employeeDAO.findById(employeeRequest.getEmployee()
								.getEmployeeReportingManager().getReportingManager().getId()).get();
						employeeReportingMgr.setReporingManager(reportingManager);
						employeeReportingMgr.setEmployee(employeeEntity);

						employeeReportingMgr.setCreatedBy(employeeRequest.getCreatedBy());
						employeeReportingMgr.setCreatedDate(new Date());
						employeeReportingMgr.setIsActive(IHRMSConstants.isActive);
						reportingManagerDAO.save(employeeReportingMgr);

						// setting general workshift by organization
						// added by ssw on 15Jun2018
						MasterWorkshift masterWorkshift = workshiftDAO.findGeneralWorkshiftByOrganization(
								IHRMSConstants.WORKSHIFT_DEFAULT_GENERAL_NAME,
								candidate.getLoginEntity().getOrganization().getId());
						// upto this added by ssw

						// add in employee current details
						Calendar calCandRet = Calendar.getInstance();
						calCandRet.setTime(candidate.getDateOfBirth());
						calCandRet.add(Calendar.YEAR, 58);
						calCandRet.add(Calendar.DATE, -1);
						EmployeeCurrentDetail employeeCurrentDetail = new EmployeeCurrentDetail();
						employeeCurrentDetail.setCreatedDate(new Date());
						employeeCurrentDetail.setEmployee(employeeEntity);
						employeeCurrentDetail.setIsActive(IHRMSConstants.isActive);
						employeeCurrentDetail.setRetirementDate(calCandRet.getTime());
						employeeCurrentDetail.setWorkshift(masterWorkshift);
						employeeCurrentDetail.setNoticePeriod(candidate.getNoticePeriod());
						employeeCurrentOrganizationDetailDAO.save(employeeCurrentDetail);

						/*
						 * To Send Email
						 */
						Map<String, String> mailContentMap = new HashMap<String, String>();
						mailContentMap.put("{employeeFirstName}", employeeEntity.getCandidate().getFirstName());
						mailContentMap.put("{employeeMiddleName}", employeeEntity.getCandidate().getMiddleName());
						mailContentMap.put("{employeeLastName}", employeeEntity.getCandidate().getLastName());
						mailContentMap.put("{employeeOfficialEmail}", employeeEntity.getOfficialEmailId());
						mailContentMap.put("{employeePassword}",
								employeeEntity.getCandidate().getLoginEntity().getPassword());
						mailContentMap.put("{employeeId}", String.valueOf(employeeEntity.getId()));
						mailContentMap.put("{employeeDesignation}", employeeEntity.getCandidate()
								.getCandidateProfessionalDetail().getDesignation().getDesignationName());
						mailContentMap.put("{employeeDepartment}", employeeEntity.getCandidate()
								.getCandidateProfessionalDetail().getDepartment().getDepartmentName());
						mailContentMap.put("{employeePassword}", randomPwdStr);

						String mailContent = HRMSHelper.replaceString(mailContentMap,
								IHRMSEmailTemplateConstants.Template_Employee_Creation);
						/*
						 * boolean emailResponse =
						 * EmailSender.toSendEmail(employeeEntity.getCandidate().getEmailId(), null,
						 * mailContent, IHRMSConstants.MailSubject_EmployeeCreation);
						 */

						emailsender.toPersistEmail(employeeEntity.getCandidate().getEmailId(), null, mailContent,
								IHRMSConstants.MailSubject_EmployeeCreation,
								employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
								employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());

						/*
						 * if (!emailResponse) { logger.info("EMail Sending Failed"); throw new
						 * HRMSException(IHRMSConstants.EmailSendingFailedCode,
						 * IHRMSConstants.EmailSendingFailedMEssage); }
						 */

						// creditLeavesForEmployee method call here
						creditLeavesForNewEmployee(employeeEntity);

						return HRMSHelper.sendSuccessResponse(IHRMSConstants.EMPLOYEE_CREATED_SUCCESFULLY,
								IHRMSConstants.successCode);
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode,
								IHRMSConstants.CandidateDoesnotExistMessage);
					}

				} else {
					throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.EmployeeIdAlreadyExist);
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

	@RequestMapping(value = "/findAll/{orgId}/{page}/{size}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getAllEmployeeByOrgId(@PathVariable("orgId") long orgId, @PathVariable("page") int page,
			@PathVariable("size") int size) {

		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				if (size <= 0) {
					size = IHRMSConstants.DefaultPageSize;
				}

				List<Employee> employees = employeeDAO.getAllEmployeeByOrgId(orgId, IHRMSConstants.isActive,
						PageRequest.of(page, size));
				List<Object> voEmployees = new ArrayList<>();

				if (!HRMSHelper.isNullOrEmpty(employees)) {
					voEmployees = HRMSResponseTranslator.translateToEmployeeVoForSearch(employees);
					HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
					hrmsListResponseObject.setTotalCount(employeeDAO.count());
					hrmsListResponseObject.setPageNo(page);
					hrmsListResponseObject.setPageSize(size);
					hrmsListResponseObject.setListResponse(voEmployees);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);

					return HRMSHelper.createJsonString(hrmsListResponseObject);

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
		} catch (IOException e) {
			e.printStackTrace();
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

	@RequestMapping(value = "/{empId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getEmployeeDetailsById(@PathVariable("empId") long empId) {
		try {
			Employee employee = employeeDAO.findById(empId).get();
			if (!HRMSHelper.isLongZero(empId) && !HRMSHelper.isNullOrEmpty(employee)) {

				VOEmployee employeeModel = HRMSEntityToModelMapper.convertToEmployeeModel(employee);
				return HRMSHelper.createJsonString(employeeModel);
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

	@RequestMapping(path = "/forgetPassword", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String forgetPassword(@RequestBody ForgetPasswordRequest changePasswordRequest) {

		logger.info(" == >> Inside Forget Password << == ");
		try {
			if (changePasswordRequest != null) {
				Employee employee = employeeDAO.findById(changePasswordRequest.getEmployeeId()).get();
				if (employee != null) {

					HRMSDateUtil.format(employee.getCandidate().getDateOfBirth(), IHRMSConstants.POSTGRE_DATE_FORMAT);
					HRMSDateUtil.format(changePasswordRequest.getDob(), IHRMSConstants.POSTGRE_DATE_FORMAT);

					SimpleDateFormat simpleFormat = new SimpleDateFormat(IHRMSConstants.POSTGRE_DATE_FORMAT);

					String empDob = HRMSDateUtil.format(employee.getCandidate().getDateOfBirth(),
							IHRMSConstants.POSTGRE_DATE_FORMAT);// simpleFormat.format(employee.getCandidate().getDateOfBirth());
					String reqDob = HRMSDateUtil.format(changePasswordRequest.getDob(),
							IHRMSConstants.POSTGRE_DATE_FORMAT);// simpleFormat.format(changePasswordRequest.getDob());

					if (empDob.equalsIgnoreCase(reqDob) && employee.getOfficialEmailId()
							.equalsIgnoreCase(changePasswordRequest.getOfficialEmailId())) {

						String randomString = HRMSHelper.randomString();

						// Start
						String encryptedString = HRMSHelper.encryptToSHA256(randomString);
						// End

						employee.getCandidate().getLoginEntity().setPassword(encryptedString);
						employee.getCandidate().getLoginEntity().setIsFirstLogin("Y");
						employeeDAO.save(employee);

						Map<String, String> mailContentMap = new HashMap<String, String>();
						mailContentMap.put("{employeeFirstName}", employee.getCandidate().getFirstName());
						mailContentMap.put("{employeeMiddleName}", employee.getCandidate().getMiddleName());
						mailContentMap.put("{employeeLastName}", employee.getCandidate().getLastName());
						mailContentMap.put("{employeeOfficialEmail}", employee.getOfficialEmailId());
						// mailContentMap.put("{employeePassword}",employee.getCandidate().getLoginEntity().getPassword());
						mailContentMap.put("{employeePassword}", randomString);
						String mailContent = HRMSHelper.replaceString(mailContentMap,
								IHRMSEmailTemplateConstants.Template_Employee_Change_Password);
						/*
						 * boolean emailResponse =
						 * EmailSender.toSendEmail(employee.getOfficialEmailId(), null, mailContent,
						 * IHRMSConstants.ForgetPasswordSubject);
						 */
						/*
						 * To Persist EMail in the table,Changed on 03 January 2018
						 */
						emailsender.toPersistEmail(employee.getOfficialEmailId(), null, mailContent,
								IHRMSConstants.ForgetPasswordSubject,
								employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
								employee.getCandidate().getLoginEntity().getOrganization().getId());

						/*
						 * if (!emailResponse) { logger.info("Email Not Send Succesfully "); throw new
						 * HRMSException(IHRMSConstants.EmailSendingFailedCode,
						 * IHRMSConstants.EmailSendingFailedMEssage); }
						 */

						return HRMSHelper.sendSuccessResponse(IHRMSConstants.successMessage,
								IHRMSConstants.successCode);
					} else {
						throw new HRMSException(IHRMSConstants.InValidDetailsCode,
								IHRMSConstants.InvalidDetailsMessage);
					}

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode,
							IHRMSConstants.EmployeeDoesnotExistMessage);
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
	 * 
	 * @param orgId
	 * @param page
	 * @param size
	 * @return string json
	 * 
	 *         This method is used to get list of employees with name and id
	 */
	@RequestMapping(value = "/findEmpNameId/{orgId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getAllEmployeeNameIdByOrgId(@PathVariable("orgId") long orgId) {
		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				// Instant start = Instant.now();

				List<Employee> employees = employeeDAO.getAllEmpNameIdByOrg(orgId);
				List<Object> voEmployees = new ArrayList<>();
				if (!HRMSHelper.isNullOrEmpty(employees)) {
					voEmployees = HRMSResponseTranslator.translateEmployeeNameIdToVO(employees, voEmployees);
					HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
					hrmsListResponseObject.setListResponse(voEmployees);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);

					// CODE HERE
					// Instant finish = Instant.now();
					// long timeElapsed = Duration.between(start, finish).toMillis();
					// logger.info("** /findEmpNameId/{orgId} :: getAllEmployeeNameIdByOrgId() ::
					// duration :: " + timeElapsed);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
		} catch (IOException e) {
			e.printStackTrace();
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

	@RequestMapping(value = "/findEmpNameIdWithFilter/{orgId}/{designationId}"
			+ "/{divisionId}/{gender}/{employeeId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getAllEmployeeNameIdByOrgIdWithFilter(@PathVariable("orgId") long orgId,
			@PathVariable("designationId") long designationId, @PathVariable("divisionId") long divisionId,
			@PathVariable("gender") String gender, @PathVariable("employeeId") long employeeId) {
		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				List<Employee> orgEmployees = new ArrayList<Employee>();
				if (HRMSHelper.isLongZero(employeeId)) { // employee filter is empty
					Employee employee = new Employee();
					employee.setCandidate(new Candidate());
					employee.getCandidate().setLoginEntity(new LoginEntity());
					employee.getCandidate().getLoginEntity().setOrganization(new Organization());
					employee.getCandidate().getLoginEntity().getOrganization().setId(orgId);
					employee.getCandidate().setCandidateProfessionalDetail(new CandidateProfessionalDetail());
					if (!HRMSHelper.isLongZero(designationId)) {
						employee.getCandidate().getCandidateProfessionalDetail()
								.setDesignation(new MasterDesignation());
						employee.getCandidate().getCandidateProfessionalDetail().getDesignation().setId(designationId);
					}
					if (!HRMSHelper.isLongZero(divisionId)) {
						employee.getCandidate().getCandidateProfessionalDetail().setDivision(new MasterDivision());
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().setId(divisionId);
					}
					if (!HRMSHelper.isNullOrEmpty(gender)) {
						employee.getCandidate().setGender(gender);
					}
					List<Employee> employees = employeeDAO.findAll(Example.of(employee));

					// List<Employee> employees =
					// employeeDAO.getAllEmpNameIdByOrgWithFilter(designationId, divisionId,gender);

					for (Employee emp : employees) {
						if (emp.getCandidate().getLoginEntity().getOrganization().getId().equals(orgId)) {
							orgEmployees.add(emp);
						}
					}
				} else { // employee search filter is not empty
					Employee employeeFromSearchEntity = employeeDAO.findById(employeeId).get();
					orgEmployees.add(employeeFromSearchEntity);
				}

				List<Object> voEmployees = new ArrayList<>();
				if (!HRMSHelper.isNullOrEmpty(orgEmployees)) {
					voEmployees = HRMSResponseTranslator.translateEmployeeNameIdToVO(orgEmployees, voEmployees);
					HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
					hrmsListResponseObject.setListResponse(voEmployees);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else { // if org is zero
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
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

	@RequestMapping(method = RequestMethod.GET, value = "/getYears/{id}", produces = "application/json")
	public String getListOfYearsFromDateOfJoining(@PathVariable("id") long empId) {

		try {
			// Employee employee = employeeDAO.findById(empId);
			/*
			 * if (employee != null) { Calendar present = Calendar.getInstance();
			 * present.setTime(employee.getCandidate().getCandidateProfessionalDetail().
			 * getDateOfJoining()); logger.info("Employee Date Of Joining  : " +
			 * employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining());
			 * int totalNoOfYears = (int) ChronoUnit.YEARS.between(
			 * LocalDate.of(present.getWeekYear(), present.MONTH, present.DAY_OF_MONTH),
			 * LocalDate.now());
			 * 
			 */
			List<Object> yearList = new ArrayList<Object>();
			yearList.add(Calendar.getInstance().getWeekYear());
			yearList.add(Calendar.getInstance().getWeekYear() - 1);

			return HRMSHelper.createJsonString(yearList);
			/*
			 * yearList.add(present.getWeekYear()); for (int i = 1; i <= totalNoOfYears;
			 * i++) { present.add(present.YEAR, +1); yearList.add(present.getWeekYear()); }
			 * Collections.reverse(yearList); return HRMSHelper.createJsonString(yearList);
			 * 
			 * 
			 * } else { throw new HRMSException(IHRMSConstants.DataNotFoundCode,
			 * IHRMSConstants.EmployeeDoesnotExistMessage); }
			 */
		} catch (IOException e) {
			e.printStackTrace();
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
	 * This service will provide the manager subordinate brief details under the
	 * provided manager id provided,if manager id is 0 then it will search all the
	 * employee under the provided organization
	 * 
	 * This service will by utilized for HR and Manager
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/findSubordinate/{orgId}/{managerId}", produces = "application/json")
	public String findEmployeeIdAndNameForManager(@PathVariable("orgId") long orgId,
			@PathVariable("managerId") long managerId)
			throws JsonGenerationException, JsonMappingException, IOException, HRMSException {
		try {
			HRMSListResponseObject hrmsListResponseObject = null;
			List<Object> voEmployees = new ArrayList<Object>();
			if (!HRMSHelper.isLongZero(orgId)) {
				if (managerId > 0) {
					logger.info("Finding Subordinate For Manager : ");
					List<EmployeeReportingManager> reportingManager = reportingManagerDAO
							.findByreporingManager(managerId);
					for (EmployeeReportingManager rptmgr : reportingManager) {
						if (rptmgr.getEmployee().getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {

							VOEmployee employeeModel = HRMSEntityToModelMapper
									.convertToEmployeeModel(rptmgr.getEmployee());
							voEmployees.add(employeeModel);
						}
					}
				} else {
					logger.info("Finding All , Based On Organization  ... ");
					List<Employee> employees = employeeDAO.getAllEmpNameIdByOrg(orgId);
					if (!HRMSHelper.isNullOrEmpty(employees)) {
						for (Employee employeeEntity : employees) {
							VOEmployee employeeModel = HRMSEntityToModelMapper.convertToEmployeeModel(employeeEntity);
							voEmployees.add(employeeModel);
						}
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				}
				if (voEmployees.isEmpty()) {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			hrmsListResponseObject = new HRMSListResponseObject();
			hrmsListResponseObject.setListResponse(voEmployees);
			hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
			hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
			return HRMSHelper.createJsonString(hrmsListResponseObject);
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	 * This method will fetch the employee having birthday today(Current Date) and
	 * will send email to them respectively ,with all other members,
	 */
	public void getEmployeeWithTodayBirthday() {
		try {
			List<Object[]> result = employeeDAO.findIfEmployeeHasBirthdayCurrentDay();
			if (result != null && !result.isEmpty()) {
				logger.info(" Employee with Birthday Found ");
				for (Object[] resultSet : result) {
					long orgId = Long.parseLong(String.valueOf(resultSet[0]));
					long divisionId = Long.parseLong(String.valueOf(resultSet[1]));
					findAndSendEmailToEmployeeForBirthDay(divisionId, orgId);
				}
			} else {
				logger.info("No Employee has Birthday Today");
			}
		} catch (Exception ee) {
			logger.info("Inside Exception ");
			ee.printStackTrace();
		}
	}

	public void toSendEmailForEmployeeServiceCompletion() {
		try {
			List<Object[]> result = employeeDAO.findIfEmployeeHasServiceCompletionCurrentDay();
			if (result != null && !result.isEmpty()) {
				for (Object[] resultSet : result) {
					long orgId = Long.parseLong(String.valueOf(resultSet[0]));
					long divisionId = Long.parseLong(String.valueOf(resultSet[1]));
					findAndSendEmailToEmployeeForServiceCompletion(divisionId, orgId);
				}
			} else {
				logger.info("No Employee has Service Completion Today");
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	private void findAndSendEmailToEmployeeForServiceCompletion(long divisionId, long orgId)
			throws FileNotFoundException, IOException, HRMSException {
		try {
			List<Employee> employeeList = employeeDAO.findServiceCompletionOfEmployee(divisionId, orgId);
			MasterOrganizationEmailConfig config = configDAO.findByorganizationAnddivision(orgId, divisionId);
			String documentVerificationHeader = IHRMSEmailTemplateConstants.TEMPLATE_SERVICE_COMPLETION;

			/**
			 * This email should be configurable
			 */
//			String imagePath = rootDirectory + orgId + "\\" + "serviceCompletionImage\\" + "serviceCompletion.jpg";

			// 21-03-2024 Linux path issue
			String imagePath = rootDirectory + orgId + File.separator + "serviceCompletionImage" + File.separator
					+ "serviceCompletion.jpg";

			// String image = HRMSHelper.base64ImageConverte(imagePath);
			Map<String, String> placeHolderMapping = new HashMap<String, String>();
			// placeHolderMapping.put("{image}", image);

			if (employeeList != null && !employeeList.isEmpty()) {
				for (Employee employee : employeeList) {

					/*
					 * boolean isEmployeeResigned = hasEmployeeResigned(employee);
					 * logger.info("Service Completed Employee Name is "+employee.getCandidate().
					 * getFirstName()+" "+employee.getCandidate().getLastName()
					 * +" Has Employee Resinged -->>  "+isEmployeeResigned);
					 */

					if (!hasEmployeeResigned(employee)) {

						placeHolderMapping.put("{candidateFirstname}", employee.getCandidate().getFirstName());
						placeHolderMapping.put("{candidateLastname}", employee.getCandidate().getLastName());
						placeHolderMapping.put("{departmentName}", employee.getCandidate()
								.getCandidateProfessionalDetail().getDepartment().getDepartmentName());
						int diffrenceInYear = HRMSHelper.calculateAge(
								employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining());
						placeHolderMapping.put("{completedYears}", String.valueOf(diffrenceInYear));

						logger.info(employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName()
								+ "Has Completed " + diffrenceInYear + " Years");

						String finalMailContent = HRMSHelper.replaceString(placeHolderMapping,
								documentVerificationHeader);
						String emailSubject = "Work Anniversary - Year " + HRMSDateUtil.getCurrentYear();
						Map<String, String> map = new HashMap<String, String>();
						map.put("image1", imagePath);
						if (diffrenceInYear > 0) {
							// No Email to be send if diffrence in year is 0 or less
							eventEmailSender.toSendEmailScheduler(employee.getOfficialEmailId(), null,
									config.getGroupEmailId(), finalMailContent, emailSubject, divisionId, orgId, map);
						}
					}
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	private void findAndSendEmailToEmployeeForBirthDay(long divisionId, long organizationId)
			throws FileNotFoundException, IOException, HRMSException {
		try {
			List<Employee> employeeList = employeeDAO.findEmployeeWithTodayBirthday(divisionId, organizationId);
			MasterOrganizationEmailConfig config = configDAO.findByorganizationAnddivision(organizationId, divisionId);
			String mailContent = IHRMSEmailTemplateConstants.BIRTHDAY_EMAIL_TEMPLATE;
			Map<String, String> placeHolderMap = new HashMap<String, String>();
//			String imagePath = rootDirectory + organizationId + "\\" + "birthdayImage\\" + "birthdayImage.jpg";

			// 21-03-2024 Linux path issue
			String imagePath = rootDirectory + organizationId + File.separator + "birthdayImage" + File.separator
					+ "birthdayImage.jpg";

			// String image = HRMSHelper.base64ImageConverte(imagePath);
			// placeHolderMap.put("image1", imagePath);
			if (employeeList != null && !employeeList.isEmpty()) {
				for (Employee employee : employeeList) {

					if (!hasEmployeeResigned(employee)) {

						logger.info("Happy Birthday to : " + employee.getCandidate().getFirstName() + " "
								+ employee.getCandidate().getLastName());

						String bccEmailId = config.getGroupEmailId();
						placeHolderMap.put("{candidateFirstname}", employee.getCandidate().getFirstName());
						placeHolderMap.put("{candidateLastname}", employee.getCandidate().getLastName());
						placeHolderMap.put("{departmentName}", employee.getCandidate().getCandidateProfessionalDetail()
								.getDepartment().getDepartmentName());
						String finalMailContent = HRMSHelper.replaceString(placeHolderMap, mailContent);
						String emailSubject = "Happy Birthday " + employee.getCandidate().getFirstName() + " "
								+ employee.getCandidate().getLastName();
						/*
						 * emailsender.toSendEmailScheduler(employee.getOfficialEmailId(), null,
						 * bccEmailId, finalMailContent, emailSubject, divisionId, organizationId);
						 */
						Map<String, String> map = new HashMap<String, String>();
						map.put("image1", imagePath);
						eventEmailSender.toSendEmailScheduler(employee.getOfficialEmailId(), null, bccEmailId,
								finalMailContent, emailSubject, divisionId, organizationId, map);
					}
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/toTriggerBirthdayOrServiceCompletion/{triggerPoint}", produces = "application/json")
	public void emailTrigger(@PathVariable("triggerPoint") String toTrigger) {

		if (toTrigger != null) {

			if (toTrigger.equalsIgnoreCase("Birthday")) {
				logger.info("Sending Birthday");
				getEmployeeWithTodayBirthday();

			} else if (toTrigger.equalsIgnoreCase("ServiceCompletion")) {
				logger.info("Sending ServiceCompletion");
				toSendEmailForEmployeeServiceCompletion();

			} else if (toTrigger.equalsIgnoreCase("BulkEmail")) {
				logger.info("Sending BulkEmail");
				emailSender.toSendBulkEmail();
			}

		} else {
			logger.info("Please choose a trigger point ");
		}
	}

	/**
	 * This method will check if the employee has resigned or not
	 * 
	 * @return boolean
	 * @return true if employee has resigned
	 * @return false if employee has not resigned
	 */
	public boolean hasEmployeeResigned(Employee employee) {
		boolean resigned = false;
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			EmployeeSeparationDetails separationDetails = employeeSeparationDAO.findEmployeeIfResigned(employee.getId(),
					IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(separationDetails)) {
				resigned = true;
			}
		}
		return resigned;
	}

	private void creditLeavesForNewEmployee(Employee employee) {
		try {
			logger.info("Inside credit Leaves For New Employee method");

			LocalDate currentDate = LocalDate.now();
			Date joiningDate = employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining();
			Date utilJoiningDate = new Date(joiningDate.getTime());
			Instant instant = utilJoiningDate.toInstant();
			LocalDate localJoiningDate = instant.atZone(ZoneId.of("UTC")).toLocalDate();
			Period period = Period.between(localJoiningDate, currentDate);
			int monthsDifference = period.getMonths();

			float earnedLeaves;

			if (monthsDifference == 0) {
				if (currentDate.getDayOfMonth() <= 15) {
					if (employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 1
							|| employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 2) {
						earnedLeaves = 12;
					} else if (employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 3
							|| employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 4) {
						earnedLeaves = 2.5f;
					} else {
						logger.error("After date 15 join : division ID: "
								+ employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
						return;
					}
				} else {
					if (employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 1
							|| employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 2) {
						earnedLeaves = 11;
					} else {
						logger.error("After date 15 join: division ID: "
								+ employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
						return;
					}
				}
			} else if (monthsDifference > 0) {
				if (currentDate.getDayOfMonth() <= 15) {
					if (employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 1
							|| employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 2) {
						earnedLeaves = 12 - monthsDifference;
					} else if (employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 3
							|| employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 4) {
						earnedLeaves = 2.5f;
					} else {
						logger.error("After date 15 join: division ID: "
								+ employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
						return;
					}
				} else {
					if (employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 1
							|| employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 2) {
						earnedLeaves = 12 - monthsDifference + 1;
					} else {
						logger.error("After date 15 join: division ID: : "
								+ employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
						return;
					}
				}
			} else {
				logger.error("Invalid monthsDifference value: " + monthsDifference);
				return;
			}

			if ((employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 1
					|| employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 2
							&& employee.getCandidate().getCandidateStatus().equals("Probation"))) {
				creditLeave(employee, IHRMSConstants.LEAVE_TYPE_CODE_SICK, earnedLeaves);
				logger.info("Crediting " + earnedLeaves + " leaves for new joiner in Division 1 or 2. Employee ID: "
						+ employee.getId());
			} else if ((employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 3
					|| employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 4
							&& employee.getCandidate().getCandidateStatus().equals("Probation"))) {
				creditLeave(employee, IHRMSConstants.LEAVE_TYPE_CODE_SICK, earnedLeaves);
				logger.info("Crediting 2.5 leaves for new joiner in Division 3 or 4 after the 15th date. Employee ID: "
						+ employee.getId());
			}

			logger.info("Exiting creditLeavesForNewEmployee method");

		} catch (Exception e) {
			logger.error("Error crediting leaves for new employee: " + employee.getId(), e);
		}
	}

	private void creditLeave(Employee employee, String leaveTypeCode, float earnedLeaves) {
		try {
			logger.info("Inside creditLeave method");

			// Get the leave type based on leaveTypeCode
			MasterLeaveType leaveType = masterLeaveTypeDAO.findByLeaveTypeCode(leaveTypeCode);

			// Get the current year
			int currentYear = Year.now().getValue();

			// Check if the employee already has leave details for the specified leave type
			// and year
			EmployeeLeaveDetail leaveDetail = employeeLeaveDetailDAO.findEmployeeLeaveByEIDYEAR(employee.getId(),
					leaveType.getId(), currentYear);

			if (HRMSHelper.isNullOrEmpty(leaveDetail)) {
				// Create a new leave entry for the employee
				leaveDetail = new EmployeeLeaveDetail();
				leaveDetail.setCreatedBy("By System When Add New Emp");
				leaveDetail.setCreatedDate(new Date());
				leaveDetail.setLeaveAvailable(earnedLeaves);
				leaveDetail.setLeaveEarned(earnedLeaves);
				leaveDetail.setMasterLeaveType(leaveType);
				leaveDetail.setTotalEligibility(earnedLeaves);
				leaveDetail.setYear(currentYear);
				leaveDetail.setEmployee(employee);
				employeeLeaveDetailDAO.save(leaveDetail);

				logger.info("Credited " + earnedLeaves + " leaves for new employee ID: " + employee.getId());

				// Entry in credit leave
				addEmployeeCreditLeaveDetail(employee, leaveType, earnedLeaves);
			}
		} catch (Exception e) {
			logger.error("Error crediting leave for new employee: " + employee.getId(), e);
		}
	}

	private void addEmployeeCreditLeaveDetail(Employee employee, MasterLeaveType leaveType, float earnedLeaves) {
		try {

			int year = Year.now().getValue(); // Get the current year
			Calendar calFromDate = Calendar.getInstance();
			calFromDate.set(year, 0, 1);
			Calendar calToDate = Calendar.getInstance();
			calToDate.set(year, 11, 31);

			// Entry in credit leave
			EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
			empCreditLeaveDtlEntity.setCreditedBy("By System When Add New Emp");
			empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
			empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
			empCreditLeaveDtlEntity.setNoOfDays(earnedLeaves);
			empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
			empCreditLeaveDtlEntity.setPostedDate(new Date());
			empCreditLeaveDtlEntity.setEmployee(employee);
			empCreditLeaveDtlEntity.setMasterLeaveType(leaveType);
			empCreditLeaveDtlEntity.setCreatedBy("By System When Add New Emp");
			empCreditLeaveDtlEntity.setCreatedDate(new Date());
			employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
		} catch (Exception e) {
			logger.error("Error adding EmployeeCreditLeaveDetail: " + employee.getId(), e);
		}
	}

}
