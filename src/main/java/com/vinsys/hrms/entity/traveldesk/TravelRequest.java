package com.vinsys.hrms.entity.traveldesk;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.Organization;

@Entity
@Table(name = "tbl_trn_travel_request")
public class TravelRequest extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_travel_request", sequenceName = "seq_travel_request", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_travel_request")
	private long id;
	@Column(name = "seq_id")
	private long seqId;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_id",insertable = false,updatable = false)
	private Organization organization;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id")
	// //@JsonBackReference
	private Employee employeeId;

	@Column(name = "work_order_no")
	private long workOrderNo;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "department_id")
	// //@JsonBackReference
	private MasterDepartment department;

	@Column(name = "book_ticket")
	private boolean bookTicket;

	@Column(name = "book_accommodation")
	private boolean bookAccommodation;

	@Column(name = "book_cab")
	private boolean bookCab;

	@Column(name = "travel_status")
	private String travelStatus;

	@Column(name = "checked_by")
	private String checkedBy;

	@Column(name = "credit_card_details")
	private String creditCardDetails;

	@Column(name = "client_name")
	private String clientName;

	@Column(name = "request_date")
	private Date requestDate;

	@Column(name = "bd_name")
	private String bdName;

	@OneToOne(mappedBy = "travelRequest", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private CabRequest cabRequest;

	@OneToOne(mappedBy = "travelRequest", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private AccommodationRequest accommodationRequest;

	@OneToOne(mappedBy = "travelRequest", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private TicketRequest ticketRequest;

	@OneToMany(mappedBy = "travelRequest", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private List<TraveldeskComment> traveldeskComment;

	@OneToMany(mappedBy = "travelRequest", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private List<CancelTravelRequest> cancelTravelRequests;
	
	@Column(name = "business_unit")
	private String businessUnit;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSeqId() {
		return seqId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Employee getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Employee employeeId) {
		this.employeeId = employeeId;
	}

	public long getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(long workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public MasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(MasterDepartment department) {
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

	public CabRequest getCabRequest() {
		return cabRequest;
	}

	public void setCabRequest(CabRequest cabRequest) {
		this.cabRequest = cabRequest;
	}

	public AccommodationRequest getAccommodationRequest() {
		return accommodationRequest;
	}

	public void setAccommodationRequest(AccommodationRequest accommodationRequest) {
		this.accommodationRequest = accommodationRequest;
	}

	public TicketRequest getTicketRequest() {
		return ticketRequest;
	}

	public void setTicketRequest(TicketRequest ticketRequest) {
		this.ticketRequest = ticketRequest;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public List<TraveldeskComment> getTraveldeskComment() {
		return traveldeskComment;
	}

	public void setTraveldeskComment(List<TraveldeskComment> traveldeskComment) {
		this.traveldeskComment = traveldeskComment;
	}

	public List<CancelTravelRequest> getCancelTravelRequests() {
		return cancelTravelRequests;
	}

	public void setCancelTravelRequests(List<CancelTravelRequest> cancelTravelRequests) {
		this.cancelTravelRequests = cancelTravelRequests;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

}
