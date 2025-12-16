package com.vinsys.hrms.master.vo;

public class ModeofSeparationReasonVO {
	private Long id;
	private String reasonName;
	private long modeOfSeparation;
	private String resignActionType;
	private long organizationId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReasonName() {
		return reasonName;
	}
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}
	public long getModeOfSeparation() {
		return modeOfSeparation;
	}
	public void setModeOfSeparation(long modeOfSeparation) {
		this.modeOfSeparation = modeOfSeparation;
	}
	public String getResignActionType() {
		return resignActionType;
	}
	public void setResignActionType(String resignActionType) {
		this.resignActionType = resignActionType;
	}
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	
	
	

}
