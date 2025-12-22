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
@Table(name = "tbl_trn_ticket_passenger")
public class TicketRequestPassenger extends TravellerDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_ticket_passenger_detail", sequenceName = "seq_ticket_passenger_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ticket_passenger_detail")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "ticket_request_id")
	private TicketRequest ticketRequest;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TicketRequest getTicketRequest() {
		return ticketRequest;
	}

	public void setTicketRequest(TicketRequest ticketRequest) {
		this.ticketRequest = ticketRequest;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
