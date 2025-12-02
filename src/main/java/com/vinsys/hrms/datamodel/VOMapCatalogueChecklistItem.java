package com.vinsys.hrms.datamodel;

public class VOMapCatalogueChecklistItem  extends VOAuditBase{

	private Long id;
	private String name;
	private VOOrganization organization;
	private VOMapCatalogue catalogue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public VOMapCatalogue getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(VOMapCatalogue catalogue) {
		this.catalogue = catalogue;
	}

}
