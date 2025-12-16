package com.vinsys.hrms.traveldesk.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="vw_travel_request_details")
public class TDExpenceSummaryReport {
	
	@Id
	@Column(name="id")
	private Long id;
	
	@Column(name = "travel_request_id")
	private String travelRequestId;
	
	@Column(name = "book_accommodation")
	private Boolean bookAccommodation;
	
	@Column(name = "book_cab")
	private Boolean bookCab;
	
	@Column(name = "book_ticket")
	private Boolean bookTicket;
	
	@Column(name = "bpm_number")
	private Long bpmNumber;
	
	@Column(name = "requester_id")
	private Long requesterId;
	
	@Column(name = "requester_name")
	private String requesterName;
	
	@Column(name="request_created_date")
	private Date requestCreatedDate;
	
	@Column(name = "total_approximate_cost")
	private Float totalApproximateCost;
	
	@Column(name = "total_final_cost")
	private Float totalFinalCost;
	
	@Column(name = "total_refund_cost")
	private Float totalRefundCost;
	
	@Column(name = "total_settled_cost")
	private Float totalSettledCost;
	
	@Column(name = "travel_reason")
	private String travelReason;
	
	@Column(name = "division_id")
	private Long divisionId;
	
	@Column(name = "division_name")
	private String divisionName;
	
	@Column(name = "department_id")
	private Long departmentId;
	
	@Column(name = "department_name")
	private String departmentName;
	
