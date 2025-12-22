package com.vinsys.hrms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_map_employee_catalogue_checklist")
public class MapEmployeeCatalogueChecklist extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_map_employee_catalogue_checklist", sequenceName = "seq_map_employee_catalogue_checklist", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_employee_catalogue_checklist")
	private Long id;
	@Column(name = "comment")
	private String comment;
	@Column(name = "amount")
	private double amount;
	@Column(name = "collected")
	private boolean haveCollected;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_catalogue_mapping_id", nullable = false)
	private MapEmployeeCatalogue employeeCatalogueMapping;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "checklist_id", nullable = false)
	private MapCatalogueChecklistItem catalogueChecklist;
	
	
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
	public MapEmployeeCatalogue getEmployeeCatalogueMapping() {
		return employeeCatalogueMapping;
	}
	public void setEmployeeCatalogueMapping(MapEmployeeCatalogue employeeCatalogueMapping) {
		this.employeeCatalogueMapping = employeeCatalogueMapping;
	}
	public MapCatalogueChecklistItem getCatalogueChecklist() {
		return catalogueChecklist;
	}
	public void setCatalogueChecklist(MapCatalogueChecklistItem catalogueChecklist) {
		this.catalogueChecklist = catalogueChecklist;
	}

	

}
