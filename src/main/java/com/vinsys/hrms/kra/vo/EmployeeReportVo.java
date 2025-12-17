package com.vinsys.hrms.kra.vo;

import java.util.Date;

public class EmployeeReportVo {
	
	private Long id;
	private String tmId;
	private String employeeName;
	private String grade;
	private String designation;
	private String department;
	private String function;
	private String linemanager;
	private String kpiSubmission;
	private String halfYear;
	private String yearEnd;
	private Date joiningDate;
	public String getTmId() {
		return tmId;
	}
	public void setTmId(String tmId) {
		this.tmId = tmId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getLinemanager() {
		return linemanager;
	}
	public void setLinemanager(String linemanager) {
		this.linemanager = linemanager;
	}
	public String getKpiSubmission() {
		return kpiSubmission;
	}
	public void setKpiSubmission(String kpiSubmission) {
		this.kpiSubmission = kpiSubmission;
	}
	public String getHalfYear() {
		return halfYear;
	}
	public void setHalfYear(String halfYear) {
		this.halfYear = halfYear;
	}
	public String getYearEnd() {
		return yearEnd;
	}
	public void setYearEnd(String yearEnd) {
		this.yearEnd = yearEnd;
	}
	public Date getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
