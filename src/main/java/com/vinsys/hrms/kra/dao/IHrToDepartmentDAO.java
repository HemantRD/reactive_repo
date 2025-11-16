package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.HrToDepartment;

public interface IHrToDepartmentDAO extends JpaRepository<HrToDepartment, Long> {

	List<HrToDepartment> findByIsActive(String isActive);

	
	@Query(value = "select department_id  from tbl_map_hr_to_department tmhtd where employee_id =?1", nativeQuery = true)
	List<Long> findByEmployeeIds(Long loggedInEmpId);


	HrToDepartment findByDepartmentIdAndIsActive(Long deptId, String isactive);

}
