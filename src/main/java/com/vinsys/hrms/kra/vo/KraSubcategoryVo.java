package com.vinsys.hrms.kra.vo;

import java.util.List;

public class KraSubcategoryVo {
	private Long subcategoryId;
	private String name; // Name of the subcategory
	private List<KraObjectiveVo> objectives;
	private List<QuestionDetailsKraIdVO> questionDatils;
	private String isQuesionsSubmitted;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<KraObjectiveVo> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<KraObjectiveVo> objectives) {
		this.objectives = objectives;
	}

	public Long getSubcategoryId() {
		return subcategoryId;
	}

	public void setSubcategoryId(Long subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	public List<QuestionDetailsKraIdVO> getQuestionDatils() {
		return questionDatils;
	}

	public void setQuestionDatils(List<QuestionDetailsKraIdVO> questionDatils) {
		this.questionDatils = questionDatils;
	}

	public String getIsQuesionsSubmitted() {
		return isQuesionsSubmitted;
	}

	public void setIsQuesionsSubmitted(String isQuesionsSubmitted) {
		this.isQuesionsSubmitted = isQuesionsSubmitted;
	}
	
	

}
