package com.vinsys.hrms.datamodel.traveldesk;

public class VOAccommodationGuest extends VOTravellerDetails {

	private long id;
	private VOAccommodationRequest accomodationReq;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOAccommodationRequest getAccomodationReq() {
		return accomodationReq;
	}

	public void setAccomodationReq(VOAccommodationRequest accomodationReq) {
		this.accomodationReq = accomodationReq;
	}

}
