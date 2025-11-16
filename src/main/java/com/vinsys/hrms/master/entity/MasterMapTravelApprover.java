package com.vinsys.hrms.master.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.Employee;

@Entity
@Table(name = "tbl_map_travel_approver")
public class MasterMapTravelApprover extends AuditBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "approver_slab")
	private Long approverSlab;

	// taking from audit
//	@Column(name="org_id")
//	private Long orgId;

	@Column(name = "division_id")
	private Long divisionId;

	@Column(name = "branch_id")
	private Long branchId;

	@Column(name = "department_id")
	private Long departmentId;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "approver_id")
	private Employee approverId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApproverSlab() {
		return approverSlab;
	}

	public void setApproverSlab(Long approverSlab) {
		this.approverSlab = approverSlab;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Employee getApproverId() {
		return approverId;
	}

	public void setApproverId(Employee approverId) {
		this.approverId = approverId;
	}

}
