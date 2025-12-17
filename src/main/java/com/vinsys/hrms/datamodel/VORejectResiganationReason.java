package com.vinsys.hrms.datamodel;

public class VORejectResiganationReason extends VOAuditBase {
	private Long id;
	private String reasonName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReasonName() {
		return reasonName;
	}
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

}
