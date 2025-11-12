package com.vinsys.hrms.traveldesk.vo;

public class TicketTravelRequestVO {

	private boolean airDetailsSubmitted;
	private boolean busDetailsSubmitted;
	private boolean trainDetailsSubmitted;
	private AirTicketVO airDetails;
	private BusTicketVO busDetails;
	private TrainTicketVO trainDetails;

	public AirTicketVO getAirDetails() {
		return airDetails;
	}

	public void setAirDetails(AirTicketVO airDetails) {
		this.airDetails = airDetails;
	}

	public BusTicketVO getBusDetails() {
		return busDetails;
	}

	public void setBusDetails(BusTicketVO busDetails) {
		this.busDetails = busDetails;
	}

	public TrainTicketVO getTrainDetails() {
		return trainDetails;
	}

	public void setTrainDetails(TrainTicketVO trainDetails) {
		this.trainDetails = trainDetails;
	}

	public boolean isAirDetailsSubmitted() {
		return airDetailsSubmitted;
	}

	public void setAirDetailsSubmitted(boolean airDetailsSubmitted) {
		this.airDetailsSubmitted = airDetailsSubmitted;
	}

	public boolean isBusDetailsSubmitted() {
		return busDetailsSubmitted;
	}

	public void setBusDetailsSubmitted(boolean busDetailsSubmitted) {
		this.busDetailsSubmitted = busDetailsSubmitted;
	}

	public boolean isTrainDetailsSubmitted() {
		return trainDetailsSubmitted;
	}

	public void setTrainDetailsSubmitted(boolean trainDetailsSubmitted) {
		this.trainDetailsSubmitted = trainDetailsSubmitted;
	}

	

}
