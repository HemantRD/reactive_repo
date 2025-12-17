package com.vinsys.hrms.master.vo;

public class MasterMaritialStatusVo {

	private Long id;
	private String maritalStatus;
	private String maritalStatusDescription;

	public MasterMaritialStatusVo() {
	}

	public MasterMaritialStatusVo(Long id, String maritalStatus, String maritalStatusDescription) {
		super();
		this.id = id;
		this.maritalStatus = maritalStatus;
		this.maritalStatusDescription = maritalStatusDescription;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getMaritalStatusDescription() {
		return maritalStatusDescription;
	}

	public void setMaritalStatusDescription(String maritalStatusDescription) {
		this.maritalStatusDescription = maritalStatusDescription;
	}

}
