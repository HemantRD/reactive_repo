package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_employee_grant_leave_detail")
public class EmployeeGrantLeaveDetail extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_employee_grant_leave_detail", sequenceName = "seq_employee_grant_leave_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_grant_leave_detail")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "leave_type_id")
	private MasterLeaveType masterLeaveType;
	@Column(name = "from_date")
	@Temporal(TemporalType.DATE)
	private Date fromDate;
	@Column(name = "to_date")
	@Temporal(TemporalType.DATE)
	private Date toDate;
	@Column(name = "from_session")
	private String fromSession;
	@Column(name = "to_session")
	private String toSession;
	@Column(name = "contact_details")
	private String contactDetails;
	@Column(name = "cc")
	private String cc;
	@Column(name = "leave_status")
	private String leaveStatus;
	@Column(name = "reason_for_apply")
	private String reasonForApply;
	@Column(name = "attachment")
	private String attachment;
	@Column(name = "reason_for_cancel")
	private String reasonForCancel;
	@Column(name = "date_of_applied")
	@Temporal(TemporalType.DATE)
	private Date dateOfApplied;
	@Column(name = "date_of_approver_action")
	@Temporal(TemporalType.DATE)
	private Date dateOfApproverAction;
	@Column(name = "applied_by")
	private String appliedBy;
	@Column(name = "date_of_cancel")
	@Temporal(TemporalType.DATE)
	private Date dateOfCancel;
	@Column(name = "reason_for_withdrawn")
	private String reasonForWithdrawn;
	@Column(name = "date_of_withdrawn")
	@Temporal(TemporalType.DATE)
	private Date dateOfWithdrawn;
	@Column(name = "approver_comment_on_withdrawn")
	private String approverCommentOnWithdrawn;
	@Column(name = "approver_action_date_withdrawn")
	@Temporal(TemporalType.DATE)
	private Date approverActionDateWithdrawn;
	@Column(name = "no_of_days")
	private float noOfDays;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public MasterLeaveType getMasterLeaveType() {
		return masterLeaveType;
	}

	public void setMasterLeaveType(MasterLeaveType masterLeaveType) {
		this.masterLeaveType = masterLeaveType;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

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

}
