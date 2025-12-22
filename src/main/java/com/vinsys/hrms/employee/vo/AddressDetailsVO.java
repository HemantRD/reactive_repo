package com.vinsys.hrms.employee.vo;

/**
 * 
 * @author Onkar A.
 *
 */
public class AddressDetailsVO {

	private String addressLine1;
	private String addressLine2;
	private String street;
	private String landMark;
	private Long pincode;
	private String addressType;
	private String city;
	private String country;
	private String state;
	private String isRental;
	private String ownerName;
	private String ownerAadhar;
	private Long ownerContact;
	private Long candidateId;
	private String nationality;
	private String citizenship;
	private Long id;

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public Long getPincode() {
		return pincode;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerAadhar() {
		return ownerAadhar;
	}

	public void setOwnerAadhar(String ownerAadhar) {
		this.ownerAadhar = ownerAadhar;
	}

	public Long getOwnerContact() {
		return ownerContact;
	}

	public void setOwnerContact(Long ownerContact) {
		this.ownerContact = ownerContact;
	}

	public String getIsRental() {
		return isRental;
	}

	public void setIsRental(String isRental) {
		this.isRental = isRental;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
