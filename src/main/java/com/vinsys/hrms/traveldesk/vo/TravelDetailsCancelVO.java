package com.vinsys.hrms.traveldesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public class TravelDetailsCancelVO {

	
	@Schema(required = true)
	private Long travelRequestId;
	
	@Schema(required = true,description = "If logged in employee is TD then only manditory")
	private Long requesterId;

	public Long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(Long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}
	
	
}
