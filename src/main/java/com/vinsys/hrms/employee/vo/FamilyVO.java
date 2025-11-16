package com.vinsys.hrms.employee.vo;

import java.util.List;

/**
 * @author Onkar A
 *
 * 
 */
public class FamilyVO {

	List<FamilyDetailsVO> familyDetails;
	List<EmergencyContactVO> emergencyContactDetails;

	public List<FamilyDetailsVO> getFamilyDetails() {
		return familyDetails;
	}

	public void setFamilyDetails(List<FamilyDetailsVO> familyDetails) {
		this.familyDetails = familyDetails;
	}

	public List<EmergencyContactVO> getEmergencyContactDetails() {
		return emergencyContactDetails;
	}

	public void setEmergencyContactDetails(List<EmergencyContactVO> emergencyContactDetails) {
		this.emergencyContactDetails = emergencyContactDetails;
	}
	
}
