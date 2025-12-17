package com.vinsys.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_candidate_checklist_action")
public class MasterCandidateChecklistAction extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_mst_candidate_checklist_action", sequenceName = "seq_mst_candidate_checklist_action", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_candidate_checklist_action")
	private long id;
	@Column(name = "checklist_action_name")
	private String candidateChecklistActionName;
	@Column(name = "checklist_action_description")
	private String candidateChecklistActionDescription;
//	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
//			CascadeType.REFRESH })
//	@JoinColumn(name = "organization_id")
//	private Organization organization;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCandidateChecklistActionName() {
		return candidateChecklistActionName;
	}

	public void setCandidateChecklistActionName(String candidateChecklistActionName) {
		this.candidateChecklistActionName = candidateChecklistActionName;
	}

	public String getCandidateChecklistActionDescription() {
		return candidateChecklistActionDescription;
	}

	public void setCandidateChecklistActionDescription(String candidateChecklistActionDescription) {
		this.candidateChecklistActionDescription = candidateChecklistActionDescription;
	}

//	public Organization getOrganization() {
//		return organization;
//	}
//
//	public void setOrganization(Organization organization) {
//		this.organization = organization;
//	}

}
