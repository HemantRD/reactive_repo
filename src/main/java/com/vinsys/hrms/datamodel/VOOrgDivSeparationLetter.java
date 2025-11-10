package com.vinsys.hrms.datamodel;

public class VOOrgDivSeparationLetter {
	
	private long id;
	
	private String letterName;
	
	private String description;
	
	private VOOrganization voOrganization;
	
	private VOMasterDivision voMasterDivision;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLetterName() {
		return letterName;
	}

	public void setLetterName(String letterName) {
		this.letterName = letterName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public VOOrganization getVoOrganization() {
		return voOrganization;
	}

	public void setVoOrganization(VOOrganization voOrganization) {
		this.voOrganization = voOrganization;
	}

	public VOMasterDivision getVoMasterDivision() {
		return voMasterDivision;
	}

	public void setVoMasterDivision(VOMasterDivision voMasterDivision) {
		this.voMasterDivision = voMasterDivision;
	}
	
}
