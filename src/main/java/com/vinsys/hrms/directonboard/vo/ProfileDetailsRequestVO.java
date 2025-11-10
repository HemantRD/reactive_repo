package com.vinsys.hrms.directonboard.vo;

import java.util.Set;

import com.vinsys.hrms.master.vo.BranchVO;
import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.DesignationVO;
import com.vinsys.hrms.master.vo.DivisionVO;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.master.vo.GenderMasterVO;
import com.vinsys.hrms.master.vo.GradeMasterVo;
import com.vinsys.hrms.master.vo.LoginEntityTypeVO;
import com.vinsys.hrms.master.vo.MasterCityVO;
import com.vinsys.hrms.master.vo.MasterCountryVO;
import com.vinsys.hrms.master.vo.MasterMaritialStatusVo;
import com.vinsys.hrms.master.vo.MasterTitleVo;
import com.vinsys.hrms.master.vo.ReportingOfficerVO;
import com.vinsys.hrms.master.vo.StateVO;

public class ProfileDetailsRequestVO {

	private Long id;
	private MasterTitleVo salutation;
	private String firstName;
	private String middleName;
	private String lastName;
	private String dateOfBirth;
	private GenderMasterVO gender;
	private Long mobileNumber;
	private String emailId;
	private MasterMaritialStatusVo maritalStatus;
	private String spouceName;
	private String socialSecurityNo;
	private EmploymentTypeVO employmentType;
	private String employeeCode;
	private DesignationVO designation;

	private String dateOfJoining;
	private String retirementDate;
	private Integer noticePeriod;
	private Integer probationPeriod;
	private BranchVO branch;
	private MasterCountryVO country;
	private StateVO state;
	private MasterCityVO city;
	private DepartmentVO department;
	private DivisionVO division;
	private ReportingOfficerVO reportingManager;
	private String officialEmailId;
	private Long officialMobileNumber;
	private GradeMasterVo grade;
	private Set<LoginEntityTypeVO> roles;
	private String isCycleAllowed;
	private String isKpiVisible;
	


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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public MasterTitleVo getSalutation() {
		return salutation;
	}

	public void setSalutation(MasterTitleVo salutation) {
		this.salutation = salutation;
	}

	public MasterMaritialStatusVo getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MasterMaritialStatusVo maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getSpouceName() {
		return spouceName;
	}

	public void setSpouceName(String spouceName) {
		this.spouceName = spouceName;
	}

	public GenderMasterVO getGender() {
		return gender;
	}

	public void setGender(GenderMasterVO gender) {
		this.gender = gender;
	}

	public String getSocialSecurityNo() {
		return socialSecurityNo;
	}

	public void setSocialSecurityNo(String socialSecurityNo) {
		this.socialSecurityNo = socialSecurityNo;
	}

	public EmploymentTypeVO getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(EmploymentTypeVO employmentType) {
		this.employmentType = employmentType;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public DesignationVO getDesignation() {
		return designation;
	}

	public void setDesignation(DesignationVO designation) {
		this.designation = designation;
	}

	public String getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getRetirementDate() {
		return retirementDate;
	}

	public void setRetirementDate(String retirementDate) {
		this.retirementDate = retirementDate;
	}

	public Integer getNoticePeriod() {
		return noticePeriod;
	}

	public void setNoticePeriod(Integer noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	public Integer getProbationPeriod() {
		return probationPeriod;
	}

	public void setProbationPeriod(Integer probationPeriod) {
		this.probationPeriod = probationPeriod;
	}

	public BranchVO getBranch() {
		return branch;
	}

	public void setBranch(BranchVO branch) {
		this.branch = branch;
	}

	public MasterCountryVO getCountry() {
		return country;
	}

	public void setCountry(MasterCountryVO country) {
		this.country = country;
	}

	public StateVO getState() {
		return state;
	}

	public void setState(StateVO state) {
		this.state = state;
	}

	public MasterCityVO getCity() {
		return city;
	}

	public void setCity(MasterCityVO city) {
		this.city = city;
	}

	public DepartmentVO getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentVO department) {
		this.department = department;
	}

	public DivisionVO getDivision() {
		return division;
	}

	public void setDivision(DivisionVO division) {
		this.division = division;
	}

	public ReportingOfficerVO getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(ReportingOfficerVO reportingManager) {
		this.reportingManager = reportingManager;
	}

	public String getOfficialEmailId() {
		return officialEmailId;
	}

	public void setOfficialEmailId(String officialEmailId) {
		this.officialEmailId = officialEmailId;
	}

	public Long getOfficialMobileNumber() {
		return officialMobileNumber;
	}

	public void setOfficialMobileNumber(Long officialMobileNumber) {
		this.officialMobileNumber = officialMobileNumber;
	}

	public GradeMasterVo getGrade() {
		return grade;
	}

	public void setGrade(GradeMasterVo grade) {
		this.grade = grade;
	}

	public Set<LoginEntityTypeVO> getRoles() {
		return roles;
	}

	public void setRoles(Set<LoginEntityTypeVO> roles) {
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIsCycleAllowed() {
		return isCycleAllowed;
	}

	public void setIsCycleAllowed(String isCycleAllowed) {
		this.isCycleAllowed = isCycleAllowed;
	}

	public String getIsKpiVisible() {
		return isKpiVisible;
	}

	public void setIsKpiVisible(String isKpiVisible) {
		this.isKpiVisible = isKpiVisible;
	}
	
}
