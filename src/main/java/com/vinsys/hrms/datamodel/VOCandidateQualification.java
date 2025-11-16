package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOCandidateQualification extends VOAuditBase {

    private long id;

    private VOCandidateProfessionalDetail candidateProfessionalDetail;
    private String qualification;
    private String degree;
    private String subjectOfSpecialization;
    private String instituteName;
    private String boardUniversity;
    private String modeOfEducation;
    private String stateLocation;
    private String gradeDivisionPercentage;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date passingYearMonth;
    private String academicAchievements;
    private String remarks;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public VOCandidateProfessionalDetail getCandidateProfessionalDetail() {
	return candidateProfessionalDetail;
    }

    public void setCandidateProfessionalDetail(VOCandidateProfessionalDetail candidateProfessionalDetail) {
	this.candidateProfessionalDetail = candidateProfessionalDetail;
    }

    public String getQualification() {
	return qualification;
    }

    public void setQualification(String qualification) {
	this.qualification = qualification;
    }

    public String getDegree() {
	return degree;
    }

    public void setDegree(String degree) {
	this.degree = degree;
    }

    public String getSubjectOfSpecialization() {
	return subjectOfSpecialization;
    }

    public void setSubjectOfSpecialization(String subjectOfSpecialization) {
	this.subjectOfSpecialization = subjectOfSpecialization;
    }

    public String getInstituteName() {
	return instituteName;
    }

    public void setInstituteName(String instituteName) {
	this.instituteName = instituteName;
    }

    public String getBoardUniversity() {
	return boardUniversity;
    }

    public void setBoardUniversity(String boardUniversity) {
	this.boardUniversity = boardUniversity;
    }

    public String getModeOfEducation() {
	return modeOfEducation;
    }

    public void setModeOfEducation(String modeOfEducation) {
	this.modeOfEducation = modeOfEducation;
    }

    public String getStateLocation() {
	return stateLocation;
    }

    public void setStateLocation(String stateLocation) {
	this.stateLocation = stateLocation;
    }

    public String getGradeDivisionPercentage() {
	return gradeDivisionPercentage;
    }

    public void setGradeDivisionPercentage(String gradeDivisionPercentage) {
	this.gradeDivisionPercentage = gradeDivisionPercentage;
    }

    public Date getPassingYearMonth() {
	return passingYearMonth;
    }

    public void setPassingYearMonth(Date passingYearMonth) {
	this.passingYearMonth = passingYearMonth;
    }

    public String getAcademicAchievements() {
	return academicAchievements;
    }

    public void setAcademicAchievements(String academicAchievements) {
	this.academicAchievements = academicAchievements;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

}
