package com.vinsys.hrms.datamodel.assets;

import com.vinsys.hrms.datamodel.VOMasterDivision;
import com.vinsys.hrms.datamodel.VOOrganization;
import com.vinsys.hrms.entity.AuditBase;

public class VOMasterAssetType extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private VOOrganization organization;

	private VOMasterDivision division;

	private String assetTypeName;

	private String assetTypeCode;

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

	public VOMasterDivision getDivision() {
		return division;
	}

	public void setDivision(VOMasterDivision division) {
		this.division = division;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAssetTypeName() {
		return assetTypeName;
	}

	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}

	public String getAssetTypeCode() {
		return assetTypeCode;
	}

	public void setAssetTypeCode(String assetTypeCode) {
		this.assetTypeCode = assetTypeCode;
	}

}
