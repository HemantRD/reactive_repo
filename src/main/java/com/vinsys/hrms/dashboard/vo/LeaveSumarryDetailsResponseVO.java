package com.vinsys.hrms.dashboard.vo;

import java.util.List;

public class LeaveSumarryDetailsResponseVO {
    private List<EmployeeLeaveSumarryDetailsVO> employeeLeaveDetail;
    
	public List<EmployeeLeaveSumarryDetailsVO> getEmployeeLeaveDetail() {
		return employeeLeaveDetail;
	}
	public void setEmployeeLeaveDetail(List<EmployeeLeaveSumarryDetailsVO> employeeLeaveDetail) {
		this.employeeLeaveDetail = employeeLeaveDetail;
	}
 
}
