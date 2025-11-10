package com.vinsys.hrms.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_candidate_family_address")
public class CandidateFamilyAddress extends Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_candidate_family_address", sequenceName = "seq_candidate_family_address", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_family_address")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_family_detail_id")
	private CandidateFamilyDetail candidateFamilyDetail;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CandidateFamilyDetail getCandidateFamilyDetail() {
		return candidateFamilyDetail;
	}

	public void setCandidateFamilyDetail(CandidateFamilyDetail candidateFamilyDetail) {
		this.candidateFamilyDetail = candidateFamilyDetail;
	}

}
