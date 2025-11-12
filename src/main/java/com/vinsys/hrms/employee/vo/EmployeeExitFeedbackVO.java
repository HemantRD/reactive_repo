package com.vinsys.hrms.employee.vo;



public class EmployeeExitFeedbackVO {
	
	private long id;
	private FeedbackQuestionVO feedbackQuestion;
	private FeedbackOptionVO feedbackOption;
	private String userFeedback;
	private String isSelected;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public FeedbackQuestionVO getFeedbackQuestion() {
		return feedbackQuestion;
	}
	public void setFeedbackQuestion(FeedbackQuestionVO feedbackQuestion) {
		this.feedbackQuestion = feedbackQuestion;
	}
	public FeedbackOptionVO getFeedbackOption() {
		return feedbackOption;
	}
	public void setFeedbackOption(FeedbackOptionVO feedbackOption) {
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
