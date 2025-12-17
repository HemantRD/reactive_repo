package com.vinsys.hrms.datamodel.traveldesk;

public class VODriverAssignmentModel extends VOCabRequestPassenger {

	private long travelRequestId;
	private String tripType;

	public long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

}
