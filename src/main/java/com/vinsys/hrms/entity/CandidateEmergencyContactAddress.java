package com.vinsys.hrms.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_candidate_emergency_contact_address")
public class CandidateEmergencyContactAddress extends Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_candidate_emergency_contact_address", sequenceName = "seq_candidate_emergency_contact_address", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_emergency_contact_address")
	private long id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "candidate_emergency_contact_id")
	private CandidateEmergencyContact candidateEmergencyContact;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CandidateEmergencyContact getCandidateEmergencyContact() {
		return candidateEmergencyContact;
	}

	public void setCandidateEmergencyContact(CandidateEmergencyContact candidateEmergencyContact) {
		this.candidateEmergencyContact = candidateEmergencyContact;
	}

}
