package com.vinsys.hrms.datamodel;

public class VOMasterCandidateChecklistAction extends VOAuditBase {
	
	private long id;
	private String candidateChecklistActionName;
	private String candidateChecklistActionDescription;
	private VOOrganization organization;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCandidateChecklistActionName() {
		return candidateChecklistActionName;
	}
	public void setCandidateChecklistActionName(String candidateChecklistActionName) {
		this.candidateChecklistActionName = candidateChecklistActionName;
	}
	public String getCandidateChecklistActionDescription() {
		return candidateChecklistActionDescription;
	}
	public void setCandidateChecklistActionDescription(String candidateChecklistActionDescription) {
		this.candidateChecklistActionDescription = candidateChecklistActionDescription;
	}
	public VOOrganization getOrganization() {
		return organization;
	}
	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

}
