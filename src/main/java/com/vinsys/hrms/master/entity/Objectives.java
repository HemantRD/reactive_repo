package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_mst_kra_objectives")
public class Objectives extends AuditBase {
	
	@Id
	@SequenceGenerator(name = "seq_mst_kra_objectives", sequenceName = "seq_mst_kra_objectives", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_kra_objectives")
	@Column(name = "id", columnDefinition = "serial")
	private long id;
	
	@Column(name = "description")
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "mst_category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "mst_sub_category_id")
	private Subcategory subCategory;
	
	@Column(name = "obj_weightage")
	private Long objWeightage;
	
	@Column(name = "metrics")
	private String metrics;
	
	@Column(name = "title")
	private String title;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Subcategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(Subcategory subCategory) {
		this.subCategory = subCategory;
	}

	public String getMetrics() {
		return metrics;
	}

	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}

	public Long getObjWeightage() {
		return objWeightage;
	}

	public void setObjWeightage(Long objWeightage) {
		this.objWeightage = objWeightage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
