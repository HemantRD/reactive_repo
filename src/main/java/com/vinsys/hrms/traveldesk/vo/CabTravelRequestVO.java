package com.vinsys.hrms.traveldesk.vo;

import java.util.List;

import com.vinsys.hrms.master.vo.MasterDriverVO;
import com.vinsys.hrms.master.vo.MasterTravelTypeVO;
import com.vinsys.hrms.master.vo.MasterTravellerTypeVO;
import com.vinsys.hrms.master.vo.MasterVehicleVO;
import com.vinsys.hrms.master.vo.TripTypeVO;

import io.swagger.v3.oas.annotations.media.Schema;

public class CabTravelRequestVO {

	private Long id;
	@Schema(required = true)
//	private Long travelType;
	private MasterTravelTypeVO travelType;
	@Schema(required = true)
//	private Long tripType;
	private TripTypeVO tripType;
	@Schema(required = true)
	private long noOfTravellers;
	private String chargableClient;
	@Schema(required = true)
	private String fromLocation;
	@Schema(required = true)
	private String toLocation;
	@Schema(required = true)
	private String dateOfjourney;
	@Schema(required = true)
	private String preferedTime;
	private String returnDateOfjourney;
	private String returnPreferedTime;
	@Schema(required = true)
	private Long travelRequestId;
	private List<TravellerDetailsVO> travellerDetails;
	
	private MasterTravellerTypeVO cabType;
	private MasterDriverVO driverName;
	private MasterVehicleVO vehicleName;
	private String approximateDistance;
//	private TravelApproverResponseVO approver;
	private Float approximateCost;
	private String approximateComment;
	private Float finalCost;
	private String finalCostcomment;
	
	
//	public Long getTravelType() {
//		return travelType;
//	}
//	public void setTravelType(Long travelType) {
//		this.travelType = travelType;
//	}
	public List<TravellerDetailsVO> getTravellerDetails() {
		return travellerDetails;
	}

	public void setTravellerDetails(List<TravellerDetailsVO> travellerDetails) {
		this.travellerDetails = travellerDetails;
	}

//	public Long getTripType() {
//		return tripType;
//	}
//
//	public void setTripType(Long tripType) {
//		this.tripType = tripType;
//	}

	public long getNoOfTravellers() {
		return noOfTravellers;
	}

	public void setNoOfTravellers(long noOfTravellers) {
		this.noOfTravellers = noOfTravellers;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MasterTravelTypeVO getTravelType() {
		return travelType;
	}

	public void setTravelType(MasterTravelTypeVO travelType) {
		this.travelType = travelType;
	}

	public TripTypeVO getTripType() {
		return tripType;
	}

	public void setTripTypeVO(TripTypeVO tripType) {
		this.tripType = tripType;
	}

	public void setTripType(TripTypeVO tripType) {
		this.tripType = tripType;
	}

	

	public MasterTravellerTypeVO getCabType() {
		return cabType;
	}

	public void setCabType(MasterTravellerTypeVO cabType) {
		this.cabType = cabType;
	}

	public MasterDriverVO getDriverName() {
		return driverName;
	}

	public void setDriverName(MasterDriverVO driverName) {
		this.driverName = driverName;
	}

	public MasterVehicleVO getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(MasterVehicleVO vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getApproximateDistance() {
		return approximateDistance;
	}

	public void setApproximateDistance(String approximateDistance) {
		this.approximateDistance = approximateDistance;
	}

//	public TravelApproverResponseVO getApprover() {
//		return approver;
//	}
//
//	public void setApprover(TravelApproverResponseVO approver) {
//		this.approver = approver;
//	}

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
