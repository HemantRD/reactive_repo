package com.vinsys.hrms.employee.vo;

import java.util.List;




public class FeedbackQuestionVO {
	
	private long id;
	private String questionName;
	private String choice;
	private int sequenceNumber;
	private long orgnizationId;
	private List<FeedbackOptionVO> feedbackOptions;
	private UserFeedbackVO userFeedback;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public List<FeedbackOptionVO> getFeedbackOptions() {
		return feedbackOptions;
	}
	public void setFeedbackOptions(List<FeedbackOptionVO> feedbackOptions) {
		this.feedbackOptions = feedbackOptions;
	}
	public UserFeedbackVO getUserFeedback() {
		return userFeedback;
	}
	public void setUserFeedback(UserFeedbackVO userFeedback) {
		this.userFeedback = userFeedback;
	}
	public long getOrgnizationId() {
		return orgnizationId;
	}
	public void setOrgnizationId(long orgnizationId) {
		this.orgnizationId = orgnizationId;
	}
	
	

}
