package com.vinsys.hrms.kra.vo;

public class KraListRequestVo {

	
	private String roleName;
	private Long deptId;
    private Long divId;
	private Long empId;
	private String yearId;
	private Long countryId;
	private String status;
	private Long cycleId;
	private Long gradeId;
	private Long delegatedTo;
	private String keyword;
	private Long totalRecord;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
    public Long getDivId() {
        return divId;
    }
    public void setDivId(Long divId) {
        this.divId = divId;
    }
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public String getYearId() {
		return yearId;
	}
	public void setYearId(String yearId) {
		this.yearId = yearId;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	public Long getGradeId() {
		return gradeId;
	}
	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Long getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(Long totalRecord) {
		this.totalRecord = totalRecord;
	}
	public Long getDelegatedTo() {
		return delegatedTo;
	}
	public void setDelegatedTo(Long delegatedTo) {
		this.delegatedTo = delegatedTo;
	}
	
	
}
