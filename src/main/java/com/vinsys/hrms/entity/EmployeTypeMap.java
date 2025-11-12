package com.vinsys.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_map_employee_type")
public class EmployeTypeMap extends AuditBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "entity_id")
	private Long entityId;

	@Column(name = "emp_type")
	private String employeeType;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "division")
	private MasterDivision division;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "branch")
	private MasterBranch branch;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department")
	private MasterDepartment department;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public MasterDivision getDivision() {
		return division;
	}

	public void setDivision(MasterDivision division) {
		this.division = division;
	}

	public MasterBranch getBranch() {
		return branch;
	}

	public void setBranch(MasterBranch branch) {
		this.branch = branch;
	}

	public MasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(MasterDepartment department) {
		this.department = department;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
