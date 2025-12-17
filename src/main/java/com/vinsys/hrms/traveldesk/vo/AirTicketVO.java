package com.vinsys.hrms.traveldesk.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class AirTicketVO {

	private Long id;
	@Schema(required = true)
	private String airType;
	@Schema(required = true)
	private Long noOfTravellers;
	private String roundTrip;
	private String chargableClient;
	@Schema(required = true)
	private String fromLocation;
	@Schema(required = true)
	private String toLocation;
	@Schema(required = true)
	private String dateOfjourney;
	private String preferedTime;
	private String returnDateOfjourney;
	private String returnPreferedTime;
	private Long travelRequestId;
	private List<TravellerDetailsVO> travellerDetails;
	private Float approximateCost;
	private String approximateComment;
//	private TravelApproverResponseVO approver;
	private Float finalCost;
	private String finalCostcomment;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAirType() {
		return airType;
	}
	public void setAirType(String airType) {
		this.airType = airType;
	}
	public Long getNoOfTravellers() {
		return noOfTravellers;
	}
	public void setNoOfTravellers(Long noOfTravellers) {
		this.noOfTravellers = noOfTravellers;
	}
	public String getRoundTrip() {
		return roundTrip;
	}
	public void setRoundTrip(String roundTrip) {
		this.roundTrip = roundTrip;
	}
	public String getChargableClient() {
		return chargableClient;
	}
	public void setChargableClient(String chargableClient) {
		this.chargableClient = chargableClient;
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
	public String getDateOfjourney() {
		return dateOfjourney;
	}
	public void setDateOfjourney(String dateOfjourney) {
		this.dateOfjourney = dateOfjourney;
	}
	public String getPreferedTime() {
		return preferedTime;
	}
	public void setPreferedTime(String preferedTime) {
		this.preferedTime = preferedTime;
	}
	public String getReturnDateOfjourney() {
		return returnDateOfjourney;
	}
	public void setReturnDateOfjourney(String returnDateOfjourney) {
		this.returnDateOfjourney = returnDateOfjourney;
	}
	public String getReturnPreferedTime() {
		return returnPreferedTime;
	}
	public void setReturnPreferedTime(String returnPreferedTime) {
		this.returnPreferedTime = returnPreferedTime;
	}
	public Long getTravelRequestId() {
		return travelRequestId;
	}
	public void setTravelRequestId(Long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}
	public List<TravellerDetailsVO> getTravellerDetails() {
		return travellerDetails;
	}
	public void setTravellerDetails(List<TravellerDetailsVO> travellerDetails) {
		this.travellerDetails = travellerDetails;
	}
	public Float getApproximateCost() {
		return approximateCost;
	}
	public void setApproximateCost(Float approximateCost) {
		this.approximateCost = approximateCost;
	}
	
	public String getApproximateComment() {
		return approximateComment;
	}
	public void setApproximateComment(String approximateComment) {
		this.approximateComment = approximateComment;
	}
//	public TravelApproverResponseVO getApprover() {
//		return approver;
//	}
//	public void setApprover(TravelApproverResponseVO approver) {
//		this.approver = approver;
//	}
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
