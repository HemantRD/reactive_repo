package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterCandidateOnboardActionReason;

/**
 * @author waikar.saurabh
 *
 */
public interface IHRMSMasterOnboardActionReasonDAO extends JpaRepository<MasterCandidateOnboardActionReason, Long> {

	@Query("SELECT actionReason FROM MasterCandidateOnboardActionReason actionReason WHERE actionReason.organization.id = ?1 "
			+ " AND actionReason.typeOfAction = ?2 AND actionReason.isActive = ?3 ")
	public List<MasterCandidateOnboardActionReason> findAllMasterOnboardActionReasonByOrgIdCustomQuery(long id,
			String typeOfAction, String isActive);
}
