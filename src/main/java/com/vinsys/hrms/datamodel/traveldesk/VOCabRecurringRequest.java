package com.vinsys.hrms.datamodel.traveldesk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.vinsys.hrms.datamodel.VOAuditBase;
import com.vinsys.hrms.util.IHRMSConstants;

public class VOCabRecurringRequest   extends VOAuditBase implements Comparable<VOCabRecurringRequest> {

	private long id;
	private VOCabRequestPassenger cabRequestPassenger;
	private String pickupDate;
	private String pickupTime;
	private String pickupAt;
	private String returnDate;
	private String returnTime;
	private String dropLocation;
	private String cabRequestRecurringStatus;
	private boolean chargeableToClient;
	// below list not mapped with entity. but useful while retriving data
	private List<VOMapCabDriverVehicle> mapCabDriverVehicles;
	
	
	private boolean tdSelfManaged;
	private String tdSelfManagedComment;
	
	private String driverpickupTime;
	private String driverreturnPickupTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOCabRequestPassenger getCabRequestPassenger() {
		return cabRequestPassenger;
	}

	public void setCabRequestPassenger(VOCabRequestPassenger cabRequestPassenger) {
		this.cabRequestPassenger = cabRequestPassenger;
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

	public String getCabRequestRecurringStatus() {
		return cabRequestRecurringStatus;
	}

	public void setCabRequestRecurringStatus(String cabRequestRecurringStatus) {
		this.cabRequestRecurringStatus = cabRequestRecurringStatus;
	}

	public boolean isChargeableToClient() {
		return chargeableToClient;
	}

	public void setChargeableToClient(boolean chargeableToClient) {
		this.chargeableToClient = chargeableToClient;
	}

	public List<VOMapCabDriverVehicle> getMapCabDriverVehicles() {
		return mapCabDriverVehicles;
	}

	public void setMapCabDriverVehicles(List<VOMapCabDriverVehicle> mapCabDriverVehicles) {
		this.mapCabDriverVehicles = mapCabDriverVehicles;
	}

	
	@Override
    public int compareTo(VOCabRecurringRequest recurringRequest) {
        Date pickupDateMain = null;
        Date pickupDateCmr = null;
		try {
			pickupDateMain = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT).parse(this.pickupDate);
			pickupDateCmr = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT).parse(recurringRequest.pickupDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return pickupDateCmr.compareTo(pickupDateMain);
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
	
	
	
	

}
