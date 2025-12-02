package com.vinsys.hrms.datamodel;

import java.io.Serializable;


public class VOEmployeeBranch extends VOAuditBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private VOEmployee employee;
    private VOMasterBranch branch;

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

    public VOMasterBranch getBranch() {
	return branch;
    }

    public void setBranch(VOMasterBranch branch) {
	this.branch = branch;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

}
