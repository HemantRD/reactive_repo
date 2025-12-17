package com.vinsys.hrms.master.vo;

import java.util.List;



public class MasterStateVO {

	
	private long id;
	private String stateName;
	private String stateDescription;
	private MasterCountryVO country;
	private List<MasterCityVO> cities;
	private String isActive;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getStateDescription() {
		return stateDescription;
	}
	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}
	
	public MasterCountryVO getCountry() {
		return country;
	}
	public void setCountry(MasterCountryVO country) {
		this.country = country;
	}
	public List<MasterCityVO> getCities() {
		return cities;
	}
	public void setCities(List<MasterCityVO> cities) {
		this.cities = cities;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	
}
