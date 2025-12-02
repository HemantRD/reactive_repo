package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.Organization;

@Entity
@Table(name = "tbl_trn_kra_history")
public class KraHistory {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_kra_history", sequenceName = "seq_kra_history", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_kra_history")
	@Column(name = "id")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "year")
	private KraYear kraYear;
	@Column(name = "self_rating")
	private float selfRating;
	@Column(name = "rm_rating")
	private float rmRating;
	@Column(name = "hod_rating")
	private float hodRating;
	@Column(name = "hr_rating")
	private float hrRating;
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@OneToOne(mappedBy = "kra")
	private KraWf kraWf;
	


	@Column(name = "employee_id", insertable = false, updatable = false)
	private Long employeeId;

	@ManyToOne
	@JoinColumn(name = "org_id",insertable = false, updatable = false)
	Organization organization;

	@ManyToOne
	@JoinColumn(name = "department")
	MasterDepartment department;
	@ManyToOne
	@JoinColumn(name = "branch")
	MasterBranch branch;
	@ManyToOne
	@JoinColumn(name = "division")
	MasterDivision division;
	
	@ManyToOne()
	@JoinColumn(name = "mst_kra_cycle_id")
	private KraCycle cycleId;

	@Column(name = "rm_final_rating")
	private String finalRating;
	
	@Column(name = "total_self_rating")
	private String totalSelfRating;
	
	@Column(name = "is_hr_calibrated")
	private String ishrcalibrate="N";
	
	@Column(name = "is_final_calibrated")
	private String isfinalcalibrate="N";
	
	
	@Column(name = "grade")
	private String grade;
	
	@Column(name = "department_name")
	private String departmentName;
	
	@Column(name = "designation")
	private String designation;
	
	@Column(name = "reporting_manager")
	private String reportingManager;
	
	@Column(name = "role")
	private String role;
	
	@Column(name = "kra_id")
	private Long kraId;
	
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getSelfRating() {
		return selfRating;
	}

	public void setSelfRating(float selfRating) {
		this.selfRating = selfRating;
	}

	public float getRmRating() {
		return rmRating;
	}

	public void setRmRating(float rmRating) {
		this.rmRating = rmRating;
	}

	public float getHodRating() {
		return hodRating;
	}

	public void setHodRating(float hodRating) {
		this.hodRating = hodRating;
	}

	public float getHrRating() {
		return hrRating;
	}

	public void setHrRating(float hrRating) {
		this.hrRating = hrRating;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public KraWf getKraWf() {
		return kraWf;
	}

	public void setKraWf(KraWf kraWf) {
		this.kraWf = kraWf;
	}

	public KraYear getKraYear() {
		return kraYear;
	}

	public void setKraYear(KraYear kraYear) {
		this.kraYear = kraYear;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public MasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(MasterDepartment department) {
		this.department = department;
	}

	public MasterBranch getBranch() {
		return branch;
	}

	public void setBranch(MasterBranch branch) {
		this.branch = branch;
	}

	public MasterDivision getDivision() {
		return division;
	}

	public void setDivision(MasterDivision division) {
		this.division = division;
	}

	public String getFinalRating() {
		return finalRating;
	}

	public void setFinalRating(String finalRating) {
		this.finalRating = finalRating;
	}

	public KraCycle getCycleId() {
		return cycleId;
	}

	public void setCycleId(KraCycle cycleId) {
		this.cycleId = cycleId;
	}

	public String getTotalSelfRating() {
		return totalSelfRating;
	}

	public void setTotalSelfRating(String totalSelfRating) {
		this.totalSelfRating = totalSelfRating;
	}

	public String getIshrcalibrate() {
		return ishrcalibrate;
	}

	public void setIshrcalibrate(String ishrcalibrate) {
		this.ishrcalibrate = ishrcalibrate;
	}

	public String getIsfinalcalibrate() {
		return isfinalcalibrate;
	}

	public void setIsfinalcalibrate(String isfinalcalibrate) {
		this.isfinalcalibrate = isfinalcalibrate;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	public Long getKraId() {
		return kraId;
	}

	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}
	

}
