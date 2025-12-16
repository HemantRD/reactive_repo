package com.vinsys.hrms.datamodel.traveldesk;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOCancelTravelRequest extends VOAuditBase {

	private long id;
	private VOTravelRequest travelRequest;
	private long childId;
	private String childType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getChildId() {
		return childId;
	}

	public void setChildId(long childId) {
		this.childId = childId;
	}

	public String getChildType() {
		return childType;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public VOTravelRequest getTravelRequest() {
		return travelRequest;
	}

	public void setTravelRequest(VOTravelRequest travelRequest) {
		this.travelRequest = travelRequest;
	}

}
