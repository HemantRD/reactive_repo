package com.vinsys.hrms.datamodel.traveldesk;

import com.vinsys.hrms.datamodel.VOEmployee;

public class VOTraveldeskApprover {

	private long id;
	private VOEmployee employee;
	private String approverType;
	private String approverTypeDesc;

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

	public String getApproverType() {
		return approverType;
	}

	public void setApproverType(String approverType) {
		this.approverType = approverType;
	}

	public String getApproverTypeDesc() {
		return approverTypeDesc;
	}

	public void setApproverTypeDesc(String approverTypeDesc) {
		this.approverTypeDesc = approverTypeDesc;
	}

}
