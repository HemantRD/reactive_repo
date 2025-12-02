package com.vinsys.hrms.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_candidate_activity_action_type")
public class MasterCandidateActivityActionType extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_mst_candidate_activity_action_type", sequenceName = "seq_mst_candidate_activity_action_type", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_candidate_activity_action_type")
	private long id;
	@Column(name = "activity_action_type_name")
	private String activityActionTypeName;
	@Column(name = "activity_action_type_description")
	private String activityActionTypeDescription;
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

	public String getActivityActionTypeName() {
		return activityActionTypeName;
	}

	public void setActivityActionTypeName(String activityActionTypeName) {
		this.activityActionTypeName = activityActionTypeName;
	}

	public String getActivityActionTypeDescription() {
		return activityActionTypeDescription;
	}

	public void setActivityActionTypeDescription(String activityActionTypeDescription) {
		this.activityActionTypeDescription = activityActionTypeDescription;
	}

//	public Organization getOrganization() {
//		return organization;
//	}
//
//	public void setOrganization(Organization organization) {
//		this.organization = organization;
//	}

}
