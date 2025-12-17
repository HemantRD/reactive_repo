package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vw_vision_kra_details")
public class VisionKraDetailsView {

	@Id
	@Column(name = "id")
	private long id;
	@Column(name = "employee_id")
	private long employeeId;
	@Column(name = "\"Category\"")
	private String category;
	@Column(name = "kra_id")
	private Long kraId;
	@Column(name = "\"Sub-category\"")
	private String subCategory;
	@Column(name = "\"Objectives\"")
	private String Objectives;
	@Column(name = "\"Category Weight\"")
	private Float categoryWeight;
	@Column(name = "\"Objective Weight\"")
	private Float objectiveWeight;
	@Column(name = "\"Metric\"")
	private String metric;
	@Column(name = "self_rating")
	private Long selfRating;
	@Column(name = "self_qaulitative_assisment")
	private String selfQaulitativeAssisment;
	@Column(name = "manager_rating")
	private Long managerRating;
	@Column(name = "manager_qaulitative_assisment")
	private String managerQaulitativeAssisment;
	@Column(name = "category_id")
	private Long categoryId;
	@Column(name = "sub_category_id")
	private Long subCategoryId;
	@Column(name = "mst_kra_cycle_id")
	private Long kraCycleId;
	@Column(name = "is_edit")
	private String is_edit = "N";
	@Column(name = "is_color")
	private String is_color = "N";
	@Column(name = "is_active")
	private String isActive;
	@Column(name = "hcd_comment")
	private String hcdComment;
	@Column(name = "cycle_name")
	private String cycleName;
	@Column(name = "rm_ai_comment")
	private String rmAiComment;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getKraId() {
		return kraId;
	}

	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getObjectives() {
		return Objectives;
	}

	public void setObjectives(String objectives) {
		Objectives = objectives;
	}

	public Float getCategoryWeight() {
		return categoryWeight;
	}

	public void setCategoryWeight(Float categoryWeight) {
		this.categoryWeight = categoryWeight;
	}

	public Float getObjectiveWeight() {
		return objectiveWeight;
	}

	public void setObjectiveWeight(Float objectiveWeight) {
		this.objectiveWeight = objectiveWeight;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public Long getSelfRating() {
		return selfRating;
	}

	public void setSelfRating(Long selfRating) {
		this.selfRating = selfRating;
	}

	public Long getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(Long managerRating) {
		this.managerRating = managerRating;
	}

	public String getSelfQaulitativeAssisment() {
		return selfQaulitativeAssisment;
	}

	public void setSelfQaulitativeAssisment(String selfQaulitativeAssisment) {
		this.selfQaulitativeAssisment = selfQaulitativeAssisment;
	}

	public String getManagerQaulitativeAssisment() {
		return managerQaulitativeAssisment;
	}

	public void setManagerQaulitativeAssisment(String managerQaulitativeAssisment) {
		this.managerQaulitativeAssisment = managerQaulitativeAssisment;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public Long getKraCycleId() {
		return kraCycleId;
	}

	public void setKraCycleId(Long kraCycleId) {
		this.kraCycleId = kraCycleId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getIs_edit() {
		return is_edit;
	}

	public void setIs_edit(String is_edit) {
		this.is_edit = is_edit;
	}

	public String getIs_color() {
		return is_color;
	}

	public void setIs_color(String is_color) {
		this.is_color = is_color;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getHcdComment() {
		return hcdComment;
	}

	public void setHcdComment(String hcdComment) {
		this.hcdComment = hcdComment;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getRmAiComment() {
		return rmAiComment;
	}

	public void setRmAiComment(String rmAiComment) {
		this.rmAiComment = rmAiComment;
	}

}
