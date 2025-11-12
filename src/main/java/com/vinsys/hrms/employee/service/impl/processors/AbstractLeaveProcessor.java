package com.vinsys.hrms.employee.service.impl.processors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.constants.ELeaveStatus;
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
import com.vinsys.hrms.employee.vo.TeamLeavesReportXLSGenerator;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCompOffCreditLeaveHistory;
import com.vinsys.hrms.entity.EmployeeCreditAppliedLeaveMapping;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeGrantLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterLeaveType;
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

public abstract class AbstractLeaveProcessor implements ILeaveProcessor {
	private Logger log = LoggerFactory.getLogger(VinsysLeaveProcessor.class);
	protected LeaveProcessorDependencies leaveProcessorDependencies;

	protected AbstractLeaveProcessor(LeaveProcessorDependencies leaveProcessorDependencies) {
		this.leaveProcessorDependencies = leaveProcessorDependencies;
	}

	@Value("${base.url}")
	private String baseURL;

	@Override
	public HRMSBaseResponse<?> applyLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException, ParseException {
		log.info("Inside applyLeave method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = null;

		if (!HRMSHelper.isNullOrEmpty(empId)) {
			employee = leaveProcessorDependencies.getEmployeeDAO().findById(empId).get();
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		leaveProcessorDependencies.getEmployeeAuthorityHelper().applyLeaveInputValidation(employee, request);

		MasterLeaveType leaveType = leaveProcessorDependencies.getMstLeaveTypeDAO()
				.findByIdAndIsActive(request.getLeaveApplied().getLeaveTypeId(), IHRMSConstants.isActive);

		// check Is Employee Apply Leave on Holiday or Weekoff.
		if (!(leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("ADPT")
				|| leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("PATR")
				|| leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("MATR"))) {
			isApplyOnHolidayOrWeekOff(employee, request);
		}

//		if(leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("PL")
//				|| leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("SL") || leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("LPE")
//				|| leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("Comp Off") || leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("STDL")
//				|| leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("SRCM") || leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("WFH") 
//				|| leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("LOP") || leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("OD")) {
//			
//			
//		}else {
//			log.info("Leave apply for Maternity or Paternity or Adoption");
//		}

		// leave policy validation
		MasterLeavePolicyVO leavePolicy = leaveProcessorDependencies.getMasterService()
				.getLeavePolicy(leaveType.getId());
		leaveProcessorDependencies.getEmployeeAuthorityHelper().validateLeavePolicy(employee, request, leavePolicy);

		/*
		 * Calculate leave
		 */
		LeaveCalculationRequestVO calculationRequest = new LeaveCalculationRequestVO();
		calculationRequest.setFromDate(request.getLeaveApplied().getFromDate());
		calculationRequest.setToDate(request.getLeaveApplied().getToDate());
		calculationRequest.setToSession(request.getLeaveApplied().getToSession());
		calculationRequest.setLeaveTypeId(request.getLeaveApplied().getLeaveTypeId());
		calculationRequest.setOrganizationId(employee.getCandidate().getLoginEntity().getOrganization().getId());
		calculationRequest.setNumberOfSession(leaveType.getNumberOfSession());
		calculationRequest.setFromSession(request.getLeaveApplied().getFromSession());
		HRMSBaseResponse calculateLeaveResponse = calculateLeave(calculationRequest);

		VOAppliedLeaveCount calculateLeave = (VOAppliedLeaveCount) calculateLeaveResponse.getResponseBody();

		if (request.getLeaveApplied().getNoOfDays() != calculateLeave.getCalculatedLeave()) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + "- Incorrect no.of days calculation");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = HRMSDateUtil.parse(request.getLeaveApplied().getFromDate(),
				IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date toDate = HRMSDateUtil.parse(request.getLeaveApplied().getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		EmployeeLeaveApplied leavesAppliedEnity = null;
		boolean validLeaveFlag = false;

		// Retrieve the joining date of the employee
		Date employeeJoiningDate = employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining();

		// Compare the leave start date with the employee's joining date
		if (fromDate.before(employeeJoiningDate)) {
			// Leave cannot be applied before the employee's joining date
			throw new HRMSException(1506, ResponseCode.getResponseCodeMap().get(1598));
		}

		if (leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("ONDT")
				|| leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("WRHM")) {

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
				throw new HRMSException(IHRMSConstants.MAX_LEAVE_RESTRICT_CODE,
						IHRMSConstants.MAX_LEAVE_RESTRICT_MESSAGE);
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied()) && !HRMSHelper.isNullOrEmpty(empId)
				&& !HRMSHelper.isNullOrEmpty(leaveType) && !HRMSHelper.isNullOrEmpty(leaveType.getLeaveTypeCode())) {
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
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}
			}
			// if not of type probation, don't do anything
		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		if (!HRMSHelper.isNullOrEmpty(employee)) {
			// long divisionId =
			// employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
			System.out.println(employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());

			// UAE employee can apply leave for next 6 month in advance.
			if (employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId() == 3) {
				Date incrementedDate = HRMSDateUtil.setTimeStampToZero(HRMSDateUtil.incByMonth(new Date(), 6));
				System.out.println(incrementedDate);
				System.out.println(incrementedDate.compareTo(toDate));
				if (incrementedDate.compareTo(toDate) < 0) {
					throw new HRMSException(IHRMSConstants.InValidDetailsCode,
							IHRMSConstants.LEAVE_RESTRICTION_6_MONTHS_MESSAGE);
				}
			} else {
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
		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		// upto this modified by SSW
		validLeaveFlag = validateLeave(request, empId);
		if (validLeaveFlag) {
			/// validate Maternity Leave
			if (leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("MATR")) {
				if (request.getLeaveApplied().getNoOfDays() <= IHRMSConstants.MATR_LEAVE_VALID_DAYS) {
					log.info("*********** VALID Maternity Leave ***********");
					leavesAppliedEnity = leaveApplyAction(request, leaveType, empId);
				} else {
					log.info("*********** Invalid Maternity Leave ***********");
					throw new HRMSException(IHRMSConstants.InValidDetailsCode,
							IHRMSConstants.MaternityLeaveOverExceedsLimit);
				}
			} else if (leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("PATR")) {
				if (request.getLeaveApplied().getNoOfDays() <= IHRMSConstants.PATR_LEAVE_VALID_DAYS) {
					// MasterLeaveType masterLeaveTypeEntity =
					// mstLeaveTypeDAO.findByLeaveTypeCode(leaveType.getLeaveTypeCode());
					List<EmployeeLeaveApplied> patrleavesAppliedByEmpList = leaveProcessorDependencies
							.getEmployeeLeaveAppliedDAO().findAllLeavesByemployeeAndmasterLeaveType(empId,
									leaveType.getId(), IHRMSConstants.LeaveStatus_CANCELLED,
									IHRMSConstants.LeaveStatus_WD_APPROVED, IHRMSConstants.LeaveStatus_REJECT);

					if (patrleavesAppliedByEmpList.size() <= 1) {
						log.info("*********** VALID Paternity Leave ***********");
						leavesAppliedEnity = leaveApplyAction(request, leaveType, empId);
					} else {
						log.info("*********** Invalid Paternity Leave ***********");
						throw new HRMSException(IHRMSConstants.InValidDetailsCode,
								IHRMSConstants.PaternityLeaveAvailed2Times);
					}

				} else {
					log.info("*********** Invalid Paternity Leave ***********");
					throw new HRMSException(IHRMSConstants.InValidDetailsCode,
							IHRMSConstants.PaternityLeaveOverExceedsLimit);
				}
			} else if (leaveType.getLeaveTypeCode().trim().equalsIgnoreCase("ADPT")) {

				int validCount = 0;
				Employee empEntity = leaveProcessorDependencies.getEmployeeDAO().findEmpCandByEmpId(empId);
				if (empEntity.getCandidate().getGender().trim().equalsIgnoreCase("Female")) {
					validCount = IHRMSConstants.ADPT_LEAVE_VALID_DAYS_FEMALE;
				} else {
					validCount = IHRMSConstants.ADPT_LEAVE_VALID_DAYS_MALE;
				}
				if (request.getLeaveApplied().getNoOfDays() <= validCount) {
					log.info("*********** VALID Paternity Leave ***********");
					leavesAppliedEnity = leaveApplyAction(request, leaveType, empId);

				} else {
					log.info("*********** Invalid Paternity Leave ***********");
					if (empEntity.getCandidate().getGender().trim().equalsIgnoreCase("Female")) {
						throw new HRMSException(IHRMSConstants.InValidDetailsCode,
								IHRMSConstants.AdoptionLeaveOverExceedsLimitFemale);
					} else {
						throw new HRMSException(IHRMSConstants.InValidDetailsCode,
								IHRMSConstants.AdoptionLeaveOverExceedsLimitMale);
					}

				}
			} else {
				log.info("*********** VALID Leave ***********");
				leavesAppliedEnity = leaveApplyAction(request, leaveType, empId);
			}
		} else {
			log.info("*********** INVALID Leave ***********");
			throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.LeaveOverlapsMessage);
		}

		response.setResponseCode(1200);
		response.setResponseMessage(IHRMSConstants.LeaveActionSuccessMessage);

		log.info("Exit from applyLeave method");
		return response;
	}

	private boolean validateLeave(ApplyLeaveRequestVO requestedLeave, Long empId) {

		MasterLeaveType leaveType = leaveProcessorDependencies.getMstLeaveTypeDAO()
				.findByIdAndIsActive(requestedLeave.getLeaveApplied().getLeaveTypeId(), IHRMSConstants.isActive);
		/***
		 * 1. get all applied leaves where from = sysdate - 5 2. create map applied
		 * leaves 3. create key of requested leave 4. if requested key is present then>>
		 * invalid leave else >> valid leave
		 */
		Map<String, String> mapOfAppliedLeaves = new HashMap<String, String>();

//		long empId = requestedLeave.getLeaveApplied().getEmployeeId();

		// Getting all applied leaves(from last 5 days) of employee

		List<EmployeeLeaveApplied> appliedLeaves = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
				.findAppliedLeavesOfEmployeeFromLastFiveDaysAndOrgId(empId, IHRMSConstants.LeaveStatus_CANCELLED,
						IHRMSConstants.LeaveStatus_WD_APPROVED, IHRMSConstants.LeaveStatus_REJECT,
						SecurityFilter.TL_CLAIMS.get().getOrgId());
		// logger.info("**********Applied Leaves***********");
		for (EmployeeLeaveApplied employeeLeaveApplied : appliedLeaves) {
			// System.out.println(employeeLeaveApplied.getId() );

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
						log.info("Key" + key);
						mapOfAppliedLeaves.put(key, "");

					}
					--i;
					start = 1;
					fromDate = HRMSDateUtil.incDate(fromDate, 1);
				}

			}
			count = 0;
			// System.out.println("*********Row END**********");

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

		MasterLeaveType masterLeaveType = leaveProcessorDependencies.getMasterLeaveTypeDAO().findById(leaveType.getId())
				.get();
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

	private EmployeeLeaveApplied leaveApplyAction(ApplyLeaveRequestVO request, MasterLeaveType leaveType, Long empId)
			throws HRMSException, ParseException {
		EmployeeLeaveApplied leavesAppliedEnity;
		log.info(" == ACTION -> LEAVE_APPLY << ==");
		Employee employee = leaveProcessorDependencies.getEmployeeDAO().findById(empId).get();
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
		MasterLeaveType masterLeaveType = leaveProcessorDependencies.getLeaveTypeDAO().findById(leaveType.getId())
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
			employeeLeaveDetail = leaveProcessorDependencies.getEmpLeaveDetailsDAO()
					.findEmployeeLeaveByEIDYEAR(employee.getId(), leaveType.getId(), HRMSDateUtil.getCurrentYear());
			if (employeeLeaveDetail == null) {
				employeeLeaveDetail = new EmployeeLeaveDetail();
				employeeLeaveDetail.setEmployee(employee);
				employeeLeaveDetail.setMasterLeaveType(masterLeaveType);
				employeeLeaveDetail.setYear(HRMSDateUtil.getCurrentYear());
				employeeLeaveDetail.setOrgId(employee.getOrgId());
				employeeLeaveDetail = leaveProcessorDependencies.getEmpLeaveDetailsDAO().save(employeeLeaveDetail);
			} else {

			}
		}

		/*
		 * If the above condition passes successfully then it will check if the other
		 * leave applied has been credited or not
		 */
		employeeLeaveDetail = leaveProcessorDependencies.getEmpLeaveDetailsDAO()
				.findEmployeeLeaveByEIDYEAR(employee.getId(), leaveType.getId(), HRMSDateUtil.getCurrentYear());

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
			log.info(" == >> Leave Auto APPROVED << == ");
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_AUTO_APPROVED);
			leavesAppliedEnity.setDateOfApproverAction(new Date());
			leavesAppliedEnity.setRemark(IHRMSConstants.LeaveRemarkForAutoApproved);

