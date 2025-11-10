package com.vinsys.hrms.employee.vo;

public class ChecklistSubmitVo {

	private Long id;
	private String comment;
	private double amount;
	private boolean haveCollected;
	private Long catalogueId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isHaveCollected() {
		return haveCollected;
	}

	public void setHaveCollected(boolean haveCollected) {
		this.haveCollected = haveCollected;
	}

	public Long getCatalogueId() {
		return catalogueId;
	}

	public void setCatalogueId(Long catalogueId) {
		this.catalogueId = catalogueId;
	}
	
}
