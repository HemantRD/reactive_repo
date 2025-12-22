package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterCandidateChecklist;

public interface IHRMSMasterCandidateChecklistDAO extends JpaRepository<MasterCandidateChecklist, Long> {

	@Query("SELECT mcc FROM MasterCandidateChecklist mcc WHERE mcc.organization.id = ?1 " + " AND mcc.isActive = ?2 ")
	public List<MasterCandidateChecklist> getCandidateChecklistByOrgId(long orgId, String isActive);

	@Query("SELECT mcc FROM MasterCandidateChecklist mcc WHERE mcc.organization.id = ?1 " 
			+ "AND mcc.checklistItemCode = ?2 AND mcc.isActive = ?3 AND mcc.masterDivision.id=?4 ")
	public MasterCandidateChecklist getMasterCandidateChecklistByOrgIdAndCode(long orgId,
			String checklistItemCode, String isActive,long  divId);

	
	@Query(" SELECT mcc FROM Candidate cand "
			+" JOIN  cand.candidateProfessionalDetail prod "
	        +" LEFT JOIN  MasterCandidateChecklist  mcc on mcc.masterDivision.id= prod.division.id and  mcc.masterDivision.id = ?3 "
	        + "and mcc.isActive = ?2 " 
	        +" WHERE mcc.organization.id = ?1 "
	  	    +" AND  cand.id = ?4 ")
	public List<MasterCandidateChecklist> getCandidateChecklistByOrgIdAndDivId(long orgId, String isActive,long  divId,
			long canId);	
}
