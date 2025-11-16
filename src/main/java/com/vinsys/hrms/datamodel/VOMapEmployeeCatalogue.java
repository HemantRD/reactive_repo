package com.vinsys.hrms.datamodel;

import java.util.Date;

public class VOMapEmployeeCatalogue {

	private Long id;
	private VOEmployee employee;
	private VOMapCatalogue catalogue;
	private String status;
	private Date actedOn;
	private String catalogueProof;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public VOEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}

	public VOMapCatalogue getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(VOMapCatalogue catalogue) {
		this.catalogue = catalogue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getActedOn() {
		return actedOn;
	}

	public void setActedOn(Date actedOn) {
		this.actedOn = actedOn;
	}
	
	public String getCatalogueProof() {
		return catalogueProof;
	}
	public void setCatalogueProof(String catalogueProof) {
		this.catalogueProof = catalogueProof;
	}

}
