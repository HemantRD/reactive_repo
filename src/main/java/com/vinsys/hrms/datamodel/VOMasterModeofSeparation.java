package com.vinsys.hrms.datamodel;


public class VOMasterModeofSeparation extends VOAuditBase {
	
	private Long id;
	private String modeOfSeparationName;
	private String modeOfSeparationCode;
	private VOOrganization organization;
	private VOMasterDivision division;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getModeOfSeparationName() {
		return modeOfSeparationName;
	}
	public void setModeOfSeparationName(String modeOfSeparationName) {
		this.modeOfSeparationName = modeOfSeparationName;
	}
	public String getModeOfSeparationCode() {
		return modeOfSeparationCode;
	}
	public void setModeOfSeparationCode(String modeOfSeparationCode) {
		this.modeOfSeparationCode = modeOfSeparationCode;
	}
	public VOOrganization getOrganization() {
		return organization;
	}
	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}
	public VOMasterDivision getDivision() {
		return division;
	}
	public void setDivision(VOMasterDivision division) {
		this.division = division;
	}
	

	
}
