package com.vinsys.hrms.kra.vo;

import java.util.List;

public class PMSKraSubCategoryVo {

	private Long subcategoryId;
	private String name;
	private List<PMSObjectivesRequestVo> objectives;
	
	public Long getSubcategoryId() {
		return subcategoryId;
	}
	public void setSubcategoryId(Long subcategoryId) {
		this.subcategoryId = subcategoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PMSObjectivesRequestVo> getObjectives() {
		return objectives;
	}
	public void setObjectives(List<PMSObjectivesRequestVo> objectives) {
		this.objectives = objectives;
	}
	
	
	
}
