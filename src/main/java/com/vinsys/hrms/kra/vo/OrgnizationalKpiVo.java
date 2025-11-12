package com.vinsys.hrms.kra.vo;

import java.util.List;

import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.GradeMasterVo;
import com.vinsys.hrms.master.vo.KpiTypeMasterVo;

public class OrgnizationalKpiVo {
	private Long id;
	private KraYearVo year;
	private KpiTypeMasterVo kpiType;
	private List<DepartmentVO> department;
	private List<GradeMasterVo> grades;
	private String objective;
	private Double objectiveWeight;
	private KpiMetricVo metric;

	public KraYearVo getYear() {
		return year;
	}

	public void setYear(KraYearVo year) {
		this.year = year;
	}

	public KpiTypeMasterVo getKpiType() {
		return kpiType;
	}

	public void setKpiType(KpiTypeMasterVo kpiType) {
		this.kpiType = kpiType;
	}

	


	public List<DepartmentVO> getDepartment() {
		return department;
	}

	public void setDepartment(List<DepartmentVO> department) {
		this.department = department;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	

	

	public Double getObjectiveWeight() {
		return objectiveWeight;
	}

	public void setObjectiveWeight(Double objectiveWeight) {
		this.objectiveWeight = objectiveWeight;
	}

	public KpiMetricVo getMetric() {
		return metric;
	}

	public void setMetric(KpiMetricVo metric) {
		this.metric = metric;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<GradeMasterVo> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeMasterVo> grades) {
		this.grades = grades;
	}
	
	

}
