package com.vinsys.hrms.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_trn_emp_yearly_leave_credited_status")
public class EmpYearlyLeaveCreditedStatus extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_emp_yearly_leave_credited_status", sequenceName = "seq_emp_yearly_leave_credited_status", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_emp_yearly_leave_credited_status")
	private long id;

	@Column(name = "year")
	private long year;

	@Column(name = "leave_credited")
	private String leaveCrideted;

	@Column(name = "leave_credited_month")
	private String leaveCreditedMonth;

	@Column(name = "emp_id")
	private long employeeId;

	public String getLeaveCreditedMonth() {
		return leaveCreditedMonth;
	}

	public void setLeaveCreditedMonth(String leaveCreditedMonth) {
		this.leaveCreditedMonth = leaveCreditedMonth;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getYear() {
		return year;
	}

	public void setYear(long year) {
		this.year = year;
	}

	public String getLeaveCrideted() {
		return leaveCrideted;
	}

	public void setLeaveCrideted(String leaveCrideted) {
		this.leaveCrideted = leaveCrideted;
	}


}
