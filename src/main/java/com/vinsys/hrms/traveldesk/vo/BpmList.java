package com.vinsys.hrms.traveldesk.vo;

public class BpmList {
	private Long bpmNo;
	private String bdName;
	private String businessUnit;
	private String clientName;
	private String bpmStatus;
	public Long getBpmNo() {
		return bpmNo;
	}
	public void setBpmNo(Long bpmNo) {
		this.bpmNo = bpmNo;
	}
	public String getBdName() {
		return bdName;
	}
	public void setBdName(String bdName) {
		this.bdName = bdName;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getBpmStatus() {
		return bpmStatus;
	}
	public void setBpmStatus(String bpmStatus) {
		this.bpmStatus = bpmStatus;
	}
}
