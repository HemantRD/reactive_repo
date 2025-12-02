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
@Table(name = "tbl_mst_kra_cycle_type")
public class KraCycleType extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_mst_kra_cycle_type", sequenceName = "seq_mst_kra_cycle_type", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_kra_cycle_type")
	private Long id;
	
	@Column(name = "cycle_type_name")
	private String cycleTypeName;
	
	@Column(name = "cycle_type_description")
	private String cycleTypeDescription;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCycleTypeName() {
		return cycleTypeName;
	}

	public void setCycleTypeName(String cycleTypeName) {
		this.cycleTypeName = cycleTypeName;
	}

	public String getCycleTypeDescription() {
		return cycleTypeDescription;
	}

	public void setCycleTypeDescription(String cycleTypeDescription) {
		this.cycleTypeDescription = cycleTypeDescription;
	}
	
}
