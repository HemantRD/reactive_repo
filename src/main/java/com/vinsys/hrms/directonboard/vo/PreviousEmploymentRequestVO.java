package com.vinsys.hrms.directonboard.vo;

import com.vinsys.hrms.master.vo.DesignationVO;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.master.vo.MasterCityVO;
import com.vinsys.hrms.master.vo.MasterCountryVO;
import com.vinsys.hrms.master.vo.MasterStateVO;

public class PreviousEmploymentRequestVO {

	private Long id;
	private EmploymentTypeVO employeeType;
	private String companyName;
	private String companyAddress;
	private MasterCityVO city;
	private MasterStateVO state;
	private MasterCountryVO country;
	private String workFromDate;
	private String workToDate;
	private String totalExperience;
	private DesignationVO designation;
	private String contactPersonName;
	private String contactPersonMobileNumber;
	private String reasonForleaving;
	private String isOverseas;
	private String contactPersonEmail;
	private String contactPersonDesignation;
	private Integer pincode;
	private Long candidateId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getWorkFromDate() {
		return workFromDate;
	}
	public void setWorkFromDate(String workFromDate) {
		this.workFromDate = workFromDate;
	}
	public String getWorkToDate() {
		return workToDate;
	}
	public void setWorkToDate(String workToDate) {
		this.workToDate = workToDate;
	}
	public String getTotalExperience() {
		return totalExperience;
	}
	public void setTotalExperience(String totalExperience) {
		this.totalExperience = totalExperience;
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
	public String getIsOverseas() {
		return isOverseas;
	}
	public void setIsOverseas(String isOverseas) {
		this.isOverseas = isOverseas;
	}
	public EmploymentTypeVO getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(EmploymentTypeVO employeeType) {
		this.employeeType = employeeType;
	}
	public DesignationVO getDesignation() {
		return designation;
	}
	public void setDesignation(DesignationVO designation) {
		this.designation = designation;
	}
	
	
}
