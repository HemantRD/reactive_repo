package com.vinsys.hrms.datamodel;

import java.io.Serializable;

public class VOEmployeeEmploymentType extends VOAuditBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private VOEmployee employee;
    private VOMasterEmploymentType employmentType;

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

    public VOMasterEmploymentType getEmploymentType() {
	return employmentType;
    }

    public void setEmploymentType(VOMasterEmploymentType employmentType) {
	this.employmentType = employmentType;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

}
