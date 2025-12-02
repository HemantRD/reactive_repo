package com.vinsys.hrms.kra.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_mst_kra_timeline")
public class KpiTimeline extends AuditBase{
	
	@Id
	@SequenceGenerator(name = "seq__mst_kra_timeline", sequenceName = "seq__mst_kra_timeline", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq__mst_kra_timeline")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "label")
	private String label;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "color")
	private String color;
	
	@Column(name = "mst_kra_cycle_id")
	private Long cycleId;
	
	@Column(name = "year")
	private Long yearId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getCycleId() {
		return cycleId;
	}

	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

}
