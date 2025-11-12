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
@Table(name = "tbl_mst_candidate_checklist")
public class MasterCandidateChecklist extends AuditBase {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_mst_candidate_checklist", sequenceName = "seq_mst_candidate_checklist", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_candidate_checklist")
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "division_id")	
	private MasterDivision masterDivision;

	public MasterDivision getMasterDivision() {
		return masterDivision;
	}

	public void setMasterdivision(MasterDivision masterDivision) {
		this.masterDivision = masterDivision;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_id",insertable = false ,updatable = false)
	private Organization organization;

	@Column(name = "checklist_template")
	private String checklistTemplate;

	@Column(name = "checklist_item")
	private String checklistItem;
	
	@Column(name = "checklist_item_code")
	private String checklistItemCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getChecklistTemplate() {
		return checklistTemplate;
	}

	public void setChecklistTemplate(String checklistTemplate) {
		this.checklistTemplate = checklistTemplate;
	}

	public String getChecklistItem() {
		return checklistItem;
	}

	public void setChecklistItem(String checklistItem) {
		this.checklistItem = checklistItem;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getChecklistItemCode() {
		return checklistItemCode;
	}

	public void setChecklistItemCode(String checklistItemCode) {
		this.checklistItemCode = checklistItemCode;
	}
	
}
