package com.vinsys.hrms.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.datamodel.VOLeaveCalculationRequest;
import com.vinsys.hrms.datamodel.attendance.AttendanceStatusDetails;
import com.vinsys.hrms.datamodel.attendance.GetLeaveTypeSessions;
import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;

@Component
public class HRMSAttendanceHelper {

	private  final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO holidayDAO;
	@Autowired
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;
	@Autowired
	IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO;

	
	/*
	public String getAttendanceStatus(AttendanceProcessedData csvProcessedData, boolean checkWeekOffHoliday) {
		String status = "";
		if (checkWeekOffHoliday) {
			try {
				status = isWeeklyOffOrHoliday(csvProcessedData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			if (!status.equals("H") && !status.equals("WO")) {

				int noOfSessions = 0;

				if (csvProcessedData.getManHours() < 2.05) {
					noOfSessions = isLeaveApplied(csvProcessedData.getEmpId(),
							csvProcessedData.getCompositePrimaryKey().getAttendanceDate());

					status = (noOfSessions >= 4 ? "P" : (noOfSessions >= 2 ? "HD" : "A"));

				//} else if (csvProcessedData.getManHours() >= 2.05 && csvProcessedData.getManHours() < 4.20) {
				} else if (csvProcessedData.getManHours() >= 2.05 && csvProcessedData.getManHours() < 4.15) {

					noOfSessions = isLeaveApplied(csvProcessedData.getEmpId(),
							csvProcessedData.getCompositePrimaryKey().getAttendanceDate());

					status = (noOfSessions >= 3 ? "P" : (noOfSessions >= 2 ? "HD" : "A"));

				//} else if (csvProcessedData.getManHours() >= 4.20 && csvProcessedData.getManHours() < 6.35) {
				} else if (csvProcessedData.getManHours() >= 4.15 && csvProcessedData.getManHours() < 6.25) {

					noOfSessions = isLeaveApplied(csvProcessedData.getEmpId(),
							csvProcessedData.getCompositePrimaryKey().getAttendanceDate());
					status = (noOfSessions >= 2 ? "P" : "HD");

				//} else if (csvProcessedData.getManHours() >= 6.35 && csvProcessedData.getManHours() < 8.50) {
				} else if (csvProcessedData.getManHours() >= 6.25 && csvProcessedData.getManHours() < 8.30) {

					noOfSessions = isLeaveApplied(csvProcessedData.getEmpId(),
							csvProcessedData.getCompositePrimaryKey().getAttendanceDate());
					status = (noOfSessions >= 1 ? "P" : "HD");

				} else
					status = "P";
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

	private int isLeaveApplied(long empId, Date leaveDate) {
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
							// logger.info("Applied leave Key :: " + key);
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
*/
	

