package com.vinsys.hrms.datamodel;

import java.util.Date;

public class VOCandidateLetter extends VOAuditBase {

	private long id;
	private VOCandidate candidate;
	private String letterType;
	private Date sentOn;
	private String status;
	private Date actionTakenOn;
	private String letterUrl;
	private String action;
	private String fileName;
	private VOCandidateActivityLetterMapping candidateActivityLetterMapping;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOCandidate getCandidate() {
		return candidate;
	}

	public void setCandidate(VOCandidate candidate) {
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public VOCandidateActivityLetterMapping getCandidateActivityLetterMapping() {
		return candidateActivityLetterMapping;
	}

	public void setCandidateActivityLetterMapping(VOCandidateActivityLetterMapping candidateActivityLetterMapping) {
		this.candidateActivityLetterMapping = candidateActivityLetterMapping;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
