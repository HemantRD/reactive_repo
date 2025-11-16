package com.vinsys.hrms.datamodel.traveldesk;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOTravelCancelRequestModel extends VOAuditBase {

	private boolean cancelTicket;
	private boolean cancelCab;
	private boolean cancelAccommodation;
	private long parentId;
	private VOTraveldeskComment cancelReqComment;

	public boolean isCancelTicket() {
		return cancelTicket;
	}

	public void setCancelTicket(boolean cancelTicket) {
		this.cancelTicket = cancelTicket;
	}

	public boolean isCancelCab() {
		return cancelCab;
	}

	public void setCancelCab(boolean cancelCab) {
		this.cancelCab = cancelCab;
	}

	public boolean isCancelAccommodation() {
		return cancelAccommodation;
	}

	public void setCancelAccommodation(boolean cancelAccommodation) {
		this.cancelAccommodation = cancelAccommodation;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public VOTraveldeskComment getCancelReqComment() {
		return cancelReqComment;
	}

	public void setCancelReqComment(VOTraveldeskComment cancelReqComment) {
		this.cancelReqComment = cancelReqComment;
	}

}
