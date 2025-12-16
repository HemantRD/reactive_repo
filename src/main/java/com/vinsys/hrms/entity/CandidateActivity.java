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
@Table(name = "tbl_candidate_activity")
public class CandidateActivity extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_candidate_activiy", sequenceName = "seq_candidate_activiy", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_activiy")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "master_candidate_activity_id")
	private MasterCandidateActivity masterCandidateActivity;
	@Column(name = "email_status")
	private String emailStatus;
	@Column(name = "hr_status")
	private String hrStatus;
	@Column(name = "hr_comment")
	private String hrComment;
	@Column(name = "activity_triggered_date")
	@Temporal(TemporalType.DATE)
	private Date activityTriggredDate;
	@Column(name = "activity_response_date")
	@Temporal(TemporalType.DATE)
	private Date activityResponseDate;
	@OneToOne(mappedBy = "candidateActivity", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private CandidateActivityLetterMapping candidateActivityLetterMapping;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public MasterCandidateActivity getMasterCandidateActivity() {
		return masterCandidateActivity;
	}

	public void setMasterCandidateActivity(MasterCandidateActivity masterCandidateActivity) {
		this.masterCandidateActivity = masterCandidateActivity;
	}

	public String getHrStatus() {
		return hrStatus;
	}

	public void setHrStatus(String hrStatus) {
		this.hrStatus = hrStatus;
	}

	public String getHrComment() {
		return hrComment;
	}

	public void setHrComment(String hrComment) {
		this.hrComment = hrComment;
	}

	public Date getActivityTriggredDate() {
		return activityTriggredDate;
	}

	public void setActivityTriggredDate(Date activityTriggredDate) {
		this.activityTriggredDate = activityTriggredDate;
	}

	public Date getActivityResponseDate() {
		return activityResponseDate;
	}

	public void setActivityResponseDate(Date activityResponseDate) {
		this.activityResponseDate = activityResponseDate;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	public CandidateActivityLetterMapping getCandidateActivityLetterMapping() {
		return candidateActivityLetterMapping;
	}

	public void setCandidateActivityLetterMapping(CandidateActivityLetterMapping candidateActivityLetterMapping) {
		this.candidateActivityLetterMapping = candidateActivityLetterMapping;
	}

}
