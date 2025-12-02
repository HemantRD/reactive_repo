package com.vinsys.hrms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_candidate_activity_letter_mapping")
public class CandidateActivityLetterMapping extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_candidate_activity_letter_mapping", sequenceName = "seq_candidate_activity_letter_mapping", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_activity_letter_mapping")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_activity_id")
	private CandidateActivity candidateActivity;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_letter_id")
	private CandidateLetter candidateLetter;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CandidateActivity getCandidateActivity() {
		return candidateActivity;
	}

	public void setCandidateActivity(CandidateActivity candidateActivity) {
		this.candidateActivity = candidateActivity;
	}

	public CandidateLetter getCandidateLetter() {
		return candidateLetter;
	}

	public void setCandidateLetter(CandidateLetter candidateLetter) {
		this.candidateLetter = candidateLetter;
	}

}
