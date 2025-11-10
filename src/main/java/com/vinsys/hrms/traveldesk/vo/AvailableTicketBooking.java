package com.vinsys.hrms.traveldesk.vo;

/**
 * @author Onkar A
 *
 * 
 */
public class AvailableTicketBooking {

	private String modeOfTravel;
	private String name;
	private boolean value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModeOfTravel() {
		return modeOfTravel;
	}

	public void setModeOfTravel(String modeOfTravel) {
		this.modeOfTravel = modeOfTravel;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
}
