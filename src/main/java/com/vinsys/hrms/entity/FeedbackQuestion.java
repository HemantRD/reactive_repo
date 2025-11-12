package com.vinsys.hrms.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_map_feedback_question")
public class FeedbackQuestion extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_map_feedback_question", sequenceName = "seq_map_feedback_question", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_feedback_question")
	private long id;
	@Column(name = "question_name")
	private String questionName;
	@Column(name = "choice")
	private String choice;
	@Column(name = "sequence_number")
	private int sequenceNumber;
	@OneToMany(mappedBy = "feedbackQuestion", fetch = FetchType.LAZY /*, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }*/)
	@OrderBy("sequence_number asc")
	private Set<FeedbackOption> feedbackOptions;
//	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
//			CascadeType.REFRESH })
//	@JoinColumn(name = "organization_id")
//	private Organization organization;
//	
	@OneToMany(mappedBy = "feedbackQuestion", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<EmployeeFeedback> employeeFeedback;

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

	public Set<FeedbackOption> getFeedbackOptions() {
		return feedbackOptions;
	}

	public void setFeedbackOptions(Set<FeedbackOption> feedbackOptions) {
		this.feedbackOptions = feedbackOptions;
	}

//	public Organization getOrganization() {
//		return organization;
//	}
//
//	public void setOrganization(Organization organization) {
//		this.organization = organization;
//	}
	public Set<EmployeeFeedback> getEmployeeFeedback() {
		return employeeFeedback;
	}
	public void setEmployeeFeedback(Set<EmployeeFeedback> employeeFeedback) {
		this.employeeFeedback = employeeFeedback;
	}
}
