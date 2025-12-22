package com.vinsys.hrms.traveldesk.vo;

public class TravelDeskRequestVO {
	private Long travelRequestId;
	private Long  ticketRequestId;
	private Long cabRequestId;
	private Long accommodationRequestId;
	private String ticketType;
	
	public Long getTravelRequestId() {
		return travelRequestId;
	}
	public void setTravelRequestId(Long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}
	public Long getTicketRequestId() {
		return ticketRequestId;
	}
	public void setTicketRequestId(Long ticketRequestId) {
		this.ticketRequestId = ticketRequestId;
	}
	public Long getCabRequestId() {
		return cabRequestId;
	}
	public void setCabRequestId(Long cabRequestId) {
		this.cabRequestId = cabRequestId;
	}
	public Long getAccommodationRequestId() {
		return accommodationRequestId;
	}
	public void setAccommodationRequestId(Long accommodationRequestId) {
		this.accommodationRequestId = accommodationRequestId;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
}
