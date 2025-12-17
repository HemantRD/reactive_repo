package com.vinsys.hrms.datamodel.assets;

import com.vinsys.hrms.datamodel.VOAuditBase;
import com.vinsys.hrms.datamodel.VOEmployee;

public class VOCompanyAsset extends VOAuditBase {

	private long id;

	private VOMasterAssetType masterAssetType;

	private VOEmployee employee;

	private String manufacturer;

	private String model;

	private String serialNumber;

	private String dateOfIssue;

	private float quantity;

	private String status;

	private String comment;
	
	private long loggedInEmpId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getDateOfIssue() {
		return dateOfIssue;
	}

	public void setDateOfIssue(String dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public VOMasterAssetType getMasterAssetType() {
		return masterAssetType;
	}

	public void setMasterAssetType(VOMasterAssetType masterAssetType) {
		this.masterAssetType = masterAssetType;
	}

	public VOEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}

	public long getLoggedInEmpId() {
		return loggedInEmpId;
	}

	public void setLoggedInEmpId(long loggedInEmpId) {
		this.loggedInEmpId = loggedInEmpId;
	}

}
