package com.vinsys.hrms.kra.vo;

public class KraCycleTypeVO {
	
	private Long id;
	
	private String cycleTypeName;
	
	private String cycleTypeDescription;
	
	private String isActive;
	
	private Long orgId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCycleTypeName() {
		return cycleTypeName;
	}

	public void setCycleTypeName(String cycleTypeName) {
		this.cycleTypeName = cycleTypeName;
	}

	public String getCycleTypeDescription() {
		return cycleTypeDescription;
	}

	public void setCycleTypeDescription(String cycleTypeDescription) {
		this.cycleTypeDescription = cycleTypeDescription;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
