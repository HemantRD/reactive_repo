package com.vinsys.hrms.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidatePersonalDetail;

public interface IHRMSCandidatePersonalDetailDAO extends JpaRepository<CandidatePersonalDetail, Long> {

	//@Query("select candPersonalDetail from CandidatePersonalDetail candPersonalDetail where candPersonalDetail.candidate.id=?1")
	//public CandidatePersonalDetail findByCandidateId(long candidate_id);
	
	
	
	@Query(value="select * from tbl_candidate_personal_detail  where candidate_id =?1 and is_active=?2 and org_id =?3",nativeQuery = true)
	public CandidatePersonalDetail findByCandidateAndIsActiveAndOrgId(Long candidateId, String isActive,Long orgId);

	public CandidatePersonalDetail findBycandidate(Candidate candidate);

	public Optional<CandidatePersonalDetail> findByCandidateId(Long id);
	
	

}
