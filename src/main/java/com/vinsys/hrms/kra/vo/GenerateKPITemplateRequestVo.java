package com.vinsys.hrms.kra.vo;

import java.util.List;

public class GenerateKPITemplateRequestVo {

	private String year;
	private List<Long> employeeIds;
	private List<Long> cycleIds;

	public List<Long> getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(List<Long> employeeIds) {
		this.employeeIds = employeeIds;
	}

	public List<Long> getCycleIds() {
		return cycleIds;
	}

	public void setCycleIds(List<Long> cycleIds) {
		this.cycleIds = cycleIds;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}
