package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vw_kra_details")
public class KraDetailsReport {
	@Id
	@Column(name = "kra_id")
	private Long kraId;
	@Column(name = "employee_id")
	private Long employeeId;
	@Column(name = "cycle_id")
	private Long cycle_id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "status")
	private String status;
	@Column(name = "pending_with")
	private String pendingWith;
	@Column(name = "department_name")
	private String departmentName;
	@Column(name = "department_id")
	private Long departmentId;
	@Column(name = "self_rating")
	private float selfRating;
	@Column(name = "rm_rating")
	private float rmRating;
	@Column(name = "hod_rating")
	private float hodRating;
	@Column(name = "hr_rating")
	private float hrRating;
	@Column(name = "year")
	private String year;
	@Column(name = "reporting_manager_id")
	private Long rmId;
	@Column(name = "emp_mail_id")
	private String empEmailId;
	@Column(name = "rm_first_name")
	private String rmFirstName;
	@Column(name = "rm_last_name")
	private String rmLastName;
	@Column(name = "country_id")
	private Long countryId;
	@Column(name = "country_name")
	private String countryName;
	
	@Column(name = "total_self_rating")
	private String totalSelfRating;
	
	@Column(name = "rm_final_rating")
	private String rmFinalRating;
	
	@Column(name = "hod_calibrated_rating")
	private String calibratedRating;
	
	@Column(name = "is_hr_calibrated")
	private String isHrCalibrated;
	
	@Column(name = "grade")
	private String grade;
	
	@Column(name = "cycle_name")
	private String cycleName;
	
	@Column(name = "employee_code")
	private String employee_code;
	
	@Column(name = "comment")
	private String comment;
	
	@Column(name = "hcd_correction")
	private String hcd_correction;
	
	@Column(name = "grade_id")
	private Long gradeId;

	@Column(name = "delegation_to")
	private Long delegationTo;
	
	@Column(name = "designation")
	private String designation;
	
	@Column(name = "reporting_manager")
	private String reportingManager;
	
	@Column(name = "role")
	private String role;
	
	@Column(name = "mst_cycle_type")
	private Long cycleType;
	
	@Column(name = "division_id")
	private Long divId;
	
	public Long getKraId() {
		return kraId;
	}

	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getCycle_id() {
		return cycle_id;
	}

	public void setCycle_id(Long cycle_id) {
		this.cycle_id = cycle_id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
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

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Long getRmId() {
		return rmId;
	}

	public void setRmId(Long rmId) {
		this.rmId = rmId;
	}

	public String getEmpEmailId() {
		return empEmailId;
	}

	public void setEmpEmailId(String empEmailId) {
		this.empEmailId = empEmailId;
	}

	public String getRmFirstName() {
		return rmFirstName;
	}

	public void setRmFirstName(String rmFirstName) {
		this.rmFirstName = rmFirstName;
	}

	public String getRmLastName() {
		return rmLastName;
	}

	public void setRmLastName(String rmLastName) {
		this.rmLastName = rmLastName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getTotalSelfRating() {
		return totalSelfRating;
	}

	public void setTotalSelfRating(String totalSelfRating) {
		this.totalSelfRating = totalSelfRating;
	}

	public String getRmFinalRating() {
		return rmFinalRating;
	}

	public void setRmFinalRating(String rmFinalRating) {
		this.rmFinalRating = rmFinalRating;
	}

	public String getCalibratedRating() {
		return calibratedRating;
	}

	public void setCalibratedRating(String calibratedRating) {
		this.calibratedRating = calibratedRating;
	}

	public String getIsHrCalibrated() {
		return isHrCalibrated;
	}

	public void setIsHrCalibrated(String isHrCalibrated) {
		this.isHrCalibrated = isHrCalibrated;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getEmployee_code() {
		return employee_code;
	}

	public void setEmployee_code(String employee_code) {
		this.employee_code = employee_code;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getHcd_correction() {
		return hcd_correction;
	}

	public void setHcd_correction(String hcd_correction) {
		this.hcd_correction = hcd_correction;
	}

	public Long getGradeId() {
		return gradeId;
	}

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}

	public Long getDelegationTo() {
		return delegationTo;
	}

	public void setDelegationTo(Long delegationTo) {
		this.delegationTo = delegationTo;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getCycleType() {
		return cycleType;
	}

	public void setCycleType(Long cycleType) {
		this.cycleType = cycleType;
	}

	public Long getDivId() {
		return divId;
	}

	public void setDivId(Long divId) {
		this.divId = divId;
	}
	
	
}
