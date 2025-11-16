package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeDepartment;

public interface IEmployeeDepartmentDAO extends JpaRepository<EmployeeDepartment, Long> {
	
	@Query(value="select * from tbl_employee_department where  department_id=?1 and is_active =?2 ",nativeQuery=true)
	public List<EmployeeDepartment> findByDepartmentId(Long deptId,String isActive);
	
	
	EmployeeDepartment findByEmployee(Employee emp);


	public boolean existsByDepartmentIdAndIsActive(Long id, String isactive);
	

}
