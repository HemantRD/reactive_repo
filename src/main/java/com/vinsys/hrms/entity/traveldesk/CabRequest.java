package com.vinsys.hrms.entity.traveldesk;

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

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_trn_cab_request")
public class CabRequest extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_cab_detail", sequenceName = "seq_cab_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cab_detail")
	private Long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "travel_request_pk_id")
	private TravelRequest travelRequest;
	@Column(name = "total_cab_cost")
	private double totalCabCost;
	@Column(name = "cab_request_status")
	private String cabRequestStatus;
	@Column(name = "approval_required")
	private boolean approvalRequired;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "approver_id")
	private MasterTraveldeskApprover masterApprover;
	@Column(name = "approver_status")
	private String approverStatus;
	@OneToMany(mappedBy = "cabRequest", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<CabRequestPassenger> cabRequestPassengers;
	@Column(name = "total_refund_amount")
	private double totalRefundAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TravelRequest getTravelRequest() {
		return travelRequest;
	}

	public void setTravelRequest(TravelRequest travelRequest) {
		this.travelRequest = travelRequest;
	}

	public double getTotalCabCost() {
		return totalCabCost;
	}

	public void setTotalCabCost(double totalCabCost) {
		this.totalCabCost = totalCabCost;
	}

	public String getCabRequestStatus() {
		return cabRequestStatus;
	}

	public void setCabRequestStatus(String cabRequestStatus) {
		this.cabRequestStatus = cabRequestStatus;
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

	public Set<CabRequestPassenger> getCabRequestPassengers() {
		return cabRequestPassengers;
	}

	public void setCabRequestPassengers(Set<CabRequestPassenger> cabRequestPassengers) {
		this.cabRequestPassengers = cabRequestPassengers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

}
