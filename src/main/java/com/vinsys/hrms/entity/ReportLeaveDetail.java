package com.vinsys.hrms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="view_leave_details_report")
public class ReportLeaveDetail {

	@Id
	@Column(name = "leave_applied_id")
	private String id;
	@Column(name = "employee_id")
	private long employeeId;
	@Column(name = "employee_name")
	private String employeeName;
	@Column(name = "designation_name")
	private String designationName;
	@Column(name = "department_name")
	private String departmentName;
	@Column(name = "branch_name")
	private String branchName;
	@Column(name = "division_name")
	private String divisionName;
	@Column(name = "date_of_applied")
	private Date dateOfApplied;
	@Column(name = "from_date")
	private Date fromDate;
	@Column(name = "to_date")
	private Date toDate;
	@Column(name = "reporting_manager")
	private String reportingManager;
	@Column(name = "cancel_request")
	private float cancelRequest;
	@Column(name = "no_action")
	private float noAction;
	@Column(name = "applied")
	private float applied;
	@Column(name = "cancelled")
	private float cancelled;
	@Column(name = "ro_approved")
	private float roApproved;
	@Column(name = "rejected")
	private float rejected;
	@Column(name = "approved")
	private float approved;
	@Column(name = "leave_type_name")
	private String leaveTypeName;
	@Column(name = "leave_type_id")
	private long leaveTypeId;
	@Column(name = "branch_id")
	private long branchId;
	@Column(name = "department_id")
	private long departmentId;
	@Column(name = "designation_id")
	private long designationId;
	@Column(name = "division_id")
	private long divisionId;
	@Column(name = "emp_is_active")
	private String empIsActive;
	@Column(name = "from_session")
	private String fromSession;
	@Column(name = "to_session")
	private String toSession;
	@Column(name = "organization_id")
	private Long orgId;
	@Transient
	private String leaveTypesStr;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Date getDateOfApplied() {
		return dateOfApplied;
	}
	public void setDateOfApplied(Date dateOfApplied) {
		this.dateOfApplied = dateOfApplied;
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
	public String getReportingManager() {
		return reportingManager;
	}
	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}
	public float getCancelRequest() {
		return cancelRequest;
	}
	public void setCancelRequest(float cancelRequest) {
		this.cancelRequest = cancelRequest;
	}
	public float getNoAction() {
		return noAction;
	}
	public void setNoAction(float noAction) {
		this.noAction = noAction;
	}
	public float getApplied() {
		return applied;
	}
	public void setApplied(float applied) {
		this.applied = applied;
	}
	public float getCancelled() {
		return cancelled;
	}
	public void setCancelled(float cancelled) {
		this.cancelled = cancelled;
	}
	public float getRoApproved() {
		return roApproved;
	}
	public void setRoApproved(float roApproved) {
		this.roApproved = roApproved;
	}
	public float getRejected() {
		return rejected;
	}
	public void setRejected(float rejected) {
		this.rejected = rejected;
	}
	public float getApproved() {
		return approved;
	}
	public void setApproved(float approved) {
		this.approved = approved;
	}
	public String getLeaveTypeName() {
		return leaveTypeName;
	}
	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}
	public long getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(long leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public String getLeaveTypesStr() {
		return leaveTypesStr;
	}
	public void setLeaveTypesStr(String leaveTypesStr) {
		this.leaveTypesStr = leaveTypesStr;
	}
	public long getBranchId() {
		return branchId;
	}
	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}
	public long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	public long getDesignationId() {
		return designationId;
	}
	public void setDesignationId(long designationId) {
		this.designationId = designationId;
	}
	public long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(long divisionId) {
		this.divisionId = divisionId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public String getEmpIsActive() {
		return empIsActive;
	}
	public void setEmpIsActive(String empIsActive) {
		this.empIsActive = empIsActive;
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
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}
