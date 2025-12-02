package com.vinsys.hrms.datamodel;

public class VOCandidateStatutoryNomination extends VOAuditBase {

    private long id;
    private VOCandidatePersonalDetail candidatePersonalDetail;
    private String type;
    private String nomineeName;
    private String relationship;
    private String dateOfBirth;
    private String age;
    private String percentage;

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

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getNomineeName() {
	return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
	this.nomineeName = nomineeName;
    }

    public String getRelationship() {
	return relationship;
    }

    public void setRelationship(String relationship) {
	this.relationship = relationship;
    }

    public String getDateOfBirth() {
	return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
	this.dateOfBirth = dateOfBirth;
    }

    public String getAge() {
	return age;
    }

    public void setAge(String age) {
	this.age = age;
    }

    public String getPercentage() {
	return percentage;
    }

    public void setPercentage(String percentage) {
	this.percentage = percentage;
    }

}
