package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOCandidatePreviousEmployment extends VOAuditBase {

	private long id;

	private VOCandidateProfessionalDetail candidateProfessionalDetail;
	private String companyName;
	private String companyAddress;
	private VOMasterCity city;
	private VOMasterState state;
	private VOMasterCountry country;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date fromDate;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date toDate;
	private String experience;
	private String designation;
	private String jobType;
	private String previousManager;
	private String previousManagerContactNumber;
	private String reasonForleaving;
	private String overseas;
	private String isRelevant;
	private String previousManagerEmail;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOCandidateProfessionalDetail getCandidateProfessionalDetail() {
		return candidateProfessionalDetail;
	}

	public void setCandidateProfessionalDetail(VOCandidateProfessionalDetail candidateProfessionalDetail) {
		this.candidateProfessionalDetail = candidateProfessionalDetail;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public VOMasterCity getCity() {
		return city;
	}

	public void setCity(VOMasterCity city) {
		this.city = city;
	}

	public VOMasterState getState() {
		return state;
	}

	public void setState(VOMasterState state) {
		this.state = state;
	}

	public VOMasterCountry getCountry() {
		return country;
	}

	public void setCountry(VOMasterCountry country) {
		this.country = country;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getPreviousManager() {
		return previousManager;
	}

	public void setPreviousManager(String previousManager) {
		this.previousManager = previousManager;
	}

	public String getPreviousManagerContactNumber() {
		return previousManagerContactNumber;
	}

	public void setPreviousManagerContactNumber(String previousManagerContactNumber) {
		this.previousManagerContactNumber = previousManagerContactNumber;
	}

	public String getReasonForleaving() {
		return reasonForleaving;
	}

	public void setReasonForleaving(String reasonForleaving) {
		this.reasonForleaving = reasonForleaving;
	}

	public String getOverseas() {
		return overseas;
	}

	public void setOverseas(String overseas) {
		this.overseas = overseas;
	}

	public String getIsRelevant() {
		return isRelevant;
	}

	public void setIsRelevant(String isRelevant) {
		this.isRelevant = isRelevant;
	}

	public String getPreviousManagerEmail() {
		return previousManagerEmail;
	}

	public void setPreviousManagerEmail(String previousManagerEmail) {
		this.previousManagerEmail = previousManagerEmail;
	}
	

}
