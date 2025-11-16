package com.vinsys.hrms.kra.vo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PmsDashboardRequestVo {

	
	@NotNull(message = "RoleName Must Not be NULL")
	 String roleName;
	 String year;
	 String cycleName;
	 Long cycleId;
	 Long employeeId;


	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getCycleId() {
		return cycleId;
	}

	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}

}
