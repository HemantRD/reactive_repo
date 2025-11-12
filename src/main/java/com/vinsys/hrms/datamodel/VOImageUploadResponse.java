package com.vinsys.hrms.datamodel;
/**
 * 
 * @author monika
 *
 */
public class VOImageUploadResponse extends HRMSBaseResponse {

	private Integer countOfSuccessFullyImages;
	private StringBuffer failureImageName;
	private StringBuffer validationImageName;
	public VOImageUploadResponse() {
	}
	
	public Integer getCountOfSuccessFullyImages() {
		return countOfSuccessFullyImages;
	}
	public void setCountOfSuccessFullyImages(Integer countOfSuccessFullyImages) {
		this.countOfSuccessFullyImages = countOfSuccessFullyImages;
	}
	public StringBuffer getFailureImageName() {
		return failureImageName;
	}
	public void setFailureImageName(StringBuffer failureImageName) {
		this.failureImageName = failureImageName;
	}
	
	public StringBuffer getValidationImageName() {
		return validationImageName;
	}

	public void setValidationImageName(StringBuffer validationImageName) {
		this.validationImageName = validationImageName;
	}

	public VOImageUploadResponse(Integer countOfSuccessFullyImages, StringBuffer failureImageName,StringBuffer validationImageName) {
		super();
		this.countOfSuccessFullyImages = countOfSuccessFullyImages;
		this.failureImageName = failureImageName;
		this.validationImageName=validationImageName;
	}
}
