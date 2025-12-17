package com.vinsys.hrms.kra.vo;

public class QuestionAndAnswerRequestVO {

	private Long id;
	private Long kraId;
	private Long questionId;
	private Long categoryId;
	private Long subCategoryId;
	private String answer;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getKraId() {
		return kraId;
	}
	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
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
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	

}
