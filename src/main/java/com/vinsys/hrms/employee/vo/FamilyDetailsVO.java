package com.vinsys.hrms.employee.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Onkar A
 *
 * 
 */
public class FamilyDetailsVO {

	private String firstName;
	private String middleName;
	private String lastName;
	private String relationship;
	private String occupation;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private String dateOfBirth;
	private String gender;
	private String working;
	private String dependent;
	private String dependentSeverelyDisabled;
	private String contactNo1;
	private String contactNo2;
	private int age;
	private String isEmergencyContact;
	private long id;
	private Long candidateId;

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

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
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

	public String getIsEmergencyContact() {
		return isEmergencyContact;
	}

	public void setIsEmergencyContact(String isEmergencyContact) {
		this.isEmergencyContact = isEmergencyContact;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

}
