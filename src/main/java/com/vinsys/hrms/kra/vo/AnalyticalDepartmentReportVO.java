package com.vinsys.hrms.kra.vo;

import java.util.List;

public class AnalyticalDepartmentReportVO {

	private List<AnalyticalDepartmentSummaryResponseVO> departmentSummary;
	

	public List<AnalyticalDepartmentSummaryResponseVO> getDepartmentSummary() {
		return departmentSummary;
	}

	public void setDepartmentSummary(List<AnalyticalDepartmentSummaryResponseVO> departmentSummary) {
		this.departmentSummary = departmentSummary;
	}
}
