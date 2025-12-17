package com.vinsys.hrms.employee.vo;

import java.util.List;

public class PreviousEmploymentVO {
	private List<PreviousEmploymentDetailsVO> previousEmployment;
	
	private List<IdentificationDetailsVO> documents;

	public List<PreviousEmploymentDetailsVO> getPreviousEmployment() {
		return previousEmployment;
	}

	public void setPreviousEmployment(List<PreviousEmploymentDetailsVO> previousEmployment) {
		this.previousEmployment = previousEmployment;
	}

	public List<IdentificationDetailsVO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<IdentificationDetailsVO> documents) {
		this.documents = documents;
	}

	
	
}
