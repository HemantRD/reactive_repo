package com.vinsys.hrms.datamodel;

import java.io.IOException;

import com.vinsys.hrms.util.HRMSHelper;

public class VOMasterDivision extends VOAuditBase {

	private long id;
	private String divisionName;
	private String divisionDescription;
	private VOOrganization organization;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getDivisionDescription() {
		return divisionDescription;
	}

	public void setDivisionDescription(String divisionDescription) {
		this.divisionDescription = divisionDescription;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public static void main(String[] args) {

		try {
			System.err.println(HRMSHelper.createJsonString(new VOMasterDivision()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
