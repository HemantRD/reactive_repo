package com.vinsys.hrms.audit.vo;

import java.sql.Timestamp;

public class AuditLogVO {

	private Long id;

	private Long actionBy;
	
	private String actionByName;

	private Long actionOn;
	
	private String actionOnName;

	private String actionName;

	private Timestamp requestedTime;
	
	private String requestUrl;
	
	private String statusCode;

	private String status;

	private String responseMessage;
	
	private String isActive;
	
	private String createdDate;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActionBy() {
		return actionBy;
	}

	public void setActionBy(Long actionBy) {
		this.actionBy = actionBy;
	}

	public Long getActionOn() {
		return actionOn;
	}

	public void setActionOn(Long actionOn) {
		this.actionOn = actionOn;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Timestamp getRequestedTime() {
		return requestedTime;
	}

	public void setRequestedTime(Timestamp requestedTime) {
		this.requestedTime = requestedTime;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getActionByName() {
		return actionByName;
	}

	public void setActionByName(String actionByName) {
		this.actionByName = actionByName;
	}

	public String getActionOnName() {
		return actionOnName;
	}

	public void setActionOnName(String actionOnName) {
		this.actionOnName = actionOnName;
	}
	

}
