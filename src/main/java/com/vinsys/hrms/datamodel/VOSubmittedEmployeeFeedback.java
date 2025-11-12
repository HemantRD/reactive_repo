package com.vinsys.hrms.datamodel;

import java.util.List;

public class VOSubmittedEmployeeFeedback {

	private VOEmployee employee;
	private List<VOFeedbackQuestion> empFeedbackList;
	
	public VOEmployee getEmployee() {
		return employee;
	}
	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}
	public List<VOFeedbackQuestion> getEmpFeedbackList() {
		return empFeedbackList;
	}
	public void setEmpFeedbackList(List<VOFeedbackQuestion> empFeedbackList) {
		this.empFeedbackList = empFeedbackList;
	}
	
}
