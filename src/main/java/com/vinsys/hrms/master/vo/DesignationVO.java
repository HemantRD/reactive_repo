package com.vinsys.hrms.master.vo;

//import com.vinsys.hrms.entity.Organization;

public class DesignationVO {
	private Long id;
	private String designationName;
	private String designationDescription;
	//private Organization organization;
	private Long organizationId;
	private String isActive;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	public String getDesignationDescription() {
		return designationDescription;
	}
	public void setDesignationDescription(String designationDescription) {
		this.designationDescription = designationDescription;
	}
//	public Organization getOrganization() {
//		return organization;
//	}
//	public void setOrganization(Organization organization) {
//		this.organization = organization;
//	}
//	
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	

}
