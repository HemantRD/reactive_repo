package com.vinsys.hrms.datamodel.attendance;

import com.vinsys.hrms.datamodel.VOEmployee;

public class VOEmployeeImeiDetailRequest {

	private VOEmployee employee;
	
	private String imei;

	public VOEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
	
}
