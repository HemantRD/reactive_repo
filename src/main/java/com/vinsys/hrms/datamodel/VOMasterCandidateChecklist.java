package com.vinsys.hrms.datamodel;

public class VOMasterCandidateChecklist extends VOAuditBase {

	private long id;

	private VOOrganization organization;

	private String checklistTemplate;

	private String checklistItem;

	
	private VOCandidateChecklist vOCandidateChecklist;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public String getChecklistTemplate() {
		return checklistTemplate;
	}

	public void setChecklistTemplate(String checklistTemplate) {
		this.checklistTemplate = checklistTemplate;
	}

	public String getChecklistItem() {
		return checklistItem;
	}

	public void setChecklistItem(String checklistItem) {
		this.checklistItem = checklistItem;
	}

	public VOCandidateChecklist getvOCandidateChecklist() {
		return vOCandidateChecklist;
	}

	public void setvOCandidateChecklist(VOCandidateChecklist vOCandidateChecklist) {
		this.vOCandidateChecklist = vOCandidateChecklist;
	}

	

}
