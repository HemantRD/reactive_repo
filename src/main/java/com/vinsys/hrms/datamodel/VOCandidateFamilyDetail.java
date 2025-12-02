package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vinsys.hrms.entity.AuditBase;

public class VOCandidateFamilyDetail extends AuditBase {

	private static final long serialVersionUID = 1L;
	private long id;
	private VOCandidatePersonalDetail candidatePersonalDetail;
	private String first_name;
	private String middle_name;
	private String last_name;
	private String relationship;
	private String occupation;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date dateOfBirth;
	private String gender;
	private String working;
	private String dependent;
	private String dependentSeverelyDisabled;
	private String contactNo1;
	private String contactNo2;
	private int age;
	private VOCandidateFamilyAddress candidateFamilyAddress;

	private String isEmergencyContact;

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

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getWorking() {
		return working;
	}

	public void setWorking(String working) {
		this.working = working;
	}

	public String getDependent() {
		return dependent;
	}

	public void setDependent(String dependent) {
		this.dependent = dependent;
	}

	public String getDependentSeverelyDisabled() {
		return dependentSeverelyDisabled;
	}

	public void setDependentSeverelyDisabled(String dependentSeverelyDisabled) {
		this.dependentSeverelyDisabled = dependentSeverelyDisabled;
	}

	public String getContactNo1() {
		return contactNo1;
	}

	public void setContactNo1(String contactNo1) {
		this.contactNo1 = contactNo1;
	}

	public String getContactNo2() {
		return contactNo2;
	}

	public void setContactNo2(String contactNo2) {
		this.contactNo2 = contactNo2;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public VOCandidateFamilyAddress getCandidateFamilyAddress() {
		return candidateFamilyAddress;
	}

	public void setCandidateFamilyAddress(VOCandidateFamilyAddress candidateFamilyAddress) {
		this.candidateFamilyAddress = candidateFamilyAddress;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIsEmergencyContact() {
		return isEmergencyContact;
	}

	public void setIsEmergencyContact(String isEmergencyContact) {
		this.isEmergencyContact = isEmergencyContact;
	}

}
