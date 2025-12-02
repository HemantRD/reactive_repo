package com.vinsys.hrms.entity;

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
@Table(name = "tbl_map_employee_separation_letter")
public class EmployeeSeparationLetterMapping extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_map_employee_separation_letter", sequenceName = "seq_map_employee_separation_letter", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_employee_separation_letter")
	private long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_div_separation_letter_id")
	private MapOrgDivSeparationLetter orgDivSeperationLetter;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_letter_id")
	private CandidateLetter candidateLetter;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public MapOrgDivSeparationLetter getOrgDivSeperationLetter() {
		return orgDivSeperationLetter;
	}
	public void setOrgDivSeperationLetter(MapOrgDivSeparationLetter orgDivSeperationLetter) {
		this.orgDivSeperationLetter = orgDivSeperationLetter;
	}
	public CandidateLetter getCandidateLetter() {
		return candidateLetter;
	}
	public void setCandidateLetter(CandidateLetter candidateLetter) {
		this.candidateLetter = candidateLetter;
	}
	
}
