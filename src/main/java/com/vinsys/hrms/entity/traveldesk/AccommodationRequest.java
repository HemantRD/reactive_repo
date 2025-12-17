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
@Table(name = "tbl_trn_accommodation_request")
public class AccommodationRequest extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_trn_accommodation_request", sequenceName = "seq_trn_accommodation_request", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_accommodation_request")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "travel_request_pk_id")
	private TravelRequest travelRequest;
	@Column(name = "no_of_people")
	private float noOfPeople;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "from_date")
	private Date fromDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "to_date")
	private Date toDate;
	@Column(name = "preference_details", length = 1000)
	private String preferenceDetails;
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
	@Column(name = "accommodation_request_status")
	private String accommodationRequestStatus;
	@Column(name = "total_accommodation_cost")
	private double totalAccommodationCost;
	@OneToMany(mappedBy = "accomodationReq", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<AccommodationGuest> accommodationGuests;
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

	public float getNoOfPeople() {
		return noOfPeople;
	}

	public void setNoOfPeople(float noOfPeople) {
		this.noOfPeople = noOfPeople;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getPreferenceDetails() {
		return preferenceDetails;
	}

	public void setPreferenceDetails(String preferenceDetails) {
		this.preferenceDetails = preferenceDetails;
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

	public String getAccommodationRequestStatus() {
		return accommodationRequestStatus;
	}

	public void setAccommodationRequestStatus(String accommodationRequestStatus) {
		this.accommodationRequestStatus = accommodationRequestStatus;
	}

	public double getTotalAccommodationCost() {
		return totalAccommodationCost;
	}

	public void setTotalAccommodationCost(double totalAccommodationCost) {
		this.totalAccommodationCost = totalAccommodationCost;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<AccommodationGuest> getAccommodationGuests() {
		return accommodationGuests;
	}

	public void setAccommodationGuests(Set<AccommodationGuest> accommodationGuests) {
		this.accommodationGuests = accommodationGuests;
	}

	public double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

}
