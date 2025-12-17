package com.vinsys.hrms.master.vo;



public class MasterCityVO {

	
	private long id;
	private String cityName;
	private String cityDescription;
	private MasterStateVO state;
	private String isActive;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityDescription() {
		return cityDescription;
	}
	public void setCityDescription(String cityDescription) {
		this.cityDescription = cityDescription;
	}
	public MasterStateVO getState() {
		return state;
	}
	public void setState(MasterStateVO state) {
		this.state = state;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
}
