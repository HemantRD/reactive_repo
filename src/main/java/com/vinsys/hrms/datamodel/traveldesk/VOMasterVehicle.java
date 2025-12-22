package com.vinsys.hrms.datamodel.traveldesk;

import com.vinsys.hrms.datamodel.VOAuditBase;
import com.vinsys.hrms.datamodel.VOMasterDivision;
import com.vinsys.hrms.datamodel.VOOrganization;

public class VOMasterVehicle extends VOAuditBase{

	private long id;
	private String vehicleName;
	private String vehicleDescription;
	private VOOrganization organization;
	private VOMasterDivision division;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getVehicleDescription() {
		return vehicleDescription;
	}

	public void setVehicleDescription(String vehicleDescription) {
		this.vehicleDescription = vehicleDescription;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public VOMasterDivision getDivision() {
		return division;
	}

	public void setDivision(VOMasterDivision division) {
		this.division = division;
	}

}
