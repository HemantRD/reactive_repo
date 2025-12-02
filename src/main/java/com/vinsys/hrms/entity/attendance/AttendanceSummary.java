package com.vinsys.hrms.entity.attendance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="vw_employee_attendance_summary")
public class AttendanceSummary {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "emp_id")
	private long empId;
	@Column(name = "emp_name")
	private String empName;	
	@Column(name = "employee_acn")
	private String employeeAcn;
	@Column(name = "attendance_date")
	private Date attendanceDate;
	@Column(name = "present")
	private String present;
	@Column(name = "holiday")
	private String holiday;
	@Column(name = "absent")
	private String absent;
	@Column(name = "weeklyoff")
	private String weeklyoff;
	@Column(name = "halfday")
	private String halfday;
	@Column(name = "quarterDay")
	private String quarterDay;
	@Column(name = "OneThirdDay")
	private String oneThirdDay;
	@Column(name = "total")
	private String total;
	
	@Column(name = "branch_name")
	private String branch;
	@Column(name = "department_name")
	private String department;
	@Column(name = "designation_name")
	private String designation;
	@Column(name = "division_name")
	private String division;
	@Column(name = "org_name")
	private String orgName;
	
	@Column(name = "emp_is_active")
	private String empIsActive;
	@Column(name = "org_id")
	private long orgId;
	
	@Column(name = "over_time")
	private String overTime;
	
	@Column(name = "lop_count")
	private double lopCount;
	@Column(name = "leave_type")
	private String leaveType;

	public double getLopCount() {
		return lopCount;
	}
	public void setLopCount(double lopCount) {
		this.lopCount = lopCount;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public String getHoliday() {
		return holiday;
	}
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
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
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getEmpId() {
		return empId;
	}
	public void setEmpId(long empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmployeeAcn() {
		return employeeAcn;
	}
	public void setEmployeeAcn(String employeeAcn) {
		this.employeeAcn = employeeAcn;
	}
	public Date getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
	public String getPresent() {
		return present;
	}
	public void setPresent(String present) {
		this.present = present;
	}
	public String getAbsent() {
		return absent;
	}
	public void setAbsent(String absent) {
		this.absent = absent;
	}
	public String getWeeklyoff() {
		return weeklyoff;
	}
	public void setWeeklyoff(String weeklyoff) {
		this.weeklyoff = weeklyoff;
	}
	public String getHalfday() {
		return halfday;
	}
	public void setHalfday(String halfday) {
		this.halfday = halfday;
	}
	public String getQuarterDay() {
		return quarterDay;
	}
	public void setQuarterDay(String quarterDay) {
		this.quarterDay = quarterDay;
	}
	public String getOneThirdDay() {
		return oneThirdDay;
	}
	public void setOneThirdDay(String oneThirdDay) {
		this.oneThirdDay = oneThirdDay;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	
	public String getEmpIsActive() {
		return empIsActive;
	}
	public void setEmpIsActive(String empIsActive) {
		this.empIsActive = empIsActive;
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}		
}
