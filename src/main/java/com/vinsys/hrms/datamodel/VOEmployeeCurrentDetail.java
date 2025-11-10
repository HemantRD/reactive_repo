package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOEmployeeCurrentDetail extends VOAuditBase {

	private static final long serialVersionUID = 1L;

	private long id;

	private VOEmployee employee;
	private String bandGrade;
	private String responsibility;
	private String process;
	private int noticePeriod;
	private String state;
	private String ptState;
	private String city;
	private String project;
	private String billable;
	private String costCenter;
	  @JsonFormat
	    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date retirementDate;
	private String housingStatus;
	private String confirmationStatus;
	private VOMasterWorkshift workshift;
	private VOEmployeeAdditionalRole employeeAdditionalRole;
	private long probationPeriod;
	private long empACN;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}

	public String getBandGrade() {
		return bandGrade;
	}

	public void setBandGrade(String bandGrade) {
		this.bandGrade = bandGrade;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public int getNoticePeriod() {
		return noticePeriod;
	}

	public void setNoticePeriod(int noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPtState() {
		return ptState;
	}

	public void setPtState(String ptState) {
		this.ptState = ptState;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getBillable() {
		return billable;
	}

	public void setBillable(String billable) {
		this.billable = billable;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public Date getRetirementDate() {
		return retirementDate;
	}

	public void setRetirementDate(Date retirementDate) {
		this.retirementDate = retirementDate;
	}

	public String getHousingStatus() {
		return housingStatus;
	}

	public void setHousingStatus(String housingStatus) {
		this.housingStatus = housingStatus;
	}

	public String getConfirmationStatus() {
		return confirmationStatus;
	}

	public void setConfirmationStatus(String confirmationStatus) {
		this.confirmationStatus = confirmationStatus;
	}

	public VOEmployeeAdditionalRole getEmployeeAdditionalRole() {
		return employeeAdditionalRole;
	}

	public void setEmployeeAdditionalRole(VOEmployeeAdditionalRole employeeAdditionalRole) {
		this.employeeAdditionalRole = employeeAdditionalRole;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public VOMasterWorkshift getWorkshift() {
		return workshift;
	}

	public void setWorkshift(VOMasterWorkshift workshift) {
		this.workshift = workshift;
	}

	public long getProbationPeriod() {
		return probationPeriod;
	}

	public void setProbationPeriod(long probationPeriod) {
		this.probationPeriod = probationPeriod;
	}

	public long getEmpACN() {
		return empACN;
	}

	public void setEmpACN(long empACN) {
		this.empACN = empACN;
	}
	
	

}
