package com.vinsys.hrms.datamodel.attendance;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOAttendanceSwapData extends VOAuditBase {

	
	private long empId;
	private long cardNo;
	private String empName;
	private String department;
	private String designation;
	private String attendanceDate;
	private String status;
	private String startTime;
	private String endTime;
	private String swapTime;
	private double manHours;
	
	
	
	public long getEmpId() {
		return empId;
	}
	public void setEmpId(long empId) {
		this.empId = empId;
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
	public long getCardNo() {
		return cardNo;
	}
	public void setCardNo(long cardNo) {
		this.cardNo = cardNo;
	}
	public String getSwapTime() {
		return swapTime;
	}
	public void setSwapTime(String swapTime) {
		this.swapTime = swapTime;
	}

	
	
	
}
