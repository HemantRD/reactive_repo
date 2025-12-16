package com.vinsys.hrms.entity.traveldesk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Immutable
@Table(name="view_traveldesk_detail_report")
public class ReportTraveldeskDetail {

	@Id
	@Column(name = "row_number")
	private String rowNumber;
	@Column(name = "request_id")
	private long travelRequestId;
	@Column(name = "sub_request_id")
	private long subRequestId;
	@Column(name = "requested_by")
	private String requestedBy;
	@Column(name = "created_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date createdDate;
	@Column(name = "won")
	private long won;
	@Column(name = "request_type")
	private String requestType;
	@Column(name = "from_location")
	private String fromLocation;
	@Column(name = "to_location")
	private String toLocation;
	@Column(name = "from_Date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date fromDate;
	@Column(name = "to_Date")	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date toDate;
	@Column(name = "roundtrip")	
	private String roundTrip;
	@Column(name = "chargeable_to_client")	
	private String chargeableToClient;
	@Column(name = "travelling_mode")	
	private String travellingMode;
	@Column(name = "passenger_name")	
	private String passengerName;
	@Column(name = "approval_required")	
	private String approvalRequired;
	@Column(name = "approver_name")	
	private String approverName;
	
	public String getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}
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
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
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

	
}
