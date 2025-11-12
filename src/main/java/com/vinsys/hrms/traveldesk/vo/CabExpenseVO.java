package com.vinsys.hrms.traveldesk.vo;

/**
 * @author Onkar A
 *
 * 
 */
public class CabExpenseVO {
	private Long id;
	private Float approximateTicketCost;
	private Float finalTicketCost;
	private Float refundCost;
	private Float setteledCost;
	private String approximateDistance;
	private String actualDistance;

	public Float getApproximateTicketCost() {
		return approximateTicketCost;
	}

	public void setApproximateTicketCost(Float approximateTicketCost) {
		this.approximateTicketCost = approximateTicketCost;
	}

	public Float getFinalTicketCost() {
		return finalTicketCost;
	}

	public void setFinalTicketCost(Float finalTicketCost) {
		this.finalTicketCost = finalTicketCost;
	}

	public Float getRefundCost() {
		return refundCost;
	}

	public void setRefundCost(Float refundCost) {
		this.refundCost = refundCost;
	}

	public Float getSetteledCost() {
		return setteledCost;
	}

	public void setSetteledCost(Float setteledCost) {
		this.setteledCost = setteledCost;
	}

	public String getApproximateDistance() {
		return approximateDistance;
	}

	public void setApproximateDistance(String approximateDistance) {
		this.approximateDistance = approximateDistance;
	}

	public String getActualDistance() {
		return actualDistance;
	}

	public void setActualDistance(String actualDistance) {
		this.actualDistance = actualDistance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
