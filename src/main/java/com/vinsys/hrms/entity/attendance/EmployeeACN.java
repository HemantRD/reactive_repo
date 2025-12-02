package com.vinsys.hrms.entity.attendance;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.Employee;

@Entity
@Table(name = "tbl_attendance_employee_acn")
public class EmployeeACN extends AuditBase implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_employee_acn", sequenceName = "seq_employee_acn", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_acn")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id")
	private Employee employee;
	
	@Column(name="emp_acn")
	private long empACN;
	
	@Column(name="is_management")
	private boolean isManagement;

	@Column(name="is_flexible")
	private boolean isFlexible;
	
	public boolean getIsManagement() {
		return isManagement;
	}

	public void setIsManagement(boolean isManagement) {
		this.isManagement = isManagement;
	}

	public boolean getIsFlexible() {
		return isFlexible;
	}

	public void setIsFlexible(boolean isFlexible) {
		this.isFlexible = isFlexible;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public long getEmpACN() {
		return empACN;
	}

	public void setEmpACN(long empACN) {
		this.empACN = empACN;
	}


}
