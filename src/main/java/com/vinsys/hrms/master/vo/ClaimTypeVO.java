package com.vinsys.hrms.master.vo;



public class ClaimTypeVO {

	private Long id;
	private Long claimCategory;
	private String claimType;
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public Long getClaimCategory() {
		return claimCategory;
	}

	public void setClaimCategory(Long claimCategory) {
		this.claimCategory = claimCategory;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
