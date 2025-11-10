package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.attendance.EmployeeACN;

public interface IHRMSEmployeeAcnDAO extends JpaRepository<EmployeeACN, Long> {

	EmployeeACN findByEmpACN(long cardNo);

	@Query("select emp from Employee emp JOIN FETCH emp.employeeACN empAcn " + "JOIN FETCH emp.candidate cand "
			+ "JOIN FETCH cand.loginEntity loginEntity " + "JOIN FETCH cand.candidateProfessionalDetail candProf "
			+ "JOIN FETCH candProf.department dpt " + "JOIN FETCH candProf.division div "
			+ "JOIN FETCH candProf.branch branch " + "JOIN FETCH candProf.designation designation "
			+ "where empAcn.empACN = ?1 and emp.isActive='Y' and empAcn.isActive='Y' and loginEntity.organization.id = ?2 ")
	Employee findByACNEmpObj(long cardNo, long orgId);

	List<EmployeeACN> findByIsActive(String isActive);

	
	@Query(value="select * from tbl_attendance_employee_acn where employee_id=?1",nativeQuery=true)
	public EmployeeACN findByEmpId(Long empId);

	@Query(value="select * from tbl_attendance_employee_acn where employee_id=?1 and is_active=?2",nativeQuery=true)
	public EmployeeACN findByEmpIdAndIsActive(Long empId,String isActive);

}
