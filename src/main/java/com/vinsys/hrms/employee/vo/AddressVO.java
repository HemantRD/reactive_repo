package com.vinsys.hrms.employee.vo;

/**
 * 
 * @author Onkar A.
 *
 */
public class AddressVO {

	private String nationality;
	private String citizenship;
	private Long SSNumber;
	AddressDetailsVO presentAddress;
	AddressDetailsVO permanentAddress;
	private IdentificationDetailsVO permanentDocuments;
	private IdentificationDetailsVO presentDocuments;

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

	public AddressDetailsVO getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(AddressDetailsVO presentAddress) {
		this.presentAddress = presentAddress;
	}

	public AddressDetailsVO getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(AddressDetailsVO permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public Long getSSNumber() {
		return SSNumber;
	}

	public void setSSNumber(Long sSNumber) {
		SSNumber = sSNumber;
	}

	public IdentificationDetailsVO getPermanentDocuments() {
		return permanentDocuments;
	}

	public void setPermanentDocuments(IdentificationDetailsVO permanentDocuments) {
		this.permanentDocuments = permanentDocuments;
	}

	public IdentificationDetailsVO getPresentDocuments() {
		return presentDocuments;
	}

	public void setPresentDocuments(IdentificationDetailsVO presentDocuments) {
		this.presentDocuments = presentDocuments;
	}

}
