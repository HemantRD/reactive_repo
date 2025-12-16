package com.vinsys.hrms.datamodel;

import java.util.Set;

public class VOCreateEmployeeCurrentOrgRequest extends VOAuditBase {

	private VOMasterDesignation designation;
	private Set<VOLoginEntityType> employeeRole;
	private VOMasterBranch branch;
	private VOMasterDivision division;
	private VOMasterEmploymentType employmentType;
	private VOMasterDepartment department;
	private VOEmployeeCurrentDetail currentOrganizationDetails;
	private VOEmployee employeeReportingManager;

	public VOEmployee getEmployeeReportingManager() {
		return employeeReportingManager;
	}

	public void setEmployeeReportingManager(VOEmployee employeeReportingManager) {
		this.employeeReportingManager = employeeReportingManager;
	}

	public VOMasterDesignation getDesignation() {
		return designation;
	}

	public void setDesignation(VOMasterDesignation designation) {
		this.designation = designation;
	}

	public Set<VOLoginEntityType> getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(Set<VOLoginEntityType> employeeRole) {
		this.employeeRole = employeeRole;
	}

	public VOMasterBranch getBranch() {
		return branch;
	}

	public void setBranch(VOMasterBranch branch) {
		this.branch = branch;
	}

	public VOMasterDivision getDivision() {
		return division;
	}

	public void setDivision(VOMasterDivision division) {
		this.division = division;
	}

	public VOMasterEmploymentType getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(VOMasterEmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	public VOMasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(VOMasterDepartment department) {
		this.department = department;
	}

	public VOEmployeeCurrentDetail getCurrentOrganizationDetails() {
		return currentOrganizationDetails;
	}

	public void setCurrentOrganizationDetails(VOEmployeeCurrentDetail currentOrganizationDetails) {
		this.currentOrganizationDetails = currentOrganizationDetails;
	}

}
