package com.vinsys.hrms.datamodel;

public class VOEmployeeExtension extends VOAuditBase {

	private long id;
	private VOEmployee employee;
	private VOOrganization organization;
	private String extensionNumber;
	private VOMasterExtensionType masterExtensionType;
	private boolean employeeCheck;
private String other;
	public boolean isEmployeeCheck() {
		return employeeCheck;
	}

	public void setEmployeeCheck(boolean employeeCheck) {
		this.employeeCheck = employeeCheck;
	}

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

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public VOMasterExtensionType getMasterExtensionType() {
		return masterExtensionType;
	}

	public void setMasterExtensionType(VOMasterExtensionType masterExtensionType) {
		this.masterExtensionType = masterExtensionType;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}
