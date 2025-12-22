package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOEmployeeGrantLeaveDetail extends VOAuditBase {

	private long id;
	private VOEmployee employee;
	private VOMasterLeaveType masterLeaveType;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String fromDate;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String toDate;
	private String fromSession;
	private String toSession;
	private String contactDetails;
	private String cc;
	private String leaveStatus;
	private String reasonForApply;
	private String attachment;
	private String reasonForCancel;
	private String dateOfApplied;
	private Date dateOfApproverAction;
	private String appliedBy;
	private Date dateOfCancel;	
	private String reasonForWithdrawn;
	private Date dateOfWithdrawn;
	private String approverCommentOnWithdrawn;
	private Date approverActionDateWithdrawn;
	private float noOfDays;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public VOEmployee getEmployee() {
		return employee;
	}
	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}
	public VOMasterLeaveType getMasterLeaveType() {
		return masterLeaveType;
	}
	public void setMasterLeaveType(VOMasterLeaveType masterLeaveType) {
		this.masterLeaveType = masterLeaveType;
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
	
}
