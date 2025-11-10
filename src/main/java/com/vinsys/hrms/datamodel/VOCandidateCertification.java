package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOCandidateCertification extends VOAuditBase {

	private long id;
	private VOCandidateProfessionalDetail candidateProfessionalDetail;
	private String certificationName;
	private String certificationType;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date certificationDate;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date certificationValidityDate;
	private String percentageGrade;
	private String modeOfEducation;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOCandidateProfessionalDetail getCandidateProfessionalDetail() {
		return candidateProfessionalDetail;
	}

	public void setCandidateProfessionalDetail(VOCandidateProfessionalDetail candidateProfessionalDetail) {
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

}
