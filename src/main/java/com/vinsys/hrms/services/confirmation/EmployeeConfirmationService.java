package com.vinsys.hrms.services.confirmation;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSEmailTemplateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSMapCatalogue;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.dao.confirmation.IHRMSMasterParameterName;
import com.vinsys.hrms.dao.confirmation.IHRMSProbationFeedback;
import com.vinsys.hrms.dao.confirmation.IHRMSProbationParameter;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidate;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.confirmation.VOEmployeeFeedback;
import com.vinsys.hrms.datamodel.confirmation.VOFeedbackResponse;
import com.vinsys.hrms.datamodel.confirmation.VOMasterEvaluationParameter;
import com.vinsys.hrms.datamodel.confirmation.VOProbationFeedback;
import com.vinsys.hrms.datamodel.confirmation.VOProbationParameter;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.confirmation.MasterEvaluationParameter;
import com.vinsys.hrms.entity.confirmation.ProbationFeedback;
import com.vinsys.hrms.entity.confirmation.ProbationParameter;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/confirmation")
//
public class EmployeeConfirmationService {

	Logger logger = LoggerFactory.getLogger(EmployeeConfirmationService.class);

	@Autowired
	private IHRMSProbationFeedback feedbackDao;

	@Autowired
	private IHRMSEmployeeDAO empDao;

	@Autowired
	private IHRMSProbationParameter paramValueDao;

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

