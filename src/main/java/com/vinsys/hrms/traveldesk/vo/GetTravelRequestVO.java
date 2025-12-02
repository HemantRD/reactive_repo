package com.vinsys.hrms.traveldesk.vo;

import java.util.List;

import com.vinsys.hrms.master.vo.CurrencyMasterVO;
import com.vinsys.hrms.master.vo.TravelApproverResponseVO;

public class GetTravelRequestVO {

	private TicketTravelRequestVO ticketDetails;
	private CabTravelRequestVO cabDetails;
	private AccommodationTravelRequestVO accommodationDetails;
	private Float totalApproximateCost;
	private Float totalFinalCost;
	private TravelApproverResponseVO approverDetails;
	private String approverComment;
	private List<AvailableTicketBooking> availableTicketBooking;

	// WFStatus
	private String status;
	private String pendingWith;
	
	private List<TravelDocumentResponseVO> travelDocumentDetails;
	
	private CurrencyMasterVO currency;
	
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

	public Float getTotalApproximateCost() {
		return totalApproximateCost;
	}

	public void setTotalApproximateCost(Float totalApproximateCost) {
		this.totalApproximateCost = totalApproximateCost;
	}

	public Float getTotalFinalCost() {
		return totalFinalCost;
	}

	public void setTotalFinalCost(Float totalFinalCost) {
		this.totalFinalCost = totalFinalCost;
	}

	public String getApproverComment() {
		return approverComment;
	}

	public void setApproverComment(String approverComment) {
		this.approverComment = approverComment;
	}

	public TravelApproverResponseVO getApproverDetails() {
		return approverDetails;
	}

	public void setApproverDetails(TravelApproverResponseVO approverDetails) {
		this.approverDetails = approverDetails;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public List<AvailableTicketBooking> getAvailableTicketBooking() {
		return availableTicketBooking;
	}

	public void setAvailableTicketBooking(List<AvailableTicketBooking> availableTicketBooking) {
		this.availableTicketBooking = availableTicketBooking;
	}

	public List<TravelDocumentResponseVO> getTravelDocumentDetails() {
		return travelDocumentDetails;
	}

	public void setTravelDocumentDetails(List<TravelDocumentResponseVO> travelDocumentDetails) {
		this.travelDocumentDetails = travelDocumentDetails;
	}

	public CurrencyMasterVO getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyMasterVO currency) {
		this.currency = currency;
	}


}
