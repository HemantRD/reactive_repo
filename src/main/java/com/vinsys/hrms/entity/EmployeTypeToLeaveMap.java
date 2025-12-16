package com.vinsys.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_map_emp_type_leave")
public class EmployeTypeToLeaveMap extends AuditBase{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "entity_id")
	private Long entityId;
	
	@Column(name = "employee_type")
	private Long empTypeId;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_type", insertable = false, updatable = false)
	private EmployeTypeMap employeeType;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "leave_type")
	private MasterLeaveType leaveType;

	

	public Long getEmpTypeId() {
		return empTypeId;
	}

	public void setEmpTypeId(Long empTypeId) {
		this.empTypeId = empTypeId;
	}


	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public EmployeTypeMap getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(EmployeTypeMap employeeType) {
		this.employeeType = employeeType;
	}

	public MasterLeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(MasterLeaveType leaveType) {
		this.leaveType = leaveType;
	}

}
