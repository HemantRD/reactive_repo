package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterDepartment;

public interface IHRMSMasterDepartmentDAO extends JpaRepository<MasterDepartment, Long> {

	@Query("select department from MasterDepartment department where department.organization.id = ?1 and department.isActive = ?2")
	public List<MasterDepartment> findAllMasterDepartmentByOrgIdCustomQuery(long orgId, String isActive);
	
	List<MasterDepartment> findByIsActive(String isActive);

	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_department WHERE LOWER(department_name) = LOWER(:departmentName)", nativeQuery = true)
	boolean existsByDepartmentName(String departmentName);
	
	
	@Query("select department from MasterDepartment department where department.departmentName = ?1 and department.isActive = ?2")
	public MasterDepartment findByName(String name, String isActive);
	
	@Query("select id from MasterDepartment department where department.isActive = ?1")
	public List<Long> findByisActive(String isActive);

	public MasterDepartment findByIdAndIsActive(Long id, String isactive);
	
	@Query(value = "SELECT * FROM tbl_mst_department " + "WHERE org_id = ?1 AND "
			+ "(department_name ILIKE %?2% OR department_description ILIKE %?2%) "
			+ "ORDER BY is_active DESC", nativeQuery = true)
	List<MasterDepartment> searchDepartmentByOrgIdAndText(Long orgId, String searchText, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_mst_department " + "WHERE org_id = ?1 AND "
			+ "(department_name ILIKE %?2% OR department_description ILIKE %?2%)", nativeQuery = true)
	long countDepartmentByOrgIdAndText(Long orgId, String searchText);

	
}
