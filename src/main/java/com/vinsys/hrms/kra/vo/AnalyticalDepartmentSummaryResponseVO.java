package com.vinsys.hrms.kra.vo;

public class AnalyticalDepartmentSummaryResponseVO {

	private String departmentName;
	private String department;
	private Long count;

	public AnalyticalDepartmentSummaryResponseVO() {
		super();
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
