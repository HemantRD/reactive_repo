package com.vinsys.hrms.master.vo;

import com.vinsys.hrms.datamodel.VOMasterRole;

public class LoginEntityTypeVO {

	private long id;
	private String loginEntityTypeName;
	private VOMasterRole role;
	private String description;
	private String details;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getLoginEntityTypeName() {
		return loginEntityTypeName;
	}
	public void setLoginEntityTypeName(String loginEntityTypeName) {
		this.loginEntityTypeName = loginEntityTypeName;
	}
	public VOMasterRole getRole() {
		return role;
	}
	public void setRole(VOMasterRole role) {
		this.role = role;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
}
