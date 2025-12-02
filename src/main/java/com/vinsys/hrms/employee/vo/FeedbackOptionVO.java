package com.vinsys.hrms.employee.vo;



public class FeedbackOptionVO {
	private long id;
	private String optionName;
	private int sequenceNumber;
	private FeedbackQuestionVO feedbackQuestion;
	private String isSelected;
	private long employeeFeedbackId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public FeedbackQuestionVO getFeedbackQuestion() {
		return feedbackQuestion;
	}
	public void setFeedbackQuestion(FeedbackQuestionVO feedbackQuestion) {
		this.feedbackQuestion = feedbackQuestion;
	}
	public String getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}
	public long getEmployeeFeedbackId() {
		return employeeFeedbackId;
	}
	public void setEmployeeFeedbackId(long employeeFeedbackId) {
		this.employeeFeedbackId = employeeFeedbackId;
	}
	
		
}
