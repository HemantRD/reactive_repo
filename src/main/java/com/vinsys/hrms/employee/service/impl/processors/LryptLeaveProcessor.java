package com.vinsys.hrms.employee.service.impl.processors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
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
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.constants.ELeaveTypeCode;
import com.vinsys.hrms.constants.EResponse;
import com.vinsys.hrms.dashboard.DashboardTransformUtils;
import com.vinsys.hrms.dashboard.vo.EmployeeLeaveSumarryDetailsVO;
import com.vinsys.hrms.dashboard.vo.LeaveSumarryDetailsResponseVO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.VOAppliedLeaveCount;
import com.vinsys.hrms.datamodel.VOHolidayResponse;
import com.vinsys.hrms.datamodel.VOLeaveCalculationRequest;
import com.vinsys.hrms.employee.vo.ApplyGrantLeaveRequestVO;
import com.vinsys.hrms.employee.vo.ApplyLeaveRequestVO;
import com.vinsys.hrms.employee.vo.AvailableLeavesVO;
import com.vinsys.hrms.employee.vo.EmployeeLeaveDetailsVO;
import com.vinsys.hrms.employee.vo.EmployeeListVO;
import com.vinsys.hrms.employee.vo.LeaveCalculationRequestVO;
import com.vinsys.hrms.employee.vo.ProfileVO;
import com.vinsys.hrms.employee.vo.SubOrdinateListVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCreditAppliedLeaveMapping;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeGrantLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.vo.MasterLeavePolicyVO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;
import com.vinsys.hrms.util.LeaveTransformUtil;
import com.vinsys.hrms.util.LogConstants;
import com.vinsys.hrms.util.PersonalDetailsTransformUtils;
import com.vinsys.hrms.util.ResponseCode;

public class LryptLeaveProcessor extends AbstractLeaveProcessor {

	@Value("${app_version}")
	String applicationVersion;
//	@Value("${base.url}")
//	private String baseURL;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private LeaveProcessorDependencies leaveProcessorDependencies;

	public LryptLeaveProcessor(LeaveProcessorDependencies leaveProcessorDependencies) {
		super(leaveProcessorDependencies);
		this.leaveProcessorDependencies = leaveProcessorDependencies;
	}

