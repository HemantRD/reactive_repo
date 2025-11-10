package com.vinsys.hrms.employee.service.impl.attendance.processors;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.constants.ELeaveTypeCode;
import com.vinsys.hrms.constants.EOrganizationId;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceCSVDataSummaryDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceCsvDataDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceSwipeDataDAO;
import com.vinsys.hrms.datamodel.VOLeaveCalculationRequest;
import com.vinsys.hrms.datamodel.attendance.AttendanceStatusDetails;
import com.vinsys.hrms.datamodel.attendance.GetLeaveTypeSessions;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.entity.attendance.AttendanceCSVDataSummary;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedDataKey;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

@Component
public class LryptAttendanceScheduledTask {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IHRMSAttendanceCsvDataDAO attendanceCsvDataDAO;
	@Autowired
	IHRMSAttendanceCSVDataSummaryDAO attendanceCSVDataSummaryDAO;
	@Autowired
	IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO;
	@Autowired
	IHRMSEmployeeAcnDAO employeeAcnDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSEmployeeSeparationDAO employeeSeparationDAO;
	@Autowired
	IHRMSAttendanceSwipeDataDAO attendanceSwipeDataDAO;
	@Autowired
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;
	@Autowired
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO holidayDAO;

	@Scheduled(cron = "0 20 1,9,15,21 * * *")
	//@Scheduled(cron = "0 */5 * * * *")
	public void processUploadedData() {
		log.info("Inside processUploadedData method");
		log.info("Processing uploades swipes.");
		Long orgId = Long.valueOf(EOrganizationId.LRYPT.toString());
		List<AttendanceCSVDataSummary> csvDataList = attendanceCSVDataSummaryDAO
				.findByOrgIdIsProcessedIsActiveAndNoACN(orgId, false);
		if (!HRMSHelper.isNullOrEmpty(csvDataList)) {
			handelProcessUploadedData(orgId, csvDataList);
		}
		log.info("Exit from processUploadedData method");

	}

