package com.vinsys.hrms.employee.vo;

import java.util.List;

import com.vinsys.hrms.datamodel.VOMasterRole;
import com.vinsys.hrms.master.vo.BranchVO;
import com.vinsys.hrms.master.vo.CityMasterVO;
import com.vinsys.hrms.master.vo.CountryMasterVO;
import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.DesignationVO;
import com.vinsys.hrms.master.vo.DivisionVO;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.master.vo.GenderMasterVO;
import com.vinsys.hrms.master.vo.GradeMasterVo;
import com.vinsys.hrms.master.vo.MasterMaritialStatusVo;
import com.vinsys.hrms.master.vo.MasterTitleVo;
import com.vinsys.hrms.master.vo.ReportingOfficerVO;
import com.vinsys.hrms.master.vo.StateVO;

public class EmployeeProfileVO {

	private Long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String officialEmailId;
	private String officialMobileNumber;
	private String employeeCode;
	private String dateOfBirth;
	private String dateOfJoining;
	private String retirementDate;
	private Long noticePeriod;
	private String probationPeriod;
	private String socialSecurityNo;
	private String spouceName;
	private Long orgId;
	private MasterTitleVo salutation;
	private BranchVO branch;
	private DepartmentVO department;
	private DivisionVO division;
	private DesignationVO designation;
	private GradeMasterVo grade;
	private CountryMasterVO country;
	private StateVO state;
	private CityMasterVO city;
	private GenderMasterVO gender;
	private MasterMaritialStatusVo maritalStatus;
	private EmploymentTypeVO employmentType;
	private ReportingOfficerVO reportingManager;
	private String emailId;
	private String cycleAllowed;
	private String mobileNumber;

	private List<VOMasterRole> roles;
	private String isKpiVisible;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getOfficialEmailId() {
		return officialEmailId;
	}

	public void setOfficialEmailId(String officialEmailId) {
		this.officialEmailId = officialEmailId;
	}

	public String getOfficialMobileNumber() {
		return officialMobileNumber;
	}

	public void setOfficialMobileNumber(String officialMobileNumber) {
		this.officialMobileNumber = officialMobileNumber;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public Long getNoticePeriod() {
		return noticePeriod;
	}

	public void setNoticePeriod(Long noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	public String getProbationPeriod() {
		return probationPeriod;
	}

	public void setProbationPeriod(String probationPeriod) {
		this.probationPeriod = probationPeriod;
	}

	public String getSocialSecurityNo() {
		return socialSecurityNo;
	}

	public void setSocialSecurityNo(String socialSecurityNo) {
		this.socialSecurityNo = socialSecurityNo;
	}

	public String getSpouceName() {
		return spouceName;
	}

	public void setSpouceName(String spouceName) {
		this.spouceName = spouceName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public MasterTitleVo getSalutation() {
		return salutation;
	}

	public void setSalutation(MasterTitleVo salutation) {
		this.salutation = salutation;
	}

	public BranchVO getBranch() {
		return branch;
	}

	public void setBranch(BranchVO branch) {
		this.branch = branch;
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

	public DesignationVO getDesignation() {
		return designation;
	}

	public void setDesignation(DesignationVO designation) {
		this.designation = designation;
	}

	public GradeMasterVo getGrade() {
		return grade;
	}

	public void setGrade(GradeMasterVo grade) {
		this.grade = grade;
	}

	public CountryMasterVO getCountry() {
		return country;
	}

	public void setCountry(CountryMasterVO country) {
		this.country = country;
	}

	public StateVO getState() {
		return state;
	}

	public void setState(StateVO state) {
		this.state = state;
	}

	public CityMasterVO getCity() {
		return city;
	}

	public void setCity(CityMasterVO city) {
		this.city = city;
	}

	public GenderMasterVO getGender() {
		return gender;
	}

	public void setGender(GenderMasterVO gender) {
		this.gender = gender;
	}

	public MasterMaritialStatusVo getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MasterMaritialStatusVo maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public EmploymentTypeVO getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(EmploymentTypeVO employmentType) {
		this.employmentType = employmentType;
	}

	public ReportingOfficerVO getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(ReportingOfficerVO reportingManager) {
		this.reportingManager = reportingManager;
	}

	public List<VOMasterRole> getRoles() {
		return roles;
	}

	public void setRoles(List<VOMasterRole> roles) {
		this.roles = roles;
	}


	public String getCycleAllowed() {
		return cycleAllowed;
	}

	public void setCycleAllowed(String cycleAllowed) {
		this.cycleAllowed = cycleAllowed;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getIsKpiVisible() {
		return isKpiVisible;
	}

	public void setIsKpiVisible(String isKpiVisible) {
		this.isKpiVisible = isKpiVisible;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	
}
