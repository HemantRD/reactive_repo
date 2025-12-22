package com.vinsys.hrms.datamodel;

public class VOEmployeeFeedback extends VOAuditBase {
private long id;
	
	private VOEmployee employee;
	private VOFeedbackQuestion feedbackQuestion;
	private VOFeedbackOption feedbackOption;
	private String userFeedback;
	private String isSelected;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public VOEmployee getEmployee() {
		return employee;
	}
	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}
	public VOFeedbackQuestion getFeedbackQuestion() {
		return feedbackQuestion;
	}
	public void setFeedbackQuestion(VOFeedbackQuestion feedbackQuestion) {
		this.feedbackQuestion = feedbackQuestion;
	}
	public VOFeedbackOption getFeedbackOption() {
		return feedbackOption;
	}
	public void setFeedbackOption(VOFeedbackOption feedbackOption) {
		this.feedbackOption = feedbackOption;
	}
	public String getUserFeedback() {
		return userFeedback;
	}
	public void setUserFeedback(String userFeedback) {
		this.userFeedback = userFeedback;
	}
	public String getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}
	
	
}
