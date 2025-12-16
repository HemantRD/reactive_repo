package com.vinsys.hrms.entity;

import java.util.Date;

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
@Table(name = "tbl_map_employee_catalogue")
public class MapEmployeeCatalogue extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_map_employee_catalogue", sequenceName = "seq_map_employee_catalogue", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_employee_catalogue")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "resigned_employee_id", nullable = false)
	private Employee resignedEmployee;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "catalogue_id", nullable = false)
	private MapCatalogue catalogue;
	@Column(name = "status")
	private String status;
	@Column(name = "acted_on")
	private Date actedOn;
	
	@Column(name = "catalogue_proof")
	private String catalogueProof;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getResignedEmployee() {
		return resignedEmployee;
	}

	public void setResignedEmployee(Employee resignedEmployee) {
		this.resignedEmployee = resignedEmployee;
	}

	public MapCatalogue getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(MapCatalogue catalogue) {
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
