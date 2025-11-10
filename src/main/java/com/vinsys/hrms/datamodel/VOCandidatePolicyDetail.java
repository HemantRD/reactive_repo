package com.vinsys.hrms.datamodel;

import java.util.Date;

public class VOCandidatePolicyDetail extends VOAuditBase {

    private long id;

    private VOCandidatePersonalDetail candidatePersonalDetail;
    private String insuranceType;
    private String insuranceCompany;
    private String policyName;
    private String policyNumber;
    private String tpa;
    private String tpaName;
    private Date startDate;
    private Date dateOfExpiry;
    private String sumInsured;
    private String employeeMembershipId;

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

    public String getInsuranceType() {
	return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
	this.insuranceType = insuranceType;
    }

    public String getInsuranceCompany() {
	return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
	this.insuranceCompany = insuranceCompany;
    }

    public String getPolicyName() {
	return policyName;
    }

    public void setPolicyName(String policyName) {
	this.policyName = policyName;
    }

    public String getPolicyNumber() {
	return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
	this.policyNumber = policyNumber;
    }

    public String getTpa() {
	return tpa;
    }

    public void setTpa(String tpa) {
	this.tpa = tpa;
    }

    public String getTpaName() {
	return tpaName;
    }

    public void setTpaName(String tpaName) {
	this.tpaName = tpaName;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getDateOfExpiry() {
	return dateOfExpiry;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
	this.dateOfExpiry = dateOfExpiry;
    }

    public String getSumInsured() {
	return sumInsured;
    }

    public void setSumInsured(String sumInsured) {
	this.sumInsured = sumInsured;
    }

    public String getEmployeeMembershipId() {
	return employeeMembershipId;
    }

    public void setEmployeeMembershipId(String employeeMembershipId) {
	this.employeeMembershipId = employeeMembershipId;
    }

}
