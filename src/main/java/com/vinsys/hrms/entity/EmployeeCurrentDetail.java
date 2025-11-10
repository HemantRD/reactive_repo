package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_employee_current_detail")
public class EmployeeCurrentDetail extends AuditBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_employee_current_detail", sequenceName = "seq_employee_current_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_current_detail")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id")
	private Employee employee;
	@Column(name = "band_grade")
	private String bandGrade;
	@Column(name = "responsibility")
	private String responsibility;
	@Column(name = "process")
	private String process;
	@Column(name = "notice_period")
	private int noticePeriod;
	@Column(name = "state")
	private String state;
	@Column(name = "ptState")
	private String ptState;
	@Column(name = "city")
	private String city;
	@Column(name = "project")
	private String project;
	@Column(name = "billable")
	private String billable;
	@Column(name = "cost_center")
	private String costCenter;
	@Column(name = "retirement_date")
	@Temporal(TemporalType.DATE)
	private Date retirementDate;
	@Column(name = "housing_status")
	private String housingStatus;
	@Column(name = "confirmation_status")
	private String confirmationStatus;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "workshift_id")
	private MasterWorkshift workshift;
	@OneToOne(mappedBy = "employeeCurrentDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeAdditionalRole employeeAdditionalRole;

	public EmployeeAdditionalRole getEmployeeAdditionalRole() {
		return employeeAdditionalRole;
	}

	public void setEmployeeAdditionalRole(EmployeeAdditionalRole employeeAdditionalRole) {
		this.employeeAdditionalRole = employeeAdditionalRole;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
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

	public MasterWorkshift getWorkshift() {
		return workshift;
	}

	public void setWorkshift(MasterWorkshift workshift) {
		this.workshift = workshift;
	}

}
