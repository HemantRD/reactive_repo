package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidateVisaDetail;

public interface IHRMSCandidateVisaDetailsDAO extends JpaRepository<CandidateVisaDetail, Long> {

	@Query("Select resp from CandidateVisaDetail resp where resp.isActive=?1")
	public List<CandidateVisaDetail> findallCandidateVisaDetails(String isActive);

	@Query("Select resp from CandidateVisaDetail resp where resp.isActive=?1 and resp.candidatePersonalDetail.id=?2")
	public List<CandidateVisaDetail> findallCandidateVisaDetailsbyId(String isActive, long candidatePersonalDetailId);

}
