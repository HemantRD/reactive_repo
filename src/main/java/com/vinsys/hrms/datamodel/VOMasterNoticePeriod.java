package com.vinsys.hrms.datamodel;


public class VOMasterNoticePeriod extends VOAuditBase {
	
	private long id;
	private String noticePeriod;
	private VOOrganization organization;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNoticePeriod() {
		return noticePeriod;
	}
	public void setNoticePeriod(String noticePeriod) {
		this.noticePeriod = noticePeriod;
	}
	public VOOrganization getOrganization() {
		return organization;
	}
	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}
	
	

}
