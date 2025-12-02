package com.vinsys.hrms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_employee_additional_role")
public class EmployeeAdditionalRole extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_employee_additional_role", sequenceName = "seq_employee_additional_role", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_additional_role")
	private long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_current_detail_id")
	private EmployeeCurrentDetail employeeCurrentDetail;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "additional_login_entity_id")
	private LoginEntityType loginEntityType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public EmployeeCurrentDetail getEmployeeCurrentDetail() {
		return employeeCurrentDetail;
	}

	public void setEmployeeCurrentDetail(EmployeeCurrentDetail employeeCurrentDetail) {
		this.employeeCurrentDetail = employeeCurrentDetail;
	}

	public LoginEntityType getLoginEntityType() {
		return loginEntityType;
	}

	public void setLoginEntityType(LoginEntityType loginEntityType) {
		this.loginEntityType = loginEntityType;
	}

}
