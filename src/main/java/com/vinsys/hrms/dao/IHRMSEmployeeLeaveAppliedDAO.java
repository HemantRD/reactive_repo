package com.vinsys.hrms.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.EmployeeLeaveApplied;

public interface IHRMSEmployeeLeaveAppliedDAO extends JpaRepository<EmployeeLeaveApplied, Long> {

	@Query("SELECT empLeaveApplied FROM EmployeeLeaveApplied empLeaveApplied "
			+ " JOIN FETCH empLeaveApplied.masterLeaveType masterLeaveType "
			+ " WHERE empLeaveApplied.employee.id = ?1 ")
	public List<EmployeeLeaveApplied> findAllAppliedLeavesByEmployee(long employeeId);

	@Query("select count(*) from EmployeeLeaveApplied appliedLeave where appliedLeave.employee.id = ?1 and appliedLeave.masterLeaveType.id = ?2 and appliedLeave.leaveStatus = ?3 and  EXTRACT(year FROM appliedLeave.createdDate) = ?4")
	public long findCountOfAppliedLeaveDetails(long employeeId, long leaveTypeId, String status, int year);

	/*
	 * @Query("select leaveApplied from EmployeeLeaveApplied leaveApplied where leaveApplied.employee.id = ?1 and leaveApplied.fromDate >= (CURRENT_DATE - 5) and leaveApplied.leaveStatus != ?2 and leaveApplied.leaveStatus != ?3 and leaveApplied.leaveStatus != ?4"
	 * ) public List<EmployeeLeaveApplied>
	 * findAppliedLeavesOfEmployeeFromLastFiveDays(long empId, String cancelled,
	 * String wdApproved, String rejeted);
	 */
	// previous applied leaves list get using following query to show overlap leaves
	// alert(change date 06-05-2021)

	/*
	 * Existing query
	 * 
	 * @Query("select leaveApplied from EmployeeLeaveApplied leaveApplied where leaveApplied.employee.id = ?1 and leaveApplied.fromDate <= CURRENT_DATE and date_part('year', leaveApplied.fromDate) = date_part('year', CURRENT_DATE) and leaveApplied.leaveStatus != ?2 and leaveApplied.leaveStatus != ?3 and leaveApplied.leaveStatus != ?4"
	 * ) public List<EmployeeLeaveApplied>
	 * findAppliedLeavesOfEmployeeFromLastFiveDays(long empId, String cancelled,
	 * String wdApproved, String rejeted);
	 */
	// New Updated Query
	@Query("select leaveApplied from EmployeeLeaveApplied leaveApplied where leaveApplied.employee.id = ?1 and leaveApplied.fromDate >= leaveApplied.fromDate and date_part('year', leaveApplied.fromDate) = date_part('year', CURRENT_DATE) and leaveApplied.leaveStatus != ?2 and leaveApplied.leaveStatus != ?3 and leaveApplied.leaveStatus != ?4")
	public List<EmployeeLeaveApplied> findAppliedLeavesOfEmployeeFromLastFiveDays(long empId, String cancelled,
			String wdApproved, String rejeted);

	@Query("select leaveApplied from EmployeeLeaveApplied leaveApplied where leaveApplied.employee.id = ?1 and leaveApplied.fromDate >= leaveApplied.fromDate and date_part('year', leaveApplied.fromDate) = date_part('year', CURRENT_DATE) and leaveApplied.leaveStatus != ?2 and leaveApplied.leaveStatus != ?3 and leaveApplied.leaveStatus != ?4 and leaveApplied.orgId=?5")
	public List<EmployeeLeaveApplied> findAppliedLeavesOfEmployeeFromLastFiveDaysAndOrgId(long empId, String cancelled,
			String wdApproved, String rejeted, Long orgId);

	@Query("SELECT leaveApplied FROM EmployeeLeaveApplied leaveApplied WHERE leaveApplied.employee.id = ?1 and leaveApplied.masterLeaveType.id = ?2 and date_part('year', leaveApplied.fromDate) = date_part('year', CURRENT_DATE)")
	public List<EmployeeLeaveApplied> findByemployeeAndmasterLeaveType(long employeeId, long masterLeaveTypeId);

	@Query(value = "SELECT * FROM tbl_employee_leave_applied  WHERE id = ?1 ", nativeQuery = true)
	public EmployeeLeaveApplied findByLeaveId(long leaveAppliedId);

