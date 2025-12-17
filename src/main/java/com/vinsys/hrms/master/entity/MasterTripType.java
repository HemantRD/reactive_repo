package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_mst_trip_type")
public class MasterTripType extends AuditBase {
	
	@Id
	@Column(name = "id")
	private Long Id;
	
	@Column(name = "trip_type")
	private String tripType;
	
	@Column(name = "description")
	private String description;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
