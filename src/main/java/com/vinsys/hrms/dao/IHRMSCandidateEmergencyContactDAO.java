package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidateEmergencyContact;

public interface IHRMSCandidateEmergencyContactDAO extends JpaRepository<CandidateEmergencyContact, Long> {

	@Query("Select resp from CandidateEmergencyContact resp")
	public List<CandidateEmergencyContact> finadallCandidateEmergencyContactDetails();

	@Query("Select resp from CandidateEmergencyContact resp where resp.candidatePersonalDetail.id=?1")
	public List<CandidateEmergencyContact> findCandidateEmergencyDetailsbyId(long id);

}
