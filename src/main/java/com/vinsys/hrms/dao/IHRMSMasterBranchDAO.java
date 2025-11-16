package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterBranch;

public interface IHRMSMasterBranchDAO extends JpaRepository<MasterBranch, Long> {

	/**
	 * this method returns all branch masters by organization id
	 * 
	 * @param Long
	 *            orgId
	 * @return list of branch master
	 * @author shinde.devendra
	 * 
	 */
	@Query("select branch from MasterBranch branch where branch.organization.id = ?1 and branch.isActive = ?2")
	public List<MasterBranch> findAllMasterBranchByOrgIdCustomQuery(long orgId, String isActive);
	
	List<MasterBranch> findByIsActiveAndOrgId(String isActive,Long orgId);

	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_branch WHERE LOWER(branch_name) = LOWER(:branchName)", nativeQuery = true)
	public boolean existsByBranchName(String branchName);
	
	@Query("select branch from MasterBranch branch where branch.branchName = ?1 and branch.isActive = ?2")
	public MasterBranch findByName(String name, String isActive);

	public MasterBranch findByIdAndIsActive(Long id, String isactive);
	
	//EmployeeBranch findByEmployeeAndOrganisation(Employee employee, Organization organisation);
	
	@Query(value = "SELECT * FROM tbl_mst_branch " + "WHERE org_id = ?1 AND "
			+ "(branch_name ILIKE %?2% OR branch_description ILIKE %?2%) "
			+ "ORDER BY is_active DESC", nativeQuery = true)
	List<MasterBranch> searchBranchByOrgIdAndText(Long orgId, String searchText, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_mst_branch " + "WHERE org_id = ?1 AND "
			+ "(branch_name ILIKE %?2% OR branch_description ILIKE %?2%)", nativeQuery = true)
	long countBranchByOrgIdAndText(Long orgId, String searchText);
	
}
