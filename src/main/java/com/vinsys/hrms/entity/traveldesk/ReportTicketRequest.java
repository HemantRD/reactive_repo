package com.vinsys.hrms.entity.traveldesk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="view_ticket_request_report")
public class ReportTicketRequest {
	
	@Id
	@Column(name = "travel_request_id")
	private long travelRequestId;
	@Column(name = "ticket_request_id")
	private long ticketRequestId;
	@Column(name = "won")
	private long won;
	@Column(name = "travel_status")
	private String travelStatus;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "created_date")
	private Date createdDate;
	@Column(name = "requested_by")
	private String requestedBy;
	@Column(name = "approval_required")
	private String approvalRequired;
	@Column(name = "approver_status")
	private String approverStatus;
	@Column(name = "fromlocation")
	private String fromLocation;
	@Column(name = "tolocation")
	private String toLocation;
	@Column(name = "chargeable_to_client")
	private String chargeableToClient;
	@Column(name = "no_of_traveller")
	private String noOfTraveller;
	@Column(name = "preference_details")
	private String preferenceDetails;
	@Column(name = "roundtrip")
	private String roundTrip;
	@Column(name = "ticket_request_status")
	private String ticketRequestStatus;
	@Column(name = "total_ticket_fare")
	private String totalTicketFare;
	@Column(name = "approver_id")
	private String approverId;
	@Column(name = "approver")
	private String approver;
	@Column(name = "mode_of_travel")
	private String modeOfTravel;
	@Column(name = "passenger_name")
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
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
