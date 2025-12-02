package com.vinsys.hrms.traveldesk.entity;

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

@Entity
@Table(name = "tbl_trn_travel_wf")
public class TravelRequestWf extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_trn_travel_wf", sequenceName = "seq_trn_travel_wf", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_travel_wf")
	private Long id;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "pending_with")
	private String pendingWith;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "travel_request_id")
	TravelRequestV2 travelRequest;
//	private Long travelRequestId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public TravelRequestV2 getTravelRequest() {
		return travelRequest;
	}

	public void setTravelRequest(TravelRequestV2 travelRequest) {
		this.travelRequest = travelRequest;
	}

//	public Long getTravelRequestId() {
//		return travelRequestId;
//	}
//
//	public void setTravelRequestId(Long travelRequestId) {
//		this.travelRequestId = travelRequestId;
//	}

	
	
	
}
