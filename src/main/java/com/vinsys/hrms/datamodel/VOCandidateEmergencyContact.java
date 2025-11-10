package com.vinsys.hrms.datamodel;

public class VOCandidateEmergencyContact extends VOAuditBase {

    private long id;
    private VOCandidatePersonalDetail candidatePersonalDetail;
    private String firstname;
    private String middlename;
    private String lastname;
    private String relationship;
    private String mobileNumber;
    private String landlineNumber;
    private VOCandidateEmergencyContactAddress candidateEmergencyContactAddress;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public VOCandidatePersonalDetail getCandidatePersonalDetail() {
	return candidatePersonalDetail;
    }

    public void setCandidatePersonalDetail(VOCandidatePersonalDetail candidatePersonalDetail) {
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

	public VOCandidateEmergencyContactAddress getCandidateEmergencyContactAddress() {
		return candidateEmergencyContactAddress;
	}

	public void setCandidateEmergencyContactAddress(VOCandidateEmergencyContactAddress candidateEmergencyContactAddress) {
		this.candidateEmergencyContactAddress = candidateEmergencyContactAddress;
	}

	   
}
