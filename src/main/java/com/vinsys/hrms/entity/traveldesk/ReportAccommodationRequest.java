package com.vinsys.hrms.entity.traveldesk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="view_accomodation_request_report")
public class ReportAccommodationRequest {
	
	@Id
	@Column(name = "travel_request_id")
	private long travelRequestId;
	@Column(name = "accommodation_request_id")
	private long accommodationRequestId;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "requested_by")
	private String requestedBy;
	@Column(name = "created_date")
	private Date createdDate;
	@Column(name = "work_order_no")
	private long won;
	@Column(name = "approval_required")
	private String approvalRequired;
	@Column(name = "approver")
	private String approver;
	@Column(name = "approver_status")
	private String approverStatus;
	@Column(name = "chargeable_to_client")
	private String chargeableToClient;
	@Column(name = "from_date")
	private Date fromDate;
	@Column(name = "to_date")
	private Date toDate;
	@Column(name = "no_of_people")
	private long noOfPeople;
	@Column(name = "total_accommodation_cost")
	private double totalAccommodationCost;
	@Column(name = "guest_name")
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
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
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
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
	public String getGuestName() {
		return guestName;
	}
	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
	
	
}
