package com.vinsys.hrms.employee.service.impl.attendance.processors;

import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendaceEmployeeACNDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceCsvDataDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.util.AttendanceDetailsTransformUtils;
import com.vinsys.hrms.util.EmployeeAttendanceHelper;
import com.vinsys.hrms.util.HRMSAttendanceHelper;

public class AttendanceProcessorDependencies {
	String applicationVersion;
	HRMSAttendanceHelper attendanceHelper;
	IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO;
	IHRMSEmployeeAcnDAO employeeAcnDAO;
	IHRMSEmployeeDAO employeeDAO;
	IHRMSOrganizationHolidayDAO holidayDAO;
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	IHRMSAttendanceCsvDataDAO attendanceCsvDataDAO;
	IHRMSAttendaceEmployeeACNDAO employeeACNDAO;
	EmployeeAttendanceHelper employeeAttendanceHelper;
	IHRMSEmployeeReportingManager reportingManagerDAO;
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;
	AttendanceDetailsTransformUtils attendanceDetailsTransformUtils;

	public AttendanceProcessorDependencies(String applicationVersion, HRMSAttendanceHelper attendanceHelper,
			IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO, IHRMSEmployeeAcnDAO employeeAcnDAO,
			IHRMSEmployeeDAO employeeDAO, IHRMSOrganizationHolidayDAO holidayDAO,
			IHRMSOrganizationWeekoffDAO organizationWeekoffDAO, IHRMSAttendanceCsvDataDAO attendanceCsvDataDAO,
			IHRMSAttendaceEmployeeACNDAO employeeACNDAO2, EmployeeAttendanceHelper employeeAttendanceHelper,
			IHRMSEmployeeReportingManager reportingManagerDAO, IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO,
			AttendanceDetailsTransformUtils attendanceDetailsTransformUtils) {
		super();
		this.applicationVersion = applicationVersion;
		this.attendanceHelper = attendanceHelper;
		this.attendanceProcessedDataDAO = attendanceProcessedDataDAO;
		this.employeeAcnDAO = employeeAcnDAO;
		this.employeeDAO = employeeDAO;
		this.holidayDAO = holidayDAO;
		this.organizationWeekoffDAO = organizationWeekoffDAO;
		this.attendanceCsvDataDAO = attendanceCsvDataDAO;
		employeeACNDAO = employeeACNDAO2;
		this.employeeAttendanceHelper = employeeAttendanceHelper;
		this.reportingManagerDAO = reportingManagerDAO;
		this.employeeLeaveAppliedDAO = employeeLeaveAppliedDAO;
		this.attendanceDetailsTransformUtils = attendanceDetailsTransformUtils;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public HRMSAttendanceHelper getAttendanceHelper() {
		return attendanceHelper;
	}

	public void setAttendanceHelper(HRMSAttendanceHelper attendanceHelper) {
		this.attendanceHelper = attendanceHelper;
	}

	public IHRMSAttendanceProcessedDataDAO getAttendanceProcessedDataDAO() {
		return attendanceProcessedDataDAO;
	}

	public void setAttendanceProcessedDataDAO(IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO) {
		this.attendanceProcessedDataDAO = attendanceProcessedDataDAO;
	}

	public IHRMSEmployeeAcnDAO getEmployeeAcnDAO() {
		return employeeAcnDAO;
	}

	public void setEmployeeAcnDAO(IHRMSEmployeeAcnDAO employeeAcnDAO) {
		this.employeeAcnDAO = employeeAcnDAO;
	}

	public IHRMSEmployeeDAO getEmployeeDAO() {
		return employeeDAO;
	}

	public void setEmployeeDAO(IHRMSEmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	public IHRMSOrganizationHolidayDAO getHolidayDAO() {
		return holidayDAO;
	}

	public void setHolidayDAO(IHRMSOrganizationHolidayDAO holidayDAO) {
		this.holidayDAO = holidayDAO;
	}

	public IHRMSOrganizationWeekoffDAO getOrganizationWeekoffDAO() {
		return organizationWeekoffDAO;
	}

	public void setOrganizationWeekoffDAO(IHRMSOrganizationWeekoffDAO organizationWeekoffDAO) {
		this.organizationWeekoffDAO = organizationWeekoffDAO;
	}

	public IHRMSAttendanceCsvDataDAO getAttendanceCsvDataDAO() {
		return attendanceCsvDataDAO;
	}

	public void setAttendanceCsvDataDAO(IHRMSAttendanceCsvDataDAO attendanceCsvDataDAO) {
		this.attendanceCsvDataDAO = attendanceCsvDataDAO;
	}

	public IHRMSAttendaceEmployeeACNDAO getEmployeeACNDAO() {
		return employeeACNDAO;
	}

	public void setEmployeeACNDAO(IHRMSAttendaceEmployeeACNDAO employeeACNDAO) {
		this.employeeACNDAO = employeeACNDAO;
	}

	public EmployeeAttendanceHelper getEmployeeAttendanceHelper() {
		return employeeAttendanceHelper;
	}

	public void setEmployeeAttendanceHelper(EmployeeAttendanceHelper employeeAttendanceHelper) {
		this.employeeAttendanceHelper = employeeAttendanceHelper;
	}

	public IHRMSEmployeeReportingManager getReportingManagerDAO() {
		return reportingManagerDAO;
	}

	public void setReportingManagerDAO(IHRMSEmployeeReportingManager reportingManagerDAO) {
		this.reportingManagerDAO = reportingManagerDAO;
	}

	public IHRMSEmployeeLeaveAppliedDAO getEmployeeLeaveAppliedDAO() {
		return employeeLeaveAppliedDAO;
	}

	public void setEmployeeLeaveAppliedDAO(IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO) {
		this.employeeLeaveAppliedDAO = employeeLeaveAppliedDAO;
	}

	public AttendanceDetailsTransformUtils getAttendanceDetailsTransformUtils() {
		return attendanceDetailsTransformUtils;
	}

	public void setAttendanceDetailsTransformUtils(AttendanceDetailsTransformUtils attendanceDetailsTransformUtils) {
		this.attendanceDetailsTransformUtils = attendanceDetailsTransformUtils;
	}

}
