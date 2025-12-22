package com.vinsys.hrms.datamodel;

import com.vinsys.hrms.entity.AuditBase;

public class VOFeedbackOption extends AuditBase {

	private long id;
	private String optionName;
	private int sequenceNumber;
	private VOFeedbackQuestion feedbackQuestion;
	private String isSelected;
	//private Set<EmployeeFeedback> employeeFeedback;
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
	public VOFeedbackQuestion getFeedbackQuestion() {
		return feedbackQuestion;
	}
	public void setFeedbackQuestion(VOFeedbackQuestion feedbackQuestion) {
		this.feedbackQuestion = feedbackQuestion;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
