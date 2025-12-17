package com.vinsys.hrms.traveldesk.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Onkar A.
 *
 */
public class LineItemDetailsResponseVO {

	private Long lineItemSequenceNo;
	private String invoicedNotinvoiced;
	private String SBU;
	private Long bpmNo;
	private String NameOfBde;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String invoiceDate;
	private String vinsysInvoiceNo;

	public Long getLineItemSequenceNo() {
		return lineItemSequenceNo;
	}

	public void setLineItemSequenceNo(Long lineItemSequenceNo) {
		this.lineItemSequenceNo = lineItemSequenceNo;
	}

	public String getInvoicedNotinvoiced() {
		return invoicedNotinvoiced;
	}

	public void setInvoicedNotinvoiced(String invoicedNotinvoiced) {
		this.invoicedNotinvoiced = invoicedNotinvoiced;
	}

	public String getSBU() {
		return SBU;
	}

	public void setSBU(String sBU) {
		SBU = sBU;
	}

	public Long getBpmNo() {
		return bpmNo;
	}

	public void setBpmNo(Long bpmNo) {
		this.bpmNo = bpmNo;
	}

	public String getNameOfBde() {
		return NameOfBde;
	}

	public void setNameOfBde(String nameOfBde) {
		NameOfBde = nameOfBde;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getVinsysInvoiceNo() {
		return vinsysInvoiceNo;
	}

	public void setVinsysInvoiceNo(String vinsysInvoiceNo) {
		this.vinsysInvoiceNo = vinsysInvoiceNo;
	}
}
