package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tbl_candidate_personal_detail")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CandidatePersonalDetail extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_candidate_personal_detail", sequenceName = "seq_candidate_personal_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_personal_detail")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;
	@OneToMany(mappedBy = "candidatePersonalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateEmergencyContact> candidateEmergencyContacts;
	@OneToMany(mappedBy = "candidatePersonalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateFamilyDetail> candidateFamilyDetails;
	@OneToOne(mappedBy = "candidatePersonalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private CandidateHealthReport candidateHealthReport;
	@OneToMany(mappedBy = "candidatePersonalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateLanguage> candidateLanguages;
	@OneToOne(mappedBy = "candidatePersonalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private CandidatePassportDetail candidatePassportDetail;
	@OneToOne(mappedBy = "candidatePersonalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private CandidatePolicyDetail candidatePolicyDetail;
	@OneToOne(mappedBy = "candidatePersonalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private CandidateStatutoryNomination candidateStatutoryNomination;
	@OneToMany(mappedBy = "candidatePersonalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateVisaDetail> candidateVisaDetails;
	@Column(name = "candidate_photo")
	private String candidatePhoto;
	@Column(name = "spouse_name")
	private String spouseName;
	@Column(name = "esi_no")
	private String esiNo;
	@Column(name = "esi_dispensary")
	private String esiDispensary;
	@Column(name = "marital_status")
	private String maritalStatus;
	@Column(name = "driving_license")
	private String drivingLicense;
	@Column(name = "official_mobile_number")
	private String officialMobileNumber;
	@Column(name = "sc_st_reservation_status")
	private String scStReservationStatus;
	@Column(name = "no_of_children")
	private String noOfChildren;
	@Column(name = "nationality")
	private String nationality;
	@Column(name = "citizenship")
	private String citizenship;
	@Column(name = "place_of_birth")
	private String placeOfBirth;
	@Column(name = "religion")
	private String religion;
	@Column(name = "mapped_imei", length = 20)
	private String mappedIMEI;

	public String getCandidatePhoto() {
		return candidatePhoto;
	}

	public void setCandidatePhoto(String candidatePhoto) {
		this.candidatePhoto = candidatePhoto;
	}

	public CandidateStatutoryNomination getCandidateStatutoryNomination() {
		return candidateStatutoryNomination;
	}

	public void setCandidateStatutoryNomination(CandidateStatutoryNomination candidateStatutoryNomination) {
		this.candidateStatutoryNomination = candidateStatutoryNomination;
	}

	public CandidatePolicyDetail getCandidatePolicyDetail() {
		return candidatePolicyDetail;
	}

	public void setCandidatePolicyDetail(CandidatePolicyDetail candidatePolicyDetail) {
		this.candidatePolicyDetail = candidatePolicyDetail;
	}

	public CandidatePassportDetail getCandidatePassportDetail() {
		return candidatePassportDetail;
	}

	public void setCandidatePassportDetail(CandidatePassportDetail candidatePassportDetail) {
		this.candidatePassportDetail = candidatePassportDetail;
	}

	public Set<CandidateEmergencyContact> getCandidateEmergencyContacts() {
		return candidateEmergencyContacts;
	}

	public void setCandidateEmergencyContacts(Set<CandidateEmergencyContact> candidateEmergencyContacts) {
		this.candidateEmergencyContacts = candidateEmergencyContacts;
	}

	public Set<CandidateFamilyDetail> getCandidateFamilyDetails() {
		return candidateFamilyDetails;
	}

	public void setCandidateFamilyDetails(Set<CandidateFamilyDetail> candidateFamilyDetails) {
		this.candidateFamilyDetails = candidateFamilyDetails;
	}

	public Set<CandidateLanguage> getCandidateLanguages() {
		return candidateLanguages;
	}

	public void setCandidateLanguages(Set<CandidateLanguage> candidateLanguages) {
		this.candidateLanguages = candidateLanguages;
	}

	public Set<CandidateVisaDetail> getCandidateVisaDetails() {
		return candidateVisaDetails;
	}

	public void setCandidateVisaDetails(Set<CandidateVisaDetail> candidateVisaDetails) {
		this.candidateVisaDetails = candidateVisaDetails;
	}

	public CandidateHealthReport getCandidateHealthReport() {
		return candidateHealthReport;
	}

	public void setCandidateHealthReport(CandidateHealthReport candidateHealthReport) {
		this.candidateHealthReport = candidateHealthReport;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

	public String getEsiNo() {
		return esiNo;
	}

	public void setEsiNo(String esiNo) {
		this.esiNo = esiNo;
	}

	public String getEsiDispensary() {
		return esiDispensary;
	}

	public void setEsiDispensary(String esiDispensary) {
		this.esiDispensary = esiDispensary;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public String getOfficialMobileNumber() {
		return officialMobileNumber;
	}

	public void setOfficialMobileNumber(String officialMobileNumber) {
		this.officialMobileNumber = officialMobileNumber;
	}

	public String getScStReservationStatus() {
		return scStReservationStatus;
	}

	public void setScStReservationStatus(String scStReservationStatus) {
		this.scStReservationStatus = scStReservationStatus;
	}

	public String getNoOfChildren() {
		return noOfChildren;
	}

	public void setNoOfChildren(String noOfChildren) {
		this.noOfChildren = noOfChildren;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getMappedIMEI() {
		return mappedIMEI;
	}

	public void setMappedIMEI(String mappedIMEI) {
		this.mappedIMEI = mappedIMEI;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
