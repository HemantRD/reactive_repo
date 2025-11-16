package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeDesignation;

public interface IEmployeeDesignationDAO  extends JpaRepository<EmployeeDesignation, Long> {
	
	EmployeeDesignation findByEmployee(Employee emp);

	boolean existsByDesignationIdAndIsActive(Long id, String isactive);

}
