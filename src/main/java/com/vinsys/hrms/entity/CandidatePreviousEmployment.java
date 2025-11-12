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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_candidate_previous_employment")
public class CandidatePreviousEmployment extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_candidate_previous_employment", sequenceName = "seq_candidate_previous_employment", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_previous_employment")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_professional_detail_id", nullable = false)
	private CandidateProfessionalDetail candidateProfessionalDetail;
	@Column(name = "company_name")
	private String companyName;
	@Column(name = "company_address")
	private String companyAddress;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "city_id")
	private MasterCity city;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "state_id")
	private MasterState state;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "country_id")
	private MasterCountry country;
	@Column(name = "from_date")
	@Temporal(TemporalType.DATE)
	private Date fromDate;
	@Column(name = "to_date")
	@Temporal(TemporalType.DATE)
	private Date toDate;
	@Column(name = "experience")
	private String experience;
	@Column(name = "designation")
	private String designation;
	@Column(name = "job_type")
	private String jobType;
	@Column(name = "previous_manager")
	private String previousManager;
	@Column(name = "previous_manager_contact_number")
	private String previousManagerContactNumber;
	@Column(name = "reason_for_leaving")
	private String reasonForleaving;
	@Column(name = "overseas")
	private String overseas;
	@Column(name = "is_relevant")
	private String isRelevant;
	@Column(name = "previous_manager_email_id")
	private String previousManagerEmail;
	@Column(name = "employee_type")
	private String employeeType;
	@Column(name = "pincode")
	private Integer pincode;
	@Column(name = "contact_person_designation")
	private String contactPersonDesignation;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CandidateProfessionalDetail getCandidateProfessionalDetail() {
		return candidateProfessionalDetail;
	}

	public void setCandidateProfessionalDetail(CandidateProfessionalDetail candidateProfessionalDetail) {
		this.candidateProfessionalDetail = candidateProfessionalDetail;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public MasterCity getCity() {
		return city;
	}

	public void setCity(MasterCity city) {
		this.city = city;
	}

	public MasterState getState() {
		return state;
	}

	public void setState(MasterState state) {
		this.state = state;
	}

	public MasterCountry getCountry() {
		return country;
	}

	public void setCountry(MasterCountry country) {
		this.country = country;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getPreviousManager() {
		return previousManager;
	}

	public void setPreviousManager(String previousManager) {
		this.previousManager = previousManager;
	}

	public String getPreviousManagerContactNumber() {
		return previousManagerContactNumber;
	}

	public void setPreviousManagerContactNumber(String previousManagerContactNumber) {
		this.previousManagerContactNumber = previousManagerContactNumber;
	}

	public String getReasonForleaving() {
		return reasonForleaving;
	}

	public void setReasonForleaving(String reasonForleaving) {
		this.reasonForleaving = reasonForleaving;
	}

	public String getOverseas() {
		return overseas;
	}

	public void setOverseas(String overseas) {
		this.overseas = overseas;
	}

	public String getIsRelevant() {
		return isRelevant;
	}

	public void setIsRelevant(String isRelevant) {
		this.isRelevant = isRelevant;
	}

	public String getPreviousManagerEmail() {
		return previousManagerEmail;
	}

	public void setPreviousManagerEmail(String previousManagerEmail) {
		this.previousManagerEmail = previousManagerEmail;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getContactPersonDesignation() {
		return contactPersonDesignation;
	}

	public void setContactPersonDesignation(String contactPersonDesignation) {
		this.contactPersonDesignation = contactPersonDesignation;
	}

}
