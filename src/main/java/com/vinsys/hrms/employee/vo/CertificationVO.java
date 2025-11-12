
package com.vinsys.hrms.employee.vo;

import java.util.List;

/**
 * @author Onkar A
 *
 * 
 */
public class CertificationVO {

	List<CertificationDetailsVO> certificationDetails;
	private List<IdentificationDetailsVO> documents;

	public List<CertificationDetailsVO> getCertificationDetails() {
		return certificationDetails;
	}

	public void setCertificationDetails(List<CertificationDetailsVO> certificationDetails) {
		this.certificationDetails = certificationDetails;
	}

	public List<IdentificationDetailsVO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<IdentificationDetailsVO> documents) {
		this.documents = documents;
	}

}
