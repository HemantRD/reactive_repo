package com.vinsys.hrms.datamodel;

import java.util.Set;

public class VOOrganization {

	private Long id;
	private String organizationName;
	private String organizationShortName;
	private VOOrganizationAddress organizationAddress;
	private String orgShortCode;
	private Set<VOSubscription> subscription;

	private Set<VOLoginEntity> loginEntities;

	private VOLoginEntityType loginEntityType;

	public VOLoginEntityType getLoginEntityType() {
		return loginEntityType;
	}

	public void setLoginEntityType(VOLoginEntityType loginEntityType) {
		this.loginEntityType = loginEntityType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationShortName() {
		return organizationShortName;
	}

	public void setOrganizationShortName(String organizationShortName) {
		this.organizationShortName = organizationShortName;
	}

	public String getOrgShortCode() {
		return orgShortCode;
	}

	public void setOrgShortCode(String orgShortCode) {
		this.orgShortCode = orgShortCode;
	}

	public Set<VOSubscription> getSubscription() {
		return subscription;
	}

	public void setSubscription(Set<VOSubscription> subscription) {
		this.subscription = subscription;
	}

	public Set<VOLoginEntity> getLoginEntities() {
		return loginEntities;
	}

	public void setLoginEntities(Set<VOLoginEntity> loginEntities) {
		this.loginEntities = loginEntities;
	}

	public VOOrganizationAddress getOrganizationAddress() {
		return organizationAddress;
	}

	public void setOrganizationAddress(VOOrganizationAddress organizationAddress) {
		this.organizationAddress = organizationAddress;
	}
}
