package com.vinsys.hrms.datamodel;

public class VOMasterEmploymentType extends VOAuditBase {

	private long id;
	private String employmentTypeName;
	private String employmentTypeDescription;
	private VOOrganization organization;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmploymentTypeName() {
		return employmentTypeName;
	}

	public void setEmploymentTypeName(String employmentTypeName) {
		this.employmentTypeName = employmentTypeName;
	}

	public String getEmploymentTypeDescription() {
		return employmentTypeDescription;
	}

	public void setEmploymentTypeDescription(String employmentTypeDescription) {
		this.employmentTypeDescription = employmentTypeDescription;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

}
