package com.vinsys.hrms.traveldesk.vo;

import com.vinsys.hrms.master.vo.CurrencyMasterVO;

import io.swagger.v3.oas.annotations.media.Schema;

public class TravelResponseVO {
	private long id;
	@Schema(required = true)
	private boolean bookTicket;
	@Schema(required = true)
	private boolean bookCab;
	@Schema(required = true)
	private boolean bookAccommodation;
	@Schema(required = true)
	private Long bpmNumber;
	@Schema(required = true)
	private String travelReason;
	private String name;
	private String status;
	private String createdDate;
	private String departmentName;
	private Long requesterId;
	private String invoiceNumber;
	private String bdName;
	private CurrencyMasterVO currency;
	private String preference;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isBookTicket() {
		return bookTicket;
	}

	public void setBookTicket(boolean bookTicket) {
		this.bookTicket = bookTicket;
	}

	public boolean isBookCab() {
		return bookCab;
	}

	public void setBookCab(boolean bookCab) {
		this.bookCab = bookCab;
	}

	public boolean isBookAccommodation() {
		return bookAccommodation;
	}

	public void setBookAccommodation(boolean bookAccommodation) {
		this.bookAccommodation = bookAccommodation;
	}

	public String getTravelReason() {
		return travelReason;
	}

	public void setTravelReason(String travelReason) {
		this.travelReason = travelReason;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Long getBpmNumber() {
		return bpmNumber;
	}

	public void setBpmNumber(Long bpmNumber) {
		this.bpmNumber = bpmNumber;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getBdName() {
		return bdName;
	}

	public void setBdName(String bdName) {
		this.bdName = bdName;
	}

	public CurrencyMasterVO getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyMasterVO currency) {
		this.currency = currency;
	}

	public String getPreference() {
		return preference;
	}

	public void setPreference(String preference) {
		this.preference = preference;
	}

	

}
