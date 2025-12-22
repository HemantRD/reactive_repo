package com.vinsys.hrms.master.vo;

public class SelfRatingVo {

	private Long id;
	private Double value;
	private String label;
	private Long categoryId;
	private String isPassFail;
	private String onOccurrence;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getIsPassFail() {
		return isPassFail;
	}
	public void setIsPassFail(String isPassFail) {
		this.isPassFail = isPassFail;
	}
	public String getOnOccurrence() {
		return onOccurrence;
	}
	public void setOnOccurrence(String onOccurrence) {
		this.onOccurrence = onOccurrence;
	}
	
	
	
}
