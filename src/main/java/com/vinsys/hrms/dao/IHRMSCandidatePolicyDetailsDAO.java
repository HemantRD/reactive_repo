package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidatePolicyDetail;

public interface IHRMSCandidatePolicyDetailsDAO extends JpaRepository<CandidatePolicyDetail, Long> {

	@Query("Select resp from CandidatePolicyDetail resp where resp.isActive=?1")
	public List<CandidatePolicyDetail> finadallCandidatePolicyDetails(String isActive);

	@Query("Select resp from CandidatePolicyDetail resp where resp.isActive=?1 and resp.candidatePersonalDetail.id=?2")
	public List<CandidatePolicyDetail> findallCandidatePolicyDetailsbyId(String isActive,
			long candidatePersonalDetailId);

	public CandidatePolicyDetail findBycandidatePersonalDetail(CandidatePersonalDetail candidatePersonalDetail);

}
