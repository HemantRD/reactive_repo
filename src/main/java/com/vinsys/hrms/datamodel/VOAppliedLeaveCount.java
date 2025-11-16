package com.vinsys.hrms.datamodel;

public class VOAppliedLeaveCount {

	private String responseMessage;
	private int responseCode;
	private double calculatedLeave;

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public double getCalculatedLeave() {
		return calculatedLeave;
	}

	public void setCalculatedLeave(double calculatedLeave) {
		this.calculatedLeave = calculatedLeave;
	}

}
