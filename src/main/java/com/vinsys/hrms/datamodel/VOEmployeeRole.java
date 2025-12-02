package com.vinsys.hrms.datamodel;

public class VOEmployeeRole extends VOAuditBase {

    private long id;
    private VOEmployee employee;
    private VOMasterRole role;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public VOEmployee getEmployee() {
	return employee;
    }

    public void setEmployee(VOEmployee employee) {
	this.employee = employee;
    }

    public VOMasterRole getRole() {
	return role;
    }

    public void setRole(VOMasterRole role) {
	this.role = role;
    }

}
