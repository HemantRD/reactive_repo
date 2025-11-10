package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author madhuri.wakchaure
 */
@Entity
@Table(name = "tbl_mst_kra_rating")
public class KraRating {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial")
	private Long id;
	@Column(name = "value")
	private Double value;
	@Column(name = "lable")
	private String label;
	
	@Column(name = "target")
	private Long target;
	
	@Column(name = "is_active")
	private String isActive;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@Column(name = "is_passfail")
	private String isPassFail;
	
	@Column(name = "on_occurrence")
	private String onOccurrence;
	
	
	@Column(name = "target_percentage")
	private Double targetPercentage;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public Long getTarget() {
		return target;
	}
	public void setTarget(Long target) {
		this.target = target;
	}
	public String getIsPassFail() {
		return isPassFail;
	}
	public void setIsPassFail(String isPassFail) {
		this.isPassFail = isPassFail;
	}
	public Double getTargetPercentage() {
		return targetPercentage;
	}
	public void setTargetPercentage(Double targetPercentage) {
		this.targetPercentage = targetPercentage;
	}
	public String getOnOccurrence() {
		return onOccurrence;
	}
	public void setOnOccurrence(String onOccurrence) {
		this.onOccurrence = onOccurrence;
	}
	
}
