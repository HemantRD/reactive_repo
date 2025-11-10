package com.vinsys.hrms.employee.vo;

/**
 * @author Onkar A
 *
 * 
 */
public class ApplyLeaveRequestVO {

	EmployeeLeaveAppliedVO leaveApplied;
	private boolean autoApproved = false;

	public EmployeeLeaveAppliedVO getLeaveApplied() {
		return leaveApplied;
	}

	public void setLeaveApplied(EmployeeLeaveAppliedVO leaveApplied) {
		this.leaveApplied = leaveApplied;
	}

	public boolean isAutoApproved() {
		return autoApproved;
	}

	public void setAutoApproved(boolean autoApproved) {
		this.autoApproved = autoApproved;
	}
}
