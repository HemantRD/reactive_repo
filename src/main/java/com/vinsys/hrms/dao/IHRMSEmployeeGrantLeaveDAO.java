package com.vinsys.hrms.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeGrantLeaveDetail;

public interface IHRMSEmployeeGrantLeaveDAO extends JpaRepository<EmployeeGrantLeaveDetail, Long> {

	@Query("SELECT egld FROM EmployeeGrantLeaveDetail egld WHERE egld.employee.id = ?1")
	public List<EmployeeGrantLeaveDetail> findAllGrantLeaveDetailsOfEmployee(long employeeId);

	@Query("SELECT egld FROM EmployeeGrantLeaveDetail egld " + " JOIN egld.employee subEmployee "
			+ " JOIN subEmployee.employeeReportingManager empRM " + " WHERE empRM.reporingManager.id = ?1 "
			+ " AND empRM.employee.id = subEmployee.id "
			+ " AND (egld.leaveStatus = 'PENDING' OR egld.leaveStatus = 'WD_PENDING') order by egld.dateOfApplied desc")
	public List<EmployeeGrantLeaveDetail> findAllGrantLeaveDetailsOfSubEmployeesOfRM(long rmEmployeeId);

	@Query("SELECT egld FROM EmployeeGrantLeaveDetail egld WHERE egld.employee.id = ?1 "
			+ " AND egld.isActive = ?2 AND EXTRACT(year FROM egld.fromDate) = ?3 ")
	public List<EmployeeGrantLeaveDetail> findAllGrantLeaveDetailsOfEmployeeOfYear(long employeeId, String isActive,
			int year);

	@Query("SELECT egld FROM EmployeeGrantLeaveDetail egld WHERE egld.id = ?1")
	public EmployeeGrantLeaveDetail findById(long id);

	@Query("SELECT egld FROM EmployeeGrantLeaveDetail egld WHERE egld.employee.id = ?1 "
			+ " AND egld.isActive = ?2 AND EXTRACT(year FROM egld.fromDate) = ?3 ORDER BY egld.dateOfApplied DESC")
	public List<EmployeeGrantLeaveDetail> findAllGrantLeaveDetailsOfEmployeeOfYear(long employeeId, String isActive,
			int year, Pageable pageable);

	@Query(value = "SELECT count(*) FROM tbl_employee_grant_leave_detail egld WHERE egld.employee_id = ?1 "
			+ " AND egld.is_active = ?2 AND EXTRACT(year FROM egld.from_date) = ?3 ", nativeQuery = true)
	public long countByGrantLeaveDetailsOfEmployeeOfYear(long employeeId, String isActive, int year);

	EmployeeGrantLeaveDetail findByFromDateAndEmployee(Date fromDate, Employee empId);

	@Query(value = "SELECT * from tbl_employee_grant_leave_detail "
			+ " WHERE from_date <= ?1 AND to_date >= ?2 AND employee_id = ?3 AND leave_status != ?4 AND leave_status != ?5 ", nativeQuery = true)
	public EmployeeGrantLeaveDetail findOverlappingLeave(Date fromDate, Date toDate, Long empId,
			String leavestatusCancelled, String leavestatusReject);

	@Query(" SELECT ela FROM EmployeeGrantLeaveDetail ela JOIN FETCH ela.employee emp JOIN FETCH emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN ela.masterLeaveType mlt "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND erm.reporingManager.id = ?3 "
			+ " AND ela.leaveStatus = ?4 or ela.leaveStatus=?5 " + " AND date_part('year', ela.fromDate) = ?6 "
			+ " ORDER BY ela.id DESC ")
	public List<EmployeeGrantLeaveDetail> findSubordinateLeaveAppliedDetailsForManager(String candIsActive,
			String empIsActive, long managerId, String leaveStatusSeq1, String leaveStatusSeq2, int year,
			Pageable pageable);

	@Query("SELECT COUNT(ela) FROM EmployeeGrantLeaveDetail ela JOIN ela.employee emp JOIN emp.candidate cand "
			+ "JOIN emp.employeeReportingManager erm JOIN ela.masterLeaveType mlt "
			+ "WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND erm.reporingManager.id = ?3 "
			+ "AND (ela.leaveStatus = ?4 OR ela.leaveStatus = ?5) " + "AND date_part('year', ela.fromDate) = ?6")
	long countSubordinateLeaveAppliedDetails(String candIsActive, String empIsActive, long managerId,
			String leaveStatusSeq1, String leaveStatusSeq2, int year);

	@Query("SELECT ela FROM EmployeeGrantLeaveDetail ela " + "JOIN FETCH ela.employee emp "
			+ "JOIN FETCH emp.candidate cand " + "JOIN emp.employeeReportingManager erm "
			+ "JOIN ela.masterLeaveType mlt " + "WHERE cand.isActive = ?1 " + "AND emp.isActive = ?2 "
			+ "AND erm.reporingManager.id = ?3 " + "AND (ela.leaveStatus = ?4 OR ela.leaveStatus = ?5) "
			+ "AND date_part('year', ela.fromDate) = ?6 " + "AND emp.id = ?7 " + "ORDER BY ela.id DESC ")
	public List<EmployeeGrantLeaveDetail> findSubordinateLeaveAppliedDetailsForEmployee(String candIsActive,
			String empIsActive, long managerId, String leaveStatusSeq1, String leaveStatusSeq2, int year,
			Long employeeId);

	@Query(value = "select * from tbl_employee_grant_leave_detail  tegld "
			+ "  join tbl_mst_leave_status status on tegld.leave_status = status.leave_status "
			+ "  where tegld.employee_id =?1	and extract(year from tegld.created_date) =?3 and tegld.leave_status !=?2 and tegld.is_active =?4 "
			+ "			order by status.sequence,tegld.date_of_applied desc ", nativeQuery = true)
	public List<EmployeeGrantLeaveDetail> findAllGrantLeaveDetailsByYear(long employeeId, String leaveStatus, int year,
			String isActive, Pageable pageable);

	@Query(value = "select count(*) from tbl_employee_grant_leave_detail tegld  "
			+ "  join tbl_mst_leave_status status on tegld.leave_status = status.leave_status "
			+ "  where tegld.employee_id =?1	and extract(year from tegld.created_date) =?3 and tegld.leave_status !=?2 and tegld.is_active =?4 ", nativeQuery = true)
	public int countByGrantLeaveDetailsByYear(long employeeId, String leaveStatus, int year, String isActive);

	@Query(value="select * from tbl_employee_grant_leave_detail grantleave where grantleave.id =?1 and grantleave.org_id =?2",nativeQuery=true)
	public EmployeeGrantLeaveDetail findByIdAndOrgId(long id, Long orgId);
	
	@Query(value = "SELECT * from tbl_employee_grant_leave_detail "
			+ " WHERE from_date <= ?1 AND to_date >= ?2 AND employee_id = ?3 AND leave_status != ?4 AND leave_status != ?5  and org_id=?6", nativeQuery = true)
	public EmployeeGrantLeaveDetail findOverlappingLeaveByOrgId(Date fromDate, Date toDate, Long empId,
			String leavestatusCancelled, String leavestatusReject, Long orgId);
	

	@Query(value = "SELECT * from tbl_employee_grant_leave_detail "
			+ "where id=?1 and org_id=?2", nativeQuery = true)
	public EmployeeGrantLeaveDetail findByIdOrgId(Long id,Long orgId);
	
	

}
