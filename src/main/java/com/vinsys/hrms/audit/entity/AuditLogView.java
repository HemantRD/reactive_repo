package com.vinsys.hrms.audit.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.vinsys.hrms.entity.AuditBase;


@Entity
@Table(name = "vw_pms_txn_audit_logs")
public class AuditLogView extends AuditBase{
	
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "action_by")
	private Long actionBy;
	@Column(name = "action_on")
	private Long actionOn;
	@Column(name = "action_name")
	private String actionName;
	@Column(name = "requested_time")
	private Timestamp requestedTime;
	@Column(name = "request_url")
	private String requestUrl;
	@Column(name = "status_code")
	private String statusCode;
	@Column(name = "status")
	private String status;
	@Column(name = "response_msg")
	private String responseMessage;
	@Column(name = "action_by_name")
	private String actionByName;
	@Column(name = "action_on_name")
	private String actionOnName;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "created_date")
	private Date createdDate;
	@Column(name = "updated_by")
	private String updatedBy;
	@Column(name = "updated_date")
	private Date updatedDate;
	@Column(name = "is_active")
	private String isActive;
	@Column(name = "remark")
	private String remark;
	@NotNull
	@Column(name = "org_id")
	private long orgId;
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	
	
	

}
