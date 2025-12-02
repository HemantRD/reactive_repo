package com.vinsys.hrms.kra.vo;

import com.vinsys.hrms.master.vo.GradeMasterVo;

public class MapGradeToStagesResponseVO {

    private Long id;
    private GradeMasterVo grade;
    private MasterCategoryVO category;
    private MasterSubCategoryVO subCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
