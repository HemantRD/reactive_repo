package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeDesignation;
import org.springframework.data.jpa.repository.Query;

public interface IEmployeeDesignationDAO  extends JpaRepository<EmployeeDesignation, Long> {
	
	EmployeeDesignation findByEmployee(Employee emp);

    @Query(value = "SELECT designation_id FROM tbl_employee_designation WHERE employee_id = ?1", nativeQuery = true)
    public Long findDesignationIdByEmployeeId(Long employeeId);

	boolean existsByDesignationIdAndIsActive(Long id, String isactive);

}
