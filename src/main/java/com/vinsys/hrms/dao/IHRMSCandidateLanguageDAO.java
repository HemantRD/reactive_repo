package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidateLanguage;

public interface IHRMSCandidateLanguageDAO extends JpaRepository<CandidateLanguage, Long> {

	@Query("select resp from CandidateLanguage resp where resp.isActive=?1")
	public List<CandidateLanguage> findallCandidateLanguage(String isActive);

	@Query(" SELECT cl FROM CandidateLanguage cl JOIN FETCH cl.language ml WHERE cl.isActive=?1 AND cl.candidatePersonalDetail.id=?2 "
			+ " AND ml.isActive = ?3 ")
	public List<CandidateLanguage> findallCandidateLanguagebyId(String isActive, long candidatePersonalDetailId,
			String masterLangIsActive);

	@Query(" SELECT count(cl) FROM CandidateLanguage cl WHERE cl.isActive=?1 AND cl.candidatePersonalDetail.id=?2 "
			+ " AND cl.motherTongue = ?3 ")
	public int getCountMotherTongueCandPersDetailswise(String isActive, long candidatePersonalDetailId,
			String motherTongue);

}
