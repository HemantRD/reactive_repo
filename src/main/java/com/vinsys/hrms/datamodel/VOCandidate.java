package com.vinsys.hrms.datamodel;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOCandidate extends VOAuditBase {

	private static final long serialVersionUID = 1L;
	private long id;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String gender;
	private String emailId;
	 @JsonFormat
	    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date dateOfBirth;
	private long mobileNumber;
	private VOLoginEntity loginEntity;
	private Set<VOCandidateAddress> candidateAddresses;
	private VOCandidatePersonalDetail candidatePersonalDetail;
	private VOCandidateProfessionalDetail candidateProfessionalDetail;
	private String candidateStatus;
	private Set<VOCandidateActivity> candidateActivities;
	private VOEmployee employee;
	private int age;
	private Set<VOCandidateLetter> letters;
	private String roleToBeCreated;
	private String candidateEmploymentStatus;
	private String candidateActivityStatus;
	private VOMasterEmploymentType employmentType;
	private Integer noticePeriod;
	private VOCandidateVisaDetail voCandidateVisaDetail;
	
	
	public VOMasterEmploymentType getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(VOMasterEmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	public VOEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}

	public long getId() {
		return id;
	}

	public Set<VOCandidateActivity> getCandidateActivities() {
		return candidateActivities;
	}

	public void setCandidateActivities(Set<VOCandidateActivity> candidateActivities) {
		this.candidateActivities = candidateActivities;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public VOLoginEntity getLoginEntity() {
		return loginEntity;
	}

	public void setLoginEntity(VOLoginEntity loginEntity) {
		this.loginEntity = loginEntity;
	}

	public Set<VOCandidateAddress> getCandidateAddresses() {
		return candidateAddresses;
	}

	public void setCandidateAddresses(Set<VOCandidateAddress> candidateAddresses) {
		this.candidateAddresses = candidateAddresses;
	}

	public VOCandidatePersonalDetail getCandidatePersonalDetail() {
		return candidatePersonalDetail;
	}

	public void setCandidatePersonalDetail(VOCandidatePersonalDetail candidatePersonalDetail) {
		this.candidatePersonalDetail = candidatePersonalDetail;
	}

	public VOCandidateProfessionalDetail getCandidateProfessionalDetail() {
		return candidateProfessionalDetail;
	}

	public void setCandidateProfessionalDetail(VOCandidateProfessionalDetail candidateProfessionalDetail) {
		this.candidateProfessionalDetail = candidateProfessionalDetail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCandidateStatus() {
		return candidateStatus;
	}

	public void setCandidateStatus(String candidateStatus) {
		this.candidateStatus = candidateStatus;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Set<VOCandidateLetter> getLetters() {
		return letters;
	}

	public void setLetters(Set<VOCandidateLetter> letters) {
		this.letters = letters;
	}

	public String getRoleToBeCreated() {
		return roleToBeCreated;
	}

	public void setRoleToBeCreated(String roleToBeCreated) {
		this.roleToBeCreated = roleToBeCreated;
	}

	public String getCandidateEmploymentStatus() {
		return candidateEmploymentStatus;
	}

	public void setCandidateEmploymentStatus(String candidateEmploymentStatus) {
		this.candidateEmploymentStatus = candidateEmploymentStatus;
	}

	public String getCandidateActivityStatus() {
		return candidateActivityStatus;
	}

	public void setCandidateActivityStatus(String candidateActivityStatus) {
		this.candidateActivityStatus = candidateActivityStatus;
	}
	public Integer getNoticePeriod() {
		return noticePeriod;
	}
	public void setNoticePeriod(Integer noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	public VOCandidateVisaDetail getVoCandidateVisaDetail() {
		return voCandidateVisaDetail;
	}

	public void setVoCandidateVisaDetail(VOCandidateVisaDetail voCandidateVisaDetail) {
		this.voCandidateVisaDetail = voCandidateVisaDetail;
	}
	
	
}
