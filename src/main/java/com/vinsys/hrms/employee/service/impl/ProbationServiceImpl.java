package com.vinsys.hrms.employee.service.impl;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSEmailTemplateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSMapCatalogue;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.dao.confirmation.IHRMSMasterParameterName;
import com.vinsys.hrms.dao.confirmation.IHRMSProbationFeedback;
import com.vinsys.hrms.dao.confirmation.IHRMSProbationParameter;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.service.IProbationService;
import com.vinsys.hrms.employee.vo.EmployeeFeedbackVO;
import com.vinsys.hrms.employee.vo.ProbationFeedbackDetailsVO;
import com.vinsys.hrms.employee.vo.ProbationFeedbackVO;
import com.vinsys.hrms.employee.vo.ProbationParameterVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.confirmation.MasterEvaluationParameter;
import com.vinsys.hrms.entity.confirmation.ProbationFeedback;
import com.vinsys.hrms.entity.confirmation.ProbationParameter;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.dao.IMasterEmployeeConfirmationCriteriaDAO;
import com.vinsys.hrms.master.entity.MasterEmployeeConfirmationCriteria;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ProbationToConfirmationAuthorityHelper;
import com.vinsys.hrms.util.ProbationTransformUtil;
import com.vinsys.hrms.util.ResponseCode;

/**
 * @author Onkar A
 *
 * 
 */
