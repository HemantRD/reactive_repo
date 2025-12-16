package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_map_grade_to_kpi")
public class GradeToStageMapping extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_map_grade_to_kpi", sequenceName = "seq_map_grade_to_kpi", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_grade_to_kpi")
	@Column(name = "id")
	
	private Long id;
	@Column(name = "sub_category")
	private String subCategoryName;
	@Column(name = "sub_category_id")
	private Long subCategoryId;
	@Column(name = "category_id")
	private Long categoryId;
	@Column(name = "grade_id")
	private Long gradeId;
	@Column(name = "objective_id")
	private Long objectiveId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getGradeId() {
		return gradeId;
	}

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}

	public Long getObjectiveId() {
		return objectiveId;
	}

	public void setObjectiveId(Long objectiveId) {
		this.objectiveId = objectiveId;
	}

}
