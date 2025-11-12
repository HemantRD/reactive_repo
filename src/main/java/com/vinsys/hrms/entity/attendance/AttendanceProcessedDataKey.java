package com.vinsys.hrms.entity.attendance;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class AttendanceProcessedDataKey implements Serializable{

	@NotNull
	@Column(name = "employee_ACN")
	private long employeeACN;
	
	@NotNull
	@Column(name = "attendance_date")
	private Date attendanceDate;
	
	@NotNull
	@Column(name = "org_id")
	private long orgId;
	@NotNull
	@Column(name = "emp_Id")
	private long empId;
	
	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attendanceDate == null) ? 0 : attendanceDate.hashCode());
		result = prime * result + (int) (employeeACN ^ (employeeACN >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttendanceProcessedDataKey other = (AttendanceProcessedDataKey) obj;
		if (attendanceDate == null) {
			if (other.attendanceDate != null)
				return false;
		} else if (!attendanceDate.equals(other.attendanceDate))
			return false;
		if (employeeACN != other.employeeACN)
			return false;
		return true;
	}

	public long getEmployeeACN() {
		return employeeACN;
	}

	public void setEmployeeACN(long employeeACN) {
		this.employeeACN = employeeACN;
	}

	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public long getEmpId() {
		return empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}
}
