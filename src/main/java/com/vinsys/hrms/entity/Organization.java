package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinsys.hrms.entity.confirmation.MasterEvaluationParameter;

@Entity
@Table(name = "tbl_organization")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Organization implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_organization", sequenceName = "seq_organization", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_organization")
	private Long id;
	@Column(name = "ORG_NAME")
	private String organizationName;
	@Column(name = "ORG_SHORT_NAME")
	private String organizationShortName;
	@OneToOne(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private OrganizationAddress organizationAddress;
	@Column(name = "ORG_SHORT_CODE")
	private String orgShortCode;

	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<Subscription> subscription;

	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<LoginEntity> loginEntities;

	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<LoginEntityType> loginEntityTypes;
	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<MasterDepartment> masterDepartments;
	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<MasterDesignation> masterDesignations;
	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<MasterBranch> masterBranchs;
	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<MasterCandidateActivity> masterCandidateActivities;
//	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
//			CascadeType.PERSIST, CascadeType.REFRESH })
//	private Set<MasterCandidateChecklistAction> masterCandidateChecklistActions;
	
	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<MasterEvaluationParameter> masterEvaluationParameter;
	
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "created_date")
	private Date createdDate;
	@Column(name = "updated_by")
	private String updatedBy;
	@Column(name = "updated_date")
	private Date updatedDate;
	@Column(name = "is_active")
	private String isActive;
	@Column(name = "remark")
	private String remark;

	public Set<MasterCandidateActivity> getMasterCandidateActivities() {
		return masterCandidateActivities;
	}

	public void setMasterCandidateActivities(Set<MasterCandidateActivity> masterCandidateActivities) {
		this.masterCandidateActivities = masterCandidateActivities;
	}

	public Set<MasterBranch> getMasterBranchs() {
		return masterBranchs;
	}

	public void setMasterBranchs(Set<MasterBranch> masterBranchs) {
		this.masterBranchs = masterBranchs;
	}

	public Set<MasterDesignation> getMasterDesignations() {
		return masterDesignations;
	}

	public void setMasterDesignations(Set<MasterDesignation> masterDesignations) {
		this.masterDesignations = masterDesignations;
	}

	public Set<MasterDepartment> getMasterDepartments() {
		return masterDepartments;
	}

	public void setMasterDepartments(Set<MasterDepartment> masterDepartments) {
		this.masterDepartments = masterDepartments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationShortName() {
		return organizationShortName;
	}

	public void setOrganizationShortName(String organizationShortName) {
		this.organizationShortName = organizationShortName;
	}

	public String getOrgShortCode() {
		return orgShortCode;
	}

	public void setOrgShortCode(String orgShortCode) {
		this.orgShortCode = orgShortCode;
	}

	public Set<Subscription> getSubscription() {
		return subscription;
	}

	public void setSubscription(Set<Subscription> subscription) {
		this.subscription = subscription;
	}

	public Set<LoginEntity> getLoginEntities() {
		return loginEntities;
	}

	public void setLoginEntities(Set<LoginEntity> loginEntities) {
		this.loginEntities = loginEntities;
	}

	public OrganizationAddress getOrganizationAddress() {
		return organizationAddress;
	}

	public void setOrganizationAddress(OrganizationAddress organizationAddress) {
		this.organizationAddress = organizationAddress;
	}

	public Set<LoginEntityType> getLoginEntityTypes() {
		return loginEntityTypes;
	}

	public void setLoginEntityTypes(Set<LoginEntityType> loginEntityTypes) {
		this.loginEntityTypes = loginEntityTypes;
	}

//	public Set<MasterCandidateChecklistAction> getMasterCandidateChecklistActions() {
//		return masterCandidateChecklistActions;
//	}
//
//	public void setMasterCandidateChecklistActions(
//			Set<MasterCandidateChecklistAction> masterCandidateChecklistActions) {
//		this.masterCandidateChecklistActions = masterCandidateChecklistActions;
//	}

	public Set<MasterEvaluationParameter> getMasterEvaluationParameter() {
		return masterEvaluationParameter;
	}

	public void setMasterEvaluationParameter(Set<MasterEvaluationParameter> masterEvaluationParameter) {
		this.masterEvaluationParameter = masterEvaluationParameter;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	
	
}
