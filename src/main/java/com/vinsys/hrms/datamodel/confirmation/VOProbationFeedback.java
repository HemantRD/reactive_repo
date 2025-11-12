package com.vinsys.hrms.datamodel.confirmation;

import java.util.List;

import com.vinsys.hrms.datamodel.VOAuditBase;
import com.vinsys.hrms.datamodel.VOEmployee;

public class VOProbationFeedback extends VOAuditBase {
	
	private Long id;
	private VOEmployee employee;
	private String hrComment;
	private String managerComment;
	private String status;
	private Boolean hrSubmitted;
	private Long extendedBy;
	private List<VOProbationParameter> probationParameter;
	private String actionTakenBy;
	private Boolean roSubmitted; 
	
	public Boolean getRoSubmitted() {
		return roSubmitted;
	}
	public void setRoSubmitted(Boolean roSubmitted) {
		this.roSubmitted = roSubmitted;
	}
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
	public Boolean getHrSubmitted() {
		return hrSubmitted;
	}
	public void setHrSubmitted(Boolean hrSubmitted) {
		this.hrSubmitted = hrSubmitted;
	}
	public Long getExtendedBy() {
		return extendedBy;
	}
	public void setExtendedBy(Long extendedBy) {
		this.extendedBy = extendedBy;
	}
	public List<VOProbationParameter> getProbationParameter() {
		return probationParameter;
	}
	public void setProbationParameter(List<VOProbationParameter> probationParameter) {
		this.probationParameter = probationParameter;
	}
	public String getActionTakenBy() {
		return actionTakenBy;
	}
	public void setActionTakenBy(String actionTakenBy) {
		this.actionTakenBy = actionTakenBy;
	}
}
