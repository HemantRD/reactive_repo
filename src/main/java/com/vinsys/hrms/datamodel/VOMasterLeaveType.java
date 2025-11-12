package com.vinsys.hrms.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VOMasterLeaveType extends VOAuditBase {

	private long id;
	private String leaveTypeName;
	private String leaveTypeDescription;
	private int numberOfSession;
	private String[] sessions;
	@JsonIgnore
	private VOOrganization organization;
	@JsonIgnore
	private VOMasterDivision division;
	@JsonIgnore
	private VOMasterBranch branch;
	private String grantLeaveStatus;
	private String isLop;
	private String leaveTypeCode;
	private int leaveExpiry;

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

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public int getNumberOfSession() {
		return numberOfSession;
	}

	public void setNumberOfSession(int numberOfSession) {
		this.numberOfSession = numberOfSession;
	}

	public VOMasterDivision getDivision() {
		return division;
	}

	public void setDivision(VOMasterDivision division) {
		this.division = division;
	}

	public VOMasterBranch getBranch() {
		return branch;
	}

	public void setBranch(VOMasterBranch branch) {
		this.branch = branch;
	}

	public String[] getSessions() {
		return sessions;
	}

	public void setSessions(String[] sessions) {
		this.sessions = sessions;
	}

	public String getGrantLeaveStatus() {
		return grantLeaveStatus;
	}

	public void setGrantLeaveStatus(String grantLeaveStatus) {
		this.grantLeaveStatus = grantLeaveStatus;
	}
	public String getIsLop() {
		return isLop;
	}
	public void setIsLop(String isLop) {
		this.isLop = isLop;
	}
	public String getLeaveTypeCode() {
		return leaveTypeCode;
	}
	public void setLeaveTypeCode(String leaveTypeCode) {
		this.leaveTypeCode = leaveTypeCode;
	}

	public int getLeaveExpiry() {
		return leaveExpiry;
	}

	public void setLeaveExpiry(int leaveExpiry) {
		this.leaveExpiry = leaveExpiry;
	}

}
