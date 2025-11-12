package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOCandidatePassportDetail extends VOAuditBase {
    private long id;
    private VOCandidatePersonalDetail candidatePersonalDetail;
    private String passportNumber;
    private String passportFirstName;
    private String passportMiddleName;
    private String passportLastName;
    private String placeOfIssue;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfIssue;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfExpiry;
    private String ecnr;

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

    public String getPassportNumber() {
	return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
	this.passportNumber = passportNumber;
    }

    public String getPassportFirstName() {
	return passportFirstName;
    }

    public void setPassportFirstName(String passportFirstName) {
	this.passportFirstName = passportFirstName;
    }

    public String getPassportMiddleName() {
	return passportMiddleName;
    }

    public void setPassportMiddleName(String passportMiddleName) {
	this.passportMiddleName = passportMiddleName;
    }

    public String getPassportLastName() {
	return passportLastName;
    }

    public void setPassportLastName(String passportLastName) {
	this.passportLastName = passportLastName;
    }

    public String getPlaceOfIssue() {
	return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
	this.placeOfIssue = placeOfIssue;
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

    public String getEcnr() {
	return ecnr;
    }

    public void setEcnr(String ecnr) {
	this.ecnr = ecnr;
    }

}
