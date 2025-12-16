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
@Table(name = "tbl_map_org_separation_reason")
public class MasterModeofSeparationReason extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_mst_mode_of_separation_reason", sequenceName = "seq_mst_mode_of_separation_reason", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_mode_of_separation_reason")
	private Long id;
	@Column(name = "reason_name")
	private String reasonName;

	@Column(name = "resign_action_type")
	private String resignActionType;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "mode_of_separation_id")
	private MasterModeofSeparation masterModeofSeparation;

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

	public MasterDivision getDivision() {
		return division;
	}

	public void setDivision(MasterDivision division) {
		this.division = division;
	}

	public String getReasonName() {
		return reasonName;
	}

	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	public MasterModeofSeparation getMasterModeofSeparation() {
		return masterModeofSeparation;
	}

	public void setMasterModeofSeparation(MasterModeofSeparation masterModeofSeparation) {
		this.masterModeofSeparation = masterModeofSeparation;
	}

	public String getResignActionType() {
		return resignActionType;
	}

	public void setResignActionType(String resignActionType) {
		this.resignActionType = resignActionType;
	}

}
