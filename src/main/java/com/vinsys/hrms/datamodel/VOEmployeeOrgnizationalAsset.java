package com.vinsys.hrms.datamodel;

import java.io.Serializable;
import java.util.Date;

public class VOEmployeeOrgnizationalAsset extends VOAuditBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private VOEmployee employee;
    private String make;
    private String assetName;
    private String serialNumber;
    private String comment;
    private Date issuedOnDate;

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

    public String getMake() {
	return make;
    }

    public void setMake(String make) {
	this.make = make;
    }

    public String getAssetName() {
	return assetName;
    }

    public void setAssetName(String assetName) {
	this.assetName = assetName;
    }

    public String getSerialNumber() {
	return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
	this.serialNumber = serialNumber;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public Date getIssuedOnDate() {
	return issuedOnDate;
    }

    public void setIssuedOnDate(Date issuedOnDate) {
	this.issuedOnDate = issuedOnDate;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

}
