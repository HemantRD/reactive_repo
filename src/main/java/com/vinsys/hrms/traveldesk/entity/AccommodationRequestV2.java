package com.vinsys.hrms.traveldesk.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_trn_accommodation_request_v2")
public class AccommodationRequestV2 extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_trn_accommodation_request_v2", sequenceName = "seq_trn_accommodation_request_v2", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_accommodation_request_v2")
	private Long id;
	@Column(name = "travel_request_id")
	private Long travelRequestId;
	@Column(name = "number_of_rooms")
	private Long numberOfRooms;
	@Column(name = "number_of_travellers")
	private Long numberOfTravellers;
	@Column(name = "location")
	private String location;
	@Column(name = "chargeable_to_client")
	private String chargeableToClient;
	@Column(name = "from_date")
	private Date fromDate;
	@Column(name = "to_date")
	private Date toDate;
	@Column(name = "check_in_time")
	private String checkInTime;
	@Column(name = "check_out_time")
	private String checkOutTime;
	
	
	@Column(name = "approximate_cost")
	private Float approximateCost;
	@Column(name = "approver_id")
	private Long approverId;

	@Column(name = "approximate_cost_comment")
	private String approximateCostComment;
	
	@Column(name = "final_cost")
	private Float finalCost;
	@Column(name = "final_cost_comment")
	private String finalCostComment;
	
	@Column(name = "refund_cost")
	private Float refundCost;
	
	@Column(name = "settled_cost")
	private Float settledCost;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(Long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public Long getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(Long numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public Long getNumberOfTravellers() {
		return numberOfTravellers;
	}

	public void setNumberOfTravellers(Long numberOfTravellers) {
		this.numberOfTravellers = numberOfTravellers;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}



	public Float getApproximateCost() {
		return approximateCost;
	}

	public void setApproximateCost(Float approximateCost) {
		this.approximateCost = approximateCost;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public String getApproximateCostComment() {
		return approximateCostComment;
	}

	public void setApproximateCostComment(String approximateCostComment) {
		this.approximateCostComment = approximateCostComment;
	}

	public Float getFinalCost() {
		return finalCost;
	}

	public void setFinalCost(Float finalCost) {
		this.finalCost = finalCost;
	}

	public String getFinalCostComment() {
		return finalCostComment;
	}

	public void setFinalCostComment(String finalCostComment) {
		this.finalCostComment = finalCostComment;
	}

	public Float getRefundCost() {
		return refundCost;
	}

	public void setRefundCost(Float refundCost) {
		this.refundCost = refundCost;
	}

	public Float getSettledCost() {
		return settledCost;
	}

	public void setSettledCost(Float settledCost) {
		this.settledCost = settledCost;
	}

	
}
