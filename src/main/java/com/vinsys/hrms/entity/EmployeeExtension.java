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
@Table(name = "tbl_trn_employee_extension")
public class EmployeeExtension extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_employee_extension", sequenceName = "seq_employee_extension", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_extension")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id")
	private Employee employee;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_id", nullable = false,insertable=false ,updatable=false)
	private Organization organization;

	public boolean isEmployeeCheck() {
		return employeeCheck;
	}

	public void setEmployeeCheck(boolean employeeCheck) {
		this.employeeCheck = employeeCheck;
	}

	@Column(name = "extension_number")
	private String extensionNumber;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "extension_type_id")
	private MasterExtensionType masterExtensionType;

	@Column(name = "is_employee")
	private boolean employeeCheck;

	@Column(name = "other")
	private String other;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public MasterExtensionType getMasterExtensionType() {
		return masterExtensionType;
	}

	public void setMasterExtensionType(MasterExtensionType masterExtensionType) {
		this.masterExtensionType = masterExtensionType;
	}

}
