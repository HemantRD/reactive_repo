package com.vinsys.hrms.entity.confirmation;

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
@Table(name = "tbl_probation_parameter_feedback")
public class ProbationParameter {

	@Id
	@SequenceGenerator(name = "seq_probation_parameter_feedback", sequenceName = "seq_probation_parameter_feedback", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_probation_parameter_feedback")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "parameter")
	// @JsonManagedReference
	private MasterEvaluationParameter parameterValue;

	@Column(name = "employee_rating")
	private long empRating;

	@Column(name = "employee_comment")
	private String employeeComment;

	@Column(name = "manager_rating")
	private long managerRating;

	@Column(name = "manager_comment")
	private String managerComment;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "feedback_id")
	// @JsonManagedReference
	// @JsonBackReference
	private ProbationFeedback feedback;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MasterEvaluationParameter getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(MasterEvaluationParameter parameterValue) {
		this.parameterValue = parameterValue;
	}

	public ProbationFeedback getFeedback() {
		return feedback;
	}

	public void setFeedback(ProbationFeedback feedback) {
		this.feedback = feedback;
	}

	public String getEmployeeComment() {
		return employeeComment;
	}

	public void setEmployeeComment(String employeeComment) {
		this.employeeComment = employeeComment;
	}

	public String getManagerComment() {
		return managerComment;
	}

	public void setManagerComment(String managerComment) {
		this.managerComment = managerComment;
	}

	public long getEmpRating() {
		return empRating;
	}

	public void setEmpRating(long empRating) {
		this.empRating = empRating;
	}

	public long getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(long managerRating) {
		this.managerRating = managerRating;
	}

}
