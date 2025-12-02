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
@Table(name = "tbl_candidate_family_detail")
public class CandidateFamilyDetail extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_candidate_family_detail", sequenceName = "seq_candidate_family_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_family_detail")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_personal_detail_id", nullable = false)
	private CandidatePersonalDetail candidatePersonalDetail;
	@Column(name = "first_name")
	private String first_name;
	@Column(name = "middle_name")
	private String middle_name;
	@Column(name = "last_name")
	private String last_name;
	@Column(name = "relationship")
	private String relationship;
	@Column(name = "occupation")
	private String occupation;
	@Column(name = "date_of_birth")
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	@Column(name = "gender")
	private String gender;
	@Column(name = "working")
	private String working;
	@Column(name = "dependent")
	private String dependent;
	@Column(name = "dependent_severely_disabled")
	private String dependentSeverelyDisabled;
	@Column(name = "contact_no_1")
	private String contactNo1;
	@Column(name = "contact_no_2")
	private String contactNo2;
	@OneToOne(mappedBy = "candidateFamilyDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private CandidateFamilyAddress candidateFamilyAddress;

	@Column(name = "is_emergency_contact")
	private String isEmergencyContact;

	public CandidateFamilyAddress getCandidateFamilyAddress() {
		return candidateFamilyAddress;
	}

	public void setCandidateFamilyAddress(CandidateFamilyAddress candidateFamilyAddress) {
		this.candidateFamilyAddress = candidateFamilyAddress;
	}

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

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getWorking() {
		return working;
	}

	public void setWorking(String working) {
		this.working = working;
	}

	public String getDependent() {
		return dependent;
	}

	public void setDependent(String dependent) {
		this.dependent = dependent;
	}

	public String getDependentSeverelyDisabled() {
		return dependentSeverelyDisabled;
	}

	public void setDependentSeverelyDisabled(String dependentSeverelyDisabled) {
		this.dependentSeverelyDisabled = dependentSeverelyDisabled;
	}

	public String getContactNo1() {
		return contactNo1;
	}

	public void setContactNo1(String contactNo1) {
		this.contactNo1 = contactNo1;
	}

	public String getContactNo2() {
		return contactNo2;
	}

	public void setContactNo2(String contactNo2) {
		this.contactNo2 = contactNo2;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIsEmergencyContact() {
		return isEmergencyContact;
	}

	public void setIsEmergencyContact(String isEmergencyContact) {
		this.isEmergencyContact = isEmergencyContact;
	}

}

	


