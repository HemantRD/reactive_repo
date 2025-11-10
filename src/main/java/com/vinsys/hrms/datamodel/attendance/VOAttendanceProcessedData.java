package com.vinsys.hrms.datamodel.attendance;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOAttendanceProcessedData extends VOAuditBase {

	private long id;
	private long empId;
	private long employeeACN;
	private String empName;
	private String department;
	private String designation;
	private String attendanceDate;
	private String status;
	private String startTime;
	private String endTime;
	private double manHours;
	private String approvalStatus;
	private String uploadStatus;
	
	public String getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getEmpId() {
		return empId;
	}
	public void setEmpId(long empId) {
		this.empId = empId;
	}
	public long getEmployeeACN() {
		return employeeACN;
	}
	public void setEmployeeACN(long employeeACN) {
		this.employeeACN = employeeACN;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(String attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public double getManHours() {
		return manHours;
	}
	public void setManHours(double manHours) {
		this.manHours = manHours;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
	
	
}
