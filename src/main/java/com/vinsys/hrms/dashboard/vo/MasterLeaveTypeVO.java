package com.vinsys.hrms.dashboard.vo;

public class MasterLeaveTypeVO {

	private long id;
	private String leaveTypeName;
	private String leaveTypeDescription;
	private int numberOfSession;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLeaveTypeName() {
		return leaveTypeName;
	}

	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}

	public String getLeaveTypeDescription() {
		return leaveTypeDescription;
	}

	public void setLeaveTypeDescription(String leaveTypeDescription) {
		this.leaveTypeDescription = leaveTypeDescription;
	}

	public int getNumberOfSession() {
		return numberOfSession;
	}

	public void setNumberOfSession(int numberOfSession) {
		this.numberOfSession = numberOfSession;
	}

}
