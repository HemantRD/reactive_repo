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
@Table(name = "tbl_map_org_mode_of_separation")
public class MasterModeofSeparation extends AuditBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_mst_mode_of_separation", sequenceName = "seq_mst_mode_of_separation", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_mode_of_separation")
	private Long id;
	@Column(name = "mode_of_separation_name")
	private String modeOfSeparationName;

	@Column(name = "mode_of_separation_code")
	private String modeOfSeparationCode;
	
//	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
//			CascadeType.REFRESH })
//	@JoinColumn(name = "organization_id")
//	private Organization organization;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "division_id")
	private MasterDivision division;
	


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
//	public Organization getOrganization() {
//		return organization;
//	}
//	public void setOrganization(Organization organization) {
//		this.organization = organization;
//	}
	
	public MasterDivision getDivision() {
		return division;
	}
	public void setDivision(MasterDivision division) {
		this.division = division;
	}
	public String getModeOfSeparationName() {
		return modeOfSeparationName;
	}
	public void setModeOfSeparationName(String modeOfSeparationName) {
		this.modeOfSeparationName = modeOfSeparationName;
	}
	public String getModeOfSeparationCode() {
		return modeOfSeparationCode;
	}
	public void setModeOfSeparationCode(String modeOfSeparationCode) {
		this.modeOfSeparationCode = modeOfSeparationCode;
	}
	
	
}
