package com.vinsys.hrms.entity.traveldesk;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_map_cab_driver_vehicle")
public class MapCabDriverVehicle extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_map_cab_driver_vehicle", sequenceName = "seq_map_cab_driver_vehicle", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_cab_driver_vehicle")
	private Long id;
	@Column(name = "cab_passenger_request_id")
	private long cabRequestPassengerId;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "driver_id")
	private MasterDriver driverId;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "vehicle_id")
	private MasterVehicle vehicleId;
	@Column(name = "trip_way")
	private String tripWay;
	@Column(name = "is_recurring")
	private boolean isRecurring;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCabRequestPassengerId() {
		return cabRequestPassengerId;
	}

	public void setCabRequestPassengerId(long cabRequestPassengerId) {
		this.cabRequestPassengerId = cabRequestPassengerId;
	}

	public MasterDriver getDriverId() {
		return driverId;
	}

	public void setDriverId(MasterDriver driverId) {
		this.driverId = driverId;
	}

	public MasterVehicle getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(MasterVehicle vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getTripWay() {
		return tripWay;
	}

	public void setTripWay(String tripWay) {
		this.tripWay = tripWay;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isRecurring() {
		return isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	
}
