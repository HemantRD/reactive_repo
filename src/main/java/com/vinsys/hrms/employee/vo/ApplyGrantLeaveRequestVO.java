package com.vinsys.hrms.employee.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Onkar A
 *
 * 
 */
public class ApplyGrantLeaveRequestVO {

	private long id;

	@Schema(required = true)
	private long employeeId;
	private long leaveTypeId;
	@Schema(required = true)
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String fromDate;
	@Schema(required = true)
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String toDate;
	@Schema(required = true)
	private String fromSession;
	@Schema(required = true)
	private String toSession;
	private String contactDetails;
	private String cc;
	private String leaveStatus;
	@Schema(required = true)
	private String reasonForApply;
	private String attachment;
	private String reasonForCancel;
	private String dateOfApplied;
	private String dateOfApproverAction;
	private String appliedBy;
	private String dateOfCancel;
	private String reasonForWithdrawn;
	private String dateOfWithdrawn;
	private String approverCommentOnWithdrawn;
	private String approverActionDateWithdrawn;
	@Schema(required = true)
	private float noOfDays;

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getLeaveTypeId() {
		return leaveTypeId;
	}

	public void setLeaveTypeId(long leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
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

	public String getDateOfApplied() {
		return dateOfApplied;
	}

	public void setDateOfApplied(String dateOfApplied) {
		this.dateOfApplied = dateOfApplied;
	}

	public String getDateOfApproverAction() {
		return dateOfApproverAction;
	}

	public void setDateOfApproverAction(String dateOfApproverAction) {
		this.dateOfApproverAction = dateOfApproverAction;
	}

	public String getAppliedBy() {
		return appliedBy;
	}

	public void setAppliedBy(String appliedBy) {
		this.appliedBy = appliedBy;
	}

	public String getDateOfCancel() {
		return dateOfCancel;
	}

	public void setDateOfCancel(String dateOfCancel) {
		this.dateOfCancel = dateOfCancel;
	}

	public String getReasonForWithdrawn() {
		return reasonForWithdrawn;
	}

	public void setReasonForWithdrawn(String reasonForWithdrawn) {
		this.reasonForWithdrawn = reasonForWithdrawn;
	}

	public String getDateOfWithdrawn() {
		return dateOfWithdrawn;
	}

	public void setDateOfWithdrawn(String dateOfWithdrawn) {
		this.dateOfWithdrawn = dateOfWithdrawn;
	}

	public String getApproverCommentOnWithdrawn() {
		return approverCommentOnWithdrawn;
	}

	public void setApproverCommentOnWithdrawn(String approverCommentOnWithdrawn) {
		this.approverCommentOnWithdrawn = approverCommentOnWithdrawn;
	}

	public String getApproverActionDateWithdrawn() {
		return approverActionDateWithdrawn;
	}

	public void setApproverActionDateWithdrawn(String approverActionDateWithdrawn) {
		this.approverActionDateWithdrawn = approverActionDateWithdrawn;
	}

	public float getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(float noOfDays) {
		this.noOfDays = noOfDays;
	}

}
