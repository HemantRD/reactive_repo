package com.vinsys.hrms.traveldesk.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.master.entity.MasterTravellerType;

@Entity
@Table(name = "tbl_trn_traveller_details")
public class TravellerDetailsV2 extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_trn_traveller_details", sequenceName = "seq_trn_traveller_details", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_traveller_details")
	private Long id;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "traveller_type")
	private MasterTravellerType masterTravellerType;
//	private Long travellerTypeId;
	@Column(name = "name")
	private String name;
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

	@Column(name = "ticket_request_id")
	private Long ticketRequestId;
	@Column(name = "cab_request")
	private Long cabRequestId;
	@Column(name = "accommodation_request")
	private Long accommodationId;

	@Column(name = "passport_number")
	private String passportNumber;
	@Column(name = "passport_date_expiry")
	private Date passportDateExpiry;
	@Column(name = "visa_number")
	private String visaNumber;
	@Column(name = "visa_country")
	private String visaCountry;
	@Column(name = "visa_type")
	private String visaType;
	@Column(name = "visa_date_expiry")
	private Date visaDateExpiry;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "emp_id")
	private Employee employee;
	@Column(name = "is_management_employee")
	private boolean isManagementEmployee;

//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "traveller_type", insertable = false, updatable = false)
//	private MasterTravellerType masterTravellerType;

	public MasterTravellerType getMasterTravellerType() {
		return masterTravellerType;
	}

	public void setMasterTravellerType(MasterTravellerType masterTravellerType) {
		this.masterTravellerType = masterTravellerType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

//	public Long getTravellerTypeId() {
//		return travellerTypeId;
//	}
//
//	public void setTravellerTypeId(Long travellerTypeId) {
//		this.travellerTypeId = travellerTypeId;
//	}

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

	public boolean isPrimaryTraveller() {
		return isPrimaryTraveller;
	}

	public void setPrimaryTraveller(boolean isPrimaryTraveller) {
		this.isPrimaryTraveller = isPrimaryTraveller;
	}

	public Long getTicketRequestId() {
		return ticketRequestId;
	}

	public void setTicketRequestId(Long ticketRequestId) {
		this.ticketRequestId = ticketRequestId;
	}

	public Long getCabRequestId() {
		return cabRequestId;
	}

	public void setCabRequestId(Long cabRequestId) {
		this.cabRequestId = cabRequestId;
	}

	public Long getAccommodationId() {
		return accommodationId;
	}

	public void setAccommodationId(Long accommodationId) {
		this.accommodationId = accommodationId;
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

	public String getVisaCountry() {
		return visaCountry;
	}

	public void setVisaCountry(String visaCountry) {
		this.visaCountry = visaCountry;
	}

	public String getVisaType() {
		return visaType;
	}

	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}

	public Date getVisaDateExpiry() {
		return visaDateExpiry;
	}

	public void setVisaDateExpiry(Date visaDateExpiry) {
		this.visaDateExpiry = visaDateExpiry;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public boolean isManagementEmployee() {
		return isManagementEmployee;
	}

	public void setManagementEmployee(boolean isManagementEmployee) {
		this.isManagementEmployee = isManagementEmployee;
	}

}
