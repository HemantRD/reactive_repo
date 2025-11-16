package com.vinsys.hrms.datamodel.traveldesk;

import com.vinsys.hrms.datamodel.VOAuditBase;
import com.vinsys.hrms.datamodel.VOEmployee;

public class VOApproverActionWithComment extends VOAuditBase {

	private long travelRequestId;
	private VOCabRequest cabRequest;
	private VOAccommodationRequest accommodationRequest;
	private VOTicketRequest ticketRequest;
	private String comment;
	private String approverAction;
	private VOEmployee loggedInEmployee;

	public long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public VOCabRequest getCabRequest() {
		return cabRequest;
	}

	public void setCabRequest(VOCabRequest cabRequest) {
		this.cabRequest = cabRequest;
	}

	public VOAccommodationRequest getAccommodationRequest() {
		return accommodationRequest;
	}

	public void setAccommodationRequest(VOAccommodationRequest accommodationRequest) {
		this.accommodationRequest = accommodationRequest;
	}

	public VOTicketRequest getTicketRequest() {
		return ticketRequest;
	}

	public void setTicketRequest(VOTicketRequest ticketRequest) {
		this.ticketRequest = ticketRequest;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getApproverAction() {
		return approverAction;
	}

	public void setApproverAction(String approverAction) {
		this.approverAction = approverAction;
	}

	public VOEmployee getLoggedInEmployee() {
		return loggedInEmployee;
	}

	public void setLoggedInEmployee(VOEmployee loggedInEmployee) {
		this.loggedInEmployee = loggedInEmployee;
	}

}
