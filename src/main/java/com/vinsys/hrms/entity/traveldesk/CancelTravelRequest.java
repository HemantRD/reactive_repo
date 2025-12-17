package com.vinsys.hrms.entity.traveldesk;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_trn_cancel_travel_request")
public class CancelTravelRequest extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_cancel_travel_request", sequenceName = "seq_cancel_travel_request", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cancel_travel_request")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "travel_request_pk_id")
	private TravelRequest travelRequest;
	@Column(name = "child_id")
	private long childId;
	@Column(name = "child_type")
	private String childType;

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

	public long getChildId() {
		return childId;
	}

	public void setChildId(long childId) {
		this.childId = childId;
	}

	public String getChildType() {
		return childType;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