			employeeLeaveDetail.setNumberOfDaysAvailed(totalAvailedLeave);
			employeeLeaveDetail.setLeaveAvailable(leaveBalance);

			leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().save(leavesAppliedEnity);
			leaveProcessorDependencies.getEmpLeaveDetailsDAO().save(employeeLeaveDetail);

			String managerEmailId = employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
			String employeeEmailID = employee.getOfficialEmailId();
			String ccEmailId = employeeEmailID + ";" + request.getLeaveApplied().getCc();
			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{rootIp}", baseURL);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContent_cc = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_LeaveApply_CC);
			// System.out.println("********************&&&&&&"+employeeLeaveDetail.getMasterLeaveType().getLeaveGrantStatus()+"&&&&&&&***********************");
			if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("On Duty")) {
				leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, ccEmailId, mailContent_cc,
						IHRMSConstants.MailSubject_OnDutyApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("Work From Home")) {
				leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, ccEmailId, mailContent_cc,
						IHRMSConstants.MailSubject_WorkFromHomeApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else {
				leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, ccEmailId, mailContent_cc,
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
			leavesAppliedEnity.setOrgId(employee.getOrgId());
			leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().save(leavesAppliedEnity);
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_PENDING);
			leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().save(leavesAppliedEnity);
			leaveProcessorDependencies.getEmpLeaveDetailsDAO().save(employeeLeaveDetail);

			// Email sender

			String managerEmailId = employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
			String ccEmailId = request.getLeaveApplied().getCc();

			Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
			placeHolderMapping.put("{rootIp}", baseURL + "/api");
			placeHolderMapping.put("{websiteURL}", baseURL);

			String mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_LeaveApply);

			// Sending email to Recipient
			if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("On Duty")) {
				leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, null, mailContent,
						IHRMSConstants.MailSubject_OnDutyApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else if (employeeLeaveDetail.getMasterLeaveType().getLeaveTypeName().equals("Work From Home")) {
				leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, null, mailContent,
						IHRMSConstants.MailSubject_WorkFromHomeApplication,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());
			} else {
				leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, null, mailContent,
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
						EmployeeCreditLeaveDetail empCreditLeaveDetailEntity = leaveProcessorDependencies
								.getCreditLeaveDAO().findById(employeeCreditLeaveDetail.getId()).get();
						float updatedLeaveAvailable = empCreditLeaveDetailEntity.getLeaveAvailable()
								- IHRMSConstants.COMP_OFF_SESSION_VALUE;
						empCreditLeaveDetailEntity.setLeaveAvailable(updatedLeaveAvailable);
						empCreditLeaveDetailEntity.setUpdatedBy(leavesAppliedEnity.getEmployee().getId().toString());
						empCreditLeaveDetailEntity.setUpdatedDate(new Date());
						leaveProcessorDependencies.getCreditLeaveDAO().save(empCreditLeaveDetailEntity);
						// add mapping of credit leave and leave applied
						EmployeeCreditAppliedLeaveMapping creditAppliedLeaveMapping = new EmployeeCreditAppliedLeaveMapping();
						creditAppliedLeaveMapping.setEmployeeLeaveApplied(leavesAppliedEnity);
						creditAppliedLeaveMapping.setCreditLeaveDetail(empCreditLeaveDetailEntity);
						creditAppliedLeaveMapping.setCreatedBy(leavesAppliedEnity.getEmployee().getId().toString());
						creditAppliedLeaveMapping.setCreatedDate(new Date());
						creditAppliedLeaveMapping.setIsActive(IHRMSConstants.isActive);
						leaveProcessorDependencies.getCreditAppliedLeaveMappingDAO().save(creditAppliedLeaveMapping);
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
			Employee emp = leaveProcessorDependencies.getEmployeeDAO()
					.findById(leavesAppliedEnity.getEmployee().getId()).get();
			long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();

			if (calFromDate.before(calToDate)) {
				Boolean isNewDay = true;
				calFromDateLoopingSameDate = calFromDateLooping;

				while (calFromDateLooping.before(calToDatePlusOne)) {
					// checking if current day is week of or holiday
					if (!"WD".equalsIgnoreCase(leaveProcessorDependencies.getEmployeeLeaveDetailsService()
							.isWeeklyOffOrHoliday(HRMSDateUtil.format(calFromDateLooping.getTime(),
									IHRMSConstants.FRONT_END_DATE_FORMAT), orgId, emp.getId()))) {
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

	protected Map<Long, Map<Integer, EmployeeCreditLeaveDetail>> getEmployeeCompoffListFromCreditLeave(
			Employee employee) throws HRMSException {

		List<Object> creditLeaveDetails = null;
		List<EmployeeCreditLeaveDetail> creditLeaveDetails2 = null;
		List<EmployeeCreditLeaveDetail> creditLeaveDetailsForMap = null;
		Map<Long, Map<Integer, EmployeeCreditLeaveDetail>> mapCreditLeaveEntityWise = null;
		if (!HRMSHelper.isNullOrEmpty(employee) && !HRMSHelper.isLongZero(employee.getId())) {
			creditLeaveDetails = new ArrayList<Object>();
			creditLeaveDetails2 = new ArrayList<EmployeeCreditLeaveDetail>();
			creditLeaveDetails = (List<Object>) leaveProcessorDependencies.getCreditLeaveDAO()
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
			masterLeaveType = leaveProcessorDependencies.getMstLeaveTypeDAO()
					.findByIdAndIsActive(requestVo.getLeaveTypeId(), IHRMSConstants.isActive);
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

			if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate)
					&& !HRMSHelper.isLongZero(fSession) && !HRMSHelper.isLongZero(tSession)
					&& !HRMSHelper.isNullOrEmpty(orgId) && !HRMSHelper.isNullOrEmpty(divId)
					&& !HRMSHelper.isNullOrEmpty(branchId) && !HRMSHelper.isNullOrEmpty(empId)
					&& !HRMSHelper.isNullOrEmpty(departmentId)) {

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
//							You can't apply for Zero Leave Days!
//							throw new HRMSException(IHRMSConstants.NotValidZeroDayLeaveCode,
//									IHRMSConstants.NotValidZeroDayLeaveMessage);
							throw new HRMSException(1506, ResponseCode.getResponseCodeMap().get(1506));
						}
					} else {
						// wrong date selection
//						throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.NotValidDateMessage);
						throw new HRMSException(1502, ResponseCode.getResponseCodeMap().get(1502));
					}

				} else {
					throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.NotValidDateMessage);
				}

			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}
		response.setResponseMessage(IHRMSConstants.successMessage);
		response.setResponseCode(1200);
		response.setResponseBody(leaveCount);

		log.info("Exit from calculateLeave method");

		return response;

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

	@Override
	public HRMSBaseResponse<?> cancelLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException {

		log.info("Inside cancelLeave method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		EmployeeLeaveApplied leavesAppliedEnity;
		log.info(" == ACTION -> LEAVE_CANCELL << ==");

		// input validation
		// employeeAuthorityHelper.cancleLeaveInputValidation(request);

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		MasterLeaveType leaveType = leaveProcessorDependencies.getMstLeaveTypeDAO()
				.findByIdAndIsActive(request.getLeaveApplied().getLeaveTypeId(), IHRMSConstants.isActive);

		leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
				.findById(request.getLeaveApplied().getId()).get();
		Long leaveEmpId = leavesAppliedEnity.getEmployee().getId();

		if (!empId.equals(leaveEmpId)) {
			throw new HRMSException(1509, ResponseCode.getResponseCodeMap().get(1509));
		}

		if (leavesAppliedEnity != null
				&& leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {

//			leavesAppliedEnity.setUpdatedBy(request.getUpdatedBy());
			leavesAppliedEnity.setUpdatedDate(new Date());
			leavesAppliedEnity.setReasonForCancel(request.getLeaveApplied().getReasonForCancel());
			leavesAppliedEnity.setDateOfCancle(new Date());
			leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_CANCELLED);

			leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().save(leavesAppliedEnity);

			EmployeeLeaveDetail emp = leaveProcessorDependencies.getEmpLeaveDetailsDAO()
					.findEmployeeLeaveByEIDYEARAndOrgId(leavesAppliedEnity.getEmployee().getId(), leaveType.getId(),
							HRMSDateUtil.getCurrentYear(), SecurityFilter.TL_CLAIMS.get().getOrgId());
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
				if (!(leaveType.getLeaveTypeCode().equals(ELeaveTypeCode.ONDT.name())
						|| (leaveType.getLeaveTypeCode().equals(ELeaveTypeCode.LOPL.name()))
						|| (leaveType.getLeaveTypeCode().equals(ELeaveTypeCode.WRHM.name())))) {

					float leaveAfterCreditedBalance = leaveAvaialble + leaveToBeCredited;
					emp.setLeaveAvailable(leaveAfterCreditedBalance);
				}
				float availedLeavesAfterWithdrawCredit = leaveAvailed - leaveToBeCredited;

				emp.setNumberOfDaysAvailed(availedLeavesAfterWithdrawCredit);

				leaveProcessorDependencies.getEmployeeLeaveDetailsDAO().save(emp);
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
			leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, null, employeeMailContent,
					IHRMSConstants.MailSubject_LeaveCancelled,
					leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
							.getId(),
					leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());

			/*
			 * EmailSender.toSendEmail(managerEmailId, null, managerMailContent,
			 * IHRMSConstants.MailSubject_LeaveCancelled);
			 */

			leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, null, managerMailContent,
					IHRMSConstants.MailSubject_LeaveCancelled,
					leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
							.getId(),
					leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());

			// if (ccEmailId != null) {
			if (!HRMSHelper.isNullOrEmpty(ccEmailId)) {
				// EmailSender.toSendEmail(ccEmailId, null, managerMailContent,
				// IHRMSConstants.MailSubject_LeaveCancelled);
				leaveProcessorDependencies.getEmailsender().toPersistEmail(ccEmailId, null, managerMailContent,
						IHRMSConstants.MailSubject_LeaveCancelled,
						leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision()
								.getId(),
						leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
			}

		} else if (!HRMSHelper.isNullOrEmpty(leavesAppliedEnity)
				&& leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_CANCELLED)) {
			// Leave has already been cancelled, throw an error
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1204));

		} else {
			throw new HRMSException(IHRMSConstants.ActivityProcessedCode, IHRMSConstants.ErrorInCancellation);
		}
		log.info("Exit from cancelLeave method");

		response.setResponseCode(1200);
		response.setResponseMessage(IHRMSConstants.LeaveCancelActionSuccessMessage);
		// response.setApplicationVersion(applicationVersion);
		return response;
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
	public HRMSBaseResponse<AvailableLeavesVO> getAvailableLeaveProcess(Long leaveTypeId) throws HRMSException {

		log.info("Inside getAvailableLeave method");
		HRMSBaseResponse<AvailableLeavesVO> response = new HRMSBaseResponse<>();

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		log.info(">>>>>>>>>>>>inside getLeaveAvailableByEmployeeAndLeaveType()<<<<<<<<<<<<<<<<<");

		if (!HRMSHelper.isLongZero(empId) && !HRMSHelper.isLongZero(leaveTypeId)) {

			// next modified by SSW on 05 Dec 2018 for : fetching leave details yearwise
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			// upto this added and next DAO call modified, year parameter added
			EmployeeLeaveDetail employeeLeaveDetail = leaveProcessorDependencies.getEmployeeLeaveDetailDAO()
					.findLeaveAvailableByEmpIdAndLeaveType(empId, leaveTypeId, IHRMSConstants.isActive, year);

			AvailableLeavesVO availableLeavesVO = null;

			if (!HRMSHelper.isNullOrEmpty(employeeLeaveDetail)) {

				availableLeavesVO = LeaveTransformUtil.ConvertToAvailableLeavesVO(employeeLeaveDetail);

			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseBody(availableLeavesVO);
//			response.setApplicationVersion(applicationVersion);

		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		log.info("Exit from getAvailableLeave method");

		return response;
	}

	@Override
	public HRMSBaseResponse<?> withdrawLeaveLeaveProcess(ApplyLeaveRequestVO request)
			throws HRMSException, ParseException {
		log.info("Inside withdrawLeave Method");
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = null;
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		EmployeeLeaveApplied leavesAppliedEnity;
		leaveProcessorDependencies.getEmployeeAuthorityHelper().withdrawLeaveInputValidation(request);
		leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
				.findByIdAndOrgId(request.getLeaveApplied().getId(), SecurityFilter.TL_CLAIMS.get().getOrgId());
		Long leaveEmpId = leavesAppliedEnity.getEmployee().getId();

		if (!empId.equals(leaveEmpId)) {
			throw new HRMSException(1511, ResponseCode.getResponseCodeMap().get(1511));
		}

		Date currentDate = new Date();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if (formatter.parse(formatter.format(currentDate)).after(leavesAppliedEnity.getFromDate())) {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.CannotApproveWithdraw);
		}
		if (leavesAppliedEnity != null
				&& leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_APPROVED)) {

			leavesAppliedEnity = saveFieldsForWithdrawLeave(request, leavesAppliedEnity);
			sendMailOnWithdrawLeave(leavesAppliedEnity);
		} else {
			throw new HRMSException(1512, ResponseCode.getResponseCodeMap().get(1512));
		}
		log.info("Exit from withdrawLeave Method");

		setResponseWithdrawLeave(response);
		return response;
	}

	private void setResponseWithdrawLeave(HRMSBaseResponse<?> response) {
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
	}

	private void sendMailOnWithdrawLeave(EmployeeLeaveApplied leavesAppliedEnity) {
		String managerEmailId = leavesAppliedEnity.getEmployee().getEmployeeReportingManager().getReporingManager()
				.getOfficialEmailId();
		String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
		String ccEmailId = leavesAppliedEnity.getCc();

		Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
		placeHolderMapping.put("{rootIp}", baseURL + "/api");
		placeHolderMapping.put("{websiteURL}", baseURL);
		String mailContentForManager = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_WithdrawLeaveRequestToManager);
		String mailContentForEmployee = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_WithdrawLeaveConfirmationToEmployee);

		leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, null, mailContentForManager,
				IHRMSConstants.MailSubject_LeaveWithdrawRequest,
				leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
		leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId, mailContentForEmployee,
				IHRMSConstants.MailSubject_LeaveWithdrawRequest,
				leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
	}

	private EmployeeLeaveApplied saveFieldsForWithdrawLeave(ApplyLeaveRequestVO request,
			EmployeeLeaveApplied leavesAppliedEnity) {
		leavesAppliedEnity.setUpdatedDate(new Date());
		leavesAppliedEnity.setReasonForWithdrawn(request.getLeaveApplied().getReasonForWithdrawn());
		leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_WD_PENDING);
		leavesAppliedEnity.setDateOfWithdrawn(new Date());
		leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().save(leavesAppliedEnity);
		return leavesAppliedEnity;
	}

	@Override
	public HRMSBaseResponse<?> approveLeaveProcess(List<ApplyLeaveRequestVO> requestList) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "approveLeaveProcess");
		}
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		for (ApplyLeaveRequestVO request : requestList) {
			leaveProcessorDependencies.getEmployeeAuthorityHelper().approvedLeaveInputValidation(request);
			List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

			if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
				Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
				Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
				List<EmployeeReportingManager> employeesUnderManager = leaveProcessorDependencies
						.getReportingManagerDAO().findByreporingManager(empId);
				Optional<EmployeeLeaveApplied> leavesAppliedOptional = leaveProcessorDependencies
						.getEmployeeLeaveAppliedDAO().findById(request.getLeaveApplied().getId());
				if (leavesAppliedOptional.isPresent()) {
					EmployeeLeaveApplied leavesAppliedEnity = leavesAppliedOptional.get();
					Long requestedEmployeeId = leavesAppliedEnity.getEmployee().getId();
					long matchCount = employeesUnderManager.stream()
							.filter(e -> e.getEmployee().getId().equals(requestedEmployeeId)).count();

					matchCountForApproveLeave(request, empId, orgId, leavesAppliedEnity, matchCount);
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}
			} else {
				throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
			}
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1535));
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "approveLeaveProcess");
		}
		return response;
	}

	private void matchCountForApproveLeave(ApplyLeaveRequestVO request, Long empId, Long orgId,
			EmployeeLeaveApplied leavesAppliedEnity, long matchCount) throws HRMSException {
		if (matchCount > 0) {
			Employee loggedInEmployee = leaveProcessorDependencies.getEmployeeDAO().findByEmpIdAndOrgId(empId, orgId);
			log.info(" == ACTION -> LEAVE_APPROVE << ==");
			if (leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
				Employee employee = leaveProcessorDependencies.getEmployeeDAO()
						.findById(leavesAppliedEnity.getEmployee().getId()).get();
				leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_APPROVED);
				leavesAppliedEnity.setDateOfApproverAction(new Date());
				leavesAppliedEnity.setUpdatedBy(String.valueOf(loggedInEmployee.getId()));
				leavesAppliedEnity.setUpdatedDate(new Date());
				/**
				 * saving approver comment on normal leave approve action in reason for reject
				 */
				leavesAppliedEnity
						.setReasonForReject(!HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForReject())
								? request.getLeaveApplied().getReasonForReject()
								: null);
				leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().save(leavesAppliedEnity);
				/**
				 * Email Sender
				 */
				approveLeaveEmailSender(leavesAppliedEnity);
			} else {
				throw new HRMSException(1513, ResponseCode.getResponseCodeMap().get(1513));
			}
		} else {
			throw new HRMSException(1520, ResponseCode.getResponseCodeMap().get(1520));
		}
	}

	private void approveLeaveEmailSender(EmployeeLeaveApplied leavesAppliedEnity) {
		String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
		String ccEmailId = leavesAppliedEnity.getCc();
		Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
		placeHolderMapping.put("{websiteURL}", baseURL);
		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_LeaveApproved);

		leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId, mailContent,
				IHRMSConstants.MailSubject_LeaveApproved,
				leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
	}

	@Override
	public HRMSBaseResponse<VOAppliedLeaveCount> acalculateLeaveProcess(LeaveCalculationRequestVO requestVo)
			throws HRMSException {

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
		// long divId = voLeaveCalculationRequest.getDivisionId();
		// long branchId = voLeaveCalculationRequest.getBranchId();

		if (!HRMSHelper.isNullOrEmpty(requestVo)) {

//			long empId = requestVo.getEmployeeId();

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
			// long departmentId =
			// employeeDAO.findDepartmentByEmployeeId(empId).getDepartment().getId();

			if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate)
					&& !HRMSHelper.isLongZero(fSession) && !HRMSHelper.isLongZero(tSession)
					&& !HRMSHelper.isNullOrEmpty(orgId) && !HRMSHelper.isNullOrEmpty(divId)
					&& !HRMSHelper.isNullOrEmpty(branchId) && !HRMSHelper.isNullOrEmpty(empId)
					&& !HRMSHelper.isNullOrEmpty(departmentId)) {

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
//							You can't apply for Zero Leave Days!
//							throw new HRMSException(IHRMSConstants.NotValidZeroDayLeaveCode,
//									IHRMSConstants.NotValidZeroDayLeaveMessage);
							throw new HRMSException(1506, ResponseCode.getResponseCodeMap().get(1506));
						}
					} else {
						// wrong date selection
//						throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.NotValidDateMessage);
						throw new HRMSException(1502, ResponseCode.getResponseCodeMap().get(1502));
					}

				} else {
					throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.NotValidDateMessage);
				}

			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}
		response.setResponseMessage(IHRMSConstants.successMessage);
		response.setResponseCode(1200);
		response.setResponseBody(leaveCount);
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);

		log.info("Exit from calculateLeave method");

		return response;
	}

	@Override
	public HRMSBaseResponse<?> rejectLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException {

		log.info("Inside  rejectLeave method");

		// input validation
		leaveProcessorDependencies.getEmployeeAuthorityHelper().rejectLeaveInputValidation(request);
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
			// Retrieve the manager's ID
			Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			// Find all employee IDs under this manager
			List<EmployeeReportingManager> employeesUnderManager = leaveProcessorDependencies.getReportingManagerDAO()
					.findByreporingManager(employeeId);
			Optional<EmployeeLeaveApplied> leavesAppliedOptional = leaveProcessorDependencies
					.getEmployeeLeaveAppliedDAO().findById(request.getLeaveApplied().getId());
			if (leavesAppliedOptional.isPresent()) {
				EmployeeLeaveApplied leavesAppliedEnity = leavesAppliedOptional.get();
				leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
						.findById(request.getLeaveApplied().getId()).get();
				// Find the employee ID for the leave request
				Long requestedEmployeeId = leavesAppliedEnity.getEmployee().getId();
				// Check if the requested employee is under the management hierarchy of the
				// manager
				long matchCount = employeesUnderManager.stream()
						.filter(e -> e.getEmployee().getId().equals(requestedEmployeeId)).count();

				if (matchCount > 0) {
					log.info(" == ACTION -> LEAVE_REJECT << ==");
					if (leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
						leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_REJECT);
						leavesAppliedEnity.setDateOfApproverAction(new Date());
						leavesAppliedEnity.setUpdatedBy(String.valueOf(empId));
						leavesAppliedEnity.setUpdatedDate(new Date());
						leavesAppliedEnity.setReasonForReject(request.getLeaveApplied().getReasonForReject());

						EmployeeLeaveDetail emp = leaveProcessorDependencies.getEmpLeaveDetailsDAO()
								.findEmployeeLeaveByEIDYEAR(leavesAppliedEnity.getEmployee().getId(),
										leavesAppliedEnity.getMasterLeaveType().getId(), HRMSDateUtil.getCurrentYear());

						float leaveAvaialble = 0;
						if (emp.getLeaveAvailable() != null) {
							leaveAvaialble = emp.getLeaveAvailable();
						}

						float leaveToBeCredited = leavesAppliedEnity.getNoOfDays();

						float leaveAvailed = 0;
						if (emp.getNumberOfDaysAvailed() != null) {
							leaveAvailed = emp.getNumberOfDaysAvailed();
						}

						float availedLeavesAfterWithdrawCredit = leaveAvailed - leaveToBeCredited;

						emp.setNumberOfDaysAvailed(availedLeavesAfterWithdrawCredit);

						if (!(leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
								.equals(ELeaveTypeCode.ONDT.name())
								|| (leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
										.equals(ELeaveTypeCode.LOPL.name()))
								|| (leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
										.equals(ELeaveTypeCode.WRHM.name())))) {
							float leaveAfterCreditedBalance = leaveAvaialble + leaveToBeCredited;
							emp.setLeaveAvailable(leaveAfterCreditedBalance);
						}

						leaveProcessorDependencies.getEmpLeaveDetailsDAO().save(emp);
						leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
								.save(leavesAppliedEnity);

						// next added by SSW on 16Nov2018 for resetting compoff balance
						// checking if leave type is compoff
						if (leavesAppliedEnity.getMasterLeaveType().getLeaveTypeCode()
								.equalsIgnoreCase(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE)) {
							try {
								List<EmployeeCreditAppliedLeaveMapping> appliedLeaveMappings = getListOfCreditAppliedLeaveMapping(
										leavesAppliedEnity);
								int updateResult = updateLeaveAvailableOnGrantLeaveCancelWithdrawn(
										appliedLeaveMappings);
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
						Map<String, String> placeHolderMapping = HRMSRequestTranslator
								.createPlaceHolderMap(leavesAppliedEnity);
						placeHolderMapping.put("{websiteURL}", baseURL);
						String mailContent = HRMSHelper.replaceString(placeHolderMapping,
								IHRMSEmailTemplateConstants.Template_LeaveReject);

						// logger.info("+++++++++++++++++++++++++++ MAIL CONTENT
						// ++++++++++++++++++++++++++++++++");
						// logger.info(mailContent);
						// EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
						// IHRMSConstants.MailSubject_LeaveRejected);
						leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId,
								mailContent, IHRMSConstants.MailSubject_LeaveRejected,
								leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail()
										.getDivision().getId(),
								leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization()
										.getId());
					} else {
						log.info(" == ACTION -> INVALID ACTION ==");
						throw new HRMSException(1515, ResponseCode.getResponseCodeMap().get(1515));
					}

				} else {
					// Throw an exception if the requested employee is not under the management
					// hierarchy
					throw new HRMSException(1520, ResponseCode.getResponseCodeMap().get(1520));
				}
			} else {
				// If data not found, throw an exception with a message
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

		} else {
			// Throw an exception if the user is not a manager
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1536));
//		response.setApplicationVersion(applicationVersion);
		log.info("Exit from rejectLeave method");
		return response;
	}

	@Override
	public HRMSBaseResponse<?> withdrawApproveLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException {

		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "withdrawApproveLeaveProcess");
		}

		// input validation
		leaveProcessorDependencies.getEmployeeAuthorityHelper().withdrawApprovedLeaveInputValidation(request);
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
			Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			List<EmployeeReportingManager> employeesUnderManager = leaveProcessorDependencies.getReportingManagerDAO()
					.findByreporingManager(employeeId);
			Optional<EmployeeLeaveApplied> leavesAppliedOptional = leaveProcessorDependencies
					.getEmployeeLeaveAppliedDAO().findById(request.getLeaveApplied().getId());
			if (leavesAppliedOptional.isPresent()) {
				EmployeeLeaveApplied leavesAppliedEnity = leavesAppliedOptional.get();
				leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
						.findByIdAndOrgId(request.getLeaveApplied().getId(), orgId);
				Long requestedEmployeeId = leavesAppliedEnity.getEmployee().getId();
				long matchCount = employeesUnderManager.stream()
						.filter(e -> e.getEmployee().getId().equals(requestedEmployeeId)).count();

				matchCountMethodForWthdrawApprove(request, matchCount);
			} else {
				log.info(" == ACTION -> INVALID ACTION ==");
				throw new HRMSException(1516, ResponseCode.getResponseCodeMap().get(1516));
			}
		} else {
			log.info(" == ACTION -> INVALID ACTION ==");
			throw new HRMSException(1516, ResponseCode.getResponseCodeMap().get(1516));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "withdrawApproveLeaveProcess");
		}
		return response;
	}

	private void matchCountMethodForWthdrawApprove(ApplyLeaveRequestVO request, long matchCount) throws HRMSException {
		EmployeeLeaveApplied leavesAppliedEnity;
		if (matchCount > 0) {

			log.info(" == ACTION -> LEAVE_WITHDRAW_APPROVE << ==");
			leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
					.findById(request.getLeaveApplied().getId()).get();
			Employee employee = leaveProcessorDependencies.getEmployeeDAO()
					.findById(leavesAppliedEnity.getEmployee().getId()).get();

			EmployeeLeaveDetail emp = leaveProcessorDependencies.getEmployeeLeaveDetailsDAO()
					.findEmployeeLeaveByEIDYEAR(employee.getId(), leavesAppliedEnity.getMasterLeaveType().getId(),
							HRMSDateUtil.getCurrentYear());
			if (emp == null) {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode,
						IHRMSConstants.YouCannotApplyTheSelectedLeaveMessage);
			}

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
				leaveProcessorDependencies.getEmpLeaveDetailsDAO().save(emp);

				leavesAppliedEnity.setApproverActionDateWithdrawn(new Date());
				leavesAppliedEnity
						.setApproverCommentOnWithdrawn(request.getLeaveApplied().getApproverCommentOnWithdrawn());
				leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_WD_APPROVED);
				leavesAppliedEnity.setDateOfApproverAction(new Date());
				leavesAppliedEnity.setUpdatedBy(String.valueOf(
						leavesAppliedEnity.getEmployee().getEmployeeReportingManager().getReporingManager().getId()));
				leavesAppliedEnity.setUpdatedDate(new Date());
				leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().save(leavesAppliedEnity);

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
				emailSenderWithdrawApprove(leavesAppliedEnity);
			} else {
				log.info(" == ACTION -> INVALID ACTION ==");
				throw new HRMSException(1516, ResponseCode.getResponseCodeMap().get(1516));
			}
		} else {
			log.info(" == ACTION -> INVALID ACTION ==");
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	private void emailSenderWithdrawApprove(EmployeeLeaveApplied leavesAppliedEnity) {
		String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
		String ccEmailId = leavesAppliedEnity.getCc();
		Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
		placeHolderMapping.put("{websiteURL}", baseURL);
		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_LeaveWithdrawApproved);

		leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId, mailContent,
				IHRMSConstants.MailSubject_LeaveWithdrawApproved,
				leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
	}

	@Override
	public HRMSBaseResponse<?> withdrawRejectLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException {

		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "withdrawRejectLeaveProcess");
		}
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		leaveProcessorDependencies.getEmployeeAuthorityHelper().WithdrawRejectLeaveInputValidation(request);

		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
			Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
			List<EmployeeReportingManager> employeesUnderManager = leaveProcessorDependencies.getReportingManagerDAO()
					.findByreporingManager(employeeId);
			Optional<EmployeeLeaveApplied> leavesAppliedOptional = leaveProcessorDependencies
					.getEmployeeLeaveAppliedDAO().findById(request.getLeaveApplied().getId());
			if (leavesAppliedOptional.isPresent()) {
				EmployeeLeaveApplied leavesAppliedEnity = leavesAppliedOptional.get();
				Long requestedEmployeeId = leavesAppliedEnity.getEmployee().getId();
				long matchCount = employeesUnderManager.stream()
						.filter(e -> e.getEmployee().getId().equals(requestedEmployeeId)).count();

				matchCountWithdarwReject(request, empId, orgId, matchCount);
			} else {
				throw new HRMSException(1517, ResponseCode.getResponseCodeMap().get(1517));
			}
		} else {
			throw new HRMSException(1517, ResponseCode.getResponseCodeMap().get(1517));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "withdrawRejectLeaveProcess");
		}
		return response;
	}

	private void matchCountWithdarwReject(ApplyLeaveRequestVO request, Long empId, Long orgId, long matchCount)
			throws HRMSException {
		EmployeeLeaveApplied leavesAppliedEnity;
		if (matchCount > 0) {
			leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO()
					.findByIdAndOrgId(request.getLeaveApplied().getId(), orgId);
			if (leavesAppliedEnity.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_WD_PENDING)) {
				leavesAppliedEnity.setApproverActionDateWithdrawn(new Date());
				leavesAppliedEnity
						.setApproverCommentOnWithdrawn(request.getLeaveApplied().getApproverCommentOnWithdrawn());
				leavesAppliedEnity.setUpdatedBy(String.valueOf(empId));
				leavesAppliedEnity.setUpdatedDate(new Date());
				leavesAppliedEnity.setLeaveStatus(IHRMSConstants.LeaveStatus_WD_REJECTED);
				leavesAppliedEnity = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().save(leavesAppliedEnity);

				/**
				 * Email Sender
				 */
				emailSenderWithdrawReject(leavesAppliedEnity);
			} else {
				throw new HRMSException(1517, ResponseCode.getResponseCodeMap().get(1517));
			}
		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	private void emailSenderWithdrawReject(EmployeeLeaveApplied leavesAppliedEnity) {
		String employeeEmailId = leavesAppliedEnity.getEmployee().getOfficialEmailId();
		String ccEmailId = leavesAppliedEnity.getCc();
		Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMap(leavesAppliedEnity);
		placeHolderMapping.put("{websiteURL}", baseURL);
		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_WithdrawLeaveRejected);
		leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId, mailContent,
				IHRMSConstants.MailSubject_LeaveWithdrawRejected,
				leavesAppliedEnity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				leavesAppliedEnity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
	}

	@Override
	public HRMSBaseResponse<?> applyGrantLeaveProcess(ApplyGrantLeaveRequestVO request)
			throws HRMSException, ParseException {

		log.info("Inside grantLeaveApply method");
		leaveProcessorDependencies.getEmployeeAuthorityHelper().applyGrantLeaveInputValidation(request);

		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity = null;
		MasterLeaveType masterLeaveTypeEntity = leaveProcessorDependencies.getMasterLeaveTypeDAO()
				.findByLeaveTypeCodeAndOrgId(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE, orgId);
		Employee employeeEntity = leaveProcessorDependencies.getEmployeeDAO().findById(empId).get();

		/*
		 * Calculate leave
		 */
		LeaveCalculationRequestVO calculationRequest = new LeaveCalculationRequestVO();
		calculationRequest.setFromDate(request.getFromDate());
		calculationRequest.setToDate(request.getToDate());
		calculationRequest.setToSession(request.getToSession());
		calculationRequest.setLeaveTypeId(request.getLeaveTypeId());
		calculationRequest.setFromSession(request.getFromSession());
		calculationRequest.setOrganizationId(employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());
		calculationRequest.setNumberOfSession(masterLeaveTypeEntity.getNumberOfSession());
		HRMSBaseResponse calculateGrantLeaveResponse = calculateGrantLeaveProcess(calculationRequest);

		VOAppliedLeaveCount calculateLeave = (VOAppliedLeaveCount) calculateGrantLeaveResponse.getResponseBody();

		if (request.getNoOfDays() != calculateLeave.getCalculatedLeave()) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + "- Incorrect no.of days calculation");
		}

		// Parse the fromDate and toDate from the request
		Date fromDate = HRMSDateUtil.parse(request.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date toDate = HRMSDateUtil.parse(request.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);

		// Check for overlapping leave
		EmployeeGrantLeaveDetail overlappingLeave = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.findOverlappingLeave(fromDate, toDate, empId, IHRMSConstants.LeaveStatus_CANCELLED,
						IHRMSConstants.LeaveStatus_REJECT);

		if (overlappingLeave != null) {
			// There is overlapping leave, throw an error
			throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.LeaveOverlapsMessage);
		}

		// Retrieve the joining date of the employee
		Date employeeJoiningDate = employeeEntity.getCandidate().getCandidateProfessionalDetail().getDateOfJoining();

		// Compare the leave start date with the employee's joining date
		if (fromDate.before(employeeJoiningDate)) {
			// Leave cannot be applied before the employee's joining date
			throw new HRMSException(1506, ResponseCode.getResponseCodeMap().get(1597));
		}

