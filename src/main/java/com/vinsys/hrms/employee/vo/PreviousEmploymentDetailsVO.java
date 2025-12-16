package com.vinsys.hrms.employee.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vinsys.hrms.master.vo.DesignationVO;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.master.vo.MasterCityVO;
import com.vinsys.hrms.master.vo.MasterCountryVO;
import com.vinsys.hrms.master.vo.MasterStateVO;

public class PreviousEmploymentDetailsVO {

	private long id;
	private EmploymentTypeVO employeeType;
	private String companyName;
	private String companyAddress;
	private MasterCityVO city;
	private MasterStateVO state;
	private MasterCountryVO country;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date workFromDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date workToDate;
	private String totalExperience;
	private DesignationVO designation;
	private String jobType;
	private String contactPersonName;
	private String contactPersonMobileNumber;
	private String reasonForleaving;
	private String isOverseas;
	private String isRelevant;
	private String contactPersonEmail;
	private String contactPersonDesignation;
	private Integer pincode;
	private Long candidateId;
	private Long orgId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public EmploymentTypeVO getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(EmploymentTypeVO employeeType) {
		this.employeeType = employeeType;
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

	public MasterCityVO getCity() {
		return city;
	}

	public void setCity(MasterCityVO city) {
		this.city = city;
	}

	public MasterStateVO getState() {
		return state;
	}

	public void setState(MasterStateVO state) {
		this.state = state;
	}

	public MasterCountryVO getCountry() {
		return country;
	}

	public void setCountry(MasterCountryVO country) {
		this.country = country;
	}

	public Date getWorkFromDate() {
		return workFromDate;
	}

	public void setWorkFromDate(Date workFromDate) {
		this.workFromDate = workFromDate;
	}

	public Date getWorkToDate() {
		return workToDate;
	}

	public void setWorkToDate(Date workToDate) {
		this.workToDate = workToDate;
	}

	public String getTotalExperience() {
		return totalExperience;
	}

	public void setTotalExperience(String totalExperience) {
		this.totalExperience = totalExperience;
	}

	public DesignationVO getDesignation() {
		return designation;
	}

	public void setDesignation(DesignationVO designation) {
		this.designation = designation;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonMobileNumber() {
		return contactPersonMobileNumber;
	}

	public void setContactPersonMobileNumber(String contactPersonMobileNumber) {
		this.contactPersonMobileNumber = contactPersonMobileNumber;
	}

	public String getReasonForleaving() {
		return reasonForleaving;
	}

	public void setReasonForleaving(String reasonForleaving) {
		this.reasonForleaving = reasonForleaving;
	}

	public String getIsOverseas() {
		return isOverseas;
	}

	public void setIsOverseas(String isOverseas) {
		this.isOverseas = isOverseas;
	}

	public String getIsRelevant() {
		return isRelevant;
	}

	public void setIsRelevant(String isRelevant) {
		this.isRelevant = isRelevant;
	}

	public String getContactPersonEmail() {
		return contactPersonEmail;
	}

	public void setContactPersonEmail(String contactPersonEmail) {
		this.contactPersonEmail = contactPersonEmail;
	}

	public String getContactPersonDesignation() {
		return contactPersonDesignation;
	}

	public void setContactPersonDesignation(String contactPersonDesignation) {
		this.contactPersonDesignation = contactPersonDesignation;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
