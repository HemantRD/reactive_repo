package com.vinsys.hrms.kra.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vinsys.hrms.master.vo.SelfRatingVo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectiveFeedbackResponseVo {
	private Long id;

	private SelfRatingVo selfRating;
	private String selfQualitativeAssessment;
	private String selfQualitativeAssessmentAI;

	private SelfRatingResponseVo managerRating;
	private String managerQualitativeAssessment;
	private String managerQualitativeAssessmentAI;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SelfRatingVo getSelfRating() {
		return selfRating;
	}

	public void setSelfRating(SelfRatingVo selfRating) {
		this.selfRating = selfRating;
	}

	public String getSelfQualitativeAssessment() {
		return selfQualitativeAssessment;
	}

	public void setSelfQualitativeAssessment(String selfQualitativeAssessment) {
		this.selfQualitativeAssessment = selfQualitativeAssessment;
	}

	public String getManagerQualitativeAssessment() {
		return managerQualitativeAssessment;
	}

	public void setManagerQualitativeAssessment(String managerQualitativeAssessment) {
		this.managerQualitativeAssessment = managerQualitativeAssessment;
	}

	public SelfRatingResponseVo getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(SelfRatingResponseVo managerRating) {
		this.managerRating = managerRating;
	}

	public String getSelfQualitativeAssessmentAI() {
		return selfQualitativeAssessmentAI;
	}

	public void setSelfQualitativeAssessmentAI(String selfQualitativeAssessmentAI) {
		this.selfQualitativeAssessmentAI = selfQualitativeAssessmentAI;
	}

	public String getManagerQualitativeAssessmentAI() {
		return managerQualitativeAssessmentAI;
	}

	public void setManagerQualitativeAssessmentAI(String managerQualitativeAssessmentAI) {
		this.managerQualitativeAssessmentAI = managerQualitativeAssessmentAI;
	}
}
