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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_login_entity_type")
public class LoginEntityType extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_login_entity_type", sequenceName = "seq_login_entity_type", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_login_entity_type")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_id",insertable=false,updatable =false)
	private Organization organization;
	@Column(name = "login_entity_type_name")
	private String loginEntityTypeName;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "role_id")
	private MasterRole role;

	@Column(name = "description")
	private String description;

	@Column(name = "details")
	private String details;

	@ManyToMany(mappedBy = "loginEntityTypes", fetch = FetchType.LAZY)
	private Set<LoginEntity> loginEntities;

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

	public String getLoginEntityTypeName() {
		return loginEntityTypeName;
	}

	public void setLoginEntityTypeName(String loginEntityTypeName) {
		this.loginEntityTypeName = loginEntityTypeName;
	}

	public MasterRole getRole() {
		return role;
	}

	public void setRole(MasterRole role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Set<LoginEntity> getLoginEntities() {
		return loginEntities;
	}

	public void setLoginEntities(Set<LoginEntity> loginEntities) {
		this.loginEntities = loginEntities;
	}

}
