package com.vinsys.hrms.entity.attendance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="vw_attendance_csv_data")
public class AttendanceCSVDataSummary {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "card_no")
	private long cardNo;

	@Column(name = "employee_id")
	private long employeeId;
	
	@Column(name = "org_id")
	private long orgId;
	@Column(name = "attendance_date")
	private Date attendanceDate;
	@Temporal(TemporalType.TIME)
	@Column(name = "start_time")
	private Date startTime;	
	@Temporal(TemporalType.TIME)
	@Column(name = "end_time")
	private Date endTime;
	@Column(name = "hours", precision=2)
	private String hours;
	@Column(name = "minutes", precision=2)
	private String minutes;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getCardNo() {
		return cardNo;
	}
	public void setCardNo(long cardNo) {
		this.cardNo = cardNo;
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	public Date getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
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
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getMinutes() {
		return minutes;
	}
	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	
	
	}
