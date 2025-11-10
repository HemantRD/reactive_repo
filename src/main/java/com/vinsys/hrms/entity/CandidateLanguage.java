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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_candidate_language")
public class CandidateLanguage extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_candidate_language", sequenceName = "seq_candidate_language", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_language")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_personal_detail_id", nullable = false)
	private CandidatePersonalDetail candidatePersonalDetail;
	@Column(name = "language_name")
	private String languageName;
	@Column(name = "can_speak")
	private String speak;
	@Column(name = "can_read")
	private String read;
	@Column(name = "can_write")
	private String write;
	@Column(name = "mother_tongue")
	private String motherTongue;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "language_id")
	private MasterLanguage language;

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

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getSpeak() {
		return speak;
	}

	public void setSpeak(String speak) {
		this.speak = speak;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getWrite() {
		return write;
	}

	public void setWrite(String write) {
		this.write = write;
	}

	public String getMotherTongue() {
		return motherTongue;
	}

	public void setMotherTongue(String motherTongue) {
		this.motherTongue = motherTongue;
	}

	public MasterLanguage getLanguage() {
		return language;
	}

	public void setLanguage(MasterLanguage language) {
		this.language = language;
	}

}
