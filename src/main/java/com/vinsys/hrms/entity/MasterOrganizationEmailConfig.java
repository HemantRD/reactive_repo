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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_organization_email_config")
public class MasterOrganizationEmailConfig extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_mst_organization_email_config", sequenceName = "seq_mst_organization_email_config", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_organization_email_config")
	private long id;
	@Column(name = "smtp_mail_server")
	private String smtpMailServer;
	@Column(name = "smtp_port")
	private String smtpPort;
	@Column(name = "is_smtp_auth")
	private String smtpAuth;
	@Column(name = "smtp_over_ssl_or_tls")
	private String smtpOverSSLorTLS;
	@Column(name = "from_name")
	private String from_name;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "email_title")
	private String emailTitle;
	@Column(name = "disclaimer")
	private String disclaimer;
	@Column(name = "hr_name")
	private String hrName;
	@Column(name = "hr_email_id")
	private String hrEmailId;
	@Column(name = "group_email_id")
	private String groupEmailId;
	@Column(name = "server_ip")
	private String server_ip;
//	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
//			CascadeType.REFRESH })
//	@JoinColumn(name = "organization_id")
//	private Organization organization;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "division_id")
	private MasterDivision division;
	@Column(name = "org_date_format")
	private String orginationDateFormat;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_level_employee_id")
	private Employee orgLevelEmployee;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSmtpMailServer() {
		return smtpMailServer;
	}

	public void setSmtpMailServer(String smtpMailServer) {
		this.smtpMailServer = smtpMailServer;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String isSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public String getSmtpOverSSLorTLS() {
		return smtpOverSSLorTLS;
	}

	public void setSmtpOverSSLorTLS(String smtpOverSSLorTLS) {
		this.smtpOverSSLorTLS = smtpOverSSLorTLS;
	}

	public String getFrom_name() {
		return from_name;
	}

	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailTitle() {
		return emailTitle;
	}

	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getHrName() {
		return hrName;
	}

	public void setHrName(String hrName) {
		this.hrName = hrName;
	}

	public String getHrEmailId() {
		return hrEmailId;
	}

	public void setHrEmailId(String hrEmailId) {
		this.hrEmailId = hrEmailId;
	}

	public String getServer_ip() {
		return server_ip;
	}

	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}

//	public Organization getOrganization() {
//		return organization;
//	}
//
//	public void setOrganization(Organization organization) {
//		this.organization = organization;
//	}

	public MasterDivision getDivision() {
		return division;
	}

	public void setDivision(MasterDivision division) {
		this.division = division;
	}

	public String getGroupEmailId() {
		return groupEmailId;
	}

	public void setGroupEmailId(String groupEmailId) {
		this.groupEmailId = groupEmailId;
	}

	public String getSmtpAuth() {
		return smtpAuth;
	}

	public String getOrginationDateFormat() {
		return orginationDateFormat;
	}

	public void setOrginationDateFormat(String orginationDateFormat) {
		this.orginationDateFormat = orginationDateFormat;
	}

	public Employee getOrgLevelEmployee() {
		return orgLevelEmployee;
	}

	public void setOrgLevelEmployee(Employee orgLevelEmployee) {
		this.orgLevelEmployee = orgLevelEmployee;
	}

	
   
}
