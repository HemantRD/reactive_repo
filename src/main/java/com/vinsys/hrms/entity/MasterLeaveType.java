package com.vinsys.hrms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "tbl_mst_leave_type")
public class MasterLeaveType extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_mst_leave_type", sequenceName = "seq_mst_leave_type", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_leave_type")
	private Long id;
	@Column(name = "leave_type_name")
	private String leaveTypeName;
	@Column(name = "leave_type_description")
	private String leaveTypeDescription;
	@Column(name = "number_of_session")
	private Integer numberOfSession;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_id",insertable = false,updatable = false)
	private Organization organization;
	//@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
	//		CascadeType.REFRESH })
	//@JoinColumn(name = "branch_id")
	//private MasterBranch branch;
	@Column(name = "leave_grant_status")
	private String leaveGrantStatus;
	
	@Column(name = "is_LOP")
	private String isLop;
	
	@Column(name = "leave_type_code")
	private String leaveTypeCode;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "division_id")
	private MasterDivision division;
	
	@Column(name = "leave_expiry")
	private Integer leaveExpiry;	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLeaveTypeName() {
		return leaveTypeName;
	}
	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}
	public String getLeaveTypeDescription() {
		return leaveTypeDescription;
	}
	public void setLeaveTypeDescription(String leaveTypeDescription) {
		this.leaveTypeDescription = leaveTypeDescription;
	}
	public Integer getNumberOfSession() {
		return numberOfSession;
	}
	public void setNumberOfSession(Integer numberOfSession) {
		this.numberOfSession = numberOfSession;
	}
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	public void setNumberOfSession(int numberOfSession) {
		this.numberOfSession = numberOfSession;
	}
	/*
	public MasterBranch getBranch() {
		return branch;
	}
	public void setBranch(MasterBranch branch) {
		this.branch = branch;
	}
	*/
	public String getLeaveGrantStatus() {
		return leaveGrantStatus;
	}
	public void setLeaveGrantStatus(String leaveGrantStatus) {
		this.leaveGrantStatus = leaveGrantStatus;
	}
	public String getIsLop() {
		return isLop;
	}
	public void setIsLop(String isLop) {
		this.isLop = isLop;
	}
	public String getLeaveTypeCode() {
		return leaveTypeCode;
	}
	public void setLeaveTypeCode(String leaveTypeCode) {
		this.leaveTypeCode = leaveTypeCode;
	}
	public MasterDivision getDivision() {
		return division;
	}
	public void setDivision(MasterDivision division) {
		this.division = division;
	}
	public Integer getLeaveExpiry() {
		return leaveExpiry;
	}
	public void setLeaveExpiry(Integer leaveExpiry) {
		this.leaveExpiry = leaveExpiry;
	}
	
}
