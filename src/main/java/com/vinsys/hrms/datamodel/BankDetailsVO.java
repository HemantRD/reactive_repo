package com.vinsys.hrms.datamodel;

public class BankDetailsVO extends VOAuditBase {
	private Long bankId;
	private String accountNumber;
	private String bankName;
	private String branchLocation;
	private String ifscCode;
	private String fullName;
	private String mobileNumber;
	private VOCandidateProfessionalDetail candidateProfessionalDetail;
	
	
	public Long getBankId() {
		return bankId;
	}
	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchLocation() {
		return branchLocation;
	}
	public void setBranchLocation(String branchLocation) {
		this.branchLocation = branchLocation;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public VOCandidateProfessionalDetail getCandidateProfessionalDetail() {
		return candidateProfessionalDetail;
	}
	public void setCandidateProfessionalDetail(VOCandidateProfessionalDetail candidateProfessionalDetail) {
		this.candidateProfessionalDetail = candidateProfessionalDetail;
	}
	
	

	
}
