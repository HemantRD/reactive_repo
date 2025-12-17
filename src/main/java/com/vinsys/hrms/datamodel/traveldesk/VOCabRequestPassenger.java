package com.vinsys.hrms.datamodel.traveldesk;

import java.util.List;

public class VOCabRequestPassenger extends VOTravellerDetails {

	private long id;
	private VOCabRequest cabRequest;
	private String pickupDate;
	private String pickupTime;
	private String pickupAt;
	private String returnDate;
	private String returnTime;
	private String dropLocation;
	private boolean isDropOnly;
	private boolean isRecurring;
	private boolean isSelfManaged;
	private boolean chargeableToClient;
	private String cabRequestPasssengerStatus;
	
	private String driverpickupTime;
	private String driverreturnPickupTime;
	public String driverComment;

	
	private List<VOCabRecurringRequest> cabRecurringRequests;
	// below list not mapped with entity. but useful while retriving data
	private List<VOMapCabDriverVehicle> mapCabDriverVehicles;

	private boolean tdSelfManaged;
	private String tdSelfManagedComment;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOCabRequest getCabRequest() {
		return cabRequest;
	}

	public void setCabRequest(VOCabRequest cabRequest) {
		this.cabRequest = cabRequest;
	}

	public String getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getPickupAt() {
		return pickupAt;
	}

	public void setPickupAt(String pickupAt) {
		this.pickupAt = pickupAt;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	public String getDropLocation() {
		return dropLocation;
	}

	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}

	public boolean isDropOnly() {
		return isDropOnly;
	}

	public void setDropOnly(boolean isDropOnly) {
		this.isDropOnly = isDropOnly;
	}

	public boolean isRecurring() {
		return isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public boolean isSelfManaged() {
		return isSelfManaged;
	}

	public void setSelfManaged(boolean isSelfManaged) {
		this.isSelfManaged = isSelfManaged;
	}

	public boolean isChargeableToClient() {
		return chargeableToClient;
	}

	public void setChargeableToClient(boolean chargeableToClient) {
		this.chargeableToClient = chargeableToClient;
	}

	public String getCabRequestPasssengerStatus() {
		return cabRequestPasssengerStatus;
	}

	public void setCabRequestPasssengerStatus(String cabRequestPasssengerStatus) {
		this.cabRequestPasssengerStatus = cabRequestPasssengerStatus;
	}

	public List<VOCabRecurringRequest> getCabRecurringRequests() {
		return cabRecurringRequests;
	}

	public void setCabRecurringRequests(List<VOCabRecurringRequest> cabRecurringRequests) {
		this.cabRecurringRequests = cabRecurringRequests;
	}

	public List<VOMapCabDriverVehicle> getMapCabDriverVehicles() {
		return mapCabDriverVehicles;
	}

	public void setMapCabDriverVehicles(List<VOMapCabDriverVehicle> mapCabDriverVehicles) {
		this.mapCabDriverVehicles = mapCabDriverVehicles;
	}

	public boolean isTdSelfManaged() {
		return tdSelfManaged;
	}

	public void setTdSelfManaged(boolean tdSelfManaged) {
		this.tdSelfManaged = tdSelfManaged;
	}

	public String getTdSelfManagedComment() {
		return tdSelfManagedComment;
	}

	public void setTdSelfManagedComment(String tdSelfManagedComment) {
		this.tdSelfManagedComment = tdSelfManagedComment;
	}

	public String getDriverpickupTime() {
		return driverpickupTime;
	}

	public void setDriverpickupTime(String driverpickupTime) {
		this.driverpickupTime = driverpickupTime;
	}

	public String getDriverreturnPickupTime() {
		return driverreturnPickupTime;
	}

	public void setDriverreturnPickupTime(String driverreturnPickupTime) {
		this.driverreturnPickupTime = driverreturnPickupTime;
	}

	public String getDriverComment() {
		return driverComment;
	}

	public void setDriverComment(String driverComment) {
		this.driverComment = driverComment;
	}
	
	
	
	
	

}