	// next is modified by SSW on 17Dec2018 for : removed page request argument from
	// next method
	// as manager should view all leave requests of his subordinates
	@Query(" SELECT ela FROM EmployeeLeaveApplied ela JOIN FETCH ela.employee emp JOIN FETCH emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN ela.masterLeaveType mlt "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND erm.reporingManager.id = ?3 "
			+ " AND date_part('year', ela.fromDate) = ?6 "
			+ " ORDER BY CASE ela.leaveStatus WHEN ?4 THEN 1 WHEN ?5 THEN 2 ELSE 3 END, " + " ela.fromDate DESC ")
	public List<EmployeeLeaveApplied> findSubordinateLeaveApplied(String candIsActive, String empIsActive,
			long managerId, String leaveStatusSeq1, String leaveStatusSeq2, int year/* , Pageable Pageable */);

	@Query(" SELECT ela FROM EmployeeLeaveApplied ela JOIN FETCH ela.employee emp JOIN FETCH emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN ela.masterLeaveType mlt "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND erm.reporingManager.id = ?3 "
			+ " AND date_part('year', ela.fromDate) = ?6 " + " AND  emp.id= ?7 "
			+ " ORDER BY CASE ela.leaveStatus WHEN ?4 THEN 1 WHEN ?5 THEN 2 ELSE 3 END, " + " ela.fromDate DESC ")
	public List<EmployeeLeaveApplied> findSubordinateLeaveAppliedByEmpId(String candIsActive, String empIsActive,
			long managerId, String leaveStatusSeq1, String leaveStatusSeq2, int year, long empId);

	// Attendance
	@Query("SELECT empLeaveApplied FROM EmployeeLeaveApplied empLeaveApplied "
			+ " JOIN FETCH empLeaveApplied.masterLeaveType masterLeaveType "
			+ " WHERE empLeaveApplied.employee.id = ?1 AND empLeaveApplied.fromDate <= ?2 AND empLeaveApplied.toDate >= ?2"
			+ " AND empLeaveApplied.leaveStatus = ?3")
	public List<EmployeeLeaveApplied> findAppliedLeavesForDate(long employeeId, Date toDate, String leaveStatus);

	@Query(value = "  select  le.organization_id as oid ,pd.division_id  as did "
			+ "  from tbl_employee e  join tbl_candidate c on e.candidate_id = c.id "
			+ "  JOIN tbl_candidate_professional_detail pd on pd.candidate_id = c.id "
			+ "  JOIN tbl_login_entity le on c.login_entity_id = le.id "
			+ "  JOIN tbl_employee_leave_applied leaveapp on leaveapp.employee_id=e.id "
			+ "  JOIN tbl_employee_reporting_manager reptmngr on reptmngr.reporting_manager_id=e.id "
			+ "  where (DATE_PART('day', CURRENT_DATE) - DATE_PART('day',leaveapp.date_of_applied)>?1) and (leave_status='PENDING' or leave_status='WD_PENDING') and e.is_active='Y' "
			+ "  group by pd.division_id ,le.organization_id ", nativeQuery = true)
	public List<Object[]> findOrgDivForLeaveReminder(int leavenoofdays);

//	@Query(value = " select count(reptmngr.reporting_manager_id),reptmngr.reporting_manager_id from tbl_employee_leave_applied leaveapp "
//			+ " JOIN tbl_employee_reporting_manager reptmngr on reptmngr.employee_id=leaveapp.employee_id "
//			+ " JOIN tbl_employee e on e.id=leaveapp.employee_id "
//			+ " join tbl_employee reptemp on reptemp.id=reptmngr.reporting_manager_id "
//			+ " JOIN tbl_candidate cand on cand.id=reptemp.candidate_id "
//			+ "where extract('day' from( Now() - leaveapp.date_of_applied))>?1 " +
//			// " where (DATE_PART('day', CURRENT_DATE) -
//			// DATE_PART('day',leaveapp.date_of_applied)>?1)"
//			" and (leave_status=?4 or leave_status=?5) and e.is_active=?2 and reptemp.is_active=?3"
//			+ " group by reptmngr.reporting_manager_id", nativeQuery = true)
//	public List<Object[]> findReportingManagerToseNdMail(int leavenoofdays, String isActive, String isActive1,
//			String LeavePending, String WD_Pending);

//	@Query(value = " select count(reptmngr.reporting_manager_id),reptmngr.reporting_manager_id,leaveapp.employee_id from tbl_employee_leave_applied leaveapp "
//			+ " JOIN tbl_employee_reporting_manager reptmngr on reptmngr.employee_id=leaveapp.employee_id "
//			+ " JOIN tbl_employee e on e.id=leaveapp.employee_id "
//			+ " join tbl_employee reptemp on reptemp.id=reptmngr.reporting_manager_id "
//			+ " JOIN tbl_candidate cand on cand.id=reptemp.candidate_id "
//			+ "where extract('day' from( Now() - leaveapp.date_of_applied))>?1 "
//			+ " and  reptmngr.reporting_manager_id=?6 "
//			+ " and (leave_status=?4 or leave_status=?5) and e.is_active=?2 and reptemp.is_active=?3"
//			+ " group by reptmngr.reporting_manager_id,leaveapp.employee_id", nativeQuery = true)
//	public List<Object[]> findEmployeeAccordingToReportingManagerForLeaveReminder(int leavenoofdays, String isActive,
//			String isActive1, String LeavePending, String WD_Pending, long roid);

