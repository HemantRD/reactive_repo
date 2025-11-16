package com.vinsys.hrms.kra.vo;

import java.util.List;

public class KraSubcategoryVo {
	private Long subcategoryId;
	private String name; // Name of the subcategory
	private List<KraObjectiveVo> objectives;

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
	
}
