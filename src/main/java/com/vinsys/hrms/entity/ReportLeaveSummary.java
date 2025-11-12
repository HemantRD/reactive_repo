package com.vinsys.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "view_leave_summary_report")

public class ReportLeaveSummary {

	@Id
	@Column(name = "leave_summary_id")
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
	@Column(name = "reporting_manager")
	private String reportingManager;
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
	@Column(name = "year")
	private int year;
	@Column(name = "closing_balance")
	private Float closingBalance;
	@Column(name = "py_leave_encashment")
	private Float pyLeaveEncashment;
	@Column(name = "leave_carried_over")
	private Float leaveCarriedOver;
	@Column(name = "leave_earned")
	private Float leaveEarned;
	@Column(name = "fy_leave_encashment")
	private Float fyLeaveEncashment;
	@Column(name = "total_eligibility")
	private Float totalEligibility;
	@Column(name = "number_of_days_availed")
	private Float numberOfDaysAvailed;
	@Column(name = "leave_available")
	private Float leaveAvailable;
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

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
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

	public String getEmpIsActive() {
		return empIsActive;
	}

	public void setEmpIsActive(String empIsActive) {
		this.empIsActive = empIsActive;
	}

	public String getLeaveTypesStr() {
		return leaveTypesStr;
	}

	public void setLeaveTypesStr(String leaveTypesStr) {
		this.leaveTypesStr = leaveTypesStr;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Float getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(Float closingBalance) {
		this.closingBalance = closingBalance;
	}

	public Float getPyLeaveEncashment() {
		return pyLeaveEncashment;
	}

	public void setPyLeaveEncashment(Float pyLeaveEncashment) {
		this.pyLeaveEncashment = pyLeaveEncashment;
	}

	public Float getLeaveCarriedOver() {
		return leaveCarriedOver;
	}

	public void setLeaveCarriedOver(Float leaveCarriedOver) {
		this.leaveCarriedOver = leaveCarriedOver;
	}

	public Float getLeaveEarned() {
		return leaveEarned;
	}

	public void setLeaveEarned(Float leaveEarned) {
		this.leaveEarned = leaveEarned;
	}

	public Float getFyLeaveEncashment() {
		return fyLeaveEncashment;
	}

	public void setFyLeaveEncashment(Float fyLeaveEncashment) {
		this.fyLeaveEncashment = fyLeaveEncashment;
	}

	public Float getTotalEligibility() {
		return totalEligibility;
	}

	public void setTotalEligibility(Float totalEligibility) {
		this.totalEligibility = totalEligibility;
	}

	public Float getNumberOfDaysAvailed() {
		return numberOfDaysAvailed;
	}

	public void setNumberOfDaysAvailed(Float numberOfDaysAvailed) {
		this.numberOfDaysAvailed = numberOfDaysAvailed;
	}

	public Float getLeaveAvailable() {
		return leaveAvailable;
	}

	public void setLeaveAvailable(Float leaveAvailable) {
		this.leaveAvailable = leaveAvailable;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
