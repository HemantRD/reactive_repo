package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeDivision;

public interface IEmployeeDivisionDAO  extends JpaRepository<EmployeeDivision, Long> {

	EmployeeDivision findByEmployeeAndIsActive(Employee employee, String name);
	
	boolean existsByDivisionIdAndIsActive(Long divisionId, String isActive);


}
