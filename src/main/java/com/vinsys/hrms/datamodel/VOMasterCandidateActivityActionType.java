package com.vinsys.hrms.datamodel;

public class VOMasterCandidateActivityActionType extends VOAuditBase {

	private long id;
	private String activityActionTypeName;
	private String activityActionTypeDescription;
	private VOOrganization organization;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getActivityActionTypeName() {
		return activityActionTypeName;
	}

	public void setActivityActionTypeName(String activityActionTypeName) {
		this.activityActionTypeName = activityActionTypeName;
	}

	public String getActivityActionTypeDescription() {
		return activityActionTypeDescription;
	}

	public void setActivityActionTypeDescription(String activityActionTypeDescription) {
		this.activityActionTypeDescription = activityActionTypeDescription;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

}
