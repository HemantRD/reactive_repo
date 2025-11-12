package com.vinsys.hrms.datamodel;

import java.io.Serializable;

public class VOMasterCandidateActivity extends VOAuditBase implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private String description;
	private VOOrganization organization;
	private VOMasterDivision division;
	
	public long getId() {
	    return id;
	}
	public void setId(long id) {
	    this.id = id;
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getDescription() {
	    return description;
	}
	public void setDescription(String description) {
	    this.description = description;
	}
	public VOOrganization getOrganization() {
	    return organization;
	}
	public void setOrganization(VOOrganization organization) {
	    this.organization = organization;
	}
	public static long getSerialversionuid() {
	    return serialVersionUID;
	}
	public VOMasterDivision getDivision() {
		return division;
	}
	public void setDivision(VOMasterDivision division) {
		this.division = division;
	}
	
}
