package com.vinsys.hrms.datamodel.confirmation;

public class VOProbationParameter {
	
	
	private Long id;
	private VOMasterEvaluationParameter parameterValue;
	private String empRating;
	private String employeeComment;
	private String managerRating;
	private String managerComment;
	private long feedbackId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public VOMasterEvaluationParameter getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(VOMasterEvaluationParameter parameterValue) {
		this.parameterValue = parameterValue;
	}
	public String getEmpRating() {
		return empRating;
	}
	public void setEmpRating(String empRating) {
		this.empRating = empRating;
	}
	public String getEmployeeComment() {
		return employeeComment;
	}
	public void setEmployeeComment(String employeeComment) {
		this.employeeComment = employeeComment;
	}
	public String getManagerRating() {
		return managerRating;
	}
	public void setManagerRating(String managerRating) {
		this.managerRating = managerRating;
	}
	public String getManagerComment() {
		return managerComment;
	}
	public void setManagerComment(String managerComment) {
		this.managerComment = managerComment;
	}
	public long getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(long feedbackId) {
		this.feedbackId = feedbackId;
	}
	
}
