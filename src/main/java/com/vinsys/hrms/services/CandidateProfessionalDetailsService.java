package com.vinsys.hrms.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSEmailTemplateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.dao.IHRMSReminderMailDAO;
import com.vinsys.hrms.dao.confirmation.IHRMSProbationFeedback;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidate;
import com.vinsys.hrms.datamodel.VOCandidateProfessionalDetail;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MapReminderMailCCEmployee;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.confirmation.ProbationFeedback;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/candidateProfessionalDetails")
//
public class CandidateProfessionalDetailsService {
	@Value("${base.url}")
	private String baseURL;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSProfessionalDetailsDAO candidateProfessionalDtlDAO;
	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO configDAO;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;
	@Autowired
	private IHRMSProbationFeedback feedbackDao;
	@Autowired
	IHRMSReminderMailDAO reminderMailDAO;

	@Autowired
	IHRMSEmailTemplateDAO emailTemplateDAO;

	@Value("${PROBATION_REMINDER_NO_OF_DAYS}")
	private int probationReminderNoofDays;

	@Value("${PROBATION_REMINDER_NO_OF_DAYS_TO_EMP}")
	private int probationReminderNoofDaysToEmp;

	private static final Logger logger = LoggerFactory.getLogger(CandidateProfessionalDetailsService.class);

