package com.vinsys.hrms.datamodel;

import java.io.Serializable;

public class VOEmployeeDivision extends VOAuditBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private VOEmployee employee;
    private VOMasterDivision division;

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

    public VOMasterDivision getDivision() {
	return division;
    }

    public void setDivision(VOMasterDivision division) {
	this.division = division;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

}
