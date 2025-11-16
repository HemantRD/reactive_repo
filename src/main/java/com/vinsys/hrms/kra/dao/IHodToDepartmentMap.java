package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.HodToDepartmentMap;

public interface IHodToDepartmentMap extends JpaRepository<HodToDepartmentMap, Long> {

	List<HodToDepartmentMap> findByEmployeeIdAndIsActive(Long empId, String isActive);

	@Query(value = "select department  from tbl_map_hod_to_department tmhtd where employee =?1 and is_active =?2 ", nativeQuery = true)
	List<Long> getDepartmentByEmployeeAndIsActive(Long empId, String isActive);

	@Query(value = "select division  from tbl_map_hod_to_department tmhtd where employee =?1 and is_active =?2 ", nativeQuery = true)
	List<Long> getDivisionByEmployeeAndIsActive(Long empId, String isActive);

	List<HodToDepartmentMap> findByIsActiveAndOrgId(String name, long orgId);
	
	
	@Query(value = "select * from tbl_map_hod_to_department tmhtd where department =?1 and org_id=?2 and is_active =?3 ", nativeQuery = true)
	HodToDepartmentMap findByDepartmentIdAndOrgIdAndIsActive(Long deptId, Long orgId, String isActive );
	
	@Query(value = "select distinct employee from tbl_map_hod_to_department where is_active =?1", nativeQuery = true)
	List<Long> findAllHodByIsActive(String isActive );

	boolean existsByEmployeeIdAndDepartmentIdAndIsActive(Long employeeId, Long departmentId, String isActive);
	
	HodToDepartmentMap findByIdAndIsActive(Long id, String isActive);

	@Query(value = "SELECT * FROM tbl_map_hod_to_department " + "WHERE org_id = ?1 " + "AND is_active = ?2 "
			+ "ORDER BY id DESC", nativeQuery = true)
	List<HodToDepartmentMap> findByActiveAndOrgId(Long orgId, String isActive, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_map_hod_to_department " + "WHERE org_id = ?1 "
			+ "AND is_active = ?2", nativeQuery = true)
	long countActiveByOrgId(Long orgId, String isActive);

	HodToDepartmentMap findByDepartmentIdAndIsActive(Long departmentId, String isactive);
	
	@Query(value = "SELECT * FROM tbl_map_hod_to_department "
			+ "WHERE employee = ?1 AND is_active = ?2 LIMIT 1", nativeQuery = true)
	HodToDepartmentMap findSingleByEmployeeIdAndIsActive(Long employeeId, String isActive);


}
