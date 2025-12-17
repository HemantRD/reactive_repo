package com.vinsys.hrms.directonboard.vo;

import java.util.List;

import com.vinsys.hrms.employee.vo.IdentificationDetailsVO;

public class AddressDetailsRequestVo {

	private String nationality;
	private String citizenship;
	private Long SSNumber;
	private CandidateAddressVo presentAddress;
	private CandidateAddressVo permanentAddress;
	private Long candidateId;
	private List<IdentificationDetailsVO> permanentDocuments;
	private List<IdentificationDetailsVO> presentDocuments;

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public Long getSSNumber() {
		return SSNumber;
	}

	public void setSSNumber(Long sSNumber) {
		SSNumber = sSNumber;
	}

	public CandidateAddressVo getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(CandidateAddressVo presentAddress) {
		this.presentAddress = presentAddress;
	}

	public CandidateAddressVo getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(CandidateAddressVo permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public List<IdentificationDetailsVO> getPermanentDocuments() {
		return permanentDocuments;
	}

	public void setPermanentDocuments(List<IdentificationDetailsVO> permanentDocuments) {
		this.permanentDocuments = permanentDocuments;
	}

	public List<IdentificationDetailsVO> getPresentDocuments() {
		return presentDocuments;
	}

	public void setPresentDocuments(List<IdentificationDetailsVO> presentDocuments) {
		this.presentDocuments = presentDocuments;
	}

}
