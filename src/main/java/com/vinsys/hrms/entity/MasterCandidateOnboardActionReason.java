package com.vinsys.hrms.entity;

import java.io.Serializable;

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
@Table(name = "tbl_mst_candidate_onboard_action_reason")
public class MasterCandidateOnboardActionReason extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_mst_candidate_onboard_action_reason", sequenceName = "seq_mst_candidate_onboard_action_reason", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_candidate_onboard_action_reason")
	private long id;
	@Column(name = "onboard_action_reason_name")
	private String onboardActionReasonName;
	@Column(name = "onboard_action_reason_description")
	private String onboardActionReasonDescription;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_id", insertable = false, updatable = false)
	private Organization organization;
	@Column(name = "type_of_action")
	private String typeOfAction;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOnboardActionReasonName() {
		return onboardActionReasonName;
	}

	public void setOnboardActionReasonName(String onboardActionReasonName) {
		this.onboardActionReasonName = onboardActionReasonName;
	}

	public String getOnboardActionReasonDescription() {
		return onboardActionReasonDescription;
	}

	public void setOnboardActionReasonDescription(String onboardActionReasonDescription) {
		this.onboardActionReasonDescription = onboardActionReasonDescription;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTypeOfAction() {
		return typeOfAction;
	}

	public void setTypeOfAction(String typeOfAction) {
		this.typeOfAction = typeOfAction;
	}

}
