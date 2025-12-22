package com.vinsys.hrms.datamodel;

import java.util.List;

public class VOOrganizationDetail {

	private String responseMessage;
	private int responseCode;
	private List<Object> branchList;
	private List<Object> departmentList;
	private List<Object> divisionList;
	private List<Object> designationList;
	private List<Object> loginEntityTypeList;
	private List<Object> employmentTypeList;

	public List<Object> getBranchList() {
		return branchList;
	}

	public void setBranchList(List<Object> branchList) {
		this.branchList = branchList;
	}

	public List<Object> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Object> departmentList) {
		this.departmentList = departmentList;
	}

	public List<Object> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<Object> divisionList) {
		this.divisionList = divisionList;
	}

	public List<Object> getDesignationList() {
		return designationList;
	}

	public void setDesignationList(List<Object> designationList) {
		this.designationList = designationList;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public List<Object> getLoginEntityTypeList() {
		return loginEntityTypeList;
	}

	public void setLoginEntityTypeList(List<Object> loginEntityTypeList) {
		this.loginEntityTypeList = loginEntityTypeList;
	}

	public List<Object> getEmploymentTypeList() {
		return employmentTypeList;
	}

	public void setEmploymentTypeList(List<Object> employmentTypeList) {
		this.employmentTypeList = employmentTypeList;
	}
	
}