//		EmployeeGrantLeaveDetail leaveDetail = employeeGrantLeaveDAO.findByFromDateAndEmployee(
//				HRMSDateUtil.parse(request.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT), employeeEntity);
//
//		if (!HRMSHelper.isNullOrEmpty(leaveDetail)) {
//			throw new HRMSException(IHRMSConstants.failedCode, IHRMSConstants.LeaveOverlapsMessage);
//		}

		if (!HRMSHelper.isNullOrEmpty(request)) {
			employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
					.findById(request.getId());

			/*
			 * changed by ssw on 03jan2018 for :: grant leave action : apply
			 */
			boolean grantLeaveApplyAllowedStatus = false;

			/*
			 * finding candidate details after this commented on 03jan2018 morn
			 */
			Candidate candidate = leaveProcessorDependencies.getCandidateDAO()
					.findById(employeeEntity.getCandidate().getId()).get();
			/*
			 * finding employee branch, division, org
			 */
			VOLeaveCalculationRequest leaveCalculation = new VOLeaveCalculationRequest();
			leaveCalculation.setBranchId(candidate.getCandidateProfessionalDetail().getBranch().getId());
			leaveCalculation.setDivisionId(candidate.getCandidateProfessionalDetail().getDivision().getId());
			leaveCalculation
					.setFromDate(HRMSDateUtil.parse(request.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			leaveCalculation.setFromSession(request.getFromSession());
			leaveCalculation.setLeaveTypeId(masterLeaveTypeEntity.getId());
			leaveCalculation.setOrganizationId(candidate.getLoginEntity().getOrganization().getId());
			leaveCalculation.setToDate(HRMSDateUtil.parse(request.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			leaveCalculation.setToSession(request.getToSession());
			/*
			 * weekly off org, branch, div wise
			 */

			List<OrganizationWeekoff> weekoffList = leaveProcessorDependencies.getOrganizationWeekoffDAO()
					.getWeekoffByOrgBranchDivDeptId(candidate.getLoginEntity().getOrganization().getId(),
							candidate.getCandidateProfessionalDetail().getDivision().getId(),
							candidate.getCandidateProfessionalDetail().getBranch().getId(),
							candidate.getCandidateProfessionalDetail().getDepartment().getId());

			int workingdays = HRMSHelper.getWorkingDays(leaveCalculation, weekoffList);

			/*
			 * weekly off org, branch, div wise
			 */

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
//					throw new HRMSException(IHRMSConstants.FUTURE_DATE_LEAVE_RESTRICT_CODE,
//							IHRMSConstants.FUTURE_DATE_LEAVE_RESTRICT_MESSAGE);
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

			/*
			 * upto this modified by ssw on 03jan2018
			 */

			/*
			 * condition : employee apply Leave grant
			 */
			employeeGrantLeaveDetailEntity = grantApplyLeaveAction(request, employeeGrantLeaveDetailEntity,
					masterLeaveTypeEntity, employeeEntity);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(IHRMSConstants.LeaveActionSuccessMessage);

		log.info("Exit from  grantLeaveApply method");

		return response;
	}

	private EmployeeGrantLeaveDetail grantApplyLeaveAction(ApplyGrantLeaveRequestVO request,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity, MasterLeaveType masterLeaveTypeEntity,
			Employee employeeEntity) throws HRMSException, ParseException {
		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity)
				&& !HRMSHelper.isNullOrEmpty(masterLeaveTypeEntity)) {
			/* update */
			// employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntity);
			MasterLeaveType masterLeaveTypeEntityUpdate = leaveProcessorDependencies.getMasterLeaveTypeDAO()
					.findById(masterLeaveTypeEntity.getId()).get();
			employeeGrantLeaveDetailEntity = HRMSRequestTranslator
					.translateToEmployeeGrantLeaveDetailEntity(employeeGrantLeaveDetailEntity, request, employeeEntity);
			employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntityUpdate);
			// resultMesage = IHRMSConstants.updatedsuccessMessage;
		} else {
			/* insert */
//			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//			Date fromDate = sdf.parse(request.getLeaveApplied().getFromDate());
//			Date toDate = sdf.parse(request.getLeaveApplied().getToDate());
//			 long dateDiff = (toDate.getTime() - fromDate.getTime())/ 1000 / 60 / 60 / 24;
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
				MasterLeaveType masterLeaveTypeEntityUpdate = leaveProcessorDependencies.getMasterLeaveTypeDAO()
						.findById(masterLeaveTypeEntity.getId()).get();
				// employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntity);
				employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntityUpdate);
				employeeGrantLeaveDetailEntity = HRMSRequestTranslator.translateToEmployeeGrantLeaveDetailEntity(
						employeeGrantLeaveDetailEntity, request, employeeEntity);
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
			// resultMesage = IHRMSConstants.addedsuccessMessage;
		}
		employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_PENDING);
		employeeGrantLeaveDetailEntity.setDateOfApplied(new Date());
		employeeGrantLeaveDetailEntity.setOrgId(employeeEntity.getOrgId());
		employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.save(employeeGrantLeaveDetailEntity);
		/*
		 * Email sender
		 */
		String managerEmailId = employeeEntity.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
		String ccEmailId = request.getCc();

		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
		placeHolderMapping.put("{rootIp}", baseURL + "/api");
		placeHolderMapping.put("{websiteURL}", baseURL);

		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_GrantLeaveApply);

		/*
		 * Sending email to Recipient
		 */

		leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, null, mailContent,
				IHRMSConstants.MailSubject_GrantLeaveApplication,
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());

		return employeeGrantLeaveDetailEntity;
	}

	@Override
	public HRMSBaseResponse<VOAppliedLeaveCount> calculateGrantLeaveProcess(LeaveCalculationRequestVO request)
			throws HRMSException {

		log.info("Inside calculateGrantLeave method");
		// input validation
		leaveProcessorDependencies.getEmployeeAuthorityHelper().calculateGrantLeaveInputValidation(request);

		HRMSBaseResponse<VOAppliedLeaveCount> response = new HRMSBaseResponse();

//		MasterLeaveType masterLeaveType = leaveProcessorDependencies.getMasterLeaveTypeDAO()
//				.findByLeaveTypeCode(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE);
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
//					// wrong date selection
					throw new HRMSException(1502, ResponseCode.getResponseCodeMap().get(1502));
				}
			} else {
				throw new HRMSException(IHRMSConstants.NotValidDateCode, IHRMSConstants.NotValidDateMessage);
			}

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		log.info("Exit from calculateGrantLeave method");

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(leaveCount);
//		response.setApplicationVersion(applicationVersion);
		return response;

	}

	private static Date removeTimeComponent(Date date) {
		// Remove the time component from the date
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			return dateFormat.parse(dateFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	@Override
	public HRMSBaseResponse<?> cancelGrantLeaveProcess(ApplyGrantLeaveRequestVO request) throws HRMSException {

		log.info("Inside  cancelGrantLeave method");
		leaveProcessorDependencies.getEmployeeAuthorityHelper().cancleGrantLeaveInputValidation(request);

		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long grantLeaveId = request.getId();
		if (grantLeaveId == null || grantLeaveId <= 0) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grant Leave Id");
		}

		EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.findByIdOrgId(request.getId(), SecurityFilter.TL_CLAIMS.get().getOrgId());

		Long leaveEmpId = employeeGrantLeaveDetailEntity.getEmployee().getId();

		if (!empId.equals(leaveEmpId)) {
			throw new HRMSException(1509, ResponseCode.getResponseCodeMap().get(1509));
		}

		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity.getLeaveStatus()
				.equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
			employeeGrantLeaveDetailEntity = setCancelGrantLeaveFields(request, empId, employeeGrantLeaveDetailEntity);

			sendMailOnCancelGrantLeave(employeeGrantLeaveDetailEntity);

		} else if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity
				.getLeaveStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_CANCELLED)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1204));

		} else {
			throw new HRMSException(IHRMSConstants.ActivityProcessedCode, IHRMSConstants.ErrorInCancellation);
		}

		response.setResponseCode(1200);
		response.setResponseMessage(IHRMSConstants.LeaveCancelActionSuccessMessage);
		response.setApplicationVersion(leaveProcessorDependencies.applicationVersion);

		log.info("Exit from  cancelGrantLeave method");

		return response;
	}

	private void sendMailOnCancelGrantLeave(EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity) {
		String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
		String managerEmailId = employeeGrantLeaveDetailEntity.getEmployee().getEmployeeReportingManager()
				.getReporingManager().getOfficialEmailId();
		String ccEmailId = employeeGrantLeaveDetailEntity.getCc();

		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
		placeHolderMapping.put("{rootIp}", baseURL);
		placeHolderMapping.put("{websiteURL}", baseURL);
		String employeeMailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_CancleLeaveForEmployee);
		String managerMailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_CancleLeaveForManager);

		leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, null, employeeMailContent,
				IHRMSConstants.MailSubject_GrantLeaveCancelled,
				employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
						.getDivision().getId(),
				employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());

		leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, null, managerMailContent,
				IHRMSConstants.MailSubject_GrantLeaveCancelled,
				employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
						.getDivision().getId(),
				employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization().getId());
	}

	private EmployeeGrantLeaveDetail setCancelGrantLeaveFields(ApplyGrantLeaveRequestVO request, Long empId,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity) {
		employeeGrantLeaveDetailEntity.setUpdatedBy(String.valueOf(empId));
		employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
		employeeGrantLeaveDetailEntity.setReasonForCancel(request.getReasonForCancel());
		employeeGrantLeaveDetailEntity.setDateOfCancel(new Date());
		employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_CANCELLED);
		employeeGrantLeaveDetailEntity.setDateOfCancel(new Date());
		employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.save(employeeGrantLeaveDetailEntity);
		return employeeGrantLeaveDetailEntity;
	}

	@Override
	public HRMSBaseResponse<?> withdrawGrantLeaveProcess(ApplyGrantLeaveRequestVO request) throws HRMSException {

		log.info("Inside GrantLeave method");
		// input validation
		leaveProcessorDependencies.getEmployeeAuthorityHelper().withdrawGrantLeaveInputValidation(request);
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.findById(request.getId());

		Long leaveEmpId = employeeGrantLeaveDetailEntity.getEmployee().getId();
		if (!empId.equals(leaveEmpId)) {
			throw new HRMSException(1509, ResponseCode.getResponseCodeMap().get(1509));
		}

		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity.getLeaveStatus()
				.equalsIgnoreCase(IHRMSConstants.LeaveStatus_APPROVED)) {
			employeeGrantLeaveDetailEntity.setUpdatedBy(String.valueOf(empId));
			employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
			employeeGrantLeaveDetailEntity.setReasonForWithdrawn(request.getReasonForWithdrawn());

			employeeGrantLeaveDetailEntity.setDateOfWithdrawn(new Date());
			employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_WD_PENDING);
			employeeGrantLeaveDetailEntity.setDateOfWithdrawn(new Date());
			employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
					.save(employeeGrantLeaveDetailEntity);

			/**
			 * Email Sender
			 */
			String managerEmailId = employeeGrantLeaveDetailEntity.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getOfficialEmailId();
			String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
			String ccEmailId = employeeGrantLeaveDetailEntity.getCc();

			Map<String, String> placeHolderMapping = HRMSRequestTranslator
					.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
			placeHolderMapping.put("{rootIp}", baseURL + "/api");
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContentForManager = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_WithdrawGrantLeaveRequestToManager);
			String mailContentForEmployee = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_WithdrawLeaveConfirmationToEmployee);

			leaveProcessorDependencies.getEmailsender().toPersistEmail(managerEmailId, null, mailContentForManager,
					IHRMSConstants.MailSubject_GrantLeaveWithdrawRequest,
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
							.getDivision().getId(),
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization()
							.getId());
			leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId,
					mailContentForEmployee, IHRMSConstants.MailSubject_LeaveWithdrawRequest,
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
							.getDivision().getId(),
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization()
							.getId());
		} else {
			throw new HRMSException(1518, ResponseCode.getResponseCodeMap().get(1518));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from  GrantLeave method");

		return response;
	}

	@Override
	public HRMSBaseResponse<?> approveGrantLeaveProcess(ApplyGrantLeaveRequestVO request) throws HRMSException {

		log.info("Inside approveGrantLeave method");
		// input validation
		leaveProcessorDependencies.getEmployeeAuthorityHelper().grantLeaveApprovedInputValidation(request);
		HRMSBaseResponse<?> response = new HRMSBaseResponse();

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.findById(request.getId());

		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity.getLeaveStatus()
				.equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
			/*
			 * next variable is flag. It is used to check whether entry of leave type
			 * applied for comp off is present in EmployeeLeaveDetails. If not, then insert
			 * into it.
			 * 
			 * 
			 */
			EmployeeReportingManager reportingManager = employeeGrantLeaveDetailEntity.getEmployee()
					.getEmployeeReportingManager();
			// Long reportingManagerId = reportingManager.getReporingManager().getId();
			long reportingManagerId = employeeGrantLeaveDetailEntity.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getId();

			if (!empId.equals(reportingManagerId)) {
				throw new HRMSException(1522, ResponseCode.getResponseCodeMap().get(1522));
			}

			boolean isCompOffAppliedLeaveTypePresent = false;
			// commented by ssw on 30Aug2018
			// to solve compoff issue

			// Set<EmployeeLeaveDetail> empLeaveDetailsList =
			// employeeEntity.getEmployeeLeaveDetails();
			// for (EmployeeLeaveDetail employeeLeaveDetail : empLeaveDetailsList) {
			// if (employeeLeaveDetail.getMasterLeaveType().getId() ==
			// employeeGrantLeaveDetailEntity
			// .getMasterLeaveType().getId() && employeeLeaveDetail.getYear() ==
			// HRMSHelper.getCurrentYear()) {
			// isCompOffAppliedLeaveTypePresent = true;
			// }
			// }
			if (!isCompOffAppliedLeaveTypePresent) {
				addToEmployeeLeaveDetailInGrant(employeeGrantLeaveDetailEntity.getEmployee().getId(),
						employeeGrantLeaveDetailEntity.getMasterLeaveType().getId(), HRMSHelper.getCurrentYear(),
						employeeGrantLeaveDetailEntity.getNoOfDays(),
						employeeGrantLeaveDetailEntity.getMasterLeaveType(),
						employeeGrantLeaveDetailEntity.getEmployee());
			}
			Employee employeeEntityUpdated = leaveProcessorDependencies.getEmployeeDAO()
					.findEmpCandByEmpId(employeeGrantLeaveDetailEntity.getEmployee().getId());
			// System.out.println(employeeEntity.getId());
			// Set<EmployeeLeaveDetail> empLeaveDetailsUpdatedList =
			// employeeEntityUpdated.getEmployeeLeaveDetails();

			// next modified by SSW on 05 Dec 2018 for : fetching leave details yearwise
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
					float leaveAvailable = employeeLeaveDetail.getLeaveAvailable();
					float grantLeaveApplied = employeeGrantLeaveDetailEntity.getNoOfDays();
					float updatedleaveAvailable = leaveAvailable + grantLeaveApplied;
					// next 2 lines commented by SSW on 30Aug2018
					// issue : extra compoff added for new employee
					// employeeLeaveDetail.setLeaveAvailable(updatedleaveAvailable);
					// employeeLeaveDetailsDAO.save(employeeLeaveDetail);

					employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_APPROVED);
					employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
					employeeGrantLeaveDetailEntity.setUpdatedBy(String.valueOf(empId));
					employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
					employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
					employeeGrantLeaveDetailEntity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
							.save(employeeGrantLeaveDetailEntity);

					/**
					 * Email Sender
					 */
					String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
					String ccEmailId = employeeGrantLeaveDetailEntity.getCc();
					Map<String, String> placeHolderMapping = HRMSRequestTranslator
							.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
					placeHolderMapping.put("{rootIp}", baseURL);
					placeHolderMapping.put("{websiteURL}", baseURL);
					String mailContent = HRMSHelper.replaceString(placeHolderMapping,
							IHRMSEmailTemplateConstants.Template_LeaveApproved);
					/*
					 * EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
					 * IHRMSConstants.MailSubject_GrantLeaveApproved);
					 */
					leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId, mailContent,
							IHRMSConstants.MailSubject_GrantLeaveApproved,
							employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
									.getDivision().getId(),
							employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity()
									.getOrganization().getId());
					/*
					 * add employee grant leave details in credit leave
					 */
					/*
					 * when comp off is getting approved, we need not to save to_date(expiry date)
					 * the to_date will be calculated from configuration data from org and div wise.
					 */
					EmployeeCreditLeaveDetail creditLeaveDetail = new EmployeeCreditLeaveDetail();
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
					creditLeaveDetail.setOrgId(employeeEntityUpdated.getOrgId());
					leaveProcessorDependencies.getCreditLeaveDAO().save(creditLeaveDetail);
					break;

				}
			}
		} else {
			throw new HRMSException(IHRMSConstants.LeaveActionErrorCode, IHRMSConstants.LeaveNotInPendingErrorMessage);
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1535));

		log.info("Exit from  approveGrantLeave method");
		return response;
	}

	private void addToEmployeeLeaveDetailInGrant(long employeeId, long masterLeaveTypeId, int year, float leave,
			MasterLeaveType masterLeaveType, Employee employee) {
		EmployeeLeaveDetail employeeLeaveDetailEntity = leaveProcessorDependencies.getEmployeeLeaveDetailsDAO()
				.findEmployeeLeaveByEIDYEAR(employeeId, masterLeaveTypeId, year);

		if (!HRMSHelper.isNullOrEmpty(employeeLeaveDetailEntity)) {
			float leaveAvailable = employeeLeaveDetailEntity.getLeaveAvailable();
			float leaveEarned = employeeLeaveDetailEntity.getLeaveEarned();
			employeeLeaveDetailEntity.setLeaveAvailable(leaveAvailable + leave);
			employeeLeaveDetailEntity.setLeaveEarned(leaveEarned + leave);
			employeeLeaveDetailEntity.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
			leaveProcessorDependencies.getEmployeeLeaveDetailsDAO().save(employeeLeaveDetailEntity);
		} else {
			EmployeeLeaveDetail employeeLeaveDetail = new EmployeeLeaveDetail();
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
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getLeaveDetailsProcess(Long employeeId, Pageable pageable)
			throws HRMSException {

		log.info("Inside getLeaveDetails method");
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		log.info(" == EMPLOYEE APPLIED LEAVE DETAILS  ==");
		Employee employee = leaveProcessorDependencies.getEmployeeDAO().findById(empId).get();

		EmployeeLeaveDetailsVO leaveDetailsVO = null;
		long totalRecord = 0;
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

//			ProfileVO managerProfile = null;
//			if (!HRMSHelper.isNullOrEmpty(employee)) {
//				managerProfile = PersonalDetailsTransformUtils.convertToProfileDetailsVO(manger);
//			}
//			leaveDetailsVO.setManagerProfile(managerProfile);

		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EmployeeDoesnotExistMessage);
		}

		log.info("Exit from getLeaveDetails method");
		response.setResponseBody(leaveDetailsVO);
		response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		return response;
	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getGrantLeaveDetailsProcess(Long employeeId, Pageable pageable)
			throws HRMSException {

		log.info("Inside getGrantLeaveDetails method");
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		Employee employee = leaveProcessorDependencies.getEmployeeDAO().findById(empId).get();

		Employee manger = employee.getEmployeeReportingManager().getReporingManager();
		EmployeeLeaveDetailsVO leaveDetailsVO = null;
		long totalRecord = 0;

		List<EmployeeGrantLeaveDetail> employeeGrantLeaveDetails = new ArrayList<EmployeeGrantLeaveDetail>();

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		log.info("in getEmployeeGrantLeaveDetails() : Find grant leave details of logged in employee");
		employeeGrantLeaveDetails = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
				.findAllGrantLeaveDetailsByYear(empId, IHRMSConstants.LeaveStatus_CANCELLED, year,
						IHRMSConstants.isActive, pageable);
		totalRecord = leaveProcessorDependencies.getEmployeeGrantLeaveDAO().countByGrantLeaveDetailsByYear(empId,
				IHRMSConstants.LeaveStatus_CANCELLED, year, IHRMSConstants.isActive);
		List<EmployeeCreditLeaveDetail> creditedLeave=leaveProcessorDependencies.creditLeaveDAO.findCreditedLeaveByEmpIdAndLeaveTypeId(empId,
				IHRMSConstants.LeaveStatus_CANCELLED, year, IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetails)|| !HRMSHelper.isNullOrEmpty(creditedLeave)) {
			leaveDetailsVO = LeaveTransformUtil.convertToEmployeeGrantLeaveDetailsForEmployee(employeeGrantLeaveDetails,creditedLeave);
			ProfileVO managerProfile = null;
			if (!HRMSHelper.isNullOrEmpty(employee)) {
				managerProfile = PersonalDetailsTransformUtils.convertToProfileDetailsVO(manger);
			}
//			leaveDetailsVO.setManagerProfile(managerProfile);
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(leaveDetailsVO);
		response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getGrantLeaveDetails method");
		return response;
	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetailsProcess(Long filterEmployeeId, Integer year,
			Pageable pageable) throws HRMSException {

		log.info("Inside getTeamLeaveDetails method");
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse<EmployeeLeaveDetailsVO>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		log.info(" == TEAM LEAVE DETAILS  ==");
		Employee employee = leaveProcessorDependencies.getEmployeeDAO().findById(empId).get();

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

				Employee filterEmployee = leaveProcessorDependencies.getEmployeeDAO().findById(filterEmployeeId).get();
				if (empId == filterEmployee.getEmployeeReportingManager().getReporingManager().getId()) {
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

		log.info("Exit from getTeamLeaveDetails method");
		response.setResponseBody(leaveDetailsVO);
		response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		return response;
	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetailsProcess(Pageable pageable, Long employeeId)
			throws HRMSException {

		log.info("Inside getTeamGrantLeaveDetails method");
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		Employee employee = leaveProcessorDependencies.getEmployeeDAO().findById(empId).get();

		EmployeeLeaveDetailsVO leaveDetailsVO = null;
		long totalRecord = 0;

		List<EmployeeGrantLeaveDetail> employeeGrantLeaveDetails = new ArrayList<EmployeeGrantLeaveDetail>();

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		log.info("in getEmployeeGrantLeaveDetails() : Find grant leave details of logged in employee");

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

		response.setResponseBody(leaveDetailsVO);
		response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getTeamGrantLeaveDetails method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<VOHolidayResponse>> holidayListProcess(Long year) throws HRMSException {

		log.info("Inside getTeamGrantLeaveDetails method");

		HRMSBaseResponse<List<VOHolidayResponse>> response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		long totalRecord = 0;
		Employee emp = leaveProcessorDependencies.getEmployeeDAO().findActiveEmployeeById(Long.valueOf(empId), "Y");
		long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
		long branchId = !HRMSHelper
				.isNullOrEmpty(emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation())
						? emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation().getId()
						: emp.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
		long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
		long deptId = emp.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
		Long currentYear = !HRMSHelper.isNullOrEmpty(year) ? year : Year.now().getValue();

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
				response.setTotalRecord(totalRecord);
				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				log.info("Exit from holiday list method");
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<SubOrdinateListVO> findSubordinate() throws HRMSException {
		log.info("Inside findSubordinate method");

		HRMSBaseResponse<SubOrdinateListVO> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		long totalRecord = 0;
		Employee emp = leaveProcessorDependencies.getEmployeeDAO().findActiveEmployeeById(Long.valueOf(empId), "Y");
		long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();

		long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();

		Long managerId = empId;
		List<EmployeeListVO> voEmployees = new ArrayList<EmployeeListVO>();
		if (!HRMSHelper.isLongZero(orgId)) {
			if (managerId > 0) {
				log.info("Finding Subordinate For Manager : ");
				List<EmployeeReportingManager> reportingManager = leaveProcessorDependencies.getReportingManagerDAO()
						.findByreporingManager(managerId);
				for (EmployeeReportingManager rptmgr : reportingManager) {
					if (rptmgr.getEmployee().getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {

						EmployeeListVO employeeModel = LeaveTransformUtil.convertToEmployeeModel(rptmgr.getEmployee());
						voEmployees.add(employeeModel);
					}
				}
			} else {
				log.info("Finding All , Based On Organization  ... ");
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
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			log.info("Exit from Find Subordinate List method");

		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<LeaveSumarryDetailsResponseVO> getEmployeeLeaveSummary(long empId, Integer year)
			throws HRMSException {
		log.info("Inside getEmployeeLeaveSummary method for manager");
		HRMSBaseResponse<LeaveSumarryDetailsResponseVO> response = new HRMSBaseResponse<>();

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Integer currentYear = !HRMSHelper.isNullOrEmpty(year) ? year : HRMSDateUtil.getCurrentYear();

		if (!HRMSHelper.isLongZero(empId)) {
			Employee employee = leaveProcessorDependencies.getEmployeeDAO().getEmployeeLeaveDetailByEmpIdAndYear(empId,
					currentYear, IHRMSConstants.isActive);

			if (HRMSHelper.isNullOrEmpty(employee)) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

			EmployeeReportingManager reportingManager = employee.getEmployeeReportingManager();
			Long reportingManagerId = reportingManager.getReporingManager().getId();

			if (!employeeId.equals(reportingManagerId)) {
				throw new HRMSException(1510, ResponseCode.getResponseCodeMap().get(1510));
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
								.findCountOfAppliedLeaveDetails(employee.getId(),
										employeeLeaveDetailsEntity.getMasterLeaveType().getId(),
										IHRMSConstants.LeaveStatus_PENDING, HRMSDateUtil.getCurrentYear());

						log.info("Leave Type : " + employeeLeaveDetailsEntity.getMasterLeaveType().getLeaveTypeName());
						log.info("Leave Type ID : " + employeeLeaveDetailsEntity.getMasterLeaveType().getId());
						log.info("For Employee ID : " + employee.getId());
						log.info("For Year : " + HRMSDateUtil.getCurrentYear());
						log.info("Count : " + countLeaveApplied);

						model.setLeaveApplied(countLeaveApplied);
						emplLeaveDtlList.add(model);
						totalRecords++;
					}
				}
			}

			leaveDetails.setEmployeeLeaveDetail(emplLeaveDtlList);
			response.setResponseBody(leaveDetails);
			response.setTotalRecord(totalRecords);
			response.setResponseCode(EResponse.SUCCESS.getCode());
			response.setResponseMessage(EResponse.SUCCESS.getMessage());
		} else {
			throw new HRMSException(EResponse.ERROR_INSUFFICIENT_DATA.getCode(),
					EResponse.ERROR_INSUFFICIENT_DATA.getMessage());
		}
		log.info("Exit from getEmployeeLeaveSummary method for manager");
		return response;
	}

	@Override
	public void clearExpiredCompOffs() {

		Year currentYear = Year.now();
		int year = currentYear.getValue();
		try {
			MasterLeaveType masterLeaveTypeEntity = leaveProcessorDependencies.getMasterLeaveTypeDAO()
					.findByLeaveTypeCode(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE);

			List<EmployeeLeaveDetail> empLeaveDetails = leaveProcessorDependencies.getEmployeeLeaveDetailsDAO()
					.findAllEmployeesLeaveDetails(masterLeaveTypeEntity.getId(), IHRMSConstants.isActive, year);
			if (!HRMSHelper.isNullOrEmpty(empLeaveDetails)) {
				for (EmployeeLeaveDetail empLeaveDetail : empLeaveDetails) {

					Float validLeaveCount = leaveProcessorDependencies.getCreditLeaveDAO()
							.findTotalCountOfCompOffsYearly(empLeaveDetail.getEmployee().getId(),
									masterLeaveTypeEntity.getId(), year);

					log.info("empId :" + empLeaveDetail.getEmployee().getId());
					log.info("actual count :" + validLeaveCount);
					log.info("leave detail count :" + empLeaveDetail.getLeaveAvailable());

					if (HRMSHelper.isNullOrEmpty(validLeaveCount)) {
						validLeaveCount = 0F;
					}

					if (Float.compare(validLeaveCount, empLeaveDetail.getLeaveAvailable()) != 0) {
						log.info("Inside valid counts compare method ");
						// Entry in history table
						log.info("Updating history...");
						EmployeeCompOffCreditLeaveHistory compOffHistory = new EmployeeCompOffCreditLeaveHistory();
						compOffHistory = LeaveTransformUtil.transformToCompOffHistory(compOffHistory, empLeaveDetail);
						leaveProcessorDependencies.getCreditCompOffHistoryDAO().save(compOffHistory);

						log.info("Updating leave balance");
						empLeaveDetail.setLeaveAvailable(validLeaveCount);
						empLeaveDetail.setUpdatedDate(new Date());

						leaveProcessorDependencies.getEmployeeLeaveDetailsDAO().save(empLeaveDetail);
						log.info("Exit from valid counts compare method ");

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public HRMSBaseResponse rejectGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException {
		log.info("Inside rejectGrantLeave method");
		// input validation
		leaveProcessorDependencies.getEmployeeAuthorityHelper().rejectGrantLeaveInputValidation(request);
		HRMSBaseResponse response = new HRMSBaseResponse();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
			// Retrieve the manager's ID
			Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			// Find all employee IDs under this manager
			List<EmployeeReportingManager> employeesUnderManager = leaveProcessorDependencies.getReportingManagerDAO()
					.findByreporingManager(employeeId);
			EmployeeGrantLeaveDetail leavesAppliedEnity = leaveProcessorDependencies.getEmployeeGrantLeaveDAO()
					.findById(request.getId());

			if (leavesAppliedEnity != null) {

				// Find the employee ID for the leave request
				Long requestedEmployeeId = leavesAppliedEnity.getEmployee().getId();
				// Check if the requested employee is under the management hierarchy of the

				long matchCount = employeesUnderManager.stream()
						.filter(e -> e.getEmployee().getId().equals(requestedEmployeeId)).count();
				if (matchCount > 0) {
					EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity = leaveProcessorDependencies
							.getEmployeeGrantLeaveDAO().findById(request.getId());
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

						/**
						 * Email Sender
						 */
						String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
						String ccEmailId = employeeGrantLeaveDetailEntity.getCc();
						Map<String, String> placeHolderMapping = HRMSRequestTranslator
								.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
						placeHolderMapping.put("{rootIp}", baseURL);
						placeHolderMapping.put("{websiteURL}", baseURL);
						String mailContent = HRMSHelper.replaceString(placeHolderMapping,
								IHRMSEmailTemplateConstants.Template_LeaveReject);

						// EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
						// IHRMSConstants.MailSubject_LeaveRejected);
						leaveProcessorDependencies.getEmailsender().toPersistEmail(employeeEmailId, ccEmailId,
								mailContent, IHRMSConstants.MailSubject_LeaveRejected,
								employeeGrantLeaveDetailEntity.getEmployee().getCandidate()
										.getCandidateProfessionalDetail().getDivision().getId(),
								employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity()
										.getOrganization().getId());
					} else {
						throw new HRMSException(1516, ResponseCode.getResponseCodeMap().get(1516));
					}

				} else {
					// Throw an exception if the requested employee is not under the management
					// hierarchy
					throw new HRMSException(1520, ResponseCode.getResponseCodeMap().get(1520));
				}

			} else {
				// If data not found, throw an exception with a message
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

		} else {
			// Throw an exception if the user is not a manager
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1536));

		log.info("Exit from  rejectGrantLeave method");
		return response;
	}

	@Override
	public byte[] downloadTeamLeavesReport(String fromDate, String toDate, HttpServletResponse httpServletResponse)
			throws HRMSException, IOException, ParseException {
		log.info("Inside downloadTeamLeavesReport Method");
		leaveProcessorDependencies.getEmployeeAuthorityHelper().downloadTeamLeavesReportInputValidation(fromDate,
				toDate);
		List<EmployeeLeaveApplied> filterCommonLeave;
		if (SecurityFilter.TL_CLAIMS.get().getRoles().contains(ERole.MANAGER.name())) {
			filterCommonLeave = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().findByDateRange(
					new SimpleDateFormat("dd-MM-yyyy").parse(fromDate),
					new SimpleDateFormat("dd-MM-yyyy").parse(toDate), SecurityFilter.TL_CLAIMS.get().getOrgId(),
					SecurityFilter.TL_CLAIMS.get().getEmployeeId());
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		if (HRMSHelper.isNullOrEmpty(filterCommonLeave)) {
			throw new HRMSException(1500, "No data found for the specified date range.");
		}
		log.info("generating report");
		return generateTeamLeavesReportProcess(httpServletResponse, filterCommonLeave);

	}

	protected byte[] generateTeamLeavesReportProcess(HttpServletResponse httpServletResponse,
			List<EmployeeLeaveApplied> appliedLeaves) throws IOException {
		Workbook workbook = new TeamLeavesReportXLSGenerator().generateXlS(appliedLeaves);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=LeaveReport.xlsx");
		workbook.write(outputStream);
		workbook.close();
		return outputStream.toByteArray();
	}

	@Override
	public byte[] downloadAllEmployeeLeavesReport(String fromDate, String toDate,
			HttpServletResponse httpServletResponse) throws HRMSException, IOException, ParseException {
		log.info("Inside downloadAllEmployeeLeavesReport Method");
		leaveProcessorDependencies.getEmployeeAuthorityHelper().downloadTeamLeavesReportInputValidation(fromDate,
				toDate);
		List<EmployeeLeaveApplied> filterCommonLeave;
		List<String> leaveStatus=new ArrayList<>();
		leaveStatus.add(ELeaveStatus.APPROVED.name());
		leaveStatus.add(ELeaveStatus.PENDING.name());
		if (SecurityFilter.TL_CLAIMS.get().getRoles().contains(ERole.HR.name())) {
			filterCommonLeave = leaveProcessorDependencies.getEmployeeLeaveAppliedDAO().findByDateRangeAndOrgId(
					new SimpleDateFormat("dd-MM-yyyy").parse(fromDate),
					new SimpleDateFormat("dd-MM-yyyy").parse(toDate), SecurityFilter.TL_CLAIMS.get().getOrgId(),leaveStatus.toArray());
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		if (HRMSHelper.isNullOrEmpty(filterCommonLeave)) {
			throw new HRMSException(1500, "No data found for the specified date range.");
		}
		log.info("generating report");
		return generateTeamLeavesReportProcess(httpServletResponse, filterCommonLeave);
	}
}
