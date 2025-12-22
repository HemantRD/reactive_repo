package com.vinsys.hrms.traveldesk.entity;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.Transient;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.master.entity.CurrencyMaster;

@Entity
@Table(name = "tbl_trn_travel_request_v2")
public class TravelRequestV2 extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_trn_travel_request_v2", sequenceName = "seq_trn_travel_request_v2", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_travel_request_v2")
	@Column(name = "travel_request_id")
	private Long id;

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
	@Column(name = "travel_reason")
	private String travelReason;
//	@Column(name = "status")
//	private String status;
	@Column(name = "requester_name")
	private String name;
	@Column(name = "division_id")
	private Long divisionId;
	@Column(name = "department_id")
	private Long departmentId;

	@Column(name = "total_approximate_cost")
	private Float totalApproximateCost;

	@Column(name = "total_final_cost")
	private Float totalFinalCost;
	@Column(name = "total_refund_cost")
	private Float totalRefundCost;
	@Column(name = "total_settled_cost")
	private Float totalSettledCost;

	@Column(name = "approver_comment")
	private String approverComment;
	@Column(name = "approver_id")
	private Long approverId;
	@Column(name = "branch_id")
	private Long branchId;

	@Transient
	private CabRequestV2 cabRequestV2;
	@Transient
	private AccommodationRequestV2 accommodationRequest;
	@Transient
	private List<TicketRequestV2> ticketBooking;

	@OneToOne(mappedBy = "travelRequest")
	private TravelRequestWf requestWF;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "department_id", insertable = false, updatable = false)
	private MasterDepartment department;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "division_id", insertable = false, updatable = false)
	private MasterDivision division;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "approver_id", insertable = false, updatable = false)
	private Employee approver;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "currency")
	private CurrencyMaster currency;

	@Column(name = "invoice_number")
	private String invoiceNumber;

	@Column(name = "bd_name")
	private String bdName;

//	@Column(name = "travel_comment")
//	private String travelComment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getBookAccommodation() {
		return bookAccommodation;
	}

	public void setBookAccommodation(Boolean bookAccommodation) {
		this.bookAccommodation = bookAccommodation;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
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

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public String getTravelReason() {
		return travelReason;
	}

	public void setTravelReason(String travelReason) {
		this.travelReason = travelReason;
	}

//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBpmNumber() {
		return bpmNumber;
	}

	public void setBpmNumber(Long bpmNumber) {
		this.bpmNumber = bpmNumber;
	}

	public CabRequestV2 getCabRequestV2() {
		return cabRequestV2;
	}

	public void setCabRequestV2(CabRequestV2 cabRequestV2) {
		this.cabRequestV2 = cabRequestV2;
	}

	public AccommodationRequestV2 getAccommodationRequest() {
		return accommodationRequest;
	}

	public void setAccommodationRequest(AccommodationRequestV2 accommodationRequest) {
		this.accommodationRequest = accommodationRequest;
	}

	public List<TicketRequestV2> getTicketBooking() {
		return ticketBooking;
	}

	public void setTicketBooking(List<TicketRequestV2> ticketBooking) {
		this.ticketBooking = ticketBooking;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public TravelRequestWf getRequestWF() {
		return requestWF;
	}

	public void setRequestWF(TravelRequestWf requestWF) {
		this.requestWF = requestWF;
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

	public MasterDepartment getDepartment() {
		return department;
	}

	public void setDepartment(MasterDepartment department) {
		this.department = department;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public MasterDivision getDivision() {
		return division;
	}

	public void setDivision(MasterDivision division) {
		this.division = division;
	}

	public Employee getApprover() {
		return approver;
	}

	public void setApprover(Employee approver) {
		this.approver = approver;
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

	public CurrencyMaster getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyMaster currency) {
		this.currency = currency;
	}

//	public String getTravelComment() {
//		return travelComment;
//	}
//
//	public void setTravelComment(String travelComment) {
//		this.travelComment = travelComment;
//	}

//	public TravelRequestWf getRequestWf() {
//		return requestWf;
//	}
//
//	public void setRequestWf(TravelRequestWf requestWf) {
//		this.requestWf = requestWf;
//	}

}
