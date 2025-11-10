package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidateStatutoryNomination;

public interface IHRMSCandidateStatutoryNominationDAO extends JpaRepository<CandidateStatutoryNomination, Long> {

	@Query("Select resp from CandidateStatutoryNomination resp where resp.isActive=?1")
	public List<CandidateStatutoryNomination> finadallCandidateStatutoryNominationDetails(String isActive);

	@Query("Select resp from CandidateStatutoryNomination resp where resp.isActive=?1 and resp.candidatePersonalDetail.id=?2")
	public List<CandidateStatutoryNomination> finadallCandidateStatutoryNominationDetailsbyId(String isActive,
			long candidatePersonalDetailId);

	public CandidateStatutoryNomination findBycandidatePersonalDetail(CandidatePersonalDetail candidatePersonalDetail);

}
