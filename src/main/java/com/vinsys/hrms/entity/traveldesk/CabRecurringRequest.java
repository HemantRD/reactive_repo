package com.vinsys.hrms.entity.traveldesk;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_trn_cab_recurring_request")
public class CabRecurringRequest extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_cab_recurring_req", sequenceName = "seq_cab_recurring_req", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cab_recurring_req")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "cab_passenger_request_id")
	private CabRequestPassenger cabRequestPassenger;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "pickup_date")
	private Date pickupDate;
	@Column(name = "pickup_time")
	private String pickupTime;
	@Column(name = "pickup_at")
	private String pickupAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "return_date")
	private Date returnDate;
	@Column(name = "return_time")
	private String returnTime;
	@Column(name = "drop_location")
	private String dropLocation;
	@Column(name = "cab_request_recurring_status")
	private String cabRequestRecurringStatus;
	@Column(name = "chargeable_to_client")
	private boolean chargeableToClient;
	
	
	@Column(name = "return_trip_status")
	private String returnTripStatus;
	@Column(name = "oneWay_trip_status")
	private String oneWayTripStatus;
	
	
	@Column(name = "td_self_managed")
	private boolean tdSelfManaged;
	
	
	@Column(name = "comment")
	private String tdSelfManagedComment;
	
	
	@Column(name = "driver_pickup_time")
	private String driverPickupTime;
	
	@Column(name = "return_driver_pickup_time")
	private String returnDriverPickupTime;
	
	@Column(name = "driver_comment")
	private String driverComment;
	
	public boolean isChargeableToClient() {
		return chargeableToClient;
	}

	public void setChargeableToClient(boolean chargeableToClient) {
		this.chargeableToClient = chargeableToClient;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CabRequestPassenger getCabRequestPassenger() {
		return cabRequestPassenger;
	}

	public void setCabRequestPassenger(CabRequestPassenger cabRequestPassenger) {
		this.cabRequestPassenger = cabRequestPassenger;
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
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

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getReturnTripStatus() {
		return returnTripStatus;
	}

	public void setReturnTripStatus(String returnTripStatus) {
		this.returnTripStatus = returnTripStatus;
	}

	public String getOneWayTripStatus() {
		return oneWayTripStatus;
	}

	public void setOneWayTripStatus(String oneWayTripStatus) {
		this.oneWayTripStatus = oneWayTripStatus;
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

	public String getDriverPickupTime() {
		return driverPickupTime;
	}

	public void setDriverPickupTime(String driverPickupTime) {
		this.driverPickupTime = driverPickupTime;
	}

	public String getReturnDriverPickupTime() {
		return returnDriverPickupTime;
	}

	public void setReturnDriverPickupTime(String returnDriverPickupTime) {
		this.returnDriverPickupTime = returnDriverPickupTime;
	}

	public String getDriverComment() {
		return driverComment;
	}

	public void setDriverComment(String driverComment) {
		this.driverComment = driverComment;
	}

	
	
	
	

}
