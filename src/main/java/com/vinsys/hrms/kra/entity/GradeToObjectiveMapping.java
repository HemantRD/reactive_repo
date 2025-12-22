package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.master.entity.Category;
import com.vinsys.hrms.master.entity.GradeMaster;
import com.vinsys.hrms.master.entity.KpiMetricMaster;
import com.vinsys.hrms.master.entity.KpiTypeMaster;

@Entity
@Table(name = "tbl_map_grade_to_objective")
public class GradeToObjectiveMapping extends AuditBase {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_map_grade_to_objective", sequenceName = "seq_map_grade_to_objective", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_grade_to_objective")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "grade_id")
	private GradeMaster grade;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private MasterDepartment department;

	@ManyToOne
	@JoinColumn(name = "year")
	private KraYear year;

	@Column(name = "kpi_objective")
	private String objective;

	@ManyToOne
	@JoinColumn(name = "kpi_type")
	private KpiTypeMaster kpiType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "metric_type")
	private KpiMetricMaster metricType;

	@Column(name = "metric_weight")
	private Double metricWeight;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GradeMaster getGrade() {
		return grade;
	}

	public void setGrade(GradeMaster grade) {
		this.grade = grade;
	}

	public MasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(MasterDepartment department) {
		this.department = department;
	}

	public KraYear getYear() {
		return year;
	}

	public void setYear(KraYear year) {
		this.year = year;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public Double getMetricWeight() {
		return metricWeight;
	}

	public void setMetricWeight(Double metricWeight) {
		this.metricWeight = metricWeight;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public KpiTypeMaster getKpiType() {
		return kpiType;
	}

	public void setKpiType(KpiTypeMaster kpiType) {
		this.kpiType = kpiType;
	}

	public KpiMetricMaster getMetricType() {
		return metricType;
	}

	public void setMetricType(KpiMetricMaster metricType) {
		this.metricType = metricType;
	}
	
	

}
