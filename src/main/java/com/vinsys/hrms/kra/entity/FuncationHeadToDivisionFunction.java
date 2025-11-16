package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.vinsys.hrms.entity.AuditBase;

@Entity(name = "tbl_map_functionheads_to_function")
public class FuncationHeadToDivisionFunction extends AuditBase {

	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "employee")
	private Long employee;
	@Column(name = "department")
	private Long department;
	@Column(name = "branch")
	private Long branch;
	@Column(name = "division")
	private Long division;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployee() {
		return employee;
	}

	public void setEmployee(Long employee) {
		this.employee = employee;
	}

	public Long getDepartment() {
		return department;
	}

	public void setDepartment(Long department) {
		this.department = department;
	}

	public Long getBranch() {
		return branch;
	}

	public void setBranch(Long branch) {
		this.branch = branch;
	}

	public Long getDivision() {
		return division;
	}

	public void setDivision(Long division) {
		this.division = division;
	}

}
