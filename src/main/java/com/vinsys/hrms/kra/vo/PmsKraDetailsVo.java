package com.vinsys.hrms.kra.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public class PmsKraDetailsVo {

	
	@Schema(required = true,description = "This field will use only approve/reject and save/submit(first time not required if he want to update then required")
	private Long id;
	private String year;
	@Schema(required = true)
	private Float weightage;
	@Schema(required = true)
	private String kraDetails;
	@Schema(required = true)
	private String description;
	@Schema(required = true)
	private String measurementCriteria;
	@Schema(required = true)
	private String achievementPlan;
	@Schema(required = true,description = "while approve and reject")
	private String rmComment;
	@Schema(required = true)
	private Long categoryId;
	@Schema(required = true)
	private Long subcategoryId;
	@Schema(required = true)
	private Float objectiveWeightage;
	@Schema(required = true)
	private String selfRating;
	@Schema(required = true)
	private String managerRating;
	@Schema(required = true)
	private String selfQaulitativeAssisment;
	@Schema(required = true)
	private String managerQaulitativeAssisment;
}
