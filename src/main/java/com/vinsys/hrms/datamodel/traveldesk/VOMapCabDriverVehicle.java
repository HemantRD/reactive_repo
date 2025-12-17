package com.vinsys.hrms.datamodel.traveldesk;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOMapCabDriverVehicle extends VOAuditBase {

	private long id;
	private long cabRequestPassenger;
	private VOMasterDriver driver;
	private VOMasterVehicle vehicle;
	// return driver and vehicle fields are used for getting those values from
	// request
	// it is not mapped with entity
	private VOMasterDriver returnDriver;
	private VOMasterVehicle returnVehicle;
	private String tripWay;
	private boolean isRecurring;
	private long mapCabDriverVehOneWayId;
	private long mapCabDriverVehReturnId;
	private boolean isDropOnly;
	private String doneBy;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCabRequestPassenger() {
		return cabRequestPassenger;
	}

	public void setCabRequestPassenger(long cabRequestPassenger) {
		this.cabRequestPassenger = cabRequestPassenger;
	}

	public VOMasterDriver getDriver() {
		return driver;
	}

	public void setDriver(VOMasterDriver driver) {
		this.driver = driver;
	}

	public VOMasterVehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(VOMasterVehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getTripWay() {
		return tripWay;
	}

	public void setTripWay(String tripWay) {
		this.tripWay = tripWay;
	}

	public boolean isRecurring() {
		return isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public VOMasterDriver getReturnDriver() {
		return returnDriver;
	}

	public void setReturnDriver(VOMasterDriver returnDriver) {
		this.returnDriver = returnDriver;
	}

	public VOMasterVehicle getReturnVehicle() {
		return returnVehicle;
	}

	public void setReturnVehicle(VOMasterVehicle returnVehicle) {
		this.returnVehicle = returnVehicle;
	}

	public long getMapCabDriverVehOneWayId() {
		return mapCabDriverVehOneWayId;
	}

	public void setMapCabDriverVehOneWayId(long mapCabDriverVehOneWayId) {
		this.mapCabDriverVehOneWayId = mapCabDriverVehOneWayId;
	}

	public long getMapCabDriverVehReturnId() {
		return mapCabDriverVehReturnId;
	}

	public void setMapCabDriverVehReturnId(long mapCabDriverVehReturnId) {
		this.mapCabDriverVehReturnId = mapCabDriverVehReturnId;
	}

	public boolean isDropOnly() {
		return isDropOnly;
	}

	public void setDropOnly(boolean isDropOnly) {
		this.isDropOnly = isDropOnly;
	}

	public String getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}

}
