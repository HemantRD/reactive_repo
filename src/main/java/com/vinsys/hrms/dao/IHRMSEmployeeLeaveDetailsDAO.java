package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;

public interface IHRMSEmployeeLeaveDetailsDAO extends JpaRepository<EmployeeLeaveDetail, Long> {

	public List<EmployeeLeaveDetail> findByemployee(Employee employee);

	@Query("select empLeaveDetail from EmployeeLeaveDetail empLeaveDetail "
			+ " join fetch empLeaveDetail.employee emp where empLeaveDetail.employee.id = ?1 "
			+ " and empLeaveDetail.masterLeaveType.id = ?2 and emp.isActive = ?3 " + " and empLeaveDetail.year = ?4")
	public List<EmployeeLeaveDetail> findLeaveAvailableByEmployeeAndLeaveType(long empId, long leaveTypeId,
			String isActive, int year);

	@Query("select empLeaveDetail from EmployeeLeaveDetail empLeaveDetail where empLeaveDetail.employee.id = ?1 "
			+ " and empLeaveDetail.masterLeaveType.id = ?2 and empLeaveDetail.year=?3")
	public EmployeeLeaveDetail findEmployeeLeaveByEIDYEAR(long EmployeeId, long masterLeaveID, int year);
	
	@Query(nativeQuery = true, value ="select * from tbl_employee_leave_detail where employee_id=?1 and leave_type_id=?2 and year=?3 and org_id=?4")
	public EmployeeLeaveDetail findEmployeeLeaveByEIDYEARAndOrgId(long EmployeeId, long masterLeaveID, int year,Long orgId);

	@Query("select empLeaveDetail from EmployeeLeaveDetail empLeaveDetail where empLeaveDetail.employee.id = ?1 "
			+ " and empLeaveDetail.year=?2")
	public List<EmployeeLeaveDetail> findEmployeeLeaveByEmpIdYear(long EmployeeId, int year);

	@Query("select empLeaveDetail from EmployeeLeaveDetail empLeaveDetail where empLeaveDetail.employee.id = ?1 "
			+ " and empLeaveDetail.masterLeaveType.id = ?2 and empLeaveDetail.year=?3")
	public EmployeeLeaveDetail findEmployeeCreditedLeaveByEIDYEAR(long EmployeeId, long masterLeaveID, int year);

	@Query("select empLeaveDetail from EmployeeLeaveDetail empLeaveDetail where employee_id = ?1 "
			+ " and leave_type_id = ?2 and leave_available !=?3")
	public List<EmployeeLeaveDetail> findEmployeeLeaveByEmployeeAndTypeLeave1(long EmployeeId, long masterLeaveID,
			int leaveAvailable);

	@Query("select empLeaveDetail from EmployeeLeaveDetail empLeaveDetail "
			+ " join fetch empLeaveDetail.employee emp where empLeaveDetail.employee.id = ?1 "
			+ " and empLeaveDetail.masterLeaveType.id = ?2 and emp.isActive = ?3 " + " and empLeaveDetail.year = ?4")
	public EmployeeLeaveDetail findLeaveAvailableByEmpIdAndLeaveType(long empId, long leaveTypeId, String isActive,
			int year);

	@Query(value = "select * from tbl_employee_leave_detail teld  where year =?1 and employee_id =?2 and leave_available >0 ", nativeQuery = true)
	public List<EmployeeLeaveDetail> getAvailableLeaveByEmployee(int year, Long empId);
	
	@Query(value = "select * from tbl_employee_leave_detail teld  where year =?1 and employee_id =?2 and leave_available >0 and org_id=?3", nativeQuery = true)
	public List<EmployeeLeaveDetail> getAvailableLeaveByEmployeeAndOrgId(int year, Long empId,Long orgId);
	
	@Query("select empLeaveDetail from EmployeeLeaveDetail empLeaveDetail where employee_id = ?1 "
			+ " and leave_type_id = ?2 and year=?3")
	public EmployeeLeaveDetail findEmployeeLeaveDetailsYearWise(long EmployeeId, long masterLeaveID,
			int year);

	
	
	@Query(nativeQuery = true, value = "SELECT * FROM tbl_employee_leave_detail d "
			+ "JOIN tbl_employee te ON d.employee_id = te.id "
			+ "WHERE d.leave_type_id = ?1 AND te.is_active = ?2 AND d.year = ?3")
	public List<EmployeeLeaveDetail> findAllEmployeesLeaveDetails(long leaveType, String isActive, int year);
	
	@Query(value="select count(*) from tbl_employee_leave_detail empLeaveDetail "
			+ " join tbl_employee emp on empleavedetail.employee_id =emp.id "
			+ " where empleavedetail.employee_id =?1 and empleavedetail.leave_type_id = ?2 and emp.is_active =?3 and empleavedetail.year =?4 and emp.org_id = ?5",nativeQuery=true)
	public long  countOfLeaveAvailableByEmpIdAndLeaveTypeAndOrgId(Long empId, long leaveTypeId, String isActive,
			int year,Long orgId);

}
