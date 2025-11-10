package com.vinsys.hrms.master.vo;

public class EmploymentTypeVO {
	private Long id;
	private String employmentTypeName;
	private String employmentTypeDescription;
	private String isActive;
	
	
public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	//	public long getId() {
//		return id;
//	}
//	public void setId(long id) {
//		this.id = id;
//	}
	public String getEmploymentTypeName() {
		return employmentTypeName;
	}
	public void setEmploymentTypeName(String employmentTypeName) {
		this.employmentTypeName = employmentTypeName;
	}
	public String getEmploymentTypeDescription() {
		return employmentTypeDescription;
	}
	public void setEmploymentTypeDescription(String employmentTypeDescription) {
		this.employmentTypeDescription = employmentTypeDescription;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	
}
