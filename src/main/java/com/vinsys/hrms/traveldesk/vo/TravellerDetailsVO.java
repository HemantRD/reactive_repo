package com.vinsys.hrms.traveldesk.vo;

import com.vinsys.hrms.master.vo.MasterTravellerTypeVO;

import io.swagger.v3.oas.annotations.media.Schema;

public class TravellerDetailsVO {

	private Long id;
	@Schema(required = true)
//	private Long travellerTypeId;
	private MasterTravellerTypeVO travellerType;
	private String name;
	private String emailId;
	private Long contactNo;
	private String dateOfBirth;
	private String pickUpLocation;
	private String pickUpTime;
	private String dropLocation;
	private String passportNo;
	private String passportDateOfExpiry;
	private String visaNo;
	private String visaCountry;
	private String visaType;
	private String visaDateOfExpiry;
	private Boolean isPrimaryTreveller;
	private Long ticketId;
	private Long cabId;
	private Long AccommodationId;
	private boolean isManagementEmployee;
	
	
	
	
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public Long getCabId() {
		return cabId;
	}
	public void setCabId(Long cabId) {
		this.cabId = cabId;
	}
	public Long getAccommodationId() {
		return AccommodationId;
	}
	public void setAccommodationId(Long accommodationId) {
		AccommodationId = accommodationId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
//	public Long getTravellerTypeId() {
//		return travellerTypeId;
//	}
//	public void setTravellerTypeId(Long travellerTypeId) {
//		this.travellerTypeId = travellerTypeId;
//	}
	public Long getContactNo() {
		return contactNo;
	}
	public void setContactNo(Long contactNo) {
		this.contactNo = contactNo;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getPickUpLocation() {
		return pickUpLocation;
	}
	public void setPickUpLocation(String pickUpLocation) {
		this.pickUpLocation = pickUpLocation;
	}
	public String getPickUpTime() {
		return pickUpTime;
	}
	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime = pickUpTime;
	}
	public String getDropLocation() {
		return dropLocation;
	}
	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}
	public String getPassportNo() {
		return passportNo;
	}
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}
	public String getPassportDateOfExpiry() {
		return passportDateOfExpiry;
	}
	public void setPassportDateOfExpiry(String passportDateOfExpiry) {
		this.passportDateOfExpiry = passportDateOfExpiry;
	}
	public String getVisaNo() {
		return visaNo;
	}
	public void setVisaNo(String visaNo) {
		this.visaNo = visaNo;
	}
	public String getVisaCountry() {
		return visaCountry;
	}
	public void setVisaCountry(String visaCountry) {
		this.visaCountry = visaCountry;
	}
	public String getVisaType() {
		return visaType;
	}
	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}
	public String getVisaDateOfExpiry() {
		return visaDateOfExpiry;
	}
	public void setVisaDateOfExpiry(String visaDateOfExpiry) {
		this.visaDateOfExpiry = visaDateOfExpiry;
	}

	public Boolean getIsPrimaryTreveller() {
		return isPrimaryTreveller;
	}
	public void setIsPrimaryTreveller(Boolean isPrimaryTreveller) {
		this.isPrimaryTreveller = isPrimaryTreveller;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public MasterTravellerTypeVO getTravellerType() {
		return travellerType;
	}
	public void setTravellerType(MasterTravellerTypeVO travellerType) {
		this.travellerType = travellerType;
	}
	public boolean isManagementEmployee() {
		return isManagementEmployee;
	}
	public void setManagementEmployee(boolean isManagementEmployee) {
		this.isManagementEmployee = isManagementEmployee;
	}
	
	
	
	
}
