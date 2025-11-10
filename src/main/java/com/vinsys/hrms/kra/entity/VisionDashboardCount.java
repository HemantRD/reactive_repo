package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Immutable
@Table(name = "vw_vision_dashboard")
@Entity
public class VisionDashboardCount {

	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "cycle_name")
	private String cycleName;
	
	@Column(name = "employee_code")
	private String employeeCode;
	
	@Column(name = "first_name")
	private String firstName;
		
	@Column(name = "role_name")
	private String roleName;
	
	@Column(name = "wfid")
	private Long wfid;
	
	@Column(name = "status")
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getWfid() {
		return wfid;
	}

	public void setWfid(Long wfid) {
		this.wfid = wfid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
	
}