	@Override
	public HRMSBaseResponse<?> applyLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException, ParseException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "applyLeave");
		}
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = null;
		employee = checkIfEmpIdNull(empId, employee);
		Long orgId = employee.getCandidate().getLoginEntity().getOrganization().getId();
		leaveProcessorDependencies.getEmployeeAuthorityHelper().applyLeaveInputValidation(employee, request);
		MasterLeaveType leaveType = leaveProcessorDependencies.getMstLeaveTypeDAO().findByIdAndIsActiveAndOrgId(
				request.getLeaveApplied().getLeaveTypeId(), IHRMSConstants.isActive, orgId);
		if (HRMSHelper.isNullOrEmpty(leaveType)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201) + "- Leave Type");
		}
		checkIfApplyLeaveOnHolidayOrWeekOff(request, employee, leaveType);
		MasterLeavePolicyVO leavePolicy = leaveProcessorDependencies.getMasterService()
				.getLeavePolicy(leaveType.getId());
		leaveProcessorDependencies.getEmployeeAuthorityHelper().validateLeavePolicy(employee, request, leavePolicy);

		LeaveCalculationRequestVO calculationRequest = setCalculationVo(request, employee, leaveType);
		HRMSBaseResponse calculateLeaveResponse = calculateLeave(calculationRequest);

		VOAppliedLeaveCount calculateLeave = (VOAppliedLeaveCount) calculateLeaveResponse.getResponseBody();

		if (request.getLeaveApplied().getNoOfDays() != calculateLeave.getCalculatedLeave()) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + "- Incorrect no.of days calculation");
		}
		EmployeeLeaveApplied leavesAppliedEnity;
		boolean validLeaveFlag;
		Date toDate = validateApplyLeave(request, empId, employee, leaveType);

		if (!HRMSHelper.isNullOrEmpty(employee)) {
			if (employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 3) {
				divisionThreeCheck(toDate);
			} else {
				checkForAllOtherDivisions(leaveType, toDate);
			}
		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		validLeaveFlag = validateLeave(request, empId);
		if (validLeaveFlag) {
			leaveTypeValidations(request, empId, leaveType);
		} else {
			log.info("*********** INVALID Leave ***********");
			throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.LeaveOverlapsMessage);
		}

		setResponseApplyLeave(response);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "applyLeave");
		}
		return response;

	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getLeaveDetailsProcess(Long employeeId, Pageable pageable)
			throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "getLeaveDetail");
		}
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Employee employee = leaveProcessorDependencies.getEmployeeDAO().findByEmpIdAndOrgId(empId, orgId);

		EmployeeLeaveDetailsVO leaveDetailsVO = null;
		leaveDetailsVO = leaveDetailsForEmployee(pageable, empId, employee, leaveDetailsVO, response);

		response.setResponseBody(leaveDetailsVO);
		setLeaveResponse(response);

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "getLeaveDetail");
		}
		return response;
	}

	private void setLeaveResponse(HRMSBaseResponse<EmployeeLeaveDetailsVO> response) {
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(leaveProcessorDependencies.getApplicationVersion());
	}

	private EmployeeLeaveDetailsVO leaveDetailsForEmployee(Pageable pageable, Long empId, Employee employee,
			EmployeeLeaveDetailsVO leaveDetailsVO, HRMSBaseResponse<EmployeeLeaveDetailsVO> response)
			throws HRMSException {
		long totalRecord;
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			List<EmployeeLeaveApplied> leaveApplied = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
					.getAppliedLeaveDetailsYearlyByLeaveStatus(empId, year, IHRMSConstants.LeaveStatus_CANCELLED,
							pageable);

			totalRecord = leaveApplied.size();

			totalRecord = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
					.getCountEmployeeAppliedLeaveDetailsYearly(empId, year, IHRMSConstants.LeaveStatus_CANCELLED);

			leaveDetailsVO = LeaveTransformUtil.convertToEmployeeLeaveDetailsVO(leaveApplied);
			response.setTotalRecord(totalRecord);
		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EmployeeDoesnotExistMessage);
		}
		return leaveDetailsVO;
	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetailsProcess(Long filterEmployeeId, Integer year,
			Pageable pageable) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "getTeamLeaveDetails");
		}
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse<EmployeeLeaveDetailsVO>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Employee employee = leaveProcessorDependencies.getEmployeeDAO().findByEmpIdAndOrgId(empId, orgId);
		EmployeeLeaveDetailsVO leaveDetailsVO = null;
		long totalRecord = 0;
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int Year = 0;
			if (!HRMSHelper.isNullOrEmpty(year)) {
				Year = year;
			} else {
				Year = calendar.get(Calendar.YEAR);
			}

			List<EmployeeLeaveApplied> leaveApplied = new ArrayList<>();

			if (!HRMSHelper.isNullOrEmpty(filterEmployeeId)) {
				Employee filteredId = leaveProcessorDependencies.getEmployeeDAO().findById(filterEmployeeId).get();
				Employee filterEmployee = leaveProcessorDependencies.getEmployeeDAO()
						.findByEmpIdAndOrgId(filterEmployeeId, filteredId.getOrgId());
				if (empId.equals(filterEmployee.getEmployeeReportingManager().getReporingManager().getId())) {
					leaveApplied = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
							.findSubordinateLeaveAppliedDetailsForManagerOrderByStatusAndEmployee(
									IHRMSConstants.isActive, empId, IHRMSConstants.LeaveStatus_CANCELLED, Year,
									pageable, filterEmployeeId);

					totalRecord = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
							.countOfSubordinateLeaveAppliedDetailsForManagerOrderByStatusAndEmployee(
									IHRMSConstants.isActive, empId, IHRMSConstants.LeaveStatus_CANCELLED, Year,
									filterEmployeeId);
				}

			} else {
				leaveApplied = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
						.findSubordinateLeaveAppliedDetailsForManagerOrderByStatus(IHRMSConstants.isActive, empId,
								IHRMSConstants.LeaveStatus_CANCELLED, Year, pageable);

				totalRecord = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
						.countOfSubordinateLeaveAppliedDetailsForManagerOrderByStatus(IHRMSConstants.isActive, empId,
								IHRMSConstants.LeaveStatus_CANCELLED, Year);

			}

			leaveDetailsVO = LeaveTransformUtil.convertToEmployeeLeaveDetailsVO(leaveApplied);

		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EmployeeDoesnotExistMessage);
		}

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "getTeamLeaveDetails");
		}
		response.setResponseBody(leaveDetailsVO);
		response.setTotalRecord(totalRecord);
		setLeaveResponse(response);
		return response;
	}

	private void leaveTypeValidations(ApplyLeaveRequestVO request, Long empId, MasterLeaveType leaveType)
			throws HRMSException, ParseException {
		EmployeeLeaveApplied leavesAppliedEnity;
		if (leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("LMATR")) {
			ifLeaveAppliedMaternity(request, empId, leaveType);
		} else if (leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("LPATR")) {
			ifLeaveAppliedPaternity(request, empId, leaveType);
		} else {
			log.info("*********** VALID Leave ***********");
			leavesAppliedEnity = leaveApplyAction(request, leaveType, empId);
		}
	}

	private void ifLeaveAppliedPaternity(ApplyLeaveRequestVO request, Long empId, MasterLeaveType leaveType)
			throws HRMSException, ParseException {
		EmployeeLeaveApplied leavesAppliedEnity;
		if (request.getLeaveApplied().getNoOfDays() <= IHRMSConstants.PATR_LEAVE_VALID_DAYS) {
			List<EmployeeLeaveApplied> patrleavesAppliedByEmpList = leaveProcessorDependencies
					.getEmployeeLeaveAppliedDAO().findAllLeavesByemployeeAndmasterLeaveType(empId, leaveType.getId(),
							IHRMSConstants.LeaveStatus_CANCELLED, IHRMSConstants.LeaveStatus_WD_APPROVED,
							IHRMSConstants.LeaveStatus_REJECT);
			if (patrleavesAppliedByEmpList.size() <= 1) {
				log.info("*********** VALID Paternity Leave ***********");
				leavesAppliedEnity = leaveApplyAction(request, leaveType, empId);
			} else {
				log.info("*********** Invalid Paternity Leave ***********");
				throw new HRMSException(IHRMSConstants.InValidDetailsCode, IHRMSConstants.PaternityLeaveAvailed2Times);
			}

		} else {
			log.info("*********** Invalid Paternity Leave ***********");
			throw new HRMSException(IHRMSConstants.InValidDetailsCode, IHRMSConstants.PaternityLeaveOverExceedsLimit);
		}
	}

	private void ifLeaveAppliedMaternity(ApplyLeaveRequestVO request, Long empId, MasterLeaveType leaveType)
			throws HRMSException, ParseException {
		EmployeeLeaveApplied leavesAppliedEnity;
		if (request.getLeaveApplied().getNoOfDays() <= IHRMSConstants.MATR_LEAVE_VALID_DAYS) {
			log.info("*********** VALID Maternity Leave ***********");
			leavesAppliedEnity = leaveApplyAction(request, leaveType, empId);
		} else {
			log.info("*********** Invalid Maternity Leave ***********");
			throw new HRMSException(IHRMSConstants.InValidDetailsCode, IHRMSConstants.MaternityLeaveOverExceedsLimit);
		}
	}

	private Date validateApplyLeave(ApplyLeaveRequestVO request, Long empId, Employee employee,
			MasterLeaveType leaveType) throws HRMSException {
		Date fromDate = HRMSDateUtil.parse(request.getLeaveApplied().getFromDate(),
				IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date toDate = HRMSDateUtil.parse(request.getLeaveApplied().getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		EmployeeLeaveApplied leavesAppliedEnity = null;
		boolean validLeaveFlag = false;
		Date employeeJoiningDate = employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining();
		if (fromDate.before(employeeJoiningDate)) {
			throw new HRMSException(1506, ResponseCode.getResponseCodeMap().get(1598));
		}
		if (leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("LONDT")
				|| leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("LWRHM")) {
			checkIfOnDutyOrWorkFromHome(fromDate, toDate);
		}
		if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied()) && !HRMSHelper.isNullOrEmpty(empId)
				&& !HRMSHelper.isNullOrEmpty(leaveType) && !HRMSHelper.isNullOrEmpty(leaveType.getLeaveTypeCode())) {
			checkApplyLeaveForProbationEmployee(employee, leaveType);
		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}
		return toDate;
	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetailsProcess(Pageable pageable, Long employeeId)
			throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "getGrantLeaveDetail");
		}
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Employee employee = leaveProcessorDependencies.getEmployeeDAO().findByEmpIdAndOrgId(empId, orgId);

		EmployeeLeaveDetailsVO leaveDetailsVO = null;
		long totalRecord = 0;

		List<EmployeeGrantLeaveDetail> employeeGrantLeaveDetails = new ArrayList<EmployeeGrantLeaveDetail>();

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		leaveDetailsVO = teamLeaveDetail(pageable, employeeId, empId, leaveDetailsVO, employeeGrantLeaveDetails, year);

		response.setResponseBody(leaveDetailsVO);
		response.setTotalRecord(totalRecord);
		setLeaveResponse(response);

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "getGrantLeaveDetail");
		}
		return response;
	}

	private EmployeeLeaveDetailsVO teamLeaveDetail(Pageable pageable, Long employeeId, Long empId,
			EmployeeLeaveDetailsVO leaveDetailsVO, List<EmployeeGrantLeaveDetail> employeeGrantLeaveDetails, int year)
			throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(employeeId)) {

			employeeGrantLeaveDetails = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
					.findSubordinateLeaveAppliedDetailsForEmployee(IHRMSConstants.isActive, IHRMSConstants.isActive,
							empId, IHRMSConstants.LeaveStatus_PENDING, IHRMSConstants.LeaveStatus_WD_PENDING, year,
							employeeId);
			leaveDetailsVO = LeaveTransformUtil.convertToEmployeeGrantLeaveDetailsVO(employeeGrantLeaveDetails);

		} else if (HRMSHelper.isNullOrEmpty(employeeId)) {

			employeeGrantLeaveDetails = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
					.findSubordinateLeaveAppliedDetailsForManager(IHRMSConstants.isActive, IHRMSConstants.isActive,
							empId, IHRMSConstants.LeaveStatus_PENDING, IHRMSConstants.LeaveStatus_WD_PENDING, year,
							pageable);
			leaveDetailsVO = LeaveTransformUtil.convertToEmployeeGrantLeaveDetailsVO(employeeGrantLeaveDetails);
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		if (HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetails)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		return leaveDetailsVO;
	}

	private void checkApplyLeaveForProbationEmployee(Employee employee, MasterLeaveType leaveType)
			throws HRMSException {
		// checking if applied leave is of type probation
		if (leaveType.getLeaveTypeCode().trim().toUpperCase()
				.equalsIgnoreCase(IHRMSConstants.PROBATION_MASTER_LEAVE_CODE.toUpperCase())) {
			if (!HRMSHelper.isNullOrEmpty(employee) && !HRMSHelper.isNullOrEmpty(employee.getCandidate())
					&& !HRMSHelper.isNullOrEmpty(employee.getCandidate().getEmploymentType())) {
				MasterEmploymentType mstEmpType = leaveProcessorDependencies.getMasterEmploymentTypeDAO()
						.findById(employee.getCandidate().getEmploymentType().getId()).get();
				// checking whether employee is confirmed
				if ((mstEmpType.getEmploymentTypeDescription().toUpperCase()
						.equalsIgnoreCase(IHRMSConstants.EMPLOYMENT_TYPE_CONFIRMED.toUpperCase())
						|| mstEmpType.getEmploymentTypeDescription().toUpperCase()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYMENT_TYPE_EMPLOYEE.toUpperCase())
						|| mstEmpType.getEmploymentTypeDescription().toUpperCase()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYMENT_TYPE_PERMANENT.toUpperCase()))) {

					if (employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() != 3) {
						throw new HRMSException(IHRMSConstants.CONFIRMED_EMP_PROB_LEAVE_RESTRICT_CODE,
								IHRMSConstants.CONFIRMED_EMP_PROB_LEAVE_RESTRICT_MESSAGE);
					}

				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		}
	}

	private void setResponseApplyLeave(HRMSBaseResponse<?> response) {
		response.setResponseCode(1200);
		response.setResponseMessage(IHRMSConstants.LeaveActionSuccessMessage);
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
	}

	private void checkIfApplyLeaveOnHolidayOrWeekOff(ApplyLeaveRequestVO request, Employee employee,
			MasterLeaveType leaveType) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(leaveType)) {
			if (!(leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("PATR")
					|| leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("MATR"))) {
				isApplyOnHolidayOrWeekOff(employee, request);
			}
		}
	}

	private Employee checkIfEmpIdNull(Long empId, Employee employee) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(empId)) {
			employee = leaveProcessorDependencies.getEmployeeDAO().findByEmpIdAndOrgId(empId,
					SecurityFilter.TL_CLAIMS.get().getOrgId());
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		return employee;
	}

	private void checkForAllOtherDivisions(MasterLeaveType leaveType, Date toDate) throws HRMSException {
		Date incrementedDate = HRMSDateUtil.setTimeStampToZero(HRMSDateUtil.incByMonth(new Date(), 1));
		System.out.println(incrementedDate);
		System.out.println(incrementedDate.compareTo(toDate));
		if (incrementedDate.compareTo(toDate) < 0) {
			if (!leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("MATR")) {
				throw new HRMSException(IHRMSConstants.InValidDetailsCode,
						IHRMSConstants.LEAVE_RESTRICTION_30_DAYS_MESSAGE);
			}
		}
	}

	private void divisionThreeCheck(Date toDate) throws HRMSException {
		Date incrementedDate = HRMSDateUtil.setTimeStampToZero(HRMSDateUtil.incByMonth(new Date(), 6));
		System.out.println(incrementedDate);
		System.out.println(incrementedDate.compareTo(toDate));
		if (incrementedDate.compareTo(toDate) < 0) {
			throw new HRMSException(IHRMSConstants.InValidDetailsCode,
					IHRMSConstants.LEAVE_RESTRICTION_6_MONTHS_MESSAGE);
		}
	}

	private LeaveCalculationRequestVO setCalculationVo(ApplyLeaveRequestVO request, Employee employee,
			MasterLeaveType leaveType) {
		LeaveCalculationRequestVO calculationRequest = new LeaveCalculationRequestVO();
		calculationRequest.setFromDate(request.getLeaveApplied().getFromDate());
		calculationRequest.setToDate(request.getLeaveApplied().getToDate());
		calculationRequest.setToSession(request.getLeaveApplied().getToSession());
		calculationRequest.setLeaveTypeId(request.getLeaveApplied().getLeaveTypeId());
		calculationRequest.setOrganizationId(employee.getCandidate().getLoginEntity().getOrganization().getId());
		calculationRequest.setNumberOfSession(leaveType.getNumberOfSession());
		calculationRequest.setFromSession(request.getLeaveApplied().getFromSession());
		return calculationRequest;
	}

	private void checkIfOnDutyOrWorkFromHome(Date fromDate, Date toDate) throws HRMSException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 10);
		Date futureDate = cal.getTime();

		System.out.println("On Duty/WFH...............");

		if (fromDate.compareTo(futureDate) > 0) {
			throw new HRMSException(IHRMSConstants.TEN_DAYS_LEAVE_RESTRICT_CODE,
					IHRMSConstants.TEN_DAYS_LEAVE_RESTRICT_MESSAGE);
		}
		long dateDiff = (toDate.getTime() - fromDate.getTime()) / 1000 / 60 / 60 / 24;

		System.out.println("Days: " + dateDiff);
		if (dateDiff >= 10) {
			throw new HRMSException(IHRMSConstants.MAX_LEAVE_RESTRICT_CODE, IHRMSConstants.MAX_LEAVE_RESTRICT_MESSAGE);
		}
	}

	private void isApplyOnHolidayOrWeekOff(Employee employee, ApplyLeaveRequestVO request) throws HRMSException {

		List<OrganizationWeekoff> weekoffList = leaveProcessorDependencies.getOrganizationWeekoffDAO()
				.getWeekoffByOrgBranchDivDeptId(employee.getCandidate().getLoginEntity().getOrganization().getId(),
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getCandidateProfessionalDetail().getBranch().getId(),
						employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getId());

		List<OrganizationHoliday> holidayList = leaveProcessorDependencies.getOrganizationHolidayDAO()
				.getHolidayListByOrgBranchDivIdYear(employee.getCandidate().getLoginEntity().getOrganization().getId(),
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getCandidateProfessionalDetail().getBranch().getId(),
						new Long(HRMSDateUtil.getCurrentYear()));

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		List<String> dayNamesList = new ArrayList<>();
		List<String> dateList = new ArrayList<>();
		Date fromDate = HRMSDateUtil.parse(request.getLeaveApplied().getFromDate(),
				IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date toDate = HRMSDateUtil.parse(request.getLeaveApplied().getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		Calendar calFromDate = Calendar.getInstance();
		Calendar calToDate = Calendar.getInstance();
		calFromDate.setTime(fromDate);
		calToDate.setTime(toDate);
		while (!calFromDate.after(calToDate)) {
			Date currentDate = calFromDate.getTime();
			String dayName = new SimpleDateFormat("EEEE").format(currentDate); // "EEEE" gives the full day name
			dayNamesList.add(dayName);
			String formattedDate = sdf.format(currentDate);
			dateList.add(formattedDate);

			calFromDate.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
		}

		for (String dayName : dayNamesList) {
			if (weekoffList.get(0).getWeekoffDays().toUpperCase().contains(dayName.toUpperCase())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501)
						+ "- This type of leave cannot be applied on Weekly off and Holidays.");
			}
			for (OrganizationHoliday date : holidayList) {
				String holidaydate = sdf.format(date.getHolidayDate());
				if (dateList.contains(holidaydate)) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501)
							+ "- This type of leave cannot be applied on Weekly off and Holidays.");
				}
			}
		}

	}

	private boolean validateLeave(ApplyLeaveRequestVO requestedLeave, Long empId) {

//		MasterLeaveType leaveType = leaveProcessorDependencies.getMstLeaveTypeDAO()
//				.findByIdAndIsActive(requestedLeave.getLeaveApplied().getLeaveTypeId(), IHRMSConstants.isActive);
		MasterLeaveType leaveType = leaveProcessorDependencies.getMstLeaveTypeDAO().findByIdAndIsActiveAndOrgId(
				requestedLeave.getLeaveApplied().getLeaveTypeId(), IHRMSConstants.isActive,
				SecurityFilter.TL_CLAIMS.get().getOrgId());
		Map<String, String> mapOfAppliedLeaves = new HashMap<String, String>();

		List<EmployeeLeaveApplied> appliedLeaves = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
				.findAppliedLeavesOfEmployeeFromLastFiveDaysAndOrgId(empId, IHRMSConstants.LeaveStatus_CANCELLED,
						IHRMSConstants.LeaveStatus_WD_APPROVED, IHRMSConstants.LeaveStatus_REJECT,
						SecurityFilter.TL_CLAIMS.get().getOrgId());

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
			log.info("Count" + count);
			int start = 0;
			int end = 0;
			String startStr = fromSession.trim().substring(fromSession.trim().length() - 1);
			start = Integer.parseInt(startStr);
			String endStr = toSession.trim().substring(toSession.trim().length() - 1);
			end = Integer.parseInt(endStr);
			// System.out.println("*********Row START**********");
			if (fromDate.compareTo(toDate) <= 0) {

				validateApplyLeaveDates(mapOfAppliedLeaves, count, fromDate, toDate, toSession, start);

			}
			count = 0;

		}

		Date reqFromDate = HRMSDateUtil.parse(requestedLeave.getLeaveApplied().getFromDate(),
				IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date reqToDate = HRMSDateUtil.parse(requestedLeave.getLeaveApplied().getToDate(),
				IHRMSConstants.FRONT_END_DATE_FORMAT);
		String reqFromSession = requestedLeave.getLeaveApplied().getFromSession().trim()
				.substring(requestedLeave.getLeaveApplied().getFromSession().trim().length() - 1);
		String reqToSession = requestedLeave.getLeaveApplied().getToSession().trim()
				.substring(requestedLeave.getLeaveApplied().getToSession().trim().length() - 1);

//		MasterLeaveType masterLeaveType = leaveProcessorDependencies.getMasterLeaveTypeDAO().findById(leaveType.getId())
//				.get();
		MasterLeaveType masterLeaveType = leaveProcessorDependencies.getMstLeaveTypeDAO().findByIdAndIsActiveAndOrgId(
				leaveType.getId(), IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());
		if (masterLeaveType != null && masterLeaveType.getLeaveGrantStatus() != null
				&& masterLeaveType.getLeaveGrantStatus().equalsIgnoreCase("Y")) {

			return ifLeaveGrantStatusY(mapOfAppliedLeaves, reqFromDate, reqToDate, reqFromSession, reqToSession);
		} else {
			return ifLeaveGrantStatusN(mapOfAppliedLeaves, reqFromDate, reqToDate, reqFromSession, reqToSession);
		}

	}

	private void validateApplyLeaveDates(Map<String, String> mapOfAppliedLeaves, long count, Date fromDate, Date toDate,
			String toSession, int start) {
		int end;
		String endStr;
		for (int i = 0; i < count; i++) {
			// logger.info("i::"+ i + "Count::"+count);

			if (fromDate.compareTo(toDate) == 0) {
				endStr = toSession.trim().substring(toSession.trim().length() - 1);
				end = Integer.parseInt(endStr);
			} else {
				end = 4;
			}

			for (int k = start; k <= end; k++) {

				String key = HRMSDateUtil.format(fromDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
						+ String.valueOf(start);
				++start;
				++i;
				log.info("Key" + key);
				mapOfAppliedLeaves.put(key, "");

			}
			--i;
			start = 1;
			fromDate = HRMSDateUtil.incDate(fromDate, 1);
		}
	}

	private boolean ifLeaveGrantStatusN(Map<String, String> mapOfAppliedLeaves, Date reqFromDate, Date reqToDate,
			String reqFromSession, String reqToSession) {
		String fromKey = HRMSDateUtil.format(reqFromDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
				+ String.valueOf(Integer.parseInt(reqFromSession));
		String toKey = HRMSDateUtil.format(reqToDate, IHRMSConstants.FRONT_END_DATE_FORMAT) + "_" + "Session"
				+ String.valueOf(Integer.parseInt(reqToSession));
		if (mapOfAppliedLeaves.containsKey(fromKey) || mapOfAppliedLeaves.containsKey(toKey))
			return false;
		else
			return true;
	}

	private boolean ifLeaveGrantStatusY(Map<String, String> mapOfAppliedLeaves, Date reqFromDate, Date reqToDate,
			String reqFromSession, String reqToSession) {
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
	}

	private EmployeeLeaveApplied leaveApplyAction(ApplyLeaveRequestVO request, MasterLeaveType leaveType, Long empId)
			throws HRMSException, ParseException {

		EmployeeLeaveApplied leavesAppliedEnity;
		log.info(" == ACTION -> LEAVE_APPLY << ==");
		Employee employee = leaveProcessorDependencies.employeeDAO.findByEmpIdAndOrgId(empId,
				SecurityFilter.TL_CLAIMS.get().getOrgId());
		EmployeeLeaveDetail employeeLeaveDetail = null;

		if (HRMSHelper.isNullOrEmpty(employee)) {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EmployeeDoesnotExistMessage);
		}

		// MasterLeaveType masterLeaveType =
		// leaveProcessorDependencies.leaveTypeDAO.findById(leaveType.getId()).get();
		MasterLeaveType masterLeaveType = leaveProcessorDependencies.getMstLeaveTypeDAO().findByIdAndIsActiveAndOrgId(
				request.getLeaveApplied().getLeaveTypeId(), IHRMSConstants.isActive,
				SecurityFilter.TL_CLAIMS.get().getOrgId());
		if (HRMSHelper.isNullOrEmpty(masterLeaveType)) {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.LeaveTypeDosentExist);
		}

		if (masterLeaveType.getIsLop().equalsIgnoreCase(IHRMSConstants.isActive)) {
			ifLeaveTypeLOP(leaveType, employee, masterLeaveType);
		}

		employeeLeaveDetail = leaveProcessorDependencies.empLeaveDetailsDAO.findEmployeeLeaveByEIDYEARAndOrgId(
				employee.getId(), leaveType.getId(), HRMSDateUtil.getCurrentYear(),
				SecurityFilter.TL_CLAIMS.get().getOrgId());

		if (employeeLeaveDetail == null) {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode,
					IHRMSConstants.YouCannotApplyTheSelectedLeaveMessage);
		}

		leavesAppliedEnity = HRMSRequestTranslator.translateToApplyLeaveDetailsEntity(request);
		leavesAppliedEnity.setMasterLeaveType(masterLeaveType);
		leavesAppliedEnity.setEmployee(employee);
		leavesAppliedEnity.setOrgId(employee.getCandidate().getLoginEntity().getOrganization().getId());

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

		if (!leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode().equalsIgnoreCase(ELeaveTypeCode.LOPL.name())
				&& !leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
						.equalsIgnoreCase(ELeaveTypeCode.ONDT.name())
				&& !leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
						.equalsIgnoreCase(ELeaveTypeCode.WRHM.name())) {
			leaveBalance = leaveAvailable - leaveApplied;
		}

		if (leaveApplied > leaveAvailable && !masterLeaveType.getIsLop().equalsIgnoreCase("Y")) {
			throw new HRMSException(IHRMSConstants.LEAVE_APPLIED_GREATER_THAN_LEAVE_AVAILABLE_CODE,
					IHRMSConstants.LEAVE_APPLIED_GREATER_THAN_LEAVE_AVAILABLE_MESSAGE);
		}
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
			log.info(" == >> Leave Auto APPROVED << == ");
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_AUTO_APPROVED);
			leavesAppliedEnity.setDateOfApproverAction(new Date());
			leavesAppliedEnity.setRemark(IHRMSConstants.LeaveRemarkForAutoApproved);

			employeeLeaveDetail.setNumberOfDaysAvailed(totalAvailedLeave);
			employeeLeaveDetail.setLeaveAvailable(leaveBalance);

			leavesAppliedEnity = leaveProcessorDependencies.employeeLeaveAppliedDAO.save(leavesAppliedEnity);
			leaveProcessorDependencies.empLeaveDetailsDAO.save(employeeLeaveDetail);

			String managerEmailId = employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
			String employeeEmailID = employee.getOfficialEmailId();
			String ccEmailId = employeeEmailID + ";" + request.getLeaveApplied().getCc();
			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{rootIp}", leaveProcessorDependencies.baseURL);
			placeHolderMapping.put("{websiteURL}", leaveProcessorDependencies.baseURL);
			String mailContent_cc = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_LeaveApply_CC);
			// System.out.println("********************&&&&&&"+employeeLeaveDetail.getMasterLeaveType().getLeaveGrantStatus()+"&&&&&&&***********************");
			if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("On Duty")) {
				leaveProcessorDependencies.emailsender.toPersistEmail(managerEmailId, ccEmailId, mailContent_cc,
						IHRMSConstants.MailSubject_OnDutyApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("Work From Home")) {
				leaveProcessorDependencies.emailsender.toPersistEmail(managerEmailId, ccEmailId, mailContent_cc,
						IHRMSConstants.MailSubject_WorkFromHomeApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else {
				leaveProcessorDependencies.emailsender.toPersistEmail(managerEmailId, ccEmailId, mailContent_cc,
						IHRMSConstants.MailSubject_LeaveApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			}

		} else {

			employeeLeaveDetail.setNumberOfDaysAvailed(totalAvailedLeave);
			employeeLeaveDetail.setLeaveAvailable(leaveBalance);

			leavesAppliedEnity = leaveProcessorDependencies.employeeLeaveAppliedDAO.save(leavesAppliedEnity);
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_PENDING);
			leavesAppliedEnity = leaveProcessorDependencies.employeeLeaveAppliedDAO.save(leavesAppliedEnity);
			leaveProcessorDependencies.empLeaveDetailsDAO.save(employeeLeaveDetail);

			// Email sender

			String managerEmailId = employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
			String ccEmailId = request.getLeaveApplied().getCc();

			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{rootIp}", leaveProcessorDependencies.baseURL + "/api");
			placeHolderMapping.put("{websiteURL}", leaveProcessorDependencies.baseURL);

			String mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_LeaveApply);

			// Sending email to Recipient
			if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("On Duty")) {
				leaveProcessorDependencies.emailsender.toPersistEmail(managerEmailId, null, mailContent,
						IHRMSConstants.MailSubject_OnDutyApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("Work From Home")) {
				leaveProcessorDependencies.emailsender.toPersistEmail(managerEmailId, null, mailContent,
						IHRMSConstants.MailSubject_WorkFromHomeApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else {
				leaveProcessorDependencies.emailsender.toPersistEmail(managerEmailId, null, mailContent,
						IHRMSConstants.MailSubject_LeaveApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			}

			// EmailSender.toSendEmail(managerEmailId, null, mailContent,
			// IHRMSConstants.MailSubject_LeaveApplication);

			// Sending email to CC

			String[] res = null;
			if (!HRMSHelper.isNullOrEmpty(ccEmailId)) {
				res = ccEmailId.split("[;]", 0);
			}

			if (!HRMSHelper.isNullOrEmpty(res)) {
				for (String myStr : res) {
					String mailContent_cc = HRMSHelper.replaceString(placeHolderMapping,
							IHRMSEmailTemplateConstants.Template_LeaveApply_CC);
					if (!HRMSHelper.isNullOrEmpty(myStr) && (!myStr.equals(managerEmailId))) {
						leaveProcessorDependencies.emailsender.toPersistEmail(myStr, null, mailContent_cc,
								IHRMSConstants.MailSubject_LeaveApplication,
								employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
								employee.getCandidate().getLoginEntity().getOrganization().getId());
					}
				}
			}

		}
		return leavesAppliedEnity;
	}

	private void ifLeaveTypeLOP(MasterLeaveType leaveType, Employee employee, MasterLeaveType masterLeaveType) {
		EmployeeLeaveDetail employeeLeaveDetail;
		Long orgId = employee.getCandidate().getLoginEntity().getOrganization().getId();
		employeeLeaveDetail = leaveProcessorDependencies.empLeaveDetailsDAO.findEmployeeLeaveByEIDYEARAndOrgId(
				employee.getId(), leaveType.getId(), HRMSDateUtil.getCurrentYear(), orgId);
		if (employeeLeaveDetail == null) {
			employeeLeaveDetail = new EmployeeLeaveDetail();
			employeeLeaveDetail.setEmployee(employee);
			employeeLeaveDetail.setMasterLeaveType(masterLeaveType);
			employeeLeaveDetail.setYear(HRMSDateUtil.getCurrentYear());

			employeeLeaveDetail.setOrgId(orgId);
			employeeLeaveDetail = leaveProcessorDependencies.empLeaveDetailsDAO.save(employeeLeaveDetail);
		} else {

		}
	}

	@Override
	protected int applyCompOffAction(EmployeeLeaveApplied leavesAppliedEnity) throws ParseException {

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
						Date today = new Date();
						Date todaysDate = HRMSDateUtil.formatDate(today, IHRMSConstants.POSTGRE_DATE_FORMAT);
						if ((employeeLeaveApplied.getFromDate().before(credLeaveDetailsSessionWise.getToDate())
								|| employeeLeaveApplied.getFromDate().equals(credLeaveDetailsSessionWise.getToDate()))
								&& (todaysDate.before(credLeaveDetailsSessionWise.getToDate())
										|| todaysDate.equals(credLeaveDetailsSessionWise.getToDate()))) {

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
						EmployeeCreditLeaveDetail empCreditLeaveDetailEntity = leaveProcessorDependencies.creditLeaveDAO
								.findById(employeeCreditLeaveDetail.getId()).get();
						float updatedLeaveAvailable = empCreditLeaveDetailEntity.getLeaveAvailable()
								- IHRMSConstants.COMP_OFF_SESSION_VALUE;
						empCreditLeaveDetailEntity.setLeaveAvailable(updatedLeaveAvailable);
						empCreditLeaveDetailEntity.setUpdatedBy(leavesAppliedEnity.getEmployee().getId().toString());
						empCreditLeaveDetailEntity.setUpdatedDate(new Date());
						leaveProcessorDependencies.creditLeaveDAO.save(empCreditLeaveDetailEntity);
						// add mapping of credit leave and leave applied
						EmployeeCreditAppliedLeaveMapping creditAppliedLeaveMapping = new EmployeeCreditAppliedLeaveMapping();
						creditAppliedLeaveMapping.setEmployeeLeaveApplied(leavesAppliedEnity);
						creditAppliedLeaveMapping.setCreditLeaveDetail(empCreditLeaveDetailEntity);
						creditAppliedLeaveMapping.setCreatedBy(leavesAppliedEnity.getEmployee().getId().toString());
						creditAppliedLeaveMapping.setCreatedDate(new Date());
						creditAppliedLeaveMapping.setIsActive(IHRMSConstants.isActive);
						leaveProcessorDependencies.creditAppliedLeaveMappingDAO.save(creditAppliedLeaveMapping);
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

	protected List<EmployeeLeaveApplied> splitEmployeeAppliedLeaves(EmployeeLeaveApplied leavesAppliedEnity)
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
			Employee emp = leaveProcessorDependencies.employeeDAO.findById(leavesAppliedEnity.getEmployee().getId())
					.get();
			long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();

			if (calFromDate.before(calToDate)) {
				Boolean isNewDay = true;
				calFromDateLoopingSameDate = calFromDateLooping;

				while (calFromDateLooping.before(calToDatePlusOne)) {
					// checking if current day is week of or holiday
					if (!"WD".equalsIgnoreCase(leaveProcessorDependencies.employeeLeaveDetailsService
							.isWeeklyOffOrHoliday(HRMSDateUtil.format(calFromDateLooping.getTime(),
									IHRMSConstants.FRONT_END_DATE_FORMAT), orgId, emp.getId()))) {
						calFromDateLooping.add(Calendar.DATE, 1);
						continue;
					}
					// creating separate employee leave applied entities day wise
					EmployeeLeaveApplied employeeLeaveApplied = new EmployeeLeaveApplied();
					employeeLeaveApplied.setEmployee(leavesAppliedEnity.getEmployee());
					employeeLeaveApplied.setFromDate(calFromDateLooping.getTime());
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

	private HRMSBaseResponse calculateLeave(LeaveCalculationRequestVO requestVo) throws HRMSException {

		log.info("Inside calculateLeave method");
		// input validation
		leaveProcessorDependencies.getEmployeeAuthorityHelper().calculateLeaveInputValidation(requestVo);
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		System.out.println("::::" + requestVo.toString());

		HRMSBaseResponse<VOAppliedLeaveCount> response = new HRMSBaseResponse<>();

		// get leaveType
		MasterLeaveType masterLeaveType = new MasterLeaveType();
		if (!HRMSHelper.isNullOrEmpty(requestVo.getLeaveTypeId())) {
			masterLeaveType = leaveProcessorDependencies.getMstLeaveTypeDAO().findByIdAndIsActiveAndOrgId(
					requestVo.getLeaveTypeId(), IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "- LeaveType");
		}

		VOAppliedLeaveCount leaveCount = new VOAppliedLeaveCount();
		Date fromDate = HRMSDateUtil.parse(requestVo.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date toDate = HRMSDateUtil.parse(requestVo.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		long fSession = HRMSHelper.isNullOrEmpty(requestVo.getFromSession()) ? 0
				: Integer.valueOf(requestVo.getFromSession().substring(requestVo.getFromSession().length() - 1));
		long tSession = HRMSHelper.isNullOrEmpty(requestVo.getToSession()) ? 0
				: Integer.valueOf(requestVo.getToSession().substring(requestVo.getToSession().length() - 1));
		long orgId = requestVo.getOrganizationId();

		if (!HRMSHelper.isNullOrEmpty(requestVo)) {

			calculateLeaveIfRequestNotNull(requestVo, empId, masterLeaveType, leaveCount, fromDate, toDate, fSession,
					tSession);
		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}
		response.setResponseMessage(IHRMSConstants.successMessage);
		response.setResponseCode(1200);
		response.setResponseBody(leaveCount);

		log.info("Exit from calculateLeave method");

		return response;

	}

	private void calculateLeaveIfRequestNotNull(LeaveCalculationRequestVO requestVo, Long empId,
			MasterLeaveType masterLeaveType, VOAppliedLeaveCount leaveCount, Date fromDate, Date toDate, long fSession,
			long tSession) throws HRMSException {
		long orgId;
		Employee tempEmployee = leaveProcessorDependencies.getEmployeeDAO()
				.findActiveEmployeeWithCandidateByEmpIdAndOrgId(empId, IHRMSConstants.isActive,
						SecurityFilter.TL_CLAIMS.get().getOrgId());

		long divId = tempEmployee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
		long branchId = !HRMSHelper
				.isNullOrEmpty(tempEmployee.getCandidate().getCandidateProfessionalDetail().getWorkingLocation())
						? tempEmployee.getCandidate().getCandidateProfessionalDetail().getWorkingLocation().getId()
						: tempEmployee.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
		long departmentId = tempEmployee.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
		orgId = tempEmployee.getCandidate().getLoginEntity().getOrganization().getId();

		if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate) && !HRMSHelper.isLongZero(fSession)
				&& !HRMSHelper.isLongZero(tSession) && !HRMSHelper.isNullOrEmpty(orgId)
				&& !HRMSHelper.isNullOrEmpty(divId) && !HRMSHelper.isNullOrEmpty(branchId)
				&& !HRMSHelper.isNullOrEmpty(empId) && !HRMSHelper.isNullOrEmpty(departmentId)) {

			if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate)) {

				long diff = HRMSDateUtil.getDifferenceInDays(fromDate, toDate);
				log.info("*****Total Day Difference::" + diff);

				if (diff >= 0) {
					double totalLeave = 0;

					if (!HRMSHelper.isNullOrEmpty(masterLeaveType)) {

						if (masterLeaveType.getLeaveTypeCode().trim().equalsIgnoreCase("MATR")) {
							totalLeave = diff + 1;
						} else {
							List<OrganizationWeekoff> weekoffList = leaveProcessorDependencies
									.getOrganizationWeekoffDAO()
									.getWeekoffByOrgBranchDivDeptId(orgId, divId, branchId, departmentId);

							fromDate = HRMSDateUtil.setTimeStampToZero(fromDate);
							toDate = HRMSDateUtil.setTimeStampToZero(toDate);
							List<OrganizationHoliday> holidayList = leaveProcessorDependencies
									.getOrganizationHolidayDAO()
									.getHolidayListByOrgBranchDivId(orgId, divId, branchId, fromDate, toDate);

							totalLeave = HRMSHelper.getWorkingDays(requestVo, weekoffList)
									- HRMSHelper.getHolidays(requestVo, holidayList, weekoffList)
									- (HRMSHelper.convertFromSession(requestVo))
									- (HRMSHelper.convertToSession(requestVo));

							HRMSHelper.checkSessionTiming(fromDate, toDate, fSession, tSession,
									requestVo.getNumberOfSession());
						}

					}

					if (totalLeave > 0) {
						log.info("*****Total Calculated Leave::" + totalLeave);

						leaveCount.setCalculatedLeave(totalLeave);
						leaveCount.setResponseCode(IHRMSConstants.successCode);
						leaveCount.setResponseMessage(IHRMSConstants.successMessage);
						// return HRMSHelper.createJsonString(leaveCount);

					} else {
//							
						throw new HRMSException(1506, ResponseCode.getResponseCodeMap().get(1506));
					}
				} else {

					throw new HRMSException(1502, ResponseCode.getResponseCodeMap().get(1502));
				}

			} else {
				throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.NotValidDateMessage);
			}

		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}
	}

	protected Map<Long, Map<Integer, EmployeeCreditLeaveDetail>> getEmployeeCompoffListFromCreditLeave(
			Employee employee) throws HRMSException {
		List<Object> creditLeaveDetails = null;
		List<EmployeeCreditLeaveDetail> creditLeaveDetails2 = null;
		List<EmployeeCreditLeaveDetail> creditLeaveDetailsForMap = null;
		Map<Long, Map<Integer, EmployeeCreditLeaveDetail>> mapCreditLeaveEntityWise = null;
		if (!HRMSHelper.isNullOrEmpty(employee) && !HRMSHelper.isLongZero(employee.getId())) {
			creditLeaveDetails = new ArrayList<Object>();
			creditLeaveDetails2 = new ArrayList<EmployeeCreditLeaveDetail>();
			creditLeaveDetails = (List<Object>) leaveProcessorDependencies.creditLeaveDAO
					.findEmployeeCompoffListFromCreditLeave(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE,
							IHRMSConstants.isActive, IHRMSConstants.isActive, employee.getId());

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

	protected EmployeeCreditLeaveDetail copyCreditLeaveObj(EmployeeCreditLeaveDetail employeeCreditLeaveDetail) {
		EmployeeCreditLeaveDetail employeeCreditLeaveDetailCopy = new EmployeeCreditLeaveDetail();
		employeeCreditLeaveDetailCopy.setId(employeeCreditLeaveDetail.getId());
		employeeCreditLeaveDetailCopy.setLeaveAvailable(employeeCreditLeaveDetail.getLeaveAvailable());
		employeeCreditLeaveDetailCopy.setFromDate(employeeCreditLeaveDetail.getFromDate());
		employeeCreditLeaveDetailCopy.setToDate(employeeCreditLeaveDetail.getToDate());
		return employeeCreditLeaveDetailCopy;
	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getGrantLeaveDetailsProcess(Long employeeId, Pageable pageable)
			throws HRMSException {

		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "getGranLeaveDetail");
		}
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Employee employee = leaveProcessorDependencies.getEmployeeDAO().findByEmpIdAndOrgId(empId, orgId);
		Employee manger = employee.getEmployeeReportingManager().getReporingManager();
		EmployeeLeaveDetailsVO leaveDetailsVO = null;
		long totalRecord = 0;
		List<EmployeeGrantLeaveDetail> employeeGrantLeaveDetails = new ArrayList<EmployeeGrantLeaveDetail>();

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);

		employeeGrantLeaveDetails = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.findAllGrantLeaveDetailsByYear(empId, IHRMSConstants.LeaveStatus_CANCELLED, year,
						IHRMSConstants.isActive, pageable);
		totalRecord = leaveProcessorDependencies.getEmployeeGrantLeaveDAO().countByGrantLeaveDetailsByYear(empId,
				IHRMSConstants.LeaveStatus_CANCELLED, year, IHRMSConstants.isActive);

		leaveDetailsVO = checkEmployeeGrantLeave(employee, manger, leaveDetailsVO, employeeGrantLeaveDetails);
		response.setResponseBody(leaveDetailsVO);
		response.setTotalRecord(totalRecord);
		setLeaveResponse(response);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "getGrantLeaveDetail");
		}
		return response;
	}

	private EmployeeLeaveDetailsVO checkEmployeeGrantLeave(Employee employee, Employee manger,
			EmployeeLeaveDetailsVO leaveDetailsVO, List<EmployeeGrantLeaveDetail> employeeGrantLeaveDetails)
			throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetails)) {
			leaveDetailsVO = LeaveTransformUtil.convertToEmployeeGrantLeaveDetailsVO(employeeGrantLeaveDetails);
			ProfileVO managerProfile = null;
			if (!HRMSHelper.isNullOrEmpty(employee)) {
				managerProfile = PersonalDetailsTransformUtils.convertToProfileDetailsVO(manger);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		return leaveDetailsVO;
	}

	@Override
	public HRMSBaseResponse<SubOrdinateListVO> findSubordinate() throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "findSubordinate");
		}

		HRMSBaseResponse<SubOrdinateListVO> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		long totalRecord = 0;
		Employee emp = leaveProcessorDependencies.getEmployeeDAO().findActiveEmployeeByIdAndOrgId(Long.valueOf(empId),
				IHRMSConstants.isActive, orgId);
		long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();

		Long managerId = empId;
		List<EmployeeListVO> voEmployees = new ArrayList<EmployeeListVO>();
		totalRecord = subOrdinateDetail(response, orgId, totalRecord, managerId, voEmployees);
		response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
		return response;
	}

	private long subOrdinateDetail(HRMSBaseResponse<SubOrdinateListVO> response, long orgId, long totalRecord,
			Long managerId, List<EmployeeListVO> voEmployees) throws HRMSException {
		if (!HRMSHelper.isLongZero(orgId)) {
			if (managerId > 0) {

				List<EmployeeReportingManager> reportingManager = leaveProcessorDependencies.getReportingManagerDAO()
						.findByreporingManager(managerId);
				for (EmployeeReportingManager rptmgr : reportingManager) {
					if (rptmgr.getEmployee().getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {

						EmployeeListVO employeeModel = LeaveTransformUtil.convertToEmployeeModel(rptmgr.getEmployee());
						voEmployees.add(employeeModel);
					}
				}
				totalRecord = leaveProcessorDependencies.getReportingManagerDAO().countfindByreporingManager(managerId,
						orgId);
			} else {

				List<Employee> employees = leaveProcessorDependencies.getEmployeeDAO().getAllEmpNameIdByOrg(orgId);
				if (!HRMSHelper.isNullOrEmpty(employees)) {
					for (Employee employeeEntity : employees) {
						EmployeeListVO employeeModel = LeaveTransformUtil.convertToEmployeeModel(employeeEntity);
						voEmployees.add(employeeModel);
					}
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}
			}
			SubOrdinateListVO employeeList = new SubOrdinateListVO();
			employeeList.setSubOrdinateList(voEmployees);
			response.setResponseBody(employeeList);
			if (log.isInfoEnabled()) {
				log.info(LogConstants.EXITED.template(), "findSubordinate");
			}

		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}
		return totalRecord;
	}

	@Override
	public HRMSBaseResponse<List<VOHolidayResponse>> holidayListProcess(Long year) throws HRMSException {

		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "holidayListProcess");
		}
		HRMSBaseResponse<List<VOHolidayResponse>> response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		long totalRecord = 0;
		Employee emp = leaveProcessorDependencies.getEmployeeDAO().findActiveEmployeeByIdAndOrgId(Long.valueOf(empId),
				IHRMSConstants.isActive, orgId);
		long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
		long branchId = !HRMSHelper
				.isNullOrEmpty(emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation())
						? emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation().getId()
						: emp.getCandidate().getCandidateProfessionalDetail().getBranch().getId();

		long deptId = emp.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
		Long currentYear = !HRMSHelper.isNullOrEmpty(year) ? year : Year.now().getValue();
		totalRecord = holidayList(response, orgId, totalRecord, divId, branchId, currentYear);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "holidayListProcess");
		}
		response.setTotalRecord(totalRecord);
		setHolidayResponse(response);
		return response;
	}

	private void setHolidayResponse(HRMSBaseResponse<List<VOHolidayResponse>> response) {
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
	}

	private long holidayList(HRMSBaseResponse<List<VOHolidayResponse>> response, Long orgId, long totalRecord,
			long divId, long branchId, Long currentYear) throws HRMSException {
		if (!HRMSHelper.isLongZero(branchId) && !HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(divId)
				&& !HRMSHelper.isLongZero(currentYear)) {

			List<OrganizationHoliday> holidayEntityList = leaveProcessorDependencies.getHolidayDAO()
					.getHolidayListByOrgBranchDivIdYear(orgId, divId, branchId, currentYear);

			List<VOHolidayResponse> voHolidayList = new ArrayList<>();
			if (!HRMSHelper.isNullOrEmpty(holidayEntityList)) {
				voHolidayList = LeaveTransformUtil.transalteToHolidayListVO(holidayEntityList);

				totalRecord = leaveProcessorDependencies.getOrgHolidayDAO().countHolidayListByOrgBranchDivIdYear(orgId,
						divId, branchId, currentYear);

				response.setResponseBody(voHolidayList);
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		return totalRecord;
	}

	@Override
	public HRMSBaseResponse<LeaveSumarryDetailsResponseVO> getEmployeeLeaveSummary(long empId, Integer year)
			throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "getEmployeeLeaveSummary");
		}
		HRMSBaseResponse<LeaveSumarryDetailsResponseVO> response = new HRMSBaseResponse<>();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Integer currentYear = !HRMSHelper.isNullOrEmpty(year) ? year : HRMSDateUtil.getCurrentYear();

		teamLeaveSummary(empId, response, orgId, employeeId, currentYear);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "getEmployeeLeaveSummary");
		}
		return response;
	}

	private void teamLeaveSummary(long empId, HRMSBaseResponse<LeaveSumarryDetailsResponseVO> response, Long orgId,
			Long employeeId, Integer currentYear) throws HRMSException {
		if (!HRMSHelper.isLongZero(empId)) {
			Employee employee = leaveProcessorDependencies.getEmployeeDAO()
					.getEmployeeLeaveDetailByEmpIdAndYearAndOrgId(empId, currentYear, IHRMSConstants.isActive, orgId);

			if (HRMSHelper.isNullOrEmpty(employee)) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
			List<String> Role = SecurityFilter.TL_CLAIMS.get().getRoles();
			
			EmployeeReportingManager reportingManager = employee.getEmployeeReportingManager();
			Long reportingManagerId = reportingManager.getReporingManager().getId();

			if(!HRMSHelper.isRolePresent(Role, ERole.HR.name())) {
				log.info("Logged in ROLE :"+ Role );
			if (!employeeId.equals(reportingManagerId)) {
					throw new HRMSException(1510, ResponseCode.getResponseCodeMap().get(1510));
				}
			}
			

			LeaveSumarryDetailsResponseVO leaveDetails = new LeaveSumarryDetailsResponseVO();
			List<EmployeeLeaveSumarryDetailsVO> emplLeaveDtlList = new ArrayList<>();

			Set<EmployeeLeaveDetail> employeeLeaveDetailsSet = employee.getEmployeeLeaveDetails();
			int totalRecords = 0;
			for (EmployeeLeaveDetail employeeLeaveDetailsEntity : employeeLeaveDetailsSet) {
				EmployeeLeaveSumarryDetailsVO model = DashboardTransformUtils
						.convertToEmployeeLeaveSumarryVo(employeeLeaveDetailsEntity);
				if (!HRMSHelper.isNullOrEmpty(model)) {
					if (!HRMSHelper.isNullOrEmpty(model.getNumberOfDaysAvailed())
							|| !HRMSHelper.isNullOrEmpty(model.getLeaveAvailable())) {

						long countLeaveApplied = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
								.findCountOfAppliedLeaveDetailsByOrgId(employee.getId(),
										employeeLeaveDetailsEntity.getMasterLeaveType().getId(),
										IHRMSConstants.LeaveStatus_PENDING, HRMSDateUtil.getCurrentYear(), orgId);

						model.setLeaveApplied(countLeaveApplied);
						emplLeaveDtlList.add(model);
						totalRecords++;
					}
				}
			}

			leaveDetails.setEmployeeLeaveDetail(emplLeaveDtlList);
			response.setResponseBody(leaveDetails);
			response.setTotalRecord(totalRecords);
			teamLeaveSummaryResponse(response);
		} else {
			throw new HRMSException(EResponse.ERROR_INSUFFICIENT_DATA.getCode(),
					EResponse.ERROR_INSUFFICIENT_DATA.getMessage());
		}
	}

	private void teamLeaveSummaryResponse(HRMSBaseResponse<LeaveSumarryDetailsResponseVO> response) {
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
		response.setResponseCode(EResponse.SUCCESS.getCode());
		response.setResponseMessage(EResponse.SUCCESS.getMessage());
	}

	@Override
	public HRMSBaseResponse<VOAppliedLeaveCount> calculateGrantLeaveProcess(LeaveCalculationRequestVO request)
			throws HRMSException {

		log.info("Inside calculateGrantLeave method");
		leaveProcessorDependencies.getEmployeeAuthorityHelper().calculateGrantLeaveInputValidation(request);
		HRMSBaseResponse<VOAppliedLeaveCount> response = new HRMSBaseResponse();
		MasterLeaveType masterLeaveType = leaveProcessorDependencies.getMstLeaveTypeDAO().findByLeaveTypeCodeAndOrgId(
				IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE, SecurityFilter.TL_CLAIMS.get().getOrgId());
		request.setNumberOfSession(masterLeaveType.getNumberOfSession());

		Date fromDate = HRMSDateUtil.parse(request.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date toDate = HRMSDateUtil.parse(request.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		long fSession = HRMSHelper.isNullOrEmpty(request.getFromSession()) ? 0
				: Integer.valueOf(request.getFromSession().substring(request.getFromSession().length() - 1));
		long tSession = HRMSHelper.isNullOrEmpty(request.getToSession()) ? 0
				: Integer.valueOf(request.getToSession().substring(request.getToSession().length() - 1));
		VOAppliedLeaveCount leaveCount = new VOAppliedLeaveCount();

		if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate) && !HRMSHelper.isLongZero(fSession)
				&& !HRMSHelper.isLongZero(tSession)) {

			validateGrantLeaveCalculation(request, fromDate, toDate, fSession, tSession, leaveCount);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		log.info("Exit from calculateGrantLeave method");
		setApplyLeaveGrantResponse(response, leaveCount);
		return response;

	}

	private void validateGrantLeaveCalculation(LeaveCalculationRequestVO request, Date fromDate, Date toDate,
			long fSession, long tSession, VOAppliedLeaveCount leaveCount) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate)) {

			long diff = HRMSDateUtil.getDifferenceInDays(fromDate, toDate);
			log.info("*****Total Day Difference::" + diff);

			if (diff >= 0) {

				double totalLeave = diff + 1 - (HRMSHelper.convertFromSession(request))
						- (HRMSHelper.convertToSession(request));

				HRMSHelper.checkSessionTiming(fromDate, toDate, fSession, tSession, request.getNumberOfSession());

				if (totalLeave > 0) {
					log.info("*****Total Calculated Leave::" + totalLeave);
					leaveCount.setCalculatedLeave(totalLeave);
					leaveCount.setResponseCode(IHRMSConstants.successCode);
					leaveCount.setResponseMessage(IHRMSConstants.successMessage);
				} else {
					throw new HRMSException(IHRMSConstants.NotValidZeroDayLeaveCode,
							IHRMSConstants.NotValidZeroDayLeaveMessage);
				}
			} else {
				throw new HRMSException(1502, ResponseCode.getResponseCodeMap().get(1502));
			}
		} else {
			throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.NotValidDateMessage);
		}
	}

	private void setApplyLeaveGrantResponse(HRMSBaseResponse<VOAppliedLeaveCount> response,
			VOAppliedLeaveCount leaveCount) {
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(leaveCount);
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
	}

	@Override
	public HRMSBaseResponse<?> applyGrantLeaveProcess(ApplyGrantLeaveRequestVO request)
			throws HRMSException, ParseException {

		log.info("Inside grantLeaveApply method");
		leaveProcessorDependencies.getEmployeeAuthorityHelper().applyGrantLeaveInputValidation(request);
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity = null;

		MasterLeaveType masterLeaveTypeEntity = leaveProcessorDependencies.getMstLeaveTypeDAO()
				.findByLeaveTypeCodeAndOrgId(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE,
						SecurityFilter.TL_CLAIMS.get().getOrgId());
		Employee employeeEntity = leaveProcessorDependencies.getEmployeeDAO().findByEmpIdAndOrgId(empId,
				SecurityFilter.TL_CLAIMS.get().getOrgId());

		LeaveCalculationRequestVO calculationRequest = setGrantLeaveCalculationVo(request, masterLeaveTypeEntity,
				employeeEntity);
		HRMSBaseResponse calculateGrantLeaveResponse = calculateGrantLeaveProcess(calculationRequest);

		VOAppliedLeaveCount calculateLeave = (VOAppliedLeaveCount) calculateGrantLeaveResponse.getResponseBody();

		if (request.getNoOfDays() != calculateLeave.getCalculatedLeave()) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + "- Incorrect no.of days calculation");
		}

		Date fromDate = HRMSDateUtil.parse(request.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date toDate = HRMSDateUtil.parse(request.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);

		EmployeeGrantLeaveDetail overlappingLeave = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.findOverlappingLeaveByOrgId(fromDate, toDate, empId, IHRMSConstants.LeaveStatus_CANCELLED,
						IHRMSConstants.LeaveStatus_REJECT, SecurityFilter.TL_CLAIMS.get().getOrgId());

		if (overlappingLeave != null) {
			throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.LeaveOverlapsMessage);
		}

		Date employeeJoiningDate = employeeEntity.getCandidate().getCandidateProfessionalDetail().getDateOfJoining();
		if (fromDate.before(employeeJoiningDate)) {
			throw new HRMSException(1506, ResponseCode.getResponseCodeMap().get(1597));
		}

		if (!HRMSHelper.isNullOrEmpty(request)) {
			employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
					.findByIdOrgId(request.getId(), SecurityFilter.TL_CLAIMS.get().getOrgId());

			boolean grantLeaveApplyAllowedStatus = false;

			Candidate candidate = leaveProcessorDependencies.getCandidateDAO().findByIdAndIsActiveAndorgId(
					employeeEntity.getCandidate().getId(), IHRMSConstants.isActive,
					SecurityFilter.TL_CLAIMS.get().getOrgId());
			VOLeaveCalculationRequest leaveCalculation = setGrantLeaveCalculationVO(request, masterLeaveTypeEntity,
					candidate);

			validateGrantLeaveDates(request, grantLeaveApplyAllowedStatus, candidate, leaveCalculation);

			employeeGrantLeaveDetailEntity = grantApplyLeaveAction(request, employeeGrantLeaveDetailEntity,
					masterLeaveTypeEntity, employeeEntity);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(IHRMSConstants.LeaveActionSuccessMessage);
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
		log.info("Exit from  grantLeaveApply method");

		return response;
	}

	private void validateGrantLeaveDates(ApplyGrantLeaveRequestVO request, boolean grantLeaveApplyAllowedStatus,
			Candidate candidate, VOLeaveCalculationRequest leaveCalculation) throws ParseException, HRMSException {
		List<OrganizationWeekoff> weekoffList = leaveProcessorDependencies.getOrganizationWeekoffDAO()
				.getWeekoffByOrgBranchDivDeptId(candidate.getLoginEntity().getOrganization().getId(),
						candidate.getCandidateProfessionalDetail().getDivision().getId(),
						candidate.getCandidateProfessionalDetail().getBranch().getId(),
						candidate.getCandidateProfessionalDetail().getDepartment().getId());

		int workingdays = HRMSHelper.getWorkingDays(leaveCalculation, weekoffList);

		List<OrganizationHoliday> holidayList = leaveProcessorDependencies.getOrgHolidayDAO()
				.getHolidayListByOrgBranchDivIdYear(candidate.getLoginEntity().getOrganization().getId(),
						candidate.getCandidateProfessionalDetail().getDivision().getId(),
						candidate.getCandidateProfessionalDetail().getBranch().getId(),
						new Long(HRMSDateUtil.getCurrentYear()));

		int holidayDays = HRMSHelper.getHolidays(leaveCalculation, holidayList, weekoffList);

		log.info("holiday days " + holidayDays);
		log.info("working days " + workingdays);

		if (workingdays == 0) {
			log.info("grant leave apply allow : working days == 0 ");
			grantLeaveApplyAllowedStatus = true;
			System.out.println("On Duty/WFH...............");
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date1 = HRMSDateUtil.parse(request.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
			if (date1.compareTo(new Date()) > 0) {
				throw new HRMSException(1505, ResponseCode.getResponseCodeMap().get(1505));
			}
		} else if (workingdays > 0) {
			if (workingdays <= holidayDays) {
				log.info("grant leave apply allow : workingdays <= holidayDays ");
				grantLeaveApplyAllowedStatus = true;
				System.out.println("On Duty/WFH...............");
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				Date date1 = HRMSDateUtil.parse(request.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
				if (date1.compareTo(new Date()) > 0) {
					throw new HRMSException(IHRMSConstants.FUTURE_DATE_LEAVE_RESTRICT_CODE,
							IHRMSConstants.FUTURE_DATE_LEAVE_RESTRICT_MESSAGE);
				}
			}
		}

		if (!grantLeaveApplyAllowedStatus) {
			throw new HRMSException(1504, ResponseCode.getResponseCodeMap().get(1504));
		}
	}

	@Override
	public HRMSBaseResponse<AvailableLeavesVO> getAvailableLeaveProcess(Long leaveTypeId) throws HRMSException {

		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "getAvailableLeaveProcess");
		}
		HRMSBaseResponse<AvailableLeavesVO> response = new HRMSBaseResponse<>();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		long totalRecord = 0;
		totalRecord = availableLeaveCountMethod(leaveTypeId, response, orgId, empId, totalRecord);

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "getAvailableLeaveProcess");
		}

		response.setTotalRecord(totalRecord);
		setAvailableCountResponse(response);
		return response;
	}

	private long availableLeaveCountMethod(Long leaveTypeId, HRMSBaseResponse<AvailableLeavesVO> response, Long orgId,
			Long empId, long totalRecord) throws HRMSException {
		if (!HRMSHelper.isLongZero(empId) && !HRMSHelper.isLongZero(leaveTypeId)) {

			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			EmployeeLeaveDetail employeeLeaveDetail = leaveProcessorDependencies.getEmployeeLeaveDetailDAO()
					.findLeaveAvailableByEmpIdAndLeaveType(empId, leaveTypeId, IHRMSConstants.isActive, year);

			AvailableLeavesVO availableLeavesVO = null;

			if (!HRMSHelper.isNullOrEmpty(employeeLeaveDetail)) {
				availableLeavesVO = LeaveTransformUtil.ConvertToAvailableLeavesVO(employeeLeaveDetail);
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
			totalRecord = leaveProcessorDependencies.getEmpLeaveDetailsDAO()
					.countOfLeaveAvailableByEmpIdAndLeaveTypeAndOrgId(empId, leaveTypeId, IHRMSConstants.isActive, year,
							orgId);
			response.setResponseBody(availableLeavesVO);
		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}
		return totalRecord;
	}

	private void setAvailableCountResponse(HRMSBaseResponse<AvailableLeavesVO> response) {
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
	}

	private VOLeaveCalculationRequest setGrantLeaveCalculationVO(ApplyGrantLeaveRequestVO request,
			MasterLeaveType masterLeaveTypeEntity, Candidate candidate) {
		VOLeaveCalculationRequest leaveCalculation = new VOLeaveCalculationRequest();
		leaveCalculation.setBranchId(candidate.getCandidateProfessionalDetail().getBranch().getId());
		leaveCalculation.setDivisionId(candidate.getCandidateProfessionalDetail().getDivision().getId());
		leaveCalculation.setFromDate(HRMSDateUtil.parse(request.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		leaveCalculation.setFromSession(request.getFromSession());
		leaveCalculation.setLeaveTypeId(masterLeaveTypeEntity.getId());
		leaveCalculation.setOrganizationId(candidate.getLoginEntity().getOrganization().getId());
		leaveCalculation.setToDate(HRMSDateUtil.parse(request.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		leaveCalculation.setToSession(request.getToSession());
		return leaveCalculation;
	}

	private LeaveCalculationRequestVO setGrantLeaveCalculationVo(ApplyGrantLeaveRequestVO request,
			MasterLeaveType masterLeaveTypeEntity, Employee employeeEntity) {
		LeaveCalculationRequestVO calculationRequest = new LeaveCalculationRequestVO();
		calculationRequest.setFromDate(request.getFromDate());
		calculationRequest.setToDate(request.getToDate());
		calculationRequest.setToSession(request.getToSession());
		calculationRequest.setLeaveTypeId(request.getLeaveTypeId());
		calculationRequest.setFromSession(request.getFromSession());
		calculationRequest.setOrganizationId(employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());
		calculationRequest.setNumberOfSession(masterLeaveTypeEntity.getNumberOfSession());
		return calculationRequest;
	}

	private EmployeeGrantLeaveDetail grantApplyLeaveAction(ApplyGrantLeaveRequestVO request,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity, MasterLeaveType masterLeaveTypeEntity,
			Employee employeeEntity) throws HRMSException, ParseException {
		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity)
				&& !HRMSHelper.isNullOrEmpty(masterLeaveTypeEntity)) {
			MasterLeaveType masterLeaveTypeEntityUpdate = leaveProcessorDependencies.getMstLeaveTypeDAO()
					.findByIdAndIsActiveAndOrgId(masterLeaveTypeEntity.getId(), IHRMSConstants.isActive,
							SecurityFilter.TL_CLAIMS.get().getOrgId());
			employeeGrantLeaveDetailEntity = HRMSRequestTranslator
					.translateToEmployeeGrantLeaveDetailEntity(employeeGrantLeaveDetailEntity, request, employeeEntity);
			employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntityUpdate);
		} else {
			// insert
			Date today = new Date();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date fromDate = HRMSDateUtil.parse(request.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);

			today = removeTimeComponent(today);
			fromDate = removeTimeComponent(fromDate);
//
//	            // Calculate the difference in milliseconds
			long differenceInMilliseconds = today.getTime() - fromDate.getTime();
//
//	            // Convert the difference to days
			long differenceInDays = differenceInMilliseconds / (1000 * 60 * 60 * 24);

			if (differenceInDays > 30) {
				throw new HRMSException("Cannot Apply leave after 30 days after working day");
			}

			employeeGrantLeaveDetailEntity = new EmployeeGrantLeaveDetail();
			if (!HRMSHelper.isNullOrEmpty(employeeEntity) && !HRMSHelper.isNullOrEmpty(masterLeaveTypeEntity)) {
				employeeGrantLeaveDetailEntity.setEmployee(employeeEntity);
				MasterLeaveType masterLeaveTypeEntityUpdate = leaveProcessorDependencies.getMstLeaveTypeDAO()
						.findByIdAndIsActiveAndOrgId(masterLeaveTypeEntity.getId(), IHRMSConstants.isActive,
								SecurityFilter.TL_CLAIMS.get().getOrgId());
				// employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntity);
				employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntityUpdate);
				employeeGrantLeaveDetailEntity = HRMSRequestTranslator.translateToEmployeeGrantLeaveDetailEntity(
						employeeGrantLeaveDetailEntity, request, employeeEntity);
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

		}
		employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_PENDING);
		employeeGrantLeaveDetailEntity.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		employeeGrantLeaveDetailEntity.setDateOfApplied(new Date());
		employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.save(employeeGrantLeaveDetailEntity);

		String managerEmailId = employeeEntity.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
		String ccEmailId = request.getCc();

		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
		placeHolderMapping.put("{rootIp}", leaveProcessorDependencies.baseURL + "/api");
		placeHolderMapping.put("{websiteURL}", leaveProcessorDependencies.baseURL);

		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_GrantLeaveApply);

		leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, null, mailContent,
				IHRMSConstants.MailSubject_GrantLeaveApplication,
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());

		return employeeGrantLeaveDetailEntity;
	}

	private static Date removeTimeComponent(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			return dateFormat.parse(dateFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	@Override
	public HRMSBaseResponse<?> approveGrantLeaveProcess(ApplyGrantLeaveRequestVO request) throws HRMSException {

		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "approveGrantLeaveProcess");
		}

		leaveProcessorDependencies.getEmployeeAuthorityHelper().grantLeaveApprovedInputValidation(request);
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.findByIdAndOrgId(request.getId(), orgId);

		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity.getLeaveStatus()
				.equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
			/*
			 * next variable is flag. It is used to check whether entry of leave type
			 * applied for comp off is present in EmployeeLeaveDetails. If not, then insert
			 * into it.
			 */
			EmployeeReportingManager reportingManager = employeeGrantLeaveDetailEntity.getEmployee()
					.getEmployeeReportingManager();
			long reportingManagerId = employeeGrantLeaveDetailEntity.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getId();

			if (!empId.equals(reportingManagerId)) {
				throw new HRMSException(1522, ResponseCode.getResponseCodeMap().get(1522));
			}

			boolean isCompOffAppliedLeaveTypePresent = false;

			if (!isCompOffAppliedLeaveTypePresent) {
				addToEmployeeLeaveDetail(employeeGrantLeaveDetailEntity.getEmployee().getId(),
						employeeGrantLeaveDetailEntity.getMasterLeaveType().getId(), HRMSHelper.getCurrentYear(),
						employeeGrantLeaveDetailEntity.getNoOfDays(),
						employeeGrantLeaveDetailEntity.getMasterLeaveType(),
						employeeGrantLeaveDetailEntity.getEmployee());
			}
			Employee employeeEntityUpdated = leaveProcessorDependencies.getEmployeeDAO()
					.findEmpCandByEmpId(employeeGrantLeaveDetailEntity.getEmployee().getId());
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			// upto this added and next DAO call modified, year parameter added
			List<EmployeeLeaveDetail> empLeaveDetailsUpdatedList = leaveProcessorDependencies
					.getEmployeeLeaveDetailsDAO()
					.findLeaveAvailableByEmployeeAndLeaveType(employeeGrantLeaveDetailEntity.getEmployee().getId(),
							employeeGrantLeaveDetailEntity.getMasterLeaveType().getId(), IHRMSConstants.isActive, year);
			for (EmployeeLeaveDetail employeeLeaveDetail : empLeaveDetailsUpdatedList) {
				if (employeeLeaveDetail.getMasterLeaveType().getId() == employeeGrantLeaveDetailEntity
						.getMasterLeaveType().getId() && employeeLeaveDetail.getYear() == HRMSHelper.getCurrentYear()) {
					employeeGrantLeaveDetailEntity = setEmployeeGrantLeaveDetail(empId, employeeGrantLeaveDetailEntity,
							employeeLeaveDetail);
					approveGrantLeaveEmailSender(employeeGrantLeaveDetailEntity);
					EmployeeCreditLeaveDetail creditLeaveDetail = new EmployeeCreditLeaveDetail();
					setCreditLeaveDetail(employeeGrantLeaveDetailEntity, employeeEntityUpdated, calendar,
							creditLeaveDetail);
					leaveProcessorDependencies.getCreditLeaveDAO().save(creditLeaveDetail);
					break;

				}
			}
		} else {
			throw new HRMSException(IHRMSConstants.LeaveActionErrorCode, IHRMSConstants.LeaveNotInPendingErrorMessage);
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1535));
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "approveGrantLeaveProcess");
		}
		return response;
	}

	private void setCreditLeaveDetail(EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity,
			Employee employeeEntityUpdated, Calendar calendar, EmployeeCreditLeaveDetail creditLeaveDetail) {
		creditLeaveDetail.setCreatedBy(employeeEntityUpdated.getId().toString());
		creditLeaveDetail.setCreatedDate(new Date());
		creditLeaveDetail.setCreditedBy(IHRMSConstants.GRANT_LEAVE_CREDITED_BY);
		creditLeaveDetail.setEmployee(employeeEntityUpdated);
		creditLeaveDetail.setFromDate(employeeGrantLeaveDetailEntity.getFromDate());
		creditLeaveDetail.setIsActive(IHRMSConstants.isActive);
		creditLeaveDetail.setMasterLeaveType(employeeGrantLeaveDetailEntity.getMasterLeaveType());
		creditLeaveDetail.setNoOfDays(employeeGrantLeaveDetailEntity.getNoOfDays());
		creditLeaveDetail.setPostedDate(new Date());
		creditLeaveDetail.setLeaveAvailable(employeeGrantLeaveDetailEntity.getNoOfDays());
		// expiry/todate calculation fro compoff
		// Calendar calendar = Calendar.getInstance();
		calendar.setTime(employeeGrantLeaveDetailEntity.getFromDate());
		calendar.add(Calendar.DAY_OF_MONTH, 90);
		Date toDate = calendar.getTime();
		creditLeaveDetail.setToDate(toDate);
	}

	private EmployeeGrantLeaveDetail setEmployeeGrantLeaveDetail(Long empId,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity, EmployeeLeaveDetail employeeLeaveDetail) {
		float leaveAvailable = employeeLeaveDetail.getLeaveAvailable();
		float grantLeaveApplied = employeeGrantLeaveDetailEntity.getNoOfDays();
		float updatedleaveAvailable = leaveAvailable + grantLeaveApplied;
		employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_APPROVED);
		employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
		employeeGrantLeaveDetailEntity.setUpdatedBy(String.valueOf(empId));
		employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
		employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
		employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.save(employeeGrantLeaveDetailEntity);
		return employeeGrantLeaveDetailEntity;
	}

	private void approveGrantLeaveEmailSender(EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity) {
		/**
		 * Email Sender
		 */
		String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
		String ccEmailId = employeeGrantLeaveDetailEntity.getCc();
		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
		placeHolderMapping.put("{rootIp}", leaveProcessorDependencies.baseURL);
		placeHolderMapping.put("{websiteURL}", leaveProcessorDependencies.baseURL);
		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_LeaveApproved);
		leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId, mailContent,
				IHRMSConstants.MailSubject_GrantLeaveApproved,
				employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
						.getDivision().getId(),
				employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
	}

	private void addToEmployeeLeaveDetail(long employeeId, long masterLeaveTypeId, int year, float leave,
			MasterLeaveType masterLeaveType, Employee employee) {
		EmployeeLeaveDetail employeeLeaveDetailEntity = leaveProcessorDependencies.getEmployeeLeaveDetailsDAO()
				.findEmployeeLeaveByEIDYEAR(employeeId, masterLeaveTypeId, year);

		if (!HRMSHelper.isNullOrEmpty(employeeLeaveDetailEntity)) {
			float leaveAvailable = employeeLeaveDetailEntity.getLeaveAvailable();
			float leaveEarned = employeeLeaveDetailEntity.getLeaveEarned();
			employeeLeaveDetailEntity.setLeaveAvailable(leaveAvailable + leave);
			employeeLeaveDetailEntity.setLeaveEarned(leaveEarned + leave);
			employeeLeaveDetailEntity.setUpdatedDate(new Date());
			leaveProcessorDependencies.getEmployeeLeaveDetailsDAO().save(employeeLeaveDetailEntity);
		} else {
			EmployeeLeaveDetail employeeLeaveDetail = new EmployeeLeaveDetail();

			employeeLeaveDetail.setCreatedDate(new Date());
			employeeLeaveDetail.setLeaveAvailable(leave);
			employeeLeaveDetail.setLeaveEarned(leave);
			employeeLeaveDetail.setMasterLeaveType(masterLeaveType);
			employeeLeaveDetail.setEmployee(employee);
			employeeLeaveDetail.setYear(year);
			employeeLeaveDetail.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
			leaveProcessorDependencies.getEmployeeLeaveDetailsDAO().save(employeeLeaveDetail);
		}
	}

	@Override
	public HRMSBaseResponse rejectGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "rejectGrantLeave");
		}
		leaveProcessorDependencies.getEmployeeAuthorityHelper().rejectGrantLeaveInputValidation(request);
		HRMSBaseResponse response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		rejectGrantLeaveMethod(request, empId, orgId, roles);
		setRejectLeaveResponse(response);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "rejectGrantLeave");
		}
		return response;
	}

	private void setRejectLeaveResponse(HRMSBaseResponse response) {
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1536));
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
	}

	private void rejectGrantLeaveMethod(ApplyGrantLeaveRequestVO request, Long empId, Long orgId, List<String> roles)
			throws HRMSException {
		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
			Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			List<EmployeeReportingManager> employeesUnderManager = leaveProcessorDependencies.getReportingManagerDAO()
					.findByreporingManager(employeeId);
			EmployeeGrantLeaveDetail leavesAppliedEnity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
					.findByIdAndOrgId(request.getId(), orgId);

			if (leavesAppliedEnity != null) {
				Long requestedEmployeeId = leavesAppliedEnity.getEmployee().getId();
				long matchCount = employeesUnderManager.stream()
						.filter(e -> e.getEmployee().getId().equals(requestedEmployeeId)).count();
				if (matchCount > 0) {
					EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity = leaveProcessorDependencies
							.getEmployeeGrantLeaveDAO().findByIdAndOrgId(request.getId(), orgId);
					if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity
							.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
						employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_REJECT);
						employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
						employeeGrantLeaveDetailEntity.setUpdatedBy(String.valueOf(empId));
						employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
						employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
						employeeGrantLeaveDetailEntity.setReasonForCancel(request.getReasonForCancel());
						employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
								.save(employeeGrantLeaveDetailEntity);

						/*** Email Sender */
						rejectGrantLeaveEmailSender(employeeGrantLeaveDetailEntity);
					} else {
						throw new HRMSException(1516, ResponseCode.getResponseCodeMap().get(1516));
					}
				} else {
					throw new HRMSException(1520, ResponseCode.getResponseCodeMap().get(1520));
				}
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private void rejectGrantLeaveEmailSender(EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity) {
		String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
		String ccEmailId = employeeGrantLeaveDetailEntity.getCc();
		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
		placeHolderMapping.put("{rootIp}", leaveProcessorDependencies.baseURL);
		placeHolderMapping.put("{websiteURL}", leaveProcessorDependencies.baseURL);
		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_LeaveReject);
		leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId, mailContent,
				IHRMSConstants.MailSubject_LeaveRejected,
				employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
						.getDivision().getId(),
				employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
	}

	@Override
	public HRMSBaseResponse<?> rejectLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "rejectLeaveProcess");
		}
		leaveProcessorDependencies.getEmployeeAuthorityHelper().rejectLeaveInputValidation(request);
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
			Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			List<EmployeeReportingManager> employeesUnderManager = leaveProcessorDependencies.getReportingManagerDAO()
					.findByreporingManager(employeeId);
			Optional<EmployeeLeaveApplied> leavesAppliedOptional = leaveProcessorDependencies
					.getEmployeeLeaveAppliedDAO().findById(request.getLeaveApplied().getId());
			checkLeaveIsPresent(request, empId, orgId, employeesUnderManager, leavesAppliedOptional);

		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "rejectLeaveProcess");
		}
		setResponseForRejectLeave(response);
		return response;
	}

	private void setResponseForRejectLeave(HRMSBaseResponse<?> response) {
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1536));
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
	}

	private void checkLeaveIsPresent(ApplyLeaveRequestVO request, Long empId, Long orgId,
			List<EmployeeReportingManager> employeesUnderManager, Optional<EmployeeLeaveApplied> leavesAppliedOptional)
			throws HRMSException {
		if (leavesAppliedOptional.isPresent()) {
			EmployeeLeaveApplied leavesAppliedEnity = leavesAppliedOptional.get();
			leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
					.findByIdAndOrgId(request.getLeaveApplied().getId(), orgId);
			Long requestedEmployeeId = leavesAppliedEnity.getEmployee().getId();
			long matchCount = employeesUnderManager.stream()
					.filter(e -> e.getEmployee().getId().equals(requestedEmployeeId)).count();
			if (matchCount > 0) {
				checkLeaveStatus(request, empId, leavesAppliedEnity);
			} else {
				throw new HRMSException(1520, ResponseCode.getResponseCodeMap().get(1520));
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	private void checkLeaveStatus(ApplyLeaveRequestVO request, Long empId, EmployeeLeaveApplied leavesAppliedEnity)
			throws HRMSException {
		if (leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_REJECT);
			leavesAppliedEnity.setDateOfApproverAction(new Date());
			leavesAppliedEnity.setUpdatedBy(String.valueOf(empId));
			leavesAppliedEnity.setUpdatedDate(new Date());
			leavesAppliedEnity.setReasonForReject(request.getLeaveApplied().getReasonForReject());

			EmployeeLeaveDetail emp = leaveProcessorDependencies.getEmpLeaveDetailsDAO().findEmployeeLeaveByEIDYEAR(
					leavesAppliedEnity.getEmployee().getId(), leavesAppliedEnity.getMasterLeaveType().getId(),
					HRMSDateUtil.getCurrentYear());

			float leaveAvaialble = 0;
			if (!HRMSHelper.isNullOrEmpty(emp.getLeaveAvailable())) {
				leaveAvaialble = emp.getLeaveAvailable();
			}
			float leaveToBeCredited = leavesAppliedEnity.getNoOfDays();
			float leaveAvailed = 0;
			if (!HRMSHelper.isNullOrEmpty(emp.getNumberOfDaysAvailed())) {
				leaveAvailed = emp.getNumberOfDaysAvailed();
			}
			float availedLeavesAfterWithdrawCredit = leaveAvailed - leaveToBeCredited;
			emp.setNumberOfDaysAvailed(availedLeavesAfterWithdrawCredit);

			if (!(leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode().equals(ELeaveTypeCode.ONDT.name())
					|| (leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode().equals(ELeaveTypeCode.LOPL.name()))
					|| (leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
							.equals(ELeaveTypeCode.WRHM.name())))) {
				float leaveAfterCreditedBalance = leaveAvaialble + leaveToBeCredited;
				emp.setLeaveAvailable(leaveAfterCreditedBalance);
			}

			leaveProcessorDependencies.getEmpLeaveDetailsDAO().save(emp);
			leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().save(leavesAppliedEnity);

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
			/** email sender */
			rejectLeaveEmailSender(leavesAppliedEnity);
		} else {
			throw new HRMSException(1515, ResponseCode.getResponseCodeMap().get(1515));
		}
	}

	private void rejectLeaveEmailSender(EmployeeLeaveApplied leavesAppliedEnity) {
		String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
		String ccEmailId = leavesAppliedEnity.getCc();
		Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
		placeHolderMapping.put("{websiteURL}", leaveProcessorDependencies.baseURL);
		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_LeaveReject);
		leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId, mailContent,
				IHRMSConstants.MailSubject_LeaveRejected,
				leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
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
					EmployeeCreditLeaveDetail creditLeaveDetail = leaveProcessorDependencies.getCreditLeaveDAO()
							.findById(employeeCreditAppliedLeaveMapping.getCreditLeaveDetail().getId()).get();
					creditLeaveDetail.setLeaveAvailable(
							creditLeaveDetail.getLeaveAvailable() + IHRMSConstants.COMP_OFF_SESSION_VALUE);
					leaveProcessorDependencies.getCreditLeaveDAO().save(creditLeaveDetail);
					// inactivating entry in applyCreditLeaveMapp table
					employeeCreditAppliedLeaveMapping.setIsActive(IHRMSConstants.isNotActive);
					leaveProcessorDependencies.getCreditAppliedLeaveMappingDAO()
							.save(employeeCreditAppliedLeaveMapping);
					updateResult = 1;
				}
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		return updateResult;
	}

	private List<EmployeeCreditAppliedLeaveMapping> getListOfCreditAppliedLeaveMapping(
			EmployeeLeaveApplied employeeLeaveApplied) throws HRMSException {
		// finding leaveApplyAction and creditLeaveDetailsMapping
		List<EmployeeCreditAppliedLeaveMapping> employeeCreditAppliedLeaveMappings = null;
		if (!HRMSHelper.isNullOrEmpty(employeeLeaveApplied) && !HRMSHelper.isLongZero(employeeLeaveApplied.getId())) {
			employeeCreditAppliedLeaveMappings = leaveProcessorDependencies.getCreditAppliedLeaveMappingDAO()
					.getCandidateWithChecklistDetails(employeeLeaveApplied.getId(), IHRMSConstants.isActive);
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		return employeeCreditAppliedLeaveMappings;
	}

	@Override
	public String creditEmpYearlyCasualLeavesMethod(Long orgId, List<Long> divList, float clCount)
			throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "creditEmpYearlyCasualLeavesMethod");
		}
		if (orgId < 0) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (HRMSHelper.isNullOrEmpty(divList)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		Organization org = leaveProcessorDependencies.organizationDAO.findById(orgId).get();
		if (!HRMSHelper.isNullOrEmpty(org)) {
			List<Employee> activeEmpList = leaveProcessorDependencies.employeeDAO
					.findActiveEmpForCreditEmergencyLeavesSch1edulerByDivision(IHRMSConstants.isActive,
							IHRMSConstants.isActive, divList);
			MasterLeaveType mstLeaveType = leaveProcessorDependencies.mstLeaveTypeDAO
					.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive, org,
							IHRMSConstants.LEAVE_TYPE_CODE_CASUAL_lEAVE);

			casualLeaveforDate(orgId, clCount, activeEmpList, mstLeaveType);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1568));
		}
		return null;
	}

	private void casualLeaveforDate(Long orgId, float clCount, List<Employee> activeEmpList,
			MasterLeaveType mstLeaveType) {
		int year = Year.now().getValue();
		int count = 0;
		Calendar calFromDate = Calendar.getInstance();
		calFromDate.set(year, calFromDate.get(Calendar.MONTH), 01);
		Calendar calToDate = Calendar.getInstance();
		calToDate.set(year, 11, 31);
		count = casualLeaveMethod(orgId, clCount, activeEmpList, mstLeaveType, year, count, calFromDate, calToDate);
	}

	private int casualLeaveMethod(Long orgId, float clCount, List<Employee> activeEmpList, MasterLeaveType mstLeaveType,
			int year, int count, Calendar calFromDate, Calendar calToDate) {
		for (Employee loopEmpMOY : activeEmpList) {
			EmployeeLeaveDetail currentYearLeaveDetail = leaveProcessorDependencies.empLeaveDetailsDAO
					.findEmployeeLeaveByEIDYEARAndOrgId(loopEmpMOY.getId(), mstLeaveType.getId(), year, orgId);
			if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetail)) {
				currentYearLeaveDetail = new EmployeeLeaveDetail();
				currentYearLeaveDetail.setCreatedDate(new Date());
				currentYearLeaveDetail.setCreatedBy("Yearly Scheduler");
				currentYearLeaveDetail.setLeaveAvailable(clCount);
				currentYearLeaveDetail.setLeaveEarned(clCount);
				currentYearLeaveDetail.setMasterLeaveType(mstLeaveType);
				currentYearLeaveDetail.setTotalEligibility(clCount);
				currentYearLeaveDetail.setYear(year);
				currentYearLeaveDetail.setEmployee(loopEmpMOY);
				currentYearLeaveDetail.setOrgId(orgId);
				leaveProcessorDependencies.empLeaveDetailsDAO.save(currentYearLeaveDetail);

				EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
				empCreditLeaveDtlEntity.setCreditedBy("Yearly Scheduler");
				empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
				empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
				empCreditLeaveDtlEntity.setNoOfDays(clCount);
				empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
				empCreditLeaveDtlEntity.setPostedDate(new Date());
				empCreditLeaveDtlEntity.setEmployee(loopEmpMOY);
				empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveType);
				empCreditLeaveDtlEntity.setCreatedBy("Yearly Scheduler");
				empCreditLeaveDtlEntity.setCreatedDate(new Date());
				empCreditLeaveDtlEntity.setOrgId(orgId);
				leaveProcessorDependencies.employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
				++count;

			} else {
				currentYearLeaveDetail.setUpdatedBy("Yearly Scheduler");
				currentYearLeaveDetail.setLeaveAvailable(currentYearLeaveDetail.getLeaveAvailable() + clCount);
				currentYearLeaveDetail.setLeaveEarned(!HRMSHelper.isNullOrEmpty(currentYearLeaveDetail.getLeaveEarned())
						? currentYearLeaveDetail.getLeaveEarned() + clCount
						: clCount);
				currentYearLeaveDetail.setMasterLeaveType(mstLeaveType);
				currentYearLeaveDetail
						.setTotalEligibility(!HRMSHelper.isNullOrEmpty(currentYearLeaveDetail.getTotalEligibility())
								? currentYearLeaveDetail.getTotalEligibility() + clCount
								: clCount);
				currentYearLeaveDetail.setUpdatedDate(new Date());
				leaveProcessorDependencies.empLeaveDetailsDAO.save(currentYearLeaveDetail);

				EmployeeCreditLeaveDetail empCreditLeaveDtlEntity = new EmployeeCreditLeaveDetail();
				empCreditLeaveDtlEntity.setCreditedBy("Yearly Scheduler");
				empCreditLeaveDtlEntity.setFromDate(calFromDate.getTime());
				empCreditLeaveDtlEntity.setIsActive(IHRMSConstants.isActive);
				empCreditLeaveDtlEntity.setNoOfDays(clCount);
				empCreditLeaveDtlEntity.setToDate(calToDate.getTime());
				empCreditLeaveDtlEntity.setPostedDate(new Date());
				empCreditLeaveDtlEntity.setEmployee(loopEmpMOY);
				empCreditLeaveDtlEntity.setMasterLeaveType(mstLeaveType);
				empCreditLeaveDtlEntity.setCreatedBy("Yearly Scheduler");
				empCreditLeaveDtlEntity.setCreatedDate(new Date());
				empCreditLeaveDtlEntity.setOrgId(orgId);
				leaveProcessorDependencies.employeeCreditLeaveDAO.save(empCreditLeaveDtlEntity);
				++count;
			}
		}
		return count;
	}

	@Override
	public String creditEarnedLeaves(Long orgId, List<Long> divsionList, float elCount)
			throws HRMSException, ParseException {

		if (orgId < 0) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (!HRMSHelper.isLongZero(orgId)) {
			if (HRMSHelper.isNullOrEmpty(divsionList)) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
			Organization org = leaveProcessorDependencies.organizationDAO.findById(orgId).get();
			if (!HRMSHelper.isNullOrEmpty(org)) {
				List<String> employmentTypeNameList = new ArrayList<String>();
				employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_CONFIRMED);
				employmentTypeNameList.add(IHRMSConstants.EMPLOYMENT_TYPE_EMPLOYEE);
				List<MasterEmploymentType> masterEmploymentTypeList = leaveProcessorDependencies.masterEmploymentTypeDAO
						.findByIsActiveAndEmploymentTypeDescriptionIn(IHRMSConstants.isActive, employmentTypeNameList);

				List<Employee> confirmedEmpList = leaveProcessorDependencies.employeeDAO
						.findEmpListByEmployementStatusAndDivision(IHRMSConstants.isActive, IHRMSConstants.isActive,
								masterEmploymentTypeList, divsionList);
				MasterLeaveType mstLeaveType = leaveProcessorDependencies.mstLeaveTypeDAO
						.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive, org,
								IHRMSConstants.LEAVE_TYPE_CODE_SICK);
				Calendar calFromDate = Calendar.getInstance();
				int year = Year.now().getValue();
				calFromDate.set(year, calFromDate.get(Calendar.MONTH), 01);
				Calendar calToDate = Calendar.getInstance();
				calToDate.set(year, 11, 31);
				int count = 0;

				earnedLeaveMethod(orgId, elCount, confirmedEmpList, mstLeaveType, calFromDate, year, calToDate, count);

			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1568));
		}

		return null;
	}

	private void earnedLeaveMethod(Long orgId, float elCount, List<Employee> confirmedEmpList,
			MasterLeaveType mstLeaveType, Calendar calFromDate, int year, Calendar calToDate, int count)
			throws ParseException {
		for (Employee employee : confirmedEmpList) {
			EmployeeLeaveDetail currentYearLeaveDetail = leaveProcessorDependencies.employeeLeaveDetailDAO
					.findEmployeeLeaveByEIDYEAR(employee.getId(), mstLeaveType.getId(), year);
			Date dateOfJoining = employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining();

			Calendar currentDate = Calendar.getInstance();
			Calendar joiningDate = Calendar.getInstance();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			joiningDate.setTime(dateOfJoining);
			joiningDate.add(Calendar.MONTH, 1);
			String formattedCurrentDate = formatter.format(currentDate.getTime());
			Date parsedCurrentDate = formatter.parse(formattedCurrentDate);
			if (parsedCurrentDate.after(joiningDate.getTime()) || parsedCurrentDate.equals(joiningDate.getTime())) {
				if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetail)) {
					currentYearLeaveDetail = new EmployeeLeaveDetail();
					currentYearLeaveDetail.setCreatedBy("Monthly Scheduler");
					currentYearLeaveDetail.setCreatedDate(new Date());
					currentYearLeaveDetail.setLeaveAvailable(elCount);
					currentYearLeaveDetail.setLeaveEarned(elCount);
					currentYearLeaveDetail.setMasterLeaveType(mstLeaveType);
					currentYearLeaveDetail.setTotalEligibility(elCount);
					currentYearLeaveDetail.setYear(year);
					currentYearLeaveDetail.setEmployee(employee);
					currentYearLeaveDetail.setOrgId(orgId);
					leaveProcessorDependencies.employeeLeaveDetailDAO.save(currentYearLeaveDetail);
				} else {
					currentYearLeaveDetail.setUpdatedDate(new Date());
					currentYearLeaveDetail.setUpdatedBy("Monthly Scheduler");
					currentYearLeaveDetail
							.setLeaveAvailable(!HRMSHelper.isNullOrEmpty(currentYearLeaveDetail.getLeaveAvailable())
									? currentYearLeaveDetail.getLeaveAvailable() + elCount
									: elCount);
					currentYearLeaveDetail
							.setLeaveEarned(!HRMSHelper.isNullOrEmpty(currentYearLeaveDetail.getLeaveEarned())
									? currentYearLeaveDetail.getLeaveEarned() + elCount
									: elCount);
					currentYearLeaveDetail.setMasterLeaveType(mstLeaveType);
					currentYearLeaveDetail
							.setTotalEligibility(!HRMSHelper.isNullOrEmpty(currentYearLeaveDetail.getTotalEligibility())
									? currentYearLeaveDetail.getTotalEligibility() + elCount
									: elCount);
					currentYearLeaveDetail.setYear(year);
					currentYearLeaveDetail.setEmployee(employee);
					leaveProcessorDependencies.employeeLeaveDetailDAO.save(currentYearLeaveDetail);
				}
				EmployeeCreditLeaveDetail empCreditLeave = new EmployeeCreditLeaveDetail();
				empCreditLeave.setCreditedBy("Monthly Scheduler");
				empCreditLeave.setFromDate(calFromDate.getTime());
				empCreditLeave.setNoOfDays(elCount);
				empCreditLeave.setToDate(calToDate.getTime());
				empCreditLeave.setPostedDate(new Date());
				empCreditLeave.setEmployee(employee);
				empCreditLeave.setMasterLeaveType(mstLeaveType);
				empCreditLeave.setCreatedDate(new Date());
				empCreditLeave.setOrgId(orgId);
				empCreditLeave.setIsActive(IHRMSConstants.isActive);
				leaveProcessorDependencies.employeeCreditLeaveDAO.save(empCreditLeave);
				++count;
			}
		}
	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getHrLeaveDetailsProcess(Long filterEmployeeId, Integer year,
			Pageable pageable) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "getHrLeaveDetails");
		}
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse<EmployeeLeaveDetailsVO>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Employee employee = leaveProcessorDependencies.getEmployeeDAO().findByEmpIdAndOrgId(empId, orgId);
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		
		EmployeeLeaveDetailsVO leaveDetailsVO = null;
		long totalRecord = 0;
		if (HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int Year = 0;
			if (!HRMSHelper.isNullOrEmpty(year)) {
				Year = year;
			} else {
				Year = calendar.get(Calendar.YEAR);
			}

			List<EmployeeLeaveApplied> leaveApplied = new ArrayList<>();

			if (!HRMSHelper.isNullOrEmpty(filterEmployeeId)) {
				
				Employee filterEmployee = leaveProcessorDependencies.getEmployeeDAO()
						.findByEmpIdAndOrgId(filterEmployeeId, orgId);
				if(!HRMSHelper.isNullOrEmpty(filterEmployee)) {
				if (orgId.equals(filterEmployee.getOrgId())) {
					leaveApplied = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
							.findSubordinateLeaveAppliedDetailsForHrOrderByStatusAndEmployee(
									IHRMSConstants.isActive, IHRMSConstants.LeaveStatus_CANCELLED, Year,
									pageable, filterEmployeeId);

					totalRecord = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
							.countOfSubordinateLeaveAppliedDetailsForHrOrderByStatusAndEmployee(
									IHRMSConstants.isActive, IHRMSConstants.LeaveStatus_CANCELLED, Year,
									filterEmployeeId);
				}else {
					throw new HRMSException(1500,  ResponseCode.getResponseCodeMap().get(1521));
				}}else {
					throw new HRMSException(1200,  ResponseCode.getResponseCodeMap().get(1201));
				}

			} else {
				leaveApplied = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
						.findSubordinateLeaveAppliedDetailsForHrOrderByStatus(IHRMSConstants.isActive, 
								IHRMSConstants.LeaveStatus_CANCELLED, Year,orgId, pageable);

				totalRecord = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
						.countOfSubordinateLeaveAppliedDetailsForHrOrderByStatus(IHRMSConstants.isActive,
								IHRMSConstants.LeaveStatus_CANCELLED, Year,orgId);

			}

			leaveDetailsVO = LeaveTransformUtil.convertToEmployeeLeaveDetailsVO(leaveApplied);

		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EmployeeDoesnotExistMessage);
		}
		}else {
			throw new HRMSException(1500,  ResponseCode.getResponseCodeMap().get(1521));
		}
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "getHrLeaveDetails");
		}
		response.setResponseBody(leaveDetailsVO);
		response.setTotalRecord(totalRecord);
		setLeaveResponse(response);
		return response;

	}

}
