package com.vinsys.hrms.kra.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vinsys.hrms.kra.entity.KraCycleType;

public class KraCycleResponseVo {

	private Long cycleId;
	private String cycleName;
	private Long cycleType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String endDate;
	private String description;
	private String isActive;
	private Long yearId;
	private String year;
	private Long cycleTypeId;
	
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	public String getCycleName() {
		return cycleName;
	}
	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public Long getYearId() {
		return yearId;
	}
	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Long getCycleTypeId() {
		return cycleTypeId;
	}
	public void setCycleTypeId(Long cycleTypeId) {
		this.cycleTypeId = cycleTypeId;
	}
	public Long getCycleType() {
		return cycleType;
	}
	public void setCycleType(Long cycleType) {
		this.cycleType = cycleType;
	}

}
