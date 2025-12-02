package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterEmploymentType;

public interface IHRMSMasterEmployementTypeDAO extends JpaRepository<MasterEmploymentType, Long> {

	@Query("select employmentType from MasterEmploymentType employmentType where employmentType.organization.id = ?1 "
			+ " AND employmentType.isActive = ?2 ")
	public List<MasterEmploymentType> findAllMasterEmploymentTypeByOrgIdCustomQuery(long orgId, String isActive);

	public List<MasterEmploymentType> findByIsActiveAndEmploymentTypeDescriptionIn(String isActive,
			List<String> employmentTypeName);
	
	public MasterEmploymentType findByIsActiveAndEmploymentTypeName(String isActive, String employmentTypeName);
	
	@Query(value="select * from tbl_mst_employment_type where is_active =?1 order by id asc",nativeQuery = true)
	public List<MasterEmploymentType> findByIsActive(String isActive);
	
	@Query(value="select count(*) from tbl_mst_employment_type where is_active =?1",nativeQuery = true)
	public long countOfTotalRecord(String isActive);

	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_employment_type WHERE LOWER(employment_type_name) = LOWER(:employmentTypeName)", nativeQuery = true)
	public boolean existsByEmploymentTypeName(String employmentTypeName);

	public MasterEmploymentType findByIdAndIsActive(Long id, String isactive);
	
	@Query(value = "SELECT * FROM tbl_mst_employment_type " + "WHERE org_id = ?1 AND "
			+ "(employment_type_name ILIKE %?2% OR employment_type_description ILIKE %?2%) "
			+ "ORDER BY is_active DESC", nativeQuery = true)
	List<MasterEmploymentType> searchEmploymentTypeByOrgIdAndText(Long orgId, String searchText, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_mst_employment_type " + "WHERE org_id = ?1 AND "
			+ "(employment_type_name ILIKE %?2% OR employment_type_description ILIKE %?2%)", nativeQuery = true)
	long countEmploymentTypeByOrgIdAndText(Long orgId, String searchText);

}
