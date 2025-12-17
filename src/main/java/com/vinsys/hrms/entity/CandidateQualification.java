package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_candidate_qualification")
public class CandidateQualification extends AuditBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_candidate_qualification", sequenceName = "seq_candidate_qualification", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_qualification")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_professional_detail_id", nullable = false)
	private CandidateProfessionalDetail candidateProfessionalDetail;
	@Column(name = "qualification")
	private String qualification;
	@Column(name = "degree")
	private String degree;
	@Column(name = "subject_of_specialization")
	private String subjectOfSpecialization;
	@Column(name = "institute_name")
	private String instituteName;
	@Column(name = "board_university")
	private String boardUniversity;
	@Column(name = "mode_of_education")
	private String modeOfEducation;
	@Column(name = "state_location")
	private String stateLocation;
	@Column(name = "grade_division_percentage")
	private String gradeDivisionPercentage;
	@Column(name = "passing_year_month")
	@Temporal(TemporalType.DATE)
	private Date passingYearMonth;
	@Column(name = "academic_achievements")
	private String academicAchievements;
	@Column(name = "remarks")
	private String remarks;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CandidateProfessionalDetail getCandidateProfessionalDetail() {
		return candidateProfessionalDetail;
	}

	public void setCandidateProfessionalDetail(CandidateProfessionalDetail candidateProfessionalDetail) {
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
