package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterCandidateActivityActionType;

public interface IHRMSMasterCandidateActivityActionTypeDAO
		extends JpaRepository<MasterCandidateActivityActionType, Long> {

	@Query("select activityActionType from MasterCandidateActivityActionType activityActionType where activityActionType.orgId = ?1"
			+ " and activityActionType.isActive  = ?2")
	public List<MasterCandidateActivityActionType> findAllCandidateActivityTypeByOrganization(Long id, String isActive);

	@Query(" select activityActionType from MasterCandidateActivityActionType activityActionType "
			+ " where activityActionType.orgId = ?1 AND activity_action_type_name = 'Onboard' ")
	public MasterCandidateActivityActionType onboardCandidateActivityTypeByOrganization(Long id);

}
