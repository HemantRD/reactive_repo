package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.CandidateEmergencyContact;
import com.vinsys.hrms.entity.CandidateEmergencyContactAddress;

public interface IHRMSCandidateEmergencyContactAddressDAO
		extends JpaRepository<CandidateEmergencyContactAddress, Long> {

	public CandidateEmergencyContactAddress findBycandidateEmergencyContact(
			CandidateEmergencyContact candidateEmergencyContact);
}
