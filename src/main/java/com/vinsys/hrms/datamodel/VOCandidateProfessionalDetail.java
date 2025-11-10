package com.vinsys.hrms.datamodel;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOCandidateProfessionalDetail extends VOAuditBase {

	private static final long serialVersionUID = 1L;
	private long id;
	private VOCandidate candidate;
	private VOMasterDepartment department;
	private VOMasterDivision division;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date requisitionDate;
	private VOMasterDesignation designation;
	private VOMasterBranch branch;
	private VOMasterBranch workingLocation;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date dateOfJoining;
	private VOEmployee recruiter;
	private String panCard;
	private String aadhaarCard;
	private String markLetterTo;
	private String comment;
	private Set<VOCandidateCertification> candidateCertifications;
	private Set<VOCandidateChecklist> candidateChecklists;
	private Set<VOCandidateOverseasExperience> candidateOverseasExperiences;
	private Set<VOCandidatePreviousEmployment> candidatePreviousEmployments;
	private Set<VOCandidateQualification> candidateQualifications;
	private String reported;
	private Date dateOfReported;
	private Date shortlistDate;
	private String hrStatus;
	private String status;
	private String uan;
	private VOMasterCity city;
	private VOMasterState state;
	private VOMasterCountry country;
	private String emiratesID;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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

	public Date getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(Date requisitionDate) {
		this.requisitionDate = requisitionDate;
	}

	public VOMasterBranch getBranch() {
		return branch;
	}

	public void setBranch(VOMasterBranch branch) {
		this.branch = branch;
	}

	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getPanCard() {
		return panCard;
	}

	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}

	public String getAadhaarCard() {
		return aadhaarCard;
	}

	public void setAadhaarCard(String aadhaarCard) {
		this.aadhaarCard = aadhaarCard;
	}

	public String getMarkLetterTo() {
		return markLetterTo;
	}

	public void setMarkLetterTo(String markLetterTo) {
		this.markLetterTo = markLetterTo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<VOCandidateCertification> getCandidateCertifications() {
		return candidateCertifications;
	}

	public void setCandidateCertifications(Set<VOCandidateCertification> candidateCertifications) {
		this.candidateCertifications = candidateCertifications;
	}

	public Set<VOCandidateChecklist> getCandidateChecklists() {
		return candidateChecklists;
	}

	public void setCandidateChecklists(Set<VOCandidateChecklist> candidateChecklists) {
		this.candidateChecklists = candidateChecklists;
	}

	public Set<VOCandidateOverseasExperience> getCandidateOverseasExperiences() {
		return candidateOverseasExperiences;
	}

	public void setCandidateOverseasExperiences(Set<VOCandidateOverseasExperience> candidateOverseasExperiences) {
		this.candidateOverseasExperiences = candidateOverseasExperiences;
	}

	public Set<VOCandidatePreviousEmployment> getCandidatePreviousEmployments() {
		return candidatePreviousEmployments;
	}

	public void setCandidatePreviousEmployments(Set<VOCandidatePreviousEmployment> candidatePreviousEmployments) {
		this.candidatePreviousEmployments = candidatePreviousEmployments;
	}

	public Set<VOCandidateQualification> getCandidateQualifications() {
		return candidateQualifications;
	}

	public void setCandidateQualifications(Set<VOCandidateQualification> candidateQualifications) {
		this.candidateQualifications = candidateQualifications;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public VOMasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(VOMasterDepartment department) {
		this.department = department;
	}

	public VOMasterDivision getDivision() {
		return division;
	}

	public void setDivision(VOMasterDivision division) {
		this.division = division;
	}

	public VOMasterDesignation getDesignation() {
		return designation;
	}

	public void setDesignation(VOMasterDesignation designation) {
		this.designation = designation;
	}

	public String getReported() {
		return reported;
	}

	public void setReported(String reported) {
		this.reported = reported;
	}

	public Date getDateOfReported() {
		return dateOfReported;
	}

	public void setDateOfReported(Date dateOfReported) {
		this.dateOfReported = dateOfReported;
	}

	public Date getShortlistDate() {
		return shortlistDate;
	}

	public void setShortlistDate(Date shortlistDate) {
		this.shortlistDate = shortlistDate;
	}

	public String getHrStatus() {
		return hrStatus;
	}

	public void setHrStatus(String hrStatus) {
		this.hrStatus = hrStatus;
	}

	public String getUan() {
		return uan;
	}

	public void setUan(String uan) {
		this.uan = uan;
	}

	public VOMasterCity getCity() {
		return city;
	}

	public void setCity(VOMasterCity city) {
		this.city = city;
	}

	public VOMasterState getState() {
		return state;
	}

	public void setState(VOMasterState state) {
		this.state = state;
	}

	public VOMasterCountry getCountry() {
		return country;
	}

	public void setCountry(VOMasterCountry country) {
		this.country = country;
	}

	public VOEmployee getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(VOEmployee recruiter) {
		this.recruiter = recruiter;
	}

	public String getEmiratesID() {
		return emiratesID;
	}

	public void setEmiratesID(String emiratesID) {
		this.emiratesID = emiratesID;
	}

	public VOMasterBranch getWorkingLocation() {
		return workingLocation;
	}

	public void setWorkingLocation(VOMasterBranch workingLocation) {
		this.workingLocation = workingLocation;
	}

}
