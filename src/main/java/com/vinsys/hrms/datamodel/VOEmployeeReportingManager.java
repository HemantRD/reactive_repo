package com.vinsys.hrms.datamodel;

import java.io.Serializable;

public class VOEmployeeReportingManager extends VOAuditBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private VOEmployee employee;
    private VOEmployee reportingManager;

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

    public VOEmployee getReportingManager() {
	return reportingManager;
    }

    public void setReportingManager(VOEmployee reportingManager) {
	this.reportingManager = reportingManager;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

}
