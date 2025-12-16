package com.vinsys.hrms.datamodel.traveldesk;

public class VOReportTicketRequest {

	private long travelRequestId;
	private long ticketRequestId;
	private long won;
	private String travelStatus;
	private String createdBy;
	private String createdDate;
	private String requestedBy;
	private String approvalRequired;
	private String approverStatus;
	private String fromLocation;
	private String toLocation;
	private String chargeableToClient;
	private String noOfTraveller;
	private String preferenceDetails;
	private String roundTrip;
	private String ticketRequestStatus;
	private String totalTicketFare;
	private String approverId;
	private String approver;
	private String modeOfTravel;
	private String passengerName;

	public long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public long getTicketRequestId() {
		return ticketRequestId;
	}

	public void setTicketRequestId(long ticketRequestId) {
		this.ticketRequestId = ticketRequestId;
	}

	public long getWon() {
		return won;
	}

	public void setWon(long won) {
		this.won = won;
	}

	public String getTravelStatus() {
		return travelStatus;
	}

	public void setTravelStatus(String travelStatus) {
		this.travelStatus = travelStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getApprovalRequired() {
		return approvalRequired;
	}

	public void setApprovalRequired(String approvalRequired) {
		this.approvalRequired = approvalRequired;
	}

	public String getApproverStatus() {
		return approverStatus;
	}

	public void setApproverStatus(String approverStatus) {
		this.approverStatus = approverStatus;
	}

	public String getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public String getChargeableToClient() {
		return chargeableToClient;
	}

	public void setChargeableToClient(String chargeableToClient) {
		this.chargeableToClient = chargeableToClient;
	}

	public String getNoOfTraveller() {
		return noOfTraveller;
	}

	public void setNoOfTraveller(String noOfTraveller) {
		this.noOfTraveller = noOfTraveller;
	}

	public String getPreferenceDetails() {
		return preferenceDetails;
	}

	public void setPreferenceDetails(String preferenceDetails) {
		this.preferenceDetails = preferenceDetails;
	}

	public String getRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(String roundTrip) {
		this.roundTrip = roundTrip;
	}

	public String getTicketRequestStatus() {
		return ticketRequestStatus;
	}

	public void setTicketRequestStatus(String ticketRequestStatus) {
		this.ticketRequestStatus = ticketRequestStatus;
	}

	public String getTotalTicketFare() {
		return totalTicketFare;
	}

	public void setTotalTicketFare(String totalTicketFare) {
		this.totalTicketFare = totalTicketFare;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getModeOfTravel() {
		return modeOfTravel;
	}

	public void setModeOfTravel(String modeOfTravel) {
		this.modeOfTravel = modeOfTravel;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

}
