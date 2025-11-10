package com.vinsys.hrms.directonboard.vo;

import java.util.Set;

import com.vinsys.hrms.master.vo.BranchVO;
import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.DesignationVO;
import com.vinsys.hrms.master.vo.DivisionVO;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.master.vo.LoginEntityTypeVO;
import com.vinsys.hrms.master.vo.MasterCityVO;
import com.vinsys.hrms.master.vo.MasterCountryVO;
import com.vinsys.hrms.master.vo.MasterStateVO;
import com.vinsys.hrms.master.vo.ReportingOfficerVO;

public class CurrentEmploymentRequestVO {

	
	private Long id;
	private EmploymentTypeVO employeeType;
	private DesignationVO designation;
	private String dateOfJoining;
	private String dateOfRetirement;
	private Integer noticePeriod;
	private Integer probationPeriod;
	private DivisionVO division;
	private BranchVO branch;
	private MasterCityVO city;
	private MasterStateVO state;
	private MasterCountryVO country;
	private DepartmentVO department;
	private ReportingOfficerVO reportingManager;
	private String officialEmailId;
	private Long officialMobileNumber;
	private Long acnNumber;
	private Long candidateId;
	private Set<LoginEntityTypeVO> roles;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public EmploymentTypeVO getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(EmploymentTypeVO employeeType) {
		this.employeeType = employeeType;
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
	public String getDateOfRetirement() {
		return dateOfRetirement;
	}
	public void setDateOfRetirement(String dateOfRetirement) {
		this.dateOfRetirement = dateOfRetirement;
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
	public DivisionVO getDivision() {
		return division;
	}
	public void setDivision(DivisionVO division) {
		this.division = division;
	}
	public BranchVO getBranch() {
		return branch;
	}
	public void setBranch(BranchVO branch) {
		this.branch = branch;
	}
	public MasterCityVO getCity() {
		return city;
	}
	public void setCity(MasterCityVO city) {
		this.city = city;
	}
	public MasterStateVO getState() {
		return state;
	}
	public void setState(MasterStateVO state) {
		this.state = state;
	}
	public MasterCountryVO getCountry() {
		return country;
	}
	public void setCountry(MasterCountryVO country) {
		this.country = country;
	}
	public DepartmentVO getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentVO department) {
		this.department = department;
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
	public Long getAcnNumber() {
		return acnNumber;
	}
	public void setAcnNumber(Long acnNumber) {
		this.acnNumber = acnNumber;
	}
	public Long getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}
	public Set<LoginEntityTypeVO> getRoles() {
		return roles;
	}
	public void setRoles(Set<LoginEntityTypeVO> roles) {
		this.roles = roles;
	}
	
	
}
