package com.vinsys.hrms.master.vo;

//import com.vinsys.hrms.entity.Organization;

public class BranchVO {
	private Long id;
	private String branchName;
	private String branchDescription;
	//private Organization organization;
	private long organizationId;
//	private long departmentId;
	private String isActive;
	
		public String getBranchName() {
		return branchName;
	}
	public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchDescription() {
		return branchDescription;
	}
	public void setBranchDescription(String branchDescription) {
		this.branchDescription = branchDescription;
	}
//	public Organization getOrganization() {
//		return organization;
//	}
//	public void setOrganization(Organization organization) {
//		this.organization = organization;
//	}
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
//	public long getDepartmentId() {
//		return departmentId;
//	}
//	public void setDepartmentId(long departmentId) {
//		this.departmentId = departmentId;
//	}
//	
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	

}
