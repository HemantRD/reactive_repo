package com.vinsys.hrms.datamodel.confirmation;

import java.util.List;

import com.vinsys.hrms.datamodel.VOAuditBase;
import com.vinsys.hrms.datamodel.VOEmployee;

public class VOFeedbackResponse extends VOAuditBase{
	
	private Long id;
	private Long employeeId;
	private String employeeName;
	private VOEmployee employee;
	private String hrComment;
	private String managerComment;
	private String status;
	private List<VOProbationParameter> probationParameter;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public VOEmployee getEmployee() {
		return employee;
	}
	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getHrComment() {
		return hrComment;
	}
	public void setHrComment(String hrComment) {
		this.hrComment = hrComment;
	}
	public String getManagerComment() {
		return managerComment;
	}
	public void setManagerComment(String managerComment) {
		this.managerComment = managerComment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<VOProbationParameter> getProbationParameter() {
		return probationParameter;
	}
	public void setProbationParameter(List<VOProbationParameter> probationParameter) {
		this.probationParameter = probationParameter;
	}
}
