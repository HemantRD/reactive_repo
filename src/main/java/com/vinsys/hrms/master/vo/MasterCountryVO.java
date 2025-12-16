package com.vinsys.hrms.master.vo;

import java.util.List;



public class MasterCountryVO {

	private long id;
	private String countryName;
	private String countryDescription;
	private List<MasterStateVO> states;
	private String isActive;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	public List<MasterStateVO> getStates() {
		return states;
	}
	public void setStates(List<MasterStateVO> states) {
		this.states = states;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	

}