	@RequestMapping(value = "/saveFeedback", method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String saveEmployeeProbationFeedback(@RequestBody VOProbationFeedback requestObj) {
		logger.info("inside saveEmployeeProbationFeedback()");

		try {
			if (!HRMSHelper.isNullOrEmpty(requestObj)) {

				ProbationFeedback tempObj = new ProbationFeedback();
				HRMSListResponseObject listResponse = new HRMSListResponseObject();

				if (requestObj.getId() == 0) {

					tempObj.setCreatedBy(requestObj.getCreatedBy());
					tempObj.setCreatedDate(new Date());
				} else {
					tempObj.setId(requestObj.getId());
					tempObj.setUpdatedBy(requestObj.getUpdatedBy());
					tempObj.setUpdatedDate(new Date());
					ProbationFeedback temp = feedbackDao.findByIdAndIsActive(requestObj.getId(),
							IHRMSConstants.isActive);
					tempObj.setCreatedBy(temp.getCreatedBy());
					tempObj.setCreatedDate(temp.getCreatedDate());
				}
				Employee emp = empDao.findActiveEmployeeById(requestObj.getEmployee().getId(), IHRMSConstants.isActive);

				if (!HRMSHelper.isNullOrEmpty(emp)) {
					tempObj.setEmployee(emp);
				} else {
					listResponse.setResponseCode(IHRMSConstants.DataNotFoundCode);
					listResponse.setResponseMessage(IHRMSConstants.DataNotFoundMessage);
					return HRMSHelper.createJsonString(listResponse);

				}
//				emp.setId(emp.getId());			
				tempObj.setIsActive(IHRMSConstants.isActive);
				tempObj.setHrComment(requestObj.getHrComment());
				tempObj.setManagerComment(requestObj.getManagerComment());
				tempObj.setStatus(requestObj.getStatus());
				tempObj.setHrSubmitted(requestObj.getHrSubmitted());
				tempObj.setRoSubmitted(requestObj.getRoSubmitted());
				tempObj.setExtendedBy(requestObj.getExtendedBy());

				List<VOProbationParameter> parameterList = requestObj.getProbationParameter();
				List<ProbationParameter> list1 = new ArrayList<>();
				for (VOProbationParameter obj : parameterList) {

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

						// temp.setId(obj.getId());
						temp.setEmpRating(Long.valueOf(obj.getEmpRating()));
						temp.setEmployeeComment(obj.getEmployeeComment());
						temp.setManagerRating(Long.valueOf(obj.getManagerRating()));
						temp.setManagerComment(obj.getManagerComment());
						MasterEvaluationParameter param = parameterDao
								.findByIdAndIsActive(obj.getParameterValue().getId(), IHRMSConstants.isActive);
						temp.setParameterValue(param);
						temp.setFeedback(tempObj);
						list1.add(temp);
					}
				}
				tempObj.setProbationParameter(list1);

				ProbationFeedback tempFeedback = feedbackDao.save(tempObj);
				// HRMSListResponseObject listResponse = new HRMSListResponseObject();

				if (!HRMSHelper.isNullOrEmpty(tempFeedback)) {

					// To update final employee status when feedback submitted by HR
					if (tempFeedback.getHrSubmitted()) {

						Employee tempEmp = this.empDao.findActiveEmployeeById(tempFeedback.getEmployee().getId(),
								IHRMSConstants.isActive);

						if (!HRMSHelper.isNullOrEmpty(tempFeedback.getStatus())) {

							if (!HRMSHelper.isNullOrEmpty(tempEmp)) {
								Candidate candidate = this.candidateDao
										.findByIdAndIsActive(tempEmp.getCandidate().getId(), IHRMSConstants.isActive);

								MasterEmploymentType employmentTypeEntity = null;

								if (tempFeedback.getStatus().equals(IHRMSConstants.CONFIRMED)) {

									employmentTypeEntity = employmenttypeDao.findByIsActiveAndEmploymentTypeName(
											IHRMSConstants.isActive, IHRMSConstants.CONFIRMED);
								} else {
									employmentTypeEntity = employmenttypeDao.findByIsActiveAndEmploymentTypeName(
											IHRMSConstants.isActive, IHRMSConstants.PROBATION);

								}

								if (!HRMSHelper.isNullOrEmpty(employmentTypeEntity)) {
									candidate
											.setCandidateEmploymentStatus(employmentTypeEntity.getEmploymentTypeName());
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
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.DataNotFoundMessage);
						}

					}

					// send mail logic
					Employee tempEmpEntity = empDao.findActiveEmployeeById(requestObj.getEmployee().getId(),
							IHRMSConstants.isActive);
					logger.info(tempEmpEntity.getId() + " " + tempEmpEntity.getCandidate().getFirstName() + " "
							+ tempEmpEntity.getCandidate().getLastName());
					MasterDivision divisionEntity = tempEmpEntity.getCandidate().getCandidateProfessionalDetail()
							.getDivision();
					Organization organizationEntity = tempEmpEntity.getCandidate().getLoginEntity().getOrganization();

					Employee reportingManager = tempEmpEntity.getEmployeeReportingManager().getReporingManager();
					String reportingManagerName = reportingManager.getCandidate().getFirstName() + " "
							+ reportingManager.getCandidate().getLastName();
					logger.info(reportingManager.getId() + " " + reportingManager.getCandidate().getFirstName() + " "
							+ reportingManager.getCandidate().getLastName());

					MapCatalogue mapcatalogue = catalogueDAO.findHR(organizationEntity.getId(), "HR",
							tempEmpEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
					Employee hr = mapcatalogue.getApprover();
					if (requestObj.getActionTakenBy().equalsIgnoreCase("Employee")) {
						// send mail to Employee
						/**
						 * Code added by Ritesh Kolhe on 02-Jun-2021 to send P2C notification to HR as
						 * well.
						 */

						Employee tempEmpEntity1 = empDao.findActiveEmployeeById(requestObj.getEmployee().getId(),
								IHRMSConstants.isActive);
						logger.info(tempEmpEntity1.getId() + " " + tempEmpEntity.getCandidate().getFirstName() + " "
								+ tempEmpEntity.getCandidate().getLastName());
						Organization organizationObj = tempEmpEntity.getCandidate().getLoginEntity().getOrganization();
						List<Object[]> empresult1 = empDao.findHREmailId(organizationObj.getId(),
								IHRMSConstants.isActive);

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

						EmailTemplate template = emailTemplateDAO
								.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
										IHRMSConstants.P2C_SUBMITTED_BY_EMPlOYEE_TEMPLATE, IHRMSConstants.isActive,
										divisionEntity, organizationEntity);
						if (!HRMSHelper.isNullOrEmpty(template)) {
							Map<String, String> placeHolderMap = new HashMap<String, String>();
							placeHolderMap.put("{employeeName}",
									tempFeedback.getEmployee().getCandidate().getFirstName() + " "
											+ tempFeedback.getEmployee().getCandidate().getLastName());
							placeHolderMap.put("{managerName}", reportingManagerName);
							String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

							emailsender.toPersistEmail(reportingManager.getOfficialEmailId(), ccEmailIds, mailBody,
									template.getEmailSubject(), divisionEntity.getId(), organizationEntity.getId());
						} else {
							throw new HRMSException(IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_CODE,
									IHRMSConstants.EMAIL_TEMPLATE_NOT_FOUND_MESSAGE);
						}

					} else if (requestObj.getActionTakenBy().equalsIgnoreCase("Manager")) {
						// send mail to Manager
						EmailTemplate template = emailTemplateDAO
								.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
										IHRMSConstants.P2C_SUBMITTED_BY_MANAGER_TEMPLATE, IHRMSConstants.isActive,
										divisionEntity, organizationEntity);
						if (!HRMSHelper.isNullOrEmpty(template)) {
							Map<String, String> placeHolderMap = new HashMap<String, String>();
							placeHolderMap.put("{employeeName}",
									tempFeedback.getEmployee().getCandidate().getFirstName() + " "
											+ tempFeedback.getEmployee().getCandidate().getLastName());
							placeHolderMap.put("{TAT}", "5");
							placeHolderMap.put("{hrName}",
									hr.getCandidate().getFirstName() + " " + hr.getCandidate().getLastName());

							String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

							/**
							 * Code added by Ritesh Kolhe on 02-Jun-2021 to send P2C notification to HR as
							 * well.
							 */

							Employee tempEmpEntity2 = empDao.findActiveEmployeeById(requestObj.getEmployee().getId(),
									IHRMSConstants.isActive);
							logger.info(tempEmpEntity2.getId() + " " + tempEmpEntity.getCandidate().getFirstName() + " "
									+ tempEmpEntity.getCandidate().getLastName());
							Organization organizationObj = tempEmpEntity.getCandidate().getLoginEntity()
									.getOrganization();
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
					} else if (requestObj.getActionTakenBy().equalsIgnoreCase("HR")) {
						// send mail to HR

						CandidateProfessionalDetail candProfDetail = candidateProfessionalDao
								.findBycandidate(tempFeedback.getEmployee().getCandidate());
						int probationPeriod = Integer.parseInt(
								HRMSHelper.isNullOrEmpty(tempFeedback.getEmployee().getProbationPeriod()) ? "0"
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

					VOFeedbackResponse feedbackResponse = new VOFeedbackResponse();
					feedbackResponse.setProbationParameter(new ArrayList<>());

					feedbackResponse.setId(tempFeedback.getId());
					feedbackResponse.setCreatedBy(tempFeedback.getCreatedBy());
					feedbackResponse.setCreatedDate(tempFeedback.getCreatedDate());
					feedbackResponse.setIsActive(feedbackResponse.getIsActive());
					feedbackResponse.setEmployeeId(tempFeedback.getEmployee().getId());
					feedbackResponse.setEmployeeName(tempFeedback.getEmployee().getCandidate().getFirstName() + " "
							+ tempFeedback.getEmployee().getCandidate().getLastName());
					feedbackResponse.setHrComment(tempFeedback.getHrComment());
					feedbackResponse.setManagerComment(tempFeedback.getManagerComment());

					feedbackResponse.setEmployee(requestObj.getEmployee());

					List<ProbationParameter> list2 = tempFeedback.getProbationParameter();

					if (!HRMSHelper.isNullOrEmpty(list2)) {
						for (ProbationParameter probationParameter : list2) {
							if (!HRMSHelper.isNullOrEmpty(probationParameter)) {
								VOProbationParameter tempParameter = new VOProbationParameter();
								tempParameter.setId(probationParameter.getId());
								tempParameter.setEmpRating(String.valueOf(probationParameter.getEmpRating()));
								tempParameter.setEmployeeComment(probationParameter.getEmployeeComment());
								tempParameter.setManagerRating(String.valueOf(probationParameter.getManagerRating()));
								tempParameter.setManagerComment(probationParameter.getManagerComment());
								// tempParameter.setFeedbackId(probationParameter.getFeedback().getId());
								VOMasterEvaluationParameter tempMaster = new VOMasterEvaluationParameter();
								tempMaster.setId(probationParameter.getParameterValue().getId());
								tempMaster.setOrganizationId(
										probationParameter.getParameterValue().getOrganization().getId());
								tempMaster.setParameterName(probationParameter.getParameterValue().getParameterName());
								tempParameter.setParameterValue(tempMaster);

								feedbackResponse.getProbationParameter().add(tempParameter);
							}

						}
					}

					List<Object> tempList = new ArrayList<>();
					tempList.add(feedbackResponse);
					listResponse.setResponseCode(IHRMSConstants.successCode);
					listResponse.setResponseMessage(IHRMSConstants.successMessage);
					listResponse.setListResponse(tempList);
					return HRMSHelper.createJsonString(listResponse);

				} else {
					throw new HRMSException(IHRMSConstants.UnknowErrorCode, IHRMSConstants.UnknowErrorMessage);
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

	@RequestMapping(value = "/getFeedbackDetails/{employeeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getFeedbackDetails(@PathVariable("employeeId") long employeeId) {

		try {
			if (!HRMSHelper.isLongZero(employeeId)) {

				List<Object> tempList = new ArrayList<>();

				Employee employee = empDao.findActiveEmployeeWithCandidateByEmpIdAndOrgId(employeeId, IHRMSConstants.isActive,SecurityFilter.TL_CLAIMS.get().getOrgId());

				VOEmployeeFeedback empFeedback = new VOEmployeeFeedback();

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

				empFeedback
						.setLocation(employee.getCandidate().getCandidateProfessionalDetail().getCity().getCityName());

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
				empFeedback.setReportingManager(employee.getEmployeeReportingManager().getReporingManager()
						.getCandidate().getFirstName() + " "
						+ employee.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());

				List<ProbationFeedback> probationFeedbacks = feedbackDao
						.findByIsActiveAndEmployeeId(IHRMSConstants.isActive, employeeId);
				List<VOProbationFeedback> feedbackList = new ArrayList<>();

				for (ProbationFeedback probationFeedback : probationFeedbacks) {
					VOProbationFeedback parameterFeedback = new VOProbationFeedback();

					// String tmpDate=HRMSDateUtil.format(probationFeedback.getCreatedDate(),
					// IHRMSConstants.POSTGRE_DATE_FORMAT);
					logger.info("date : " + probationFeedback.getCreatedDate().getTime());
					parameterFeedback.setCreatedDate(probationFeedback.getCreatedDate());
					parameterFeedback.setCreatedBy(probationFeedback.getCreatedBy());
					parameterFeedback.setHrComment(probationFeedback.getHrComment());
					parameterFeedback.setManagerComment(probationFeedback.getManagerComment());
					parameterFeedback.setStatus(probationFeedback.getStatus());
					parameterFeedback.setExtendedBy(probationFeedback.getExtendedBy());
					parameterFeedback.setId(probationFeedback.getId());

					List<ProbationParameter> parameterList = probationFeedback.getProbationParameter();
					List<VOProbationParameter> list1 = new ArrayList<>();

					for (ProbationParameter obj : parameterList) {

						if (!HRMSHelper.isNullOrEmpty(obj)) {
							VOProbationParameter temp = new VOProbationParameter();
							temp.setId(obj.getId());
							temp.setEmpRating(String.valueOf(obj.getEmpRating()));
							temp.setEmployeeComment(obj.getEmployeeComment());
							temp.setManagerRating(String.valueOf(obj.getManagerRating()));
							temp.setManagerComment(obj.getManagerComment());

							if (!HRMSHelper.isNullOrEmpty(obj.getParameterValue())) {
								MasterEvaluationParameter param = parameterDao
										.findByIdAndIsActive(obj.getParameterValue().getId(), IHRMSConstants.isActive);
								VOMasterEvaluationParameter tempParam = new VOMasterEvaluationParameter();
								tempParam.setId(param.getId());
								tempParam.setOrganizationId(param.getOrganization().getId());
								tempParam.setParameterName(param.getParameterName());
								temp.setParameterValue(tempParam);
							}

							temp.setFeedbackId(parameterFeedback.getId());
							list1.add(temp);
						}

					}
					parameterFeedback.setProbationParameter(list1);

					feedbackList.add(parameterFeedback);
				}

				empFeedback.setFeedback(feedbackList);

				HRMSListResponseObject listResponse = new HRMSListResponseObject();
				listResponse.setResponseCode(IHRMSConstants.successCode);
				listResponse.setResponseMessage(IHRMSConstants.successMessage);
				tempList.add(empFeedback);

				listResponse.setListResponse(tempList);
				return HRMSHelper.createJsonString(listResponse);
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

	@RequestMapping(value = "/getPendingForManager/{employeeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getPendingListOfFeedbackConfimationForManager(@PathVariable("employeeId") long employeeId) {

		try {
			if (!HRMSHelper.isLongZero(employeeId)) {

				List<Object> tempList = new ArrayList<>();
				List<Employee> employeeList = feedbackDao.findPendingSubordinateConfirmation(employeeId,
						IHRMSConstants.isActive, false);
				if (!HRMSHelper.isNullOrEmpty(employeeList)) {
					for (Employee emp : employeeList) {
						VOEmployee employee = new VOEmployee();
						VOCandidate candidate = new VOCandidate();
						candidate.setFirstName(emp.getCandidate().getFirstName());
						candidate.setLastName(emp.getCandidate().getLastName());
						candidate.setCandidateEmploymentStatus(emp.getCandidate().getCandidateEmploymentStatus());
						employee.setCandidate(candidate);
						employee.setId(emp.getId());
						tempList.add(employee);

						VOEmployeeFeedback empFeedback = new VOEmployeeFeedback();
						empFeedback.setEmployeeId(emp.getId());
						empFeedback.setConfirmationStatus(emp.getCandidate().getCandidateEmploymentStatus());
						empFeedback.setDepartment(emp.getCandidate().getCandidateProfessionalDetail().getDepartment()
								.getDepartmentName());
						empFeedback.setDesignation(emp.getCandidate().getCandidateProfessionalDetail().getDesignation()
								.getDesignationName());
						String date = HRMSDateUtil.format(
								emp.getCandidate().getCandidateProfessionalDetail().getDateOfJoining(),
								IHRMSConstants.POSTGRE_DATE_FORMAT);
						empFeedback.setDateOfJoining(date);
						empFeedback.setLocation(
								emp.getCandidate().getCandidateProfessionalDetail().getCity().getCityName());

						int probationPeriod = 0;
						if (!HRMSHelper.isNullOrEmpty(emp.getProbationPeriod())) {
							probationPeriod = Integer.parseInt((emp.getProbationPeriod()));
						}

						Date tempDate = new Date();
						Calendar cal = Calendar.getInstance();
						cal.setTime(emp.getCandidate().getCandidateProfessionalDetail().getDateOfJoining());
						cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) + probationPeriod));
						tempDate = HRMSDateUtil.setTimeStampToZero(cal.getTime());
						String confirmationDueDate = HRMSDateUtil.format(tempDate, IHRMSConstants.POSTGRE_DATE_FORMAT);

						empFeedback.setConfirmationDueDate(confirmationDueDate);
						empFeedback.setConfirmationStatus(employee.getCandidate().getCandidateEmploymentStatus());
						empFeedback.setReportingManager(emp.getEmployeeReportingManager().getReporingManager()
								.getCandidate().getFirstName() + " "
								+ emp.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());

						tempList.add(empFeedback);

					}

					HRMSListResponseObject listResponse = new HRMSListResponseObject();
					listResponse.setResponseCode(IHRMSConstants.successCode);
					listResponse.setResponseMessage(IHRMSConstants.successMessage);
					listResponse.setListResponse(tempList);
					return HRMSHelper.createJsonString(listResponse);
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

	@RequestMapping(value = "/getPendingForHR/{employeeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getPendingListOfFeedbackConfimationForHR(@PathVariable("employeeId") long employeeId) {

		try {
			if (!HRMSHelper.isLongZero(employeeId)) {

				List<Object> tempList = new ArrayList<>();

				List<Employee> employeeList = feedbackDao.findPendingConfirmationForHR(IHRMSConstants.PROBATION,
						IHRMSConstants.isActive, false, true);
				if (!HRMSHelper.isNullOrEmpty(employeeList)) {
					for (Employee emp : employeeList) {

						VOEmployee employee = new VOEmployee();

						VOCandidate candidate = new VOCandidate();
						candidate.setFirstName(emp.getCandidate().getFirstName());
						candidate.setLastName(emp.getCandidate().getLastName());
						candidate.setCandidateEmploymentStatus(emp.getCandidate().getCandidateEmploymentStatus());

						employee.setCandidate(candidate);
						employee.setId(emp.getId());

						tempList.add(employee);

					}

					HRMSListResponseObject listResponse = new HRMSListResponseObject();
					listResponse.setResponseCode(IHRMSConstants.successCode);
					listResponse.setResponseMessage(IHRMSConstants.successMessage);

					listResponse.setListResponse(tempList);
					return HRMSHelper.createJsonString(listResponse);
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

	@RequestMapping(value = "/getPendingForEmployee/{employeeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getPendingFeedbackConfimationForEmployee(@PathVariable("employeeId") long employeeId) {

		try {
			if (!HRMSHelper.isLongZero(employeeId)) {

				List<Object> tempList = new ArrayList<>();

				Employee employee = feedbackDao.findPendingConfirmationForEmployee(employeeId, IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(employee)) {

					VOEmployee emp1 = new VOEmployee();

					VOCandidate candidate = new VOCandidate();
					candidate.setFirstName(employee.getCandidate().getFirstName());
					candidate.setLastName(employee.getCandidate().getLastName());
					candidate.setCandidateEmploymentStatus(employee.getCandidate().getCandidateEmploymentStatus());

					emp1.setCandidate(candidate);
					emp1.setId(employee.getId());

					tempList.add(emp1);

					VOEmployeeFeedback empFeedback = new VOEmployeeFeedback();
					empFeedback.setEmployeeId(employee.getId());
					empFeedback.setConfirmationStatus(employee.getCandidate().getCandidateEmploymentStatus());
					empFeedback.setDepartment(employee.getCandidate().getCandidateProfessionalDetail().getDepartment()
							.getDepartmentName());
					empFeedback.setDesignation(employee.getCandidate().getCandidateProfessionalDetail().getDesignation()
							.getDesignationName());
					String date = HRMSDateUtil.format(
							employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining(),
							IHRMSConstants.POSTGRE_DATE_FORMAT);
					empFeedback.setDateOfJoining(date);
					empFeedback.setLocation(
							employee.getCandidate().getCandidateProfessionalDetail().getCity().getCityName());

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
					empFeedback.setReportingManager(employee.getEmployeeReportingManager().getReporingManager()
							.getCandidate().getFirstName() + " "
							+ employee.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());

					tempList.add(empFeedback);

					HRMSListResponseObject listResponse = new HRMSListResponseObject();
					listResponse.setResponseCode(IHRMSConstants.successCode);
					listResponse.setResponseMessage(IHRMSConstants.successMessage);

					listResponse.setListResponse(tempList);
					return HRMSHelper.createJsonString(listResponse);
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

	// Akanksha Gaikwad

	@RequestMapping(value = "/deleteFeedback/{employeeId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String DeleteEmployeeFeedback(@PathVariable("employeeId") long employeeId) {

		try {
			if (!HRMSHelper.isLongZero(employeeId)) {

				List<ProbationFeedback> user = feedbackDao.findAll();
				// ProbationFeedback user=
				// feedbackDao.findByIdAndIsActive(employeeId,IHRMSConstants.isActive);

				if (!HRMSHelper.isNullOrEmpty(user)) {

					for (int i = 0; i < user.size(); i++) {

						if (user.get(i).getEmployee().getId() == employeeId) {

							feedbackDao.deleteById(user.get(i).getId());

						}
					}

				}
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

	private void creditLeavesForConfirmedEmployee(Employee employee) {
		try {
			logger.info("Inside credit Leaves For Confirmed Employee method");
			LocalDate currentDate = LocalDate.now();

			// Check if the employee is in Division 1 or 2 and confirmed between 1 to 15 of
			// any month
			if ((employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 1
					|| employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 2)
					&& currentDate.getDayOfMonth() <= 15) {

				float earnedLeaves = 1.5f;

				creditLeave(employee, IHRMSConstants.LEAVE_TYPE_CODE_PRIVILAGE, earnedLeaves);
				logger.info("Crediting " + earnedLeaves
						+ " leaves for confirmed employee in Division 1 or 2. Employee ID: " + employee.getId());
			}

			logger.info("Exiting creditLeavesForConfirmedEmployee method");

		} catch (Exception e) {
			logger.error("Error crediting leaves for confirmed employee: " + employee.getId(), e);
		}
	}

	private void creditLeave(Employee employee, String leaveTypeCode, float earnedLeaves) {
		try {
			logger.info("Inside Confirmed Employee credit Leave method");

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
			logger.error("Error adding EmployeeCreditLeaveDetail: " + employee.getId(), e);
		}
	}

	/**************************************************************************/
	@RequestMapping(value = "/getFeedbackDetailsForEmp/{employeeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getFeedbackDetailsForEmp(@PathVariable("employeeId") long employeeId) {

		try {
			if (!HRMSHelper.isLongZero(employeeId)) {

				Employee employee = empDao.findActiveEmployeeWithCandidateByEmpIdAndOrgId(employeeId, IHRMSConstants.isActive,SecurityFilter.TL_CLAIMS.get().getOrgId());

				ProbationFeedback probationFeedbacks = feedbackDao.findByIsActiveAndEmpId(IHRMSConstants.isActive,
						employeeId);

				VOProbationFeedback parameterFeedback = new VOProbationFeedback();
				parameterFeedback.setCreatedDate(probationFeedbacks.getCreatedDate());
				parameterFeedback.setCreatedBy(probationFeedbacks.getCreatedBy());
				parameterFeedback.setHrComment(probationFeedbacks.getHrComment());
				parameterFeedback.setManagerComment(probationFeedbacks.getManagerComment());
				parameterFeedback.setStatus(probationFeedbacks.getStatus());
				parameterFeedback.setExtendedBy(probationFeedbacks.getExtendedBy());
				parameterFeedback.setId(probationFeedbacks.getId());
				return HRMSHelper.createJsonString(parameterFeedback);

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
