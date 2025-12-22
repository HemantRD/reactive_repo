package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Immutable
@Table(name = "vw_vision_dashboard_count")
@Entity
public class ViDashboardCount {

	@Id
	@Column(name = "role_name")
	private String roleName;
	@Column(name = "department_id")
	private Long departmentId;
	@Column(name = "employee_code")
	private String employeeCode;
	@Column(name = "submitted_count")
	private Long submittedCount;
	@Column(name = "pending_count")
	private Long pendingCount;
	@Column(name = "submitted_percentage")
	private Double submittedPercentage;
	@Column(name = "pending_percentage")
	private Double pendingPercentage;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}
	
	

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getSubmittedCount() {
		return submittedCount;
	}

	public void setSubmittedCount(Long submittedCount) {
		this.submittedCount = submittedCount;
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

}
