package com.vinsys.hrms.traveldesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_map_td_approver")
public class MapTravelDeskApprover extends AuditBase {

	@Id
//	@SequenceGenerator(name = "seq_map_td_approver", sequenceName = "seq_map_td_approver", initialValue = 1, allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_td_approver")
	private Long id;
	
	@Column(name="td_approver_id")
	private Long approverId;
	
	@Column(name="division_id")
	private Long divisionId;
	@Column(name="branch_id")
	private Long branchId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
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
	

}
