package com.vinsys.hrms.kra.vo;

import javax.validation.constraints.NotEmpty;

public class CalibrationRequestVo {

	private Long employeeId;
	private KraRatingRequestVo rating;
	
	@NotEmpty(message = "Comment must not be empty")
	private String comment;
	private Long cycleId;
	private Long kraId;
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public KraRatingRequestVo getRating() {
		return rating;
	}
	public void setRating(KraRatingRequestVo rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	public Long getKraId() {
		return kraId;
	}
	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}
	
	
}
