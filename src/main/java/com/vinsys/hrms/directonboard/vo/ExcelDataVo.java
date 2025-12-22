package com.vinsys.hrms.directonboard.vo;

import java.util.Date;

public class ExcelDataVo {

	// login entity
	private Long srNo;
	private String email;
	private Date dob;
	private String salutation;
	private String firstName;
	private String middleName;
	private String lastName;
	private String gender;
	private Long mobileNo;

	private String employmentTypeId;

	private String designationId;
	private Date dateOfJoining;
	private String branchId;
	private String contryId;
	private String stateId;
	private String cityId;
	private String departmentId;
	private String divisionId;


	// cand personal
	private String marritalStatus;
	

	// employee
	private int probationPeriod;
	private int noticePeriod;
	private Long employeeCode;

	private String role;
	private String reportingManager;
	public Long getSrNo() {
		return srNo;
	}
	public void setSrNo(Long srNo) {
		this.srNo = srNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Long getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmploymentTypeId() {
		return employmentTypeId;
	}
	public void setEmploymentTypeId(String employmentTypeId) {
		this.employmentTypeId = employmentTypeId;
	}
	public String getDesignationId() {
		return designationId;
	}
	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}
	public Date getDateOfJoining() {
		return dateOfJoining;
	}
	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getContryId() {
		return contryId;
	}
	public void setContryId(String contryId) {
		this.contryId = contryId;
	}
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	public String getMarritalStatus() {
		return marritalStatus;
	}
	public void setMarritalStatus(String marritalStatus) {
		this.marritalStatus = marritalStatus;
	}
	public int getProbationPeriod() {
		return probationPeriod;
	}
	public void setProbationPeriod(int probationPeriod) {
		this.probationPeriod = probationPeriod;
	}
	public int getNoticePeriod() {
		return noticePeriod;
	}
	public void setNoticePeriod(int noticePeriod) {
		this.noticePeriod = noticePeriod;
	}
	public Long getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(Long employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getReportingManager() {
		return reportingManager;
	}
	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}
	@Override
	public String toString() {
		return "ExcelDataVo [srNo=" + srNo + ", email=" + email + ", dob=" + dob + ", salutation=" + salutation
				+ ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", gender="
				+ gender + ", mobileNo=" + mobileNo + ", employmentTypeId=" + employmentTypeId + ", designationId="
				+ designationId + ", dateOfJoining=" + dateOfJoining + ", branchId=" + branchId + ", contryId="
				+ contryId + ", stateId=" + stateId + ", cityId=" + cityId + ", departmentId=" + departmentId
				+ ", divisionId=" + divisionId + ", marritalStatus=" + marritalStatus + ", probationPeriod="
				+ probationPeriod + ", noticePeriod=" + noticePeriod + ", employeeCode=" + employeeCode + ", role="
				+ role + ", reportingManager=" + reportingManager + "]";
	}

	
	
	
	

}
