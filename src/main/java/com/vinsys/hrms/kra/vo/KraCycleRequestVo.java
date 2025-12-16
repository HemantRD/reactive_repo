package com.vinsys.hrms.kra.vo;

public class KraCycleRequestVo {

	private Long cycleId;

	private Long yearId;

	private String cycleName;

	private String description;

	private String startDate;

	private String endDate;
	
	private Long cycleTypeId;
	

	public String getCycleName() {
		return cycleName;
	}

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Long getCycleId() {
		return cycleId;
	}

	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}

	public Long getCycleTypeId() {
		return cycleTypeId;
	}

	public void setCycleTypeId(Long cycleTypeId) {
		this.cycleTypeId = cycleTypeId;
	}

}
