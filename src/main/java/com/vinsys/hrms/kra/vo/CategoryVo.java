package com.vinsys.hrms.kra.vo;

import java.util.List;

public class CategoryVo {
	private Long kraCycleId;
	private Long categoryId;
	private String name;
	private String categoryweight;
	private List<SubCategoryVo> subcategory;
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
	public String getCategoryweight() {
		return categoryweight;
	}
	public void setCategoryweight(String categoryweight) {
		this.categoryweight = categoryweight;
	}
	public List<SubCategoryVo> getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(List<SubCategoryVo> subcategory) {
		this.subcategory = subcategory;
	}
	public Long getKraCycleId() {
		return kraCycleId;
	}
	public void setKraCycleId(Long kraCycleId) {
		this.kraCycleId = kraCycleId;
	}
	
	
	
	

}
