package com.vinsys.hrms.datamodel.attendance;

import com.vinsys.hrms.datamodel.VOEmployee;

public class VOMobileSwapDetailReq {

	private long id;
	private VOEmployee employee;
	private long empACN;
	private long orgId;
	private String imei;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}

	public long getEmpACN() {
		return empACN;
	}

	public void setEmpACN(long empACN) {
		this.empACN = empACN;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

}
