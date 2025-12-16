package com.vinsys.hrms.entity.attendance;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_attendance_raw_data")
public class AttendanceRawData extends AuditBase {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_attendance_raw_data", sequenceName = "seq_attendance_raw_data", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_attendance_raw_data")
	private long id;
	
	@Column(name = "emp_Id")
	private long empId;
	@Column(name = "employee_ACN")
	private long employeeACN;
	@Column(name = "emp_name")
	private String empName;
	@Column(name = "department")
	private String department;
	@Column(name = "designation")
	private String designation;
	@Column(name = "attendance_date")
	private Date attendanceDate;
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
	
	@Column(name = "org_id")
	private long orgId;
	
	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEmpId() {
		return empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public long getEmployeeACN() {
		return employeeACN;
	}

	public void setEmployeeACN(long l) {
		this.employeeACN = l;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
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

	
}
