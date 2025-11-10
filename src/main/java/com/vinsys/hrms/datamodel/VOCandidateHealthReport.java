package com.vinsys.hrms.datamodel;

public class VOCandidateHealthReport extends VOAuditBase {

    private long id;
    private VOCandidatePersonalDetail candidatePersonalDetail;
    private String bloodGroup;
    private String interestedToDonateBlood;
    private String physicallyHandicapped;
    private String severelyHandicapped;
    private String visionProblem;
    private String surgery;
    private String hospitalization;
    private String allergy;
    private String identificationMark;
    private String healthHistory;
    private String esic;
    private String physicallyHandicapDiscription;
    private String surgeryDiscription;
    private String allergyDiscription;
    private String healthHistoryDiscription;

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

    public String getBloodGroup() {
	return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
	this.bloodGroup = bloodGroup;
    }

    public String getInterestedToDonateBlood() {
	return interestedToDonateBlood;
    }

    public void setInterestedToDonateBlood(String interestedToDonateBlood) {
	this.interestedToDonateBlood = interestedToDonateBlood;
    }

    public String getPhysicallyHandicapped() {
	return physicallyHandicapped;
    }

    public void setPhysicallyHandicapped(String physicallyHandicapped) {
	this.physicallyHandicapped = physicallyHandicapped;
    }

    public String getSeverelyHandicapped() {
	return severelyHandicapped;
    }

    public void setSeverelyHandicapped(String severelyHandicapped) {
	this.severelyHandicapped = severelyHandicapped;
    }

    public String getVisionProblem() {
	return visionProblem;
    }

    public void setVisionProblem(String visionProblem) {
	this.visionProblem = visionProblem;
    }

    public String getSurgery() {
	return surgery;
    }

    public void setSurgery(String surgery) {
	this.surgery = surgery;
    }

    public String getHospitalization() {
	return hospitalization;
    }

    public void setHospitalization(String hospitalization) {
	this.hospitalization = hospitalization;
    }

    public String getAllergy() {
	return allergy;
    }

    public void setAllergy(String allergy) {
	this.allergy = allergy;
    }

    public String getIdentificationMark() {
	return identificationMark;
    }

    public void setIdentificationMark(String identificationMark) {
	this.identificationMark = identificationMark;
    }
    
    public String getHealthHistory() {
		return healthHistory;
	}
    public void setHealthHistory(String healthHistory) {
		this.healthHistory = healthHistory;
	}

	public String getEsic() {
		return esic;
	}

	public void setEsic(String esic) {
		this.esic = esic;
	}

	public String getPhysicallyHandicapDiscription() {
		return physicallyHandicapDiscription;
	}

	public void setPhysicallyHandicapDiscription(String physicallyHandicapDiscription) {
		this.physicallyHandicapDiscription = physicallyHandicapDiscription;
	}

	public String getSurgeryDiscription() {
		return surgeryDiscription;
	}

	public void setSurgeryDiscription(String surgeryDiscription) {
		this.surgeryDiscription = surgeryDiscription;
	}

	public String getAllergyDiscription() {
		return allergyDiscription;
	}

	public void setAllergyDiscription(String allergyDiscription) {
		this.allergyDiscription = allergyDiscription;
	}

	public String getHealthHistoryDiscription() {
		return healthHistoryDiscription;
	}

	public void setHealthHistoryDiscription(String healthHistoryDiscription) {
		this.healthHistoryDiscription = healthHistoryDiscription;
	}
    
}
