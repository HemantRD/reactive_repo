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
@Table(name = "tbl_map_hr_to_hod")
public class HrToHodMap extends AuditBase{

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_map_hod_to_hr", sequenceName = "seq_map_hod_to_hr", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_hod_to_hr")
	private Long id;
	
	@Column(name = "hr_employee_id")
	private Long hrEmployeeId;

	@Column(name = "hod_employee_id")
	private Long hodEmployeeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHrEmployeeId() {
		return hrEmployeeId;
	}

	public void setHrEmployeeId(Long hrEmployeeId) {
		this.hrEmployeeId = hrEmployeeId;
	}

	public Long getHodEmployeeId() {
		return hodEmployeeId;
	}

	public void setHodEmployeeId(Long hodEmployeeId) {
		this.hodEmployeeId = hodEmployeeId;
	}
	
	
	
	
	
}
