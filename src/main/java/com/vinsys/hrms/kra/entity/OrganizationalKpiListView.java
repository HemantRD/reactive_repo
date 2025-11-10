package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vw_organizational_kpi_list")
public class OrganizationalKpiListView {

	@Id
	@Column(name = "id")
	private Long mapId;

	@Column(name = "kpi_objective")
	private String kpiObjective;

	@Column(name = "metric_weight")
	private Double metricWeight;

	@Column(name = "dep_id")
	private Long departmentId;

	@Column(name = "department_name")
	private String departmentName;

	@Column(name = "grade_id")
	private Long gradeId;

	@Column(name = "grade_description")
	private String gradeDesc;

	@Column(name = "year_id")
	private Long yearId;

	@Column(name = "year")
	private String year;

	@Column(name = "kpi_type_id")
	private Long KpiTypeId;

	@Column(name = "kpi_type")
	private String kpiType;

	@Column(name = "metric_type_id")
	private Long metricTypeId;

	@Column(name = "measurement_criteria")
	private String metricType;

	@Column(name = "org_id")
	private Long orgId;

	public Long getMapId() {
		return mapId;
	}

	public void setMapId(Long mapId) {
		this.mapId = mapId;
	}

	public String getKpiObjective() {
		return kpiObjective;
	}

	public void setKpiObjective(String kpiObjective) {
		this.kpiObjective = kpiObjective;
	}

	

	public Double getMetricWeight() {
		return metricWeight;
	}

	public void setMetricWeight(Double metricWeight) {
		this.metricWeight = metricWeight;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
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

	public String getGradeDesc() {
		return gradeDesc;
	}

	public void setGradeDesc(String gradeDesc) {
		this.gradeDesc = gradeDesc;
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
		return KpiTypeId;
	}

	public void setKpiTypeId(Long kpiTypeId) {
		KpiTypeId = kpiTypeId;
	}

	public String getKpiType() {
		return kpiType;
	}

	public void setKpiType(String kpiType) {
		this.kpiType = kpiType;
	}

	public Long getMetricTypeId() {
		return metricTypeId;
	}

	public void setMetricTypeId(Long metricTypeId) {
		this.metricTypeId = metricTypeId;
	}

	public String getMetricType() {
		return metricType;
	}

	public void setMetricType(String metricType) {
		this.metricType = metricType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
