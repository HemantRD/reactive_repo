package com.vinsys.hrms.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_map_catalogue")
public class MapCatalogue extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_map_catalogue", sequenceName = "seq_map_catalogue", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_catalogue")
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "department_id")
	private MasterDepartment department;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "approver_id", nullable = true)
	private Employee approver;
	@OneToMany(mappedBy = "catalogue", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<MapCatalogueChecklistItem> catalogueChecklistItems;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "division_id", nullable = false)
	private MasterDivision division;

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

	public MasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(MasterDepartment department) {
		this.department = department;
	}

	public Employee getApprover() {
		return approver;
	}

	public void setApprover(Employee approver) {
		this.approver = approver;
	}

	public Set<MapCatalogueChecklistItem> getCatalogueChecklistItems() {
		return catalogueChecklistItems;
	}

	public void setCatalogueChecklistItems(Set<MapCatalogueChecklistItem> catalogueChecklistItems) {
		this.catalogueChecklistItems = catalogueChecklistItems;
	}

	public MasterDivision getDevision() {
		return division;
	}

	public void setDevision(MasterDivision devision) {
		this.division = devision;
	}

}
