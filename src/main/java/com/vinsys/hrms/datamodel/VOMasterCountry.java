package com.vinsys.hrms.datamodel;

import java.util.List;

public class VOMasterCountry extends VOAuditBase {

	private long id;
	private String countryName;
	private String countryDescription;
	private List<VOMasterState> states;

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

	public List<VOMasterState> getStates() {
		return states;
	}

	public void setStates(List<VOMasterState> states) {
		this.states = states;
	}

}