	private void handelProcessUploadedData(long orgId, List<AttendanceCSVDataSummary> csvDataList) {
		for (AttendanceCSVDataSummary attendanceCSVData : csvDataList) {
			AttendanceProcessedData csvProcessedData = new AttendanceProcessedData();
			try {
				Employee empObj = employeeDAO.findActiveEmployeeByIdAndOrgId(attendanceCSVData.getEmployeeId(),
						ERecordStatus.Y.name(), attendanceCSVData.getOrgId());
				injectCsvProcessedData(orgId, attendanceCSVData, csvProcessedData, empObj);
				int isResignedEmp = employeeSeparationDAO.countByEmployeeActualRelievingDateIsActive(empObj.getId(),
						attendanceCSVData.getAttendanceDate(), "Y");
				if (isResignedEmp == 0) {
					attendanceProcessedDataDAO.save(csvProcessedData);
				}
				attendanceCsvDataDAO.updateStatus(csvProcessedData.getCompositePrimaryKey().getAttendanceDate(),
						csvProcessedData.getCompositePrimaryKey().getEmployeeACN(),
						csvProcessedData.getCompositePrimaryKey().getOrgId());
			} catch (NumberFormatException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void injectCsvProcessedData(long orgId, AttendanceCSVDataSummary attendanceCSVData,
			AttendanceProcessedData csvProcessedData, Employee empObj) {

		if (!HRMSHelper.isNullOrEmpty(empObj)) {
			csvProcessedData
					.setCompositePrimaryKey(generateCompsiteKeyForProcessedData(orgId, attendanceCSVData, empObj));
			csvProcessedData.setStartTime(attendanceCSVData.getStartTime());
			csvProcessedData.setEndTime(attendanceCSVData.getEndTime());
			csvProcessedData.setCreatedBy("System");
			csvProcessedData.setCreatedDate(new Date());
			csvProcessedData.setIsActive(ERecordStatus.Y.name());

			csvProcessedData.getCompositePrimaryKey().setEmpId(empObj.getId());
			csvProcessedData
					.setEmpName(empObj.getCandidate().getFirstName() + " " + empObj.getCandidate().getLastName());
			csvProcessedData.setBranchId(empObj.getCandidate().getCandidateProfessionalDetail().getBranch().getId());
			csvProcessedData
					.setDivisionId(empObj.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
			csvProcessedData
					.setDepartmentId(empObj.getCandidate().getCandidateProfessionalDetail().getDepartment().getId());
			csvProcessedData
					.setDesignationId(empObj.getCandidate().getCandidateProfessionalDetail().getDesignation().getId());
			csvProcessedData
					.setBranchName(empObj.getCandidate().getCandidateProfessionalDetail().getBranch().getBranchName());
			csvProcessedData.setDivisionName(
					empObj.getCandidate().getCandidateProfessionalDetail().getDivision().getDivisionName());
			csvProcessedData.setDepartmentName(
					empObj.getCandidate().getCandidateProfessionalDetail().getDepartment().getDepartmentName());
			csvProcessedData.setDesignationName(
					empObj.getCandidate().getCandidateProfessionalDetail().getDesignation().getDesignationName());
			csvProcessedData.setRemark("System Auto Processed");
			injectManHoursAndOverTime(attendanceCSVData, csvProcessedData);
		}
	}

	private void injectAttendanceStatusForPorcessedData(AttendanceProcessedData csvProcessedData) {
		AttendanceStatusDetails statusDetails = getAttendanceStatus(csvProcessedData, true);
		csvProcessedData.setStatus(statusDetails.getStatus());
		csvProcessedData.setLeaveType(statusDetails.getLeaveType());
		csvProcessedData.setLop(statusDetails.getLopCount());
	}

	private AttendanceProcessedData injectManHoursAndOverTime(AttendanceCSVDataSummary attendanceCSVData,
			AttendanceProcessedData csvProcessedData) {
		String manHours = "";
		double newOvertime = 0;
		long timeDiff = 0;
		if (!HRMSHelper.isNullOrEmpty(attendanceCSVData.getEndTime())
				&& !HRMSHelper.isNullOrEmpty(attendanceCSVData.getStartTime())) {
			timeDiff = attendanceCSVData.getEndTime().getTime() - attendanceCSVData.getStartTime().getTime();
			newOvertime = TimeUnit.MILLISECONDS.toMinutes(timeDiff) - (9 * 60);
			long diffInHours = TimeUnit.MILLISECONDS.toHours(timeDiff);
			long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(diffInHours);
			manHours = String.format("%02d.%02d", diffInHours, diffInMinutes);
		} else {
			manHours = "0";
		}
		csvProcessedData.setManHours(Double.parseDouble(manHours));
		csvProcessedData.setOverTime(newOvertime > 0 ? newOvertime : 0);
		// attendance status
		injectAttendanceStatusForPorcessedData(csvProcessedData);
		if (csvProcessedData.getStatus().equals("H") || csvProcessedData.getStatus().equals("WO")) {
			csvProcessedData.setOverTime(TimeUnit.MILLISECONDS.toMinutes(timeDiff));
		}
		return csvProcessedData;
	}

	private AttendanceProcessedDataKey generateCompsiteKeyForProcessedData(long orgId,
			AttendanceCSVDataSummary attendanceCSVData, Employee empObj) {
		AttendanceProcessedDataKey compositePrimaryKey = new AttendanceProcessedDataKey();
		compositePrimaryKey.setEmployeeACN(attendanceCSVData.getCardNo());
		compositePrimaryKey.setAttendanceDate(attendanceCSVData.getAttendanceDate());
		compositePrimaryKey.setOrgId(orgId);
		compositePrimaryKey.setEmpId(empObj.getId());
		return compositePrimaryKey;
	}

	public AttendanceStatusDetails getAttendanceStatus(AttendanceProcessedData csvProcessedData,
			boolean checkWeekOffHoliday) {
		AttendanceStatusDetails statusDetails = new AttendanceStatusDetails();
		statusDetails.setStatus("");
		if (checkWeekOffHoliday) {
			try {
				statusDetails.setStatus(isWeeklyOffOrHoliday(csvProcessedData));
				if (!statusDetails.getStatus().equals("H") && !statusDetails.getStatus().equals("WO")) {
					setAttendanceStatusDetails(csvProcessedData, statusDetails);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusDetails;

	}

	private void setAttendanceStatusDetails(AttendanceProcessedData csvProcessedData,
			AttendanceStatusDetails statusDetails) {
		int noOfSessions = 0;
		if (csvProcessedData.getManHours() >= 9.0) {
			statusDetails.setStatus("P");
			statusDetails.setLeaveType("");
			statusDetails.setLopCount(0);
		} else {
			GetLeaveTypeSessions leaveTypeAndSessions = isLeaveApplied(
					csvProcessedData.getCompositePrimaryKey().getEmpId(),
					csvProcessedData.getCompositePrimaryKey().getAttendanceDate());
			noOfSessions = leaveTypeAndSessions.getNoOfSessions();
			statusDetails.setLeaveType(leaveTypeAndSessions.getLeaveType().toString());
			statusDetails.setLopCount(leaveTypeAndSessions.getLopCount());
			if (csvProcessedData.getManHours() < 2.15) {
				statusDetails.setStatus(noOfSessions >= 4 ? "P" : (noOfSessions >= 2 ? "HD" : "A"));
			} else if (csvProcessedData.getManHours() >= 2.15 && csvProcessedData.getManHours() < 4.30) {
				statusDetails.setStatus(noOfSessions >= 3 ? "P" : (noOfSessions >= 2 ? "HD" : "A"));
			} else if (csvProcessedData.getManHours() >= 4.30 && csvProcessedData.getManHours() < 6.45) {
				statusDetails.setStatus(noOfSessions >= 2 ? "P" : "HD");
			} else if (csvProcessedData.getManHours() >= 6.45 && csvProcessedData.getManHours() < 9.0) {
				statusDetails.setStatus(noOfSessions >= 1 ? "P" : "HD");
			} else
				statusDetails.setStatus("P");
		}
	}

	private GetLeaveTypeSessions isLeaveApplied(long empId, Date leaveDate) {
		int noOfSession = 0;
		double lop = 0;
		String leaveTypes = "";
		HashSet<String> leavetypeSet = new HashSet<>();
		GetLeaveTypeSessions leaveTypeAndSessions = new GetLeaveTypeSessions();
		if (!HRMSHelper.isNullOrEmpty(leaveDate) && !HRMSHelper.isLongZero(empId)) {
			leaveDate = HRMSDateUtil.setTimeStampToZero(leaveDate);
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
							if (key.contains(HRMSDateUtil.format(leaveDate, IHRMSConstants.FRONT_END_DATE_FORMAT))) {
								if (employeeLeaveApplied.getMasterLeaveType().getId() == 2
										|| employeeLeaveApplied.getMasterLeaveType().getId() == 12) {
									noOfSession += 2;
									leavetypeSet
											.add(employeeLeaveApplied.getMasterLeaveType().getLeaveTypeDescription());
								} else {
									noOfSession += 1;
									leavetypeSet
											.add(employeeLeaveApplied.getMasterLeaveType().getLeaveTypeDescription());
									if (employeeLeaveApplied.getMasterLeaveType().getLeaveTypeCode().equals(ELeaveTypeCode.LOPL.name()))
										lop++;
								}
							}
						}
					}
				}
			}
			leaveTypes = String.join(",", leavetypeSet);
		}
		leaveTypeAndSessions.setLeaveType(leaveTypes);
		leaveTypeAndSessions.setNoOfSessions(noOfSession);
		leaveTypeAndSessions.setLopCount(lop / 4);
		return leaveTypeAndSessions;
	}

	private String isWeeklyOffOrHoliday(AttendanceProcessedData csvProcessedData) {
		long orgId = csvProcessedData.getCompositePrimaryKey().getOrgId();
		long empId = csvProcessedData.getCompositePrimaryKey().getEmpId();
		long divId = csvProcessedData.getDivisionId();
		long departId = csvProcessedData.getDepartmentId();
		long branchId = csvProcessedData.getBranchId();
		Date leaveDate = csvProcessedData.getCompositePrimaryKey().getAttendanceDate();
		if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(empId)) {
			leaveDate = HRMSDateUtil.setTimeStampToZero(leaveDate);
			VOLeaveCalculationRequest request = new VOLeaveCalculationRequest();
			request.setFromDate(leaveDate);
			request.setToDate(leaveDate);
			List<OrganizationWeekoff> weekoffList = organizationWeekoffDAO.getWeekoffByOrgBranchDivDeptId(orgId, divId,
					branchId, departId);
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
		}
		return "";
	}
}
