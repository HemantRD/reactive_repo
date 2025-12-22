package com.vinsys.hrms.datamodel;

public class VOMasterCity extends VOAuditBase {

	private long id;
	private String cityName;
	private String cityDescription;
	private VOMasterState state;

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

	public VOMasterState getState() {
		return state;
	}

	public void setState(VOMasterState state) {
		this.state = state;
	}

}