	@Query(" select appliedLeave from EmployeeLeaveApplied appliedLeave where appliedLeave.employee.id = ?1 "
			+ " and  EXTRACT(year FROM appliedLeave.createdDate) = ?2 order by appliedLeave.dateOfApplied desc ")
	public List<EmployeeLeaveApplied> getEmployeeAppliedLeaveDetailsYearly(long employeeId, int year);

	@Query("SELECT leaveApplied FROM EmployeeLeaveApplied leaveApplied "
			+ " WHERE leaveApplied.employee.id = ?1 and leaveApplied.masterLeaveType.id = ?2"
			+ " and leaveApplied.leaveStatus != ?3 and leaveApplied.leaveStatus != ?4 and leaveApplied.leaveStatus != ?5")
	public List<EmployeeLeaveApplied> findAllLeavesByemployeeAndmasterLeaveType(long employeeId, long masterLeaveTypeId,
			String cancelled, String wdApproved, String rejeted);

	@Query(" select appliedLeave from EmployeeLeaveApplied appliedLeave where appliedLeave.employee.id = ?1 "
			+ " and  EXTRACT(year FROM appliedLeave.createdDate) = ?2 and appliedLeave.leaveStatus !=?3 order by appliedLeave.leaveStatus desc ")
	public List<EmployeeLeaveApplied> getEmployeeAppliedLeaveDetailsYearly(long employeeId, int year,
			String leaveStatus, Pageable pageable);

	@Query(value = " select count(*) from ( select * from tbl_employee_leave_applied appliedLeave where appliedLeave.employee_id = ?1 "
			+ " and  EXTRACT(year FROM appliedLeave.created_date) = ?2 and appliedLeave.leave_status !=?3 )  a ", nativeQuery = true)
	long getCountEmployeeAppliedLeaveDetailsYearly(long employeeId, int year, String leaveStatus);

	@Query(" SELECT ela FROM EmployeeLeaveApplied ela JOIN FETCH ela.employee emp JOIN FETCH emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN ela.masterLeaveType mlt "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND erm.reporingManager.id = ?3 "
			+ " AND date_part('year', ela.fromDate) = ?6 "
			+ " ORDER BY CASE ela.leaveStatus WHEN ?4 THEN 1 WHEN ?5 THEN 2 ELSE 3 END, " + " ela.fromDate DESC ")
	public List<EmployeeLeaveApplied> findSubordinateLeaveAppliedDetails(String candIsActive, String empIsActive,
			long managerId, String leaveStatusSeq1, String leaveStatusSeq2, int year, Pageable pageable);

	@Query(" SELECT ela FROM EmployeeLeaveApplied ela JOIN FETCH ela.employee emp JOIN FETCH emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN ela.masterLeaveType mlt "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND erm.reporingManager.id = ?3 "
			+ " AND ela.leaveStatus = ?4 or ela.leaveStatus=?5 " + " AND date_part('year', ela.fromDate) = ?6 "
			+ " ORDER BY ela.id DESC ")
	public List<EmployeeLeaveApplied> findSubordinateLeaveAppliedDetailsForManager(String candIsActive,
			String empIsActive, long managerId, String leaveStatusSeq1, String leaveStatusSeq2, int year,
			Pageable pageable);

