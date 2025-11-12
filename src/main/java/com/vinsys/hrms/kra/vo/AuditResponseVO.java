package com.vinsys.hrms.kra.vo;

import java.util.Date;

public class AuditResponseVO {

	private String ActionBy ;
	private String ActionOn ;
	private String ActionName ;
	private String RequestUrl;
	private Long StatusCode ;
	private String ResponseMessage ;
	private Date CreatedDate;
	public String getActionBy() {
		return ActionBy;
	}
	public void setActionBy(String actionBy) {
		ActionBy = actionBy;
	}
	public String getActionOn() {
		return ActionOn;
	}
	public void setActionOn(String actionOn) {
		ActionOn = actionOn;
	}
	public String getActionName() {
		return ActionName;
	}
	public void setActionName(String actionName) {
		ActionName = actionName;
	}
	public String getRequestUrl() {
		return RequestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		RequestUrl = requestUrl;
	}
	public Long getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(Long statusCode) {
		StatusCode = statusCode;
	}
	public String getResponseMessage() {
		return ResponseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		ResponseMessage = responseMessage;
	}
	public Date getCreatedDate() {
		return CreatedDate;
	}
	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}
	
	
	
}