	@RequestMapping(path = "/add", method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addCandidateProfessionalDetails(@RequestBody CandidateProfessionalDetail professionalDtl)
			throws JsonGenerationException, JsonMappingException, IOException {

		try {
			if (professionalDtl != null && professionalDtl.getCandidate() != null) {

				Candidate candidate = candidateDAO.findById(professionalDtl.getCandidate().getId()).get();

				if (candidate != null) {
					professionalDtl.setCandidate(candidate);
					candidateProfessionalDtlDAO.save(professionalDtl);
					HRMSBaseResponse response = new HRMSBaseResponse();
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(response);

				} else {
					throw new HRMSException(IHRMSConstants.UserNotFoundCode,
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
		// HashSet<CandidateCertification> candidateCertificationSet
		// =certifactionDtlDAO.findBycandidateProfessionalDetail(professionalDetails);
		// HashSet<CandidateChecklist>
		// candidateChecklistSet=candidateChecklistDAO.findyBycandidateProfessionalDetail(professionalDetails);
		// HashSet<CandidateOverseasExperience>
		// CandidateOverseasExperienceSet=candidateOverseasExperienceDAO.findBycandidateProfessionalDetail(professionalDetails);
		// HashSet<CandidatePreviousEmployment>
		// candidatePreviousEmploymentSet=candidatePreviousEmploymentDAO.findBycandidateProfessionalDetail(professionalDetails);
		// HashSet<CandidateQualification>
		// candidateQualificationSet=candidateQualificationDAO.findBycandidateProfessionalDetail(professionalDetails);

		// professionalDtl.setCandidate(candidate);
		// professionalDtl.setCandidateChecklists(candidateChecklistSet);
		// professionalDtl.setCandidateCertifications(candidateCertificationSet);
		// professionalDtl.setCandidateQualifications(candidateQualificationSet);
		// professionalDtl.setCandidatePreviousEmployments(candidatePreviousEmploymentSet);
		// professionalDtl.setCandidateOverseasExperiences(CandidateOverseasExperienceSet);

		// CandidateCertification certificationDtl=certificateDAO.findById(arg0)
		return null;
		// return HRMSHelper.createJsonString(new CandidateProfessionalDetail());

	}

	public void probationCompletionReminder() {
		// TODO Auto-generated method stub
		List<Object[]> probReportingManageremployeeList = new ArrayList<Object[]>();
		probReportingManageremployeeList = candidateDAO.findProbationaryCandidate(IHRMSConstants.isActive,
				IHRMSConstants.PROBATIONARYDESCRIPTION, IHRMSConstants.isActive, probationReminderNoofDays);

		for (Object[] resultSet : probReportingManageremployeeList) {
			{

				logger.info("Reporting officer " + resultSet[0]);
				if (!HRMSHelper.isNullOrEmpty(String.valueOf(resultSet[0]))) {
					long roId = Long.parseLong(String.valueOf(resultSet[0]));

					logger.info("Reporting officer " + roId);
					List<Employee> empList = new ArrayList<>();
					List<Object[]> probationaryEmployeeList = candidateDAO.findProbationdaryEmployeeUSingRO(
							IHRMSConstants.isActive, IHRMSConstants.PROBATIONARYDESCRIPTION, probationReminderNoofDays,
							roId);
					Employee reportingManager = employeeDAO.findById(roId).get();
					for (Object[] resultSetEmployee : probationaryEmployeeList) {

						long empId = Long.parseLong(String.valueOf(resultSetEmployee[1]));
						logger.info("Emp Id----->" + empId + "Reporting officer " + roId);

						Employee employee = employeeDAO.findById(empId).get();
						if (!employeeService.hasEmployeeResigned(employee)) {
							empList.add(employee);
						}

					}
					if (!HRMSHelper.isNullOrEmpty(empList)) {
						long divId = reportingManager.getCandidate().getCandidateProfessionalDetail().getDivision()
								.getId();
						long orgId = reportingManager.getCandidate().getLoginEntity().getOrganization().getId();
						/*
						 * MasterOrganizationEmailConfig masterOrganizationEmailConfigEntity =
						 * configDAO.findByorganizationAnddivision(
						 * reportingManager.getCandidate().getLoginEntity().getOrganization().getId(),
						 * reportingManager.getCandidate().getCandidateProfessionalDetail().getDivision(
						 * ).getId()); //String hrEmailIds = ""; String orgLevelEmailId =
						 * masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getOfficialEmailId(
						 * ); // Map<String, String> map = createMailContent(empList);
						 */

						String ccEmailId = null;
						StringBuffer ccEmailIdSB = null;
						// String orgLevelEmailId =
						// masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getOfficialEmailId();
						List<MapReminderMailCCEmployee> reminderMailCCEmpList = reminderMailDAO
								.findReminderMailCCByOrgDiv(
										IHRMSConstants.REMINDER_MAIL_TYPE_MANAGER_ACTION_PENDING_PROBATION,
										reportingManager.getCandidate().getLoginEntity().getOrganization().getId(),
										reportingManager.getCandidate().getCandidateProfessionalDetail().getDivision()
												.getId(),
										IHRMSConstants.isActive);
						if (!HRMSHelper.isNullOrEmpty(reminderMailCCEmpList)) {
							for (MapReminderMailCCEmployee ccEmp : reminderMailCCEmpList) {
								if (!HRMSHelper.isNullOrEmpty(ccEmp) && !HRMSHelper.isNullOrEmpty(ccEmp.getEmployee())
										&& !HRMSHelper.isNullOrEmpty(ccEmp.getEmployee().getOfficialEmailId())) {
									if (HRMSHelper.isNullOrEmpty(ccEmailIdSB)) {
										ccEmailIdSB = new StringBuffer(ccEmp.getEmployee().getOfficialEmailId());
									} else {
										ccEmailIdSB.append(";" + ccEmp.getEmployee().getOfficialEmailId());
									}
								}
							}
						}
						if (!HRMSHelper.isNullOrEmpty(ccEmailIdSB)) {
							ccEmailId = ccEmailIdSB.toString();
						}
						/**
						 * Code added by Ritesh Kolhe on 02-Jun-2021 to send P2C notification to HR as
						 * well.
						 */

						// Employee tempEmpEntity = employeeDAO.findActiveEmployeeById(,
						// IHRMSConstants.isActive);
						// logger.info( tempEmpEntity.getId() + " "
						// +tempEmpEntity.getCandidate().getFirstName() + " " +
						// tempEmpEntity.getCandidate().getLastName());
						Organization organizationEntity = organizationDAO.findById(orgId).get();
						List<Object[]> empresult = employeeDAO.findHREmailId(organizationEntity.getId(),
								IHRMSConstants.isActive);

						List<Employee> emplist = new ArrayList<>();

						if (empresult != null && !empresult.isEmpty()) {

							for (Object[] resultSet1 : empresult) {
								Employee employee = new Employee();
								employee.setId(Long.parseLong(String.valueOf(resultSet1[0])));
								employee.setOfficialEmailId(String.valueOf(resultSet1[3]));
								emplist.add(employee);
							}
						}
						String ccEmailIds = "";
						for (Employee hrobj : emplist) {
							// ccEmailIds= ccEmailIds +";"+ hrobj.getOfficialEmailId()+";"+ccEmailId;
							ccEmailIds = ccEmailIds + ";" + hrobj.getOfficialEmailId();
						}

						// existing code starts

						Map<String, String> map = createMailContentNew(empList);
						map.put("{roName}", reportingManager.getCandidate().getFirstName() + " "
								+ reportingManager.getCandidate().getLastName());
						String mailContent = "";

						String mailSubject = IHRMSConstants.MailSubject_ProbationReminder;
						mailContent = HRMSHelper.replaceString(map,
								IHRMSEmailTemplateConstants.Template_Probation_ReminderNew);
						emailsender.toPersistEmail(reportingManager.getOfficialEmailId(), ccEmailIds, mailContent,
								mailSubject, divId, orgId, IHRMSConstants.IS_MAIL_WITH_ATTACHMENT_N, null);
					}

				}
			}
		}
	}

	private Map<String, String> createMailContentNew(List<Employee> empList) {

		try {
			Map<String, String> placeHolder = new HashMap<String, String>();
			placeHolder.put("{websiteURL}", baseURL);

			StringBuffer empDetailDataSB = new StringBuffer();
			if (!HRMSHelper.isNullOrEmpty(empList)) {
				for (Employee employee : empList) {
					empDetailDataSB.append("<tr><td style=\"width:33.33%;\">" + employee.getCandidate().getFirstName()
							+ " " + employee.getCandidate().getLastName() + "</td>" + "<td style=\"width:33.33%;\">"
							+ employee.getId() + "</td><td style=\"width:33.33%;\">"
							+ employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining()
							+ "</td></tr>");
				}
			}

			StringBuffer empDetailMailContentSB = null;

			empDetailMailContentSB = new StringBuffer(
					"<table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\"><tbody>"
							+ "<tr style=\"color:white;\" bgcolor=\"#e98700\"><td style=\"width:14.28%;\">Employee Name</td>"
							+ "<td style=\"width:14.28%;\">Employee Id</td>"
							+ "<td style=\"width:14.28%;\">Date Of Joining</td></tr>" + empDetailDataSB.toString()
							+ "</tbody></table>");

			placeHolder.put("{empDetail}", empDetailMailContentSB.toString());

			return placeHolder;
		} catch (Exception unknown) {
			unknown.printStackTrace();
		}
		return null;
	}

	/**
	 * Code Added By Ritesh Kolhe on 07 June 2021 For Probation Reminder to
	 * candidates...
	 */
	public String probationCompletionReminderForEmployees() {

		try {

			List<Object> listResponse = new ArrayList<Object>();
			List<Object[]> probationaryEmployeeList = candidateDAO.findProbationdaryEmployee(IHRMSConstants.isActive,
					IHRMSConstants.PROBATIONARYDESCRIPTION, probationReminderNoofDaysToEmp);

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

					listResponse.add(candObj);

					long divisionId = Long.parseLong(objects[5].toString());
					long orgId = Long.parseLong(objects[6].toString());

					MasterDivision divisionEntity = divisionDAO.findById(divisionId).get();
					Organization organizationEntity = organizationDAO.findById(orgId).get();

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

					ProbationFeedback probationFeedbacks = feedbackDao
							.findByIsActiveAndEmployeeIdAndProbationStatus(IHRMSConstants.isActive, empObj.getId());

					Employee emp = new Employee();
					List<Employee> prbemplist = new ArrayList<>();
					if (probationFeedbacks != null) {
						emp.setId(probationFeedbacks.getEmployee().getId());
						emp.setOfficialEmailId(probationFeedbacks.getEmployee().getOfficialEmailId());
						prbemplist.add(emp);
					}

					if (HRMSHelper.isNullOrEmpty(prbemplist)) {

						EmailTemplate template = emailTemplateDAO
								.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
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

							emailsender.toPersistEmail(empObj.getOfficialEmailId(), ccEmailIds, mailBody,
									template.getEmailSubject(), divisionId, orgId); // New Line tO pERSIST
						} else {
							throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
									IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
						}
					} else {
						logger.info("Employees has already filled the performance remarks");
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

	/**
	 * @author Monika Gargote
	 * @description :probation remainder scheduler for Reporting manager after
	 *              5days.
	 */
	public void probationCompletionReminderToRO() {
		// TODO Auto-generated method stub
		List<Object[]> probReportingManageremployeeList = new ArrayList<Object[]>();
		probReportingManageremployeeList = candidateDAO.findProbationaryCandidateToHR(IHRMSConstants.isActive,
				IHRMSConstants.PROBATIONARYDESCRIPTION, IHRMSConstants.isActive, probationReminderNoofDays);

		for (Object[] resultSet : probReportingManageremployeeList) {
			{

				logger.info("Reporting officer " + resultSet[0]);
				if (!HRMSHelper.isNullOrEmpty(String.valueOf(resultSet[0]))) {
					long roId = Long.parseLong(String.valueOf(resultSet[0]));

					logger.info("Reporting officer " + roId);
					List<Employee> empList = new ArrayList<>();
					List<Object[]> probationaryEmployeeList = candidateDAO.findProbationdaryEmployeeUSingROToHR(
							IHRMSConstants.isActive, IHRMSConstants.PROBATIONARYDESCRIPTION, probationReminderNoofDays,
							roId);
					Employee reportingManager = employeeDAO.findById(roId).get();
					for (Object[] resultSetEmployee : probationaryEmployeeList) {

						long empId = Long.parseLong(String.valueOf(resultSetEmployee[1]));
						logger.info("Emp Id----->" + empId + "Reporting officer " + roId);

						Employee employee = employeeDAO.findById(empId).get();
						if (!employeeService.hasEmployeeResigned(employee)) {
							empList.add(employee);
						}

					}
					if (!HRMSHelper.isNullOrEmpty(empList)) {
						long divId = reportingManager.getCandidate().getCandidateProfessionalDetail().getDivision()
								.getId();
						long orgId = reportingManager.getCandidate().getLoginEntity().getOrganization().getId();
						/*
						 * MasterOrganizationEmailConfig masterOrganizationEmailConfigEntity =
						 * configDAO.findByorganizationAnddivision(
						 * reportingManager.getCandidate().getLoginEntity().getOrganization().getId(),
						 * reportingManager.getCandidate().getCandidateProfessionalDetail().getDivision(
						 * ).getId()); //String hrEmailIds = ""; String orgLevelEmailId =
						 * masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getOfficialEmailId(
						 * ); // Map<String, String> map = createMailContent(empList);
						 */

						String ccEmailId = null;
						StringBuffer ccEmailIdSB = null;
						// String orgLevelEmailId =
						// masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getOfficialEmailId();
						List<MapReminderMailCCEmployee> reminderMailCCEmpList = reminderMailDAO
								.findReminderMailCCByOrgDiv(
										IHRMSConstants.REMINDER_MAIL_TYPE_MANAGER_ACTION_PENDING_PROBATION,
										reportingManager.getCandidate().getLoginEntity().getOrganization().getId(),
										reportingManager.getCandidate().getCandidateProfessionalDetail().getDivision()
												.getId(),
										IHRMSConstants.isActive);
					
						if (!HRMSHelper.isNullOrEmpty(reminderMailCCEmpList)) {
							for (MapReminderMailCCEmployee ccEmp : reminderMailCCEmpList) {
								if (!HRMSHelper.isNullOrEmpty(ccEmp) && !HRMSHelper.isNullOrEmpty(ccEmp.getEmployee())
										&& !HRMSHelper.isNullOrEmpty(ccEmp.getEmployee().getOfficialEmailId())) {
									
									if (HRMSHelper.isNullOrEmpty(ccEmailIdSB)) {
										ccEmailIdSB = new StringBuffer(ccEmp.getEmployee().getOfficialEmailId());
									} else {
										ccEmailIdSB.append(";" + ccEmp.getEmployee().getOfficialEmailId());
									}
								}
							}
						}
						if (!HRMSHelper.isNullOrEmpty(ccEmailIdSB)) {
							ccEmailId = ccEmailIdSB.toString();
						}
						
						// Employee tempEmpEntity = employeeDAO.findActiveEmployeeById(,
						// IHRMSConstants.isActive);
						// logger.info( tempEmpEntity.getId() + " "
						// +tempEmpEntity.getCandidate().getFirstName() + " " +
						// tempEmpEntity.getCandidate().getLastName());
						Organization organizationEntity = organizationDAO.findById(orgId).get();
						List<Object[]> empresult = employeeDAO.findHREmailId(organizationEntity.getId(),
								IHRMSConstants.isActive);
						
						List<Employee> emplist = new ArrayList<>();

						if (empresult != null && !empresult.isEmpty()) {

							for (Object[] resultSet1 : empresult) {
								Employee employee = new Employee();
								employee.setId(Long.parseLong(String.valueOf(resultSet1[0])));
								employee.setOfficialEmailId(String.valueOf(resultSet1[3]));
								emplist.add(employee);
							}
						}
						String ccEmailIds = "";
						for (Employee hrobj : emplist) {
							// ccEmailIds= ccEmailIds +";"+ hrobj.getOfficialEmailId()+";"+ccEmailId;
							ccEmailIds = ccEmailIds + ";" + hrobj.getOfficialEmailId();
						}

						// existing code starts

						Map<String, String> map = createMailContentNew(empList);
						map.put("{roName}", reportingManager.getCandidate().getFirstName() + " "
								+ reportingManager.getCandidate().getLastName());
						String mailContent = "";

						String mailSubject = IHRMSConstants.MailSubject_ProbationReminder;
						mailContent = HRMSHelper.replaceString(map,
								IHRMSEmailTemplateConstants.Template_Probation_ReminderNew);
						emailsender.toPersistEmail(reportingManager.getOfficialEmailId(), ccEmailIds, mailContent,
								mailSubject, divId, orgId, IHRMSConstants.IS_MAIL_WITH_ATTACHMENT_N, null);
					}

				}
			}
		}
	}
	/**
	 * @author monika
	 * @description after completion of Probation period this is reminder to employee on Monday in every week when he/she didn't fill the p2cform
	 */
	public void probationCompletionReminderToEmployee() {
		try {
			// TODO Auto-generated method stub
			List<Object[]> probemployeeList = new ArrayList<Object[]>();
			probemployeeList = candidateDAO.findProbationaryCandidate(IHRMSConstants.isActive,
					IHRMSConstants.PROBATIONARYDESCRIPTION);

			for (Object[] resultSet : probemployeeList) {

				logger.info("Employee Id : " + resultSet[0]);
				if (!HRMSHelper.isNullOrEmpty(String.valueOf(resultSet[0]))) {
					long empId = Long.parseLong(String.valueOf(resultSet[0]));
					logger.info("Employee Id :" + empId);

					Employee employee = employeeDAO.findById(empId).get();

					long divisionId = Long.parseLong(resultSet[1].toString());
					long orgId = Long.parseLong(resultSet[2].toString());

					MasterDivision divisionEntity = divisionDAO.findById(divisionId).get();
					Organization organizationEntity = organizationDAO.findById(orgId).get();

					Date probationCompletionDate = null;
					VOCandidateProfessionalDetail voCandidateProfessionalDetail = new VOCandidateProfessionalDetail();
					voCandidateProfessionalDetail.setDateOfJoining(
							HRMSDateUtil.parse(resultSet[4].toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
					if (!HRMSHelper.isNullOrEmpty(voCandidateProfessionalDetail.getDateOfJoining())) {
						probationCompletionDate = HRMSDateUtil.incByMonth(
								voCandidateProfessionalDetail.getDateOfJoining(),
								Integer.parseInt(resultSet[5].toString()));
					}

					if (feedbackDao.existsByEmployeeId(empId)) {
						logger.info("Employee is already Fill The Form--->" + empId);
					} else {
						EmailTemplate template = emailTemplateDAO
								.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
										IHRMSConstants.Template_Probation_Reminder_To_Employee, IHRMSConstants.isActive,
										divisionEntity, organizationEntity);
						if (!HRMSHelper.isNullOrEmpty(template)) {
							Map<String, String> placeHolderMap = new HashMap<String, String>();
							placeHolderMap.put("{employeeName}", employee.getCandidate().getFirstName() + " "
									+ employee.getCandidate().getLastName());
							placeHolderMap.put("{websiteURL}", baseURL);

							String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

							emailsender.toPersistEmail(employee.getOfficialEmailId(), null, mailBody,
									template.getEmailSubject(), divisionId, orgId); // New Line tO pERSIST
						} else {
							throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
									IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
						}

					}
				} // empId-if

			} // outer for

		}

		catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				// return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage,
				// IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}// method

}
