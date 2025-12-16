package com.vinsys.hrms.traveldesk.vo;

import java.util.List;

public class BPMDetailsVO {
	private long id;
	private String businessUnit;
	private String clientName;
	public String bdName;
	private String bpmStatus;
	
	List<LineItemDetailsResponseVO> lineItems;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getBdName() {
		return bdName;
	}

	public void setBdName(String bdName) {
		this.bdName = bdName;
	}

	public String getBpmStatus() {
		return bpmStatus;
	}

	public void setBpmStatus(String bpmStatus) {
		this.bpmStatus = bpmStatus;
	}

	public List<LineItemDetailsResponseVO> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItemDetailsResponseVO> lineItems) {
		this.lineItems = lineItems;
	}

}
