package com.vinsys.hrms.datamodel;

public class VOOrganizationAddress extends VOAddress {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private VOOrganization organization;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public VOOrganization getOrganization() {
	return organization;
    }

    public void setOrganization(VOOrganization organization) {
	this.organization = organization;
    }

}
