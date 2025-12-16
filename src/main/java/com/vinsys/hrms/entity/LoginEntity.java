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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_login_entity")
public class LoginEntity extends AuditBase {

	@Column(name = "API_KEY")
	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_login_entity", sequenceName = "seq_login_entity", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_login_entity")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_id", nullable = false, insertable = false, updatable = false)
	private Organization organization;

	@Column(name = "login_entity_name")
	private String loginEntityName;

	@Column(name = "username")
	private String username;

	@Column(name = "primary_email")
	private String primaryEmail;

	@Column(name = "super_user")
	private String superUser;

	@Column(name = "no_org")
	private String noOrg;

	@Column(name = "first_password")
	private String firstPassword;

	@Column(name = "password")
	private String password;

	@Column(name = "is_first_login")
	private String isFirstLogin;

	@Column(name = "login_hashcode")
	private String loginHashcode;

	@OneToOne(mappedBy = "loginEntity", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Candidate candidate;

	// @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH,
	// CascadeType.MERGE, CascadeType.PERSIST,
	// CascadeType.REFRESH })
	// @JoinColumn(name = "login_entity_type_id")
	// private LoginEntityType loginEntityType;

	@Transient
	private Set<MasterMenu> menuList;

//	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
//	@JoinTable(name = "tbl_map_login_entity_type", joinColumns = { @JoinColumn(name = "login_entity_id", referencedColumnName = "id"),@JoinColumn(name = "org_id",nullable = true)}, 
//		inverseJoinColumns = {@JoinColumn(name = "login_entity_type_id", referencedColumnName = "id")})
//	private Set<LoginEntityType> loginEntityTypes;
	
	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "tbl_map_login_entity_type", joinColumns = @JoinColumn(name = "login_entity_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "login_entity_type_id", referencedColumnName = "id"))
	private Set<LoginEntityType> loginEntityTypes;


	// public LoginEntityType getLoginEntityType() {
	// return loginEntityType;
	// }
	//
	// public void setLoginEntityType(LoginEntityType loginEntityType) {
	// this.loginEntityType = loginEntityType;
	// }

	public Set<MasterMenu> getMenuList() {
		return menuList;
	}

	public void setMenuList(Set<MasterMenu> menuList) {
		this.menuList = menuList;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getLoginEntityName() {
		return loginEntityName;
	}

	public void setLoginEntityName(String loginEntityName) {
		this.loginEntityName = loginEntityName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getSuperUser() {
		return superUser;
	}

	public void setSuperUser(String superUser) {
		this.superUser = superUser;
	}

	public String getNoOrg() {
		return noOrg;
	}

	public void setNoOrg(String noOrg) {
		this.noOrg = noOrg;
	}

	public String getFirstPassword() {
		return firstPassword;
	}

	public void setFirstPassword(String firstPassword) {
		this.firstPassword = firstPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(String isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public String getLoginHashcode() {
		return loginHashcode;
	}

	public void setLoginHashcode(String loginHashcode) {
		this.loginHashcode = loginHashcode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<LoginEntityType> getLoginEntityTypes() {
		return loginEntityTypes;
	}

	public void setLoginEntityTypes(Set<LoginEntityType> loginEntityTypes) {
		this.loginEntityTypes = loginEntityTypes;
	}

}
