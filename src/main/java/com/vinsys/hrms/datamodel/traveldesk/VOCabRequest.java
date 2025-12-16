package com.vinsys.hrms.datamodel.traveldesk;

import java.util.Set;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOCabRequest extends VOAuditBase {

	private long id;
	private VOTravelRequest travelRequest;
	private double totalCabCost;
	private String cabRequestStatus;
	private boolean approvalRequired;
	private VOTraveldeskApprover masterApprover;
	private String approverStatus;
	// private List<Object> cabPassengerColHeaders;

	private Set<VOCabRequestPassenger> cabRequestPassengers;
	private double totalRefundAmount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOTravelRequest getTravelRequest() {
		return travelRequest;
	}

	public void setTravelRequest(VOTravelRequest travelRequest) {
		this.travelRequest = travelRequest;
	}

	public double getTotalCabCost() {
		return totalCabCost;
	}

	public void setTotalCabCost(double totalCabCost) {
		this.totalCabCost = totalCabCost;
	}

	public String getCabRequestStatus() {
		return cabRequestStatus;
	}

	public void setCabRequestStatus(String cabRequestStatus) {
		this.cabRequestStatus = cabRequestStatus;
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

	public Set<VOCabRequestPassenger> getCabRequestPassengers() {
		return cabRequestPassengers;
	}

	public void setCabRequestPassengers(Set<VOCabRequestPassenger> cabRequestPassengers) {
		this.cabRequestPassengers = cabRequestPassengers;
	}

	public double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

}
