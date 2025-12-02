package com.vinsys.hrms.directonboard.vo;

import com.vinsys.hrms.master.vo.MasterCityVO;
import com.vinsys.hrms.master.vo.MasterCountryVO;
import com.vinsys.hrms.master.vo.MasterStateVO;

public class CandidateAddressVo {
	private Long id;
	private String addressLine1;
	private String street;
	private String landMark;
	private MasterCountryVO country;
	private MasterCityVO city;
	private MasterStateVO state;
	private Long pincode;
	private String isRental;
	private String ownerName;
	private Long ownerContact;
	private String ownerAadhar;
	private String addressType;
	private String nationality;
	private String citizenship;
	private long candidateId;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
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

	public MasterCountryVO getCountry() {
		return country;
	}

	public void setCountry(MasterCountryVO country) {
		this.country = country;
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

	public Long getPincode() {
		return pincode;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public String getIsRental() {
		return isRental;
	}

	public void setIsRental(String isRental) {
		this.isRental = isRental;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Long getOwnerContact() {
		return ownerContact;
	}

	public void setOwnerContact(Long ownerContact) {
		this.ownerContact = ownerContact;
	}

	public String getOwnerAadhar() {
		return ownerAadhar;
	}

	public void setOwnerAadhar(String ownerAadhar) {
		this.ownerAadhar = ownerAadhar;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
	}
	
	

}
