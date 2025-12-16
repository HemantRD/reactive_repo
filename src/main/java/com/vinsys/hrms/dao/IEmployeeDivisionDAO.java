package com.vinsys.hrms.dao;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IEmployeeDivisionDAO  extends JpaRepository<EmployeeDivision, Long> {

	EmployeeDivision findByEmployeeAndIsActive(Employee employee, String name);
	
	boolean existsByDivisionIdAndIsActive(Long divisionId, String isActive);

	List<EmployeeDivision> findByDivision_IdAndIsActive(Long divisionId, String isActive);

    @Query(value = "SELECT division_id FROM tbl_employee_division WHERE employee_id = ?1", nativeQuery = true)
    public Long findDivisionIdByEmployeeId(Long employeeId);

}
