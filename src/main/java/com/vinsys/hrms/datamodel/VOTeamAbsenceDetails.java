package com.vinsys.hrms.datamodel;

public class VOTeamAbsenceDetails {
	
	private VOEmployee employee;
	private VOMasterLeaveType masterLeaveType;
	private float leaveApplied;
	private float leaveApproved;
	private float leaveUnapproved;
	
	public VOEmployee getEmployee() {
		return employee;
	}
	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}
	public VOMasterLeaveType getMasterLeaveType() {
		return masterLeaveType;
	}
	public void setMasterLeaveType(VOMasterLeaveType masterLeaveType) {
		this.masterLeaveType = masterLeaveType;
	}
	public float getLeaveApplied() {
		return leaveApplied;
	}
	public void setLeaveApplied(float leaveApplied) {
		this.leaveApplied = leaveApplied;
	}
	public float getLeaveApproved() {
		return leaveApproved;
	}
	public void setLeaveApproved(float leaveApproved) {
		this.leaveApproved = leaveApproved;
	}
	public float getLeaveUnapproved() {
		return leaveUnapproved;
	}
	public void setLeaveUnapproved(float leaveUnapproved) {
		this.leaveUnapproved = leaveUnapproved;
	}

}
