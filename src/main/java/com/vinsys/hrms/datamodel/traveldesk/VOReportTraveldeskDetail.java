package com.vinsys.hrms.datamodel.traveldesk;

public class VOReportTraveldeskDetail {

	private long travelRequestId;
	private long subRequestId;
	private String createdBy;
	private String requestedBy;
	private String createdDate;
	private long won;
	private String fromLocation;
	private String toLocation;
	private String fromDate;
	private String toDate;
	private String roundTrip;
	private String chargeableToClient;
	private String travellingMode;
	private String passengerName;
	private String approvalRequired;
	private String approverName;
	private String requestType;
	
	public long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public long getSubRequestId() {
		return subRequestId;
	}

	public void setSubRequestId(long subRequestId) {
		this.subRequestId = subRequestId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public long getWon() {
		return won;
	}

	public void setWon(long won) {
		this.won = won;
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

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(String roundTrip) {
		this.roundTrip = roundTrip;
	}

	public String getChargeableToClient() {
		return chargeableToClient;
	}

	public void setChargeableToClient(String chargeableToClient) {
		this.chargeableToClient = chargeableToClient;
	}

	public String getTravellingMode() {
		return travellingMode;
	}

	public void setTravellingMode(String travellingMode) {
		this.travellingMode = travellingMode;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getApprovalRequired() {
		return approvalRequired;
	}

	public void setApprovalRequired(String approvalRequired) {
		this.approvalRequired = approvalRequired;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public String getRequestType() {
		return requestType;
	}
	
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
}
