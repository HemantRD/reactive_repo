package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_mst_traveller_type")
public class MasterTravellerType extends AuditBase {
	
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_mst_traveller_type", sequenceName = "seq_mst_traveller_type", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_traveller_type")
	private Long id;
	
	@Column(name="traveller_type")
	private String travellerType;
	
	@Column(name="description")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTravellerType() {
		return travellerType;
	}

	public void setTravellerType(String travellerType) {
		this.travellerType = travellerType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
