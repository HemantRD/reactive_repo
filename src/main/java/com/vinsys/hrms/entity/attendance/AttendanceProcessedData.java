package com.vinsys.hrms.entity.attendance;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_attendance_processed_data")
public class AttendanceProcessedData  {

	private static final long serialVersionUID = 1L;
	
	 
	@EmbeddedId
	private AttendanceProcessedDataKey compositePrimaryKey;
	
	public AttendanceProcessedDataKey getCompositePrimaryKey() {
		return compositePrimaryKey;
	}

	public void setCompositePrimaryKey(AttendanceProcessedDataKey compositePrimaryKey) {
		this.compositePrimaryKey = compositePrimaryKey;
	}

	
	/*@Column(name = "employee_ACN")
	private long employeeACN;*/
	@Column(name = "emp_name")
	private String empName;
	
	/*@Column(name = "attendance_date")
	private Date attendanceDate;*/
	@Column(name = "status")
	private String status;
	
	@Temporal(TemporalType.TIME)
	@Column(name = "start_time")
	private Date startTime;	
	@Temporal(TemporalType.TIME)
	@Column(name = "end_time")
	private Date endTime;
	@Column(name = "man_hours", precision=2)
	private double manHours;
	@Column(name = "approval_status")
	private String approvalStatus;
	@Column(name = "upload_status")
	private String uploadStatus;
	@Column(name = "over_time", precision=2)
	private double overTime;
	
	@Column(name = "lop_count", precision=2)
	private double lop;
	@Column(name = "leave_type")
	private String leaveType;
	
	/*@Column(name = "over")
	private LocalTime over;
	
	
	public LocalTime getOver() {
		return over;
	}

	public void setOver(LocalTime over) {
		this.over = over;
	}*/

	@Column(name = "designation_id")
	private long designationId;
	@Column(name = "department_id")
	private long departmentId;
	@Column(name = "branch_id")
	private long branchId;
	@Column(name = "division_id")
	private long divisionId;
	
	
	@Column(name = "designation_name")
	private String designationName;
	@Column(name = "department_name")
	private String departmentName;
	@Column(name = "branch_name")
	private String branchName;
	@Column(name = "division_name")
	private String divisionName;
	
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
	
	
	public double getLop() {
		return lop;
	}

	public void setLop(double lop) {
		this.lop = lop;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
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

	public double getOverTime() {
		return overTime;
	}

	public void setOverTime(double overTime) {
		this.overTime = overTime;
	}
	
	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

//	public long getEmpId() {
//		return empId;
//	}
//
//	public void setEmpId(long empId) {
//		this.empId = empId;
//	}

	/*public long getEmployeeACN() {
		return employeeACN;
	}

	public void setEmployeeACN(long l) {
		this.employeeACN = l;
	}*/

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}



	/*public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}*/

	public long getDesignationId() {
		return designationId;
	}

	public void setDesignationId(long designationId) {
		this.designationId = designationId;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(long divisionId) {
		this.divisionId = divisionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public double getManHours() {
		return manHours;
	}

	public void setManHours(double manHours) {
		this.manHours = manHours;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
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

	
	
	
}
