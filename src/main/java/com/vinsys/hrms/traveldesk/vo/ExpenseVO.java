package com.vinsys.hrms.traveldesk.vo;

/**
 * @author Onkar A
 *
 * 
 */
public class ExpenseVO {
	private Long id;
	private Float approximateTicketCost;
	private Float finalTicketCost;
	private Float refundCost;
	private Float setteledCost;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

}
