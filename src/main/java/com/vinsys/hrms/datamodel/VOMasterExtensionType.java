package com.vinsys.hrms.datamodel;

public class VOMasterExtensionType extends VOAuditBase {
	private Long id;
	private VOOrganization organization;
	private String extensionType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public String getExtensionType() {
		return extensionType;
	}

	public void setExtensionType(String extensionType) {
		this.extensionType = extensionType;
	}

}
