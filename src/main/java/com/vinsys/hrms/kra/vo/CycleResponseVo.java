package com.vinsys.hrms.kra.vo;

public class CycleResponseVo {
	private Long id;
	private String cycle;
	private Long cycleType;
	private String status;
	private String isDateEntered;
	private String islocked;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsDateEntered() {
		return isDateEntered;
	}
	public void setIsDateEntered(String isDateEntered) {
		this.isDateEntered = isDateEntered;
	}
	public String getIslocked() {
		return islocked;
	}
	public void setIslocked(String islocked) {
		this.islocked = islocked;
	}
	public Long getCycleType() {
		return cycleType;
	}
	public void setCycleType(Long cycleType) {
		this.cycleType = cycleType;
	}
	
	
}
