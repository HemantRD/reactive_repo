package com.vinsys.hrms.traveldesk.vo;

import com.vinsys.hrms.master.vo.TravelApproverResponseVO;

public class SaveTravelRequestVO {
	private Long id;

	private TicketTravelRequestVO ticketDetails;
	private CabTravelRequestVO cabDetails;
	private AccommodationTravelRequestVO accommodationDetails;
	private TravelApproverResponseVO approverDetails;

	public TicketTravelRequestVO getTicketDetails() {
		return ticketDetails;
	}

	public void setTicketDetails(TicketTravelRequestVO ticketDetails) {
		this.ticketDetails = ticketDetails;
	}

	public CabTravelRequestVO getCabDetails() {
		return cabDetails;
	}

	public void setCabDetails(CabTravelRequestVO cabDetails) {
		this.cabDetails = cabDetails;
	}

	public AccommodationTravelRequestVO getAccommodationDetails() {
		return accommodationDetails;
	}

	public void setAccommodationDetails(AccommodationTravelRequestVO accommodationDetails) {
		this.accommodationDetails = accommodationDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TravelApproverResponseVO getApproverDetails() {
		return approverDetails;
	}

	public void setApproverDetails(TravelApproverResponseVO approverDetails) {
		this.approverDetails = approverDetails;
	}

}
