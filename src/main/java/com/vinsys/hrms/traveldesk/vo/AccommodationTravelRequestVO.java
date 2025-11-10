package com.vinsys.hrms.traveldesk.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class AccommodationTravelRequestVO {

	
	private long id;
	@Schema(required = true)
	private Long noOfRooms;
	@Schema(required = true)
	private Long noOfTravellers;
	private String chargableClient;
	@Schema(required = true)
	private String location;
	@Schema(required = true)
	private String fromDate;
	@Schema(required = true)
	private String toDate;
	private String checkInTime;
	private String checkOutTime;
	@Schema(required = true)
	private Long travelRequestId;
	private List<TravellerDetailsVO> travellerDetails;
	private Float approximateCost;
	private String approximateComment;
//	private TravelApproverResponseVO approver;
	private Float finalCost;
	private String finalCostcomment;
	
	public List<TravellerDetailsVO> getTravellerDetails() {
		return travellerDetails;
	}

	public void setTravellerDetails(List<TravellerDetailsVO> travellerDetails) {
		this.travellerDetails = travellerDetails;
	}

	

	public Long getNoOfRooms() {
		return noOfRooms;
	}

	public void setNoOfRooms(Long noOfRooms) {
		this.noOfRooms = noOfRooms;
	}

	public Long getNoOfTravellers() {
		return noOfTravellers;
	}

	public void setNoOfTravellers(Long noOfTravellers) {
		this.noOfTravellers = noOfTravellers;
	}

	public String getChargableClient() {
		return chargableClient;
	}

	public void setChargableClient(String chargableClient) {
		this.chargableClient = chargableClient;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public Long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(Long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public Float getApproximateCost() {
		return approximateCost;
	}

	public void setApproximateCost(Float approximateCost) {
		this.approximateCost = approximateCost;
	}

	

//	public TravelApproverResponseVO getApprover() {
//		return approver;
//	}
//
//	public void setApprover(TravelApproverResponseVO approver) {
//		this.approver = approver;
//	}



	public String getApproximateComment() {
		return approximateComment;
	}

	public void setApproximateComment(String approximateComment) {
		this.approximateComment = approximateComment;
	}

	public Float getFinalCost() {
		return finalCost;
	}

	public void setFinalCost(Float finalCost) {
		this.finalCost = finalCost;
	}

	public String getFinalCostcomment() {
		return finalCostcomment;
	}

	public void setFinalCostcomment(String finalCostcomment) {
		this.finalCostcomment = finalCostcomment;
	}

	

}
