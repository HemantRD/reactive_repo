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
@Table(name = "tbl_candidate_activity_action_detail")
public class CandidateActivityActionDetail extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_candidate_activity_action_detail", sequenceName = "seq_candidate_activity_action_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_activity_action_detail")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;
	@Column(name = "type_of_action")
	private String typeOfAction;
	@Column(name = "hr_comment")
	private String hrComment;
	@Column(name = "hr_acted_on")
	@Temporal(TemporalType.DATE)
	private Date hrActedOn;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "master_candidate_onboard_action_reason_id")
	private MasterCandidateOnboardActionReason masterCandidateOnboardActionReason;

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

	public String getHrComment() {
		return hrComment;
	}

	public void setHrComment(String hrComment) {
		this.hrComment = hrComment;
	}

	public Date getHrActedOn() {
		return hrActedOn;
	}

	public void setHrActedOn(Date hrActedOn) {
		this.hrActedOn = hrActedOn;
	}

	public String getTypeOfAction() {
		return typeOfAction;
	}

	public void setTypeOfAction(String typeOfAction) {
		this.typeOfAction = typeOfAction;
	}

	public MasterCandidateOnboardActionReason getMasterCandidateOnboardActionReason() {
		return masterCandidateOnboardActionReason;
	}

	public void setMasterCandidateOnboardActionReason(
			MasterCandidateOnboardActionReason masterCandidateOnboardActionReason) {
		this.masterCandidateOnboardActionReason = masterCandidateOnboardActionReason;
	}

}
