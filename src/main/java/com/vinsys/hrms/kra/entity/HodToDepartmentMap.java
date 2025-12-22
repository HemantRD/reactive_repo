package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_map_hod_to_department")
public class HodToDepartmentMap extends AuditBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_map_hod_to_department", sequenceName = "seq_map_hod_to_department", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_hod_to_department")
	@Column(name = "id")
	private Long id;
	@Column(name = "employee")
	private Long employeeId;

//	@Column(name = "org_id")
//	private Long orgId;
	@Column(name = "department")
	private Long departmentId;
	@Column(name = "branch")
	private Long branchId;
	@Column(name = "division")
	private Long divisionId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

//	public Long getOrgId() {
//		return orgId;
//	}
//
//	public void setOrgId(Long orgId) {
//		this.orgId = orgId;
//	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

}
