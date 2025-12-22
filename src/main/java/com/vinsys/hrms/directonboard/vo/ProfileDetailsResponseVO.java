package com.vinsys.hrms.directonboard.vo;

import com.vinsys.hrms.master.vo.GenderMasterVO;
import com.vinsys.hrms.master.vo.MasterMaritialStatusVo;
import com.vinsys.hrms.master.vo.MasterTitleVo;

public class ProfileDetailsResponseVO {

	
	private Long id;
	private MasterTitleVo salutation;
	private String firstName;
	private String middleName;
	private String lastName;
	private GenderMasterVO gender;
	private String emailId;
	private String dateOfBirth;
	private Long mobileNumber;
	private MasterMaritialStatusVo maritalStatus;
	private String spouceName;
	private String adharCardNumber;
	private String panCardNumber;
	private String drivingLicenseNumber;
	private String passportNumber;
	private Long orgId;
	private String roleUpdated;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public MasterTitleVo getSalutation() {
		return salutation;
	}
	public void setSalutation(MasterTitleVo salutation) {
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
	public GenderMasterVO getGender() {
		return gender;
	}
	public void setGender(GenderMasterVO gender) {
		this.gender = gender;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Long getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public MasterMaritialStatusVo getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(MasterMaritialStatusVo maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getSpouceName() {
		return spouceName;
	}
	public void setSpouceName(String spouceName) {
		this.spouceName = spouceName;
	}
	public String getAdharCardNumber() {
		return adharCardNumber;
	}
	public void setAdharCardNumber(String adharCardNumber) {
		this.adharCardNumber = adharCardNumber;
	}
	public String getPanCardNumber() {
		return panCardNumber;
	}
	public void setPanCardNumber(String panCardNumber) {
		this.panCardNumber = panCardNumber;
	}
	public String getDrivingLicenseNumber() {
		return drivingLicenseNumber;
	}
	public void setDrivingLicenseNumber(String drivingLicenseNumber) {
		this.drivingLicenseNumber = drivingLicenseNumber;
	}
	public String getPassportNumber() {
		return passportNumber;
	}
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getRoleUpdated() {
		return roleUpdated;
	}
	public void setRoleUpdated(String roleUpdated) {
		this.roleUpdated = roleUpdated;
	}
	
}
