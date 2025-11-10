package com.vinsys.hrms.employee.vo;

import com.vinsys.hrms.master.vo.ProbationFeedbackParameterVO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Onkar A
 *
 * 
 */
public class ProbationParameterVO {
	@Schema(required = true, description = "Only when HR/Manager savefeed back")
	private Long id;
	@Schema(required = true, description = "Rquired for all role.")
	private ProbationFeedbackParameterVO parameterValue;
	@Schema(required = true, description = "Rquired for  employee")
	private long empRating;
	@Schema(required = true, description = "Rquired for  employee.")
	private String employeeComment;
	@Schema(required = true, description = "Rquired for manager")
	private long managerRating;
	@Schema(required = true, description = "Rquired for manager")
	private String managerComment;
	@Schema(required = true, description = "Only when HR/Manager savefeed back")
	private long feedbackId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public long getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public ProbationFeedbackParameterVO getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(ProbationFeedbackParameterVO parameterValue) {
		this.parameterValue = parameterValue;
	}

	public long getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(long managerRating) {
		this.managerRating = managerRating;
	}

	public long getEmpRating() {
		return empRating;
	}

	public void setEmpRating(long empRating) {
		this.empRating = empRating;
	}
}
