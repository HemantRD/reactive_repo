package com.vinsys.hrms.employee.vo;

import java.util.List;

/**
 * 
 * @author Onkar A.
 *
 */
public class EducationVO {
	List<EducationalDetailsVO> educationDetails;
	private List<IdentificationDetailsVO> documents;

	public List<EducationalDetailsVO> getEducationDetails() {
		return educationDetails;
	}

	public void setEducationDetails(List<EducationalDetailsVO> educationDetails) {
		this.educationDetails = educationDetails;
	}

	public List<IdentificationDetailsVO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<IdentificationDetailsVO> documents) {
		this.documents = documents;
	}

	
}
