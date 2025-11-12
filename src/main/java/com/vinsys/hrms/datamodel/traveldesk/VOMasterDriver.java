package com.vinsys.hrms.datamodel.traveldesk;

import com.vinsys.hrms.datamodel.VOAuditBase;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOMasterDivision;
import com.vinsys.hrms.datamodel.VOOrganization;

public class VOMasterDriver extends VOAuditBase{
	
	private long id;
	private String driverName;
	private String driverDescription;
	private VOOrganization organization;
	private VOMasterDivision division;
	private VOEmployee employee;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverDescription() {
		return driverDescription;
	}
	public void setDriverDescription(String driverDescription) {
		this.driverDescription = driverDescription;
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
	public VOEmployee getEmployee() {
		return employee;
	}
	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}
	
}
