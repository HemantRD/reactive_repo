package com.vinsys.hrms.datamodel;

public class VOLeaveGrantRequest extends VOAuditBase {

	private String actionPerformed;
	private VOEmployee reportingManager;
	private VOEmployeeGrantLeaveDetail leaveApplied;

	public String getActionPerformed() {
		return actionPerformed;
	}

	public void setActionPerformed(String actionPerformed) {
		this.actionPerformed = actionPerformed;
	}

	public VOEmployee getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(VOEmployee reportingManager) {
		this.reportingManager = reportingManager;
	}

	public VOEmployeeGrantLeaveDetail getLeaveApplied() {
		return leaveApplied;
	}

	public void setLeaveApplied(VOEmployeeGrantLeaveDetail leaveApplied) {
		this.leaveApplied = leaveApplied;
	}

}
