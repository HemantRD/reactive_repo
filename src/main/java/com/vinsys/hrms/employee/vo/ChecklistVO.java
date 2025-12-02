package com.vinsys.hrms.employee.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vinsys.hrms.datamodel.VOMapCatalogueChecklistItem;
import com.vinsys.hrms.datamodel.VOMapEmployeeCatalogue;
import com.vinsys.hrms.entity.AuditBase;

public class ChecklistVO extends AuditBase{

	private Long id;
	private String comment;
	private double amount;
	private boolean haveCollected;
	@JsonIgnore
	private VOMapEmployeeCatalogue employeeCatalogueMapping;
	@JsonIgnore
	private VOMapCatalogueChecklistItem catalogueChecklist;
	private String checklistItem;
	private String checklistApprover;
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

	public String getChecklistItem() {
		return checklistItem;
	}

	public void setChecklistItem(String checklistItem) {
		this.checklistItem = checklistItem;
	}

	public String getChecklistApprover() {
		return checklistApprover;
	}

	public void setChecklistApprover(String checklistApprover) {
		this.checklistApprover = checklistApprover;
	}

	public Long getCatalogueId() {
		return catalogueId;
	}

	public void setCatalogueId(Long catalogueId) {
		this.catalogueId = catalogueId;
	}

}