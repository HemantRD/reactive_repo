package com.vinsys.hrms.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterDesignation;

public interface IHRMSMasterDesignationDAO extends JpaRepository<MasterDesignation, Long> {

	@Query("select designation from MasterDesignation designation where designation.organization.id = ?1 "
			+ " and designation.isActive = ?2")
	public List<MasterDesignation> findAllMasterDesignationByOrgIdCustomQuery(long orgId, String isActive);
	

	//added by akanksha Gaikwad for manage designation
	
	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_designation WHERE LOWER(designation_name) = LOWER(:designationName)", nativeQuery = true)
	boolean existsByDesignationNameIgnoreCase(String designationName);

	@Modifying
	@Query("update MasterDesignation designation set is_active='N' where designation.id = ?1 ")
	@Transactional
	void deleteDesignation(long desigationId);
	
	List<MasterDesignation> findByIsActive(String isActive);
	
	@Query("select designation from MasterDesignation designation where designation.designationName = ?1 "
			+ " and designation.isActive = ?2")
	public MasterDesignation findDesignationNameAndIsActive(String designationName, String isActive);


	public MasterDesignation findByIdAndIsActive(Long id, String isactive);
	
	@Query(value = "SELECT * FROM tbl_mst_designation "
			+ "WHERE org_id = ?1 AND (designation_name ILIKE %?2% OR designation_description ILIKE %?2%) "
			+ "ORDER BY is_active DESC", nativeQuery = true)
	List<MasterDesignation> searchDesignationByOrgIdAndText(Long orgId, String keyword, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_mst_designation "
			+ "WHERE org_id = ?1 AND (designation_name ILIKE %?2% OR designation_description ILIKE %?2%)", nativeQuery = true)
	long countDesignationByOrgIdAndText(Long orgId, String keyword);

	

}
