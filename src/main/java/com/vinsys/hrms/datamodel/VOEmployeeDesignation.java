package com.vinsys.hrms.datamodel;

import java.io.Serializable;


public class VOEmployeeDesignation extends VOAuditBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private VOEmployee employee;

    private VOMasterDesignation designation;

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

    public VOMasterDesignation getDesignation() {
	return designation;
    }

    public void setDesignation(VOMasterDesignation designation) {
	this.designation = designation;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

}
