package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_candidate_checklist")
public class CandidateChecklist extends AuditBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_candidate_checklist", sequenceName = "seq_candidate_checklist", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_checklist")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_professional_detail_id", nullable = false)
	private CandidateProfessionalDetail candidateProfessionalDetail;
	@Column(name = "checklist_template")
	private String checklistTemplate;
	@Column(name = "checklist_item")
	private String checklistItem;
	@Column(name = "mandatory")
	private String mandatory;
	@Column(name = "submitted")
	private String submitted;
	@Column(name = "attachment")
	private String attachment;
	@Temporal(TemporalType.DATE)
	@Column(name = "updated_datetime")
	private Date updatedDateTime;
	@Column(name = "hr_validation_status")
	// private String hrValidationStatus;
	private boolean hrValidationStatus;
	@Column(name = "hr_validation_acted_on")
	private Date hrValidationActedOn;
	@Column(name = "hr_validation_comment")
	private String hrValidationComment;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			// CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "master_candidate_checklist_action_id")
	private MasterCandidateChecklistAction masterCandidateChecklistAction;

	/*
	 * public String getHrValidationStatus() { return hrValidationStatus; }
	 * 
	 * public void setHrValidationStatus(String hrValidationStatus) {
	 * this.hrValidationStatus = hrValidationStatus; }
	 */
	
	/*
	 * next 2 columns added by SSW on 03Apr2019 For : 
	 * some documents like "SALARY ANNEXURE" should not be visible in 
	 * HR -> manage candidate -> Verify Candidate Document
	 * 
	 */
	@Column(name = "is_doc_only_for_employee")
	private int isDocumentOnlyForEmployee;
	
	@Column(name = "is_doc_only_for_candidate")
	private int isDocumentOnlyForCandidate;
	
	

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CandidateProfessionalDetail getCandidateProfessionalDetail() {
		return candidateProfessionalDetail;
	}

	public void setCandidateProfessionalDetail(CandidateProfessionalDetail candidateProfessionalDetail) {
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

	public boolean getHrValidationStatus() {
		return hrValidationStatus;
	}

	public void setHrValidationStatus(boolean hrValidationStatus) {
		this.hrValidationStatus = hrValidationStatus;
	}

	public MasterCandidateChecklistAction getMasterCandidateChecklistAction() {
		return masterCandidateChecklistAction;
	}

	public void setMasterCandidateChecklistAction(MasterCandidateChecklistAction masterCandidateChecklistAction) {
		this.masterCandidateChecklistAction = masterCandidateChecklistAction;
	}

	public int getIsDocumentOnlyForEmployee() {
		return isDocumentOnlyForEmployee;
	}

	public void setIsDocumentOnlyForEmployee(int isDocumentOnlyForEmployee) {
		this.isDocumentOnlyForEmployee = isDocumentOnlyForEmployee;
	}

	public int getIsDocumentOnlyForCandidate() {
		return isDocumentOnlyForCandidate;
	}

	public void setIsDocumentOnlyForCandidate(int isDocumentOnlyForCandidate) {
		this.isDocumentOnlyForCandidate = isDocumentOnlyForCandidate;
	}

}
