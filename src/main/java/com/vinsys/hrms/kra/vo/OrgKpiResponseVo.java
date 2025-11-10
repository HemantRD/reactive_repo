package com.vinsys.hrms.kra.vo;

public class OrgKpiResponseVo {
    private Long id;
    private Long yearId;
	private String year;
	private Long kpiTypeId;	
	private String kpiType;	
	private String deptId;
	private String departmentName;
	private String gradeId;
	private String gradeDescription;
	private String objective;
	private Double objectiveWeight;
	private Long metricId;
	private String metric;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getKpiType() {
		return kpiType;
	}
	public void setKpiType(String kpiType) {
		this.kpiType = kpiType;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getGradeDescription() {
		return gradeDescription;
	}
	public void setGradeDescription(String gradeDescription) {
		this.gradeDescription = gradeDescription;
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
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	public Long getYearId() {
		return yearId;
	}
	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}
	public Long getKpiTypeId() {
		return kpiTypeId;
	}
	public void setKpiTypeId(Long kpiTypeId) {
		this.kpiTypeId = kpiTypeId;
	}
	public Long getMetricId() {
		return metricId;
	}
	public void setMetricId(Long metricId) {
		this.metricId = metricId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	
	
}
