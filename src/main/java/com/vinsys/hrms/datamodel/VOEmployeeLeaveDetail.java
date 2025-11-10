package com.vinsys.hrms.datamodel;

import java.io.Serializable;

public class VOEmployeeLeaveDetail extends VOAuditBase implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private VOEmployee employee;
	private VOMasterLeaveType masterLeaveType;
	private float closingBalance;
	private float pyLeaveEncashment;
	private float leaveCarriedOver;
	private float leaveEarned;
	private float fyLeaveEncashment;
	private float totalEligibility;
	private float numberOfDaysAvailed;
	private float leaveAvailable;
	private float leaveApplied;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public float getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(float closingBalance) {
		this.closingBalance = closingBalance;
	}

	public float getPyLeaveEncashment() {
		return pyLeaveEncashment;
	}

	public void setPyLeaveEncashment(float pyLeaveEncashment) {
		this.pyLeaveEncashment = pyLeaveEncashment;
	}

	public float getLeaveCarriedOver() {
		return leaveCarriedOver;
	}

	public void setLeaveCarriedOver(float leaveCarriedOver) {
		this.leaveCarriedOver = leaveCarriedOver;
	}

	public float getLeaveEarned() {
		return leaveEarned;
	}

	public void setLeaveEarned(float leaveEarned) {
		this.leaveEarned = leaveEarned;
	}

	public float getFyLeaveEncashment() {
		return fyLeaveEncashment;
	}

	public void setFyLeaveEncashment(float fyLeaveEncashment) {
		this.fyLeaveEncashment = fyLeaveEncashment;
	}

	public float getTotalEligibility() {
		return totalEligibility;
	}

	public void setTotalEligibility(float totalEligibility) {
		this.totalEligibility = totalEligibility;
	}

	public float getNumberOfDaysAvailed() {
		return numberOfDaysAvailed;
	}

	public void setNumberOfDaysAvailed(float numberOfDaysAvailed) {
		this.numberOfDaysAvailed = numberOfDaysAvailed;
	}

	public float getLeaveAvailable() {
		return leaveAvailable;
	}

	public void setLeaveAvailable(float leaveAvailable) {
		this.leaveAvailable = leaveAvailable;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public float getLeaveApplied() {
		return leaveApplied;
	}

	public void setLeaveApplied(float leaveApplied) {
		this.leaveApplied = leaveApplied;
	}

}
