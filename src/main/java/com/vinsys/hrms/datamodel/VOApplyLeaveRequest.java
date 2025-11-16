package com.vinsys.hrms.datamodel;

public class VOApplyLeaveRequest extends VOAuditBase {

    private String actionPerformed;
    private VOEmployee reportingManager;
    private boolean autoApproved;
    private VOEmployeeLeaveApplied leaveApplied;
    
    
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
	public VOEmployeeLeaveApplied getLeaveApplied() {
        return leaveApplied;
    }
    public void setLeaveApplied(VOEmployeeLeaveApplied leaveApplied) {
        this.leaveApplied = leaveApplied;
    }
	public boolean isAutoApproved() {
		return autoApproved;
	}
	public void setAutoApproved(boolean autoApproved) {
		this.autoApproved = autoApproved;
	}
    
    
    
}
