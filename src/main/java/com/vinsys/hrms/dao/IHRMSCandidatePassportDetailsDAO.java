package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidatePassportDetail;
import com.vinsys.hrms.entity.CandidatePersonalDetail;

public interface IHRMSCandidatePassportDetailsDAO extends JpaRepository<CandidatePassportDetail, Long> {

	@Query("Select resp from CandidatePassportDetail resp where resp.isActive=?1")
	public List<CandidatePassportDetail> findallCandidatePassportDetails(String isActive);

	@Query("Select resp from CandidatePassportDetail resp where resp.isActive=?1 and resp.candidatePersonalDetail.id=?2")
	public List<CandidatePassportDetail> findallCandidatePassportDetailsbyId(String isActive,
			long candidatePersonalDetailId);

	public CandidatePassportDetail findBycandidatePersonalDetail(CandidatePersonalDetail candidatePersonalDetail);
}
