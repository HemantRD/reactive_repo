package com.vinsys.hrms.master.vo;

import javax.validation.constraints.NotBlank;

public class CountryMasterVO {

	private Long id;
	private String countryName;
	private String countryDescription;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountryDescription() {
		return countryDescription;
	}
	public void setCountryDescription(String countryDescription) {
		this.countryDescription = countryDescription;
	}
	
}
