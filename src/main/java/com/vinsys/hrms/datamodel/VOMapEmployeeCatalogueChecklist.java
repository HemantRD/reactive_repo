package com.vinsys.hrms.datamodel;

public class VOMapEmployeeCatalogueChecklist extends VOAuditBase{

	private Long id;
	private String comment;
	private double amount;
	private boolean haveCollected;
	private VOMapEmployeeCatalogue employeeCatalogueMapping;
	private VOMapCatalogueChecklistItem catalogueChecklist;

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

	public VOMapEmployeeCatalogue getEmployeeCatalogueMapping() {
		return employeeCatalogueMapping;
	}

	public void setEmployeeCatalogueMapping(VOMapEmployeeCatalogue employeeCatalogueMapping) {
		this.employeeCatalogueMapping = employeeCatalogueMapping;
	}

	public VOMapCatalogueChecklistItem getCatalogueChecklist() {
		return catalogueChecklist;
	}

	public void setCatalogueChecklist(VOMapCatalogueChecklistItem catalogueChecklist) {
		this.catalogueChecklist = catalogueChecklist;
	}

}
