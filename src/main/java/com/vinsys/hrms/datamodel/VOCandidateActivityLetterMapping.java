package com.vinsys.hrms.datamodel;

public class VOCandidateActivityLetterMapping extends VOAuditBase {

	private long id;
	private VOCandidateActivity candidateActivity;
	private VOCandidateLetter candidateLetter;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOCandidateActivity getCandidateActivity() {
		return candidateActivity;
	}

	public void setCandidateActivity(VOCandidateActivity candidateActivity) {
		this.candidateActivity = candidateActivity;
	}

	public VOCandidateLetter getCandidateLetter() {
		return candidateLetter;
	}

	public void setCandidateLetter(VOCandidateLetter candidateLetter) {
		this.candidateLetter = candidateLetter;
	}

}
