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

import com.vinsys.hrms.kra.entity.KraYear;
@Entity
@Table(name = "tbl_mst_kra_category")
public class Category {

@Id
@SequenceGenerator(name = "seq_mst_kra_category", sequenceName = "seq_mst_kra_category", initialValue = 1, allocationSize = 1)
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_kra_category")
@Column(name = "id", columnDefinition = "serial")
private long id;
@Column(name = "category_name")
private String categoryName;
@Column(name = "is_active")
private String isActive;
@Column(name = "org_id")
private long orgId;
@Column(name = "weightage")
private Long weightage;

@ManyToOne
@JoinColumn(name = "kra_year")
private KraYear kraYear;

public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getCategoryName() {
	return categoryName;
}
public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}
public String getIsActive() {
	return isActive;
}
public void setIsActive(String isActive) {
	this.isActive = isActive;
}

public long getOrgId() {
	return orgId;
}
public void setOrgId(long orgId) {
	this.orgId = orgId;
}
public KraYear getKraYear() {
	return kraYear;
}
public void setKraYear(KraYear kraYear) {
	this.kraYear = kraYear;
}
public long getWeightage() {
	return weightage;
}
public void setWeightage(long weightage) {
	this.weightage = weightage;
}







}
