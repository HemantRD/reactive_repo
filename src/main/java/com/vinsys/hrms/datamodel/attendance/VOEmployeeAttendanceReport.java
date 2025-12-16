package com.vinsys.hrms.datamodel.attendance;

public class VOEmployeeAttendanceReport {
	
	private long employeeId;
	private String fromDate;
	private String toDate;
	private long orgId;
	private long divisionId;
	private long branchId;
	private long departmentId;
	private String empIsActive;
	private long accessCardNo;
	
	public long getAccessCardNo() {
		return accessCardNo;
	}
	public void setAccessCardNo(long accessCardNo) {
		this.accessCardNo = accessCardNo;
	}
	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	public long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(long divisionId) {
		this.divisionId = divisionId;
	}
	public long getBranchId() {
		return branchId;
	}
	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}
	public long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	public String getEmpIsActive() {
		return empIsActive;
	}
	public void setEmpIsActive(String empIsActive) {
		this.empIsActive = empIsActive;
	}
	
	
}
