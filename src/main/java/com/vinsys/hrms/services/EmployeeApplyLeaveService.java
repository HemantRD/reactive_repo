package com.vinsys.hrms.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.cookie.DateParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeCreditAppliedLeaveMappingDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSHtmlTemplateDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveType;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOAppliedLeaveCount;
import com.vinsys.hrms.datamodel.VOApplyLeaveRequest;
import com.vinsys.hrms.datamodel.VOEmployeeLeaveApplied;
import com.vinsys.hrms.datamodel.VOLeaveCalculationRequest;
import com.vinsys.hrms.datamodel.VOMasterLeaveType;
import com.vinsys.hrms.datamodel.VOTeamAbsenceDetails;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCreditAppliedLeaveMapping;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/leave")

//@PropertySource(value="${HRMSCONFIG}")
public class EmployeeApplyLeaveService {
	@Value("${base.url}")
	private String baseURL;
	private static final Logger logger = LoggerFactory.getLogger(EmployeeApplyLeaveService.class);

	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSEmployeeReportingManager reportingManagerDAO;
	@Autowired
	IHRMSMasterLeaveType leaveTypeDAO;
	@Autowired
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO empLeaveDetailsDAO;
	@Autowired
	IHRMSMasterLeaveType masterLeaveTypeDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO organizationHolidayDAO;
	@Autowired
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	@Autowired
	IHRMSHtmlTemplateDAO emailTemplateDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailsDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSEmployeeCreditLeaveDAO creditLeaveDAO;
	@Autowired
	IHRMSEmployeeCreditAppliedLeaveMappingDAO creditAppliedLeaveMappingDAO;
	@Autowired
	IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO;
	@Autowired
	EmployeeLeaveDetailsService employeeLeaveDetailsService;
	@Autowired
	IHRMSMasterLeaveTypeDAO mstLeaveTypeDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String applyLeave(@RequestBody VOApplyLeaveRequest request) {

		EmployeeLeaveApplied leavesAppliedEnity = null;
		boolean validLeaveFlag = false;

		try {

			if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getActionPerformed())) {
				if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_APPLY)) {
					// next modified by SSW on 19 Sept 2018
					// Employee whose employment status is :: employee, confirmed, permanent
					// should not apply Probation Leave
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					Date fromDate = sdf.parse(request.getLeaveApplied().getFromDate());
					Date toDate = sdf.parse(request.getLeaveApplied().getToDate());

					if (request.getLeaveApplied().getMasterLeaveType().getLeaveTypeCode().trim()
							.equalsIgnoreCase("ONDT")
							|| request.getLeaveApplied().getMasterLeaveType().getLeaveTypeCode().trim()
									.equalsIgnoreCase("WRHM")) {

						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DAY_OF_MONTH, 10);
						Date futureDate = cal.getTime();

						if (fromDate.compareTo(futureDate) > 0) {
							throw new HRMSException(IHRMSConstants.TEN_DAYS_LEAVE_RESTRICT_CODE,
									IHRMSConstants.TEN_DAYS_LEAVE_RESTRICT_MESSAGE);
						}
						long dateDiff = (toDate.getTime() - fromDate.getTime()) / 1000 / 60 / 60 / 24;

						if (dateDiff >= 10) {
							throw new HRMSException(IHRMSConstants.MAX_LEAVE_RESTRICT_CODE,
									IHRMSConstants.MAX_LEAVE_RESTRICT_MESSAGE);
						}
					}

					// Future date Restriction for SICK leave
					/*
					 * if(request.getLeaveApplied().getMasterLeaveType().getLeaveTypeCode().trim()
					 * .equalsIgnoreCase("SICK") && fromDate.compareTo(new Date()) > 0) { throw new
					 * HRMSException(IHRMSConstants.FUTURE_DATE_LEAVE_RESTRICT_CODE,IHRMSConstants.
					 * FUTURE_DATE_LEAVE_RESTRICT_MESSAGE); }
					 */

					Employee employee = null;

					if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getEmployee())
							&& !HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getEmployee().getId())) {
						employee = employeeDAO.findById(request.getLeaveApplied().getEmployee().getId()).get();
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}

					if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied())
							&& !HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getEmployee())
							&& !HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getEmployee().getId())
							&& !HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getMasterLeaveType()) && !HRMSHelper
									.isNullOrEmpty(request.getLeaveApplied().getMasterLeaveType().getLeaveTypeCode())) {
						// checking if applied leave is of type probation
						if (request.getLeaveApplied().getMasterLeaveType().getLeaveTypeCode().trim().toUpperCase()
								.equals(IHRMSConstants.PROBATION_MASTER_LEAVE_CODE.toUpperCase())) {
							// employee =
							// employeeDAO.findById(request.getLeaveApplied().getEmployee().getId());
							if (!HRMSHelper.isNullOrEmpty(employee)
									&& !HRMSHelper.isNullOrEmpty(employee.getCandidate())
									&& !HRMSHelper.isNullOrEmpty(employee.getCandidate().getEmploymentType())) {
								MasterEmploymentType mstEmpType = masterEmploymentTypeDAO
										.findById(employee.getCandidate().getEmploymentType().getId()).get();
								// checking whether employee is confirmed
								if ((mstEmpType.getEmploymentTypeDescription().toUpperCase()
										.equals(IHRMSConstants.EMPLOYMENT_TYPE_CONFIRMED.toUpperCase())
										|| mstEmpType.getEmploymentTypeDescription().toUpperCase()
												.equals(IHRMSConstants.EMPLOYMENT_TYPE_EMPLOYEE.toUpperCase())
										|| mstEmpType.getEmploymentTypeDescription().toUpperCase()
												.equals(IHRMSConstants.EMPLOYMENT_TYPE_PERMANENT.toUpperCase()))) {
									if (employee.getCandidate().getCandidateProfessionalDetail().getDivision()
											.getId() != 3) {
										throw new HRMSException(IHRMSConstants.CONFIRMED_EMP_PROB_LEAVE_RESTRICT_CODE,
												IHRMSConstants.CONFIRMED_EMP_PROB_LEAVE_RESTRICT_MESSAGE);
									}

								}
							} else {
								throw new HRMSException(IHRMSConstants.InsufficientDataCode,
										IHRMSConstants.InsufficientDataMessage);
							}
						}
						// if not of type probation, don't do anything
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}

					if (!HRMSHelper.isNullOrEmpty(employee)) {
						// long divisionId =
						// employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
						// UAE employee can apply leave for next 6 month in advance.
						if (employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 3) {
							Date incrementedDate = HRMSDateUtil
									.setTimeStampToZero(HRMSDateUtil.incByMonth(new Date(), 6));
							if (incrementedDate.compareTo(toDate) < 0) {
								throw new HRMSException(IHRMSConstants.InValidDetailsCode,
										IHRMSConstants.LEAVE_RESTRICTION_6_MONTHS_MESSAGE);
							}
						} else {
							Date incrementedDate = HRMSDateUtil
									.setTimeStampToZero(HRMSDateUtil.incByMonth(new Date(), 1));
							if (incrementedDate.compareTo(toDate) < 0) {
								if (!request.getLeaveApplied().getMasterLeaveType().getLeaveTypeCode().trim()
										.equalsIgnoreCase("MATR")) {
									throw new HRMSException(IHRMSConstants.InValidDetailsCode,
											IHRMSConstants.LEAVE_RESTRICTION_30_DAYS_MESSAGE);
								}
							}
						}
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}

					// upto this modified by SSW
					validLeaveFlag = validateLeave(request);
					if (validLeaveFlag) {
						/// validate Maternity Leave
						if (request.getLeaveApplied().getMasterLeaveType().getLeaveTypeCode().trim()
								.equalsIgnoreCase("MATR")) {
							if (request.getLeaveApplied().getNoOfDays() <= IHRMSConstants.MATR_LEAVE_VALID_DAYS) {
								logger.info("*********** VALID Maternity Leave ***********");
								leavesAppliedEnity = leaveApplyAction(request);
							} else {
								logger.info("*********** Invalid Maternity Leave ***********");
								throw new HRMSException(IHRMSConstants.InValidDetailsCode,
										IHRMSConstants.MaternityLeaveOverExceedsLimit);
							}
						} else if (request.getLeaveApplied().getMasterLeaveType().getLeaveTypeCode().trim()
								.equalsIgnoreCase("PATR")) {
							if (request.getLeaveApplied().getNoOfDays() <= IHRMSConstants.PATR_LEAVE_VALID_DAYS) {
								// MasterLeaveType masterLeaveTypeEntity =
								// mstLeaveTypeDAO.findByLeaveTypeCode(request.getLeaveApplied().getMasterLeaveType().getLeaveTypeCode());
								List<EmployeeLeaveApplied> patrleavesAppliedByEmpList = employeeLeaveAppliedDAO
										.findAllLeavesByemployeeAndmasterLeaveType(
												request.getLeaveApplied().getEmployee().getId(),
												request.getLeaveApplied().getMasterLeaveType().getId(),
												IHRMSConstants.LeaveStatus_CANCELLED,
												IHRMSConstants.LeaveStatus_WD_APPROVED,
												IHRMSConstants.LeaveStatus_REJECT);

								if (patrleavesAppliedByEmpList.size() <= 1) {
									logger.info("*********** VALID Paternity Leave ***********");
									leavesAppliedEnity = leaveApplyAction(request);
								} else {
									logger.info("*********** Invalid Paternity Leave ***********");
									throw new HRMSException(IHRMSConstants.InValidDetailsCode,
											IHRMSConstants.PaternityLeaveAvailed2Times);
								}

							} else {
								logger.info("*********** Invalid Paternity Leave ***********");
								throw new HRMSException(IHRMSConstants.InValidDetailsCode,
										IHRMSConstants.PaternityLeaveOverExceedsLimit);
							}
						} else if (request.getLeaveApplied().getMasterLeaveType().getLeaveTypeCode().trim()
								.equalsIgnoreCase("ADPT")) {

							int validCount = 0;
							Employee empEntity = employeeDAO
									.findEmpCandByEmpId(request.getLeaveApplied().getEmployee().getId());
							if (empEntity.getCandidate().getGender().trim().equalsIgnoreCase("Female")) {
								validCount = IHRMSConstants.ADPT_LEAVE_VALID_DAYS_FEMALE;
							} else {
								validCount = IHRMSConstants.ADPT_LEAVE_VALID_DAYS_MALE;
							}
							if (request.getLeaveApplied().getNoOfDays() <= validCount) {
								logger.info("*********** VALID Paternity Leave ***********");
								leavesAppliedEnity = leaveApplyAction(request);

							} else {
								logger.info("*********** Invalid Paternity Leave ***********");
								if (empEntity.getCandidate().getGender().trim().equalsIgnoreCase("Female")) {
									throw new HRMSException(IHRMSConstants.InValidDetailsCode,
											IHRMSConstants.AdoptionLeaveOverExceedsLimitFemale);
								} else {
									throw new HRMSException(IHRMSConstants.InValidDetailsCode,
											IHRMSConstants.AdoptionLeaveOverExceedsLimitMale);
								}

							}
						} else {
							logger.info("*********** VALID Leave ***********");
							leavesAppliedEnity = leaveApplyAction(request);
						}
					} else {
						logger.info("*********** INVALID Leave ***********");
						throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.LeaveOverlapsMessage);
					}
				} else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_CANCEL)) {
					leavesAppliedEnity = leaveCancelAction(request);
				} else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_WITHDRAW)) {
					leavesAppliedEnity = leaveWithdrawAction(request);
				} else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_APPROVE)) {
					leavesAppliedEnity = leaveApproveAction(request);
				} else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_REJECT)) {
					leavesAppliedEnity = leaveRejectAction(request);
				} else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_WITHDRAW_APPROVE)) {
					leavesAppliedEnity = leaveWithdrawApproveAction(request);
				} else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_WITHDRAW_REJECT)) {
					leavesAppliedEnity = leaveWithdrawRejectAction(request);
				} else {
					throw new HRMSException(IHRMSConstants.InvalidLeaveActionCode,
							IHRMSConstants.InvalidLeaveActionMessage);
				}
				List<Object> appliedLeaveList = new ArrayList<Object>();
				appliedLeaveList.add(HRMSEntityToModelMapper.convertToEmployeLeaveAppliedModel(leavesAppliedEnity));
				HRMSListResponseObject res = new HRMSListResponseObject();
				res.setResponseCode(IHRMSConstants.successCode);
				res.setResponseMessage(IHRMSConstants.LeaveActionSuccessMessage);
				res.setListResponse(appliedLeaveList);
				return HRMSHelper.createJsonString(res);
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

	private EmployeeLeaveApplied leaveWithdrawRejectAction(VOApplyLeaveRequest request)
			throws FileNotFoundException, IOException, HRMSException {
		EmployeeLeaveApplied leavesAppliedEnity;
		leavesAppliedEnity = employeeLeaveAppliedDAO.findById(request.getLeaveApplied().getId()).get();
		if (leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_WD_PENDING)) {
			leavesAppliedEnity.setApproverActionDateWithdrawn(new Date());
			leavesAppliedEnity.setApproverCommentOnWithdrawn(request.getLeaveApplied().getApproverCommentOnWithdrawn());
			leavesAppliedEnity.setUpdatedBy(request.getUpdatedBy());
			leavesAppliedEnity.setUpdatedDate(new Date());
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_WD_REJECTED);
			leavesAppliedEnity = employeeLeaveAppliedDAO.save(leavesAppliedEnity);

			/**
			 * Email Sender
			 */
			String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
			String ccEmailId = leavesAppliedEnity.getCc();
			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_WithdrawLeaveRejected);

			// logger.info("+++++++++++++++++++++++++++ MAIL CONTENT
			// ++++++++++++++++++++++++++++++++");
			// logger.info(mailContent);
			/*
			 * EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
			 * IHRMSConstants.MailSubject_LeaveWithdrawRejected);
			 */
			emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContent,
					IHRMSConstants.MailSubject_LeaveWithdrawRejected,
					leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
							.getId(),
					leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
		} else {
			throw new HRMSException(IHRMSConstants.LeaveActionErrorCode, IHRMSConstants.CannotRejectMessage);
		}
		return leavesAppliedEnity;
	}

	private EmployeeLeaveApplied leaveWithdrawApproveAction(VOApplyLeaveRequest request)
			throws FileNotFoundException, IOException, HRMSException {
		EmployeeLeaveApplied leavesAppliedEnity;
		logger.info(" == ACTION -> LEAVE_WITHDRAW_APPROVE << ==");
		leavesAppliedEnity = employeeLeaveAppliedDAO.findById(request.getLeaveApplied().getId()).get();
		Employee employee = employeeDAO.findById(leavesAppliedEnity.getEmployee().getId()).get();

		EmployeeLeaveDetail emp = employeeLeaveDetailsDAO.findEmployeeLeaveByEIDYEAR(employee.getId(),
				leavesAppliedEnity.getMasterLeaveType().getId(), HRMSDateUtil.getCurrentYear());
		if (emp == null) {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode,
					IHRMSConstants.YouCannotApplyTheSelectedLeaveMessage);
		}

		// Set<EmployeeLeaveDetail> empDetailsList = employee.getEmployeeLeaveDetails();

		if (leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_WD_PENDING)) {
			float leaveAvaialble = 0;
			if (emp.getLeaveAvailable() != null) {
				leaveAvaialble = emp.getLeaveAvailable();
			}

			float leaveToBeCredited = leavesAppliedEnity.getNoOfDays();

			float leaveAvailed = 0;
			if (emp.getNumberOfDaysAvailed() != null) {
				leaveAvailed = emp.getNumberOfDaysAvailed();
			}

			float leaveAfterCreditedBalance = leaveAvaialble + leaveToBeCredited;
			float availedLeavesAfterWithdrawCredit = leaveAvailed - leaveToBeCredited;

			emp.setNumberOfDaysAvailed(availedLeavesAfterWithdrawCredit);
			emp.setLeaveAvailable(leaveAfterCreditedBalance);
			empLeaveDetailsDAO.save(emp);

			leavesAppliedEnity.setApproverActionDateWithdrawn(new Date());
			leavesAppliedEnity.setApproverCommentOnWithdrawn(request.getLeaveApplied().getApproverCommentOnWithdrawn());
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_WD_APPROVED);
			leavesAppliedEnity.setDateOfApproverAction(new Date());
			leavesAppliedEnity.setUpdatedBy(String.valueOf(
					leavesAppliedEnity.getEmployee().getEmployeeReportingManager().getReporingManager().getId()));
			leavesAppliedEnity.setUpdatedDate(new Date());
			leavesAppliedEnity = employeeLeaveAppliedDAO.save(leavesAppliedEnity);

			// update credit leave if leave type is compoff
			if (leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
					.equalsIgnoreCase(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE)) {
				try {
					List<EmployeeCreditAppliedLeaveMapping> appliedLeaveMappings = getListOfCreditAppliedLeaveMapping(
							leavesAppliedEnity);
					int updateResult = updateLeaveAvailableOnGrantLeaveCancelWithdrawn(appliedLeaveMappings);
				} catch (HRMSException hrmsExc) {
					throw new HRMSException(hrmsExc.getResponseCode(), hrmsExc.getResponseMessage());
				}
			}

			/**
			 * Email Sender
			 */
			String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
			String ccEmailId = leavesAppliedEnity.getCc();
			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_LeaveWithdrawApproved);

			// logger.info("+++++++++++++++++++++++++++ MAIL CONTENT
			// ++++++++++++++++++++++++++++++++");
			// logger.info(mailContent);
			/*
			 * EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
			 * IHRMSConstants.MailSubject_LeaveWithdrawApproved);
			 */

			emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContent,
					IHRMSConstants.MailSubject_LeaveWithdrawApproved,
					leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
							.getId(),
					leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
		} else {
			logger.info(" == ACTION -> INVALID ACTION ==");
			throw new HRMSException(IHRMSConstants.LeaveActionErrorCode, IHRMSConstants.CannotApproveWithdraw);
		}
		return leavesAppliedEnity;
	}

	private EmployeeLeaveApplied leaveRejectAction(VOApplyLeaveRequest request)
			throws FileNotFoundException, IOException, HRMSException {

		EmployeeLeaveApplied leavesAppliedEnity;
		logger.info(" == ACTION -> LEAVE_REJECT << ==");
		leavesAppliedEnity = employeeLeaveAppliedDAO.findById(request.getLeaveApplied().getId()).get();
		if (leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_REJECT);
			leavesAppliedEnity.setDateOfApproverAction(new Date());
			leavesAppliedEnity.setUpdatedBy(request.getUpdatedBy());
			leavesAppliedEnity.setUpdatedDate(new Date());

			EmployeeLeaveDetail emp = empLeaveDetailsDAO.findEmployeeLeaveByEIDYEAR(
					leavesAppliedEnity.getEmployee().getId(), leavesAppliedEnity.getMasterLeaveType().getId(),
					HRMSDateUtil.getCurrentYear());

			float leaveAvaialble = 0;
			if (emp.getLeaveAvailable() != null) {
				leaveAvaialble = emp.getLeaveAvailable();
			}

			float leaveToBeCredited = leavesAppliedEnity.getNoOfDays();

			float leaveAvailed = 0;
			if (emp.getNumberOfDaysAvailed() != null) {
				leaveAvailed = emp.getNumberOfDaysAvailed();
			}

			float leaveAfterCreditedBalance = leaveAvaialble + leaveToBeCredited;
			float availedLeavesAfterWithdrawCredit = leaveAvailed - leaveToBeCredited;

			emp.setNumberOfDaysAvailed(availedLeavesAfterWithdrawCredit);
			emp.setLeaveAvailable(leaveAfterCreditedBalance);
			empLeaveDetailsDAO.save(emp);
			leavesAppliedEnity = employeeLeaveAppliedDAO.save(leavesAppliedEnity);

			// next added by SSW on 16Nov2018 for resetting compoff balance
			// checking if leave type is compoff
			if (leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
					.equalsIgnoreCase(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE)) {
				try {
					List<EmployeeCreditAppliedLeaveMapping> appliedLeaveMappings = getListOfCreditAppliedLeaveMapping(
							leavesAppliedEnity);
					int updateResult = updateLeaveAvailableOnGrantLeaveCancelWithdrawn(appliedLeaveMappings);
				} catch (HRMSException hrmsExc) {
					throw new HRMSException(hrmsExc.getResponseCode(), hrmsExc.getResponseMessage());
				}
			}
			// up to this added by SSW on 16 Nov 2018

			/**
			 * Email Sender
			 */
			String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
			String ccEmailId = leavesAppliedEnity.getCc();
			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_LeaveReject);

			// logger.info("+++++++++++++++++++++++++++ MAIL CONTENT
			// ++++++++++++++++++++++++++++++++");
			// logger.info(mailContent);
			// EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
			// IHRMSConstants.MailSubject_LeaveRejected);
			emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContent,
					IHRMSConstants.MailSubject_LeaveRejected,
					leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
							.getId(),
					leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
		} else {
			logger.info(" == ACTION -> INVALID ACTION ==");
			throw new HRMSException(IHRMSConstants.LeaveActionErrorCode, IHRMSConstants.CannotRejectMessage);
		}
		return leavesAppliedEnity;
	}

	private EmployeeLeaveApplied leaveApproveAction(VOApplyLeaveRequest request)
			throws FileNotFoundException, IOException, HRMSException {
		EmployeeLeaveApplied leavesAppliedEnity;
		logger.info(" == ACTION -> LEAVE_APPROVE << ==");
		leavesAppliedEnity = employeeLeaveAppliedDAO.findById(request.getLeaveApplied().getId()).get();
		if (leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
			Employee employee = employeeDAO.findById(leavesAppliedEnity.getEmployee().getId()).get();

			// String query ="select leaveDetail from EmployeeLeaveDetail leaveDetail where
			// leaveDetail.employee,id = 2 and leaveDetail.masterLeaveType.id = 2 and
			// leaveDetail.year = 0";

			// Commented this code on 06-01-2021 by S.K
			/*
			 * EmployeeLeaveDetail emp =
			 * employeeLeaveDetailsDAO.findEmployeeLeaveByEIDYEAR(employee.getId(),
			 * leavesAppliedEnity.getMasterLeaveType().getId(),
			 * HRMSDateUtil.getCurrentYear()); if (emp == null) { throw new
			 * HRMSException(IHRMSConstants.DataNotFoundCode,
			 * IHRMSConstants.YouCannotApplyTheSelectedLeaveMessage); }
			 */
			// Commented upto here on 06-01-2021 by S.k

			/*
			 * float leaveAvaialble = 0; if (emp.getLeaveAvailable() != null) {
			 * leaveAvaialble = emp.getLeaveAvailable(); }
			 * 
			 * float leaveApplied = leavesAppliedEnity.getNoOfDays();
			 * 
			 * float leavesAvailed = 0; if (emp.getNumberOfDaysAvailed() != null) {
			 * leavesAvailed = emp.getNumberOfDaysAvailed(); }
			 * 
			 * float totalAvailedLeave = leavesAvailed + leaveApplied; float leaveBalance =
			 * leaveAvaialble - leaveApplied;
			 * 
			 * emp.setNumberOfDaysAvailed(totalAvailedLeave);
			 * emp.setLeaveAvailable(leaveBalance); empLeaveDetailsDAO.save(emp);
			 */

			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_APPROVED);
			leavesAppliedEnity.setDateOfApproverAction(new Date());
			leavesAppliedEnity.setUpdatedBy(request.getUpdatedBy());
			leavesAppliedEnity.setUpdatedDate(new Date());
			leavesAppliedEnity = employeeLeaveAppliedDAO.save(leavesAppliedEnity);
			/**
			 * Email Sender
			 */
			String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
			String ccEmailId = leavesAppliedEnity.getCc();
			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_LeaveApproved);

			// logger.info("+++++++++++++++++++++++++++ MAIL CONTENT
			// ++++++++++++++++++++++++++++++++");
			// logger.info(mailContent);
			// EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
			// IHRMSConstants.MailSubject_LeaveApproved);
			emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContent,
					IHRMSConstants.MailSubject_LeaveApproved,
					leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
							.getId(),
					leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());

		} else {
			throw new HRMSException(IHRMSConstants.LeaveActionErrorCode, IHRMSConstants.LeaveNotInPendingErrorMessage);
		}
		return leavesAppliedEnity;
	}

	@PostMapping("/multi")
	@RequestMapping(value = "/multi", method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	private String MultipleleaveApproveAction(@RequestBody VOApplyLeaveRequest[] request) {
		EmployeeLeaveApplied leavesAppliedEnity = new EmployeeLeaveApplied();
		EmployeeLeaveApplied leavesAppliedEnitySaved = new EmployeeLeaveApplied();
		List<Object> appliedLeaveList = new ArrayList<Object>();
		logger.info(" == ACTION -> LEAVE_APPROVE << ==");
		try {
			for (VOApplyLeaveRequest request1 : request) {
				leavesAppliedEnity = employeeLeaveAppliedDAO.findById(request1.getLeaveApplied().getId()).get();
				if ((leavesAppliedEnity).getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
					Employee employee = employeeDAO.findById((leavesAppliedEnity).getEmployee().getId()).get();

					leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_APPROVED);
					leavesAppliedEnity.setDateOfApproverAction(new Date());
					leavesAppliedEnity.setUpdatedBy(request1.getUpdatedBy());
					leavesAppliedEnity.setUpdatedDate(new Date());
					leavesAppliedEnitySaved = employeeLeaveAppliedDAO.save(leavesAppliedEnity);
					appliedLeaveList.add(leavesAppliedEnitySaved);
					/**
					 * Email Sender
					 */
					String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
					String ccEmailId = leavesAppliedEnity.getCc();
					Map<String, String> placeHolderMapping = HRMSRequestTranslator
							.createPlaceHolderMap((EmployeeLeaveApplied) leavesAppliedEnity);
					placeHolderMapping.put("{websiteURL}", baseURL);
					String mailContent = HRMSHelper.replaceString(placeHolderMapping,
							IHRMSEmailTemplateConstants.Template_LeaveApproved);

					emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContent,
							IHRMSConstants.MailSubject_LeaveApproved,
							leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail()
									.getDivision().getId(),
							leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
				}

				else {
					throw new HRMSException(IHRMSConstants.LeaveActionErrorCode,
							IHRMSConstants.LeaveNotInPendingErrorMessage);
				}
			}
			HRMSListResponseObject res = new HRMSListResponseObject();
			res.setResponseCode(IHRMSConstants.successCode);
			res.setResponseMessage(IHRMSConstants.LeaveActionSuccessMessage);
			return HRMSHelper.createJsonString(res);
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

	private EmployeeLeaveApplied leaveWithdrawAction(VOApplyLeaveRequest request)
			throws FileNotFoundException, IOException, HRMSException {
		EmployeeLeaveApplied leavesAppliedEnity;
		logger.info(" == ACTION -> LEAVE_WITHDRAW << ==");
		leavesAppliedEnity = employeeLeaveAppliedDAO.findById(request.getLeaveApplied().getId()).get();

		if (leavesAppliedEnity != null
				&& leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_APPROVED)) {

			leavesAppliedEnity.setUpdatedBy(request.getUpdatedBy());
			leavesAppliedEnity.setUpdatedDate(new Date());
			leavesAppliedEnity.setReasonForWithdrawn(request.getLeaveApplied().getReasonForWithdrawn());
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_WD_PENDING);
			leavesAppliedEnity.setDateOfWithdrawn(new Date());
			leavesAppliedEnity = employeeLeaveAppliedDAO.save(leavesAppliedEnity);

			/**
			 * Email Sender
			 */
			String managerEmailId = leavesAppliedEnity.getEmployee().getEmployeeReportingManager().getReporingManager()
					.getOfficialEmailId();
			String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
			String ccEmailId = leavesAppliedEnity.getCc();

			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{rootIp}", baseURL);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContentForManager = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_WithdrawLeaveRequestToManager);
			String mailContentForEmployee = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_WithdrawLeaveConfirmationToEmployee);
			// logger.info("+++++++++++++++++++++++++++ MAIL CONTENT
			// ++++++++++++++++++++++++++++++++");
			// logger.info(mailContentForManager);

			/*
			 * EmailSender.toSendEmail(managerEmailId, null, mailContentForManager,
			 * IHRMSConstants.MailSubject_LeaveWithdrawRequest);
			 */
			/*
			 * EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContentForEmployee,
			 * IHRMSConstants.MailSubject_LeaveWithdrawRequest);
			 */

			emailsender.toPersistEmail(managerEmailId, null, mailContentForManager,
					IHRMSConstants.MailSubject_LeaveWithdrawRequest,
					leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
							.getId(),
					leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
			emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContentForEmployee,
					IHRMSConstants.MailSubject_LeaveWithdrawRequest,
					leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
							.getId(),
					leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
		} else {
			throw new HRMSException(IHRMSConstants.CannotWithdrawLeaveCode, IHRMSConstants.CannotWithdrawLeaveMessage);
		}
		return leavesAppliedEnity;
	}

	private EmployeeLeaveApplied leaveCancelAction(VOApplyLeaveRequest request)
			throws FileNotFoundException, IOException, HRMSException {
		EmployeeLeaveApplied leavesAppliedEnity;
		logger.info(" == ACTION -> LEAVE_CANCELL << ==");
		leavesAppliedEnity = employeeLeaveAppliedDAO.findById(request.getLeaveApplied().getId()).get();

		if (leavesAppliedEnity != null
				&& leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {

			leavesAppliedEnity.setUpdatedBy(request.getUpdatedBy());
			leavesAppliedEnity.setUpdatedDate(new Date());
			leavesAppliedEnity.setReasonForCancel(request.getLeaveApplied().getReasonForCancel());
			leavesAppliedEnity.setDateOfCancle(new Date());
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_CANCELLED);

			leavesAppliedEnity = employeeLeaveAppliedDAO.save(leavesAppliedEnity);

			EmployeeLeaveDetail emp = empLeaveDetailsDAO.findEmployeeLeaveByEIDYEAR(
					leavesAppliedEnity.getEmployee().getId(), request.getLeaveApplied().getMasterLeaveType().getId(),
					HRMSDateUtil.getCurrentYear());
			if (emp != null) {
				float leaveAvaialble = 0;
				if (emp.getLeaveAvailable() != null) {
					leaveAvaialble = emp.getLeaveAvailable();
				}

				float leaveToBeCredited = leavesAppliedEnity.getNoOfDays();

				float leaveAvailed = 0;
				if (emp.getNumberOfDaysAvailed() != null) {
					leaveAvailed = emp.getNumberOfDaysAvailed();
				}

				float leaveAfterCreditedBalance = leaveAvaialble + leaveToBeCredited;
				float availedLeavesAfterWithdrawCredit = leaveAvailed - leaveToBeCredited;

				emp.setNumberOfDaysAvailed(availedLeavesAfterWithdrawCredit);
				emp.setLeaveAvailable(leaveAfterCreditedBalance);

				employeeLeaveDetailsDAO.save(emp);
			}

			// update credit leave if leave type is compoff
			if (leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
					.equalsIgnoreCase(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE)) {
				try {
					List<EmployeeCreditAppliedLeaveMapping> appliedLeaveMappings = getListOfCreditAppliedLeaveMapping(
							leavesAppliedEnity);
					int updateResult = updateLeaveAvailableOnGrantLeaveCancelWithdrawn(appliedLeaveMappings);
				} catch (HRMSException hrmsExc) {
					throw new HRMSException(hrmsExc.getResponseCode(), hrmsExc.getResponseMessage());
				}
			}

			/**
			 * Email Sender
			 */
			String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
			String managerEmailId = leavesAppliedEnity.getEmployee().getEmployeeReportingManager().getReporingManager()
					.getOfficialEmailId();
			String ccEmailId = leavesAppliedEnity.getCc();

			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String employeeMailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_CancleLeaveForEmployee);
			String managerMailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_CancleLeaveForManager);

			// logger.info("+++++++++++++++++++++++++++ MAIL CONTENT
			// ++++++++++++++++++++++++++++++++");
			// logger.info(employeeMailContent);

			/*
			 * Sending email to manager,employee,and CC
			 */
			/*
			 * EmailSender.toSendEmail(employeeEmailId, null, employeeMailContent,
			 * IHRMSConstants.MailSubject_LeaveCancelled);
			 */
			emailsender.toPersistEmail(employeeEmailId, null, employeeMailContent,
					IHRMSConstants.MailSubject_LeaveCancelled,
					leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
							.getId(),
					leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());

			/*
			 * EmailSender.toSendEmail(managerEmailId, null, managerMailContent,
			 * IHRMSConstants.MailSubject_LeaveCancelled);
			 */

			emailsender.toPersistEmail(managerEmailId, null, managerMailContent,
					IHRMSConstants.MailSubject_LeaveCancelled,
					leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
							.getId(),
					leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());

			// if (ccEmailId != null) {
			if (!HRMSHelper.isNullOrEmpty(ccEmailId)) {
				// EmailSender.toSendEmail(ccEmailId, null, managerMailContent,
				// IHRMSConstants.MailSubject_LeaveCancelled);
				emailsender.toPersistEmail(ccEmailId, null, managerMailContent,
						IHRMSConstants.MailSubject_LeaveCancelled,
						leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
								.getId(),
						leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
			}
		} else {
			throw new HRMSException(IHRMSConstants.ActivityProcessedCode, IHRMSConstants.ErrorInCancellation);
		}
		return leavesAppliedEnity;
	}

	private EmployeeLeaveApplied leaveApplyAction(VOApplyLeaveRequest request)
			throws HRMSException, FileNotFoundException, IOException, DateParseException {

		EmployeeLeaveApplied leavesAppliedEnity;
		logger.info(" == ACTION -> LEAVE_APPLY << ==");
		Employee employee = employeeDAO.findById(request.getLeaveApplied().getEmployee().getId()).get();
		EmployeeLeaveDetail employeeLeaveDetail = null;

		/*
		 * Checking employee
		 */
		if (HRMSHelper.isNullOrEmpty(employee)) {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EmployeeDoesnotExistMessage);
		}

		/*
		 * Checking master leave type
		 */
		MasterLeaveType masterLeaveType = leaveTypeDAO.findById(request.getLeaveApplied().getMasterLeaveType().getId())
				.get();
		if (HRMSHelper.isNullOrEmpty(masterLeaveType)) {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.LeaveTypeDosentExist);
		}

		/*
		 * Checking if the leave applied is LOP or not,if not then will check if the
		 * employee has LOP or not,if not then will create a leave detail for the type
		 * of LOP
		 */
		if (masterLeaveType.getIsLop().equalsIgnoreCase(IHRMSConstants.isActive)) {
			employeeLeaveDetail = empLeaveDetailsDAO.findEmployeeLeaveByEIDYEAR(employee.getId(),
					request.getLeaveApplied().getMasterLeaveType().getId(), HRMSDateUtil.getCurrentYear());
			if (employeeLeaveDetail == null) {
				employeeLeaveDetail = new EmployeeLeaveDetail();
				employeeLeaveDetail.setEmployee(employee);
				employeeLeaveDetail.setMasterLeaveType(masterLeaveType);
				employeeLeaveDetail.setYear(HRMSDateUtil.getCurrentYear());
				employeeLeaveDetail = empLeaveDetailsDAO.save(employeeLeaveDetail);
			} else {

			}
		}

		/*
		 * If the above condition passes successfully then it will check if the other
		 * leave applied has been credited or not
		 */
		employeeLeaveDetail = empLeaveDetailsDAO.findEmployeeLeaveByEIDYEAR(employee.getId(),
				request.getLeaveApplied().getMasterLeaveType().getId(), HRMSDateUtil.getCurrentYear());

		if (employeeLeaveDetail == null) {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode,
					IHRMSConstants.YouCannotApplyTheSelectedLeaveMessage);
		}

		leavesAppliedEnity = HRMSRequestTranslator.translateToApplyLeaveDetailsEntity(request);
		leavesAppliedEnity.setMasterLeaveType(masterLeaveType);
		leavesAppliedEnity.setEmployee(employee);

		float leaveApplied = leavesAppliedEnity.getNoOfDays();
		float leaveAvailable = 0;
		if (employeeLeaveDetail.getLeaveAvailable() != null) {
			leaveAvailable = employeeLeaveDetail.getLeaveAvailable();
		}

		float leavesAvailed = 0;
		if (employeeLeaveDetail.getNumberOfDaysAvailed() != null) {
			leavesAvailed = employeeLeaveDetail.getNumberOfDaysAvailed();
		}

		float totalAvailedLeave = leavesAvailed + leaveApplied;
		float leaveBalance = 0;

		if (leavesAppliedEnity.getMasterLeaveType().getId() != 7
				&& leavesAppliedEnity.getMasterLeaveType().getId() != 12
				&& leavesAppliedEnity.getMasterLeaveType().getId() != 5) {
			leaveBalance = leaveAvailable - leaveApplied;
		}

		if (leaveApplied > leaveAvailable && !masterLeaveType.getIsLop().equalsIgnoreCase("Y")) {
			throw new HRMSException(IHRMSConstants.LEAVE_APPLIED_GREATER_THAN_LEAVE_AVAILABLE_CODE,
					IHRMSConstants.LEAVE_APPLIED_GREATER_THAN_LEAVE_AVAILABLE_MESSAGE);
		}

		/*
		 * if compoff, find list of leaves which are earliest
		 */
		int resultCompOff = 0;
		if (leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
				.equals(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE)) {
			resultCompOff = applyCompOffAction(leavesAppliedEnity);
			if (resultCompOff == 2) {
				throw new HRMSException(IHRMSConstants.COMP_OFF_VALID_LEAVES_UNAVAILABLE_CODE,
						IHRMSConstants.COMP_OFF_VALID_LEAVES_UNAVAILABLE_MESSAGE);
			}
			if (resultCompOff == 0) {
				throw new HRMSException(IHRMSConstants.UnknowErrorCode,
						IHRMSConstants.LEAVE_APPLIED_GREATER_THAN_LEAVE_AVAILABLE_MESSAGE);
			}
		}

		if (request.isAutoApproved()) {
			logger.info(" == >> Leave Auto Approved << == ");
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_AUTO_APPROVED);
			leavesAppliedEnity.setDateOfApproverAction(new Date());
			leavesAppliedEnity.setRemark(IHRMSConstants.LeaveRemarkForAutoApproved);

			employeeLeaveDetail.setNumberOfDaysAvailed(totalAvailedLeave);
			employeeLeaveDetail.setLeaveAvailable(leaveBalance);

			leavesAppliedEnity = employeeLeaveAppliedDAO.save(leavesAppliedEnity);
			empLeaveDetailsDAO.save(employeeLeaveDetail);

			String managerEmailId = employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
			String employeeEmailID = employee.getOfficialEmailId();
			String ccEmailId = employeeEmailID + ";" + request.getLeaveApplied().getCc();
			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{rootIp}", baseURL);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContent_cc = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_LeaveApply_CC);
			if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("On Duty")) {
				emailsender.toPersistEmail(managerEmailId, ccEmailId, mailContent_cc,
						IHRMSConstants.MailSubject_OnDutyApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("Work From Home")) {
				emailsender.toPersistEmail(managerEmailId, ccEmailId, mailContent_cc,
						IHRMSConstants.MailSubject_WorkFromHomeApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else {
				emailsender.toPersistEmail(managerEmailId, ccEmailId, mailContent_cc,
						IHRMSConstants.MailSubject_LeaveApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			}

			/*
			 * EmailSender.toSendEmail(managerEmailId, ccEmailId, mailContent_cc,
			 * IHRMSConstants.MailSubject_LeaveApplication);
			 */
		} else {

			// Now calculation has to be implemented in leave apply

			employeeLeaveDetail.setNumberOfDaysAvailed(totalAvailedLeave);
			employeeLeaveDetail.setLeaveAvailable(leaveBalance);

			leavesAppliedEnity = employeeLeaveAppliedDAO.save(leavesAppliedEnity);
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_PENDING);
			leavesAppliedEnity = employeeLeaveAppliedDAO.save(leavesAppliedEnity);
			empLeaveDetailsDAO.save(employeeLeaveDetail);

			// Email sender

			String managerEmailId = employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
			String ccEmailId = request.getLeaveApplied().getCc();

			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{rootIp}", baseURL);
			placeHolderMapping.put("{websiteURL}", baseURL);

			String mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_LeaveApply);

			// Sending email to Recipient
			if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("On Duty")) {
				emailsender.toPersistEmail(managerEmailId, null, mailContent,
						IHRMSConstants.MailSubject_OnDutyApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("Work From Home")) {
				emailsender.toPersistEmail(managerEmailId, null, mailContent,
						IHRMSConstants.MailSubject_WorkFromHomeApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else {
				emailsender.toPersistEmail(managerEmailId, null, mailContent,
						IHRMSConstants.MailSubject_LeaveApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			}

			// EmailSender.toSendEmail(managerEmailId, null, mailContent,
			// IHRMSConstants.MailSubject_LeaveApplication);

			// Sending email to CC

			String[] res = ccEmailId.split("[;]", 0);
			for (String myStr : res) {

				String mailContent_cc = HRMSHelper.replaceString(placeHolderMapping,
						IHRMSEmailTemplateConstants.Template_LeaveApply_CC);
				if (!HRMSHelper.isNullOrEmpty(myStr) && (!myStr.equals(managerEmailId))) {
					emailsender.toPersistEmail(myStr, null, mailContent_cc, IHRMSConstants.MailSubject_LeaveApplication,
							employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							employee.getCandidate().getLoginEntity().getOrganization().getId());
				}
			}
		}
		return leavesAppliedEnity;
	}

	@RequestMapping(value = "/{empId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getEmployeeLeaveApppliedDetails(@RequestBody @PathVariable("empId") long empId) {

		try {

			logger.info(" == EMPLOYEE APPLIED LEAVE DETAILS  ==");
			Employee employee = employeeDAO.findById(empId).get();
			HRMSListResponseObject response = null;
			if (employee != null) {
				response = new HRMSListResponseObject();
				// following modified by SSW on 04 Dec 2018 for : previous year leaves were
				// visible
				// to requester
				// below line commented
				// Set<EmployeeLeaveApplied> leaveApplied = employee.getEmployeeLeaveApplieds();
				Date date = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int year = calendar.get(Calendar.YEAR);
				List<EmployeeLeaveApplied> leaveApplied = employeeLeaveAppliedDAO
						.getEmployeeAppliedLeaveDetailsYearly(empId, year);
				// upto this modified by SSW on 04 dec
				// List<EmployeeLeaveApplied> leaveApplied=
				// employeeLeaveAppliedDAO.findAllAppliedLeavesByEmployeeInDescOrder(empId);
				if (leaveApplied != null && !leaveApplied.isEmpty()) {
					List<Object> responseList = new ArrayList<Object>();
					for (EmployeeLeaveApplied empLeaveAppliedEntity : leaveApplied) {
						VOEmployeeLeaveApplied model = HRMSEntityToModelMapper
								.convertToEmployeLeaveAppliedModel(empLeaveAppliedEntity);
						responseList.add(model);
					}
					response = new HRMSListResponseObject();
					response.setListResponse(responseList);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(response);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.NoLeavesApplied);
				}
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

	@RequestMapping(value = "manager/{mgrId}/{year}/{empId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getEmployeeLeaveApppliedDetailsToManager(@RequestBody @PathVariable("mgrId") long mgrId,
			@PathVariable("year") int year, @PathVariable("empId") int empId) {

		try {

			logger.info(" == EMPLOYEE APPLIED LEAVE DETAILS TO MANAGER ==");

			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> listResponse = new ArrayList<Object>();
			// next is modified by SSW on 17Dec2018 for : removed page request from next
			// method
			// as manager should view all leave requests of his subordinates
			if (empId == 0) {

				List<EmployeeLeaveApplied> subordinateLeaveAppliedList = employeeLeaveAppliedDAO
						.findSubordinateLeaveApplied(IHRMSConstants.isActive, IHRMSConstants.isActive, mgrId,
								IHRMSConstants.LeaveStatus_PENDING, IHRMSConstants.LeaveStatus_WD_PENDING,
								year/* , pageRequest */);
				for (EmployeeLeaveApplied employeeLeaveApplied : subordinateLeaveAppliedList) {

					VOEmployeeLeaveApplied leaveAppliedModel = HRMSEntityToModelMapper
							.convertToEmployeLeaveAppliedModel(employeeLeaveApplied);
					listResponse.add(leaveAppliedModel);
				}
			} else {

				List<EmployeeLeaveApplied> subordinateLeaveAppliedList = employeeLeaveAppliedDAO
						.findSubordinateLeaveAppliedByEmpId(IHRMSConstants.isActive, IHRMSConstants.isActive, mgrId,
								IHRMSConstants.LeaveStatus_PENDING, IHRMSConstants.LeaveStatus_WD_PENDING, year,
								empId/* , pageRequest */);
				for (EmployeeLeaveApplied employeeLeaveApplied : subordinateLeaveAppliedList) {

					VOEmployeeLeaveApplied leaveAppliedModel = HRMSEntityToModelMapper
							.convertToEmployeLeaveAppliedModel(employeeLeaveApplied);
					listResponse.add(leaveAppliedModel);
				}
			}

			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			response.setListResponse(listResponse);

			return HRMSHelper.createJsonString(response);

		} /*
			 * catch (HRMSException e) { e.printStackTrace(); try { return
			 * HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			 * 
			 * } catch (Exception e1) { e1.printStackTrace(); } }
			 */catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(value = "/calculateLeave", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findLeaveDifference(@RequestBody VOLeaveCalculationRequest voLeaveCalculationRequest) {
		Date fromDate = voLeaveCalculationRequest.getFromDate();
		Date toDate = voLeaveCalculationRequest.getToDate();
		long fSession = HRMSHelper.isNullOrEmpty(voLeaveCalculationRequest.getFromSession()) ? 0
				: Integer.valueOf(voLeaveCalculationRequest.getFromSession()
						.substring(voLeaveCalculationRequest.getFromSession().length() - 1));
		long tSession = HRMSHelper.isNullOrEmpty(voLeaveCalculationRequest.getToSession()) ? 0
				: Integer.valueOf(voLeaveCalculationRequest.getToSession()
						.substring(voLeaveCalculationRequest.getToSession().length() - 1));
		long orgId = voLeaveCalculationRequest.getOrganizationId();
		// long divId = voLeaveCalculationRequest.getDivisionId();
		// long branchId = voLeaveCalculationRequest.getBranchId();

		try {

			if (!HRMSHelper.isNullOrEmpty(voLeaveCalculationRequest)
					&& !HRMSHelper.isNullOrEmpty(voLeaveCalculationRequest.getEmployeeId())) {

				long empId = voLeaveCalculationRequest.getEmployeeId();
				Employee tempEmployee = employeeDAO.findActiveEmployeeWithCandidateByEmpIdAndOrgId(empId,
						IHRMSConstants.isActive,SecurityFilter.TL_CLAIMS.get().getOrgId());

				long divId = tempEmployee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
				long branchId = !HRMSHelper.isNullOrEmpty(
						tempEmployee.getCandidate().getCandidateProfessionalDetail().getWorkingLocation())
								? tempEmployee.getCandidate().getCandidateProfessionalDetail().getWorkingLocation()
										.getId()
								: tempEmployee.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
				long departmentId = tempEmployee.getCandidate().getCandidateProfessionalDetail().getDepartment()
						.getId();
				// long departmentId =
				// employeeDAO.findDepartmentByEmployeeId(empId).getDepartment().getId();

				if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate)
						&& !HRMSHelper.isLongZero(fSession) && !HRMSHelper.isLongZero(tSession)
						&& !HRMSHelper.isNullOrEmpty(orgId) && !HRMSHelper.isNullOrEmpty(divId)
						&& !HRMSHelper.isNullOrEmpty(branchId) && !HRMSHelper.isNullOrEmpty(empId)
						&& !HRMSHelper.isNullOrEmpty(departmentId)) {

					if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate)) {

						long diff = HRMSDateUtil.getDifferenceInDays(fromDate, toDate);
						logger.info("*****Total Day Difference::" + diff);

						if (diff >= 0) {
							double totalLeave = 0;

							if (!HRMSHelper.isNullOrEmpty(voLeaveCalculationRequest.getLeaveTypeCode())) {
								if (voLeaveCalculationRequest.getLeaveTypeCode().trim().equalsIgnoreCase("MATR")) {
									totalLeave = diff;
								} else {
									List<OrganizationWeekoff> weekoffList = organizationWeekoffDAO
											.getWeekoffByOrgBranchDivDeptId(orgId, divId, branchId, departmentId);

									fromDate = HRMSDateUtil.setTimeStampToZero(fromDate);
									toDate = HRMSDateUtil.setTimeStampToZero(toDate);
									List<OrganizationHoliday> holidayList = organizationHolidayDAO
											.getHolidayListByOrgBranchDivId(orgId, divId, branchId, fromDate, toDate);

									totalLeave = HRMSHelper.getWorkingDays(voLeaveCalculationRequest, weekoffList)
											- HRMSHelper.getHolidays(voLeaveCalculationRequest, holidayList,
													weekoffList)
											- (HRMSHelper.convertFromSession(voLeaveCalculationRequest))
											- (HRMSHelper.convertToSession(voLeaveCalculationRequest));
								}

							}

							if (totalLeave > 0) {
								logger.info("*****Total Calculated Leave::" + totalLeave);

								VOAppliedLeaveCount leaveCount = new VOAppliedLeaveCount();
								leaveCount.setCalculatedLeave(totalLeave);
								leaveCount.setResponseCode(IHRMSConstants.successCode);
								leaveCount.setResponseMessage(IHRMSConstants.successMessage);
								return HRMSHelper.createJsonString(leaveCount);

							} else {
								throw new HRMSException(IHRMSConstants.NotValidDateCode,
										IHRMSConstants.NotValidDateMessage);
							}
						} else {
							// wrong date selection
							throw new HRMSException(IHRMSConstants.NotValidDateCode,
									IHRMSConstants.NotValidDateMessage);
						}

					} else {
						throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.NotValidDateMessage);
					}

				} else {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
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

	@RequestMapping(value = "teamAbsenceDetails/{empId}/{fromDate}/{toDate}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getTeamAbsenceDetails(@RequestBody @PathVariable("empId") long empId,
			@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		List<EmployeeLeaveApplied> employeeLeaveApplieds = null;
		List<VOTeamAbsenceDetails> voTeamAbsenceDetails = null;
		List<Object> responseObjectList = null;
		HRMSListResponseObject response = new HRMSListResponseObject();
		try {
			if (!HRMSHelper.isLongZero(empId)) {
				employeeLeaveApplieds = employeeLeaveAppliedDAO.findAllAppliedLeavesByEmployee(empId);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EmployeeDoesnotExistMessage);
			}
			if (!HRMSHelper.isNullOrEmpty(employeeLeaveApplieds)) {
				voTeamAbsenceDetails = new ArrayList<VOTeamAbsenceDetails>();
				responseObjectList = new ArrayList<Object>();
				for (EmployeeLeaveApplied employeeLeaveApplied : employeeLeaveApplieds) {
					// leave type already exist
					if (isLeaveTypeExist(employeeLeaveApplied, voTeamAbsenceDetails)) {
						for (VOTeamAbsenceDetails voTeamAbsenceDetails2 : voTeamAbsenceDetails) {
							if (voTeamAbsenceDetails2.getMasterLeaveType().getId() == employeeLeaveApplied
									.getMasterLeaveType().getId()) {
								voTeamAbsenceDetails2 = updateVOTeamAbsenceDetails(voTeamAbsenceDetails2,
										employeeLeaveApplied);
								break;
							}
						}
					} else { // leave type does not exist
						VOTeamAbsenceDetails newVOTeamAbsenceDetail = new VOTeamAbsenceDetails();
						VOMasterLeaveType newVOMasterLeaveType = new VOMasterLeaveType();
						MasterLeaveType masterLeaveType = masterLeaveTypeDAO
								.findById(employeeLeaveApplied.getMasterLeaveType().getId()).get();
						newVOMasterLeaveType = HRMSResponseTranslator.translateToVOMasterLeaveTypeVO(masterLeaveType);
						newVOTeamAbsenceDetail.setMasterLeaveType(newVOMasterLeaveType);
						newVOTeamAbsenceDetail = updateVOTeamAbsenceDetails(newVOTeamAbsenceDetail,
								employeeLeaveApplied);
						voTeamAbsenceDetails.add(newVOTeamAbsenceDetail);
						responseObjectList.add(newVOTeamAbsenceDetail);
					}
				}
				response.setListResponse(responseObjectList);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(response);
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

	private boolean isLeaveTypeExist(EmployeeLeaveApplied employeeLeaveApplied,
			List<VOTeamAbsenceDetails> voTeamAbsenceDetails) {
		boolean status = false;
		for (VOTeamAbsenceDetails voTeamAbsenceDetails2 : voTeamAbsenceDetails) {
			if (voTeamAbsenceDetails2.getMasterLeaveType().getId() == employeeLeaveApplied.getMasterLeaveType()
					.getId()) {
				status = true;
				break;
			}
		}
		return status;
	}

	private VOTeamAbsenceDetails updateVOTeamAbsenceDetails(VOTeamAbsenceDetails voTeamAbsenceDetails,
			EmployeeLeaveApplied employeeLeaveApplied) {
		if (employeeLeaveApplied.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
			if (HRMSHelper.isFloatZero(voTeamAbsenceDetails.getLeaveApplied())) {
				voTeamAbsenceDetails.setLeaveApplied(employeeLeaveApplied.getNoOfDays());
			} else {
				float existingValue = voTeamAbsenceDetails.getLeaveApplied();
				voTeamAbsenceDetails.setLeaveApplied(existingValue + employeeLeaveApplied.getNoOfDays());
			}
		}
		if (employeeLeaveApplied.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_APPROVED)) {
			if (HRMSHelper.isFloatZero(voTeamAbsenceDetails.getLeaveApproved())) {
				voTeamAbsenceDetails.setLeaveApproved(employeeLeaveApplied.getNoOfDays());
			} else {
				float existingValue = voTeamAbsenceDetails.getLeaveApproved();
				voTeamAbsenceDetails.setLeaveApproved(existingValue + employeeLeaveApplied.getNoOfDays());
			}
		}
		if (employeeLeaveApplied.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_REJECT)) {
			if (HRMSHelper.isFloatZero(voTeamAbsenceDetails.getLeaveUnapproved())) {
				voTeamAbsenceDetails.setLeaveUnapproved(employeeLeaveApplied.getNoOfDays());
			} else {
				float existingValue = voTeamAbsenceDetails.getLeaveUnapproved();
				voTeamAbsenceDetails.setLeaveUnapproved(existingValue + employeeLeaveApplied.getNoOfDays());
			}
		}
		return voTeamAbsenceDetails;
	}

	@RequestMapping(value = "/leaveActionByMail/{leaveAction}/{leaveId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String leaveApplyByMail(@RequestBody @PathVariable("leaveAction") String leaveAction,
			@PathVariable("leaveId") String leaveId) throws IOException, URISyntaxException {
		try {

			logger.info(" == >> Taking action from email << == ");
			if (!HRMSHelper.isNullOrEmpty(leaveAction) && !HRMSHelper.isNullOrEmpty(leaveId)) {

				long id = Long.parseLong(HRMSHelper.decodeString(leaveId));
				VOApplyLeaveRequest leaveRequest = new VOApplyLeaveRequest();
				leaveRequest.setActionPerformed(HRMSHelper.decodeString(leaveAction));
				VOEmployeeLeaveApplied leaveApplied = new VOEmployeeLeaveApplied();
				leaveApplied.setId(id);
				leaveRequest.setLeaveApplied(leaveApplied);
				leaveApplied.setRemark(IHRMSConstants.ByMailerRemark);
				String response = applyLeave(leaveRequest);
				HRMSListResponseObject res = HRMSHelper.getObjectMapper().readValue(response,
						HRMSListResponseObject.class);

				if (res.getResponseCode() == 0) {
					List<Object> listObject = res.getListResponse();

					switch (HRMSHelper.decodeString(leaveAction)) {
					case "APPROVE_LEAVE":
						response = IHRMSEmailTemplateConstants.LEAVE_RESPONSE_VIA_EMAIL_APPROVED;
						break;
					case "REJECT_LEAVE":
						response = IHRMSEmailTemplateConstants.LEAVE_RESPONSE_VIA_EMAIL_REJECTED;
						break;
					case "WD_APPROVE":
						response = IHRMSEmailTemplateConstants.LEAVE_RESPONSE_VIA_EMAIL_WD_APPROVED;
						break;
					case "WD_REJECT":
						response = IHRMSEmailTemplateConstants.LEAVE_RESPONSE_VIA_EMAIL_WD_REJECTED;
						break;
					}
				} else {
					response = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "<body onload=\"myFunction()\">\r\n" + "\r\n"
							+ "<script>\r\n" + "function myFunction() {\r\n" + "    alert('" + res.getResponseMessage()
							+ "');\r\n" + "}\r\n" + "</script>\r\n" + "\r\n" + "</body>\r\n" + "</html>";
				}
				return response;
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

	@RequestMapping(value = "/calculateLeaveGrant", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findLeaveDifferenceLeaveGrant(@RequestBody VOLeaveCalculationRequest voLeaveCalculationRequest) {
		Date fromDate = voLeaveCalculationRequest.getFromDate();
		Date toDate = voLeaveCalculationRequest.getToDate();
		long fSession = HRMSHelper.isNullOrEmpty(voLeaveCalculationRequest.getFromSession()) ? 0
				: Integer.valueOf(voLeaveCalculationRequest.getFromSession()
						.substring(voLeaveCalculationRequest.getFromSession().length() - 1));
		long tSession = HRMSHelper.isNullOrEmpty(voLeaveCalculationRequest.getToSession()) ? 0
				: Integer.valueOf(voLeaveCalculationRequest.getToSession()
						.substring(voLeaveCalculationRequest.getToSession().length() - 1));
		// long orgId = voLeaveCalculationRequest.getOrganizationId();
		// long divId = voLeaveCalculationRequest.getDivisionId();
		// long branchId = voLeaveCalculationRequest.getBranchId();

		try {

			if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate)
					&& !HRMSHelper.isLongZero(fSession) && !HRMSHelper.isLongZero(tSession)) {

				if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate)) {

					long diff = HRMSDateUtil.getDifferenceInDays(fromDate, toDate);
					logger.info("*****Total Day Difference::" + diff);

					if (diff >= 0) {

						// List<OrganizationWeekoff> weekoffList =
						// organizationWeekoffDAO.getWeekoffByOrgBranchDivId(orgId,
						// divId, branchId);
						// List<OrganizationHoliday> holidayList = organizationHolidayDAO
						// .getHolidayListByOrgBranchDivId(orgId, divId, branchId, fromDate, toDate);

						// double totalLeave = HRMSHelper.getWorkingDays(voLeaveCalculationRequest,
						// weekoffList)
						// - HRMSHelper.getHolidays(voLeaveCalculationRequest, holidayList)
						// - (HRMSHelper.convertFromSession(voLeaveCalculationRequest))
						// - (HRMSHelper.convertToSession(voLeaveCalculationRequest));
						double totalLeave = diff + 1 - (HRMSHelper.convertFromSession(voLeaveCalculationRequest))
								- (HRMSHelper.convertToSession(voLeaveCalculationRequest));
						if (totalLeave > 0) {
							logger.info("*****Total Calculated Leave::" + totalLeave);

							VOAppliedLeaveCount leaveCount = new VOAppliedLeaveCount();
							leaveCount.setCalculatedLeave(totalLeave);
							leaveCount.setResponseCode(IHRMSConstants.successCode);
							leaveCount.setResponseMessage(IHRMSConstants.successMessage);
							return HRMSHelper.createJsonString(leaveCount);

						} else {
							throw new HRMSException(IHRMSConstants.NotValidDateCode,
									IHRMSConstants.NotValidDateMessage);
						}
					} else {
						// wrong date selection
						throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.NotValidDateMessage);
					}

				} else {
					throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.NotValidDateMessage);
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

	public boolean validateLeave(VOApplyLeaveRequest requestedLeave) {

		/***
		 * 1. get all applied leaves where from = sysdate - 5 2. create map applied
		 * leaves 3. create key of requested leave 4. if requested key is present then>>
		 * invalid leave else >> valid leave
		 */
		Map<String, String> mapOfAppliedLeaves = new HashMap<String, String>();
		long empId = requestedLeave.getLeaveApplied().getEmployee().getId();

		// Getting all applied leaves(from last 5 days) of employee

		List<EmployeeLeaveApplied> appliedLeaves = employeeLeaveAppliedDAO.findAppliedLeavesOfEmployeeFromLastFiveDays(
				empId, IHRMSConstants.LeaveStatus_CANCELLED, IHRMSConstants.LeaveStatus_WD_APPROVED,
				IHRMSConstants.LeaveStatus_REJECT);
		// logger.info("**********Applied Leaves***********");
		for (EmployeeLeaveApplied employeeLeaveApplied : appliedLeaves) {
			long dayDiff = HRMSDateUtil.getDifferenceInDays(employeeLeaveApplied.getFromDate(),
					employeeLeaveApplied.getToDate());

			long count = 4 * (dayDiff + 1);
			Date fromDate = employeeLeaveApplied.getFromDate();
			Date toDate = employeeLeaveApplied.getToDate();
			String fromSession = employeeLeaveApplied.getFromSession();
			String toSession = employeeLeaveApplied.getToSession();

			if (fromSession.equalsIgnoreCase("Session 4")) {
				count = count - 3;
			} else if (fromSession.equalsIgnoreCase("Session 3")) {
				count = count - 2;
			} else if (fromSession.equalsIgnoreCase("Session 2")) {
				count = count - 1;
			}

			if (toSession.equalsIgnoreCase("Session 1")) {
				count = count - 3;
			} else if (toSession.equalsIgnoreCase("Session 2")) {
				count = count - 2;
			} else if (toSession.equalsIgnoreCase("Session 3")) {
				count = count - 1;
			}
			logger.info("Count" + count);
			int start = 0;
			int end = 0;
			String startStr = fromSession.trim().substring(fromSession.trim().length() - 1);
			start = Integer.parseInt(startStr);
			String endStr = toSession.trim().substring(toSession.trim().length() - 1);
			end = Integer.parseInt(endStr);
			if (fromDate.compareTo(toDate) <= 0) {

				for (int i = 0; i < count; i++) {
					// logger.info("i::"+ i + "Count::"+count);

					if (fromDate.compareTo(toDate) == 0) {
						endStr = toSession.trim().substring(toSession.trim().length() - 1);
						end = Integer.parseInt(endStr);
					} else {
						end = 4;
					}

					for (int k = start; k <= end; k++) {

						String key = HRMSDateUtil.format(fromDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_"
								+ "Session" + String.valueOf(start);
						++start;
						++i;
						logger.info("Key" + key);
						mapOfAppliedLeaves.put(key, "");

					}
					--i;
					start = 1;
					fromDate = HRMSDateUtil.incDate(fromDate, 1);
				}

			}
			count = 0;

		}
		// logger.info("******* Map of Applied Leaves *********");
		/*
		 * Set<String> tempSet = mapOfAppliedLeaves.keySet(); for (String string :
		 * tempSet) { logger.info(string); }
		 */

		// EARLIER IN PLACE OF FRONT_END_DATE_FORMAT IT WAS "yyyy-MM-dd"
		Date reqFromDate = HRMSDateUtil.parse(requestedLeave.getLeaveApplied().getFromDate(),
				IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date reqToDate = HRMSDateUtil.parse(requestedLeave.getLeaveApplied().getToDate(),
				IHRMSConstants.FRONT_END_DATE_FORMAT);
		String reqFromSession = requestedLeave.getLeaveApplied().getFromSession().trim()
				.substring(requestedLeave.getLeaveApplied().getFromSession().trim().length() - 1);
		String reqToSession = requestedLeave.getLeaveApplied().getToSession().trim()
				.substring(requestedLeave.getLeaveApplied().getToSession().trim().length() - 1);

		MasterLeaveType masterLeaveType = masterLeaveTypeDAO
				.findById(requestedLeave.getLeaveApplied().getMasterLeaveType().getId()).get();
		if (masterLeaveType != null && masterLeaveType.getLeaveGrantStatus() != null
				&& masterLeaveType.getLeaveGrantStatus().equalsIgnoreCase("Y")) {

			String fromKey1 = "";
			String fromKey2 = "";
			String toKey1 = "";
			String toKey2 = "";

			if (Integer.parseInt(reqFromSession) == 1) {
				// reqFromSession = String.valueOf(4);
				fromKey1 = HRMSDateUtil.format(reqFromDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
						+ String.valueOf(1);
				fromKey2 = HRMSDateUtil.format(reqFromDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
						+ String.valueOf(2);
			} else {
				fromKey1 = HRMSDateUtil.format(reqFromDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
						+ String.valueOf(3);
				fromKey2 = HRMSDateUtil.format(reqFromDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
						+ String.valueOf(4);
			}

			if (Integer.parseInt(reqToSession) == 1) {
				// reqToSession = String.valueOf(4);
				toKey1 = HRMSDateUtil.format(reqToDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
						+ String.valueOf(1);
				toKey2 = HRMSDateUtil.format(reqToDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
						+ String.valueOf(2);
			} else {
				toKey1 = HRMSDateUtil.format(reqToDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
						+ String.valueOf(3);
				toKey2 = HRMSDateUtil.format(reqToDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
						+ String.valueOf(4);
			}

			if (mapOfAppliedLeaves.containsKey(fromKey1) || mapOfAppliedLeaves.containsKey(fromKey2)
					|| mapOfAppliedLeaves.containsKey(toKey1) || mapOfAppliedLeaves.containsKey(toKey2))
				return false;
			else
				return true;
		} else {

			String fromKey = HRMSDateUtil.format(reqFromDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
					+ String.valueOf(Integer.parseInt(reqFromSession));
			String toKey = HRMSDateUtil.format(reqToDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
					+ String.valueOf(Integer.parseInt(reqToSession));
			if (mapOfAppliedLeaves.containsKey(fromKey) || mapOfAppliedLeaves.containsKey(toKey))
				return false;
			else
				return true;
		}
	}

	/**
	 * To Get Leave Applied Details for the current year
	 */
	@RequestMapping(value = "/leaveAppliedByLeaveType/{employeeId}/{leaveTypeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getLeaveAppliedDetailsByLeaveType(@PathVariable("employeeId") long employeeId,
			@PathVariable("leaveTypeId") long leaveTypeId) {

		try {

			Employee employee = employeeDAO.findById(employeeId).get();
			MasterLeaveType masterLeaveType = masterLeaveTypeDAO.findById(leaveTypeId).get();

			if (employee != null) {

				if (masterLeaveType != null) {

					List<EmployeeLeaveApplied> employeeLeaveApplied = employeeLeaveAppliedDAO
							.findByemployeeAndmasterLeaveType(employee.getId(), masterLeaveType.getId());
					HRMSListResponseObject response = new HRMSListResponseObject();
					List<Object> listObject = new ArrayList<Object>();
					for (EmployeeLeaveApplied leaveApliedEntity : employeeLeaveApplied) {

						VOEmployeeLeaveApplied model = HRMSEntityToModelMapper
								.convertToEmployeLeaveAppliedModel(leaveApliedEntity);
						listObject.add(model);
					}
					response.setListResponse(listObject);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(response);
				}

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

	private int applyCompOffAction(EmployeeLeaveApplied leavesAppliedEnity) throws HRMSException {

		int result = 0;
		// split applied leave days into single
		// for employee find list of compoff from credit leave

		try {
			Map<Long, Map<Integer, EmployeeCreditLeaveDetail>> creditLeaveDetailMap = getEmployeeCompoffListFromCreditLeave(
					leavesAppliedEnity.getEmployee());
			List<EmployeeLeaveApplied> employeeLeaveAppliedSplitted = splitEmployeeAppliedLeaves(leavesAppliedEnity);
			int creditLeaveDeductedForVerify = 0;
			List<EmployeeCreditLeaveDetail> deducedEmpCreditLeaveDetailList = new ArrayList<EmployeeCreditLeaveDetail>();

			// logger.info("List of Employee leave applied :: ");
			for (EmployeeLeaveApplied employeeLeaveApplied : employeeLeaveAppliedSplitted) {
				// logger.info("Leave Applied From Date : " +
				// employeeLeaveApplied.getFromDate());
				// logger.info("Leave Applied From Session : " +
				// employeeLeaveApplied.getFromSession());
				// logger.info("Leave Applied To Date : " + employeeLeaveApplied.getToDate());
				// logger.info("Leave Applied To Session : " +
				// employeeLeaveApplied.getToSession());
				// iterating entity wise map
				Iterator<Long> mapCredLeaveEntityWiseKeyItr = creditLeaveDetailMap.keySet().iterator();
				Boolean sessionWiseCreditLeaveRemove = false;
				while (mapCredLeaveEntityWiseKeyItr.hasNext()) {
					Map<Integer, EmployeeCreditLeaveDetail> mapCredLeaveEntityWise = creditLeaveDetailMap
							.get(mapCredLeaveEntityWiseKeyItr.next());
					// iterating session wise map
					Iterator<Integer> mapCredLeaveSessionWiseItr = mapCredLeaveEntityWise.keySet().iterator();
					while (mapCredLeaveSessionWiseItr.hasNext()) {
						EmployeeCreditLeaveDetail credLeaveDetailsSessionWise = mapCredLeaveEntityWise
								.get(mapCredLeaveSessionWiseItr.next());
						// if leave applied is before or on expiry date of credit leave
						if (employeeLeaveApplied.getFromDate().before(credLeaveDetailsSessionWise.getToDate())
								|| employeeLeaveApplied.getFromDate().equals(credLeaveDetailsSessionWise.getToDate())) {

							sessionWiseCreditLeaveRemove = true;
							// logger.info("leave deduced ::: " + " Credit Leave Id : "
							// + credLeaveDetailsSessionWise.getId() + " :: Credit Leave From Date : "
							// + credLeaveDetailsSessionWise.getToDate() + " :: Applied Leave From Date : "
							// + employeeLeaveApplied.getFromDate() + " :: Applied Leave From Session : "
							// + employeeLeaveApplied.getFromSession());

							mapCredLeaveSessionWiseItr.remove();
							creditLeaveDeductedForVerify++;
							deducedEmpCreditLeaveDetailList.add(credLeaveDetailsSessionWise);
							break;
						}
					}
					if (sessionWiseCreditLeaveRemove) {
						break;
					}
				}
			}

			// updating credit leaves
			if (creditLeaveDeductedForVerify == employeeLeaveAppliedSplitted.size()) {
				// logger.info("Employee can apply comp off");
				// get creditLeaveId from map which are removed above
				// it means that leaves are availed.
				// so subtract 0.5 from available-balance of the credit leave
				for (EmployeeCreditLeaveDetail employeeCreditLeaveDetail : deducedEmpCreditLeaveDetailList) {
					if (!HRMSHelper.isNullOrEmpty(employeeCreditLeaveDetail)
							&& !HRMSHelper.isLongZero(employeeCreditLeaveDetail.getId())) {
						EmployeeCreditLeaveDetail empCreditLeaveDetailEntity = creditLeaveDAO
								.findById(employeeCreditLeaveDetail.getId()).get();
						float updatedLeaveAvailable = empCreditLeaveDetailEntity.getLeaveAvailable()
								- IHRMSConstants.COMP_OFF_SESSION_VALUE;
						empCreditLeaveDetailEntity.setLeaveAvailable(updatedLeaveAvailable);
						empCreditLeaveDetailEntity.setUpdatedBy(leavesAppliedEnity.getEmployee().getId().toString());
						empCreditLeaveDetailEntity.setUpdatedDate(new Date());
						creditLeaveDAO.save(empCreditLeaveDetailEntity);
						// add mapping of credit leave and leave applied
						EmployeeCreditAppliedLeaveMapping creditAppliedLeaveMapping = new EmployeeCreditAppliedLeaveMapping();
						creditAppliedLeaveMapping.setEmployeeLeaveApplied(leavesAppliedEnity);
						creditAppliedLeaveMapping.setCreditLeaveDetail(empCreditLeaveDetailEntity);
						creditAppliedLeaveMapping.setCreatedBy(leavesAppliedEnity.getEmployee().getId().toString());
						creditAppliedLeaveMapping.setCreatedDate(new Date());
						creditAppliedLeaveMapping.setIsActive(IHRMSConstants.isActive);
						creditAppliedLeaveMappingDAO.save(creditAppliedLeaveMapping);
					}
				}
				result = 1;
			} else {
				// logger.info("Noooo.. Employee cannot apply comp off");
				return 2;
			}
		} catch (HRMSException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param employee
	 * @return List<EmployeeCreditLeaveDetail>
	 * 
	 *         Fetches list of Compoff list from Credit Leave order by from_date
	 *         desc
	 * @throws HRMSException
	 * 
	 */
	private Map<Long, Map<Integer, EmployeeCreditLeaveDetail>> getEmployeeCompoffListFromCreditLeave(Employee employee)
			throws HRMSException {
		List<Object> creditLeaveDetails = null;
		List<EmployeeCreditLeaveDetail> creditLeaveDetails2 = null;
		List<EmployeeCreditLeaveDetail> creditLeaveDetailsForMap = null;
		Map<Long, Map<Integer, EmployeeCreditLeaveDetail>> mapCreditLeaveEntityWise = null;
		if (!HRMSHelper.isNullOrEmpty(employee) && !HRMSHelper.isLongZero(employee.getId())) {
			creditLeaveDetails = new ArrayList<Object>();
			creditLeaveDetails2 = new ArrayList<EmployeeCreditLeaveDetail>();
			creditLeaveDetails = (List<Object>) creditLeaveDAO.findEmployeeCompoffListFromCreditLeave(
					IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE, IHRMSConstants.isActive, IHRMSConstants.isActive,
					employee.getId());

			// if to_date is not null, then its entry is entered by HR if to_date is null,
			// then its entry if from approved grant leave

			if (!HRMSHelper.isNullOrEmpty(creditLeaveDetails)) {
				for (Iterator<Object> it = creditLeaveDetails.listIterator(); it.hasNext();) {
					EmployeeCreditLeaveDetail employeeCreditLeaveDetail = new EmployeeCreditLeaveDetail();
					Object[] creditLeaveDetailInLoop = (Object[]) it.next();

					// above object obj[0] = fromDate, obj[1] = toDate, obj[2] = leaveAvailable,
					// obj[3] = leaveExpiry, obj[4] = creditLeaveId

					employeeCreditLeaveDetail.setFromDate(HRMSDateUtil.parse(creditLeaveDetailInLoop[0].toString(),
							IHRMSConstants.POSTGRE_DATE_FORMAT));
					employeeCreditLeaveDetail
							.setLeaveAvailable(Float.parseFloat(creditLeaveDetailInLoop[2].toString()));

					// employeeCreditLeaveDetail.getMasterLeaveType()
					// .setLeaveExpiry(Integer.parseInt(creditLeaveDetailInLoop[3].toString()));

					employeeCreditLeaveDetail.setId(Long.parseLong(creditLeaveDetailInLoop[4].toString()));

					if (!HRMSHelper.isNullOrEmpty(creditLeaveDetailInLoop)) {
						if (HRMSHelper.isNullOrEmpty(String.valueOf(creditLeaveDetailInLoop[1]))) {
							// in calendar instance, setting from date And adding expiry days from master to
							// it to set in to_date
							Calendar calToDate = Calendar.getInstance();
							Calendar calToDateNew = Calendar.getInstance();
							calToDate.setTime(HRMSDateUtil.parse(creditLeaveDetailInLoop[0].toString(),
									IHRMSConstants.POSTGRE_DATE_FORMAT));
							calToDate.add(Calendar.DATE, Integer.parseInt(creditLeaveDetailInLoop[3].toString()));
							creditLeaveDetailInLoop[1] = calToDate.getTime();
							employeeCreditLeaveDetail.setToDate(HRMSDateUtil
									.parse(creditLeaveDetailInLoop[1].toString(), "EE MMM dd HH:mm:ss z yyyy"));
						} else {
							// this entry of compoff is added by HR Hence the to_date will be considered
							// from DB which is entred by HR
							employeeCreditLeaveDetail.setToDate(HRMSDateUtil
									.parse(creditLeaveDetailInLoop[1].toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
						}
					}
					creditLeaveDetails2.add(employeeCreditLeaveDetail);
				}
				Collections.sort(creditLeaveDetails2, new Comparator<EmployeeCreditLeaveDetail>() {
					public int compare(EmployeeCreditLeaveDetail o1, EmployeeCreditLeaveDetail o2) {
						return o1.getToDate().compareTo(o2.getToDate());
					}
				});

				// shallow copy CreditLeaveDetail List for Map
				creditLeaveDetailsForMap = new ArrayList<EmployeeCreditLeaveDetail>();
				for (EmployeeCreditLeaveDetail employeeCreditLeaveDetail2 : creditLeaveDetails2) {
					EmployeeCreditLeaveDetail employeeCreditLeaveDetailCopyToMap = new EmployeeCreditLeaveDetail();
					employeeCreditLeaveDetailCopyToMap = copyCreditLeaveObj(employeeCreditLeaveDetail2);
					creditLeaveDetailsForMap.add(employeeCreditLeaveDetailCopyToMap);
				}
				// need to create map with <CreditLeaveDetail(Long), noOfDay(Float)>
				mapCreditLeaveEntityWise = new LinkedHashMap<Long, Map<Integer, EmployeeCreditLeaveDetail>>();
				for (EmployeeCreditLeaveDetail employeeCreditLeaveDetailInMapLoop : creditLeaveDetailsForMap) {
					Map<Integer, EmployeeCreditLeaveDetail> mapCreditLeaveSessionWise = new LinkedHashMap<Integer, EmployeeCreditLeaveDetail>();
					Integer index = 0;
					while (employeeCreditLeaveDetailInMapLoop.getLeaveAvailable() > 0) {
						EmployeeCreditLeaveDetail leaveDetail = new EmployeeCreditLeaveDetail();
						leaveDetail = copyCreditLeaveObj(leaveDetail);
						leaveDetail.setLeaveAvailable(IHRMSConstants.COMP_OFF_SESSION_VALUE);
						leaveDetail.setId(employeeCreditLeaveDetailInMapLoop.getId());
						leaveDetail.setFromDate(employeeCreditLeaveDetailInMapLoop.getFromDate());
						leaveDetail.setToDate(employeeCreditLeaveDetailInMapLoop.getToDate());
						mapCreditLeaveSessionWise.put(index, leaveDetail);
						employeeCreditLeaveDetailInMapLoop
								.setLeaveAvailable(employeeCreditLeaveDetailInMapLoop.getLeaveAvailable()
										- IHRMSConstants.COMP_OFF_SESSION_VALUE);
						index++;
					}
					mapCreditLeaveEntityWise.put(employeeCreditLeaveDetailInMapLoop.getId(), mapCreditLeaveSessionWise);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode,
						IHRMSConstants.LEAVE_APPLIED_GREATER_THAN_LEAVE_AVAILABLE_MESSAGE);
			}
		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EmployeeDoesnotExistMessage);
		}
		return mapCreditLeaveEntityWise;
	}

	private List<EmployeeLeaveApplied> splitEmployeeAppliedLeaves(EmployeeLeaveApplied leavesAppliedEnity)
			throws HRMSException {
		List<EmployeeLeaveApplied> employeeLeaveApplieds = null;
		if (!HRMSHelper.isNullOrEmpty(leavesAppliedEnity.getFromDate())
				&& !HRMSHelper.isNullOrEmpty(leavesAppliedEnity.getToDate())) {
			Date fromDate = leavesAppliedEnity.getFromDate();
			Date toDate = leavesAppliedEnity.getToDate();
			Calendar calFromDate = Calendar.getInstance();
			Calendar calFromDateLooping = Calendar.getInstance();
			Calendar calToDate = Calendar.getInstance();
			calFromDate.setTime(fromDate);
			calToDate.setTime(toDate);
			calFromDateLooping.setTime(fromDate);
			Calendar calToDatePlusOne = Calendar.getInstance();
			calToDatePlusOne.setTime(toDate);
			calToDatePlusOne.add(Calendar.DATE, 1);
			employeeLeaveApplieds = new ArrayList<EmployeeLeaveApplied>();

			// next variable used for splitting two sessions of same date
			Calendar calFromDateLoopingSameDate = Calendar.getInstance();
			// calendar from date is less than to date
			Employee emp = employeeDAO.findById(leavesAppliedEnity.getEmployee().getId()).get();
			long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();

			if (calFromDate.before(calToDate)) {
				Boolean isNewDay = true;
				calFromDateLoopingSameDate = calFromDateLooping;

				while (calFromDateLooping.before(calToDatePlusOne)) {
					// checking if current day is week of or holiday
					if (!"WD".equalsIgnoreCase(employeeLeaveDetailsService.isWeeklyOffOrHoliday(
							HRMSDateUtil.format(calFromDateLooping.getTime(), IHRMSConstants.FRONT_END_DATE_FORMAT),
							orgId, emp.getId()))) {
						calFromDateLooping.add(Calendar.DATE, 1);
						continue;
					}
					// creating separate employee leave applied entities day wise
					EmployeeLeaveApplied employeeLeaveApplied = new EmployeeLeaveApplied();
					employeeLeaveApplied.setEmployee(leavesAppliedEnity.getEmployee());
					employeeLeaveApplied.setFromDate(calFromDateLooping.getTime());

					// if loopingFromDate equals "FromDate of leave applied" Set : From Session =
					// "FromSession of leave applied" else ::: looping date is greater than from
					// date Set : From Session = "Session 1" or session 2
					if (calFromDateLooping.equals(calFromDate) && isNewDay) {
						employeeLeaveApplied.setFromSession(leavesAppliedEnity.getFromSession());
					} else if (calFromDateLooping.equals(calFromDate) && !isNewDay) {
						employeeLeaveApplied.setFromSession(IHRMSConstants.COMP_OFF_SESSION_2);
					} else {
						if (isNewDay) {
							employeeLeaveApplied.setFromSession(IHRMSConstants.COMP_OFF_SESSION_1);
						} else {
							employeeLeaveApplied.setFromSession(IHRMSConstants.COMP_OFF_SESSION_2);
						}
					}
					// setting toDate of leave applied to looping date because need to create
					// entries for single day

					employeeLeaveApplied.setToDate(calFromDateLooping.getTime());
					// if loopingFromDate equals "ToDate of leave applied" Set : To Session =
					// "toSession of leave applied" else ::: looping date is smaller than to date
					// Set : From Session = "Session 2"

					if (calFromDateLooping.equals(calToDate) && isNewDay) {
						employeeLeaveApplied.setToSession(IHRMSConstants.COMP_OFF_SESSION_1);
					} else if (calFromDateLooping.equals(calToDate) && !isNewDay) {
						employeeLeaveApplied.setToSession(IHRMSConstants.COMP_OFF_SESSION_2);
					} else if (calFromDateLooping.equals(calFromDate)) {
						employeeLeaveApplied.setToSession(leavesAppliedEnity.getFromSession());
					} else {
						if (isNewDay) {
							employeeLeaveApplied.setToSession(IHRMSConstants.COMP_OFF_SESSION_1);
						} else {
							employeeLeaveApplied.setToSession(IHRMSConstants.COMP_OFF_SESSION_2);
						}
					}
					employeeLeaveApplied.setLeaveStatus(leavesAppliedEnity.getLeaveStatus());
					employeeLeaveApplied.setMasterLeaveType(leavesAppliedEnity.getMasterLeaveType());
					employeeLeaveApplieds.add(employeeLeaveApplied);

					if (employeeLeaveApplied.getFromSession().equals(IHRMSConstants.COMP_OFF_SESSION_2)
							|| (calFromDateLooping.equals(calToDate)
									&& employeeLeaveApplied.getToSession().equals(leavesAppliedEnity.getToSession()))) {
						isNewDay = true;
					} else {
						isNewDay = false;
					}
					// incrementing from date
					if (isNewDay) {
						calFromDateLooping.add(Calendar.DATE, 1);
					}
				}
			} else {
				// calendar fromDate equals toDate
				EmployeeLeaveApplied employeeLeaveApplied = new EmployeeLeaveApplied();
				employeeLeaveApplied.setEmployee(leavesAppliedEnity.getEmployee());
				employeeLeaveApplied.setFromDate(leavesAppliedEnity.getFromDate());
				employeeLeaveApplied.setToDate(leavesAppliedEnity.getToDate());
				// only one session of one day
				if (leavesAppliedEnity.getFromSession().equals(leavesAppliedEnity.getToSession())) {
					employeeLeaveApplied.setFromSession(leavesAppliedEnity.getFromSession());
					employeeLeaveApplied.setToSession(leavesAppliedEnity.getToSession());
					employeeLeaveApplieds.add(employeeLeaveApplied);
				} else {
					// two session on a day
					employeeLeaveApplied.setFromSession(leavesAppliedEnity.getFromSession());
					employeeLeaveApplied.setToSession(leavesAppliedEnity.getFromSession());
					employeeLeaveApplieds.add(employeeLeaveApplied);

					EmployeeLeaveApplied employeeLeaveApplied2 = new EmployeeLeaveApplied();
					employeeLeaveApplied2.setEmployee(leavesAppliedEnity.getEmployee());
					employeeLeaveApplied2.setFromDate(leavesAppliedEnity.getFromDate());
					employeeLeaveApplied2.setToDate(leavesAppliedEnity.getToDate());
					employeeLeaveApplied2.setFromSession(leavesAppliedEnity.getToSession());
					employeeLeaveApplied2.setToSession(leavesAppliedEnity.getToSession());
					employeeLeaveApplieds.add(employeeLeaveApplied2);
				}
			}
		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EmployeeDoesnotExistMessage);
		}
		return employeeLeaveApplieds;
	}

	private EmployeeCreditLeaveDetail copyCreditLeaveObj(EmployeeCreditLeaveDetail employeeCreditLeaveDetail) {
		EmployeeCreditLeaveDetail employeeCreditLeaveDetailCopy = new EmployeeCreditLeaveDetail();
		employeeCreditLeaveDetailCopy.setId(employeeCreditLeaveDetail.getId());
		employeeCreditLeaveDetailCopy.setLeaveAvailable(employeeCreditLeaveDetail.getLeaveAvailable());
		employeeCreditLeaveDetailCopy.setFromDate(employeeCreditLeaveDetail.getFromDate());
		employeeCreditLeaveDetailCopy.setToDate(employeeCreditLeaveDetail.getToDate());
		return employeeCreditLeaveDetailCopy;
	}

	private List<EmployeeCreditAppliedLeaveMapping> getListOfCreditAppliedLeaveMapping(
			EmployeeLeaveApplied employeeLeaveApplied) throws HRMSException {
		// finding leaveApplyAction and creditLeaveDetailsMapping
		List<EmployeeCreditAppliedLeaveMapping> employeeCreditAppliedLeaveMappings = null;
		if (!HRMSHelper.isNullOrEmpty(employeeLeaveApplied) && !HRMSHelper.isLongZero(employeeLeaveApplied.getId())) {
			employeeCreditAppliedLeaveMappings = creditAppliedLeaveMappingDAO
					.getCandidateWithChecklistDetails(employeeLeaveApplied.getId(), IHRMSConstants.isActive);
		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
		}
		return employeeCreditAppliedLeaveMappings;
	}

	private int updateLeaveAvailableOnGrantLeaveCancelWithdrawn(
			List<EmployeeCreditAppliedLeaveMapping> employeeCreditAppliedLeaveMappings) throws HRMSException {
		int updateResult = 0;
		if (!HRMSHelper.isNullOrEmpty(employeeCreditAppliedLeaveMappings)) {
			for (EmployeeCreditAppliedLeaveMapping employeeCreditAppliedLeaveMapping : employeeCreditAppliedLeaveMappings) {
				if (!HRMSHelper.isNullOrEmpty(employeeCreditAppliedLeaveMapping)
						&& !HRMSHelper.isLongZero(employeeCreditAppliedLeaveMapping.getCreditLeaveDetail().getId())
						&& !HRMSHelper.isLongZero(employeeCreditAppliedLeaveMapping.getId())) {
					// adding comp off session value in credit leave which is canceled or withdrawn
					EmployeeCreditLeaveDetail creditLeaveDetail = creditLeaveDAO
							.findById(employeeCreditAppliedLeaveMapping.getCreditLeaveDetail().getId()).get();
					creditLeaveDetail.setLeaveAvailable(
							creditLeaveDetail.getLeaveAvailable() + IHRMSConstants.COMP_OFF_SESSION_VALUE);
					creditLeaveDAO.save(creditLeaveDetail);
					// inactivating entry in applyCreditLeaveMapp table
					employeeCreditAppliedLeaveMapping.setIsActive(IHRMSConstants.isNotActive);
					creditAppliedLeaveMappingDAO.save(employeeCreditAppliedLeaveMapping);
					updateResult = 1;
				}
			}
		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
		}
		return updateResult;
	}

	/**
	 * @author SSW
	 * @param empId
	 * @param year
	 * @return
	 * 
	 *         Get employee wise and year wise leave details
	 */
	@RequestMapping(value = "/getEmpAndYearWiseAppliedLeaveDetails/{empId}/{year}"
			+ "", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getEmployeeLeaveApppliedDetailsYearwise(@RequestBody @PathVariable("empId") long empId,
			@PathVariable("year") int year) {

		try {
			Employee employee = employeeDAO.findById(empId).get();
			HRMSListResponseObject response = null;
			if (!HRMSHelper.isNullOrEmpty(employee) && !HRMSHelper.isLongZero(year)) {
				response = new HRMSListResponseObject();
				List<EmployeeLeaveApplied> leaveApplied = employeeLeaveAppliedDAO
						.getEmployeeAppliedLeaveDetailsYearly(empId, year);
				if (leaveApplied != null && !leaveApplied.isEmpty()) {
					List<Object> responseList = new ArrayList<Object>();
					for (EmployeeLeaveApplied empLeaveAppliedEntity : leaveApplied) {
						VOEmployeeLeaveApplied model = HRMSEntityToModelMapper
								.convertToEmployeLeaveAppliedModel(empLeaveAppliedEntity);
						responseList.add(model);
					}
					response = new HRMSListResponseObject();
					response.setListResponse(responseList);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(response);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.NoLeavesApplied);
				}
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

	/*
	 * Akanksha Gaikwad
	 * 
	 * flush compoff available leaves after 90 days
	 */
	public void restrictCompLeaves() {

		List<EmployeeCreditLeaveDetail> empCreditLeaveDetailEntity = creditLeaveDAO.findAvailableComp(0, 2, "Y");

		for (int i = 0; i < empCreditLeaveDetailEntity.size(); i++) {

			try {

				Date fromDate = empCreditLeaveDetailEntity.get(i).getPostedDate();

				Date today = new Date();

				long days = getDateDiff(fromDate, today, TimeUnit.DAYS);

				float availableLeave = empCreditLeaveDetailEntity.get(i).getLeaveAvailable();

				EmployeeCreditLeaveDetail empLeav = creditLeaveDAO.findById(empCreditLeaveDetailEntity.get(i).getId())
						.get();

				if (days == 91 && availableLeave == 1) {

					float updatedLeaveAvailable = availableLeave - IHRMSConstants.COMP_OFF_SESSION_VALUE_1;

					empCreditLeaveDetailEntity.get(i).setId(empCreditLeaveDetailEntity.get(i).getId());
					empCreditLeaveDetailEntity.get(i).setLeaveAvailable(updatedLeaveAvailable);

					empCreditLeaveDetailEntity.get(i).setUpdatedDate(new Date());

					creditLeaveDAO.save(empCreditLeaveDetailEntity.get(i));

					List<EmployeeLeaveDetail> empLeav2 = employeeLeaveDetailsDAO
							.findEmployeeLeaveByEmployeeAndTypeLeave1(
									empCreditLeaveDetailEntity.get(i).getEmployee().getId(),
									empCreditLeaveDetailEntity.get(i).getMasterLeaveType().getId(), 0);

					if (empLeav2.size() != 0) {

						for (int x = 0; x == 0; x++) {

							empLeav2.get(0).getEmployee().getId();

							float leaveBalance = empLeav2.get(0).getLeaveAvailable();

							float updatedLeaveBalance = leaveBalance - IHRMSConstants.COMP_OFF_SESSION_VALUE_1;

							empLeav2.get(0).setLeaveAvailable(updatedLeaveBalance);

							employeeLeaveDetailsDAO.save(empLeav2.get(0));

						}

					}

				} else if (days == 91 && availableLeave == 2) {

					float updatedLeaveAvailable = availableLeave - IHRMSConstants.COMP_OFF_SESSION_VALUE_2;

					empCreditLeaveDetailEntity.get(i).setId(empCreditLeaveDetailEntity.get(i).getId());
					empCreditLeaveDetailEntity.get(i).setLeaveAvailable(updatedLeaveAvailable);

					empCreditLeaveDetailEntity.get(i).setUpdatedDate(new Date());

					creditLeaveDAO.save(empCreditLeaveDetailEntity.get(i));

					List<EmployeeLeaveDetail> empLeav2 = employeeLeaveDetailsDAO
							.findEmployeeLeaveByEmployeeAndTypeLeave1(
									empCreditLeaveDetailEntity.get(i).getEmployee().getId(),
									empCreditLeaveDetailEntity.get(i).getMasterLeaveType().getId(), 0);

					if (empLeav2.size() != 0) {

						for (int x = 0; x == 0; x++) {

							empLeav2.get(0).getEmployee().getId();

							float leaveBalance = empLeav2.get(0).getLeaveAvailable();

							float updatedLeaveBalance = leaveBalance - IHRMSConstants.COMP_OFF_SESSION_VALUE_2;

							empLeav2.get(0).setLeaveAvailable(updatedLeaveBalance);

							employeeLeaveDetailsDAO.save(empLeav2.get(0));

						}

					}

				} else if (days == 91 && availableLeave == 0.5) {

					float updatedLeaveAvailable = availableLeave - IHRMSConstants.COMP_OFF_SESSION_VALUE;

					empCreditLeaveDetailEntity.get(i).setId(empCreditLeaveDetailEntity.get(i).getId());
					empCreditLeaveDetailEntity.get(i).setLeaveAvailable(updatedLeaveAvailable);

					empCreditLeaveDetailEntity.get(i).setUpdatedDate(new Date());

					creditLeaveDAO.save(empCreditLeaveDetailEntity.get(i));
					List<EmployeeLeaveDetail> empLeav2 = employeeLeaveDetailsDAO
							.findEmployeeLeaveByEmployeeAndTypeLeave1(
									empCreditLeaveDetailEntity.get(i).getEmployee().getId(),
									empCreditLeaveDetailEntity.get(i).getMasterLeaveType().getId(), 0);

					if (empLeav2.size() != 0) {

						for (int x = 0; x == 0; x++) {
							empLeav2.get(0).getEmployee().getId();
							float leaveBalance = empLeav2.get(0).getLeaveAvailable();
							float updatedLeaveBalance = leaveBalance - IHRMSConstants.COMP_OFF_SESSION_VALUE;
							empLeav2.get(0).setLeaveAvailable(updatedLeaveBalance);
							employeeLeaveDetailsDAO.save(empLeav2.get(0));
						}
					}
				}

				else if (days == 91 && availableLeave == 1.5) {

					float updatedLeaveAvailable = availableLeave - IHRMSConstants.COMP_OFF_SESSION_VALUE_3;

					empCreditLeaveDetailEntity.get(i).setId(empCreditLeaveDetailEntity.get(i).getId());
					empCreditLeaveDetailEntity.get(i).setLeaveAvailable(updatedLeaveAvailable);

					empCreditLeaveDetailEntity.get(i).setUpdatedDate(new Date());

					creditLeaveDAO.save(empCreditLeaveDetailEntity.get(i));
					List<EmployeeLeaveDetail> empLeav2 = employeeLeaveDetailsDAO
							.findEmployeeLeaveByEmployeeAndTypeLeave1(
									empCreditLeaveDetailEntity.get(i).getEmployee().getId(),
									empCreditLeaveDetailEntity.get(i).getMasterLeaveType().getId(), 0);

					if (empLeav2.size() != 0) {

						for (int x = 0; x == 0; x++) {

							empLeav2.get(0).getEmployee().getId();

							float leaveBalance = empLeav2.get(0).getLeaveAvailable();

							float updatedLeaveBalance = leaveBalance - IHRMSConstants.COMP_OFF_SESSION_VALUE_3;

							empLeav2.get(0).setLeaveAvailable(updatedLeaveBalance);

							employeeLeaveDetailsDAO.save(empLeav2.get(0));

						}

					}

				}
			} catch (Exception e) {
				logger.error("Error :: ", e);
			}

		}

	}

	public static long getDateDiff(final Date date1, final Date date2, final TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

}
