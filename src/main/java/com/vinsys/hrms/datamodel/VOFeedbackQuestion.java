package com.vinsys.hrms.datamodel;

import java.util.List;

public class VOFeedbackQuestion extends VOAuditBase {

	private long id;
	private String questionName;
	private VOOrganization organization;
	private String choice;
	private int sequenceNumber;
	private List<VOFeedbackOption> feedbackOptions;
	private VOUserFeedback userFeedback;
	// private Set<EmployeeFeedback> employeeFeedback;

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

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
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

	public List<VOFeedbackOption> getFeedbackOptions() {
		return feedbackOptions;
	}

	public void setFeedbackOptions(List<VOFeedbackOption> feedbackOptions) {
		this.feedbackOptions = feedbackOptions;
	}

	public VOUserFeedback getUserFeedback() {
		return userFeedback;
	}

	public void setUserFeedback(VOUserFeedback userFeedback) {
		this.userFeedback = userFeedback;
	}
}
