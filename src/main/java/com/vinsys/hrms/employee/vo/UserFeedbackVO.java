package com.vinsys.hrms.employee.vo;

public class UserFeedbackVO {
	private long employeeFeedbackId;
	private String comment;
	public long getEmployeeFeedbackId() {
		return employeeFeedbackId;
	}
	public void setEmployeeFeedbackId(long employeeFeedbackId) {
		this.employeeFeedbackId = employeeFeedbackId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

}
