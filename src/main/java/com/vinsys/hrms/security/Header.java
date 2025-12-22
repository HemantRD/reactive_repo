package com.vinsys.hrms.security;

import java.sql.Timestamp;

public class Header {
	private Long actionBy;
	private Long actionOn;
	private String actionName;
	private Timestamp requestedTime;
	private String requestUrl;
	private Long statusCode;
	private String status;

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

	public Long getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
