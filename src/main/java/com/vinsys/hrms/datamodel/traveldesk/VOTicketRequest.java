package com.vinsys.hrms.datamodel.traveldesk;

import java.util.List;
import java.util.Set;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOTicketRequest extends VOAuditBase {

	private long id;
	private VOTravelRequest travelRequestId;
	private VOMasterModeOfTravel masterModeOfTravel;
	private float noOfTraveller;
	private String preferenceDetails;
	private String preferredDate;
	private String preferredTime;
	private String fromLocation;
	private String toLocation;
	private boolean roundTrip;
	private String returnPreferenceDetails;
	private String returnPreferredDate;
	private String returnPreferredTime;
	private double totalTicketFare;
	private boolean chargeableToClient;
	private boolean approvalRequired;
	private VOTraveldeskApprover masterApprover;

	private String approverStatus;
	private String ticketRequestStatus;

	// private List<Object> ticketPassengerColHeaders;
	private Set<VOTicketRequestPassenger> ticketRequestPassengers;
	private List<VOTravelDeskDocument> ticketDocument;
	private double totalRefundAmount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOTravelRequest getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(VOTravelRequest travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public VOMasterModeOfTravel getMasterModeOfTravel() {
		return masterModeOfTravel;
	}

	public void setMasterModeOfTravel(VOMasterModeOfTravel masterModeOfTravel) {
		this.masterModeOfTravel = masterModeOfTravel;
	}

	public float getNoOfTraveller() {
		return noOfTraveller;
	}

	public void setNoOfTraveller(float noOfTraveller) {
		this.noOfTraveller = noOfTraveller;
	}

	public String getPreferenceDetails() {
		return preferenceDetails;
	}

	public void setPreferenceDetails(String preferenceDetails) {
		this.preferenceDetails = preferenceDetails;
	}

	public String getPreferredDate() {
		return preferredDate;
	}

	public void setPreferredDate(String preferredDate) {
		this.preferredDate = preferredDate;
	}

	public String getPreferredTime() {
		return preferredTime;
	}

	public void setPreferredTime(String preferredTime) {
		this.preferredTime = preferredTime;
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

	public boolean isRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(boolean roundTrip) {
		this.roundTrip = roundTrip;
	}

	public String getReturnPreferenceDetails() {
		return returnPreferenceDetails;
	}

	public void setReturnPreferenceDetails(String returnPreferenceDetails) {
		this.returnPreferenceDetails = returnPreferenceDetails;
	}

	public String getReturnPreferredDate() {
		return returnPreferredDate;
	}

	public void setReturnPreferredDate(String returnPreferredDate) {
		this.returnPreferredDate = returnPreferredDate;
	}

	public String getReturnPreferredTime() {
		return returnPreferredTime;
	}

	public void setReturnPreferredTime(String returnPreferredTime) {
		this.returnPreferredTime = returnPreferredTime;
	}

	public double getTotalTicketFare() {
		return totalTicketFare;
	}

	public void setTotalTicketFare(double totalTicketFare) {
		this.totalTicketFare = totalTicketFare;
	}

	public boolean isChargeableToClient() {
		return chargeableToClient;
	}

	public void setChargeableToClient(boolean chargeableToClient) {
		this.chargeableToClient = chargeableToClient;
	}

	public boolean isApprovalRequired() {
		return approvalRequired;
	}

	public void setApprovalRequired(boolean approvalRequired) {
		this.approvalRequired = approvalRequired;
	}

	public VOTraveldeskApprover getMasterApprover() {
		return masterApprover;
	}

	public void setMasterApprover(VOTraveldeskApprover masterApprover) {
		this.masterApprover = masterApprover;
	}

	public String getApproverStatus() {
		return approverStatus;
	}

	public void setApproverStatus(String approverStatus) {
		this.approverStatus = approverStatus;
	}

	public String getTicketRequestStatus() {
		return ticketRequestStatus;
	}

	public void setTicketRequestStatus(String ticketRequestStatus) {
		this.ticketRequestStatus = ticketRequestStatus;
	}

	public Set<VOTicketRequestPassenger> getTicketRequestPassengers() {
		return ticketRequestPassengers;
	}

	public void setTicketRequestPassengers(Set<VOTicketRequestPassenger> ticketRequestPassengers) {
		this.ticketRequestPassengers = ticketRequestPassengers;
	}

	public List<VOTravelDeskDocument> getTicketDocument() {
		return ticketDocument;
	}

	public void setTicketDocument(List<VOTravelDeskDocument> ticketDocument) {
		this.ticketDocument = ticketDocument;
	}

	public double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

}
