package com.vinsys.hrms.datamodel;

import java.util.Set;

public class VOMapCatalogue  extends VOAuditBase{

	private long id;
	private String name;
	private String description;
	private VOMasterDepartment department;
	private VOEmployee approver;
	private VOOrganization organization;
	private Set<VOMapCatalogueChecklistItem> catalogueChecklistItems;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public VOMasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(VOMasterDepartment department) {
		this.department = department;
	}

	public VOEmployee getApprover() {
		return approver;
	}

	public void setApprover(VOEmployee approver) {
		this.approver = approver;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public Set<VOMapCatalogueChecklistItem> getCatalogueChecklistItems() {
		return catalogueChecklistItems;
	}

	public void setCatalogueChecklistItems(Set<VOMapCatalogueChecklistItem> catalogueChecklistItems) {
		this.catalogueChecklistItems = catalogueChecklistItems;
	}

}
