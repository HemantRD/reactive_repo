package com.vinsys.hrms.kra.vo;

public class DeptwiseCountResponseVO {

	private String departmentName;
	private String cycleName;
	private Integer totalCount;
	private Integer submittedCount;
	private Double submittedPercentage;
	private Integer pendingCount;
	private Double pendingPercentage;

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getSubmittedCount() {
		return submittedCount;
	}

	public void setSubmittedCount(Integer submittedCount) {
		this.submittedCount = submittedCount;
	}

	public Double getSubmittedPercentage() {
		return submittedPercentage;
	}

	public void setSubmittedPercentage(Double submittedPercentage) {
		this.submittedPercentage = submittedPercentage;
	}

	public Integer getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(Integer pendingCount) {
		this.pendingCount = pendingCount;
	}

	public Double getPendingPercentage() {
		return pendingPercentage;
	}

	public void setPendingPercentage(Double pendingPercentage) {
		this.pendingPercentage = pendingPercentage;
	}

}
