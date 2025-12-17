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
@Table(name = "tbl_mst_mode_of_education")
public class ModeOfEducationMaster extends AuditBase{

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_mst_mode_of_education", sequenceName = "seq_mst_mode_of_education", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_mode_of_education")
	private Long id;
	
	@Column(name ="mode_of_education")
	private String modeOfEducation;
	
	@Column(name="description")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModeOfEducation() {
		return modeOfEducation;
	}

	public void setModeOfEducation(String modeOfEducation) {
		this.modeOfEducation = modeOfEducation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
