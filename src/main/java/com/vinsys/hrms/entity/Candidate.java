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

@Entity
@Table(name = "tbl_candidate")
public class Candidate extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_candidate", sequenceName = "seq_candidate", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate")
	private Long id;
	@Column(name = "title")
	private String title;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "middle_name")
	private String middleName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "gender")
	private String gender;
	@Column(name = "email_id")
	private String emailId;
	@Column(name = "date_of_birth")
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	@Column(name = "mobile_number")
	private Long mobileNumber;
	@OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateLetter> letters;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "login_entity_id")
	private LoginEntity loginEntity;
	@OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateAddress> candidateAddress;
	@OneToOne(mappedBy = "candidate", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private CandidatePersonalDetail candidatePersonalDetail;
	@OneToOne(mappedBy = "candidate", fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private CandidateProfessionalDetail candidateProfessionalDetail;
	@OneToOne(mappedBy = "candidate", fetch = FetchType.LAZY)
	private Employee employee;
	@OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CandidateActivity> candidateActivities;
	@OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
	private Set<CandidateActivityActionDetail> candidateActivityActionDetails;
	@Column(name = "candidate_status")
	private String candidateStatus;
	@Column(name = "candidate_employment_status")
	private String candidateEmploymentStatus;
	@Column(name = "candidate_activity_status")
	private String candidateActivityStatus;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employment_type_id")
	private MasterEmploymentType employmentType;
	@Column(name = "notice_period")
	private Integer noticePeriod;
	@OneToOne(mappedBy = "candidate", fetch = FetchType.LAZY)
	private BankDetails bank;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "title_id")
	private MasterTitle titleId;

	public Set<CandidateActivityActionDetail> getCandidateActivityActionDetails() {
		return candidateActivityActionDetails;
	}

	public void setCandidateActivityActionDetails(Set<CandidateActivityActionDetail> candidateActivityActionDetails) {
		this.candidateActivityActionDetails = candidateActivityActionDetails;
	}

	public Set<CandidateActivity> getCandidateActivities() {
		return candidateActivities;
	}

	public void setCandidateActivities(Set<CandidateActivity> candidateActivities) {
		this.candidateActivities = candidateActivities;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public CandidateProfessionalDetail getCandidateProfessionalDetail() {
		return candidateProfessionalDetail;
	}

	public void setCandidateProfessionalDetail(CandidateProfessionalDetail candidateProfessionalDetail) {
		this.candidateProfessionalDetail = candidateProfessionalDetail;
	}

	public CandidatePersonalDetail getCandidatePersonalDetail() {
		return candidatePersonalDetail;
	}

	public void setCandidatePersonalDetail(CandidatePersonalDetail candidatePersonalDetail) {
		this.candidatePersonalDetail = candidatePersonalDetail;
	}

	public LoginEntity getLoginEntity() {
		return loginEntity;
	}

	public void setLoginEntity(LoginEntity loginEntity) {
		this.loginEntity = loginEntity;
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

	public Set<CandidateLetter> getLetters() {
		return letters;
	}

	public void setLetters(Set<CandidateLetter> letters) {
		this.letters = letters;
	}

	public String getCandidateStatus() {
		return candidateStatus;
	}

	public void setCandidateStatus(String candidateStatus) {
		this.candidateStatus = candidateStatus;
	}

	public Set<CandidateAddress> getCandidateAddress() {
		return candidateAddress;
	}

	public void setCandidateAddress(Set<CandidateAddress> candidateAddress) {
		this.candidateAddress = candidateAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCandidateEmploymentStatus() {
		return candidateEmploymentStatus;
	}

	public void setCandidateEmploymentStatus(String candidateEmploymentStatus) {
		this.candidateEmploymentStatus = candidateEmploymentStatus;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCandidateActivityStatus() {
		return candidateActivityStatus;
	}

	public void setCandidateActivityStatus(String candidateActivityStatus) {
		this.candidateActivityStatus = candidateActivityStatus;
	}

	public MasterEmploymentType getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(MasterEmploymentType employmentType) {
		this.employmentType = employmentType;
	}
	public Integer getNoticePeriod() {
		return noticePeriod;
	}
	public void setNoticePeriod(Integer noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	public BankDetails getBank() {
		return bank;
	}

	public void setBank(BankDetails bank) {
		this.bank = bank;
	}

	public MasterTitle getTitleId() {
		return titleId;
	}

	public void setTitleId(MasterTitle titleId) {
		this.titleId = titleId;
	}
	
}
