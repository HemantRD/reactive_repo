package com.vinsys.hrms.entity.traveldesk;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_trn_accommodation_guest")
public class AccommodationGuest extends TravellerDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_guest_detail", sequenceName = "seq_guest_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_guest_detail")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "accomodation_req_id")
	private AccommodationRequest accomodationReq;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AccommodationRequest getAccomodationReq() {
		return accomodationReq;
	}

	public void setAccomodationReq(AccommodationRequest accomodationReq) {
		this.accomodationReq = accomodationReq;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
