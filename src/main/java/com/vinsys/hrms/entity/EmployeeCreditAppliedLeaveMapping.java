package com.vinsys.hrms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_map_employee_credit_applied_leave_mapping")
public class EmployeeCreditAppliedLeaveMapping extends AuditBase {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_employee_credit_applied_leave_mapping", sequenceName = "seq_employee_credit_applied_leave_mapping", 
		initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_credit_applied_leave_mapping")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_leave_applied_id")
	private EmployeeLeaveApplied employeeLeaveApplied;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "credit_leave_details_id")
	private EmployeeCreditLeaveDetail creditLeaveDetail;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public EmployeeLeaveApplied getEmployeeLeaveApplied() {
		return employeeLeaveApplied;
	}
	public void setEmployeeLeaveApplied(EmployeeLeaveApplied employeeLeaveApplied) {
		this.employeeLeaveApplied = employeeLeaveApplied;
	}
	public EmployeeCreditLeaveDetail getCreditLeaveDetail() {
		return creditLeaveDetail;
	}
	public void setCreditLeaveDetail(EmployeeCreditLeaveDetail creditLeaveDetail) {
		this.creditLeaveDetail = creditLeaveDetail;
	}
	
}
