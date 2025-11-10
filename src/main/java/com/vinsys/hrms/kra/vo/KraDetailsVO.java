package com.vinsys.hrms.kra.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public class KraDetailsVO {
	@Schema(required = true,description = "This field will use only approve/reject and save/submit(first time not required if he want to update then required")
	private Long id;
	private String year;
	@Schema(required = true)
	private Float weightage;
	@Schema(required = true)
	private String kraDetails;
	@Schema(required = true)
	private String description;
	@Schema(required = true)
	private String measurementCriteria;
	@Schema(required = true)
	private String achievementPlan;
	@Schema(required = true,description = "while approve and reject")
	private String rmComment;
	@Schema(required = true)
	private Long categoryId;
	@Schema(required = true)
	private Long subcategoryId;
	@Schema(required = true)
	private Float objectiveWeightage;
	@Schema(required = true)
	private String selfRating;
	@Schema(required = true)
	private String managerRating;
	@Schema(required = true)
	private String selfQaulitativeAssisment;
	@Schema(required = true)
	private String managerQaulitativeAssisment;
	
	
	public String getKraDetails() {
		return kraDetails;
	}

	public void setKraDetails(String kraDetails) {
		this.kraDetails = kraDetails;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMeasurementCriteria() {
		return measurementCriteria;
	}

	public void setMeasurementCriteria(String measurementCriteria) {
		this.measurementCriteria = measurementCriteria;
	}

	public String getAchievementPlan() {
		return achievementPlan;
	}

	public void setAchievementPlan(String achievementPlan) {
		this.achievementPlan = achievementPlan;
	}

	public String getRmComment() {
		return rmComment;
	}

	public void setRmComment(String rmComment) {
		this.rmComment = rmComment;
	}

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

	public Float getWeightage() {
		return weightage;
	}

	public void setWeightage(Float weightage) {
		this.weightage = weightage;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getSubcategoryId() {
		return subcategoryId;
	}

	public void setSubcategoryId(Long subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	public Float getObjectiveWeightage() {
		return objectiveWeightage;
	}

	public void setObjectiveWeightage(Float objectiveWeightage) {
		this.objectiveWeightage = objectiveWeightage;
	}

	public String getSelfRating() {
		return selfRating;
	}

	public void setSelfRating(String selfRating) {
		this.selfRating = selfRating;
	}

	public String getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(String managerRating) {
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

}
