package com.vinsys.hrms.datamodel.traveldesk;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vinsys.hrms.datamodel.VOAuditBase;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOMasterDepartment;
import com.vinsys.hrms.datamodel.VOOrganization;

public class VOTravelRequest extends VOAuditBase {

	private long id;
	private String requestedBy;
	private VOEmployee employeeId;
	private long workOrderNo;
	private VOMasterDepartment department;
	private boolean bookTicket;
	private boolean bookAccommodation;
	private boolean bookCab;
	private String travelStatus;
	private String checkedBy;
	private String creditCardDetails;
	private String clientName;
	private Date requestDate;
	private String bdName;
	private VOCabRequest cabRequest;
	private VOAccommodationRequest accommodationRequest;
	private VOTicketRequest ticketRequest;
	private String comment;
	private VOEmployee loggedInEmployee;
	private VOOrganization organization;
	private List<VOTraveldeskComment> travelDeskComment;
	private boolean disableCab;
	private boolean disableTicket;
	private boolean disableAccommodation;
	// private Map<String, List<Object>> headers;
	private long seqId;
	private String childRequestType;
	private String businessUnit;
	private String requestSummaryCount;
	
	private boolean travelTypeTD;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public VOEmployee getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(VOEmployee employeeId) {
		this.employeeId = employeeId;
	}

	public long getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(long workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public VOMasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(VOMasterDepartment department) {
		this.department = department;
	}

	public boolean isBookTicket() {
		return bookTicket;
	}

	public void setBookTicket(boolean bookTicket) {
		this.bookTicket = bookTicket;
	}

	public boolean isBookAccommodation() {
		return bookAccommodation;
	}

	public void setBookAccommodation(boolean bookAccommodation) {
		this.bookAccommodation = bookAccommodation;
	}

	public boolean isBookCab() {
		return bookCab;
	}

	public void setBookCab(boolean bookCab) {
		this.bookCab = bookCab;
	}

	public String getTravelStatus() {
		return travelStatus;
	}

	public void setTravelStatus(String travelStatus) {
		this.travelStatus = travelStatus;
	}

	public String getCheckedBy() {
		return checkedBy;
	}

	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}

	public String getCreditCardDetails() {
		return creditCardDetails;
	}

	public void setCreditCardDetails(String creditCardDetails) {
		this.creditCardDetails = creditCardDetails;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getBdName() {
		return bdName;
	}

	public void setBdName(String bdName) {
		this.bdName = bdName;
	}

	public VOAccommodationRequest getAccommodationRequest() {
		return accommodationRequest;
	}

	public void setAccommodationRequest(VOAccommodationRequest accommodationRequest) {
		this.accommodationRequest = accommodationRequest;
	}

	public VOTicketRequest getTicketRequest() {
		return ticketRequest;
	}

	public void setTicketRequest(VOTicketRequest ticketRequest) {
		this.ticketRequest = ticketRequest;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public VOEmployee getLoggedInEmployee() {
		return loggedInEmployee;
	}

	public void setLoggedInEmployee(VOEmployee loggedInEmployee) {
		this.loggedInEmployee = loggedInEmployee;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public VOCabRequest getCabRequest() {
		return cabRequest;
	}

	public void setCabRequest(VOCabRequest cabRequest) {
		this.cabRequest = cabRequest;
	}

	public List<VOTraveldeskComment> getTravelDeskComment() {
		return travelDeskComment;
	}

	public void setTravelDeskComment(List<VOTraveldeskComment> travelDeskComment) {
		this.travelDeskComment = travelDeskComment;
	}

	public boolean isDisableCab() {
		return disableCab;
	}

	public void setDisableCab(boolean disableCab) {
		this.disableCab = disableCab;
	}

	public boolean isDisableTicket() {
		return disableTicket;
	}

	public void setDisableTicket(boolean disableTicket) {
		this.disableTicket = disableTicket;
	}

	public boolean isDisableAccommodation() {
		return disableAccommodation;
	}

	public void setDisableAccommodation(boolean disableAccommodation) {
		this.disableAccommodation = disableAccommodation;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Override
	public Date getCreatedDate() {
		// TODO Auto-generated method stub
		return super.getCreatedDate();
	}

	public long getSeqId() {
		return seqId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public String getChildRequestType() {
		return childRequestType;
	}

	public void setChildRequestType(String childRequestType) {
		this.childRequestType = childRequestType;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getRequestSummaryCount() {
		return requestSummaryCount;
	}

	public void setRequestSummaryCount(String requestSummaryCount) {
		this.requestSummaryCount = requestSummaryCount;
	}

	public boolean isTravelTypeTD() {
		return travelTypeTD;
	}

	public void setTravelTypeTD(boolean travelTypeTD) {
		this.travelTypeTD = travelTypeTD;
	}

}
