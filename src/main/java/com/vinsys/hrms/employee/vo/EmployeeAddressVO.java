package com.vinsys.hrms.employee.vo;
 
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDivision;
 
public class EmployeeAddressVO {
 
	private long id;
	private String officialEmailId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String department;
	private String branch;
	private String division;
	private String dateOfBirth;
	private long contactNo;
	private String officialContactNo;
	private String employeeCode;
	private String name;
	private String designation;
	private String ReportingManager;
	private String Grade;
	private Long branchId;
	private Long departmentId;
	private Long divisionId;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOfficialEmailId() {
		return officialEmailId;
	}
	public void setOfficialEmailId(String officialEmailId) {
		this.officialEmailId = officialEmailId;
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public long getContactNo() {
		return contactNo;
	}
	public void setContactNo(long contactNo) {
		this.contactNo = contactNo;
	}
	public String getOfficialContactNo() {
		return officialContactNo;
	}
	public void setOfficialContactNo(String officialContactNo) {
		this.officialContactNo = officialContactNo;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getReportingManager() {
		return ReportingManager;
	}
	public void setReportingManager(String reportingManager) {
		ReportingManager = reportingManager;
	}
	public String getGrade() {
		return Grade;
	}
	public void setGrade(String grade) {
		Grade = grade;
	}
	public Long getBranchId() {
		return branchId;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public Long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

 
}