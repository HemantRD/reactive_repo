package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vw_deptwise_count")
public class DeptwiseCount {

	@Id
	@Column(name = "department_name")
	private String departmentName;

	@Column(name = "cycle_name")
	private String cycleName;

	@Column(name = "total_count")
	private Integer totalCount;

	@Column(name = "submitted_count")
	private Integer submittedCount;

	@Column(name = "submitted_percentage")
	private Double submittedPercentage;

	@Column(name = "pending_count")
	private Integer pendingCount;

	@Column(name = "pending_percentage")
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
