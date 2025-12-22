package com.vinsys.hrms.reimbursement.vo;

import com.vinsys.hrms.master.vo.ClaimTypeVO;
import com.vinsys.hrms.master.vo.ReimbursementTravelTypeVO;
import com.vinsys.hrms.master.vo.StayTypeVO;

import io.swagger.v3.oas.annotations.media.Schema;

public class AddClaimResponseVO {

	
	private Long id;
	@Schema(required = true)
	private Long requestId;
	@Schema(required = true)
	private ClaimTypeVO claimType;
	@Schema(required = true)
	private String expenseDate;
	@Schema(required = true)
	private Float expenseAmount;
	@Schema(required = true)
	private String comment;
	@Schema(required = true)
	private String dailyAllowance;
	@Schema(required = true)
	private ReimbursementTravelTypeVO travelType;
	@Schema(required = true)
	private String travelFromLocation;
	@Schema(required = true)
	private String travelToLocation;
	@Schema(required = true)
	private String travelFromDate;
	@Schema(required = true)
	private String travelToDate;
	@Schema(required = true)
	private long noOfDays;
	private String stayFromDate;
	@Schema(required = true)
	private String stayToDate;
	@Schema(required = true)
	private String stayLocation;
	@Schema(required = true)
	private StayTypeVO stayType;
	@Schema(required = true)
	private String hotelName;
	@Schema(required = true)
	private String roomNo;
	@Schema(required = true)
	private String location;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getExpenseDate() {
		return expenseDate;
	}
	public void setExpenseDate(String expenseDate) {
		this.expenseDate = expenseDate;
	}
	public Float getExpenseAmount() {
		return expenseAmount;
	}
	public void setExpenseAmount(Float expenseAmount) {
		this.expenseAmount = expenseAmount;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDailyAllowance() {
		return dailyAllowance;
	}
	public void setDailyAllowance(String dailyAllowance) {
		this.dailyAllowance = dailyAllowance;
	}
	
	public String getTravelFromLocation() {
		return travelFromLocation;
	}
	public void setTravelFromLocation(String travelFromLocation) {
		this.travelFromLocation = travelFromLocation;
	}
	public String getTravelToLocation() {
		return travelToLocation;
	}
	public void setTravelToLocation(String travelToLocation) {
		this.travelToLocation = travelToLocation;
	}
	public String getTravelFromDate() {
		return travelFromDate;
	}
	public void setTravelFromDate(String travelFromDate) {
		this.travelFromDate = travelFromDate;
	}
	public String getTravelToDate() {
		return travelToDate;
	}
	public void setTravelToDate(String travelToDate) {
		this.travelToDate = travelToDate;
	}
	public long getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(long noOfDays) {
		this.noOfDays = noOfDays;
	}
	public String getStayLocation() {
		return stayLocation;
	}
	public void setStayLocation(String stayLocation) {
		this.stayLocation = stayLocation;
	}
	
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public ClaimTypeVO getClaimType() {
		return claimType;
	}
	public void setClaimType(ClaimTypeVO claimType) {
		this.claimType = claimType;
	}
	public ReimbursementTravelTypeVO getTravelType() {
		return travelType;
	}
	public void setTravelType(ReimbursementTravelTypeVO travelType) {
		this.travelType = travelType;
	}
	public StayTypeVO getStayType() {
		return stayType;
	}
	public void setStayType(StayTypeVO stayType) {
		this.stayType = stayType;
	}
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public String getStayFromDate() {
		return stayFromDate;
	}
	public void setStayFromDate(String stayFromDate) {
		this.stayFromDate = stayFromDate;
	}
	public String getStayToDate() {
		return stayToDate;
	}
	public void setStayToDate(String stayToDate) {
		this.stayToDate = stayToDate;
	}
	
	
	
	
	
	
}
