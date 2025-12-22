package com.vinsys.hrms.directonboard.vo;

public class FileUploadRequestVO {

	private Long candidateId;
	private String uploadtype;
	private String documentName;
	private String submited;
	private String mandatory;
	
	public Long getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}
	public String getUploadtype() {
		return uploadtype;
	}
	public void setUploadtype(String uploadtype) {
		this.uploadtype = uploadtype;
	}
	
	public String getSubmited() {
		return submited;
	}
	public void setSubmited(String submited) {
		this.submited = submited;
	}
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
	
	
	
	
	
}
