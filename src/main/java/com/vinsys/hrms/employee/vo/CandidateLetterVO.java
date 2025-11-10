package com.vinsys.hrms.employee.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CandidateLetterVO {
	private long id;
	private Long candidateId;
	private String documentType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date sentOn;
	private String status;
	//private Date actionTakenOn;
	//private String letterUrl;
	//private String action;
	private String documentName;
	//private VOCandidateActivityLetterMapping candidateActivityLetterMapping;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Long getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
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
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	
	
	
}
