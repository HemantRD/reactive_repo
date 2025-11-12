package com.vinsys.hrms.kra.vo;

import java.util.List;

public class KraCategoryVo {
	private Long kraCycleId;
	private Long kraId;
	private Long categoryId;
	private String name;   
	 private String categoryweight;
    private List<KraSubcategoryVo> subcategory;
    
   
	
	public String getCategoryweight() {
		return categoryweight;
	}
	public void setCategoryweight(String categoryweight) {
		this.categoryweight = categoryweight;
	}
	public List<KraSubcategoryVo> getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(List<KraSubcategoryVo> subcategory) {
		this.subcategory = subcategory;
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
	public Long getKraCycleId() {
		return kraCycleId;
	}
	public void setKraCycleId(Long kraCycleId) {
		this.kraCycleId = kraCycleId;
	}
	public Long getKraId() {
		return kraId;
	}
	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}

    
    
    
}
