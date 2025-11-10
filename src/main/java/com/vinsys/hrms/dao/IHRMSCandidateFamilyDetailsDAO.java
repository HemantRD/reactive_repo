package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidateFamilyDetail;

public interface IHRMSCandidateFamilyDetailsDAO extends JpaRepository<CandidateFamilyDetail, Long> {

	@Query("select resp from CandidateFamilyDetail resp")
	public List<CandidateFamilyDetail> findallCandidateFamilyDetails();

	@Query("select resp from CandidateFamilyDetail resp where resp.candidatePersonalDetail.id=?1")
	public List<CandidateFamilyDetail> findCandidateFamilyDetailsbyId(long candidatePersonalDetailId);
	
	@Query(value="select * from tbl_candidate_family_detail detail where detail.id =?1 and detail.candidate_personal_detail_id =?2 and detail.org_id =?3 and detail.is_active =?4",nativeQuery = true)
	public CandidateFamilyDetail findByIdAndCandidatePersonalIdAndOrgIdAndIsActive(Long id,Long candidatePersonalDetailId , Long orgId,String isActive);

}
