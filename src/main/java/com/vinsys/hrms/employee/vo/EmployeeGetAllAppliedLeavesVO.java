package com.vinsys.hrms.employee.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public class EmployeeGetAllAppliedLeavesVO {

	private long id;

	private String employeeName;
//	private VOEmployee employee;
//	private VOMasterLeaveType masterLeaveType;
	private Long employeeId;
	@Schema(required = true)
	private Long leaveTypeId;
	@Schema(required = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date fromDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	@Schema(required = true)
	private Date toDate;
	@Schema(required = true)
	private String fromSession;
	@Schema(required = true)
	private String toSession;
	private String contactDetails;
	@Schema(required = true)
	private String cc;
	private String leaveStatus;
	private String leaveTypeName;
	private String leaveTypeDescription;
	private String reasonForApply;
	private String attachment;
	private String reasonForCancel;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date dateOfApplied;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date dateOfApproverAction;
	private String appliedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date dateOfCancel;
	private String reasonForWithdrawn;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date dateOfWithdrawn;
	private String approverCommentOnWithdrawn;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date approverActionDateWithdrawn;
	private float noOfDays;
	private String reasonForReject;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date validTill;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getLeaveTypeId() {
		return leaveTypeId;
	}

	public void setLeaveTypeId(Long leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

//	public Date getFromDate() {
//		return fromDate;
//	}
//	public void setFromDate(Date fromDate) {
//		this.fromDate = fromDate;
//	}
	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getFromSession() {
		return fromSession;
	}

	public void setFromSession(String fromSession) {
		this.fromSession = fromSession;
	}

	public String getToSession() {
		return toSession;
	}

	public void setToSession(String toSession) {
		this.toSession = toSession;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public String getReasonForApply() {
		return reasonForApply;
	}

	public void setReasonForApply(String reasonForApply) {
		this.reasonForApply = reasonForApply;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getReasonForCancel() {
		return reasonForCancel;
	}

	public void setReasonForCancel(String reasonForCancel) {
		this.reasonForCancel = reasonForCancel;
	}

	public Date getDateOfApplied() {
		return dateOfApplied;
	}

	public void setDateOfApplied(Date dateOfApplied) {
		this.dateOfApplied = dateOfApplied;
	}

	public Date getDateOfApproverAction() {
		return dateOfApproverAction;
	}

	public void setDateOfApproverAction(Date dateOfApproverAction) {
		this.dateOfApproverAction = dateOfApproverAction;
	}

	public String getAppliedBy() {
		return appliedBy;
	}

	public void setAppliedBy(String appliedBy) {
		this.appliedBy = appliedBy;
	}

	public Date getDateOfCancel() {
		return dateOfCancel;
	}

	public void setDateOfCancel(Date dateOfCancel) {
		this.dateOfCancel = dateOfCancel;
	}

	public String getReasonForWithdrawn() {
		return reasonForWithdrawn;
	}

	public void setReasonForWithdrawn(String reasonForWithdrawn) {
		this.reasonForWithdrawn = reasonForWithdrawn;
	}

	public Date getDateOfWithdrawn() {
		return dateOfWithdrawn;
	}

	public void setDateOfWithdrawn(Date dateOfWithdrawn) {
		this.dateOfWithdrawn = dateOfWithdrawn;
	}

	public String getApproverCommentOnWithdrawn() {
		return approverCommentOnWithdrawn;
	}

	public void setApproverCommentOnWithdrawn(String approverCommentOnWithdrawn) {
		this.approverCommentOnWithdrawn = approverCommentOnWithdrawn;
	}

	public Date getApproverActionDateWithdrawn() {
		return approverActionDateWithdrawn;
	}

	public void setApproverActionDateWithdrawn(Date approverActionDateWithdrawn) {
		this.approverActionDateWithdrawn = approverActionDateWithdrawn;
	}

	public float getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(float noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getLeaveTypeName() {
		return leaveTypeName;
	}

	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}

	public String getLeaveTypeDescription() {
		return leaveTypeDescription;
	}

	public void setLeaveTypeDescription(String leaveTypeDescription) {
		this.leaveTypeDescription = leaveTypeDescription;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getReasonForReject() {
		return reasonForReject;
	}

	public void setReasonForReject(String reasonForReject) {
		this.reasonForReject = reasonForReject;
	}

	public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

}
