package com.vinsys.hrms.datamodel.traveldesk;

import com.vinsys.hrms.datamodel.VOOrganization;

public class VOMasterModeOfTravel {

	private long id;
	private String modeOfTravel;
	private String modeOfTravelDescription;
	private VOOrganization organization;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getModeOfTravel() {
		return modeOfTravel;
	}

	public void setModeOfTravel(String modeOfTravel) {
		this.modeOfTravel = modeOfTravel;
	}

	public String getModeOfTravelDescription() {
		return modeOfTravelDescription;
	}

	public void setModeOfTravelDescription(String modeOfTravelDescription) {
		this.modeOfTravelDescription = modeOfTravelDescription;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

}