	@Query("SELECT COUNT(ela) FROM EmployeeLeaveApplied ela JOIN ela.employee emp JOIN emp.candidate cand "
			+ "JOIN emp.employeeReportingManager erm JOIN ela.masterLeaveType mlt "
			+ "WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND erm.reporingManager.id = ?3 "
			+ "AND (ela.leaveStatus = ?4 OR ela.leaveStatus = ?5) " + "AND date_part('year', ela.fromDate) = ?6")
	long countSubordinateLeaveAppliedDetails(String candIsActive, String empIsActive, long managerId,
			String leaveStatusSeq1, String leaveStatusSeq2, int year);

	@Query(value = " select * from tbl_employee_leave_applied appliedLeave  "
			+ " join tbl_mst_leave_status status on appliedLeave.leave_status = status.leave_status "
			+ "	where appliedLeave.employee_id =?1	and extract(year from appliedLeave.created_date) =?2 and appliedLeave.leave_status !=?3 "
			+ "order by status.sequence,appliedLeave.date_of_applied desc ", nativeQuery = true)
	public List<EmployeeLeaveApplied> getAppliedLeaveDetailsYearlyByLeaveStatus(long employeeId, int year,
			String leaveStatus, Pageable pageable);

	@Query(value = "  SELECT * FROM tbl_employee_leave_applied ela JOIN  tbl_employee  emp on ela.employee_id =emp.id  "
			+ "  				JOIN tbl_candidate cand on cand.id =emp.candidate_id  "
			+ "			 JOIN tbl_employee_reporting_manager erm on erm.employee_id =emp.id "
			+ "			 join tbl_mst_leave_status status on ela.leave_status = status.leave_status "
			+ "		     WHERE cand.is_active  =?1 AND emp.is_active  =?1 AND erm.reporting_manager_id  =?2 "
			+ "			 AND ela.leave_status != ?3  AND date_part('year', ela.from_date) = ?4 AND ela.employee_id =?5"
			+ "			 ORDER BY status.sequence,ela.date_of_applied desc ", nativeQuery = true)
	public List<EmployeeLeaveApplied> findSubordinateLeaveAppliedDetailsForManagerOrderByStatusAndEmployee(
			String isActive, long managerId, String leaveStatus, int year, Pageable pageable, long filterEmpId);

	@Query(value = "  SELECT * FROM tbl_employee_leave_applied ela JOIN  tbl_employee  emp on ela.employee_id =emp.id  "
			+ "  				JOIN tbl_candidate cand on cand.id =emp.candidate_id  "
			+ "			 JOIN tbl_employee_reporting_manager erm on erm.employee_id =emp.id "
			+ "			 join tbl_mst_leave_status status on ela.leave_status = status.leave_status "
			+ "		     WHERE cand.is_active  =?1 AND emp.is_active  =?1 AND erm.reporting_manager_id  =?2 "
			+ "			 AND ela.leave_status != ?3  AND date_part('year', ela.from_date) = ?4 "
			+ "			 ORDER BY status.sequence,ela.date_of_applied desc ", nativeQuery = true)
	public List<EmployeeLeaveApplied> findSubordinateLeaveAppliedDetailsForManagerOrderByStatus(String isActive,
			long managerId, String leaveStatus, int year, Pageable pageable);

	@Query(value = "  SELECT count(*) FROM tbl_employee_leave_applied ela JOIN  tbl_employee  emp on ela.employee_id =emp.id  "
			+ "  				JOIN tbl_candidate cand on cand.id =emp.candidate_id  "
			+ "			 JOIN tbl_employee_reporting_manager erm on erm.employee_id =emp.id "
			+ "			 join tbl_mst_leave_status status on ela.leave_status = status.leave_status "
			+ "		     WHERE cand.is_active  =?1 AND emp.is_active  =?1 AND erm.reporting_manager_id  =?2 "
			+ "			 AND ela.leave_status != ?3  AND date_part('year', ela.from_date) = ?4 ", nativeQuery = true)
	public int countOfSubordinateLeaveAppliedDetailsForManagerOrderByStatus(String isActive, long managerId,
			String leaveStatus, int year);

