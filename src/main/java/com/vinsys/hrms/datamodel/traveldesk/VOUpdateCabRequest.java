package com.vinsys.hrms.datamodel.traveldesk;

import java.util.List;

import com.vinsys.hrms.entity.AuditBase;

public class VOUpdateCabRequest extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long travelRequestId;
	private long cabRequestId;
	private boolean isRecurring;
	private List<VOMapCabDriverVehicle> mapCabDriverVehList;
    private boolean selfManaged;
	private String selfManagedComment;
	private String driverpickupTime;
	private String driverreturnPickupTime;
	private String driverComment;
	
	
	public long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public long getCabRequestId() {
		return cabRequestId;
	}

	public void setCabRequestId(long cabRequestId) {
		this.cabRequestId = cabRequestId;
	}

	public boolean isRecurring() {
		return isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<VOMapCabDriverVehicle> getMapCabDriverVehList() {
		return mapCabDriverVehList;
	}

	public void setMapCabDriverVehList(List<VOMapCabDriverVehicle> mapCabDriverVehList) {
		this.mapCabDriverVehList = mapCabDriverVehList;
	}

	public boolean isSelfManaged() {
		return selfManaged;
	}

	public void setSelfManaged(boolean selfManaged) {
		this.selfManaged = selfManaged;
	}

	public String getSelfManagedComment() {
		return selfManagedComment;
	}

	public void setSelfManagedComment(String selfManagedComment) {
		this.selfManagedComment = selfManagedComment;
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
