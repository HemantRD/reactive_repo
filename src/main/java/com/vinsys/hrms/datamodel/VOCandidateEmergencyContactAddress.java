package com.vinsys.hrms.datamodel;

import java.io.Serializable;


public class VOCandidateEmergencyContactAddress extends VOAddress implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private VOCandidateEmergencyContact candidateEmergencyContact;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public VOCandidateEmergencyContact getCandidateEmergencyContact() {
		return candidateEmergencyContact;
	}
	public void setCandidateEmergencyContact(VOCandidateEmergencyContact candidateEmergencyContact) {
		this.candidateEmergencyContact = candidateEmergencyContact;
	}


}
