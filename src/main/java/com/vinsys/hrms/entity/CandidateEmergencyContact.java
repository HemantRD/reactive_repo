package com.vinsys.hrms.entity;

import java.io.Serializable;

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

@Entity
@Table(name = "tbl_candidate_emergency_contact")
public class CandidateEmergencyContact extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_candidate_emergency_contact", sequenceName = "seq_candidate_emergency_contact", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_emergency_contact")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_personal_detail_id", nullable = false)
	private CandidatePersonalDetail candidatePersonalDetail;
	@Column(name = "firstname")
	private String firstname;
	@Column(name = "middlename")
	private String middlename;
	@Column(name = "lastname")
	private String lastname;
	@Column(name = "relationship")
	private String relationship;
	@Column(name = "mobile_number")
	private String mobileNumber;
	@Column(name = "landline_number")
	private String landlineNumber;
	@OneToOne(mappedBy = "candidateEmergencyContact", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private CandidateEmergencyContactAddress candidateEmergencyContactAddress;

	public CandidateEmergencyContactAddress getCandidateEmergencyContactAddress() {
		return candidateEmergencyContactAddress;
	}

	public void setCandidateEmergencyContactAddress(CandidateEmergencyContactAddress candidateEmergencyContactAddress) {
		this.candidateEmergencyContactAddress = candidateEmergencyContactAddress;
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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getLandlineNumber() {
		return landlineNumber;
	}

	public void setLandlineNumber(String landlineNumber) {
		this.landlineNumber = landlineNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
