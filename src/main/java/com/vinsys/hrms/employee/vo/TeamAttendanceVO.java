package com.vinsys.hrms.employee.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public class TeamAttendanceVO {
	@Schema(required = true)
    private long empId;
	private String fromDate;
	private String toDate;
	public long getEmpId() {
		return empId;
	}
	public void setEmpId(long empId) {
		this.empId = empId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	
	
}
