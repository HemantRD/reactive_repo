package com.vinsys.hrms.datamodel;

import com.vinsys.hrms.entity.Organization;

public class VOMasterBandGrade {

	private long id;
	private String bandGrade;
	private Organization organization;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBandGrade() {
		return bandGrade;
	}

	public void setBandGrade(String bandGrade) {
		this.bandGrade = bandGrade;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
