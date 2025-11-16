package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeBranch;

public interface IEmployeeBranchDAO  extends JpaRepository<EmployeeBranch, Long> {

	EmployeeBranch findByEmployeeAndIsActive(Employee emp, String name);

	boolean existsByBranchIdAndIsActive(Long id, String isactive);

}
