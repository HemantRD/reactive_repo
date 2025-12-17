package com.vinsys.hrms.datamodel;

import java.io.Serializable;

public class VOEmployeeDepartment extends VOAuditBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private VOEmployee employee;
    private VOMasterDepartment department;

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

    public VOMasterDepartment getDepartment() {
	return department;
    }

    public void setDepartment(VOMasterDepartment department) {
	this.department = department;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

}
