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
import com.vinsys.hrms.master.entity.MasterBusType;
import com.vinsys.hrms.master.entity.MasterTravellerType;
import com.vinsys.hrms.master.entity.MasterTripType;

@Entity
@Table(name = "tbl_trn_cab_request_v2")
public class CabRequestV2 extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_trn_cab_request_v2", sequenceName = "seq_trn_cab_request_v2", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_cab_request_v2")
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "travel_type")
	private MasterBusType travelType;
//	private Long travelType;

	@Column(name = "number_of_travellers")
	private Long numberOfTravellers;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "trip_type")
	private MasterTripType tripType;
//	private Long tripType;
	@Column(name = "chargeable_to_client")
	private String chargeableToClient;
	@Column(name = "from_location")
	private String fromLocation;
	@Column(name = "to_location")
	private String toLocation;
	@Column(name = "date_of_journey")
	private Date dateOfJourney;
	@Column(name = "return_date")
	private Date returnDate;
	@Column(name = "preferred_time")
	private String preferredTime;
	@Column(name = "preferred_return_time")
	private String preferredReturnTime;
	@Column(name = "travel_request_id")
	private Long travelRequestId;

	@Column(name = "approximate_distance")
	private String approximateDistance;
	@Column(name = "actual_distance")
	private String actualDistance;
	@Column(name = "approximate_cost")
	private Float approximateCost;
	@Column(name = "approver_id")
	private Long approverId;
	@Column(name = "driver_id")
	private Long driverId;
	@Column(name = "vehicle_id")
	private Long vehicleId;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cab_type_id")
	private MasterTravellerType cabType;
//	private Long cabTypeId;

	@Column(name = "approximate_cost_comment")
	private String approximateCostComment;

//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "trip_type", insertable = false, updatable = false)
//	private MasterTripType masterTripType;
//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "travel_type", insertable = false, updatable = false)
//	private MasterBusType masterTravelType;

//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "cab_type_id", insertable = false, updatable = false)
//	private MasterTravellerType masterTravellerType;

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

	public Date getDateOfJourney() {
		return dateOfJourney;
	}

	public void setDateOfJourney(Date dateOfJourney) {
		this.dateOfJourney = dateOfJourney;
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

	public String getPreferredReturnTime() {
		return preferredReturnTime;
	}

	public void setPreferredReturnTime(String preferredReturnTime) {
		this.preferredReturnTime = preferredReturnTime;
	}

	public Long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(Long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

//	public Long getTravelType() {
//		return travelType;
//	}
//
//	public void setTravelType(Long travelType) {
//		this.travelType = travelType;
//	}

	public Long getNumberOfTravellers() {
		return numberOfTravellers;
	}

	public void setNumberOfTravellers(Long numberOfTravellers) {
		this.numberOfTravellers = numberOfTravellers;
	}

//	public Long getTripType() {
//		return tripType;
//	}
//
//	public void setTripType(Long tripType) {
//		this.tripType = tripType;
//	}

	public String getApproximateDistance() {
		return approximateDistance;
	}

	public void setApproximateDistance(String approximateDistance) {
		this.approximateDistance = approximateDistance;
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

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

//	public Long getCabTypeId() {
//		return cabTypeId;
//	}
//
//	public void setCabTypeId(Long cabTypeId) {
//		this.cabTypeId = cabTypeId;
//	}

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

//	public MasterTravellerType getMasterTravellerType() {
//		return masterTravellerType;
//	}
//
//	public void setMasterTravellerType(MasterTravellerType masterTravellerType) {
//		this.masterTravellerType = masterTravellerType;
//	}

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

	public String getActualDistance() {
		return actualDistance;
	}

	public void setActualDistance(String actualDistance) {
		this.actualDistance = actualDistance;
	}

	public MasterBusType getTravelType() {
		return travelType;
	}

	public void setTravelType(MasterBusType travelType) {
		this.travelType = travelType;
	}

	public MasterTripType getTripType() {
		return tripType;
	}

	public void setTripType(MasterTripType tripType) {
		this.tripType = tripType;
	}

	public MasterTravellerType getCabType() {
		return cabType;
	}

	public void setCabType(MasterTravellerType cabType) {
		this.cabType = cabType;
	}

}