@Service
public class ProbationServiceImpl implements IProbationService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ProbationTransformUtil probationTransformUtil;

	@Autowired
	IHRMSEmployeeDAO empDao;
	@Autowired
	IHRMSProbationFeedback feedbackDao;
	@Autowired
	IHRMSProbationParameter paramValueDao;
	@Autowired
	IHRMSMasterParameterName parameterDao;
	@Autowired
	IHRMSCandidateDAO candidateDao;
	@Autowired
	IHRMSMasterEmployementTypeDAO employmenttypeDao;
	@Autowired
	IHRMSEmailTemplateDAO emailTemplateDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSMapCatalogue catalogueDAO;
	@Autowired
	IHRMSProfessionalDetailsDAO candidateProfessionalDao;
	@Autowired
	IHRMSMasterLeaveTypeDAO masterLeaveTypeDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailDAO;
	@Autowired
	IHRMSEmployeeCreditLeaveDAO employeeCreditLeaveDAO;
	@Autowired
	ProbationToConfirmationAuthorityHelper p2cAuthorityHelper;
	@Autowired
	IMasterEmployeeConfirmationCriteriaDAO employeeConfirmationCriteriaDAO;
	@Autowired
	IHRMSEmployeeReportingManager employeeReportingManager;

	@Override
	public HRMSBaseResponse<ProbationFeedbackVO> getProbationFeedback(Long employeeId, Long feedbackId)
			throws HRMSException {

		log.info("Inside getProbationFeedback method");
		// check role
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<ProbationFeedbackVO> response = new HRMSBaseResponse<ProbationFeedbackVO>();
		Long empId = null;
		if (!HRMSHelper.isNullOrEmpty(employeeId) && (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())
				|| HRMSHelper.isRolePresent(roles, ERole.HR.name()))) {
			empId = employeeId;
		} else {
			empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		}

		if (HRMSHelper.isNullOrEmpty(empId) || HRMSHelper.isLongZero(empId)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name()) && !HRMSHelper.isNullOrEmpty(employeeId)) {
			if (!HRMSHelper.isLongZero(employeeId)) {
				// get manager detail
				EmployeeReportingManager reportingManager = employeeReportingManager
						.findByEmployeeAndIsActive(employeeId, ERecordStatus.Y.name());
				// check parameter employee id manager
				if (!loggedInEmpId.equals(reportingManager.getReporingManager().getId())) {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1523));
				}
			}
		}

		if (!HRMSHelper.isLongZero(empId)) {

			Employee employee = empDao.findActiveEmployeeWithCandidateByEmpIdAndOrgId(empId, IHRMSConstants.isActive,SecurityFilter.TL_CLAIMS.get().getOrgId());

			EmployeeFeedbackVO empFeedback = new EmployeeFeedbackVO();

			empFeedback.setEmployeeId(employee.getId());
			empFeedback.setConfirmationStatus(employee.getCandidate().getCandidateEmploymentStatus());
			empFeedback.setDepartment(
					employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getDepartmentName());
			empFeedback.setDesignation(
					employee.getCandidate().getCandidateProfessionalDetail().getDesignation().getDesignationName());
			String date = HRMSDateUtil.format(
					employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining(),
					IHRMSConstants.POSTGRE_DATE_FORMAT);
			empFeedback.setDateOfJoining(date);

			empFeedback.setLocation(employee.getCandidate().getCandidateProfessionalDetail().getCity().getCityName());

			/********* Find date of confirmation */

			int probationPeriod = 0;
			if (!HRMSHelper.isNullOrEmpty(employee.getProbationPeriod())) {
				probationPeriod = Integer.parseInt((employee.getProbationPeriod()));
			}

			Date tempDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining());
			cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) + probationPeriod));
			tempDate = HRMSDateUtil.setTimeStampToZero(cal.getTime());
			String confirmationDueDate = HRMSDateUtil.format(tempDate, IHRMSConstants.POSTGRE_DATE_FORMAT);
			empFeedback.setConfirmationDueDate(confirmationDueDate);
			empFeedback.setConfirmationStatus(employee.getCandidate().getCandidateEmploymentStatus());
			empFeedback.setReportingManager(
					employee.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName() + " "
							+ employee.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());

			// List<ProbationFeedback> probationFeedbacks = feedbackDao.findByEmpId(empId);
			ProbationFeedback probationFeedbacks = new ProbationFeedback();
			if (HRMSHelper.isNullOrEmpty(feedbackId)) {
				probationFeedbacks = feedbackDao.findByIsActiveAndEmpId(IHRMSConstants.isActive, empId);
			} else {
				probationFeedbacks = feedbackDao.findByEmpIdAndId(empId, feedbackId);
			}
			if (HRMSHelper.isNullOrEmpty(probationFeedbacks)) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

			ProbationFeedbackVO probationFeedbackVO = probationTransformUtil
					.convertToProbationFeedbackVO(probationFeedbacks);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseBody(probationFeedbackVO);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		log.info("Exit from getProbationFeedback method");

		return response;
	}

	@Override
	public HRMSBaseResponse<?> submitProbationFeedback(ProbationFeedbackVO request) throws HRMSException {
		HRMSBaseResponse<?> baseResponse = new HRMSBaseResponse();
		log.info("Inside saveProbationFeedback method");
		// get employee from token
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = empDao.findActiveEmployeeById(loggedInEmpId, IHRMSConstants.isActive);

		// check role
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
//		String hasRole = SecurityFilter.TL_CLAIMS.get().getHasRole();
		Long empId = null;
		if ((HRMSHelper.isRolePresent(roles, ERole.MANAGER.name()) || HRMSHelper.isRolePresent(roles, ERole.HR.name()))
				&& !HRMSHelper.isNullOrEmpty(request.getEmployeeId())) {
			empId = request.getEmployeeId();
		} else {
			empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		}

		if (HRMSHelper.isNullOrEmpty(empId) || HRMSHelper.isLongZero(empId)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		Employee empOnProbation = empDao.findActiveEmployeeById(empId, IHRMSConstants.isActive);

		// validate request payload
		p2cAuthorityHelper.saveProbationFeedbackInputValidation(request, roles, loggedInEmployee, empOnProbation);

		if (!HRMSHelper.isNullOrEmpty(request)) {
			ProbationFeedback probationFeedback = null;
			Boolean isRoSubmitted = false;
			probationFeedback = feedbackDao.findByIsActiveAndEmpId(ERecordStatus.Y.name(), empId);
			if (!HRMSHelper.isNullOrEmpty(probationFeedback)
					&& !HRMSHelper.isNullOrEmpty(probationFeedback.getRoSubmitted())) {
				isRoSubmitted = probationFeedback.getRoSubmitted();
			}

			// check feedback form already submitted by employee or not
			if (!HRMSHelper.isNullOrEmpty(probationFeedback)
					&& empOnProbation.getId().equals(loggedInEmployee.getId())) {
				throw new HRMSException(1500, "form already submitted.");
			}
			if (!HRMSHelper.isNullOrEmpty(probationFeedback)) {
				probationFeedback.setUpdatedDate(new Date());
				probationFeedback.setUpdatedBy(String.valueOf(loggedInEmployee.getId()));
			} else {
				probationFeedback = new ProbationFeedback();
				probationFeedback.setOrgId(empOnProbation.getOrgId());
				probationFeedback.setCreatedDate(new Date());
				probationFeedback.setCreatedBy(String.valueOf(empOnProbation.getId()));
			}
			probationFeedback.setEmployee(empOnProbation);
			probationFeedback.setIsActive(IHRMSConstants.isActive);
			if (HRMSHelper.isRolePresent(roles, ERole.HR.name()) && isRoSubmitted) {
				probationFeedback.setHrComment(request.getHrComment());
				probationFeedback.setHrSubmitted(true);
				probationFeedback.setExtendedBy(request.getExtendedBy());
				probationFeedback.setStatus(getConfirmationStatus(request));

			} else if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())
					&& !empOnProbation.getId().equals(loggedInEmployee.getId())) {
				if (isRoSubmitted) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1526));
				}
				probationFeedback.setManagerComment(request.getManagerComment());
				probationFeedback.setRoSubmitted(true);
				probationFeedback.setStatus(getConfirmationStatus(request));
			} else {
				probationFeedback.setStatus(IHRMSConstants.PROBATION);
				probationFeedback.setHrSubmitted(false);
				probationFeedback.setRoSubmitted(false);
			}

			List<ProbationParameterVO> parameterList = request.getProbationParameter();
			List<ProbationParameter> probationPrameterList = new ArrayList<>();
			for (ProbationParameterVO obj : parameterList) {
				if (!HRMSHelper.isNullOrEmpty(obj)) {
					ProbationParameter temp = new ProbationParameter();
					if (!HRMSHelper.isNullOrEmpty(obj.getId()) && !HRMSHelper.isLongZero(obj.getId())) {
						ProbationParameter temp1 = paramValueDao.findById(obj.getId()).get();

						if (!HRMSHelper.isNullOrEmpty(temp1)) {
							temp.setId(temp1.getId());
						}
					} else {
						temp.setId(obj.getId());
					}
					temp.setId(obj.getId());
					temp.setEmpRating(obj.getEmpRating());
					temp.setEmployeeComment(obj.getEmployeeComment());
					temp.setManagerRating(obj.getManagerRating());
					temp.setManagerComment(obj.getManagerComment());
					MasterEvaluationParameter param = parameterDao.findByIdAndIsActive(obj.getParameterValue().getId(),
							IHRMSConstants.isActive);
					temp.setParameterValue(param);
					temp.setFeedback(probationFeedback);
					probationPrameterList.add(temp);
				}
			}
			probationFeedback.setProbationParameter(probationPrameterList);
			ProbationFeedback tempFeedback = feedbackDao.save(probationFeedback);
			// HRMSListResponseObject listResponse = new HRMSListResponseObject();
			if (!HRMSHelper.isNullOrEmpty(tempFeedback)) {
				// To update final employee status when feedback submitted by HR
				if (tempFeedback.getHrSubmitted()) {
					Employee tempEmp = this.empDao.findActiveEmployeeById(tempFeedback.getEmployee().getId(),
							IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(tempFeedback.getStatus())) {
						if (!HRMSHelper.isNullOrEmpty(tempEmp)) {
							Candidate candidate = this.candidateDao.findByIdAndIsActive(tempEmp.getCandidate().getId(),
									IHRMSConstants.isActive);
							MasterEmploymentType employmentTypeEntity = null;
							if (tempFeedback.getStatus().equals(IHRMSConstants.CONFIRMED)) {
								employmentTypeEntity = employmenttypeDao.findByIsActiveAndEmploymentTypeName(
										IHRMSConstants.isActive, IHRMSConstants.CONFIRMED);
							} else {
								employmentTypeEntity = employmenttypeDao.findByIsActiveAndEmploymentTypeName(
										IHRMSConstants.isActive, IHRMSConstants.PROBATION);
							}
							if (!HRMSHelper.isNullOrEmpty(employmentTypeEntity)) {
								candidate.setCandidateEmploymentStatus(employmentTypeEntity.getEmploymentTypeName());
								candidate.setEmploymentType(employmentTypeEntity);
								Candidate candidate1 = candidateDao.save(candidate);
							} else {
								throw new HRMSException(IHRMSConstants.DataNotFoundCode,
										IHRMSConstants.DataNotFoundMessage);
							}
							if (!HRMSHelper.isNullOrEmpty(tempFeedback.getStatus())
									&& tempFeedback.getStatus().equals(IHRMSConstants.EXTENDED)) {
								/* Added for extension of probation */
								ProbationFeedback feedback = feedbackDao.findByIdAndIsActive(tempFeedback.getId(),
										IHRMSConstants.isActive);
								feedback.setIsActive(IHRMSConstants.isNotActive);
								feedbackDao.save(feedback);

								String period = tempEmp.getProbationPeriod() == null ? "0"
										: tempEmp.getProbationPeriod();
								Long extendedBy = Long.parseLong(period) + tempFeedback.getExtendedBy();
								tempEmp.setProbationPeriod(extendedBy.toString());
								empDao.save(tempEmp);
							}
						} else {
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.DataNotFoundMessage);
						}
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				}

				// send mail logic
				Employee tempEmpEntity = empDao.findActiveEmployeeById(empId, IHRMSConstants.isActive);
				log.info(tempEmpEntity.getId() + " " + tempEmpEntity.getCandidate().getFirstName() + " "
						+ tempEmpEntity.getCandidate().getLastName());
				MasterDivision divisionEntity = tempEmpEntity.getCandidate().getCandidateProfessionalDetail()
						.getDivision();
				Organization organizationEntity = tempEmpEntity.getCandidate().getLoginEntity().getOrganization();
				Employee reportingManager = tempEmpEntity.getEmployeeReportingManager().getReporingManager();
				String reportingManagerName = reportingManager.getCandidate().getFirstName() + " "
						+ reportingManager.getCandidate().getLastName();
				log.info(reportingManager.getId() + " " + reportingManager.getCandidate().getFirstName() + " "
						+ reportingManager.getCandidate().getLastName());

				MapCatalogue mapcatalogue = catalogueDAO.findHR(organizationEntity.getId(), "HR",
						tempEmpEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
				Employee hr = mapcatalogue.getApprover();
				if (HRMSHelper.isRolePresent(roles, ERole.EMPLOYEE.name())
						&& loggedInEmployee.getId().equals(empOnProbation.getId())) {
					// send mail to Employee
					/**
					 * Code added by Ritesh Kolhe on 02-Jun-2021 to send P2C notification to HR as
					 * well.
					 */
					Employee tempEmpEntity1 = empDao.findActiveEmployeeById(empId, IHRMSConstants.isActive);
					log.info(tempEmpEntity1.getId() + " " + tempEmpEntity.getCandidate().getFirstName() + " "
							+ tempEmpEntity.getCandidate().getLastName());
					Organization organizationObj = tempEmpEntity.getCandidate().getLoginEntity().getOrganization();
					List<Object[]> empresult1 = empDao.findHREmailId(organizationObj.getId(), IHRMSConstants.isActive);
					List<Employee> emplist1 = new ArrayList<>();
					if (empresult1 != null && !empresult1.isEmpty()) {
						for (Object[] resultSet : empresult1) {
							Employee employee = new Employee();
							employee.setId(Long.parseLong(String.valueOf(resultSet[0])));
							employee.setOfficialEmailId(String.valueOf(resultSet[3]));
							emplist1.add(employee);
						}
					}
					String ccEmailIds = "";
					for (Employee hrobj : emplist1) {
						ccEmailIds = ccEmailIds + ";" + hrobj.getOfficialEmailId();
					}
					// existing code starts
					EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
							IHRMSConstants.P2C_SUBMITTED_BY_EMPlOYEE_TEMPLATE, IHRMSConstants.isActive, divisionEntity,
							organizationEntity);
					if (!HRMSHelper.isNullOrEmpty(template)) {
						Map<String, String> placeHolderMap = new HashMap<String, String>();
						placeHolderMap.put("{employeeName}", tempFeedback.getEmployee().getCandidate().getFirstName()
								+ " " + tempFeedback.getEmployee().getCandidate().getLastName());
						placeHolderMap.put("{managerName}", reportingManagerName);
						String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

						emailsender.toPersistEmail(reportingManager.getOfficialEmailId(), ccEmailIds, mailBody,
								template.getEmailSubject(), divisionEntity.getId(), organizationEntity.getId());
					} else {
						throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
								IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
					}
				} else if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())
						&& !loggedInEmployee.getId().equals(empOnProbation.getId())) {
					// send mail to Manager
					EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
							IHRMSConstants.P2C_SUBMITTED_BY_MANAGER_TEMPLATE, IHRMSConstants.isActive, divisionEntity,
							organizationEntity);
					if (!HRMSHelper.isNullOrEmpty(template)) {
						Map<String, String> placeHolderMap = new HashMap<String, String>();
						placeHolderMap.put("{employeeName}", tempFeedback.getEmployee().getCandidate().getFirstName()
								+ " " + tempFeedback.getEmployee().getCandidate().getLastName());
						placeHolderMap.put("{TAT}", "5");
						placeHolderMap.put("{hrName}",
								hr.getCandidate().getFirstName() + " " + hr.getCandidate().getLastName());

						String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());
						/**
						 * Code added by Ritesh Kolhe on 02-Jun-2021 to send P2C notification to HR as
						 * well.
						 */
						Employee tempEmpEntity2 = empDao.findActiveEmployeeById(empId, IHRMSConstants.isActive);
						log.info(tempEmpEntity2.getId() + " " + tempEmpEntity.getCandidate().getFirstName() + " "
								+ tempEmpEntity.getCandidate().getLastName());
						Organization organizationObj = tempEmpEntity.getCandidate().getLoginEntity().getOrganization();
						List<Object[]> empresult2 = empDao.findHREmailId(organizationObj.getId(),
								IHRMSConstants.isActive);
						List<Employee> emplist2 = new ArrayList<>();
						if (empresult2 != null && !empresult2.isEmpty()) {
							for (Object[] resultSet : empresult2) {
								Employee employee = new Employee();
								employee.setId(Long.parseLong(String.valueOf(resultSet[0])));
								employee.setOfficialEmailId(String.valueOf(resultSet[3]));
								emplist2.add(employee);
							}
						}
						String ccEmailIds = "";
						for (Employee hrobj : emplist2) {
							ccEmailIds = ccEmailIds + ";" + hrobj.getOfficialEmailId();
						}
						// existing code starts
						emailsender.toPersistEmail(hr.getOfficialEmailId(), ccEmailIds, mailBody,
								template.getEmailSubject(), divisionEntity.getId(), organizationEntity.getId());
					} else {
						throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
								IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
					}
				} else if (HRMSHelper.isRolePresent(roles, ERole.HR.name())
						&& !loggedInEmployee.getId().equals(empOnProbation.getId())) {
					// send mail to HR
					CandidateProfessionalDetail candProfDetail = candidateProfessionalDao
							.findBycandidate(tempFeedback.getEmployee().getCandidate());
					int probationPeriod = Integer
							.parseInt(HRMSHelper.isNullOrEmpty(tempFeedback.getEmployee().getProbationPeriod()) ? "0"
									: tempFeedback.getEmployee().getProbationPeriod());
					String confirmationDate = HRMSDateUtil.format(
							HRMSDateUtil.incByMonth(candProfDetail.getDateOfJoining(), probationPeriod),
							IHRMSConstants.FRONT_END_DATE_FORMAT);
					String evaluationDate = HRMSDateUtil.format(
							HRMSDateUtil.incByMonth(candProfDetail.getDateOfJoining(), (probationPeriod)),
							IHRMSConstants.FRONT_END_DATE_FORMAT);
					String endDate = HRMSDateUtil.format(
							HRMSDateUtil.incByMonth(candProfDetail.getDateOfJoining(), (probationPeriod)),
							IHRMSConstants.FRONT_END_DATE_FORMAT);
					EmailTemplate template = null;
					if (tempFeedback.getStatus().equals(IHRMSConstants.CONFIRMED)) {
						// Call the method to credit leaves for confirmed employees
						creditLeavesForConfirmedEmployee(tempFeedback.getEmployee());
						template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
								IHRMSConstants.P2C_SUBMITTED_BY_HR_TEMPLATE_CONFIRMED, IHRMSConstants.isActive,
								divisionEntity, organizationEntity);
						if (!HRMSHelper.isNullOrEmpty(template)) {
							Map<String, String> placeHolderMap = new HashMap<String, String>();
							placeHolderMap.put("{employeeName}",
									tempFeedback.getEmployee().getCandidate().getFirstName() + " "
											+ tempFeedback.getEmployee().getCandidate().getLastName());
							placeHolderMap.put("{confirmationDate}", confirmationDate);
							placeHolderMap.put("{divisionName}", divisionEntity.getDivisionName());
							String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());
							String ccEmailId = reportingManager.getOfficialEmailId();
							List<Object[]> empresult = empDao.findHREmailId(organizationEntity.getId(),
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
							for (Employee hrobj : emplist) {
								ccEmailId = ccEmailId + ";" + hrobj.getOfficialEmailId();
							}
							emailsender.toPersistEmail(tempEmpEntity.getOfficialEmailId(), ccEmailId, mailBody,
									template.getEmailSubject(), divisionEntity.getId(), organizationEntity.getId());
						} else {
							throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
									IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
						}
					} else if (tempFeedback.getStatus().equals(IHRMSConstants.EXTENDED)) {

						template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
								IHRMSConstants.P2C_SUBMITTED_BY_HR_TEMPLATE_EXTENDED, IHRMSConstants.isActive,
								divisionEntity, organizationEntity);
						if (!HRMSHelper.isNullOrEmpty(template)) {
							Map<String, String> placeHolderMap = new HashMap<String, String>();
							placeHolderMap.put("{employeeName}",
									tempFeedback.getEmployee().getCandidate().getFirstName() + " "
											+ tempFeedback.getEmployee().getCandidate().getLastName());
							placeHolderMap.put("{extendedBy}", String.valueOf(tempFeedback.getExtendedBy()));
							placeHolderMap.put("{evaluationDate}", evaluationDate);

							String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

							/**
							 * @author Ritesh
							 * @Date 10-01-2022 Code Reuse as per requirement by Hr
							 */

							String ccEmailId = reportingManager.getOfficialEmailId();

							List<Object[]> empresult = empDao.findHREmailId(organizationEntity.getId(),
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
							for (Employee hrobj : emplist) {
								ccEmailId = ccEmailId + ";" + hrobj.getOfficialEmailId();

							}
							/**
							 * Existing Code Starts
							 * 
							 * @date 10-01-2022
							 */
							emailsender.toPersistEmail(tempEmpEntity.getOfficialEmailId(), ccEmailId, mailBody,
									template.getEmailSubject(), divisionEntity.getId(), organizationEntity.getId());
						} else {
							throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
									IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
						}
					} else if (tempFeedback.getStatus().equals(IHRMSConstants.REJECTED)) {

						template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
								IHRMSConstants.P2C_SUBMITTED_BY_HR_TEMPLATE_REJECTED, IHRMSConstants.isActive,
								divisionEntity, organizationEntity);
						if (!HRMSHelper.isNullOrEmpty(template)) {
							Map<String, String> placeHolderMap = new HashMap<String, String>();
							placeHolderMap.put("{employeeName}",
									tempFeedback.getEmployee().getCandidate().getFirstName() + " "
											+ tempFeedback.getEmployee().getCandidate().getLastName());
							placeHolderMap.put("{endDate}", endDate);
							String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

							/**
							 * @author Ritesh
							 * @Date 10-01-2022 Code Reuse as per requirement by Hr
							 */

							String ccEmailId = reportingManager.getOfficialEmailId();

							List<Object[]> empresult = empDao.findHREmailId(organizationEntity.getId(),
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
							for (Employee hrobj : emplist) {
								ccEmailId = ccEmailId + ";" + ";" + hrobj.getOfficialEmailId();

							}
							/**
							 * Existing Code Starts
							 * 
							 * @date 10-01-2022
							 */

							emailsender.toPersistEmail(tempEmpEntity.getOfficialEmailId(), ccEmailId, mailBody,
									template.getEmailSubject(), divisionEntity.getId(), organizationEntity.getId());
						} else {
							throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
									IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
						}
					}
				}

