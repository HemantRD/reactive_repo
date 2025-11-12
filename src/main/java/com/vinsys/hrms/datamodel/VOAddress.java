package com.vinsys.hrms.datamodel;

import com.vinsys.hrms.entity.AuditBase;

public class VOAddress extends AuditBase {

	private static final long serialVersionUID = 1L;
	private String addressLine1;
	private String addressLine2;
	private VOMasterCity city;
	private VOMasterState state;
	private VOMasterCountry country;
	private Long pincode;
	private String phone1;
	private String phone2;
	private Long ssnNumber;
	private String nationality;
	private String isRental;
	private String ownerName;
	private Long ownerContact;
	private String ownerAdhar;

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

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public VOMasterCountry getCountry() {
		return country;
	}

	public void setCountry(VOMasterCountry country) {
		this.country = country;
	}

	public Long getSsnNumber() {
		return ssnNumber;
	}

	public void setSsnNumber(Long ssnNumber) {
		this.ssnNumber = ssnNumber;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
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

	public String getOwnerAdhar() {
		return ownerAdhar;
	}

	public void setOwnerAdhar(String ownerAdhar) {
		this.ownerAdhar = ownerAdhar;
	}
	

}
