package com.vinsys.hrms.datamodel;

import java.util.List;

public class VOMasterState extends VOAuditBase {

	private long id;
	private String stateName;
	private String stateDescription;
	private VOMasterCountry country;
	private List<VOMasterCity> cities;

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

	public VOMasterCountry getCountry() {
		return country;
	}

	public void setCountry(VOMasterCountry country) {
		this.country = country;
	}

	public List<VOMasterCity> getCities() {
		return cities;
	}

	public void setCities(List<VOMasterCity> cities) {
		this.cities = cities;
	}

}
