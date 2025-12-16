package com.vinsys.hrms.kra.vo;

import com.vinsys.hrms.master.vo.SelfRatingVo;

public class ObjectiveFeedbackVo {
	private Long id;

	private SelfRatingVo selfRating;
	private String selfQualitativeAssessment;

	private SelfRatingResponseVo managerRating;
	private String managerQualitativeAssessment;

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
}
