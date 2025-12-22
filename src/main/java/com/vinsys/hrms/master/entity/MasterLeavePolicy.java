package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

/**
 * 
 * @author Onkar A
 *
 *
 */
@Entity
@Table(name = "tbl_mst_leave_policy")
public class MasterLeavePolicy extends AuditBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "entity_id")
	private Long entityId;

	@Column(name = "organization")
	private Long organizationId;

	@Column(name = "division")
	private Long divisionId;

	@Column(name = "branch")
	private Long branchId;

	@Column(name = "department")
	private Long departmentId;

	@Column(name = "leave_type")
	private Long leaveTypeId;

	@Column(name = "allowed_min_days")
	private Long allowedMinDays;

	@Column(name = "allowed_max_days")
	private Long allowedMaxDays;

	@Column(name = "allowed_previous_month")
	private String allowedPreviousMonth;

	@Column(name = "allowed_months_laps")
	private Long allowedMonthsLaps;

	@Column(name = "allowed_days")
	private Long allowedDays;

	@Column(name = "is_weekend_allowed")
	private String isWeekendAllowed;

	@Column(name = "approval_required")
	private String approvalRequired;

	@Column(name = "auto_approve_required")
	private String autoApproveRequired;

	@Column(name = "comment_required")
	private String commentRequired;

	@Column(name = "auto_approve_days")
	private Long autoApproveDays;

	@Column(name = "validity_days")
	private Long validityDays;

	@Column(name = "carry_forward")
	private String carryForward;

	@Column(name = "allowed_gender")
	private String allowedGender;

	@Column(name = "optional1")
	private String optional1;

	@Column(name = "optional2")
	private String optional2;

	@Column(name = "optional3")
	private String optional3;

	@Column(name = "optional4")
	private String optional4;

	@Column(name = "optional5")
	private String optional5;
	@Column(name = "cc_required")
	private String ccRequired;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getLeaveTypeId() {
		return leaveTypeId;
	}

	public void setLeaveTypeId(Long leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

	public Long getAllowedMinDays() {
		return allowedMinDays;
	}

	public void setAllowedMinDays(Long allowedMinDays) {
		this.allowedMinDays = allowedMinDays;
	}

	public Long getAllowedMaxDays() {
		return allowedMaxDays;
	}

	public void setAllowedMaxDays(Long allowedMaxDays) {
		this.allowedMaxDays = allowedMaxDays;
	}

	public String getAllowedPreviousMonth() {
		return allowedPreviousMonth;
	}

	public void setAllowedPreviousMonth(String allowedPreviousMonth) {
		this.allowedPreviousMonth = allowedPreviousMonth;
	}

	public Long getAllowedMonthsLaps() {
		return allowedMonthsLaps;
	}

	public void setAllowedMonthsLaps(Long allowedMonthsLaps) {
		this.allowedMonthsLaps = allowedMonthsLaps;
	}

	public Long getAllowedDays() {
		return allowedDays;
	}

	public void setAllowedDays(Long allowedDays) {
		this.allowedDays = allowedDays;
	}

	public String getIsWeekendAllowed() {
		return isWeekendAllowed;
	}

	public void setIsWeekendAllowed(String isWeekendAllowed) {
		this.isWeekendAllowed = isWeekendAllowed;
	}

	public String getApprovalRequired() {
		return approvalRequired;
	}

	public void setApprovalRequired(String approvalRequired) {
		this.approvalRequired = approvalRequired;
	}

	public String getAutoApproveRequired() {
		return autoApproveRequired;
	}

	public void setAutoApproveRequired(String autoApproveRequired) {
		this.autoApproveRequired = autoApproveRequired;
	}

	public String getCommentRequired() {
		return commentRequired;
	}

	public void setCommentRequired(String commentRequired) {
		this.commentRequired = commentRequired;
	}

	public Long getAutoApproveDays() {
		return autoApproveDays;
	}

	public void setAutoApproveDays(Long autoApproveDays) {
		this.autoApproveDays = autoApproveDays;
	}

	public Long getValidityDays() {
		return validityDays;
	}

	public void setValidityDays(Long validityDays) {
		this.validityDays = validityDays;
	}

	public String getCarryForward() {
		return carryForward;
	}

	public void setCarryForward(String carryForward) {
		this.carryForward = carryForward;
	}

	public String getAllowedGender() {
		return allowedGender;
	}

	public void setAllowedGender(String allowedGender) {
		this.allowedGender = allowedGender;
	}

	public String getOptional1() {
		return optional1;
	}

	public void setOptional1(String optional1) {
		this.optional1 = optional1;
	}

	public String getOptional2() {
		return optional2;
	}

	public void setOptional2(String optional2) {
		this.optional2 = optional2;
	}

	public String getOptional3() {
		return optional3;
	}

	public void setOptional3(String optional3) {
		this.optional3 = optional3;
	}

	public String getOptional4() {
		return optional4;
	}

	public void setOptional4(String optional4) {
		this.optional4 = optional4;
	}

	public String getOptional5() {
		return optional5;
	}

	public void setOptional5(String optional5) {
		this.optional5 = optional5;
	}

	public String getCcRequired() {
		return ccRequired;
	}

	public void setCcRequired(String ccRequired) {
		this.ccRequired = ccRequired;
	}
}
