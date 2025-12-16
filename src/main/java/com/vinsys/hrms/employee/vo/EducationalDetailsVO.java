package com.vinsys.hrms.employee.vo;

/**
 * 
 * @author Onkar A.
 *
 */
public class EducationalDetailsVO {

	//private String qualification;
	private String degree;
	private String subjectOfSpecialization;
	private String instituteName;
	private String boardUniversity;
	private String modeOfEducation;
	private String stateLocation;
	private String gradeDivisionPercentage;
	private String passingYearMonth;
	private String academicAchievements;
	//private String remarks;
	private long qualificationId;
	private long candidateId;
	private long OrgId;

	/*
	 * public String getQualification() { return qualification; }
	 * 
	 * public void setQualification(String qualification) { this.qualification =
	 * qualification; }
	 */
	
	public String getDegree() {
		return degree;
	}

	public long getOrgId() {
		return OrgId;
	}

	public void setOrgId(long orgId) {
		OrgId = orgId;
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



	public String getPassingYearMonth() {
		return passingYearMonth;
	}

	public void setPassingYearMonth(String passingYearMonth) {
		this.passingYearMonth = passingYearMonth;
	}

	public String getAcademicAchievements() {
		return academicAchievements;
	}

	public void setAcademicAchievements(String academicAchievements) {
		this.academicAchievements = academicAchievements;
	}

	/*
	 * public String getRemarks() { return remarks; }
	 * 
	 * public void setRemarks(String remarks) { this.remarks = remarks; }
	 */

	public String getGradeDivisionPercentage() {
		return gradeDivisionPercentage;
	}

	public void setGradeDivisionPercentage(String gradeDivisionPercentage) {
		this.gradeDivisionPercentage = gradeDivisionPercentage;
	}

	public long getQualificationId() {
		return qualificationId;
	}

	public void setQualificationId(long qualificationId) {
		this.qualificationId = qualificationId;
	}

	public long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
	}
	
	
	
}
