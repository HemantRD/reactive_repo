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
@Table(name = "tbl_trn_traveldesk_document")
public class TraveldeskDocument extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_traveldesk_document", sequenceName = "seq_traveldesk_document", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_traveldesk_document")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "travel_request_pk_id")
	private TravelRequest travelRequest;
	@Column(name = "child_id")
	private long childId;
	@Column(name = "child_type")
	private String childType;
	@Column(name = "document_name")
	private String documentName;

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

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
