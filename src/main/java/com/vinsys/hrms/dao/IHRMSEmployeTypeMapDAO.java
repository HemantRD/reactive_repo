package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.EmployeTypeMap;

public interface IHRMSEmployeTypeMapDAO extends JpaRepository<EmployeTypeMap, Long> {

	EmployeTypeMap findByEntityId(Long entityId);

	@Query(value = "select * from tbl_map_employee_type where org_id =?1 and division =?2 and branch =?3 and department =?4 ", nativeQuery = true)
	EmployeTypeMap getEmployeeType(Long organizationId, Long divisionId, Long branch, Long department);
}
