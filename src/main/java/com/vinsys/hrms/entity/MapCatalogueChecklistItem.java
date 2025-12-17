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
@Table(name = "tbl_map_catalogue_checklist")
public class MapCatalogueChecklistItem extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_map_catalogue_checklist", sequenceName = "seq_map_catalogue_checklist", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_catalogue_checklist")
	private Long id;
	@Column(name = "name")
	private String name;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "catalogue_id", nullable = false)
	private MapCatalogue catalogue;

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

	public MapCatalogue getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(MapCatalogue catalogue) {
		this.catalogue = catalogue;
	}

}
