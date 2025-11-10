package com.vinsys.hrms.kra.vo;

import javax.validation.constraints.NotBlank;

public class SubCategoryRequestVo {
	
	private Long id;

	@NotBlank(message = "SubCategory name is mandatory")
	private String subCategoryname;
	
	@NotBlank(message = "Stage name is mandatory")
	private String stageName;
	
	@NotBlank(message = "Stage ID  is mandatory")
	private Long stageId;

	@NotBlank(message = "Category ID  is mandatory")
	private Long categoryId;
	
	private String isActive;
	
	private String keyword;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getSubCategoryname() {
		return subCategoryname;
	}

	public void setSubCategoryname(String subCategoryname) {
		this.subCategoryname = subCategoryname;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
