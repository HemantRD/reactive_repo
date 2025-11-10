package com.vinsys.hrms.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmpYearlyLeaveCreditedDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveType;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeMapping;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.dao.IHRMSReminderMailDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.LeaveDetailsResponse;
import com.vinsys.hrms.datamodel.VOEmployeeLeaveDetail;
import com.vinsys.hrms.datamodel.VOLeaveCalculationRequest;
import com.vinsys.hrms.entity.EmpYearlyLeaveCreditedStatus;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.MapReminderMailCCEmployee;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.exception.HRMSException;
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
@RequestMapping(path = "/leaveDetails")

public class EmployeeLeaveDetailsService {

	private static final Logger log = LoggerFactory.getLogger(EmployeeLeaveDetailsService.class);

	@Autowired
	IHRMSOrganizationHolidayDAO holidayDAO;
	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO configDAO;
	@Autowired
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;

	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	IHRMSEmployeeReportingManager reportingManagerDAO;

	@Autowired
	IHRMSMasterLeaveType leaveTypeDAO;

	@Autowired
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;

	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailDAO;

	@Autowired
	IHRMSReminderMailDAO reminderMailDAO;

	@Autowired
	IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO;

	@Autowired
	IHRMSMasterLeaveTypeDAO masterLeaveTypeDAO;

	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@Autowired
	IHRMSEmployeeCreditLeaveDAO employeeCreditLeaveDAO;

	@Autowired
	IHRMSMasterLeaveTypeMapping masterLeaveTypeMappingDAO;

	@Autowired
	IHRMSEmpYearlyLeaveCreditedDAO empYearlyLeaveCreditedDAO;

	@Value("${LEAVE_APPLIED_REMINDER_NO_OF_DAYS}")
	private int leaveReminderNoOfDays;

	@Autowired
	EmailSender emailsender;
	@Value("${base.url}")
	private String baseURL;

