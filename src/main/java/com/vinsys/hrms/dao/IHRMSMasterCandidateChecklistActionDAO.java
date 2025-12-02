package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterCandidateChecklistAction;

public interface IHRMSMasterCandidateChecklistActionDAO extends JpaRepository<MasterCandidateChecklistAction, Long> {

	@Query("select checklistAction from MasterCandidateChecklistAction checklistAction where checklistAction.orgId = ?1"
			+ " and checklistAction.isActive = ?2")
	public List<MasterCandidateChecklistAction> findAllCandidateChecklistActionOrgwise(long orgId, String isActive);

	@Query("SELECT checklistAction FROM MasterCandidateChecklistAction checklistAction "
			+ "WHERE checklistAction.orgId = ?1 AND checklistAction.candidateChecklistActionName = ?2 ")
	public MasterCandidateChecklistAction findAllCandidateChecklistActionOrgwiseAndActionNamewise(long orgId,
			String actionName);

}
