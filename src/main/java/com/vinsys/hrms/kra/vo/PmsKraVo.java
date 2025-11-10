package com.vinsys.hrms.kra.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class PmsKraVo {

	
	@Schema(required = true,description = "This field will use only for approve and reject kra")
	private Long kraId;
	@Schema(required = true)
//	private List<CategoryVo> category;
	private Long categoryId;
	private String pendingWith;
	private String status;
	private Long kraCycleId;
	 private List<SubCategoryVo> subcategory;
	
	
	public Long getKraId() {
		return kraId;
	}
	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}
	
	public String getPendingWith() {
		return pendingWith;
	}
	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
//	public List<CategoryVo> getCategory() {
//		return category;
//	}
//	public void setCategory(List<CategoryVo> category) {
//		this.category = category;
//	}
	
	public Long getKraCycleId() {
		return kraCycleId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public void setKraCycleId(Long kraCycleId) {
		this.kraCycleId = kraCycleId;
	}
	public List<SubCategoryVo> getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(List<SubCategoryVo> subcategory) {
		this.subcategory = subcategory;
	}
	
	
	
}
