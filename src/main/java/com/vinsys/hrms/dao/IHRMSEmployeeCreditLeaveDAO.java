package com.vinsys.hrms.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;

public interface IHRMSEmployeeCreditLeaveDAO
		extends JpaRepository<EmployeeCreditLeaveDetail, Long>, JpaSpecificationExecutor<EmployeeCreditLeaveDetail> {

	@Query("select resp from EmployeeCreditLeaveDetail resp where resp.employee.id=?1")
	public List<EmployeeCreditLeaveDetail> findEmployeeCreditLeaveDetailbyId(long employeeId);

	@Query("select employeeLeaveDetail from EmployeeCreditLeaveDetail employeeLeaveDetail where employeeLeaveDetail.employee.id = ?1"
			+ " AND employeeLeaveDetail.masterLeaveType.id = ?2 "
			+ " AND date_part('year', employeeLeaveDetail.fromDate) = date_part('year', CURRENT_DATE)")
	public List<EmployeeCreditLeaveDetail> findEmployeeCreditLeaveDetailByEmployeeIdAndLeaveType(long employeeId,
			long leaveTypeId);

	// public EmployeeCreditLeaveDetail
	// findEmployeeCreditLeaveByLeaveTypeAndYear(long employeeId, int year, long
	// masterLeaveType);

	// public EmployeeCreditLeaveDetail
	// findEmployeeCreditLeaveByLeaveTypeAndYear(long employeeId, int year, long
	// masterLeaveType);

	@Query(" SELECT ecld.fromDate, ecld.toDate, ecld.leaveAvailable, mlt.leaveExpiry, ecld.id "
			+ " FROM EmployeeCreditLeaveDetail ecld JOIN ecld.masterLeaveType mlt "
			+ " WHERE mlt.leaveTypeCode = ?1 AND mlt.isActive = ?2 AND ecld.isActive = ?3 AND ecld.employee.id = ?4 "
			+ " AND ecld.leaveAvailable > 0 ORDER BY ecld.fromDate ")
	public List<Object> findEmployeeCompoffListFromCreditLeave(String leaveTypeCode, String masterLeaveTypeIsActive,
			String creditLeaveIsActive, long employeeId);

	@Query("select employeeLeaveDetail from EmployeeCreditLeaveDetail employeeLeaveDetail where employeeLeaveDetail.employee.id = ?1"
			+ " AND employeeLeaveDetail.masterLeaveType.id = ?2 "
			+ " AND date_part('year', employeeLeaveDetail.fromDate) = date_part('year', CURRENT_DATE)"
			+ "AND employeeLeaveDetail.id != ?3")
	public List<EmployeeCreditLeaveDetail> findEmployeeCreditLeaveDetailByEmployeeIdAndLeaveTypeExceptCreditLeaveId(
			long employeeId, long leaveTypeId, long creditedLeaveId);

	@Query("select employeeLeaveDetail from EmployeeCreditLeaveDetail employeeLeaveDetail where employeeLeaveDetail.employee.id = ?1"
			+ " AND employeeLeaveDetail.masterLeaveType.id = ?2 "
			+ " AND date_part('year', employeeLeaveDetail.fromDate) = date_part('year', CURRENT_DATE)"
			+ "AND employeeLeaveDetail.id != ?3")
	public List<EmployeeCreditLeaveDetail> findEmployeeCreditLeaveDetailByEmployeeIdWithFilter(long employeeId,
			long leaveTypeId, long creditedLeaveId);

	@Query( nativeQuery = true, value = " SELECT ecld.id, ecld.employee_id, ecld.from_date, "
			+ " ecld.from_date + interval '90' day, ecld.no_of_days " 
			+ " FROM tbl_employee_credit_leave_detail ecld " 
			+ " JOIN tbl_employee emp on emp.id = ecld.employee_id " 
			+ " WHERE ecld.leave_type_id = ?1 AND ecld.leave_available > 0 "
			+ " AND (ecld.from_date + interval '90' day) > ?2 AND ecld.is_active = ?3 "
			+ " AND emp.is_active = ?3 ORDER BY ecld.employee_id ")
	public List<Object> getEmployeeCompoffDetailsForYearChange(long leaveTypeId,
			Date dateWhenOldLeavesLapsed, String isActive);
	
	/*@Query("SELECT emp.id, ecld.fromDate, ecld.fromDate + ?2 DAY, "
			+ " ecld.noOfDays "
			+ " FROM EmployeeCreditLeaveDetail ecld JOIN ecld.employee emp "
			+ " WHERE ecld.masterLeaveType.id = ?1 AND ecld.leaveAvailable > 0 "
			+ " AND (ecld.fromDate + ?2 DAY) > ?3 AND ecld.isActive = ?4 AND emp.isActive = ?4 ")
	public List<Object> getEmployeeCompoffDetailsForYearChange(long leaveTypeId, int leaveExpiry,
			Date dateWhenOldLeavesLapsed, String isActive);*/
	/*@Query(value = "SELECT *" + 
			"FROM   tbl_employee_credit_leave_detail employeeLeaveDetail" + 
			"WHERE  employeeLeaveDetail.employee_id = ?1" + 
			"       AND employeeLeaveDetail.leave_type_id = ?2" + 
			"       AND  date_part('year', employeeleavedetail.from_date) = ?3" + 
			"	   AND Date_part('years', employeeleavedetail.to_date) = ?3; ", nativeQuery= true)
	public List<EmployeeCreditLeaveDetail> findEmployeeForCreditedLeaves(long employeeId,long leaveTypeId, int year);*/
	
	//leave type ID commented to show all type leaves on credited leaves page (change date 06-05-2021)
	@Query("select employeeLeaveDetail from EmployeeCreditLeaveDetail employeeLeaveDetail where employeeLeaveDetail.employee.id = ?1"
			/* + " AND employeeLeaveDetail.masterLeaveType.id = ?2 " */
			+ " AND date_part('year', employeeLeaveDetail.fromDate) <= ?2"
			+ " AND date_part('year', employeeLeaveDetail.toDate) >= ?2")
	public List<EmployeeCreditLeaveDetail> findEmployeeForCreditedLeaves(long employeeId,
			/* long leaveTypeId, */ int year);
	
	
	@Query("select employeeLeaveDetail from EmployeeCreditLeaveDetail employeeLeaveDetail where leave_available !=?1 AND leave_type_id=?2 AND is_active=?3 ")
	public List<EmployeeCreditLeaveDetail> findAvailableComp(int leave , int leaveType, String isActive);


	@Query("SELECT ecld FROM EmployeeCreditLeaveDetail ecld " +
		       "WHERE ecld.masterLeaveType.id = ?1 AND ?2 < ecld.toDate " +
		       "ORDER BY ecld.fromDate")
		List<EmployeeCreditLeaveDetail> findEmployeeCreditedLeaveByTypeAndDate(long leaveTypeId, Date lastDayOfPrevYear);
	
	
	
	@Query("select employeeLeaveDetail from EmployeeCreditLeaveDetail employeeLeaveDetail where leave_available !=0 AND leave_type_id=?1 AND is_active=?2 and Extract(year from from_date)=?3 ")
	public List<EmployeeCreditLeaveDetail> findAvailableCompOffs(long leaveType, String isActive,int year);
	
	@Query( nativeQuery = true, value = " SELECT SUM(leave_available) AS total_leave_available\r\n"
			+ "FROM tbl_employee_credit_leave_detail\r\n"
			+ "WHERE employee_id = ?1 \r\n"
			+ "    AND leave_type_id = ?2 \r\n"
			+ "    AND EXTRACT(YEAR FROM from_date) = ?3 and to_date >= CURRENT_DATE")
	public Float findTotalCountOfCompOffsYearly(long empId,
			long leaveTypeId, int year);
	
	@Query(value="select * from tbl_employee_credit_leave_detail tecld "
			+ "join tbl_employee_grant_leave_detail grantLeave on grantleave.employee_id =tecld.employee_id "
			+ "where tecld.employee_id =?1 and tecld.leave_type_id =grantleave.leave_type_id  and grantleave.from_date = tecld.from_date and grantleave.is_active =?4 and grantleave.leave_status !=?2 and extract(year from tecld.created_date) =?3",nativeQuery = true)
	public List<EmployeeCreditLeaveDetail> findCreditedLeaveByEmpIdAndLeaveTypeId(long employeeId, String leaveStatus, int year,
			String isActive);
	
}
