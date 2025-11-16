package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_candidate_passport_detail")
public class CandidatePassportDetail extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_candidate_passport_detail", sequenceName = "seq_candidate_passport_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_passport_detail")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_personal_detail_id")
	private CandidatePersonalDetail candidatePersonalDetail;
	@Column(name = "passport_number")
	private String passportNumber;
	@Column(name = "passport_first_name")
	private String passportFirstName;
	@Column(name = "passport_middle_name")
	private String passportMiddleName;
	@Column(name = "passport_last_name")
	private String passportLastName;
	@Column(name = "place_of_issue")
	private String placeOfIssue;
	@Column(name = "date_of_issue")
	@Temporal(TemporalType.DATE)
	private Date dateOfIssue;
	@Column(name = "date_of_expiry")
	@Temporal(TemporalType.DATE)
	private Date dateOfExpiry;
	@Column(name = "ecnr")
	private String ecnr;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CandidatePersonalDetail getCandidatePersonalDetail() {
		return candidatePersonalDetail;
	}

	public void setCandidatePersonalDetail(CandidatePersonalDetail candidatePersonalDetail) {
		this.candidatePersonalDetail = candidatePersonalDetail;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getPassportFirstName() {
		return passportFirstName;
	}

	public void setPassportFirstName(String passportFirstName) {
		this.passportFirstName = passportFirstName;
	}

	public String getPassportMiddleName() {
		return passportMiddleName;
	}

	public void setPassportMiddleName(String passportMiddleName) {
		this.passportMiddleName = passportMiddleName;
	}

	public String getPassportLastName() {
		return passportLastName;
	}

	public void setPassportLastName(String passportLastName) {
		this.passportLastName = passportLastName;
	}

	public String getPlaceOfIssue() {
		return placeOfIssue;
	}

	public void setPlaceOfIssue(String placeOfIssue) {
		this.placeOfIssue = placeOfIssue;
	}

	public Date getDateOfIssue() {
		return dateOfIssue;
	}

	public void setDateOfIssue(Date dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}

	public Date getDateOfExpiry() {
		return dateOfExpiry;
	}

	public void setDateOfExpiry(Date dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
	}

	public String getEcnr() {
		return ecnr;
	}

	public void setEcnr(String ecnr) {
		this.ecnr = ecnr;
	}

}
