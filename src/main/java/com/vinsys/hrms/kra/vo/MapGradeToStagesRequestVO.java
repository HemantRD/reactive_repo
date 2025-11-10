package com.vinsys.hrms.kra.vo;

import com.vinsys.hrms.master.vo.GradeMasterVo;

public class MapGradeToStagesRequestVO {

	private Long id;
	private GradeMasterVo grade;
	private MasterCategoryVO category;
	private MasterSubCategoryVO subCategory;
	private String keyword;
	

	public GradeMasterVo getGrade() {
		return grade;
	}

	public void setGrade(GradeMasterVo grade) {
		this.grade = grade;
	}

	public MasterCategoryVO getCategory() {
		return category;
	}

	public void setCategory(MasterCategoryVO category) {
		this.category = category;
	}

	public MasterSubCategoryVO getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(MasterSubCategoryVO subCategory) {
		this.subCategory = subCategory;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
