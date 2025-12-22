package com.vinsys.hrms.kra.vo;

public class KpiQuestionRequestVO {

	private Long Id;
	private Long subcategoryId;
	private Long categoryId;
	private String questionName;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
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
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	
}
