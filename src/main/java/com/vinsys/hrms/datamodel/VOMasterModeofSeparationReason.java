package com.vinsys.hrms.datamodel;


public class VOMasterModeofSeparationReason extends VOAuditBase {
	
	private Long id;
	private String reasonName;
	private String resignActionType;
	private VOMasterModeofSeparation masterModeofSeparation;
	private VOOrganization organization;
	private VOMasterDivision division;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReasonName() {
		return reasonName;
	}
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}
	public VOMasterModeofSeparation getMasterModeofSeparation() {
		return masterModeofSeparation;
	}
	public void setMasterModeofSeparation(VOMasterModeofSeparation masterModeofSeparation) {
		this.masterModeofSeparation = masterModeofSeparation;
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
	public String getResignActionType() {
		return resignActionType;
	}
	public void setResignActionType(String resignActionType) {
		this.resignActionType = resignActionType;
	}
	
	
	

}
