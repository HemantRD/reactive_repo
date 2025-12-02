package com.vinsys.hrms.directonboarding.vo;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

public class DirectOnboardingRequest {

	String validationMessage;

	@NotNull(message = "Employee Code Cannot be Empty")
	private Long empCode;

	@NotNull(message = "Employee FirstName Cannot be Empty")
	@Pattern(regexp = "^[a-zA-Z]*", message = "Employee FirstName Invalid")
	private String firstName;

	@NotNull(message = "Employee MiddleName Cannot be Empty")
	@Pattern(regexp = "^[a-zA-Z]*", message = "Employee MiddleName Invalid")
	private String middleName;

	@NotNull(message = "Employee LastName Cannot be Empty")
	@Pattern(regexp = "^[a-zA-Z]*", message = "Employee LastName Invalid")
	private String lastName;

	@NotNull(message = "Employee Designation Cannot be Empty")
	private String designation;

	@NotNull(message = "Employee Department Cannot be Empty")
	private String department;

	@NotNull(message = "Employee Reporting Manager Cannot be Empty")
	private String reportingManager;

	@NotNull(message = "Employee Email Cannot be Empty")
	@Email(message = "Invalid Email Id")
	private String emailId;

	@NotNull(message = "RmCell No. Cannot be Empty")
	private String rmCellNumber;

	@NotNull(message = "State Cannot be Empty")
	private String state;

	@NotNull(message = "Location Cannot be Empty")
	private String location;

	@NotNull(message = "Date of Joining Cannot be Empty")
	@Past(message = "Date of Joining cannot be future")
	private Date doj;

	@NotNull(message = "Pancard Cannot be Empty")
	private String panCard;

	@NotNull(message = "UANNumber Cannot be Empty")
	private String uanNumber;

	@NotNull(message = "ESICNumber Cannot be Empty")
	private String esicNumber;

	@NotNull(message = "BankName Cannot be Empty")
	private String bankName;

	@NotBlank(message = "AccountNumber Cannot be Blank")
	@NotNull(message = "AccountNumber Cannot be Empty")
	private String bankAccountNumber;

	@NotNull(message = "IFSC Code Cannot be Empty")
	private String ifscCode;

	@NotNull
	private Long orgId;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public Long getEmpCode() {
		return empCode;
	}

	public void setEmpCode(Long empCode) {
		this.empCode = empCode;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getRmCellNumber() {
		return rmCellNumber;
	}

	public void setRmCellNumber(String rmCellNumber) {
		this.rmCellNumber = rmCellNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getDoj() {
		return doj;
	}

	public void setDoj(Date doj) {
		this.doj = doj;
	}

	public String getPanCard() {
		return panCard;
	}

	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}

	public String getUanNumber() {
		return uanNumber;
	}

	public void setUanNumber(String uanNumber) {
		this.uanNumber = uanNumber;
	}

	public String getEsicNumber() {
		return esicNumber;
	}

	public void setEsicNumber(String esicNumber) {
		this.esicNumber = esicNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

}
