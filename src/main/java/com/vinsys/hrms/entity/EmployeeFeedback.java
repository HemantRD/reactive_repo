package com.vinsys.hrms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_trn_employee_feedback")
public class EmployeeFeedback extends AuditBase {
	@Id
	@SequenceGenerator(name = "seq_trn_employee_feedback", sequenceName = "seq_trn_employee_feedback", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_employee_feedback")
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "feedback_question_id", nullable = false)
	private FeedbackQuestion feedbackQuestion;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "feedback_option_id", nullable = true)
	private FeedbackOption feedbackOption;
	@Column(name = "user_feedback")
	private String userFeedback;
	@Column(name = "is_selected")
	private String isSelected;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public FeedbackQuestion getFeedbackQuestion() {
		return feedbackQuestion;
	}
	public void setFeedbackQuestion(FeedbackQuestion feedbackQuestion) {
		this.feedbackQuestion = feedbackQuestion;
	}
	public FeedbackOption getFeedbackOption() {
		return feedbackOption;
	}
	public void setFeedbackOption(FeedbackOption feedbackOption) {
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