//				VOFeedbackResponse feedbackResponse = new VOFeedbackResponse();
//				feedbackResponse.setProbationParameter(new ArrayList<>());
//
//				feedbackResponse.setId(tempFeedback.getId());
//				feedbackResponse.setCreatedBy(tempFeedback.getCreatedBy());
//				feedbackResponse.setCreatedDate(tempFeedback.getCreatedDate());
//				feedbackResponse.setIsActive(feedbackResponse.getIsActive());
//				feedbackResponse.setEmployeeId(tempFeedback.getEmployee().getId());
//				feedbackResponse.setEmployeeName(tempFeedback.getEmployee().getCandidate().getFirstName() + " "
//						+ tempFeedback.getEmployee().getCandidate().getLastName());
//				feedbackResponse.setHrComment(tempFeedback.getHrComment());
//				feedbackResponse.setManagerComment(tempFeedback.getManagerComment());
//
//				feedbackResponse.setEmployee(request.getEmployee());
//
//				List<ProbationParameter> list2 = tempFeedback.getProbationParameter();
//
//				if (!HRMSHelper.isNullOrEmpty(list2)) {
//					for (ProbationParameter probationParameter : list2) {
//						if (!HRMSHelper.isNullOrEmpty(probationParameter)) {
//							ProbationParameterVO tempParameter = new ProbationParameterVO();
//							tempParameter.setId(probationParameter.getId());
//							tempParameter.setEmpRating(probationParameter.getEmpRating());
//							tempParameter.setEmployeeComment(probationParameter.getEmployeeComment());
//							tempParameter.setManagerRating(probationParameter.getManagerRating());
//							tempParameter.setManagerComment(probationParameter.getManagerComment());
//							// tempParameter.setFeedbackId(probationParameter.getFeedback().getId());
//							ProbationFeedbackParameterVO tempMaster = new ProbationFeedbackParameterVO();
//							tempMaster.setId(probationParameter.getParameterValue().getId());
//							tempMaster.setOrganizationId(
//									probationParameter.getParameterValue().getOrganization().getId());
//							tempMaster.setParameterName(probationParameter.getParameterValue().getParameterName());
//							tempParameter.setParameterValue(tempMaster);
//
//							feedbackResponse.getProbationParameter().add(tempParameter);
//						}
//
//					}
//				}

