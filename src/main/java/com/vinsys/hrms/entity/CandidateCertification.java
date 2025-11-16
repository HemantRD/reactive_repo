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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_candidate_certification")
public class CandidateCertification extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_candidate_certification", sequenceName = "seq_candidate_certification", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_certification")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "candidate_professional_detail_id", nullable = false)
	private CandidateProfessionalDetail candidateProfessionalDetail;
	@Column(name = "certification_name")
	private String certificationName;
	@Column(name = "certification_type")
	private String certificationType;
	@Column(name = "certification_date")
	@Temporal(TemporalType.DATE)
	private Date certificationDate;
	@Column(name = "certification_validity_date")
	@Temporal(TemporalType.DATE)
	private Date certificationValidityDate;
	@Column(name = "percentage_grade")
	private String percentageGrade;
	@Column(name = "mode_of_education")
	private String modeOfEducation;
	@Column(name="name_of_institute")
	private String nameOfInstitute;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CandidateProfessionalDetail getCandidateProfessionalDetail() {
		return candidateProfessionalDetail;
	}

	public void setCandidateProfessionalDetail(CandidateProfessionalDetail candidateProfessionalDetail) {
		this.candidateProfessionalDetail = candidateProfessionalDetail;
	}

	public String getCertificationName() {
		return certificationName;
	}

	public void setCertificationName(String certificationName) {
		this.certificationName = certificationName;
	}

	public String getCertificationType() {
		return certificationType;
	}

	public void setCertificationType(String certificationType) {
		this.certificationType = certificationType;
	}

	public Date getCertificationDate() {
		return certificationDate;
	}

	public void setCertificationDate(Date certificationDate) {
		this.certificationDate = certificationDate;
	}

	public Date getCertificationValidityDate() {
		return certificationValidityDate;
	}

	public void setCertificationValidityDate(Date certificationValidityDate) {
		this.certificationValidityDate = certificationValidityDate;
	}

	public String getPercentageGrade() {
		return percentageGrade;
	}

	public void setPercentageGrade(String percentageGrade) {
		this.percentageGrade = percentageGrade;
	}

	public String getModeOfEducation() {
		return modeOfEducation;
	}

	public void setModeOfEducation(String modeOfEducation) {
		this.modeOfEducation = modeOfEducation;
	}
	

	

	public String getNameOfInstitute() {
		return nameOfInstitute;
	}

	public void setNameOfInstitute(String nameOfInstitute) {
		this.nameOfInstitute = nameOfInstitute;
	}

	@Override
	public String toString() {
		return "CandidateCertification [id=" + id + ", candidateProfessionalDetail=" + candidateProfessionalDetail
				+ ", certificationName=" + certificationName + ", certificationType=" + certificationType
				+ ", certificationDate=" + certificationDate + ", certificationValidityDate="
				+ certificationValidityDate + ", percentageGrade=" + percentageGrade + ", modeOfEducation="
				+ modeOfEducation + ", nameOfIstitute=" + nameOfInstitute + "]";
	}

	

}
