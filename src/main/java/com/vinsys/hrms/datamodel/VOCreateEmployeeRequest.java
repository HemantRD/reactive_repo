package com.vinsys.hrms.datamodel;

import java.util.Set;

public class VOCreateEmployeeRequest extends VOAuditBase {

	private Set<VOLoginEntityType> loginEntityTypes;
	private VOEmployee employee;

	public Set<VOLoginEntityType> getLoginEntityTypes() {
		return loginEntityTypes;
	}

	public void setLoginEntityTypes(Set<VOLoginEntityType> loginEntityTypes) {
		this.loginEntityTypes = loginEntityTypes;
	}

	public VOEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}

}
