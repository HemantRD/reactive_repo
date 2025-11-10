package com.vinsys.hrms.entity.traveldesk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="view_cab_request_report")
public class ReportCabRequest {
	
	@Id
	@Column(name = "row_number")
	private long rowNumber;
	@Column(name = "travel_request_id")
	private long travelRequestId;
	@Column(name = "cab_request_id")
	private long cabRequestId;
	@Column(name = "work_order_no")
	private long won;
	@Column(name = "travel_status")
	private String travelStatus;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "requested_date")
	private Date createdDate;
	@Column(name = "requested_by")
	private String requestedBy;
	@Column(name = "passenger_name")
	private String passengerName;
	@Column(name = "email_id")
	private String emailId;
	@Column(name = "contact_number")
	private String contactNumber;
	@Column(name = "chargeable_to_client")
	private String chargeableToClient;
	@Column(name = "pickup_at")
	private String pickupAt;
	@Column(name = "drop_location")
	private String dropLocation;
	@Column(name = "pickup_date")
	private Date pickupDate;
	@Column(name = "pickup_time")
	private String pickupTime;
	@Column(name = "return_date")
	private Date returnDate;
	@Column(name = "return_time")
	private String returnTime;
	@Column(name = "is_drop_only")
	private String isDropOnly;
	@Column(name = "is_recurring")
	private String isRecurring;
	@Column(name = "is_self_managed")
	private String isSelfManaged;
	@Column(name = "approval_required")
	private String approvalRequired;
	@Column(name = "approver")
	private String approver;
	@Column(name = "approver_status")
	private String approverStatus;
	@Column(name = "cab_request_status")
	private String cabRequestStatus;
	@Column(name = "total_cab_cost")
	private String totalCabCost;
	
	public long getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(long rowNumber) {
		this.rowNumber = rowNumber;
	}
	public long getTravelRequestId() {
		return travelRequestId;
	}
	public void setTravelRequestId(long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}
	public long getCabRequestId() {
		return cabRequestId;
	}
	public void setCabRequestId(long cabRequestId) {
		this.cabRequestId = cabRequestId;
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
	public String getPassengerName() {
		return passengerName;
	}
	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getChargeableToClient() {
		return chargeableToClient;
	}
	public void setChargeableToClient(String chargeableToClient) {
		this.chargeableToClient = chargeableToClient;
	}
	public String getPickupAt() {
		return pickupAt;
	}
	public void setPickupAt(String pickupAt) {
		this.pickupAt = pickupAt;
	}
	public String getDropLocation() {
		return dropLocation;
	}
	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}
	public Date getPickupDate() {
		return pickupDate;
	}
	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}
	public String getPickupTime() {
		return pickupTime;
	}
	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public String getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}
	public String getIsDropOnly() {
		return isDropOnly;
	}
	public void setIsDropOnly(String isDropOnly) {
		this.isDropOnly = isDropOnly;
	}
	public String getIsRecurring() {
		return isRecurring;
	}
	public void setIsRecurring(String isRecurring) {
		this.isRecurring = isRecurring;
	}
	public String getIsSelfManaged() {
		return isSelfManaged;
	}
	public void setIsSelfManaged(String isSelfManaged) {
		this.isSelfManaged = isSelfManaged;
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
	public String getCabRequestStatus() {
		return cabRequestStatus;
	}
	public void setCabRequestStatus(String cabRequestStatus) {
		this.cabRequestStatus = cabRequestStatus;
	}
	public String getTotalCabCost() {
		return totalCabCost;
	}
	public void setTotalCabCost(String totalCabCost) {
		this.totalCabCost = totalCabCost;
	}
	
}
