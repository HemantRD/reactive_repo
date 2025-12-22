package com.vinsys.hrms.kra.vo;

import javax.persistence.Column;

public class KpiQuestionsVO {

	private Long Id;
	private String questionName;
	private Long questionCategory;
	private Long subcategoryId;
	private Long categoryId;
	private String isActive;
	private String categoryName;
	private String subcategoryName;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public Long getQuestionCategory() {
		return questionCategory;
	}

	public void setQuestionCategory(Long questionCategory) {
		this.questionCategory = questionCategory;
	}

	public Long getSubcategoryId() {
		return subcategoryId;
	}

	public void setSubcategoryId(Long subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSubcategoryName() {
		return subcategoryName;
	}

	public void setSubcategoryName(String subcategoryName) {
		this.subcategoryName = subcategoryName;
	}
	
	

}
