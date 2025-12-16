package com.vinsys.hrms.traveldesk.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.traveldesk.MasterModeOfTravel;

@Entity
@Table(name = "tbl_trn_ticket_request_v2")
public class TicketRequestV2 extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_trn_ticket_request_v2", sequenceName = "seq_trn_ticket_request_v2", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_ticket_request_v2")
	private Long id;

	@Column(name = "travel_request_id")
	private Long travelRequestId;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "mode_of_travel")
	private MasterModeOfTravel modeOfTravel;
//	private Long modeOfTravelId;

	@Column(name = "air_type")
	private String airType;
	@Column(name = "bus_type")
	private String busType;
	@Column(name = "train_type")
	private String trainType;
	@Column(name = "round_trip")
	private String roundTrip;
	@Column(name = "no_of_travellers")
	private Long noOfTravellers;
	@Column(name = "chargeable_to_client")
	private String chargeableToClient;
	@Column(name = "from_location")
	private String fromLocation;
	@Column(name = "to_location")
	private String toLocation;
	@Column(name = "date_of_journey")
	private Date dateofJourney;
	@Column(name = "return_date")
	private Date returnDate;
	@Column(name = "preferred_time")
	private String preferredTime;
	
	@Column(name = "approximate_cost")
	private Float approximateCost;
	@Column(name = "approver_id")
	private Long approverId;
	
	@Column(name = "approximate_cost_comment")
	private String approximateCostComment;
	
	@Column(name = "refund_cost")
	private Float refundCost;
	
	@Column(name = "settled_cost")
	private Float settledCost;
	
	
//	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//	@JoinColumn(name = "mode_of_travel", insertable = false, updatable = false)
//	private MasterModeOfTravel modeOfTravel;

	public String getPreferredReturnTime() {
		return preferredReturnTime;
	}

	public void setPreferredReturnTime(String preferredReturnTime) {
		this.preferredReturnTime = preferredReturnTime;
	}

	@Column(name = "preferred_return_time")
	private String preferredReturnTime;
	
	@Column(name = "final_cost")
	private Float finalCost;
	@Column(name = "final_cost_comment")
	private String finalCostComment;

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

//	public Long getModeOfTravelId() {
//		return modeOfTravelId;
//	}
//
//	public void setModeOfTravelId(Long modeOfTravelId) {
//		this.modeOfTravelId = modeOfTravelId;
//	}

	public String getAirType() {
		return airType;
	}

	public void setAirType(String airType) {
		this.airType = airType;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getTrainType() {
		return trainType;
	}

	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}

	public String getRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(String roundTrip) {
		this.roundTrip = roundTrip;
	}

	public Long getNoOfTravellers() {
		return noOfTravellers;
	}

	public void setNoOfTravellers(Long noOfTravellers) {
		this.noOfTravellers = noOfTravellers;
	}

	public String getChargeableToClient() {
		return chargeableToClient;
	}

	public void setChargeableToClient(String chargeableToClient) {
		this.chargeableToClient = chargeableToClient;
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

	public Date getDateofJourney() {
		return dateofJourney;
	}

	public void setDateofJourney(Date dateofJourney) {
		this.dateofJourney = dateofJourney;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getPreferredTime() {
		return preferredTime;
	}

	public void setPreferredTime(String preferredTime) {
		this.preferredTime = preferredTime;
	}

	public MasterModeOfTravel getModeOfTravel() {
		return modeOfTravel;
	}

	public void setModeOfTravel(MasterModeOfTravel modeOfTravel) {
		this.modeOfTravel = modeOfTravel;
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
