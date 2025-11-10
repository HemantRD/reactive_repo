package com.vinsys.hrms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public class Address extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "address_line_1")
	private String addressLine1;
	@Column(name = "address_line_2")
	private String addressLine2;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "city_id")
	private MasterCity city;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "state_id")
	private MasterState state;
	@Column(name = "pincode")
	private Long pincode;
	
	@Column(name = "phone_1")
	private String phone1;
	
	@Column(name = "phone_2")
	private String phone2;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "country_id")
	private MasterCountry country;
	

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

	public MasterCity getCity() {
		return city;
	}

	public void setCity(MasterCity city) {
		this.city = city;
	}

	public MasterState getState() {
		return state;
	}

	public void setState(MasterState state) {
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

	public MasterCountry getCountry() {
		return country;
	}

	public void setCountry(MasterCountry country) {
		this.country = country;
	}
	
}
