package com.vinsys.hrms.traveldesk.vo;

import java.util.List;

public class GetBpmDetailsResponeVO {
	private Long id;
	private String businessUnit;
	private String clientName;
	private String bdName;
	private String bpmStatus;
	private List<LineItemDetailsResponseVO> data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public List<LineItemDetailsResponseVO> getData() {
		return data;
	}

	public void setData(List<LineItemDetailsResponseVO> data) {
		this.data = data;
	}

	public String getBpmStatus() {
		return bpmStatus;
	}

	public void setBpmStatus(String bpmStatus) {
		this.bpmStatus = bpmStatus;
	}

	

}
