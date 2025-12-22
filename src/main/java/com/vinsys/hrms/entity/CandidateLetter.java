package com.vinsys.hrms.entity;

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

@Entity
@Table(name = "tbl_candidate_letter")
public class CandidateLetter extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_candidate_letter", sequenceName = "seq_candidate_letter", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_letter")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;

	@Column(name = "letter_type")
	private String letterType;

	@Column(name = "sent_on")
	private Date sentOn;

	@Column(name = "letter_status")
	private String status;

	@Column(name = "action_taken_on")
	private Date actionTakenOn;

	@Column(name = "letter_url")
	private String letterUrl;

	@Column(name = "file_name")
	private String fileName;

	@OneToOne(mappedBy = "candidateActivity", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private CandidateActivityLetterMapping candidateActivityLetterMapping;

	@Column(name = "server_ip")
	private String serverIp;
	
	@OneToOne(mappedBy = "candidateLetter", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeSeparationLetterMapping employeeSeparationLetterMapping;
	

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

	public String getLetterType() {
		return letterType;
	}

	public void setLetterType(String letterType) {
		this.letterType = letterType;
	}

	public Date getSentOn() {
		return sentOn;
	}

	public void setSentOn(Date sentOn) {
		this.sentOn = sentOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getActionTakenOn() {
		return actionTakenOn;
	}

	public void setActionTakenOn(Date actionTakenOn) {
		this.actionTakenOn = actionTakenOn;
	}

	public String getLetterUrl() {
		return letterUrl;
	}

	public void setLetterUrl(String letterUrl) {
		this.letterUrl = letterUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public CandidateActivityLetterMapping getCandidateActivityLetterMapping() {
		return candidateActivityLetterMapping;
	}

	public void setCandidateActivityLetterMapping(CandidateActivityLetterMapping candidateActivityLetterMapping) {
		this.candidateActivityLetterMapping = candidateActivityLetterMapping;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public EmployeeSeparationLetterMapping getEmployeeSeparationLetterMapping() {
		return employeeSeparationLetterMapping;
	}

	public void setEmployeeSeparationLetterMapping(EmployeeSeparationLetterMapping employeeSeparationLetterMapping) {
		this.employeeSeparationLetterMapping = employeeSeparationLetterMapping;
	}


}
