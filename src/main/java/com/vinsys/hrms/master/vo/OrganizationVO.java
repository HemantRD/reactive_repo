package com.vinsys.hrms.master.vo;

import com.vinsys.hrms.datamodel.VOOrganizationAddress;

public class OrganizationVO {
	
	private Long id;
	private String organizationName;
	private String organizationShortName;
	private VOOrganizationAddress organizationAddress;
	private String orgShortCode;
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
	public VOOrganizationAddress getOrganizationAddress() {
		return organizationAddress;
	}
	public void setOrganizationAddress(VOOrganizationAddress organizationAddress) {
		this.organizationAddress = organizationAddress;
	}
	public String getOrgShortCode() {
		return orgShortCode;
	}
	public void setOrgShortCode(String orgShortCode) {
		this.orgShortCode = orgShortCode;
	}
	
	

}
