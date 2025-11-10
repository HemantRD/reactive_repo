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
@Table(name = "tbl_mst_kra_year")
public class KraYear extends AuditBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_mst_kra_year", sequenceName = "seq_mst_kra_year", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_kra_year")
	@Column(name = "id")
	private Long id;

	@Column(name = "year")
	private String year;

	@Column(name = "label")
	private String label;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	

}
