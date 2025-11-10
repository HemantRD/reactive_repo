package com.vinsys.hrms.kra.vo;

import java.util.List;

public class PMSKraCategoryVo {
	private Long kraCycleId;
	private Long categoryId;
	private String name;
	private List<PMSKraSubCategoryVo> subcategory;
	
	public Long getKraCycleId() {
		return kraCycleId;
	}
	public void setKraCycleId(Long kraCycleId) {
		this.kraCycleId = kraCycleId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PMSKraSubCategoryVo> getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(List<PMSKraSubCategoryVo> subcategory) {
		this.subcategory = subcategory;
	}
	
	
	
}
