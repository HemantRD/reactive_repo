package com.vinsys.hrms.datamodel.traveldesk;

import java.util.List;
import java.util.Set;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOAccommodationRequest extends VOAuditBase {

	private long id;
	private VOTravelRequest travelRequestId;
	private float noOfPeople;
	private String fromDate;
	private String toDate;
	private String preferenceDetails;
	private boolean chargeableToClient;
	private boolean approvalRequired;
	private VOTraveldeskApprover masterApprover;
	private String approverStatus;
	private String accommodationRequestStatus;
	private double totalAccommodationCost;
	// private List<Object> accommodationGuestColHeaders;
	private double totalRefundAmount;

	private Set<VOAccommodationGuest> accommodationGuests;
	private List<VOTravelDeskDocument> accommodationDocument;

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

	public float getNoOfPeople() {
		return noOfPeople;
	}

	public void setNoOfPeople(float noOfPeople) {
		this.noOfPeople = noOfPeople;
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

	public String getPreferenceDetails() {
		return preferenceDetails;
	}

	public void setPreferenceDetails(String preferenceDetails) {
		this.preferenceDetails = preferenceDetails;
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

	public String getAccommodationRequestStatus() {
		return accommodationRequestStatus;
	}

	public void setAccommodationRequestStatus(String accommodationRequestStatus) {
		this.accommodationRequestStatus = accommodationRequestStatus;
	}

	public double getTotalAccommodationCost() {
		return totalAccommodationCost;
	}

	public void setTotalAccommodationCost(double totalAccommodationCost) {
		this.totalAccommodationCost = totalAccommodationCost;
	}

	public Set<VOAccommodationGuest> getAccommodationGuests() {
		return accommodationGuests;
	}

	public void setAccommodationGuests(Set<VOAccommodationGuest> accommodationGuests) {
		this.accommodationGuests = accommodationGuests;
	}

	public List<VOTravelDeskDocument> getAccommodationDocument() {
		return accommodationDocument;
	}

	public void setAccommodationDocument(List<VOTravelDeskDocument> accommodationDocument) {
		this.accommodationDocument = accommodationDocument;
	}

	public double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

}
