package com.vinsys.hrms.kra.vo;

public class HodCycleFinalRatingResponseVO {
	
	private Long id;

	private String employeeName;
	
	private String mcMemberName;
	
	private String cycleName;
	
	private String year;
	
	private String score;
	
	private String comment;
	
	private Long kraId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getMcMemberName() {
		return mcMemberName;
	}

	public void setMcMemberName(String mcMemberName) {
		this.mcMemberName = mcMemberName;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getKraId() {
		return kraId;
	}

	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}

}
