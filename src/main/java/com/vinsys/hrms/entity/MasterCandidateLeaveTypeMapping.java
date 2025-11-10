package com.vinsys.hrms.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_leave_mapping")
public class MasterCandidateLeaveTypeMapping extends AuditBase implements Serializable {


	
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name = "seq_mst_leave_mapping", sequenceName = "seq_mst_leave_mapping", initialValue = 1, allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_leave_mapping")
	private long id;
	
	@Column(name = "leave_type")
	private long leaveType;

	@Column(name = "leave_count")
	private long leavecount;
	
	@Column(name = "division_id")
	private long divisionId;
	
	@Column(name = "leave_cycle")
	private String leaveCycle;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(long leaveType) {
		this.leaveType = leaveType;
	}

	public long getLeavecount() {
		return leavecount;
	}

	public void setLeavecount(long leavecount) {
		this.leavecount = leavecount;
	}

	public long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(long divisionId) {
		this.divisionId = divisionId;
	}
	
	

	public String getLeavecycle() {
		return leaveCycle;
	}

	public void setLeavecycle(String leavecycle) {
		this.leaveCycle = leavecycle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