	@Query(value = "  SELECT count(*) FROM tbl_employee_leave_applied ela JOIN  tbl_employee  emp on ela.employee_id =emp.id  "
			+ "  				JOIN tbl_candidate cand on cand.id =emp.candidate_id  "
			+ "			 JOIN tbl_employee_reporting_manager erm on erm.employee_id =emp.id "
			+ "			 join tbl_mst_leave_status status on ela.leave_status = status.leave_status "
			+ "		     WHERE cand.is_active  =?1 AND emp.is_active  =?1 AND erm.reporting_manager_id  =?2 "
			+ "			 AND ela.leave_status != ?3  AND date_part('year', ela.from_date) = ?4  AND ela.employee_id =?5", nativeQuery = true)
	public int countOfSubordinateLeaveAppliedDetailsForManagerOrderByStatusAndEmployee(String isActive, long managerId,
			String leaveStatus, int year, long filterEmpId);

	////// added by akanksha
	@Query(value = " select count(reptmngr.reporting_manager_id),reptmngr.reporting_manager_id from tbl_employee_leave_applied leaveapp "
			+ " JOIN tbl_employee_reporting_manager reptmngr on reptmngr.employee_id=leaveapp.employee_id "
			+ " JOIN tbl_employee e on e.id=leaveapp.employee_id "
			+ " join tbl_employee reptemp on reptemp.id=reptmngr.reporting_manager_id "
			+ " JOIN tbl_candidate cand on cand.id=reptemp.candidate_id "
			+ "where extract('day' from( Now() - leaveapp.date_of_applied))>?1 " +
			// " where (DATE_PART('day', CURRENT_DATE) -
			// DATE_PART('day',leaveapp.date_of_applied)>?1)"
			" and (leave_status=?4 or leave_status=?5) and e.is_active=?2 and reptemp.is_active=?3 and EXTRACT(year FROM leaveapp.created_date) = ?6"
			+ " group by reptmngr.reporting_manager_id", nativeQuery = true)
	public List<Object[]> findReportingManagerToseNdMailYearWise(int leavenoofdays, String isActive, String isActive1,
			String LeavePending, String WD_Pending, int year);

	@Query(value = " select count(reptmngr.reporting_manager_id),reptmngr.reporting_manager_id,leaveapp.employee_id from tbl_employee_leave_applied leaveapp "
			+ " JOIN tbl_employee_reporting_manager reptmngr on reptmngr.employee_id=leaveapp.employee_id "
			+ " JOIN tbl_employee e on e.id=leaveapp.employee_id "
			+ " join tbl_employee reptemp on reptemp.id=reptmngr.reporting_manager_id "
			+ " JOIN tbl_candidate cand on cand.id=reptemp.candidate_id "
			+ "where extract('day' from( Now() - leaveapp.date_of_applied))>?1 "
			+ " and  reptmngr.reporting_manager_id=?6 "
			+ " and (leave_status=?4 or leave_status=?5) and e.is_active=?2 and reptemp.is_active=?3 and EXTRACT(year FROM leaveapp.created_date) = ?7"
			+ " group by reptmngr.reporting_manager_id,leaveapp.employee_id", nativeQuery = true)
	public List<Object[]> findEmployeeAccordingToReportingManagerForLeaveReminderYearWise(int leavenoofdays,
			String isActive, String isActive1, String LeavePending, String WD_Pending, long roid, int year);

	@Query(value = "select *  from tbl_employee_leave_applied leaves join tbl_mst_leave_type leavetype on leavetype.id =leaves.leave_type_id \r\n"
			+ "where leaves.employee_id =?1 and leaves.from_date =?2 and leaves.leave_status =?3", nativeQuery = true)
	public List<EmployeeLeaveApplied> findAppliedLeaveByEmployeeAndFromDateAndLeaveStatus(Long employeeId,
			Date fromDate, String status);

	@Query(value = "select count(*) from tbl_employee_leave_applied appliedLeave where appliedLeave.employee_id  = ?1 and appliedLeave.leave_type_id  = ?2 and appliedLeave.leave_status  = ?3 and "
			+ "extract(year from appliedLeave.created_date) = ?4 and appliedLeave.org_id =?5", nativeQuery = true)
	public long findCountOfAppliedLeaveDetailsByOrgId(long employeeId, long leaveTypeId, String status, int year,
			Long orgId);

	@Query(value = "select * from tbl_employee_leave_applied leave where leave.id =?1 and org_id = ?2", nativeQuery = true)
	public EmployeeLeaveApplied findByIdAndOrgId(long leaveId, Long orgId);

	@Query(value = "SELECT * FROM tbl_employee_leave_applied la "
			+ "WHERE la.from_date BETWEEN ?1 AND ?2 AND la.to_date BETWEEN ?1 AND ?2 AND la.org_id=?3 "
			+ "AND  la.employee_id in(select employee_id  from tbl_employee_reporting_manager term where term.reporting_manager_id =?4 and term.org_id =?3) ", nativeQuery = true)
	List<EmployeeLeaveApplied> findByDateRange(Date fromDate, Date toDate, Long orgId, Long managerId);
	
