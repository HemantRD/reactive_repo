package com.vinsys.hrms.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidateLetter;

public interface IHRMSCandidateLetterDAO extends JpaRepository<CandidateLetter, Long> {

	@Query("SELECT candidateLetter FROM CandidateLetter candidateLetter WHERE candidateLetter.candidate.id = ?1 ")
	public Set<CandidateLetter> findLettersByCandidateId(long candidateId);
	
	@Query("SELECT candidateLetter FROM CandidateLetter candidateLetter WHERE candidateLetter.candidate.id = ?1 and  candidateLetter.letterType=?2 ")
	public CandidateLetter findLetterByCandidateIdAndLetterType(long candidateId, String letterType );
	
	@Query(value = "SELECT cc.* FROM tbl_candidate_letter cc " +
	        "WHERE cc.letter_type IN " +
	        "(select tmca.name from tbl_mst_candidate_activity tmca where division_id =?2) " +
	        "AND cc.candidate_id = ?1", nativeQuery = true)
	public List<CandidateLetter> getCandidateWithChecklistDetailsByItemId(long candidateId,long divId);
	
	@Query("SELECT candidateLetter FROM CandidateLetter candidateLetter WHERE candidateLetter.candidate.id = ?1 AND (candidateLetter.letterType ='Experience Letter' OR candidateLetter.letterType ='Relieving Letter') ")
	public List<CandidateLetter> getLetterListByCandidateId(long candidateId);
	
	@Query(value = "SELECT cc.* FROM tbl_candidate_letter cc " +
	        "WHERE cc.letter_type IN " +
	        "(select tmca.name from tbl_mst_candidate_activity tmca where division_id =?2) " +
	        "AND cc.candidate_id = ?1 AND cc.org_id = ?3 ", nativeQuery = true)
	public List<CandidateLetter> getCandidateWithChecklistDetailsByItemIdAndOrgId(long candidateId,long divId,Long orgId);
	
	@Query("SELECT candidateLetter FROM CandidateLetter candidateLetter WHERE candidateLetter.candidate.id = ?1 and  candidateLetter.letterType=?2 and  candidateLetter.isActive=?3 ")
	public CandidateLetter findLetterByCandidateIdAndLetterTypeAndIsActive(long candidateId, String letterType, String isActive );

}
