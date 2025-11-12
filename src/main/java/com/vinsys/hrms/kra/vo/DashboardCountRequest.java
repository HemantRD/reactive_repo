package com.vinsys.hrms.kra.vo;

public class DashboardCountRequest {

	private String roleName;
	private Long deptId;
	private Long submittedCount;
	private Long pendingCount;
	private Double submittedPercentage;
	private Double pendingPercentage;
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getSubmittedCount() {
		return submittedCount;
	}
	public void setSubmittedCount(Long approvedCount) {
		this.submittedCount = approvedCount;
	}
	public Long getPendingCount() {
		return pendingCount;
	}
	public void setPendingCount(Long pendingCount) {
		this.pendingCount = pendingCount;
	}
	public Double getSubmittedPercentage() {
		return submittedPercentage;
	}
	public void setSubmittedPercentage(Double submittedPercentage) {
		this.submittedPercentage = submittedPercentage;
	}
	public Double getPendingPercentage() {
		return pendingPercentage;
	}
	public void setPendingPercentage(Double pendingPercentage) {
		this.pendingPercentage = pendingPercentage;
	}
	public DashboardCountRequest(String roleName, Long deptId, Long submittedCount, Long pendingCount,
			Double submittedPercentage, Double pendingPercentage) {
		super();
		this.roleName = roleName;
		this.deptId = deptId;
		this.submittedCount = submittedCount;
		this.pendingCount = pendingCount;
		this.submittedPercentage = submittedPercentage;
		this.pendingPercentage = pendingPercentage;
	}

	
	
	
}
