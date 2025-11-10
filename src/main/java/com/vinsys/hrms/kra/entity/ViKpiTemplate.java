package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Immutable
@Table(name = "vw_kpi_template")
@Entity
public class ViKpiTemplate {

	@Id
	@Column(name = "sr_no")
	private Long srNo;
	@Column(name = "emp_id")
	private Long empId;
	@Column(name = "grade_description")
	private String grade;
	@Column(name = "grade_id")
	private String gradeId;
	@Column(name = "department_id")
	private Long departmentId;
	@Column(name = "branch_id")
	private Long branchId;
	@Column(name = "division_id")
	private Long divisionId;
	@Column(name = "sub_category_id")
	private Long subCategoryId;
	@Column(name = "category_id")
	private Long categoryId;
	@Column(name = "objectives")
	private String objectives;
	@Column(name = "metrics")
	private String metrics;

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
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

	public String getObjectives() {
		return objectives;
	}

	public void setObjectives(String objectives) {
		this.objectives = objectives;
	}

	public Long getSrNo() {
		return srNo;
	}

	public void setSrNo(Long srNo) {
		this.srNo = srNo;
	}

	public String getMetrics() {
		return metrics;
	}

	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

}
