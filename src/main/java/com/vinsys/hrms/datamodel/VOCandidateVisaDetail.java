package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOCandidateVisaDetail extends VOAuditBase {

    private long id;

    private VOCandidatePersonalDetail candidatePersonalDetail;
    private String country;
    private String visaNumber;
    private String visaType;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfIssue;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfExpiry;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public VOCandidatePersonalDetail getCandidatePersonalDetail() {
	return candidatePersonalDetail;
    }

    public void setCandidatePersonalDetail(VOCandidatePersonalDetail candidatePersonalDetail) {
	this.candidatePersonalDetail = candidatePersonalDetail;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getVisaNumber() {
	return visaNumber;
    }

    public void setVisaNumber(String visaNumber) {
	this.visaNumber = visaNumber;
    }

    public String getVisaType() {
	return visaType;
    }

    public void setVisaType(String visaType) {
	this.visaType = visaType;
    }

    public Date getDateOfIssue() {
	return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
	this.dateOfIssue = dateOfIssue;
    }

    public Date getDateOfExpiry() {
	return dateOfExpiry;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
	this.dateOfExpiry = dateOfExpiry;
    }

}
