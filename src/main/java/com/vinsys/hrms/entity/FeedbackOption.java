package com.vinsys.hrms.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_map_feedback_option")
public class FeedbackOption extends AuditBase {
	@Id
	@SequenceGenerator(name = "seq_map_feedback_option", sequenceName = "seq_map_feedback_option", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_feedback_option")
	private long id;
	@Column(name = "option_name")
	private String optionName;
	@Column(name = "sequence_number")
	private int sequenceNumber;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "feedback_question_id", nullable = false)
	private FeedbackQuestion feedbackQuestion;
	
	@OneToMany(mappedBy = "feedbackOption", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<EmployeeFeedback> employeeFeedback;

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

	public FeedbackQuestion getFeedbackQuestion() {
		return feedbackQuestion;
	}

	public void setFeedbackQuestion(FeedbackQuestion feedbackQuestion) {
		this.feedbackQuestion = feedbackQuestion;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
}
