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
@Table(name = "tbl_trn_question_answer")

public class QuestionAndAnswer extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_tbl_trn_question_answer", sequenceName = "seq_tbl_trn_question_answer", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tbl_trn_question_answer")

	@Column(name = "id")
	private Long id;
	
	@Column(name = "kra_id")
	private Long kraId;
	
	@Column(name = "question_id")
	private Long questionId;
	
	@Column(name = "sub_category_id")
	private Long subCategoryId;
	
	@Column(name = "category_id")
	private Long categoryId;
	
	@Column(name = "answer")
	private String answer;
	
	@Column(name = "previous_answer")
	private String previousAnswer;
	
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
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getPreviousAnswer() {
		return previousAnswer;
	}
	public void setPreviousAnswer(String previousAnswer) {
		this.previousAnswer = previousAnswer;
	}
	
	

}
