package com.vinsys.hrms.datamodel.traveldesk;

public class VOTicketRequestPassenger extends VOTravellerDetails {

	private long id;
	private VOTicketRequest ticketRequest;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOTicketRequest getTicketRequest() {
		return ticketRequest;
	}

	public void setTicketRequest(VOTicketRequest ticketRequest) {
		this.ticketRequest = ticketRequest;
	}

}
