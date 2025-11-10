package com.vinsys.hrms.dao.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.attendance.EmployeeACN;

public interface IHRMSAttendaceEmployeeACNDAO extends JpaRepository<EmployeeACN, Long> {

	@Query(" SELECT empAcn FROM EmployeeACN empAcn WHERE empAcn.isActive = ?1 AND "
			+ " empAcn.employee.id = ?2 ")
	public EmployeeACN getEmployeeACNDetailsByEmpId(String isActive, long empId);
}