	@Column(name = "name")
	private String travellerName;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "contact_number")
	private Long contactNumber;
	
	@Column(name = "dob")
	private Date dob;
	
	@Column(name = "pickup_location")
	private String pickupLocation;
	
	@Column(name = "pickup_time")
	private String pickupTime;
	
	@Column(name = "drop_location")
	private String dropLocation;
	
	@Column(name = "is_primary_traveller")
	private Boolean isPrimaryTraveller;
	
	@Column(name = "passport_number")
	private String passportNumber;
	
	
	@Column(name = "passport_date_expiry")
	private Date passportDateExpiry;
	
	@Column(name = "visa_number")
	private String visaNumber;
	
	@Column(name = "visa_date_expiry")
	private Date visaDateExpiry;
	
	@Column(name = "is_management_employee")
	private Boolean isManagementEmployee;
	
	@Column(name = "traveller_type")
	private String travellerType;
	
	@Column(name = "mode_of_travel")
	private String modeOfTravel;
	
	@Column(name = "invoice_number")
	private String invoiceNumber;
	
	@Column(name = "bd_name")
	private String bdName;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "tiket_date_of_journey")
	private Date tiketDateOfJourney;
	
	@Column(name = "tiket_return_date")
	private Date tiketReturnDate;
	
	@Column(name = "tiket_from_location")
	private String tiketFromLocation;
	
	@Column(name = "tiket_to_location")
	private String tiketToLocation;
	
	@Column(name = "cab_date_of_journey")
	private Date cabDateOfJourney;
	
	@Column(name = "cab_return_date")
	private Date cabReturnDate;
	
	@Column(name = "cab_from_location")
	private String cabFromLocation;
	
	@Column(name = "cab_to_location")
	private String cabToLocation;
	
	@Column(name = "acc_location")
	private String accLocation;
	
	@Column(name = "acc_no_of_rooms")
	private Long accNoOfRooms;
	
	@Column(name = "acc_from_date")
	private Date accFromDate;
	
	@Column(name = "acc_to_date")
	private Date accToDate;
	
	
	@Column(name = "approver_first_name")
	private String approverFirstName;
	
	@Column(name = "approver_last_name")
	private String approverLastName;
	
	@Column(name="ticket_id")
	private Long ticketId;
	
	@Column(name="cab_id")
	private Long cabId;
	
	@Column(name="acc_is")
	private Long accId;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name="cab_approx_cost")
	private Long cabApproxCost;
	
	@Column(name="ticket_approx_cost")
	private Long ticketApproxCost;
	
	@Column(name="accomm_approx_cost")
	private Long accApproxCost;
	
	@Column(name="cab_final_cost")
	private Long cabFinalCost;
	
	@Column(name="ticket_final_cost")
	private Long ticketFinalCost;
	
	@Column(name="accomm_final_cost")
	private Long accFinalCost;
	
	@Column(name = "cab_type")
	private String cabMode;
	
	@Column(name = "cab_driver_first_name")
	private String cabDriverFirstName;
	
	@Column(name = "cab_driver_last_name")
	private String cabDriverLastName;
	
	

	public String getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(String travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public Boolean getBookAccommodation() {
		return bookAccommodation;
	}

	public void setBookAccommodation(Boolean bookAccommodation) {
		this.bookAccommodation = bookAccommodation;
	}

	public Boolean getBookCab() {
		return bookCab;
	}

	public void setBookCab(Boolean bookCab) {
		this.bookCab = bookCab;
	}

	public Boolean getBookTicket() {
		return bookTicket;
	}

	public void setBookTicket(Boolean bookTicket) {
		this.bookTicket = bookTicket;
	}

	public Long getBpmNumber() {
		return bpmNumber;
	}

	public void setBpmNumber(Long bpmNumber) {
		this.bpmNumber = bpmNumber;
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
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

	public Float getTotalRefundCost() {
		return totalRefundCost;
	}

	public void setTotalRefundCost(Float totalRefundCost) {
		this.totalRefundCost = totalRefundCost;
	}

	public Float getTotalSettledCost() {
		return totalSettledCost;
	}

	public void setTotalSettledCost(Float totalSettledCost) {
		this.totalSettledCost = totalSettledCost;
	}

	public String getTravelReason() {
		return travelReason;
	}

	public void setTravelReason(String travelReason) {
		this.travelReason = travelReason;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getTravellerName() {
		return travellerName;
	}

	public void setTravellerName(String travellerName) {
		this.travellerName = travellerName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getDropLocation() {
		return dropLocation;
	}

	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}

	public Boolean getIsPrimaryTraveller() {
		return isPrimaryTraveller;
	}

	public void setIsPrimaryTraveller(Boolean isPrimaryTraveller) {
		this.isPrimaryTraveller = isPrimaryTraveller;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public Date getPassportDateExpiry() {
		return passportDateExpiry;
	}

	public void setPassportDateExpiry(Date passportDateExpiry) {
		this.passportDateExpiry = passportDateExpiry;
	}

	public String getVisaNumber() {
		return visaNumber;
	}

	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	public Date getVisaDateExpiry() {
		return visaDateExpiry;
	}

	public void setVisaDateExpiry(Date visaDateExpiry) {
		this.visaDateExpiry = visaDateExpiry;
	}

	public Boolean getIsManagementEmployee() {
		return isManagementEmployee;
	}

	public void setIsManagementEmployee(Boolean isManagementEmployee) {
		this.isManagementEmployee = isManagementEmployee;
	}

	public String getTravellerType() {
		return travellerType;
	}

	public void setTravellerType(String travellerType) {
		this.travellerType = travellerType;
	}
	

	public Date getRequestCreatedDate() {
		return requestCreatedDate;
	}

	public void setRequestCreatedDate(Date requestCreatedDate) {
		this.requestCreatedDate = requestCreatedDate;
	}

	public String getModeOfTravel() {
		return modeOfTravel;
	}

	public void setModeOfTravel(String modeOfTravel) {
		this.modeOfTravel = modeOfTravel;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTiketDateOfJourney() {
		return tiketDateOfJourney;
	}

	public void setTiketDateOfJourney(Date tiketDateOfJourney) {
		this.tiketDateOfJourney = tiketDateOfJourney;
	}

	public Date getTiketReturnDate() {
		return tiketReturnDate;
	}

	public void setTiketReturnDate(Date tiketReturnDate) {
		this.tiketReturnDate = tiketReturnDate;
	}

	public String getTiketFromLocation() {
		return tiketFromLocation;
	}

	public void setTiketFromLocation(String tiketFromLocation) {
		this.tiketFromLocation = tiketFromLocation;
	}

	public String getTiketToLocation() {
		return tiketToLocation;
	}

	public void setTiketToLocation(String tiketToLocation) {
		this.tiketToLocation = tiketToLocation;
	}

	public Date getCabDateOfJourney() {
		return cabDateOfJourney;
	}

	public void setCabDateOfJourney(Date cabDateOfJourney) {
		this.cabDateOfJourney = cabDateOfJourney;
	}

	public Date getCabReturnDate() {
		return cabReturnDate;
	}

	public void setCabReturnDate(Date cabReturnDate) {
		this.cabReturnDate = cabReturnDate;
	}

	public String getCabFromLocation() {
		return cabFromLocation;
	}

	public void setCabFromLocation(String cabFromLocation) {
		this.cabFromLocation = cabFromLocation;
	}

	public String getCabToLocation() {
		return cabToLocation;
	}

	public void setCabToLocation(String cabToLocation) {
		this.cabToLocation = cabToLocation;
	}

	public String getAccLocation() {
		return accLocation;
	}

	public void setAccLocation(String accLocation) {
		this.accLocation = accLocation;
	}

	public Long getAccNoOfRooms() {
		return accNoOfRooms;
	}

	public void setAccNoOfRooms(Long accNoOfRooms) {
		this.accNoOfRooms = accNoOfRooms;
	}

	public Date getAccFromDate() {
		return accFromDate;
	}

	public void setAccFromDate(Date accFromDate) {
		this.accFromDate = accFromDate;
	}

	public Date getAccToDate() {
		return accToDate;
	}

	public void setAccToDate(Date accToDate) {
		this.accToDate = accToDate;
	}

	public String getApproverFirstName() {
		return approverFirstName;
	}

	public void setApproverFirstName(String approverFirstName) {
		this.approverFirstName = approverFirstName;
	}

	public String getApproverLastName() {
		return approverLastName;
	}

	public void setApproverLastName(String approverLastName) {
		this.approverLastName = approverLastName;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public Long getCabId() {
		return cabId;
	}

	public void setCabId(Long cabId) {
		this.cabId = cabId;
	}

	public Long getAccId() {
		return accId;
	}

	public void setAccId(Long accId) {
		this.accId = accId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Long getCabApproxCost() {
		return cabApproxCost;
	}

	public void setCabApproxCost(Long cabApproxCost) {
		this.cabApproxCost = cabApproxCost;
	}

	public Long getTicketApproxCost() {
		return ticketApproxCost;
	}

	public void setTicketApproxCost(Long ticketApproxCost) {
		this.ticketApproxCost = ticketApproxCost;
	}

	public Long getAccApproxCost() {
		return accApproxCost;
	}

	public void setAccApproxCost(Long accApproxCost) {
		this.accApproxCost = accApproxCost;
	}

	public Long getCabFinalCost() {
		return cabFinalCost;
	}

	public void setCabFinalCost(Long cabFinalCost) {
		this.cabFinalCost = cabFinalCost;
	}

	public Long getTicketFinalCost() {
		return ticketFinalCost;
	}

	public void setTicketFinalCost(Long ticketFinalCost) {
		this.ticketFinalCost = ticketFinalCost;
	}

	public Long getAccFinalCost() {
		return accFinalCost;
	}

	public void setAccFinalCost(Long accFinalCost) {
		this.accFinalCost = accFinalCost;
	}

	public String getCabMode() {
		return cabMode;
	}

	public void setCabMode(String cabMode) {
		this.cabMode = cabMode;
	}

	public String getCabDriverFirstName() {
		return cabDriverFirstName;
	}

	public void setCabDriverFirstName(String cabDriverFirstName) {
		this.cabDriverFirstName = cabDriverFirstName;
	}

	public String getCabDriverLastName() {
		return cabDriverLastName;
	}

	public void setCabDriverLastName(String cabDriverLastName) {
		this.cabDriverLastName = cabDriverLastName;
	}

	
	
	
	
	
	
}
