package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vinsys.hrms.master.entity.GradeMaster;

@Entity
@Table(name = "tbl_candidate_professional_detail")
public class CandidateProfessionalDetail extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_candidate_professional_detail", sequenceName = "seq_candidate_professional_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_professional_detail")
	private Long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;
	@Column(name = "requisition_date")
	private Date requisitionDate;
	@Column(name = "date_of_joining")
	@Temporal(TemporalType.DATE)
	private Date dateOfJoining;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "recruiter_id")
	private Employee recruiter;
	@Column(name = "pan_card")
	private String panCard;
	@Column(name = "aadhaar_card")
	private String aadhaarCard;
	@Column(name = "mark_letter_to")
	private String markLetterTo;
	@Column(name = "comment")
	private String comment;
	@OneToMany(mappedBy = "candidateProfessionalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateCertification> candidateCertifications;
	@OneToMany(mappedBy = "candidateProfessionalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateChecklist> candidateChecklists;
	@OneToMany(mappedBy = "candidateProfessionalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateOverseasExperience> candidateOverseasExperiences;
	@OneToMany(mappedBy = "candidateProfessionalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidatePreviousEmployment> candidatePreviousEmployments;
	@OneToMany(mappedBy = "candidateProfessionalDetail", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateQualification> candidateQualifications;
	@Column(name = "reported")
	private String reported;
	@Column(name = "date_of_reported")
	@Temporal(TemporalType.DATE)
	private Date dateOfReported;
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "designation_id")
	private MasterDesignation designation;
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "department_id")
	private MasterDepartment department;
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "division_id")
	private MasterDivision division;
	@Column(name = "shortlist_date")
	@Temporal(TemporalType.DATE)
	private Date shortlistDate;
	@Column(name = "status")
	private String status;
	@Column(name = "hr_status")
	private String hrStatus;
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "branch_id")
	private MasterBranch branch;
	@Column(name = "uan")
	private String uan;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "city_id")
	private MasterCity city;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "state_id")
	private MasterState state;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "country_id")
	private MasterCountry country;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "working_location")
	private MasterBranch workingLocation;

	@Column(name = "emirates_id")
	private String emiratesId;

	@Column(name = "grade")
	private String grade;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "grade_id")
	private GradeMaster gradeId;
	
	public Set<CandidateCertification> getCandidateCertifications() {
		return candidateCertifications;
	}

	public void setCandidateCertifications(Set<CandidateCertification> candidateCertifications) {
		this.candidateCertifications = candidateCertifications;
	}

	public Set<CandidateChecklist> getCandidateChecklists() {
		return candidateChecklists;
	}

	public void setCandidateChecklists(Set<CandidateChecklist> candidateChecklists) {
		this.candidateChecklists = candidateChecklists;
	}

	public Set<CandidateOverseasExperience> getCandidateOverseasExperiences() {
		return candidateOverseasExperiences;
	}

	public void setCandidateOverseasExperiences(Set<CandidateOverseasExperience> candidateOverseasExperiences) {
		this.candidateOverseasExperiences = candidateOverseasExperiences;
	}

	public Set<CandidatePreviousEmployment> getCandidatePreviousEmployments() {
		return candidatePreviousEmployments;
	}

	public void setCandidatePreviousEmployments(Set<CandidatePreviousEmployment> candidatePreviousEmployments) {
		this.candidatePreviousEmployments = candidatePreviousEmployments;
	}

	public Set<CandidateQualification> getCandidateQualifications() {
		return candidateQualifications;
	}

	public void setCandidateQualifications(Set<CandidateQualification> candidateQualifications) {
		this.candidateQualifications = candidateQualifications;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public Date getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(Date requisitionDate) {
		this.requisitionDate = requisitionDate;
	}

	public MasterBranch getBranch() {
		return branch;
	}

	public void setBranch(MasterBranch branch) {
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

	public MasterDesignation getDesignation() {
		return designation;
	}

	public void setDesignation(MasterDesignation designation) {
		this.designation = designation;
	}

	public MasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(MasterDepartment department) {
		this.department = department;
	}

	public MasterDivision getDivision() {
		return division;
	}

	public void setDivision(MasterDivision division) {
		this.division = division;
	}

	public Date getShortlistDate() {
		return shortlistDate;
	}

	public void setShortlistDate(Date shortlistDate) {
		this.shortlistDate = shortlistDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public MasterCity getCity() {
		return city;
	}

	public void setCity(MasterCity city) {
		this.city = city;
	}

	public MasterState getState() {
		return state;
	}

	public void setState(MasterState state) {
		this.state = state;
	}

	public MasterCountry getCountry() {
		return country;
	}

	public void setCountry(MasterCountry country) {
		this.country = country;
	}

	public Employee getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(Employee recruiter) {
		this.recruiter = recruiter;
	}

	public String getEmiratesId() {
		return emiratesId;
	}

	public void setEmiratesId(String emiratesId) {
		this.emiratesId = emiratesId;
	}

	public MasterBranch getWorkingLocation() {
		return workingLocation;
	}

	public void setWorkingLocation(MasterBranch workingLocation) {
		this.workingLocation = workingLocation;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public GradeMaster getGradeId() {
		return gradeId;
	}

	public void setGradeId(GradeMaster gradeId) {
		this.gradeId = gradeId;
	}

}