	@RequestMapping(value = "/{empId}/{year}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getEmployeeLeaveSummary(@RequestBody @PathVariable("empId") long empId,
			@PathVariable("year") int year) {

		try {

			Employee employee = null;
			List<Object> leaveDetailsResponses = new ArrayList<>();
			LeaveDetailsResponse leaveDetails = new LeaveDetailsResponse();

			log.info(" == >> FINDING EMPLOYEE LEAVE SUMMARY DETAILS << ==");

			// employee = employeeDAO.findById(empId);

			employee = new Employee();

			if (!HRMSHelper.isLongZero(year)) {
				// employeeLeaveDetail.setYear(year);
			} else {
				throw new HRMSException(IHRMSConstants.YearNotValidCode, IHRMSConstants.YearNotValidMessage);
			}

			// employee = employeeDAO.findById(Example.of(employee));
			employee = employeeDAO.getEmployeeLeaveDetailByEmpIdAndYear(empId, year, IHRMSConstants.isActive);

			if (HRMSHelper.isNullOrEmpty(employee)) {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
			leaveDetails = new LeaveDetailsResponse();
			List<VOEmployeeLeaveDetail> emplLeaveDtlList = new ArrayList<VOEmployeeLeaveDetail>();
			Set<EmployeeLeaveDetail> employeeLeaveDetailsSet = employee.getEmployeeLeaveDetails();

			for (EmployeeLeaveDetail employeeLeaveDetailsEntity : employeeLeaveDetailsSet) {
				VOEmployeeLeaveDetail model = HRMSEntityToModelMapper
						.convertToEmployeeLeaveModel(employeeLeaveDetailsEntity);

				long countLeaveApplied = employeeLeaveAppliedDAO.findCountOfAppliedLeaveDetails(employee.getId(),
						employeeLeaveDetailsEntity.getMasterLeaveType().getId(), IHRMSConstants.LeaveStatus_PENDING,
						year);
				log.info("Leave Type : " + employeeLeaveDetailsEntity.getMasterLeaveType().getLeaveTypeName());
				log.info("Leave Type ID : " + employeeLeaveDetailsEntity.getMasterLeaveType().getId());
				log.info("For Employee ID : " + employee.getId());
				log.info("For Year : " + year);
				log.info("Count : " + countLeaveApplied);
				model.setLeaveApplied(countLeaveApplied);

				emplLeaveDtlList.add(model);
			}
			String employeeFullName = "";
			employeeFullName = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getMiddleName()
					+ " " + employee.getCandidate().getLastName();

			leaveDetails.setId(employee.getId());
			leaveDetails.setName(employeeFullName);
			leaveDetails.setEmployeeLeaveDetail(emplLeaveDtlList);
			leaveDetailsResponses.add(leaveDetails);

			HRMSListResponseObject response = new HRMSListResponseObject();
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			response.setListResponse(leaveDetailsResponses);
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

	@RequestMapping(value = "/dashboardLeaveSummary/{empId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getEmployeeLeaveSummaryForDashboard(@RequestBody @PathVariable("empId") long empId) {

		try {

			Employee employee = null;
			List<Object> leaveDetailsResponses = new ArrayList<>();
			LeaveDetailsResponse leaveDetails = new LeaveDetailsResponse();

			log.info(" == >> FINDING EMPLOYEE LEAVE SUMMARY DETAILS << ==");

			// employee = employeeDAO.findById(empId);

			employee = new Employee();

			int year = HRMSDateUtil.getCurrentYear();
			if (!HRMSHelper.isLongZero(year)) {
				// employeeLeaveDetail.setYear(year);
			} else {
				throw new HRMSException(IHRMSConstants.YearNotValidCode, IHRMSConstants.YearNotValidMessage);
			}

			// employee = employeeDAO.findById(Example.of(employee));
			employee = employeeDAO.getEmployeeLeaveDetailByEmpIdAndYear(empId, year, IHRMSConstants.isActive);

			if (HRMSHelper.isNullOrEmpty(employee)) {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
			leaveDetails = new LeaveDetailsResponse();
			List<VOEmployeeLeaveDetail> emplLeaveDtlList = new ArrayList<VOEmployeeLeaveDetail>();
			Set<EmployeeLeaveDetail> employeeLeaveDetailsSet = employee.getEmployeeLeaveDetails();

			for (EmployeeLeaveDetail employeeLeaveDetailsEntity : employeeLeaveDetailsSet) {
				VOEmployeeLeaveDetail model = HRMSEntityToModelMapper
						.convertToEmployeeLeaveModel(employeeLeaveDetailsEntity);

				long countLeaveApplied = employeeLeaveAppliedDAO.findCountOfAppliedLeaveDetails(employee.getId(),
						employeeLeaveDetailsEntity.getMasterLeaveType().getId(), IHRMSConstants.LeaveStatus_PENDING,
						year);
				log.info("Leave Type : " + employeeLeaveDetailsEntity.getMasterLeaveType().getLeaveTypeName());
				log.info("Leave Type ID : " + employeeLeaveDetailsEntity.getMasterLeaveType().getId());
				log.info("For Employee ID : " + employee.getId());
				log.info("For Year : " + year);
				log.info("Count : " + countLeaveApplied);
				model.setLeaveApplied(countLeaveApplied);

				emplLeaveDtlList.add(model);
			}
			String employeeFullName = "";
			employeeFullName = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getMiddleName()
					+ " " + employee.getCandidate().getLastName();

			leaveDetails.setId(employee.getId());
			leaveDetails.setName(employeeFullName);
			leaveDetails.setEmployeeLeaveDetail(emplLeaveDtlList);
			leaveDetailsResponses.add(leaveDetails);

			HRMSListResponseObject response = new HRMSListResponseObject();
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			response.setListResponse(leaveDetailsResponses);
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

	@RequestMapping(method = RequestMethod.GET, value = "leaveAvailable/{empId}/{leaveTypeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getLeaveAvailableByEmployeeAndLeaveType(@PathVariable("empId") long empId,
			@PathVariable("leaveTypeId") long leaveTypeId) {

		log.info(">>>>>>>>>>>>inside getLeaveAvailableByEmployeeAndLeaveType()<<<<<<<<<<<<<<<<<");
		List<EmployeeLeaveDetail> employeeLeaveDetailList = new ArrayList<>();
		List<Object> voEmployeeLeaveDetailList = new ArrayList<>();
		try {

			if (!HRMSHelper.isLongZero(empId) && !HRMSHelper.isLongZero(leaveTypeId)) {

				// next modified by SSW on 05 Dec 2018 for : fetching leave details yearwise
				Date date = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int year = calendar.get(Calendar.YEAR);
				// upto this added and next DAO call modified, year parameter added
				employeeLeaveDetailList = employeeLeaveDetailDAO.findLeaveAvailableByEmployeeAndLeaveType(empId,
						leaveTypeId, IHRMSConstants.isActive, year);
				if (!HRMSHelper.isNullOrEmpty(employeeLeaveDetailList)) {
					voEmployeeLeaveDetailList = HRMSResponseTranslator
							.translateToEmployeeLeaveDetailVoForAvailableLeave(employeeLeaveDetailList);

					HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
					hrmsListResponseObject.setListResponse(voEmployeeLeaveDetailList);
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

	@RequestMapping(method = RequestMethod.GET, value = "isLeavePresent/{empId}/{leaveDate}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getLeavePresent(@PathVariable("empId") long empId, @PathVariable("leaveDate") String leaveDate) {

		log.info(">>>>>>>>>>>>inside getLeavePresent()<<<<<<<<<<<<<<<<<");
		try {

			if (!HRMSHelper.isNullOrEmpty(empId)) {
				boolean leaveFlag = false;
				int leaveCount = getSessionOfLeaveDate(empId, HRMSDateUtil.parse(leaveDate, "dd-MM-yyyy"));

				// HRMSResponseTranslator.leave

				String dayStatus = isWeeklyOffOrHoliday(leaveDate, 1, 1200);
				List<Object> listResponse = new ArrayList<>();
				listResponse.add(leaveCount);
				listResponse.add(dayStatus);
				HRMSListResponseObject response = new HRMSListResponseObject();
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				response.setListResponse(listResponse);
				return HRMSHelper.createJsonString(response);

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

	public int getSessionOfLeaveDate(long empId, Date leaveDate) throws HRMSException {

		int noOfSession = 0;

		if (!HRMSHelper.isNullOrEmpty(leaveDate) && !HRMSHelper.isLongZero(empId)) {

			// Date leaveDate = HRMSDateUtil.parse(date,
			// IHRMSConstants.FRONT_END_DATE_FORMAT);
			leaveDate = HRMSDateUtil.setTimeStampToZero(leaveDate);

			// List<EmployeeLeaveApplied> appliedLeaves =
			// employeeLeaveAppliedDAO.findAllAppliedLeavesByEmployee(empId);
			List<EmployeeLeaveApplied> appliedLeaves = employeeLeaveAppliedDAO.findAppliedLeavesForDate(empId,
					leaveDate, IHRMSConstants.LeaveStatus_APPROVED);
			if (!HRMSHelper.isNullOrEmpty(appliedLeaves)) {

				for (EmployeeLeaveApplied employeeLeaveApplied : appliedLeaves) {

					Map<String, EmployeeLeaveApplied> empLeaveMap = new HashMap<>();

					if (employeeLeaveApplied.getFromDate().compareTo(leaveDate)
							* leaveDate.compareTo((employeeLeaveApplied.getToDate())) >= 0) {

						Calendar cal1 = Calendar.getInstance();
						Calendar cal2 = Calendar.getInstance();
						cal1.setTime(employeeLeaveApplied.getFromDate());
						cal2.setTime(employeeLeaveApplied.getToDate());
						cal2.add(Calendar.DATE, 1);

						String reqFromSession = employeeLeaveApplied.getFromSession().trim()
								.substring(employeeLeaveApplied.getFromSession().trim().length() - 1);
						String reqToSession = employeeLeaveApplied.getToSession().trim()
								.substring(employeeLeaveApplied.getToSession().trim().length() - 1);

						int fromSession = Integer.parseInt(reqFromSession);
						int toSession = Integer.parseInt(reqToSession);
						int totalNoOfSessions = employeeLeaveApplied.getMasterLeaveType().getNumberOfSession();

						while (cal1.before(cal2)) {

							String key = "";

							if (employeeLeaveApplied.getToDate().compareTo(cal1.getTime()) == 0) {
								for (int i = fromSession; i <= toSession; i++) {
									key = HRMSDateUtil.format(cal1.getTime(), IHRMSConstants.FRONT_END_DATE_FORMAT)
											+ "_Session_" + String.valueOf(i);
									empLeaveMap.put(key, employeeLeaveApplied);
								}
							} else {
								for (int i = fromSession; i <= totalNoOfSessions; i++) {
									key = HRMSDateUtil.format(cal1.getTime(), IHRMSConstants.FRONT_END_DATE_FORMAT)
											+ "_Session_" + String.valueOf(i);
									empLeaveMap.put(key, employeeLeaveApplied);
								}
							}

							fromSession = 1;

							cal1.add(Calendar.DATE, 1);
						}

						for (String key : empLeaveMap.keySet()) {
							// log.info("Applied leave Key :: " + key);
							if (key.contains(HRMSDateUtil.format(leaveDate, IHRMSConstants.FRONT_END_DATE_FORMAT))) {
								noOfSession += 1;
							}
						}

						if (employeeLeaveApplied.getMasterLeaveType().getId() == 2
								|| employeeLeaveApplied.getMasterLeaveType().getId() == 12) {
							noOfSession *= 2;
						}
					}
				}
			}
		}
		return noOfSession;
	}

	public String isWeeklyOffOrHoliday(String date, long orgId, long empId) throws HRMSException {

		if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(empId)) {

			Date leaveDate = HRMSDateUtil.parse(date, IHRMSConstants.FRONT_END_DATE_FORMAT);
			leaveDate = HRMSDateUtil.setTimeStampToZero(leaveDate);
			Employee employee = employeeDAO.findByOrgId(orgId, empId);

			if (!HRMSHelper.isNullOrEmpty(employee)) {

				long divId = employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
				long departId = employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
				long branchId = employee.getCandidate().getCandidateProfessionalDetail().getBranch().getId();

				VOLeaveCalculationRequest request = new VOLeaveCalculationRequest();

				request.setFromDate(leaveDate);
				request.setToDate(leaveDate);

				List<OrganizationWeekoff> weekoffList = organizationWeekoffDAO.getWeekoffByOrgBranchDivDeptId(orgId,
						divId, branchId, departId);

				int dayCount = HRMSHelper.getWorkingDays(request, weekoffList);

				if (dayCount == 0) {
					return "WO";
				} else {
					OrganizationHoliday holiday = holidayDAO.getHoliday(orgId, divId, branchId, leaveDate);

					if (!HRMSHelper.isNullOrEmpty(holiday)) {
						return "H";
					} else {
						return "WD";
					}
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}

		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}
	}

	public void leavereminder() {
		try {
			List<Object[]> result = employeeLeaveAppliedDAO.findOrgDivForLeaveReminder(leaveReminderNoOfDays);
			if (result != null && !result.isEmpty()) {
				for (Object[] resultSet : result) {
					long orgId = Long.parseLong(String.valueOf(resultSet[0]));
					long divisionId = Long.parseLong(String.valueOf(resultSet[1]));
					// findAndSendEmailToRoForLeaveAction(divisionId, orgId);
				}
			} else {
				log.info("No Leave Reminder for RO");
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	public void findAndSendEmailToRoForLeaveAction() {

		try {
			Year currentYear = Year.now();
			int year = currentYear.getValue();
			List<Object[]> reportingManagerEmployeeList = employeeLeaveAppliedDAO
					.findReportingManagerToseNdMailYearWise(leaveReminderNoOfDays, IHRMSConstants.isActive,
							IHRMSConstants.isActive, IHRMSConstants.LeaveStatus_PENDING,
							IHRMSConstants.LeaveStatus_WD_PENDING, year);

			if (reportingManagerEmployeeList != null && !reportingManagerEmployeeList.isEmpty()) {
				for (Object[] resultSet : reportingManagerEmployeeList) {

					long employeeId = Long.parseLong(String.valueOf(resultSet[1]));

					Employee ROemployeeEntity = employeeDAO.findById(employeeId).get();
					String employeeEmailID = ROemployeeEntity.getOfficialEmailId();

					List<Employee> empList = new ArrayList<>();
					List<Object[]> probationaryEmployeeList = employeeLeaveAppliedDAO
							.findEmployeeAccordingToReportingManagerForLeaveReminderYearWise(leaveReminderNoOfDays,
									IHRMSConstants.isActive, IHRMSConstants.isActive,
									IHRMSConstants.LeaveStatus_PENDING, IHRMSConstants.LeaveStatus_WD_PENDING,
									ROemployeeEntity.getId(), year);

					for (Object[] resultSetEmployee : probationaryEmployeeList) {

						long empId = Long.parseLong(String.valueOf(resultSetEmployee[2]));

						Employee employeeEntity = employeeDAO.findById(empId).get();
						if (!employeeService.hasEmployeeResigned(employeeEntity)) {
							empList.add(employeeEntity);
						}

					}

					if (!HRMSHelper.isNullOrEmpty(empList)) {

						String ccEmailId = null;
						StringBuffer ccEmailIdSB = null;
						List<MapReminderMailCCEmployee> reminderMailCCEmpList = reminderMailDAO
								.findReminderMailCCByOrgDiv(
										IHRMSConstants.REMINDER_MAIL_TYPE_MANAGER_ACTION_PENDING_LEAVE,
										ROemployeeEntity.getCandidate().getLoginEntity().getOrganization().getId(),
										ROemployeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision()
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
						Map<String, String> placeHolderMapping = HRMSRequestTranslator
								.createPlaceHolderMapForLeaveReminder(ROemployeeEntity);
						placeHolderMapping.put("{websiteURL}", baseURL);

						String mailContent_cc = HRMSHelper.replaceString(placeHolderMapping,
								IHRMSEmailTemplateConstants.Template_Leave_Reminder);
						emailsender.toPersistEmail(employeeEmailID, ccEmailId, mailContent_cc,
								IHRMSConstants.MailSubject_LeaveApplication_Reminder,
								ROemployeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
								ROemployeeEntity.getCandidate().getLoginEntity().getOrganization().getId());

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "testLeave", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String testCredLeave() {
		creditEmpYearlyLeavesService(1L, 2019, 9.0f, 21.0f);
		return "a";
	}

	public String creditEmpYearlyLeavesService(long orgId, int year, float sickLeaveCount, float plLeaveCount) {
		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				Organization org = organizationDAO.findById(orgId).get();
				if (!HRMSHelper.isNullOrEmpty(org)) {
					// setting type of confirmed employment status code
					List<String> employmentTypeNameList = new ArrayList<String>();
					employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_CONFIRMED);
					employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_EMPLOYEE);
					employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_PERMANENT);
					List<MasterEmploymentType> masterEmploymentTypeList = masterEmploymentTypeDAO
							.findByIsActiveAndEmploymentTypeDescriptionIn(IHRMSConstants.isActive,
									employmentTypeNameList);
					// finding list of employees who are confirmed
					List<Employee> confirmedEmpListDOJMoreThanOneYear = employeeDAO
							.findEmpListByEmployementStatusAndDOJMoreThanOneYear(IHRMSConstants.isActive,
									IHRMSConstants.isActive, masterEmploymentTypeList);
					List<Employee> confirmedEmpListDOJLessThanOneYear = employeeDAO
							.findEmpListByEmployementStatusAndDOJLessThanOneYear(IHRMSConstants.isActive,
									IHRMSConstants.isActive, masterEmploymentTypeList);
					// adding new leaves in employee leave detail

					// get details of leave type
					MasterLeaveType mstLeaveTypeSick = masterLeaveTypeDAO.findByIsActiveAndOrganizationAndLeaveTypeCode(
							IHRMSConstants.isActive, org, IHRMSConstants.LEAVE_TYPE_CODE_SICK);
					MasterLeaveType mstLeaveTypePrivilage = masterLeaveTypeDAO
							.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive, org,
									IHRMSConstants.LEAVE_TYPE_CODE_PRIVILAGE);
					Calendar calFromDate = Calendar.getInstance();
					calFromDate.set(year, 0, 01);
					Calendar calToDate = Calendar.getInstance();
					calToDate.set(year, 11, 31);

					// loopEmpMOY :: looped employee more than one year
					for (Employee loopEmpMOY : confirmedEmpListDOJMoreThanOneYear) {
						// checking for each employee if the leave detail entry (row) for
						// current year is present
						// SICK LEAVE
						EmployeeLeaveDetail currentYearLeaveDetailSick = employeeLeaveDetailDAO
								.findEmployeeLeaveByEIDYEAR(loopEmpMOY.getId(), mstLeaveTypeSick.getId(), year);
						if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailSick)) {
							EmployeeLeaveDetail curYearLeaveDetSick = new EmployeeLeaveDetail();
							curYearLeaveDetSick.setCreatedBy("Admin");
							curYearLeaveDetSick.setCreatedDate(new Date());
							curYearLeaveDetSick.setLeaveAvailable(sickLeaveCount);
							curYearLeaveDetSick.setLeaveEarned(sickLeaveCount);
							curYearLeaveDetSick.setMasterLeaveType(mstLeaveTypeSick);
							curYearLeaveDetSick.setTotalEligibility(sickLeaveCount);
							curYearLeaveDetSick.setYear(year);
							curYearLeaveDetSick.setEmployee(loopEmpMOY);
							employeeLeaveDetailDAO.save(curYearLeaveDetSick);

							// entry in credit leave
							EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
							empCreditLeaveDtlEntity.setCreditedBy("System New year Scheduler");
							empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
							empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
							empCreditLeaveDtlEntity.setNoOfDays(sickLeaveCount);
							empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
							empCreditLeaveDtlEntity.setPostedDate(new Date());
							empCreditLeaveDtlEntity.setEmployee(loopEmpMOY);
							empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveTypeSick);
							empCreditLeaveDtlEntity.setCreatedBy("System New year Scheduler");
							empCreditLeaveDtlEntity.setCreatedDate(new Date());
							employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);

						} else {
							log.info("*** EmployeeLeaveDetails ::: creditEmpYearlyLeavesService() ::: "
									+ "Confirmed More Than One Year ::: EmpId :: " + loopEmpMOY.getId() + " ::: "
									+ " Leave Type :: " + mstLeaveTypeSick.getLeaveTypeName() + " ::: " + "year :: "
									+ year);
						}

						// checking for each employee if the leave detail entry (row) for
						// current year is present
						// PRIVILAGE LEAVE
						EmployeeLeaveDetail currentYearLeaveDetailPrivilage = employeeLeaveDetailDAO
								.findEmployeeLeaveByEIDYEAR(loopEmpMOY.getId(), mstLeaveTypePrivilage.getId(), year);
						if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailPrivilage)) {
							EmployeeLeaveDetail curYearLeaveDetPrivl = new EmployeeLeaveDetail();
							curYearLeaveDetPrivl.setCreatedBy("Admin");
							curYearLeaveDetPrivl.setCreatedDate(new Date());
							curYearLeaveDetPrivl.setLeaveAvailable(plLeaveCount);
							curYearLeaveDetPrivl.setLeaveEarned(plLeaveCount);
							curYearLeaveDetPrivl.setMasterLeaveType(mstLeaveTypePrivilage);
							curYearLeaveDetPrivl.setTotalEligibility(plLeaveCount);
							curYearLeaveDetPrivl.setYear(year);
							curYearLeaveDetPrivl.setEmployee(loopEmpMOY);
							employeeLeaveDetailDAO.save(curYearLeaveDetPrivl);

							// entry in credit leave
							EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
							empCreditLeaveDtlEntity.setCreditedBy("System New year Scheduler");
							empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
							empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
							empCreditLeaveDtlEntity.setNoOfDays(plLeaveCount);
							empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
							empCreditLeaveDtlEntity.setPostedDate(new Date());
							empCreditLeaveDtlEntity.setEmployee(loopEmpMOY);
							empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveTypePrivilage);
							empCreditLeaveDtlEntity.setCreatedBy("System New year Scheduler");
							empCreditLeaveDtlEntity.setCreatedDate(new Date());
							employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
						} else {
							log.info("*** EmployeeLeaveDetails ::: creditEmpYearlyLeavesService() ::: "
									+ "Confirmed More Than One Year ::: EmpId :: " + loopEmpMOY.getId() + " ::: "
									+ " Leave Type :: " + mstLeaveTypePrivilage.getLeaveTypeName() + " ::: "
									+ "year :: " + year);
						}
					}

					// addin sick leave for confirmed emps whose less than one year
					for (Employee loopEmpLOY : confirmedEmpListDOJLessThanOneYear) {
						// checking for each employee if the leave detail entry (row) for
						// current year is present
						// SICK LEAVE
						EmployeeLeaveDetail currentYearLeaveDetailSick = employeeLeaveDetailDAO
								.findEmployeeLeaveByEIDYEAR(loopEmpLOY.getId(), mstLeaveTypeSick.getId(), year);
						if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailSick)) {
							EmployeeLeaveDetail curYearLeaveDetSick = new EmployeeLeaveDetail();
							curYearLeaveDetSick.setCreatedBy("Admin");
							curYearLeaveDetSick.setCreatedDate(new Date());
							curYearLeaveDetSick.setLeaveAvailable(sickLeaveCount);
							curYearLeaveDetSick.setLeaveEarned(sickLeaveCount);
							curYearLeaveDetSick.setMasterLeaveType(mstLeaveTypeSick);
							curYearLeaveDetSick.setTotalEligibility(sickLeaveCount);
							curYearLeaveDetSick.setYear(year);
							curYearLeaveDetSick.setEmployee(loopEmpLOY);
							employeeLeaveDetailDAO.save(curYearLeaveDetSick);

							// entry in credit leave
							EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
							empCreditLeaveDtlEntity.setCreditedBy("Admin");
							empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
							empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
							empCreditLeaveDtlEntity.setNoOfDays(sickLeaveCount);
							empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
							empCreditLeaveDtlEntity.setPostedDate(new Date());
							empCreditLeaveDtlEntity.setEmployee(loopEmpLOY);
							empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveTypeSick);
							empCreditLeaveDtlEntity.setCreatedBy("Admin");
							empCreditLeaveDtlEntity.setCreatedDate(new Date());
							employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
						} else {
							log.info("*** EmployeeLeaveDetails ::: creditEmpYearlyLeavesService() ::: "
									+ "Confirmed Less Than One Year ::: EmpId :: " + loopEmpLOY.getId() + " ::: "
									+ " Leave Type :: " + mstLeaveTypeSick.getLeaveTypeName() + " ::: " + "year :: "
									+ year);
						}
					}

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

	@RequestMapping(method = RequestMethod.GET, value = "testLeaveForDubai", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String testCredLeaveForDubai() {
		creditEmpYearlyLeavesForDubaiService(1L, 2020, 9.0f, 21.0f, 3);
		return "a";
	}

	public String creditEmpYearlyLeavesForDubaiService(long orgId, int year, float sickLeaveCount, float plLeaveCount,
			long divId) {
		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				Organization org = organizationDAO.findById(orgId).get();
				if (!HRMSHelper.isNullOrEmpty(org)) {
					// setting type of confirmed employment status code
					List<String> employmentTypeNameList = new ArrayList<String>();
					employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_CONFIRMED);
					employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_EMPLOYEE);
					employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_PERMANENT);
					List<MasterEmploymentType> masterEmploymentTypeList = masterEmploymentTypeDAO
							.findByIsActiveAndEmploymentTypeDescriptionIn(IHRMSConstants.isActive,
									employmentTypeNameList);
					// finding list of employees who are confirmed
					List<Employee> confirmedEmpListDOJMoreThanOneYear = employeeDAO
							.findEmpListByEmployementStatusDOJAndDivMoreThanOneYear(IHRMSConstants.isActive,
									IHRMSConstants.isActive, masterEmploymentTypeList, divId);
					List<Employee> confirmedEmpListDOJLessThanOneYear = employeeDAO
							.findEmpListByEmployementStatusDOJAndDivIdLessThanOneYear(IHRMSConstants.isActive,
									IHRMSConstants.isActive, masterEmploymentTypeList, divId);
					// adding new leaves in employee leave detail

					// get details of leave type
					MasterLeaveType mstLeaveTypeSick = masterLeaveTypeDAO.findByIsActiveAndOrganizationAndLeaveTypeCode(
							IHRMSConstants.isActive, org, IHRMSConstants.LEAVE_TYPE_CODE_SICK);
					MasterLeaveType mstLeaveTypePrivilage = masterLeaveTypeDAO
							.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive, org,
									IHRMSConstants.LEAVE_TYPE_CODE_PRIVILAGE);
					Calendar calFromDate = Calendar.getInstance();
					calFromDate.set(year, 0, 01);
					Calendar calToDate = Calendar.getInstance();
					calToDate.set(year, 11, 31);

					// loopEmpMOY :: looped employee more than one year
					for (Employee loopEmpMOY : confirmedEmpListDOJMoreThanOneYear) {
						// checking for each employee if the leave detail entry (row) for
						// current year is present
						// SICK LEAVE
						EmployeeLeaveDetail currentYearLeaveDetailSick = employeeLeaveDetailDAO
								.findEmployeeLeaveByEIDYEAR(loopEmpMOY.getId(), mstLeaveTypeSick.getId(), year);
						if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailSick)) {
							EmployeeLeaveDetail curYearLeaveDetSick = new EmployeeLeaveDetail();
							curYearLeaveDetSick.setCreatedBy("System New year Scheduler");
							curYearLeaveDetSick.setCreatedDate(new Date());
							curYearLeaveDetSick.setLeaveAvailable(sickLeaveCount);
							curYearLeaveDetSick.setLeaveEarned(sickLeaveCount);
							curYearLeaveDetSick.setMasterLeaveType(mstLeaveTypeSick);
							curYearLeaveDetSick.setTotalEligibility(sickLeaveCount);
							curYearLeaveDetSick.setYear(year);
							curYearLeaveDetSick.setEmployee(loopEmpMOY);
							employeeLeaveDetailDAO.save(curYearLeaveDetSick);

							// entry in credit leave
							EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
							empCreditLeaveDtlEntity.setCreditedBy("System New year Scheduler");
							empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
							empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
							empCreditLeaveDtlEntity.setNoOfDays(sickLeaveCount);
							empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
							empCreditLeaveDtlEntity.setPostedDate(new Date());
							empCreditLeaveDtlEntity.setEmployee(loopEmpMOY);
							empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveTypeSick);
							empCreditLeaveDtlEntity.setCreatedBy("System New year Scheduler");
							empCreditLeaveDtlEntity.setCreatedDate(new Date());
							employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);

						} else {
							log.info("*** EmployeeLeaveDetails ::: creditEmpYearlyLeavesService() ::: "
									+ "Confirmed More Than One Year ::: EmpId :: " + loopEmpMOY.getId() + " ::: "
									+ " Leave Type :: " + mstLeaveTypeSick.getLeaveTypeName() + " ::: " + "year :: "
									+ year);
						}

						// checking for each employee if the leave detail entry (row) for
						// current year is present
						// PRIVILAGE LEAVE
						EmployeeLeaveDetail currentYearLeaveDetailPrivilage = employeeLeaveDetailDAO
								.findEmployeeLeaveByEIDYEAR(loopEmpMOY.getId(), mstLeaveTypePrivilage.getId(), year);
						if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailPrivilage)) {
							EmployeeLeaveDetail curYearLeaveDetPrivl = new EmployeeLeaveDetail();
							curYearLeaveDetPrivl.setCreatedBy("System New year Scheduler");
							curYearLeaveDetPrivl.setCreatedDate(new Date());
							curYearLeaveDetPrivl.setLeaveAvailable(plLeaveCount);
							curYearLeaveDetPrivl.setLeaveEarned(plLeaveCount);
							curYearLeaveDetPrivl.setMasterLeaveType(mstLeaveTypePrivilage);
							curYearLeaveDetPrivl.setTotalEligibility(plLeaveCount);
							curYearLeaveDetPrivl.setYear(year);
							curYearLeaveDetPrivl.setEmployee(loopEmpMOY);
							employeeLeaveDetailDAO.save(curYearLeaveDetPrivl);

							// entry in credit leave
							EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
							empCreditLeaveDtlEntity.setCreditedBy("System New year Scheduler");
							empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
							empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
							empCreditLeaveDtlEntity.setNoOfDays(plLeaveCount);
							empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
							empCreditLeaveDtlEntity.setPostedDate(new Date());
							empCreditLeaveDtlEntity.setEmployee(loopEmpMOY);
							empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveTypePrivilage);
							empCreditLeaveDtlEntity.setCreatedBy("System New year Scheduler");
							empCreditLeaveDtlEntity.setCreatedDate(new Date());
							employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
						} else {
							log.info("*** EmployeeLeaveDetails ::: creditEmpYearlyLeavesService() ::: "
									+ "Confirmed More Than One Year ::: EmpId :: " + loopEmpMOY.getId() + " ::: "
									+ " Leave Type :: " + mstLeaveTypePrivilage.getLeaveTypeName() + " ::: "
									+ "year :: " + year);
						}
					}

					// addin sick leave for confirmed emps whose less than one year
					for (Employee loopEmpLOY : confirmedEmpListDOJLessThanOneYear) {
						// checking for each employee if the leave detail entry (row) for
						// current year is present
						// SICK LEAVE
						EmployeeLeaveDetail currentYearLeaveDetailSick = employeeLeaveDetailDAO
								.findEmployeeLeaveByEIDYEAR(loopEmpLOY.getId(), mstLeaveTypeSick.getId(), year);
						if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailSick)) {
							EmployeeLeaveDetail curYearLeaveDetSick = new EmployeeLeaveDetail();
							curYearLeaveDetSick.setCreatedBy("System New year Scheduler");
							curYearLeaveDetSick.setCreatedDate(new Date());
							curYearLeaveDetSick.setLeaveAvailable(sickLeaveCount);
							curYearLeaveDetSick.setLeaveEarned(sickLeaveCount);
							curYearLeaveDetSick.setMasterLeaveType(mstLeaveTypeSick);
							curYearLeaveDetSick.setTotalEligibility(sickLeaveCount);
							curYearLeaveDetSick.setYear(year);
							curYearLeaveDetSick.setEmployee(loopEmpLOY);
							employeeLeaveDetailDAO.save(curYearLeaveDetSick);

							// entry in credit leave
							EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
							empCreditLeaveDtlEntity.setCreditedBy("System New year Scheduler");
							empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
							empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
							empCreditLeaveDtlEntity.setNoOfDays(sickLeaveCount);
							empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
							empCreditLeaveDtlEntity.setPostedDate(new Date());
							empCreditLeaveDtlEntity.setEmployee(loopEmpLOY);
							empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveTypeSick);
							empCreditLeaveDtlEntity.setCreatedBy("System New year Scheduler");
							empCreditLeaveDtlEntity.setCreatedDate(new Date());
							employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
						} else {
							log.info("*** EmployeeLeaveDetails ::: creditEmpYearlyLeavesService() ::: "
									+ "Confirmed Less Than One Year ::: EmpId :: " + loopEmpLOY.getId() + " ::: "
									+ " Leave Type :: " + mstLeaveTypeSick.getLeaveTypeName() + " ::: " + "year :: "
									+ year);
						}
					}

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

	@RequestMapping(method = RequestMethod.GET, value = "addCompOffYearEnd", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addCompOffYearEndService() {
		addCompOffYearEnd(1L, 2020);

	}

	public String addCompOffYearEnd(long orgId, int year) {
		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				Organization org = organizationDAO.findById(orgId).get();
				if (!HRMSHelper.isNullOrEmpty(org)) {
					// get comp off master leave type
					MasterLeaveType mstLeaveTypeCompoff = masterLeaveTypeDAO
							.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive, org,
									IHRMSConstants.LEAVE_TYPE_CODE_COMP_OFF);
					Calendar calPrevYearLastDate = Calendar.getInstance();
					calPrevYearLastDate.set((year - 1), 11, 31);
					Calendar calNewYearFirstDay = Calendar.getInstance();
					calNewYearFirstDay.set(year, 0, 01);
					List<Object> compOffLeaveDetails = employeeCreditLeaveDAO.getEmployeeCompoffDetailsForYearChange(
							mstLeaveTypeCompoff.getId(), calPrevYearLastDate.getTime(), IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(compOffLeaveDetails)) {
						// Map of employee
						// Map<> employeeWithCompOff = new HashMap<>();
						for (Iterator<Object> it = compOffLeaveDetails.listIterator(); it.hasNext();) {
							Object[] compOffLeaveDetailInLoop = (Object[]) it.next();
							Employee emp = employeeDAO.findActiveEmployeeById(
									Integer.valueOf(compOffLeaveDetailInLoop[1].toString()), IHRMSConstants.isActive);
							// checking if no of days are correct in format
							Boolean isNoOfDaysCorrect = false;
							if (!HRMSHelper.isNullOrEmpty(compOffLeaveDetailInLoop[4].toString())) {
								if (NumberUtils.isParsable(compOffLeaveDetailInLoop[4].toString())) {
									isNoOfDaysCorrect = true;
								}
							}
							if (!HRMSHelper.isNullOrEmpty(emp) && isNoOfDaysCorrect) {
								// new year employee credit leave detail is getting stored
								EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
								empCreditLeaveDtlEntity.setCreditedBy("Admin");
								empCreditLeaveDtlEntity.setFromDate(calNewYearFirstDay.getTime());
								empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
								empCreditLeaveDtlEntity
										.setNoOfDays(Float.valueOf(compOffLeaveDetailInLoop[4].toString()));
								empCreditLeaveDtlEntity.setToDate(HRMSDateUtil.parse(
										compOffLeaveDetailInLoop[3].toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
								empCreditLeaveDtlEntity.setPostedDate(new Date());
								empCreditLeaveDtlEntity.setEmployee(emp);
								empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveTypeCompoff);
								empCreditLeaveDtlEntity.setCreatedBy("Admin");
								empCreditLeaveDtlEntity.setCreatedDate(new Date());
								empCreditLeaveDtlEntity
										.setLeaveAvailable(Float.valueOf(compOffLeaveDetailInLoop[4].toString()));
								employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);

								// Adding entry in leave details table

								EmployeeLeaveDetail currentYearLeaveDetailCompoff = employeeLeaveDetailDAO
										.findEmployeeLeaveByEIDYEAR(emp.getId(), mstLeaveTypeCompoff.getId(), year);
								if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailCompoff)) {
									// new entry in employee leave details
									EmployeeLeaveDetail curYearLeaveDetCompOff = new EmployeeLeaveDetail();
									curYearLeaveDetCompOff.setCreatedBy("Admin");
									curYearLeaveDetCompOff.setCreatedDate(new Date());
									curYearLeaveDetCompOff.setIsActive(IHRMSConstants.isActive);
									curYearLeaveDetCompOff
											.setLeaveAvailable(Float.valueOf(compOffLeaveDetailInLoop[4].toString()));
									curYearLeaveDetCompOff
											.setLeaveEarned(Float.valueOf(compOffLeaveDetailInLoop[4].toString()));
									curYearLeaveDetCompOff.setMasterLeaveType(mstLeaveTypeCompoff);
									curYearLeaveDetCompOff.setYear(year);
									curYearLeaveDetCompOff.setEmployee(emp);
									employeeLeaveDetailDAO.save(curYearLeaveDetCompOff);
								} else {
									// update existing entry in employee leave details
									currentYearLeaveDetailCompoff
											.setLeaveAvailable(currentYearLeaveDetailCompoff.getLeaveAvailable()
													+ Float.valueOf(compOffLeaveDetailInLoop[4].toString()));
									currentYearLeaveDetailCompoff
											.setLeaveEarned(currentYearLeaveDetailCompoff.getLeaveEarned()
													+ Float.valueOf(compOffLeaveDetailInLoop[4].toString()));
									employeeLeaveDetailDAO.save(currentYearLeaveDetailCompoff);
								}

							} else {
								// don't throw exception here or else handle it here itself
								// cause
								// if one of employee is not present, we will continue for others
								log.info("Employee Leave detail service ::: addCompOffYearEnd() ::: "
										+ " Employee Not found ID or No of days is incorrect :: "
										+ compOffLeaveDetailInLoop[1].toString()
										+ " ::: Employee credit leave detail ID ::: "
										+ compOffLeaveDetailInLoop[0].toString() + " ::: No of days ::: "
										+ compOffLeaveDetailInLoop[2].toString());
							}
						}
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
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
				return HRMSHelper.sendErrorResponse(unknown.getMessage(), IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "testCredPrivalageLeave", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String testCredPrivalageLeave() {

		List<Long> divisionList = new ArrayList<>();
		divisionList.add(1L);
		divisionList.add(2L);
		String val = creditEmpYearlyPrivilageLeavesService(1L, divisionList, 2020, 1.5f);

		return val;
	}

	public String creditEmpYearlyPrivilageLeavesService(long orgId, List<Long> divList, int year, float plLeaveCount) {
		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				Organization org = organizationDAO.findById(orgId).get();
				if (!HRMSHelper.isNullOrEmpty(org)) {
					// setting type of confirmed employment status code
					List<String> employmentTypeNameList = new ArrayList<String>();
					employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_CONFIRMED);
					employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_EMPLOYEE);
					employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_PERMANENT);
					List<MasterEmploymentType> masterEmploymentTypeList = masterEmploymentTypeDAO
							.findByIsActiveAndEmploymentTypeDescriptionIn(IHRMSConstants.isActive,
									employmentTypeNameList);

					// finding list of employees who are confirmed
					// List<Employee> confirmedEmpListDOJMoreThanOneYear =
					// employeeDAO.findEmpListByEmployementStatusAndDOJMoreThanOneYear(IHRMSConstants.isActive,IHRMSConstants.isActive,
					// masterEmploymentTypeList);
					// List<Employee> confirmedEmpListDOJLessThanOneYear =
					// employeeDAO.findEmpListByEmployementStatusAndDOJLessThanOneYear(IHRMSConstants.isActive,IHRMSConstants.isActive,
					// masterEmploymentTypeList);

					List<Employee> confirmedEmpList = employeeDAO.findEmpListByEmployementStatusAndDivision(
							IHRMSConstants.isActive, IHRMSConstants.isActive, masterEmploymentTypeList, divList);

					// get valid confirmed employee list

					// List<Employee> validConfirmedEmpList = new ArrayList();
					// for (Employee confEmp : confirmedEmpList) {

					// Date dateOfJoining =
					// confEmp.getCandidate().getCandidateProfessionalDetail().getDateOfJoining();

					// long diffInMonths;
					// Period diff = Period.between(LocalDate.parse(
					// HRMSDateUtil.format(dateOfJoining, IHRMSConstants.POSTGRE_DATE_FORMAT)),
					// LocalDate.parse(HRMSDateUtil.format(new Date(),
					// IHRMSConstants.POSTGRE_DATE_FORMAT)));
					// diffInMonths = diff.toTotalMonths();

					// if(HRMSDateUtil.getDay(dateOfJoining) <= 15) {

					// if(diffInMonths >= 6) {
					// validConfirmedEmpList.add(confEmp);
					// }

					// }else if (HRMSDateUtil.getDay(dateOfJoining) > 15) {

					// if(diffInMonths >= 7) {
					// validConfirmedEmpList.add(confEmp);
					// }
					// }

					// log.info(" Confirmed Employee Count for PL :: "+confirmedEmpList.size());
					// log.info(" Valid Confirmed Employee Count for PL ::
					// "+validConfirmedEmpList.size());

					// }

					// adding new leaves in employee leave detail

					// get details of leave type
					// MasterLeaveType mstLeaveTypeSick =
					// masterLeaveTypeDAO.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive,
					// org, IHRMSConstants.LEAVE_TYPE_CODE_SICK);
					MasterLeaveType mstLeaveTypePrivilage = masterLeaveTypeDAO
							.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive, org,
									IHRMSConstants.LEAVE_TYPE_CODE_PRIVILAGE);

					Calendar calFromDate = Calendar.getInstance();
					calFromDate.set(year, calFromDate.get(Calendar.MONTH), 01);
					Calendar calToDate = Calendar.getInstance();
					calToDate.set(year, 11, 31);
					// loopEmpMOY :: looped employee more than one year
					int count = 0;
					// for (Employee loopEmpMOY : validConfirmedEmpList) {
					for (Employee loopEmpMOY : confirmedEmpList) {

						// checking for each employee if the leave detail entry (row) for
						// current year is present
						// PRIVILAGE LEAVE
						EmployeeLeaveDetail currentYearLeaveDetailPrivilage = employeeLeaveDetailDAO
								.findEmployeeLeaveByEIDYEAR(loopEmpMOY.getId(), mstLeaveTypePrivilage.getId(), year);

						if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailPrivilage)) {
							// EmployeeLeaveDetail curYearLeaveDetPrivl = new EmployeeLeaveDetail();
							currentYearLeaveDetailPrivilage = new EmployeeLeaveDetail();
							currentYearLeaveDetailPrivilage.setCreatedBy("Monthly Scheduler");
							currentYearLeaveDetailPrivilage.setCreatedDate(new Date());
							currentYearLeaveDetailPrivilage.setLeaveAvailable(plLeaveCount);
							currentYearLeaveDetailPrivilage.setLeaveEarned(plLeaveCount);
							currentYearLeaveDetailPrivilage.setMasterLeaveType(mstLeaveTypePrivilage);
							currentYearLeaveDetailPrivilage.setTotalEligibility(plLeaveCount);
							currentYearLeaveDetailPrivilage.setYear(year);
							currentYearLeaveDetailPrivilage.setEmployee(loopEmpMOY);
							employeeLeaveDetailDAO.save(currentYearLeaveDetailPrivilage);

							log.info("*** EmployeeLeaveDetails ::: creditEmpYearlyPrivilageLeavesService() ::: "
									+ "Valid confirmed ::: EmpId :: " + loopEmpMOY.getId() + " ::: " + " Leave Type :: "
									+ mstLeaveTypePrivilage.getLeaveTypeName() + " ::: " + "year :: " + year);
						} else {
							// EmployeeLeaveDetail curYearLeaveDetPrivl = new EmployeeLeaveDetail();
							currentYearLeaveDetailPrivilage.setCreatedBy("Monthly Scheduler");
							currentYearLeaveDetailPrivilage.setCreatedDate(new Date());
							currentYearLeaveDetailPrivilage.setLeaveAvailable(
									currentYearLeaveDetailPrivilage.getLeaveAvailable() + plLeaveCount);
							currentYearLeaveDetailPrivilage
									.setLeaveEarned(currentYearLeaveDetailPrivilage.getLeaveEarned() + plLeaveCount);
							currentYearLeaveDetailPrivilage.setMasterLeaveType(mstLeaveTypePrivilage);

							if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailPrivilage.getTotalEligibility())) {
								currentYearLeaveDetailPrivilage.setTotalEligibility(new Float(0));
							}

							currentYearLeaveDetailPrivilage.setTotalEligibility(
									currentYearLeaveDetailPrivilage.getTotalEligibility() + plLeaveCount);
							currentYearLeaveDetailPrivilage.setYear(year);
							currentYearLeaveDetailPrivilage.setEmployee(loopEmpMOY);

							employeeLeaveDetailDAO.save(currentYearLeaveDetailPrivilage);

							log.info("*** EmployeeLeaveDetails ::: creditEmpYearlyPrivilageLeavesService() ::: "
									+ "Valid confirmed ::: EmpId :: " + loopEmpMOY.getId() + " ::: " + " Leave Type :: "
									+ mstLeaveTypePrivilage.getLeaveTypeName() + " ::: " + "year :: " + year);
						}
						// entry in credit leave
						EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
						empCreditLeaveDtlEntity.setCreditedBy("Monthly Scheduler");
						empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
						empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
						empCreditLeaveDtlEntity.setNoOfDays(plLeaveCount);
						empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
						empCreditLeaveDtlEntity.setPostedDate(new Date());
						empCreditLeaveDtlEntity.setEmployee(loopEmpMOY);
						empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveTypePrivilage);
						empCreditLeaveDtlEntity.setCreatedBy("System Month Scheduler");
						empCreditLeaveDtlEntity.setCreatedDate(new Date());
						employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
						++count;
					}

					log.info("Total number of employee PL to be credited:: " + confirmedEmpList.size());
					log.info("Total number of employee PL credited:: " + count);

					if (confirmedEmpList.size() == count) {
						log.info("Success count :: " + count);
						return "Success count :: " + count;
					} else {
						return "Mismatch between count of active employee and count of leave credited employee";
					}
					// return String.valueOf(count);
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
				log.info("Unknown Error while credit monthly privilage leave ::" + unknown.getStackTrace());
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "testCredEmergencyLeave", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String testCredEmergencyLeave() {

		List<Long> divisionList = new ArrayList<>();
		divisionList.add(1L);
		divisionList.add(2L);
		String val = creditEmpYearlyEmergencyLeavesService(1l, divisionList, 2020, 12.0f);

		return val;
	}

	// Credit emergency leaves every year
	public String creditEmpYearlyEmergencyLeavesService(long orgId, List<Long> divList, int year, float plLeaveCount) {
		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				Organization org = organizationDAO.findById(orgId).get();
				if (!HRMSHelper.isNullOrEmpty(org)) {
					// setting type of confirmed employment status code
					// List<String> employmentTypeNameList = new ArrayList<String>();
					// employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_CONFIRMED);
					// employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_EMPLOYEE);
					// employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_PERMANENT);
					// List<MasterEmploymentType> masterEmploymentTypeList =
					// masterEmploymentTypeDAO.findByIsActiveAndEmploymentTypeDescriptionIn(IHRMSConstants.isActive,employmentTypeNameList);

					// finding list of employees who are confirmed
					// List<Employee> confirmedEmpListDOJMoreThanOneYear =
					// employeeDAO.findEmpListByEmployementStatusAndDOJMoreThanOneYear(IHRMSConstants.isActive,IHRMSConstants.isActive,
					// masterEmploymentTypeList);
					// List<Employee> confirmedEmpListDOJLessThanOneYear =
					// employeeDAO.findEmpListByEmployementStatusAndDOJLessThanOneYear(IHRMSConstants.isActive,IHRMSConstants.isActive,
					// masterEmploymentTypeList);

					List<Employee> activeEmpList = employeeDAO
							.findActiveEmpForCreditEmergencyLeavesSch1edulerByDivision(IHRMSConstants.isActive,
									IHRMSConstants.isActive, divList);

					// get valid confirmed employee list

					/*
					 * List<Employee> validConfirmedEmpList = new ArrayList(); for (Employee confEmp
					 * : activeEmpList) {
					 * 
					 * Date dateOfJoining =
					 * confEmp.getCandidate().getCandidateProfessionalDetail().getDateOfJoining();
					 * 
					 * long diffInMonths; Period diff = Period.between(LocalDate.parse(
					 * HRMSDateUtil.format(dateOfJoining, IHRMSConstants.POSTGRE_DATE_FORMAT)),
					 * LocalDate.parse(HRMSDateUtil.format(new Date(),
					 * IHRMSConstants.POSTGRE_DATE_FORMAT))); diffInMonths = diff.toTotalMonths();
					 * if(HRMSDateUtil.getDay(dateOfJoining) <= 15) {
					 * 
					 * if(diffInMonths >= 6) { validConfirmedEmpList.add(confEmp); }
					 * 
					 * }else if (HRMSDateUtil.getDay(dateOfJoining) > 15) {
					 * 
					 * if(diffInMonths >= 7) { validConfirmedEmpList.add(confEmp); } }
					 * 
					 * }
					 */

					// log.info(" Valid Confirmed Employee Count for PL ::
					// "+validConfirmedEmpList.size());

					// adding new leaves in employee leave detail

					// get details of leave type
					MasterLeaveType mstLeaveTypeSick = masterLeaveTypeDAO.findByIsActiveAndOrganizationAndLeaveTypeCode(
							IHRMSConstants.isActive, org, IHRMSConstants.LEAVE_TYPE_CODE_SICK);
					// MasterLeaveType mstLeaveTypePrivilage =
					// masterLeaveTypeDAO.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive,
					// org,IHRMSConstants.LEAVE_TYPE_CODE_PRIVILAGE);

					Calendar calFromDate = Calendar.getInstance();
					calFromDate.set(year, calFromDate.get(Calendar.MONTH), 01);
					Calendar calToDate = Calendar.getInstance();
					calToDate.set(year, 11, 31);
					// loopEmpMOY :: looped employee more than one year
					int count = 0;

					for (Employee loopEmpMOY : activeEmpList) {

						// checking for each employee if the leave detail entry (row) for
						// current year is present
						// PRIVILAGE LEAVE
						EmployeeLeaveDetail currentYearLeaveDetailPrivilage = employeeLeaveDetailDAO
								.findEmployeeLeaveByEIDYEAR(loopEmpMOY.getId(), mstLeaveTypeSick.getId(), year);

						if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailPrivilage)) {
							// EmployeeLeaveDetail curYearLeaveDetPrivl = new EmployeeLeaveDetail();
							currentYearLeaveDetailPrivilage = new EmployeeLeaveDetail();
							currentYearLeaveDetailPrivilage.setCreatedBy("Monthly Scheduler");
							currentYearLeaveDetailPrivilage.setCreatedDate(new Date());
							currentYearLeaveDetailPrivilage.setLeaveAvailable(plLeaveCount);
							currentYearLeaveDetailPrivilage.setLeaveEarned(plLeaveCount);
							currentYearLeaveDetailPrivilage.setMasterLeaveType(mstLeaveTypeSick);
							currentYearLeaveDetailPrivilage.setTotalEligibility(plLeaveCount);
							currentYearLeaveDetailPrivilage.setYear(year);
							currentYearLeaveDetailPrivilage.setEmployee(loopEmpMOY);
							employeeLeaveDetailDAO.save(currentYearLeaveDetailPrivilage);

							// entry in credit leave
							EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
							empCreditLeaveDtlEntity.setCreditedBy("Yearly Scheduler");
							empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
							empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
							empCreditLeaveDtlEntity.setNoOfDays(plLeaveCount);
							empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
							empCreditLeaveDtlEntity.setPostedDate(new Date());
							empCreditLeaveDtlEntity.setEmployee(loopEmpMOY);
							empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveTypeSick);
							empCreditLeaveDtlEntity.setCreatedBy("Yearly Scheduler");
							empCreditLeaveDtlEntity.setCreatedDate(new Date());
							employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
							++count;

							log.info("*** EmployeeLeaveDetails ::: creditEmpYearlyEmergencyLeavesService() ::: "
									+ "Active Employee ::: EmpId :: " + loopEmpMOY.getId() + " ::: " + " Leave Type :: "
									+ mstLeaveTypeSick.getLeaveTypeName() + " ::: " + "year :: " + year);
						} else {

							log.info("Yearly emergency leave already present for >>>> ");
							log.info("*** EmployeeLeaveDetails ::: creditEmpYearlyEmergencyLeavesService() ::: "
									+ "Active Employee ::: EmpId :: " + loopEmpMOY.getId() + " ::: " + " Leave Type :: "
									+ mstLeaveTypeSick.getLeaveTypeName() + " ::: " + "year :: " + year);
						}

					}

					log.info("Total number of employee EL to be credited:: " + activeEmpList.size());
					log.info("Total number of employee EL credited:: " + count);

					if (activeEmpList.size() == count) {
						log.info("Success count :: " + count);
						return "Success count :: " + count;
					} else {
						return "Mismatch between count of active employee and count of leave credited employee";
					}
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
				log.info("Unknown Error while credit yearly emergency leave ::" + unknown.getStackTrace());
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public String creditEmpMonthlyLeavesForDivision4Service(long orgId, List<Long> divList) {
		log.info("Inside creditEmpMonthlyLeavesForDivision4Service method");
		log.info("Today date ::" + new Date());
		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				Organization org = organizationDAO.findById(orgId).get();
				for (Long divId : divList) {
					log.info("Crediting liv for Division::" + divId);

					if (!HRMSHelper.isNullOrEmpty(org)) {
						List<Employee> employeeList = employeeDAO.findDivfourEmpList(IHRMSConstants.isActive, divId);

						MasterLeaveType mstLeaveTypeEmergency = masterLeaveTypeDAO
								.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive, org,
										IHRMSConstants.LEAVE_TYPE_CODE_SICK);

						MasterLeaveType mstLeaveTypePrivilage = masterLeaveTypeDAO
								.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive, org,
										IHRMSConstants.LEAVE_TYPE_CODE_PRIVILAGE);

						int year = Year.now().getValue(); // Get the current year
						Calendar calToday = Calendar.getInstance();
						Calendar calFromDate = Calendar.getInstance();
						calFromDate.set(year, 0, 1);
						Calendar calToDate = Calendar.getInstance();
						calToDate.set(year, 11, 31);

						// Fetch leave count dynamically based on division ID and leave cycle
						Float leaveCount = masterLeaveTypeMappingDAO.findLeaveCountByDivisionIdAndLeaveCycle(divId,
								IHRMSConstants.LEAVE_CYCLE_MONTHLY);
						if (leaveCount != null) {
							for (Employee loopEmp : employeeList) {
								boolean isEmployeeConfirmed = false;
								String employeeStatus = null;
								Date joiningDate = loopEmp.getCandidate().getCandidateProfessionalDetail()
										.getDateOfJoining();

								if (!HRMSHelper.isNullOrEmpty(loopEmp.getCandidate())) {
									if (loopEmp.getCandidate().getCandidateEmploymentStatus()
											.equalsIgnoreCase(IHRMSConstants.EMPLOYMENT_TYPE_CONFIRMED)
											|| loopEmp.getCandidate().getCandidateEmploymentStatus()
													.equalsIgnoreCase(IHRMSConstants.EMPLOYMENT_TYPE_PERMANENT)) {
										isEmployeeConfirmed = true;
										employeeStatus = "Confirmed";
									}
								}

								log.info(
										"Crediting leave for Employee ID: " + loopEmp.getId() + " DOJ: " + joiningDate);
								log.info("Employment Status:: " + employeeStatus);
								if (joiningDate != null) {
									Calendar calJoiningDate = Calendar.getInstance();
									calJoiningDate.setTime(joiningDate);

									// Check if the employee has completed one year
									Calendar calOneYearFromJoining = Calendar.getInstance();
									calOneYearFromJoining.setTime(joiningDate);
									calOneYearFromJoining.add(Calendar.YEAR, 1);

									// Convert the java.sql.Date to java.util.Date
									Date sqlJoiningDate = joiningDate;
									Date utilJoiningDate = new java.util.Date(sqlJoiningDate.getTime());

									// Convert the java.util.Date to Instant
									Instant instant = utilJoiningDate.toInstant();

									// Convert the Instant to LocalDate using UTC time zone
									LocalDate localJoiningDate = instant.atZone(ZoneId.of("UTC")).toLocalDate();

									// Get the current date
									LocalDate currentDate = LocalDate.now();

									// Calculate the period between the two dates
									Period period = Period.between(localJoiningDate, currentDate);

									// Extract the years, months, and days from the period
									int yearsDifference = period.getYears();
									int monthsDifference = period.getMonths();
									int daysDifference = period.getDays();

									log.info("Total Service:: YEARS-" + yearsDifference + " MONTHS-" + monthsDifference
											+ " DAYS-" + daysDifference);

									// calculate years till 15 of current month
									LocalDate yearTillDay15 = LocalDate.of(year, calToday.get(Calendar.MONTH) + 1, 15);
									Period calculatePeriodTillDay15 = Period.between(localJoiningDate, yearTillDay15);

									// calculate years till last day of current month
									LocalDate yearTillLastDayOfMonth = LocalDate.of(year,
											calToday.get(Calendar.MONTH) + 1,
											calToday.getActualMaximum(Calendar.DAY_OF_MONTH));
									Period calculatePeriodTillLastDayOfMonth = Period.between(localJoiningDate,
											yearTillLastDayOfMonth);

									log.info("Till 15, YEARS::" + calculatePeriodTillDay15.getYears() + " MONTHS:: "
											+ calculatePeriodTillDay15.getMonths() + " DAYS:: "
											+ calculatePeriodTillDay15.getDays());
									log.info("Till 30/31, YEARS::" + calculatePeriodTillLastDayOfMonth.getYears()
											+ " MONTHS:: " + calculatePeriodTillLastDayOfMonth.getMonths() + " DAYS:: "
											+ calculatePeriodTillLastDayOfMonth.getDays());

									if (calToday.get(Calendar.MONTH) == Calendar.JANUARY) {
										log.info("Inside January month.");
										// calculate years from 16 jan to 31 jan
										LocalDate yearTillJan31 = LocalDate.of(year, calToday.get(Calendar.MONTH) + 1,
												31);
										Period calculatePeriodTillJan31 = Period.between(localJoiningDate,
												yearTillJan31);
										if (isEmployeeConfirmed) {
											// log.info("one year or more than one year completed before 15");
											log.info("Employee is Confirmed");
											creditPrivilegeLeaves(loopEmp, mstLeaveTypePrivilage, year,
													IHRMSConstants.PRIVILEGE_LEAVE_COUNT, calFromDate, calToDate);
											creditEmergencyLeaves(loopEmp, mstLeaveTypeEmergency, year,
													IHRMSConstants.EMERGENCY_LEAVE_COUNT, calFromDate, calToDate);

											log.info(" - Credited " + IHRMSConstants.PRIVILEGE_LEAVE_COUNT
													+ " privilege leaves and " + IHRMSConstants.EMERGENCY_LEAVE_COUNT
													+ " emergency leaves");

											updateLeaveCreditedStatus(loopEmp);

										} else {

											log.info("2.5 leave credited before one year complete");
											// Credit 2.5 leaves for each month
											creditEmergencyLeave(loopEmp, mstLeaveTypeEmergency, year, leaveCount,
													calFromDate, calToDate);

											log.info("Credited " + leaveCount + " leaves for each month");
										}

										/*
										 * else if (calculatePeriodTillJan31.getYears() >= 1 && isEmployeeConfirmed) {
										 * log.info("one year or more than one year completed after  15");
										 * calFromDate.add(Calendar.MONTH, 1); calToDate.add(Calendar.MONTH, 1);
										 * 
										 * float additionalLeaveCount = 11 * leaveCount;
										 * creditEmergencyLeaveDirectUpdate(loopEmp, mstLeaveTypeEmergency, year,
										 * additionalLeaveCount, calFromDate, calToDate);
										 * 
										 * log.info("Credited additional leave: " + additionalLeaveCount); }
										 */

									} else {

										if (isEmployeeConfirmed) {
											boolean isYearlyLeaveCredited = false;
											// checking in leave credited or not
											EmpYearlyLeaveCreditedStatus empYearlyLeaveCreditedStatus = empYearlyLeaveCreditedDAO
													.findByEmployeeIdAndYear(loopEmp.getId(), Year.now().getValue());

											if (!HRMSHelper.isNullOrEmpty(empYearlyLeaveCreditedStatus)) {
												isYearlyLeaveCredited = true;
											}

											if (!isYearlyLeaveCredited) {
												log.info("Confirmed but yearly leave not credited");
												int additionalMonths = 0;
												float additionalLeaveCount = 0;
												if ((calculatePeriodTillDay15.getYears() == 1
														&& calculatePeriodTillDay15.getMonths() == 0
														&& daysDifference != 1
														&& calculatePeriodTillDay15.getDays() <= 15
														&& isEmployeeConfirmed)
														|| (calculatePeriodTillDay15.getYears() >= 1
																&& calculatePeriodTillDay15.getMonths() >= 1
																&& isEmployeeConfirmed)) {
													log.info("Years:" + calculatePeriodTillDay15.getYears() + "Months:"
															+ calculatePeriodTillDay15.getMonths());
													log.info("exact one year complete other than JAN before 15");

													// additionalMonths = 12 - calJoiningDate.get(Calendar.MONTH);
													additionalMonths = 12 - calToday.get(Calendar.MONTH);
													log.info("Joining month::" + calJoiningDate.get(Calendar.MONTH)
															+ " Months for leave calculate::" + additionalMonths);
													additionalLeaveCount = additionalMonths * leaveCount;
													// creditEmergencyLeaveDirectUpdate(loopEmp, mstLeaveTypeEmergency,
													// year, additionalLeaveCount, calFromDate, calToDate);

													creditEmergencyLeave(loopEmp, mstLeaveTypeEmergency, year,
															additionalLeaveCount, calFromDate, calToDate);
													log.info("Credited additional leave: " + additionalLeaveCount);

													// insert in yearly leave credit status
													updateLeaveCreditedStatus(loopEmp);

												} else if ((calculatePeriodTillLastDayOfMonth.getYears() == 1
														&& calculatePeriodTillLastDayOfMonth.getMonths() == 0
														&& daysDifference != 1 && isEmployeeConfirmed)
														|| (calculatePeriodTillLastDayOfMonth.getYears() == 1
																&& calculatePeriodTillLastDayOfMonth.getMonths() == 0
																&& calculatePeriodTillLastDayOfMonth.getDays() == 0
																&& isEmployeeConfirmed)) {
													log.info("Years:" + calculatePeriodTillLastDayOfMonth.getYears()
															+ "Months:"
															+ calculatePeriodTillLastDayOfMonth.getMonths());
													log.info("exact one year complete other than JAN before 31");
													calFromDate.add(Calendar.MONTH, 1);
													calToDate.add(Calendar.MONTH, 1);

													if (calculatePeriodTillLastDayOfMonth.getYears() == 1
															&& calculatePeriodTillLastDayOfMonth.getMonths() == 0
															&& calculatePeriodTillLastDayOfMonth.getDays() == 0) {
														additionalMonths = 12 - calToday.get(Calendar.MONTH);
													} else {
														additionalMonths = 11 - calToday.get(Calendar.MONTH);
													}

													log.info("Joining month::" + calJoiningDate.get(Calendar.MONTH)
															+ " Months for leave calculate::" + additionalMonths);
													additionalLeaveCount = additionalMonths * leaveCount;
													/*
													 * creditEmergencyLeaveDirectUpdate(loopEmp, mstLeaveTypeEmergency,
													 * year, additionalLeaveCount, calFromDate, calToDate);
													 */

													creditEmergencyLeave(loopEmp, mstLeaveTypeEmergency, year,
															additionalLeaveCount, calFromDate, calToDate);
													log.info("Credited additional leave: " + additionalLeaveCount);

													// insert in yearly leave credit status
													updateLeaveCreditedStatus(loopEmp);

												} else if ((calculatePeriodTillLastDayOfMonth.getYears() == 1
														&& calculatePeriodTillLastDayOfMonth.getMonths() == 0
														&& daysDifference != 1)
														|| calculatePeriodTillLastDayOfMonth.getYears() < 1
														|| !isEmployeeConfirmed) {
													log.info("2.5 leave credited before one year complete");
													// // Credit 2.5 leaves for each month
													creditEmergencyLeave(loopEmp, mstLeaveTypeEmergency, year,
															leaveCount, calFromDate, calToDate);
													log.info("Credited " + leaveCount + " leaves for each month");
												}
											} else {
												log.info("Employee is confirmed and yearly leave already credited.");
											}
										} else {
											log.info("2.5 leave credited because employee not confirmed");
											// Credit 2.5 leaves for each month
											creditEmergencyLeave(loopEmp, mstLeaveTypeEmergency, year, leaveCount,
													calFromDate, calToDate);

											log.info("Credited " + leaveCount + " leaves for each month");
										}

									}
								}
								log.info("*******************************************************************");
							}
						} else {
							// Handle the case when leave count is not found for the division and leave
							throw new HRMSException("LeaveCountNotFound");

						}

						log.info("Emergency leaves credited successfully for Division" + divId + "employees.");
					}
				}

			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} catch (HRMSException e) {
			handleHRMSException(e);
		} catch (Exception unknown) {

			handleUnknownException(unknown);
		}
		return null;
	}

	private void updateLeaveCreditedStatus(Employee emp) {
		log.info("Inside updateLeaveCreditedStatus method");
		EmpYearlyLeaveCreditedStatus leaveCreditedStatus = new EmpYearlyLeaveCreditedStatus();
		leaveCreditedStatus.setIsActive(IHRMSConstants.isActive);
		leaveCreditedStatus.setCreatedBy("Monthely scheduler");
		leaveCreditedStatus.setCreatedDate(new Date());
		leaveCreditedStatus.setEmployeeId(emp.getId());
		leaveCreditedStatus.setYear(Year.now().getValue());
		leaveCreditedStatus.setLeaveCrideted(IHRMSConstants.isActive);
		empYearlyLeaveCreditedDAO.save(leaveCreditedStatus);
		log.info("Exit from updateLeaveCreditedStatus method");

	}

	private void handleHRMSException(HRMSException e) {
		log.error("HRMSException: " + e.getMessage(), e);
	}

	private void handleUnknownException(Exception unknown) {
		log.error("Unknown Exception: " + unknown.getMessage(), unknown);
	}

	private void creditEmergencyLeave(Employee employee, MasterLeaveType leaveType, int year, float leaveCount,
			Calendar calFromDate, Calendar calToDate) {
		try {
			EmployeeLeaveDetail currentYearLeaveDetailEmergency = employeeLeaveDetailDAO
					.findEmployeeLeaveByEIDYEAR(employee.getId(), leaveType.getId(), year);
			if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailEmergency)) {
				// Create a new leave entry for the employee
				EmployeeLeaveDetail leaveDetail = new EmployeeLeaveDetail();
				leaveDetail.setCreatedBy("System Monthly Scheduler");
				leaveDetail.setCreatedDate(new Date());
				leaveDetail.setLeaveAvailable(leaveCount);
				leaveDetail.setLeaveEarned(leaveCount);
				leaveDetail.setMasterLeaveType(leaveType);
				leaveDetail.setTotalEligibility(leaveCount);
				leaveDetail.setYear(year);
				leaveDetail.setEmployee(employee);
				employeeLeaveDetailDAO.save(leaveDetail);

				// Entry in credit leave
				EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
				empCreditLeaveDtlEntity.setCreditedBy("System Monthly Scheduler");
				empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
				empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
				empCreditLeaveDtlEntity.setNoOfDays(leaveCount);
				empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
				empCreditLeaveDtlEntity.setPostedDate(new Date());
				empCreditLeaveDtlEntity.setEmployee(employee);
				empCreditLeaveDtlEntity.setMasterLeaveType(leaveType);
				empCreditLeaveDtlEntity.setCreatedBy("System Monthly Scheduler");
				empCreditLeaveDtlEntity.setCreatedDate(new Date());
				employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
			} else {
				// Update existing leave details
				currentYearLeaveDetailEmergency
						.setLeaveAvailable(currentYearLeaveDetailEmergency.getLeaveAvailable() + leaveCount);
				currentYearLeaveDetailEmergency
						.setLeaveEarned(currentYearLeaveDetailEmergency.getLeaveEarned() + leaveCount);
				currentYearLeaveDetailEmergency
						.setTotalEligibility(currentYearLeaveDetailEmergency.getTotalEligibility() + leaveCount);
				employeeLeaveDetailDAO.save(currentYearLeaveDetailEmergency);

				// Entry in credit leave
				EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
				empCreditLeaveDtlEntity.setCreditedBy("System Monthly Scheduler");
				empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
				empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
				empCreditLeaveDtlEntity.setNoOfDays(leaveCount);
				empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
				empCreditLeaveDtlEntity.setPostedDate(new Date());
				empCreditLeaveDtlEntity.setEmployee(employee);
				empCreditLeaveDtlEntity.setMasterLeaveType(leaveType);
				empCreditLeaveDtlEntity.setCreatedBy("System Monthly Scheduler");
				empCreditLeaveDtlEntity.setCreatedDate(new Date());
				employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
			}
		} catch (Exception e) {
			log.error("Error crediting emergency leave for employee: " + employee.getId(), e);
		}
	}

	private void creditEmergencyLeaveDirectUpdate(Employee employee, MasterLeaveType leaveType, int year,
			float leaveCount, Calendar calFromDate, Calendar calToDate) {
		try {
			EmployeeLeaveDetail currentYearLeaveDetailEmergency = employeeLeaveDetailDAO
					.findEmployeeLeaveByEIDYEAR(employee.getId(), leaveType.getId(), year);

			if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailEmergency)) {
				// Create a new leave entry for the employee
				EmployeeLeaveDetail leaveDetail = new EmployeeLeaveDetail();
				leaveDetail.setCreatedBy("System Monthly Scheduler");
				leaveDetail.setCreatedDate(new Date());
				leaveDetail.setLeaveAvailable(leaveCount);
				leaveDetail.setLeaveEarned(leaveCount);
				leaveDetail.setMasterLeaveType(leaveType);
				leaveDetail.setTotalEligibility(leaveCount);
				leaveDetail.setYear(year);
				leaveDetail.setEmployee(employee);
				employeeLeaveDetailDAO.save(leaveDetail);
			} else {
				// Update existing leave details without adding to the existing count
				currentYearLeaveDetailEmergency.setLeaveAvailable(leaveCount);
				currentYearLeaveDetailEmergency.setLeaveEarned(leaveCount);
				currentYearLeaveDetailEmergency.setTotalEligibility(leaveCount);
				employeeLeaveDetailDAO.save(currentYearLeaveDetailEmergency);
			}

			// Entry in credit leave
			EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
			empCreditLeaveDtlEntity.setCreditedBy("System Monthly Scheduler");
			empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
			empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
			empCreditLeaveDtlEntity.setNoOfDays(leaveCount);
			empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
			empCreditLeaveDtlEntity.setPostedDate(new Date());
			empCreditLeaveDtlEntity.setEmployee(employee);
			empCreditLeaveDtlEntity.setMasterLeaveType(leaveType);
			empCreditLeaveDtlEntity.setCreatedBy("System Monthly Scheduler");
			empCreditLeaveDtlEntity.setCreatedDate(new Date());
			employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);

		} catch (Exception e) {
			log.error("Error crediting emergency leave for employee: " + employee.getId(), e);
		}
	}

	private void creditEmergencyLeaves(Employee employee, MasterLeaveType leaveType, int year, float leaveCount,
			Calendar calFromDate, Calendar calToDate) {
		try {
			// Check if the employee already has leave details for the specified leave type
			// and year
			EmployeeLeaveDetail currentYearLeaveDetailEmergency = employeeLeaveDetailDAO
					.findEmployeeLeaveByEIDYEAR(employee.getId(), leaveType.getId(), year);

			if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailEmergency)) {
				// Create a new leave entry for the employee
				EmployeeLeaveDetail leaveDetail = new EmployeeLeaveDetail();
				leaveDetail.setCreatedBy("System Monthly Scheduler");
				leaveDetail.setCreatedDate(new Date());
				leaveDetail.setLeaveAvailable(leaveCount);
				leaveDetail.setLeaveEarned(leaveCount);
				leaveDetail.setMasterLeaveType(leaveType);
				leaveDetail.setTotalEligibility(leaveCount);
				leaveDetail.setYear(year);
				leaveDetail.setEmployee(employee);
				employeeLeaveDetailDAO.save(leaveDetail);
			}

			// Entry in credit leave
			EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
			empCreditLeaveDtlEntity.setCreditedBy("System Monthly Scheduler");
			empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
			empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
			empCreditLeaveDtlEntity.setNoOfDays(leaveCount);
			empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
			empCreditLeaveDtlEntity.setPostedDate(new Date());
			empCreditLeaveDtlEntity.setEmployee(employee);
			empCreditLeaveDtlEntity.setMasterLeaveType(leaveType);
			empCreditLeaveDtlEntity.setCreatedBy("System Monthly Scheduler");
			empCreditLeaveDtlEntity.setCreatedDate(new Date());
			employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);

		} catch (Exception e) {
			log.error("Error crediting emergency leave for employee: " + employee.getId(), e);
		}
	}

	private void creditPrivilegeLeaves(Employee employee, MasterLeaveType leaveType, int year, float leaveCount,
			Calendar calFromDate, Calendar calToDate) {
		try {
			// Check if the employee already has leave details for the specified leave type
			// and year
			EmployeeLeaveDetail currentYearLeaveDetailEmergency = employeeLeaveDetailDAO
					.findEmployeeLeaveByEIDYEAR(employee.getId(), leaveType.getId(), year);

			if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetailEmergency)) {
				// Create a new leave entry for the employee
				EmployeeLeaveDetail leaveDetail = new EmployeeLeaveDetail();
				leaveDetail.setCreatedBy("System Monthly Scheduler");
				leaveDetail.setCreatedDate(new Date());
				leaveDetail.setLeaveAvailable(leaveCount);
				leaveDetail.setLeaveEarned(leaveCount);
				leaveDetail.setMasterLeaveType(leaveType);
				leaveDetail.setTotalEligibility(leaveCount);
				leaveDetail.setYear(year);
				leaveDetail.setEmployee(employee);
				employeeLeaveDetailDAO.save(leaveDetail);
			}

			// Entry in credit leave
			EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
			empCreditLeaveDtlEntity.setCreditedBy("System Monthly Scheduler");
			empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
			empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
			empCreditLeaveDtlEntity.setNoOfDays(leaveCount);
			empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
			empCreditLeaveDtlEntity.setPostedDate(new Date());
			empCreditLeaveDtlEntity.setEmployee(employee);
			empCreditLeaveDtlEntity.setMasterLeaveType(leaveType);
			empCreditLeaveDtlEntity.setCreatedBy("System Monthly Scheduler");
			empCreditLeaveDtlEntity.setCreatedDate(new Date());
			employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);

		} catch (Exception e) {
			log.error("Error crediting emergency leave for employee: " + employee.getId(), e);
		}
	}

	public String addServiceCompletionLeavesYearEnd(long orgId, int year) {

		try {
			log.info("Inside addServiceCompletionLeavesYearEnd method...");

			if (!HRMSHelper.isLongZero(orgId)) {
				Organization org = organizationDAO.findById(orgId).get();
				if (!HRMSHelper.isNullOrEmpty(org)) {

					MasterLeaveType mstLeaveTypeServiceCompletion = masterLeaveTypeDAO
							.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive, org,
									IHRMSConstants.LEAVE_TYPE_CODE_SERVICE_COMPLETION);

					int prevYear = year - 1;
					LocalDate lastDayOfPrevYear = LocalDate.of(prevYear, 12, 31);
					Date dateLastDayOfPrevYear = java.sql.Date.valueOf(lastDayOfPrevYear);

					List<EmployeeCreditLeaveDetail> prevYearLeaveDetails = employeeCreditLeaveDAO
							.findEmployeeCreditedLeaveByTypeAndDate(mstLeaveTypeServiceCompletion.getId(),
									dateLastDayOfPrevYear);

					if (!HRMSHelper.isNullOrEmpty(prevYearLeaveDetails)) {
						log.info("Found employees with service completion leave from the previous year."
								+ prevYearLeaveDetails.size());

						for (EmployeeCreditLeaveDetail prevYearLeaveDetail : prevYearLeaveDetails) {
							long empId = prevYearLeaveDetail.getEmployee().getId();
							Date fromDate = prevYearLeaveDetail.getFromDate();
							Date toDate = prevYearLeaveDetail.getToDate();
							String leaveType = prevYearLeaveDetail.getMasterLeaveType().getLeaveTypeName();
							Long leaveTypeId = prevYearLeaveDetail.getMasterLeaveType().getId();

							log.info("Processing employee ID: " + empId);
							log.info("From Date: " + fromDate + ", To Date: " + toDate + ", Leave Type: " + leaveType);

							EmployeeLeaveDetail currentYearLeaveDetail = employeeLeaveDetailDAO
									.findEmployeeLeaveByEIDYEAR(empId, leaveTypeId, prevYear);

							if (!HRMSHelper.isNullOrEmpty(currentYearLeaveDetail)
									&& currentYearLeaveDetail.getLeaveAvailable() > 0) {
								log.info("Employee has leave available for the current year.===> Processing <===");

								EmployeeLeaveDetail newLeaveDetail = new EmployeeLeaveDetail();
								newLeaveDetail.setEmployee(currentYearLeaveDetail.getEmployee());
								newLeaveDetail.setMasterLeaveType(currentYearLeaveDetail.getMasterLeaveType());
								newLeaveDetail.setLeaveAvailable(currentYearLeaveDetail.getLeaveAvailable());
								newLeaveDetail.setCreatedBy("System Yearly Scheduler");
								newLeaveDetail.setCreatedDate(new Date());
								newLeaveDetail.setYear(year);
								employeeLeaveDetailDAO.save(newLeaveDetail);
								log.info("Carry Forward: Yes");
							} else {
								log.info(
										"Employee either has no leave available or not found for the current year. ===> Skipping <===");
								log.info(" Carry Forward: No ");
							}

							log.info("*********************************************************************");
						}
					} else {
						log.info("No employees found with service completion leave from the previous year.");
					}

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			log.info("Exiting addServiceCompletionLeavesYearEnd method.");
			return null;
		} catch (HRMSException e) {
			log.error("HRMSException: " + e.getMessage(), e);
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			} catch (Exception e1) {
				log.error("Error sending error response.", e1);
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			log.error("Unknown Exception: " + unknown.getMessage(), unknown);
			try {
				return HRMSHelper.sendErrorResponse(unknown.getMessage(), IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				log.error("Error sending error response.", e1);
				e1.printStackTrace();
			}
		}
		return null;
	}

}
