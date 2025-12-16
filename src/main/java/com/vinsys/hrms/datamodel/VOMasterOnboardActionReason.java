package com.vinsys.hrms.datamodel;

public class VOMasterOnboardActionReason extends VOAuditBase{
	
	private long id;
	private String onboardActionReasonName;
	private String onboardActionReasonDescription;
	private VOOrganization organization;
	private String typeOfAction;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOnboardActionReasonName() {
		return onboardActionReasonName;
	}
	public void setOnboardActionReasonName(String onboardActionReasonName) {
		this.onboardActionReasonName = onboardActionReasonName;
	}
	public String getOnboardActionReasonDescription() {
		return onboardActionReasonDescription;
	}
	public void setOnboardActionReasonDescription(String onboardActionReasonDescription) {
		this.onboardActionReasonDescription = onboardActionReasonDescription;
	}
	public VOOrganization getOrganization() {
		return organization;
	}
	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}
	public String getTypeOfAction() {
		return typeOfAction;
	}
	public void setTypeOfAction(String typeOfAction) {
		this.typeOfAction = typeOfAction;
	}
	
}
