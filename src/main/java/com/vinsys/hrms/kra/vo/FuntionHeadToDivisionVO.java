package com.vinsys.hrms.kra.vo;

public class FuntionHeadToDivisionVO {

	private Long id;

	private Long functionHeadId;

	private Long divisionId;
	
	private String divisionName;

	private String isActive;
	
	private String funtionHeadName;

	public Long getFunctionHeadId() {
		return functionHeadId;
	}

	public void setFunctionHeadId(Long functionHeadId) {
		this.functionHeadId = functionHeadId;
	}

	public String getFuntionHeadName() {
		return funtionHeadName;
	}

	public void setFuntionHeadName(String funtionHeadName) {
		this.funtionHeadName = funtionHeadName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
}
