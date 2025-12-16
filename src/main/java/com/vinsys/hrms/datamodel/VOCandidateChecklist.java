package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOCandidateChecklist extends VOAuditBase {

    private long id;

    private VOCandidateProfessionalDetail candidateProfessionalDetail;
    private String checklistTemplate;
    private String checklistItem;
    private String mandatory;
    private String submitted;
    private String attachment;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date updatedDateTime;
    //private String hrValidationStatus;
    private Date hrValidationActedOn;
    private String hrValidationComment;
    private boolean hrValidationStatus;
    private String url;
    private VOMasterCandidateChecklistAction masterCandidateChecklistAction;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public VOCandidateProfessionalDetail getCandidateProfessionalDetail() {
	return candidateProfessionalDetail;
    }

    public void setCandidateProfessionalDetail(VOCandidateProfessionalDetail candidateProfessionalDetail) {
	this.candidateProfessionalDetail = candidateProfessionalDetail;
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

    public String getMandatory() {
	return mandatory;
    }

    public void setMandatory(String mandatory) {
	this.mandatory = mandatory;
    }

    public String getSubmitted() {
	return submitted;
    }

    public void setSubmitted(String submitted) {
	this.submitted = submitted;
    }

    public String getAttachment() {
	return attachment;
    }

    public void setAttachment(String attachment) {
	this.attachment = attachment;
    }

    public Date getUpdatedDateTime() {
	return updatedDateTime;
    }

    public void setUpdatedDateTime(Date updatedDateTime) {
	this.updatedDateTime = updatedDateTime;
    }

    /*
    public String getHrValidationStatus() {
	return hrValidationStatus;
    }

    public void setHrValidationStatus(String hrValidationStatus) {
	this.hrValidationStatus = hrValidationStatus;
    }
    */

    public Date getHrValidationActedOn() {
	return hrValidationActedOn;
    }

    public void setHrValidationActedOn(Date hrValidationActedOn) {
	this.hrValidationActedOn = hrValidationActedOn;
    }

    public String getHrValidationComment() {
	return hrValidationComment;
    }

    public void setHrValidationComment(String hrValidationComment) {
	this.hrValidationComment = hrValidationComment;
    }

	public boolean getHrValidationStatus() {
		return hrValidationStatus;
	}

	public void setHrValidationStatus(boolean hrValidationStatus) {
		this.hrValidationStatus = hrValidationStatus;
	}

	public VOMasterCandidateChecklistAction getMasterCandidateChecklistAction() {
		return masterCandidateChecklistAction;
	}

	public void setMasterCandidateChecklistAction(VOMasterCandidateChecklistAction masterCandidateChecklistAction) {
		this.masterCandidateChecklistAction = masterCandidateChecklistAction;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
