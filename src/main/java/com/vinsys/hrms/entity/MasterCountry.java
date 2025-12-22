package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_country")
public class MasterCountry extends AuditBase implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_mst_country", sequenceName = "seq_mst_country", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_country")
	private long id;
	@Column(name = "country_name")
	private String countryName;
	@Column(name = "country_description")
	private String countryDescription;
	@OneToMany(mappedBy = "masterCountry", fetch = FetchType.LAZY)
	private Set<MasterState> masterStates;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryDescription() {
		return countryDescription;
	}

	public void setCountryDescription(String countryDescription) {
		this.countryDescription = countryDescription;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<MasterState> getMasterStates() {
		return masterStates;
	}

	public void setMasterStates(Set<MasterState> masterStates) {
		this.masterStates = masterStates;
	}

}
