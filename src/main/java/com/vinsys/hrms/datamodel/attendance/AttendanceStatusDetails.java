package com.vinsys.hrms.datamodel.attendance;

public class AttendanceStatusDetails {
	private String status;
	private String leaveType;
	private double lopCount;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public double getLopCount() {
		return lopCount;
	}
	public void setLopCount(double lopCount) {
		this.lopCount = lopCount;
	}
	
	

}
