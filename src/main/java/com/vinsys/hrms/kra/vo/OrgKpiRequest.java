package com.vinsys.hrms.kra.vo;

public class OrgKpiRequest {

	private Long deptId;
	private Long kpiTypeId;
	private String yearId;
	private Long gradeId;
	private String keyword;
	private Long totalRecord;
	
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getKpiTypeId() {
		return kpiTypeId;
	}
	public void setKpiTypeId(Long kpiTypeId) {
		this.kpiTypeId = kpiTypeId;
	}
	
	public String getYearId() {
		return yearId;
	}
	public void setYearId(String yearId) {
		this.yearId = yearId;
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
	
	
}
