package com.vinsys.hrms.datamodel;

import java.util.Set;

public class VOLoginEntityType {

	private long id;
	private VOOrganization organization;
	private String loginEntityTypeName;
	private VOMasterRole role;
	private String description;
	private String details;

	private Set<VOLoginEntity> loginEntities;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public String getLoginEntityTypeName() {
		return loginEntityTypeName;
	}

	public void setLoginEntityTypeName(String loginEntityTypeName) {
		this.loginEntityTypeName = loginEntityTypeName;
	}

	public VOMasterRole getRole() {
		return role;
	}

	public void setRole(VOMasterRole role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Set<VOLoginEntity> getLoginEntities() {
		return loginEntities;
	}

	public void setLoginEntities(Set<VOLoginEntity> loginEntities) {
		this.loginEntities = loginEntities;
	}

}
