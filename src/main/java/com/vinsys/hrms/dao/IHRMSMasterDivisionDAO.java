package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterDivision;

public interface IHRMSMasterDivisionDAO extends JpaRepository<MasterDivision, Long> {

	@Query("select division from MasterDivision division where division.organization.id = ?1 "
			+ " and division.isActive = ?2")
	public List<MasterDivision> findAllMasterDivisionByOrgIdCustomQuery(long orgId, String isActive);
	
	@Query(value="select * from tbl_mst_division tmd where is_active =?1 and org_id =?2 order by id asc",nativeQuery=true)
	public List<MasterDivision> findByIsActiveAndOrgId(String isActive,Long orgId);

	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_division WHERE LOWER(division_name) = LOWER(:divisionName)", nativeQuery = true)
	public boolean existsByDivisionName(String divisionName);
	
	@Query("select division from MasterDivision division where division.divisionName = ?1 "
			+ " and division.isActive = ?2")
	public MasterDivision findByName(String name, String isActive);

	public MasterDivision findByIdAndIsActive(Long id, String isactive);
	
	@Query(value = "SELECT * FROM tbl_mst_division " + "WHERE org_id = ?1 AND division_name ILIKE %?2% "
			+ "ORDER BY is_active DESC", nativeQuery = true)
	List<MasterDivision> searchDivisionByOrgIdAndText(Long orgId, String keyword, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_mst_division "
			+ "WHERE org_id = ?1 AND division_name ILIKE %?2%", nativeQuery = true)
	long countDivisionByOrgIdAndText(Long orgId, String keyword);
}
