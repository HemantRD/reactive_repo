package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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

import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.entity.confirmation.ProbationFeedback;

@Entity
@Table(name = "tbl_employee")
public class Employee extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_employee", sequenceName = "seq_employee", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee")
	private Long id;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;
	
	/*@Column(name="emp_management")
	private boolean isManagement;
	@Column(name="emp_flexible")
	private boolean isFlexible;*/
	
	@Column(name = "official_email_id")
	private String officialEmailId;
	@Column(name = "position_code")
	private String positionCode;
	
	@Column(name = "probation_period")
	private String probationPeriod;
	
	@Column(name = "position_code_effective_date")
	@Temporal(TemporalType.DATE)
	private Date positionCodeEffectiveDate;
	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeBranch employeeBranch;
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<EmployeeCreditLeaveDetail> employeeCreditLeaveDetails;
	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeCurrentDetail employeeCurrentDetail;
	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeDepartment employeeDepartment;
	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeDesignation employeeDesignation;
	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeDivision employeeDivision;
	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeEmploymentType employeeEmploymentType;
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<EmployeeLeaveApplied> employeeLeaveApplieds;
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<EmployeeLeaveDetail> employeeLeaveDetails;
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<EmployeeOrgnizationalAsset> employeeOrgnizationalAssets;
	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeReportingManager employeeReportingManager;
	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeRole employeeRole;
	
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<EmployeeSeparationDetails> employeeSeparationDetails;

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<EmployeeFeedback> employeeFeedback;
	
	@OneToMany(mappedBy = "approver", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private List<MapCatalogue> mapCatalogue;
	
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<EmployeeExtension> employeeExtensions;
	
	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private EmployeeACN employeeACN;
	
	
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<ProbationFeedback> probationFeedback;
	
	@Column(name = "official_mobile_number")
	private Long officialMobileNumber;
	
	@Column(name = "employee_code")
	private String employeeCode;
	
	@Column(name = "is_allowed_in_cycle")
	private String cycleAllowed;

	public Set<ProbationFeedback> getProbationFeedback() {
		return probationFeedback;
	}

	public void setProbationFeedback(Set<ProbationFeedback> probationFeedback) {
		this.probationFeedback = probationFeedback;
	}

	public EmployeeACN getEmployeeACN() {
		return employeeACN;
	}

	public void setEmployeeACN(EmployeeACN employeeACN) {
		this.employeeACN = employeeACN;
	}

	public Set<EmployeeLeaveApplied> getEmployeeLeaveApplieds() {
		return employeeLeaveApplieds;
	}

	public void setEmployeeLeaveApplieds(Set<EmployeeLeaveApplied> employeeLeaveApplieds) {
		this.employeeLeaveApplieds = employeeLeaveApplieds;
	}

	public Set<EmployeeLeaveDetail> getEmployeeLeaveDetails() {
		return employeeLeaveDetails;
	}

	public void setEmployeeLeaveDetails(Set<EmployeeLeaveDetail> employeeLeaveDetails) {
		this.employeeLeaveDetails = employeeLeaveDetails;
	}

	public Set<EmployeeOrgnizationalAsset> getEmployeeOrgnizationalAssets() {
		return employeeOrgnizationalAssets;
	}

	public void setEmployeeOrgnizationalAssets(Set<EmployeeOrgnizationalAsset> employeeOrgnizationalAssets) {
		this.employeeOrgnizationalAssets = employeeOrgnizationalAssets;
	}

	public Set<EmployeeCreditLeaveDetail> getEmployeeCreditLeaveDetails() {
		return employeeCreditLeaveDetails;
	}

	public void setEmployeeCreditLeaveDetails(Set<EmployeeCreditLeaveDetail> employeeCreditLeaveDetails) {
		this.employeeCreditLeaveDetails = employeeCreditLeaveDetails;
	}

	public EmployeeReportingManager getEmployeeReportingManager() {
		return employeeReportingManager;
	}

	public void setEmployeeReportingManager(EmployeeReportingManager employeeReportingManager) {
		this.employeeReportingManager = employeeReportingManager;
	}

	public EmployeeRole getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(EmployeeRole employeeRole) {
		this.employeeRole = employeeRole;
	}

	public EmployeeEmploymentType getEmployeeEmploymentType() {
		return employeeEmploymentType;
	}

	public void setEmployeeEmploymentType(EmployeeEmploymentType employeeEmploymentType) {
		this.employeeEmploymentType = employeeEmploymentType;
	}

	public EmployeeDivision getEmployeeDivision() {
		return employeeDivision;
	}

	public void setEmployeeDivision(EmployeeDivision employeeDivision) {
		this.employeeDivision = employeeDivision;
	}

	public EmployeeDesignation getEmployeeDesignation() {
		return employeeDesignation;
	}

	public void setEmployeeDesignation(EmployeeDesignation employeeDesignation) {
		this.employeeDesignation = employeeDesignation;
	}

	public EmployeeDepartment getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(EmployeeDepartment employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}

	public EmployeeCurrentDetail getEmployeeCurrentDetail() {
		return employeeCurrentDetail;
	}

	public void setEmployeeCurrentDetail(EmployeeCurrentDetail employeeCurrentDetail) {
		this.employeeCurrentDetail = employeeCurrentDetail;
	}

	public EmployeeBranch getEmployeeBranch() {
		return employeeBranch;
	}

	public void setEmployeeBranch(EmployeeBranch employeeBranch) {
		this.employeeBranch = employeeBranch;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public String getOfficialEmailId() {
		return officialEmailId;
	}

	public void setOfficialEmailId(String officialEmailId) {
		this.officialEmailId = officialEmailId;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public Date getPositionCodeEffectiveDate() {
		return positionCodeEffectiveDate;
	}

	public void setPositionCodeEffectiveDate(Date positionCodeEffectiveDate) {
		this.positionCodeEffectiveDate = positionCodeEffectiveDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<EmployeeSeparationDetails> getEmployeeSeparationDetails() {
		return employeeSeparationDetails;
	}

	public void setEmployeeSeparationDetails(Set<EmployeeSeparationDetails> employeeSeparationDetails) {
		this.employeeSeparationDetails = employeeSeparationDetails;
	}
	public Set<EmployeeFeedback> getEmployeeFeedback() {
		return employeeFeedback;
	}
	public void setEmployeeFeedback(Set<EmployeeFeedback> employeeFeedback) {
		this.employeeFeedback = employeeFeedback;
	}
	public List<MapCatalogue> getMapCatalogue() {
		return mapCatalogue;
	}
	public void setMapCatalogue(List<MapCatalogue> mapCatalogue) {
		this.mapCatalogue = mapCatalogue;
	}
	public Set<EmployeeExtension> getEmployeeExtensions() {
		return employeeExtensions;
	}
	public void setEmployeeExtensions(Set<EmployeeExtension> employeeExtensions) {
		this.employeeExtensions = employeeExtensions;
	}

	public String getProbationPeriod() {
		return probationPeriod;
	}

	public void setProbationPeriod(String probationPeriod) {
		this.probationPeriod = probationPeriod;
	}

	public Long getOfficialMobileNumber() {
		return officialMobileNumber;
	}

	public void setOfficialMobileNumber(Long officialMobileNumber) {
		this.officialMobileNumber = officialMobileNumber;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCycleAllowed() {
		return cycleAllowed;
	}

	public void setCycleAllowed(String cycleAllowed) {
		this.cycleAllowed = cycleAllowed;
	}
	
}
