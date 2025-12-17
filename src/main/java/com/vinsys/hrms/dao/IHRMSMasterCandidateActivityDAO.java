package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterCandidateActivity;

public interface IHRMSMasterCandidateActivityDAO extends JpaRepository<MasterCandidateActivity, Long> {
	@Query("select candidateActivity from MasterCandidateActivity candidateActivity where candidateActivity.organization.id = ?1 "
			+ " AND candidateActivity.division.id = ?2 AND candidateActivity.isActive = ?3 ")
	public List<MasterCandidateActivity> findAllMasterCandidateActivityByOrgIdCustomQuery(long orgId, long divisionId,
			String active);

}
