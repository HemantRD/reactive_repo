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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_candidate_health_report")
public class CandidateHealthReport extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_candidate_health_report", sequenceName = "seq_candidate_health_report", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_health_report")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_personal_detail_id")
	private CandidatePersonalDetail candidatePersonalDetail;
	@Column(name = "blood_group")
	private String bloodGroup;
	@Column(name = "interested_to_donate_blood")
	private String interestedToDonateBlood;
	@Column(name = "physically_handicapped")
	private String physicallyHandicapped;
	@Column(name = "severely_handicapped")
	private String severelyHandicapped;
	@Column(name = "vision_problem")
	private String visionProblem;
	@Column(name = "surgery")
	private String surgery;
	@Column(name = "hospitalization")
	private String hospitalization;
	@Column(name = "allergy")
	private String allergy;
	@Column(name = "identification_mark")
	private String identificationMark;
	@Column(name = "health_history")
	private String healthHistory;

	@Column(name = "physically_handicap_description")
	private String physicallyHandicapDescription;

	@Column(name = "surgery_description")
	private String surgeryDescription;

	@Column(name = "allergy_description")
	private String allergyDescription;

	@Column(name = "health_history_description")
	private String healthHistoryDescription;

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

	public String getPhysicallyHandicapDescription() {
		return physicallyHandicapDescription;
	}

	public void setPhysicallyHandicapDescription(String physicallyHandicapDescription) {
		this.physicallyHandicapDescription = physicallyHandicapDescription;
	}

	public String getSurgeryDescription() {
		return surgeryDescription;
	}

	public void setSurgeryDescription(String surgeryDescription) {
		this.surgeryDescription = surgeryDescription;
	}

	public String getAllergyDescription() {
		return allergyDescription;
	}

	public void setAllergyDescription(String allergyDescription) {
		this.allergyDescription = allergyDescription;
	}

	public String getHealthHistoryDescription() {
		return healthHistoryDescription;
	}

	public void setHealthHistoryDescription(String healthHistoryDescription) {
		this.healthHistoryDescription = healthHistoryDescription;
	}

}
