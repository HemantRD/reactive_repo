package com.vinsys.hrms.kra.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DashboardCountResponse {

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<DashboardResponseVo> currentYear;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<DashboardResponseVo> previousYear;
	private String message;


	public List<DashboardResponseVo> getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(List<DashboardResponseVo> currentYear) {
		this.currentYear = currentYear;
	}

	public List<DashboardResponseVo> getPreviousYear() {
		return previousYear;
	}

	public void setPreviousYear(List<DashboardResponseVo> previousYear) {
		this.previousYear = previousYear;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
