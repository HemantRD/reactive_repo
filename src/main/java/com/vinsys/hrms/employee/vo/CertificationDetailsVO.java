package com.vinsys.hrms.employee.vo;

/**
 * @author Onkar A
 *
 * 
 */
public class CertificationDetailsVO {
	private String certificationName;
	private String certificationType;
	private String certificationDate;
	private String certificationValidityDate;
	private String percentageGrade;
	private String modeOfEducation;
	private String nameOfInstitute;
	private long certificationId;
	private IdentificationDetailsVO documents;
	private long candidateId;
	private long orgId;

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
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

	public String getCertificationDate() {
		return certificationDate;
	}

	public void setCertificationDate(String certificationDate) {
		this.certificationDate = certificationDate;
	}

	public String getCertificationValidityDate() {
		return certificationValidityDate;
	}

	public void setCertificationValidityDate(String certificationValidityDate) {
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

	public long getCertificationId() {
		return certificationId;
	}

	public void setCertificationId(long certificationId) {
		this.certificationId = certificationId;
	}

	public IdentificationDetailsVO getDocuments() {
		return documents;
	}

	public void setDocuments(IdentificationDetailsVO documents) {
		this.documents = documents;
	}

}
