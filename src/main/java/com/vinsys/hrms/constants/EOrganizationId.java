package com.vinsys.hrms.constants;

public enum EOrganizationId {

	VINSYS(1L), LRYPT(2L);

	private final Long recordStatus;

	private EOrganizationId(Long recordStatus) {
		this.recordStatus = recordStatus;
	}

	@Override
	public String toString() {
		return String.valueOf(recordStatus);
	}
}
