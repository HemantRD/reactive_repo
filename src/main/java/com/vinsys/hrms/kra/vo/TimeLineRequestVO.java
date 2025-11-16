package com.vinsys.hrms.kra.vo;

public class TimeLineRequestVO {

	private Long id;

	private String label;

	private String date;

	private String color;

	private Long kraCycleId;

	private Long yearId;
	
	private String kraCycleName;

	private String year;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getKraCycleId() {
		return kraCycleId;
	}

	public void setKraCycleId(Long kraCycleId) {
		this.kraCycleId = kraCycleId;
	}

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKraCycleName() {
		return kraCycleName;
	}

	public void setKraCycleName(String kraCycleName) {
		this.kraCycleName = kraCycleName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}
