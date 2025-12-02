package com.vinsys.hrms.traveldesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_trn_travel_document")
public class TravelDocument extends AuditBase{

	@Id
	@SequenceGenerator(name = "seq_trn_travel_document", sequenceName = "seq_trn_travel_document", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_travel_document")
	private Long id;
	
	@Column(name="travel_request_id")
	private Long travelRequestId;
	
	@Column(name="ticket_request_id")
	private Long ticketRequestId;
	
	@Column(name="cab_request_id")
	private Long cabRequestId;
	
	@Column(name="accommodation_request_id")
	private Long accommodationRequestId;
	
	@Column(name="file_name")
	private String fileName;
	
	@Column(name="file_path")
	private String filePath;
	
	@Column(name="ticket_type")
	private String ticketType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(Long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public Long getTicketRequestId() {
		return ticketRequestId;
	}

	public void setTicketRequestId(Long ticketRequestId) {
		this.ticketRequestId = ticketRequestId;
	}

	public Long getCabRequestId() {
		return cabRequestId;
	}

	public void setCabRequestId(Long cabRequestId) {
		this.cabRequestId = cabRequestId;
	}

	public Long getAccommodationRequestId() {
		return accommodationRequestId;
	}

	public void setAccommodationRequestId(Long accommodationRequestId) {
		this.accommodationRequestId = accommodationRequestId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
}
