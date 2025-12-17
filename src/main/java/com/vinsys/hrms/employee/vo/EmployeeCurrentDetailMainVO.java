package com.vinsys.hrms.employee.vo;

import java.util.List;

public class EmployeeCurrentDetailMainVO {

	private EmployeeCurrentDetailVO employeeDetails;
	private List<IdentificationDetailsVO> documents;

	public List<IdentificationDetailsVO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<IdentificationDetailsVO> documents) {
		this.documents = documents;
	}

	public EmployeeCurrentDetailVO getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(EmployeeCurrentDetailVO employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

}