	@Query(value = "SELECT * FROM tbl_employee_leave_applied la "
			+ "WHERE la.from_date BETWEEN ?1 AND ?2 AND la.to_date BETWEEN ?1 AND ?2 AND la.org_id=?3 and la.leave_status in (?4) ", nativeQuery = true)
	List<EmployeeLeaveApplied> findByDateRangeAndOrgId(Date fromDate, Date toDate, Long orgId, Object[] leaveStatus);

	
	@Query(value = "  SELECT * FROM tbl_employee_leave_applied ela JOIN  tbl_employee  emp on ela.employee_id =emp.id  "
			+ "  				JOIN tbl_candidate cand on cand.id =emp.candidate_id  "
			+ "			 JOIN tbl_employee_reporting_manager erm on erm.employee_id =emp.id "
			+ "			 join tbl_mst_leave_status status on ela.leave_status = status.leave_status "
			+ "		     WHERE cand.is_active  =?1 AND emp.is_active  =?1 "
			+ "			 AND ela.leave_status != ?2  AND date_part('year', ela.from_date) = ?3 AND ela.employee_id =?4"
			+ "			 ORDER BY status.sequence,ela.date_of_applied desc ", nativeQuery = true)
	public List<EmployeeLeaveApplied> findSubordinateLeaveAppliedDetailsForHrOrderByStatusAndEmployee(
			String isActive, String leaveStatus, int year, Pageable pageable, long filterEmpId);
	
	
	@Query(value = "  SELECT count(*) FROM tbl_employee_leave_applied ela JOIN  tbl_employee  emp on ela.employee_id =emp.id  "
			+ "  				JOIN tbl_candidate cand on cand.id =emp.candidate_id  "
			+ "			 JOIN tbl_employee_reporting_manager erm on erm.employee_id =emp.id "
			+ "			 join tbl_mst_leave_status status on ela.leave_status = status.leave_status "
			+ "		     WHERE cand.is_active  =?1 AND emp.is_active  =?1"
			+ "			 AND ela.leave_status != ?2  AND date_part('year', ela.from_date) = ?3  AND ela.employee_id =?4", nativeQuery = true)
	public int countOfSubordinateLeaveAppliedDetailsForHrOrderByStatusAndEmployee(String isActive,
			String leaveStatus, int year, long filterEmpId);
	
	
	
	@Query(value = "  SELECT * FROM tbl_employee_leave_applied ela JOIN  tbl_employee  emp on ela.employee_id =emp.id  "
			+ "  				JOIN tbl_candidate cand on cand.id =emp.candidate_id  "
			+ "			 JOIN tbl_employee_reporting_manager erm on erm.employee_id =emp.id "
			+ "			 join tbl_mst_leave_status status on ela.leave_status = status.leave_status "
			+ "		     WHERE cand.is_active  =?1 AND emp.is_active  =?1 "
			+ "			 AND ela.leave_status != ?2  AND date_part('year', ela.from_date) = ?3 AND ela.org_id = ?4"
			+ "			 ORDER BY status.sequence,ela.date_of_applied desc ", nativeQuery = true)
	public List<EmployeeLeaveApplied> findSubordinateLeaveAppliedDetailsForHrOrderByStatus(String isActive,
			 String leaveStatus, int year,Long orgId, Pageable pageable);
	
	
	@Query(value = "  SELECT count(*) FROM tbl_employee_leave_applied ela JOIN  tbl_employee  emp on ela.employee_id =emp.id  "
			+ "  				JOIN tbl_candidate cand on cand.id =emp.candidate_id  "
			+ "			 JOIN tbl_employee_reporting_manager erm on erm.employee_id =emp.id "
			+ "			 join tbl_mst_leave_status status on ela.leave_status = status.leave_status "
			+ "		     WHERE cand.is_active  =?1 AND emp.is_active  =?1 "
			+ "			 AND ela.leave_status != ?2  AND date_part('year', ela.from_date) = ?3 AND ela.org_id = ?4", nativeQuery = true)
	public int countOfSubordinateLeaveAppliedDetailsForHrOrderByStatus(String isActive, 
			String leaveStatus, int year,Long orgId);
}
