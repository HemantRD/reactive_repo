package com.vinsys.hrms.datamodel;

import java.util.Set;

public class VOCandidatePersonalDetail {

	private static final long serialVersionUID = 1L;
	private long id;
	private VOCandidate candidate;
	private Set<VOCandidateEmergencyContact> candidateEmergencyContacts;
	private Set<VOCandidateFamilyDetail> candidateFamilyDetails;
	private VOCandidateHealthReport candidateHealthReport;
	private Set<VOCandidateLanguage> candidateLanguages;
	private VOCandidatePassportDetail candidatePassportDetail;
	private VOCandidatePolicyDetail candidatePolicyDetail;
	private VOCandidateStatutoryNomination candidateStatutoryNomination;
	private Set<VOCandidateVisaDetail> candidateVisaDetails;
	private String candidatePhoto;
	private String spouseName;
	private String esiNo;
	private String esiDispensary;
	private String maritalStatus;
	private String drivingLicense;
	private String officialMobileNumber;
	private String scStReservationStatus;
	private String noOfChildren;
	private String nationality;
	private String citizenship;
	private String placeOfBirth;
	private String religion;
	private String mappedIMEI;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOCandidate getCandidate() {
		return candidate;
	}

	public void setCandidate(VOCandidate candidate) {
		this.candidate = candidate;
	}

	public Set<VOCandidateEmergencyContact> getCandidateEmergencyContacts() {
		return candidateEmergencyContacts;
	}

	public void setCandidateEmergencyContacts(Set<VOCandidateEmergencyContact> candidateEmergencyContacts) {
		this.candidateEmergencyContacts = candidateEmergencyContacts;
	}

	public Set<VOCandidateFamilyDetail> getCandidateFamilyDetails() {
		return candidateFamilyDetails;
	}

	public void setCandidateFamilyDetails(Set<VOCandidateFamilyDetail> candidateFamilyDetails) {
		this.candidateFamilyDetails = candidateFamilyDetails;
	}

	public VOCandidateHealthReport getCandidateHealthReport() {
		return candidateHealthReport;
	}

	public void setCandidateHealthReport(VOCandidateHealthReport candidateHealthReport) {
		this.candidateHealthReport = candidateHealthReport;
	}

	public Set<VOCandidateLanguage> getCandidateLanguages() {
		return candidateLanguages;
	}

	public void setCandidateLanguages(Set<VOCandidateLanguage> candidateLanguages) {
		this.candidateLanguages = candidateLanguages;
	}

	public VOCandidatePassportDetail getCandidatePassportDetail() {
		return candidatePassportDetail;
	}

	public void setCandidatePassportDetail(VOCandidatePassportDetail candidatePassportDetail) {
		this.candidatePassportDetail = candidatePassportDetail;
	}

	public VOCandidatePolicyDetail getCandidatePolicyDetail() {
		return candidatePolicyDetail;
	}

	public void setCandidatePolicyDetail(VOCandidatePolicyDetail candidatePolicyDetail) {
		this.candidatePolicyDetail = candidatePolicyDetail;
	}

	public VOCandidateStatutoryNomination getCandidateStatutoryNomination() {
		return candidateStatutoryNomination;
	}

	public void setCandidateStatutoryNomination(VOCandidateStatutoryNomination candidateStatutoryNomination) {
		this.candidateStatutoryNomination = candidateStatutoryNomination;
	}

	public Set<VOCandidateVisaDetail> getCandidateVisaDetails() {
		return candidateVisaDetails;
	}

	public void setCandidateVisaDetails(Set<VOCandidateVisaDetail> candidateVisaDetails) {
		this.candidateVisaDetails = candidateVisaDetails;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCandidatePhoto() {
		return candidatePhoto;
	}

	public void setCandidatePhoto(String candidatePhoto) {
		this.candidatePhoto = candidatePhoto;
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

}
