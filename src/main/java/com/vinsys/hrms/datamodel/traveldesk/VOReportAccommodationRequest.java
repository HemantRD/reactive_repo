package com.vinsys.hrms.datamodel.traveldesk;

public class VOReportAccommodationRequest {

	private long travelRequestId;
	private long accommodationRequestId;
	private String createdBy;
	private String requestedBy;
	private String createdDate;
	private long won;
	private String approvalRequired;
	private String approver;
	private String approverStatus;
	private String chargeableToClient;
	private String fromDate;
	private String toDate;
	private long noOfPeople;
	private double totalAccommodationCost;
	private String travelRequestStatus;
	private String accommodationRequestStatus;
	private String guestName;
	
	public long getTravelRequestId() {
		return travelRequestId;
	}
	public void setTravelRequestId(long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}
	public long getAccommodationRequestId() {
		return accommodationRequestId;
	}
	public void setAccommodationRequestId(long accommodationRequestId) {
		this.accommodationRequestId = accommodationRequestId;
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
	public String getApprovalRequired() {
		return approvalRequired;
	}
	public void setApprovalRequired(String approvalRequired) {
		this.approvalRequired = approvalRequired;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getApproverStatus() {
		return approverStatus;
	}
	public void setApproverStatus(String approverStatus) {
		this.approverStatus = approverStatus;
	}
	public String getChargeableToClient() {
		return chargeableToClient;
	}
	public void setChargeableToClient(String chargeableToClient) {
		this.chargeableToClient = chargeableToClient;
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
	public long getNoOfPeople() {
		return noOfPeople;
	}
	public void setNoOfPeople(long noOfPeople) {
		this.noOfPeople = noOfPeople;
	}
	public double getTotalAccommodationCost() {
		return totalAccommodationCost;
	}
	public void setTotalAccommodationCost(double totalAccommodationCost) {
		this.totalAccommodationCost = totalAccommodationCost;
	}
	public String getTravelRequestStatus() {
		return travelRequestStatus;
	}
	public void setTravelRequestStatus(String travelRequestStatus) {
		this.travelRequestStatus = travelRequestStatus;
	}
	public String getAccommodationRequestStatus() {
		return accommodationRequestStatus;
	}
	public void setAccommodationRequestStatus(String accommodationRequestStatus) {
		this.accommodationRequestStatus = accommodationRequestStatus;
	}
	public String getGuestName() {
		return guestName;
	}
	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
	
	
}
