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
@Table(name = "tbl_candidate_policy_detail")
public class CandidatePolicyDetail extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_candidate_policy_detail", sequenceName = "seq_candidate_policy_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_policy_detail")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_personal_detail_id")
	private CandidatePersonalDetail candidatePersonalDetail;
	@Column(name = "insurance_type")
	private String insuranceType;
	@Column(name = "insurance_company")
	private String insuranceCompany;
	@Column(name = "policy_number")
	private String policyNumber;
	@Column(name = "policy_name")
	private String policyName;
	@Column(name = "tpa")
	private String tpa;
	@Column(name = "tpa_name")
	private String tpaName;
	@Column(name = "start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Column(name = "date_of_expiry")
	@Temporal(TemporalType.DATE)
	private Date dateOfExpiry;
	@Column(name = "sum_insured")
	private String sumInsured;
	@Column(name = "employee_membership_id")
	private String employeeMembershipId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CandidatePersonalDetail getCandidatePersonalDetail() {
		return candidatePersonalDetail;
	}

	public void setCandidatePersonalDetail(CandidatePersonalDetail candidatePersonalDetail) {
		this.candidatePersonalDetail = candidatePersonalDetail;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getTpa() {
		return tpa;
	}

	public void setTpa(String tpa) {
		this.tpa = tpa;
	}

	public String getTpaName() {
		return tpaName;
	}

	public void setTpaName(String tpaName) {
		this.tpaName = tpaName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDateOfExpiry() {
		return dateOfExpiry;
	}

	public void setDateOfExpiry(Date dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
	}

	public String getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}

	public String getEmployeeMembershipId() {
		return employeeMembershipId;
	}

	public void setEmployeeMembershipId(String employeeMembershipId) {
		this.employeeMembershipId = employeeMembershipId;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

}
