package com.vinsys.hrms.traveldesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public class TravelDetailsDeleteVO {

	@Schema(required = true)
	private Long id;

	@Schema(required = true)
	private Long travelRequestId;

    private Long ticketRequestId;
    
    private Long cabRequestId;
    
    private Long accomadationRequestId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Long getAccomadationRequestId() {
		return accomadationRequestId;
	}

	public void setAccomadationRequestId(Long accomadationRequestId) {
		this.accomadationRequestId = accomadationRequestId;
	}

	

}
