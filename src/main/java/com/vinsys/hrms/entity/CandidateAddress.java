package com.vinsys.hrms.entity;

import java.io.Serializable;

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

@Entity
@Table(name = "tbl_candidate_address")
public class CandidateAddress extends Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_candidate_address", sequenceName = "seq_candidate_address", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_address")
	private long id;
	@Column(name = "address_type")
	private String addressType;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;
	@Column(name="ssn_number")
	private Long ssnNumber;
	
	@Column(name="nationality")
	private String nationality;
	
	@Column(name="is_rental")
	private String isRental;
	
	@Column(name="owner_name")
	private String ownerName;
	
	@Column(name="owner_contact")
	private Long ownerContact;
	
	@Column(name="owner_adhar")
	private String ownerAdhar;
	
	@Column(name="street")
	private String Street;
	
	@Column (name="land_mark")
	private String landMark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public Long getSsnNumber() {
		return ssnNumber;
	}

	public void setSsnNumber(Long ssnNumber) {
		this.ssnNumber = ssnNumber;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getIsRental() {
		return isRental;
	}

	public void setIsRental(String isRental) {
		this.isRental = isRental;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Long getOwnerContact() {
		return ownerContact;
	}

	public void setOwnerContact(Long ownerContact) {
		this.ownerContact = ownerContact;
	}

	public String getOwnerAdhar() {
		return ownerAdhar;
	}

	public void setOwnerAdhar(String ownerAdhar) {
		this.ownerAdhar = ownerAdhar;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
	}
	
	
	

}