//				List<Object> tempList = new ArrayList<>();
//				tempList.add(feedbackResponse);
//				listResponse.setResponseCode(IHRMSConstants.successCode);
//				listResponse.setResponseMessage(IHRMSConstants.successMessage);
//				listResponse.setListResponse(tempList);
//				return HRMSHelper.createJsonString(listResponse);

				baseResponse.setResponseCode(1200);
				baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1595));

			} else {
				throw new HRMSException(IHRMSConstants.UnknowErrorCode, IHRMSConstants.UnknowErrorMessage);
			}

		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		log.info("Exit from saveProbationFeedback method");

		return baseResponse;
	}

	private String getConfirmationStatus(ProbationFeedbackVO request) {
		String confirmationStatus = null;
		float managerTotalRating = 0;
		float maxTotalRating = 55;
		float calculatedPercentage = 0;
		for (ProbationParameterVO parameterVO : request.getProbationParameter()) {
			managerTotalRating = managerTotalRating + parameterVO.getManagerRating();
		}

		calculatedPercentage = (managerTotalRating / maxTotalRating) * 100;

		MasterEmployeeConfirmationCriteria confirmationCriteria = employeeConfirmationCriteriaDAO
				.getStatusByMinAndMaxPercentage(calculatedPercentage, calculatedPercentage, ERecordStatus.Y.name());

		confirmationStatus = confirmationCriteria.getStatus();

		return confirmationStatus;
	}

	private void creditLeavesForConfirmedEmployee(Employee employee) {
		try {
			log.info("Inside credit Leaves For Confirmed Employee method");
			LocalDate currentDate = LocalDate.now();

			// Check if the employee is in Division 1 or 2 and confirmed between 1 to 15 of
			// any month
			if ((employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 1
					|| employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 2)
					&& currentDate.getDayOfMonth() <= 15) {

				float earnedLeaves = 1.5f;

				creditLeave(employee, IHRMSConstants.LEAVE_TYPE_CODE_PRIVILAGE, earnedLeaves);
				log.info("Crediting " + earnedLeaves
						+ " leaves for confirmed employee in Division 1 or 2. Employee ID: " + employee.getId());
			}

			log.info("Exiting creditLeavesForConfirmedEmployee method");

		} catch (Exception e) {
			log.error("Error crediting leaves for confirmed employee: " + employee.getId(), e);
		}
	}

	private void creditLeave(Employee employee, String leaveTypeCode, float earnedLeaves) {
		try {
			log.info("Inside Confirmed Employee credit Leave method");

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
				leaveDetail.setCreatedBy("By System P2C Emp");
				leaveDetail.setCreatedDate(new Date());
				leaveDetail.setLeaveAvailable(earnedLeaves);
				leaveDetail.setLeaveEarned(earnedLeaves);
				leaveDetail.setMasterLeaveType(leaveType);
				leaveDetail.setTotalEligibility(earnedLeaves);
				leaveDetail.setYear(currentYear);
				leaveDetail.setEmployee(employee);
				employeeLeaveDetailDAO.save(leaveDetail);

				log.info("Credited " + earnedLeaves + " leaves for new employee ID: " + employee.getId());

				// Entry in credit leave
				addEmployeeCreditLeaveDetail(employee, leaveType, earnedLeaves);
			}
		} catch (Exception e) {
			log.error("Error crediting leave for new employee: " + employee.getId(), e);
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
			empCreditLeaveDtlEntity.setCreditedBy("By System P2C Emp");
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
			log.error("Error adding EmployeeCreditLeaveDetail: " + employee.getId(), e);
		}
	}

	@Override
	public HRMSBaseResponse<List<ProbationFeedbackVO>> getProbationFeedbackList(Pageable pageable)
			throws HRMSException {
		log.info("Inside getProbationFeedbackList method");
		// check role
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = empDao.findActiveEmployeeWithCandidateByEmpIdAndOrgId(loggedInEmpId,
				IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());
		long totalRecord = 0;

		HRMSBaseResponse<List<ProbationFeedbackVO>> response = new HRMSBaseResponse<>();
		List<ProbationFeedbackVO> probationFeedbackVO = null;
		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {

			List<ProbationFeedback> probationFeedbacks = feedbackDao.findByIsActiveAndRmIdAndStatus(
					IHRMSConstants.isActive, loggedInEmpId, IHRMSConstants.PROBATION, pageable);
			totalRecord = feedbackDao.countByIsActiveAndRmIdAndStatus(IHRMSConstants.isActive, loggedInEmpId,
					IHRMSConstants.PROBATION);
			if (HRMSHelper.isNullOrEmpty(probationFeedbacks)) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

			probationFeedbackVO = probationTransformUtil.convertToProbationFeedbackVO(probationFeedbacks);
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(probationFeedbackVO);
		response.setTotalRecord(totalRecord);

		log.info("Exit from getProbationFeedbackList method");

		return response;

	}

	@Override
	public HRMSBaseResponse<ProbationFeedbackDetailsVO> getProbationFeedbackListForEmployee(Long empid)
			throws HRMSException {
		HRMSBaseResponse<ProbationFeedbackDetailsVO> response = new HRMSBaseResponse<>();
		log.info("Inside getProbationFeedback method");
		// check role
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long empId = null;
		if (!HRMSHelper.isNullOrEmpty(empid) && (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())
				|| HRMSHelper.isRolePresent(roles, ERole.HR.name()))) {
			empId = empid;
		} else {
			empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		}

		if (HRMSHelper.isNullOrEmpty(empId) || HRMSHelper.isLongZero(empId)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name()) && !HRMSHelper.isNullOrEmpty(empid)) {
			if (!HRMSHelper.isLongZero(empid)) {
				// get manager detail
				EmployeeReportingManager reportingManager = employeeReportingManager.findByEmployeeAndIsActive(empid,
						ERecordStatus.Y.name());
				// check parameter employee id manager
				if (!loggedInEmpId.equals(reportingManager.getReporingManager().getId())) {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1523));
				}
			}
		}

		if (!HRMSHelper.isLongZero(empId)) {

			Employee employee = empDao.findActiveEmployeeWithCandidateByEmpIdAndOrgId(empId, IHRMSConstants.isActive,SecurityFilter.TL_CLAIMS.get().getOrgId());

			EmployeeFeedbackVO empFeedback = new EmployeeFeedbackVO();

			empFeedback.setEmployeeId(employee.getId());
			empFeedback.setConfirmationStatus(employee.getCandidate().getCandidateEmploymentStatus());
			empFeedback.setDepartment(
					employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getDepartmentName());
			empFeedback.setDesignation(
					employee.getCandidate().getCandidateProfessionalDetail().getDesignation().getDesignationName());
			String date = HRMSDateUtil.format(
					employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining(),
					IHRMSConstants.POSTGRE_DATE_FORMAT);
			empFeedback.setDateOfJoining(date);

			empFeedback.setLocation(employee.getCandidate().getCandidateProfessionalDetail().getCity().getCityName());

			/********* Find date of confirmation */

			int probationPeriod = 0;
			if (!HRMSHelper.isNullOrEmpty(employee.getProbationPeriod())) {
				probationPeriod = Integer.parseInt((employee.getProbationPeriod()));
			}

			Date tempDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining());
			cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) + probationPeriod));
			tempDate = HRMSDateUtil.setTimeStampToZero(cal.getTime());
			String confirmationDueDate = HRMSDateUtil.format(tempDate, IHRMSConstants.POSTGRE_DATE_FORMAT);

			empFeedback.setConfirmationDueDate(confirmationDueDate);
			empFeedback.setConfirmationStatus(employee.getCandidate().getCandidateEmploymentStatus());
			empFeedback.setReportingManager(
					employee.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName() + " "
							+ employee.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());

			List<ProbationFeedback> probationFeedbacks = feedbackDao.findByEmpId(empId);

			// ProbationFeedback probationFeedbacks =
			// feedbackDao.findByIsActiveAndEmpId(IHRMSConstants.isActive, empId);

			List<ProbationFeedbackVO> probationFeedbackVO = probationTransformUtil
					.convertToProbationFeedbackVO(probationFeedbacks);

			ProbationFeedbackDetailsVO feedbackDetailsVO = new ProbationFeedbackDetailsVO();

			LocalDate confirmationDueDate1 = LocalDate.parse(confirmationDueDate, DateTimeFormatter.ISO_LOCAL_DATE);
			LocalDate currentDate = LocalDate.now();

			if (confirmationDueDate1.isBefore(currentDate)) {

				feedbackDetailsVO.setProbationPeriodCompleted(true);
			} else {
				feedbackDetailsVO.setProbationPeriodCompleted(false);
			}

			feedbackDetailsVO.setEmploymentStatus(employee.getCandidate().getCandidateEmploymentStatus());
			feedbackDetailsVO.setFeedbackVOList(probationFeedbackVO);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseBody(feedbackDetailsVO);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		log.info("Exit from getProbationFeedback method");

		return response;
	}

}
