package com.vinsys.hrms.kra.vo;

public class OrgKpiReportResponse {
	private Long id;
    private Long yearId;
	private String year;
	private Long kpiTypeId;	
	private String kpiType;	
	private Long deptId;
	private String departmentName;
	private Long gradeId;
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
	public Long getYearId() {
		return yearId;
	}
	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Long getKpiTypeId() {
		return kpiTypeId;
	}
	public void setKpiTypeId(Long kpiTypeId) {
		this.kpiTypeId = kpiTypeId;
	}
	public String getKpiType() {
		return kpiType;
	}
	public void setKpiType(String kpiType) {
		this.kpiType = kpiType;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Long getGradeId() {
		return gradeId;
	}
	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
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
	public Long getMetricId() {
		return metricId;
	}
	public void setMetricId(Long metricId) {
		this.metricId = metricId;
	}
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	
	
}
