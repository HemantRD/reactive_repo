package com.vinsys.hrms.datamodel;

import java.io.Serializable;

public class VOCandidateAddress extends VOAddress implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String addressType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// private Candidate candidate;

}
