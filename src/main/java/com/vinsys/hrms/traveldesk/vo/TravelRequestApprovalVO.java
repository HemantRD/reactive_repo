package com.vinsys.hrms.traveldesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public class TravelRequestApprovalVO {

	@Schema(required = true)
	private Long travelRequestId;
	
	@Schema(required = true)
    private String approverComment;

	public Long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(Long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}
	public String getApproverComment() {
		return approverComment;
	}
	public void setApproverComment(String approverComment) {
		this.approverComment = approverComment;
	}

	

	
	
	
}
