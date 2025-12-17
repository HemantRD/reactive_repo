package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterRole;

public interface IHRMSRoleMasterDAO extends JpaRepository<MasterRole, Long> {

	/**
	 * this method returns all branch masters by organization id
	 * 
	 * @param Long
	 *            orgId
	 * @return list of branch master
	 * @author shinde.devendra
	 * 
	 */

	@Query("select branch from MasterBranch branch where branch.organization.id = ?1")
	public List<MasterBranch> findAllMasterBranchByOrgIdCustomQuery(long orgId);

	public MasterRole findByroleName(String roleName);
}