	public AttendanceStatusDetails getAttendanceStatus(AttendanceProcessedData csvProcessedData, boolean checkWeekOffHoliday) {

		AttendanceStatusDetails statusDetails= new AttendanceStatusDetails();
		statusDetails.setStatus("");
		if (checkWeekOffHoliday) {
			try {
				statusDetails.setStatus(isWeeklyOffOrHoliday(csvProcessedData));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			
			if(csvProcessedData.getDivisionId()==1 || csvProcessedData.getDivisionId()==2)
			{
				
			if (!statusDetails.getStatus().equals("H") && !statusDetails.getStatus().equals("WO")) {

				int noOfSessions = 0;

				if(csvProcessedData.getManHours() >= 9.0) {
					
					statusDetails.setStatus("P");
					statusDetails.setLeaveType("");
					statusDetails.setLopCount(0);
				}else {
					GetLeaveTypeSessions leaveTypeAndSessions =  isLeaveApplied(csvProcessedData.getCompositePrimaryKey().getEmpId(),
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
			
			}else {
				if (!statusDetails.getStatus().equals("H") && !statusDetails.getStatus().equals("WO")) {

					int noOfSessions = 0;

					if(csvProcessedData.getManHours() >= 8.30) {
						
						statusDetails.setStatus("P");
						statusDetails.setLeaveType("");
						statusDetails.setLopCount(0);
					}else {
						GetLeaveTypeSessions leaveTypeAndSessions =  isLeaveApplied(csvProcessedData.getCompositePrimaryKey().getEmpId(),
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

						} else if (csvProcessedData.getManHours() >= 6.45 && csvProcessedData.getManHours() < 8.30) {

							statusDetails.setStatus(noOfSessions >= 1 ? "P" : "HD");

						} else
							statusDetails.setStatus("P");
					}
					
				}
				
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statusDetails;
	}

	private GetLeaveTypeSessions isLeaveApplied(long empId, Date leaveDate) {

		int noOfSession = 0;
		double lop = 0;
		String leaveTypes = "";
		HashSet<String> leave_type_set = new HashSet<>();
		
		GetLeaveTypeSessions leaveTypeAndSessions =  new GetLeaveTypeSessions();

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
							// logger.info("Applied leave Key :: " + key);
							if (key.contains(HRMSDateUtil.format(leaveDate, IHRMSConstants.FRONT_END_DATE_FORMAT))) {
								
								if (employeeLeaveApplied.getMasterLeaveType().getId() == 2
										|| employeeLeaveApplied.getMasterLeaveType().getId() == 12) {
									noOfSession += 2;
									leave_type_set.add(employeeLeaveApplied.getMasterLeaveType().getLeaveTypeDescription());
								}else {
										noOfSession += 1;
										leave_type_set.add(employeeLeaveApplied.getMasterLeaveType().getLeaveTypeDescription());
										if (employeeLeaveApplied.getMasterLeaveType().getLeaveTypeCode().equals("LOPL"))
											lop++;
										
								}
							}
						}

					/*	if (employeeLeaveApplied.getMasterLeaveType().getId() == 2
								|| employeeLeaveApplied.getMasterLeaveType().getId() == 12) {
							noOfSession *= 2;
							leaveTypeAndSessions.setLeaveType(employeeLeaveApplied.getMasterLeaveType().getLeaveTypeName());
							leaveTypeAndSessions.setNoOfSessions(noOfSession);
						}*/
					}
					
					
				}
			}
			leaveTypes=String.join(",", leave_type_set);	
		}
		
		leaveTypeAndSessions.setLeaveType(leaveTypes);
		leaveTypeAndSessions.setNoOfSessions(noOfSession);
		leaveTypeAndSessions.setLopCount(lop/4);
		
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

	public void UpdateProcessedData(Date fDate, Date tDate, long orgId, long empId) {
		
		List<AttendanceProcessedData> attendanceProcessedDataList = new ArrayList<>();
		List<String> status = new ArrayList<>();
		status.add("H");
		status.add("WO");
		status.add("P");
		if (HRMSHelper.isLongZero(empId))
			attendanceProcessedDataList = attendanceProcessedDataDAO.findByDateOrgIDStatus(fDate, tDate, orgId, status);
		else
			attendanceProcessedDataList = attendanceProcessedDataDAO.findByDateOrgIDStatusEmpId(fDate, tDate, orgId,
					status, empId);

		for (AttendanceProcessedData attendanceProcessedData : attendanceProcessedDataList) {

			AttendanceStatusDetails statusDetails= new AttendanceStatusDetails(); 
			statusDetails= getAttendanceStatus(attendanceProcessedData,false);
			try {
				if (attendanceProcessedData != null) {
					attendanceProcessedData.setStatus(statusDetails.getStatus());
					attendanceProcessedData.setLeaveType(statusDetails.getLeaveType());
					attendanceProcessedData.setLop(statusDetails.getLopCount());
					
					attendanceProcessedDataDAO.save(attendanceProcessedData);
				} else {
					attendanceProcessedDataList.add(attendanceProcessedData);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
