package com.vinsys.hrms.datamodel;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vinsys.hrms.datamodel.attendance.VOEmployeeACN;

public class VOEmployee extends VOAuditBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private VOCandidate candidate;
	private String officialEmailId;
	private String positionCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date positionCodeEffectiveDate;
	private VOEmployeeBranch employeeBranch;
	private Set<VOEmployeeCreditLeaveDetail> employeeCreditLeaveDetails;
	private VOEmployeeCurrentDetail employeeCurrentDetail;
	private VOEmployeeDepartment employeeDepartment;
	private VOEmployeeDesignation employeeDesignation;
	private VOEmployeeDivision employeeDivision;
	private VOEmployeeEmploymentType employeeEmploymentType;
	private Set<VOEmployeeLeaveApplied> employeeLeaveApplieds;
	private Set<VOEmployeeLeaveDetail> employeeLeaveDetails;
	private Set<VOEmployeeOrgnizationalAsset> employeeOrgnizationalAssets;
	private VOEmployeeReportingManager employeeReportingManager;
	private VOEmployeeRole employeeRole;
	private Set<VOEmployeeSeparationDetails> employeeSeparationDetails;
	private Set<VOEmployeeExtension> employeeExtensions;
	private long serialNumber;
	private VOEmployeeACN employeeACN;
	private String empACN;
	private String employeeProbationPeriod;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOCandidate getCandidate() {
		return candidate;
	}

	public void setCandidate(VOCandidate candidate) {
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

	public VOEmployeeBranch getEmployeeBranch() {
		return employeeBranch;
	}

	public void setEmployeeBranch(VOEmployeeBranch employeeBranch) {
		this.employeeBranch = employeeBranch;
	}

	public Set<VOEmployeeCreditLeaveDetail> getEmployeeCreditLeaveDetails() {
		return employeeCreditLeaveDetails;
	}

	public void setEmployeeCreditLeaveDetails(Set<VOEmployeeCreditLeaveDetail> employeeCreditLeaveDetails) {
		this.employeeCreditLeaveDetails = employeeCreditLeaveDetails;
	}

	public VOEmployeeCurrentDetail getEmployeeCurrentDetail() {
		return employeeCurrentDetail;
	}

	public void setEmployeeCurrentDetail(VOEmployeeCurrentDetail employeeCurrentDetail) {
		this.employeeCurrentDetail = employeeCurrentDetail;
	}

	public VOEmployeeDepartment getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(VOEmployeeDepartment employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}

	public VOEmployeeDesignation getEmployeeDesignation() {
		return employeeDesignation;
	}

	public void setEmployeeDesignation(VOEmployeeDesignation employeeDesignation) {
		this.employeeDesignation = employeeDesignation;
	}

	public VOEmployeeDivision getEmployeeDivision() {
		return employeeDivision;
	}

	public void setEmployeeDivision(VOEmployeeDivision employeeDivision) {
		this.employeeDivision = employeeDivision;
	}

	public VOEmployeeEmploymentType getEmployeeEmploymentType() {
		return employeeEmploymentType;
	}

	public void setEmployeeEmploymentType(VOEmployeeEmploymentType employeeEmploymentType) {
		this.employeeEmploymentType = employeeEmploymentType;
	}

	public Set<VOEmployeeLeaveApplied> getEmployeeLeaveApplieds() {
		return employeeLeaveApplieds;
	}

	public void setEmployeeLeaveApplieds(Set<VOEmployeeLeaveApplied> employeeLeaveApplieds) {
		this.employeeLeaveApplieds = employeeLeaveApplieds;
	}

	public Set<VOEmployeeLeaveDetail> getEmployeeLeaveDetails() {
		return employeeLeaveDetails;
	}

	public void setEmployeeLeaveDetails(Set<VOEmployeeLeaveDetail> employeeLeaveDetails) {
		this.employeeLeaveDetails = employeeLeaveDetails;
	}

	public Set<VOEmployeeOrgnizationalAsset> getEmployeeOrgnizationalAssets() {
		return employeeOrgnizationalAssets;
	}

	public void setEmployeeOrgnizationalAssets(Set<VOEmployeeOrgnizationalAsset> employeeOrgnizationalAssets) {
		this.employeeOrgnizationalAssets = employeeOrgnizationalAssets;
	}

	public VOEmployeeReportingManager getEmployeeReportingManager() {
		return employeeReportingManager;
	}

	public void setEmployeeReportingManager(VOEmployeeReportingManager employeeReportingManager) {
		this.employeeReportingManager = employeeReportingManager;
	}

	public VOEmployeeRole getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(VOEmployeeRole employeeRole) {
		this.employeeRole = employeeRole;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<VOEmployeeSeparationDetails> getEmployeeSeparationDetails() {
		return employeeSeparationDetails;
	}

	public void setEmployeeSeparationDetails(Set<VOEmployeeSeparationDetails> employeeSeparationDetails) {
		this.employeeSeparationDetails = employeeSeparationDetails;
	}

	public Set<VOEmployeeExtension> getEmployeeExtensions() {
		return employeeExtensions;
	}

	public void setEmployeeExtensions(Set<VOEmployeeExtension> employeeExtensions) {
		this.employeeExtensions = employeeExtensions;
	}

	public long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}

	public VOEmployeeACN getEmployeeACN() {
		return employeeACN;
	}

	public void setEmployeeACN(VOEmployeeACN employeeACN) {
		this.employeeACN = employeeACN;
	}

	public String getEmpACN() {
		return empACN;
	}

	public void setEmpACN(String empACN) {
		this.empACN = empACN;
	}

	public String getEmployeeProbationPeriod() {
		return employeeProbationPeriod;
	}

	public void setEmployeeProbationPeriod(String employeeProbationPeriod) {
		this.employeeProbationPeriod = employeeProbationPeriod;
	}
	
	
	
	
}
