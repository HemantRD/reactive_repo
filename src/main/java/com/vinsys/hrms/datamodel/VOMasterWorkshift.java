package com.vinsys.hrms.datamodel;

public class VOMasterWorkshift extends VOAuditBase {

	private Long id;
	private String workshiftName;
	private VOOrganization organization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWorkshiftName() {
		return workshiftName;
	}

	public void setWorkshiftName(String workshiftName) {
		this.workshiftName = workshiftName;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

}