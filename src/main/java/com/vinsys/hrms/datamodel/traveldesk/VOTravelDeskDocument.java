package com.vinsys.hrms.datamodel.traveldesk;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOTravelDeskDocument extends VOAuditBase {

	private long id;
	private VOTravelRequest travelRequestId;
	private long childId;
	private String childType;
	private String documentName;
	private String location;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOTravelRequest getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(VOTravelRequest travelRequestId) {
		this.travelRequestId = travelRequestId;
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

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
