package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.EmployeeCurrentDetail;

public interface IHRMSEmployeeCurrentOrganizationDetailDAO extends JpaRepository<EmployeeCurrentDetail, Long> {

	@Query(" SELECT ecd FROM EmployeeCurrentDetail ecd WHERE ecd.employee.id = ?1 ")
	public EmployeeCurrentDetail findEmployeeCurrentDetailEmployeeWise(long empId);
	
	@Query(" SELECT ecd FROM EmployeeCurrentDetail ecd WHERE ecd.employee.id = ?1 and ecd.isActive=?2 ")
	public EmployeeCurrentDetail findByEmpIdAndIsActive(long empId, String isActive);
}
