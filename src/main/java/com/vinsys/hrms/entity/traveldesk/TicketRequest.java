package com.vinsys.hrms.entity.traveldesk;

import java.util.Date;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_trn_ticket_request")
public class TicketRequest extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_trn_ticket_request", sequenceName = "seq_trn_ticket_request", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_ticket_request")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "travel_request_pk_id")
	private TravelRequest travelRequest;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "master_mode_of_travel_id")
	private MasterModeOfTravel masterModeOfTravel;
	@Column(name = "no_of_traveller")
	private float noOfTraveller;
	@Column(name = "preference_details", length = 1000)
	private String preferenceDetails;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "preferred_date")
	private Date preferredDate;
	@Column(name = "preferred_time")
	private String preferredTime;
	@Column(name = "fromLocation")
	private String fromLocation;
	@Column(name = "toLocation")
	private String toLocation;
	@Column(name = "roundTrip")
	private boolean roundTrip;
	@Column(name = "return_preference_details", length = 1000)
	private String returnPreferenceDetails;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "return_preferred_date")
	private Date returnPreferredDate;
	@Column(name = "return_preferred_time")
	private String returnPreferredTime;
	@Column(name = "total_ticket_fare")
	private double totalTicketFare;
	@Column(name = "chargeable_to_client")
	private boolean chargeableToClient;
	@Column(name = "approval_required")
	private boolean approvalRequired;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "approver_id")
	private MasterTraveldeskApprover masterApprover;
	@Column(name = "approver_status")
	private String approverStatus;
	@Column(name = "ticket_request_status")
	private String ticketRequestStatus;
	@OneToMany(mappedBy = "ticketRequest", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<TicketRequestPassenger> ticketRequestPassengers;
	@Column(name = "total_refund_amount")
	private double totalRefundAmount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TravelRequest getTravelRequest() {
		return travelRequest;
	}

	public void setTravelRequest(TravelRequest travelRequest) {
		this.travelRequest = travelRequest;
	}

	public MasterModeOfTravel getMasterModeOfTravel() {
		return masterModeOfTravel;
	}

	public void setMasterModeOfTravel(MasterModeOfTravel masterModeOfTravel) {
		this.masterModeOfTravel = masterModeOfTravel;
	}

	public float getNoOfTraveller() {
		return noOfTraveller;
	}

	public void setNoOfTraveller(float noOfTraveller) {
		this.noOfTraveller = noOfTraveller;
	}

	public String getPreferenceDetails() {
		return preferenceDetails;
	}

	public void setPreferenceDetails(String preferenceDetails) {
		this.preferenceDetails = preferenceDetails;
	}

	public Date getPreferredDate() {
		return preferredDate;
	}

	public void setPreferredDate(Date preferredDate) {
		this.preferredDate = preferredDate;
	}

	public String getPreferredTime() {
		return preferredTime;
	}

	public void setPreferredTime(String preferredTime) {
		this.preferredTime = preferredTime;
	}

	public String getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public boolean isRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(boolean roundTrip) {
		this.roundTrip = roundTrip;
	}

	public String getReturnPreferenceDetails() {
		return returnPreferenceDetails;
	}

	public void setReturnPreferenceDetails(String returnPreferenceDetails) {
		this.returnPreferenceDetails = returnPreferenceDetails;
	}

	public Date getReturnPreferredDate() {
		return returnPreferredDate;
	}

	public void setReturnPreferredDate(Date returnPreferredDate) {
		this.returnPreferredDate = returnPreferredDate;
	}

	public String getReturnPreferredTime() {
		return returnPreferredTime;
	}

	public void setReturnPreferredTime(String returnPreferredTime) {
		this.returnPreferredTime = returnPreferredTime;
	}

	public double getTotalTicketFare() {
		return totalTicketFare;
	}

	public void setTotalTicketFare(double totalTicketFare) {
		this.totalTicketFare = totalTicketFare;
	}

	public boolean isChargeableToClient() {
		return chargeableToClient;
	}

	public void setChargeableToClient(boolean chargeableToClient) {
		this.chargeableToClient = chargeableToClient;
	}

	public boolean isApprovalRequired() {
		return approvalRequired;
	}

	public void setApprovalRequired(boolean approvalRequired) {
		this.approvalRequired = approvalRequired;
	}

	public MasterTraveldeskApprover getMasterApprover() {
		return masterApprover;
	}

	public void setMasterApprover(MasterTraveldeskApprover masterApprover) {
		this.masterApprover = masterApprover;
	}

	public String getApproverStatus() {
		return approverStatus;
	}

	public void setApproverStatus(String approverStatus) {
		this.approverStatus = approverStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTicketRequestStatus() {
		return ticketRequestStatus;
	}

	public void setTicketRequestStatus(String ticketRequestStatus) {
		this.ticketRequestStatus = ticketRequestStatus;
	}

	public Set<TicketRequestPassenger> getTicketRequestPassengers() {
		return ticketRequestPassengers;
	}

	public void setTicketRequestPassengers(Set<TicketRequestPassenger> ticketRequestPassengers) {
		this.ticketRequestPassengers = ticketRequestPassengers;
	}

	public double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

}
