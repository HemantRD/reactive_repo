package com.vinsys.hrms.dao.attendance;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.attendance.AttendanceSummary;

public interface IHRMSAttendanceSummaryDAO extends JpaRepository<AttendanceSummary, String> {
/*	
	@Query("select attendanceSummary from AttendanceSummary attendanceSummary WHERE "
			+ "attendanceSummary.attendanceDate BETWEEN ?2 AND ?3 "
			+ "AND attendanceSummary.empId = ?1 ")
	*/
	@Query("select attendanceSummary.empId AS empId,attendanceSummary.employeeAcn  AS employeeAcn,attendanceSummary.empName as empName,sum(attendanceSummary.present) as present,sum(attendanceSummary.absent) AS absent,"
			+ "sum(attendanceSummary.weeklyoff) as weeklyoff,sum(attendanceSummary.halfday) as halfday,sum(attendanceSummary.holiday) as holiday,attendanceSummary.designation,attendanceSummary.department, " 
			+ " attendanceSummary.branch, attendanceSummary.division, attendanceSummary.orgName,sum(attendanceSummary.overTime),sum(attendanceSummary.lopCount) as lopCount from AttendanceSummary attendanceSummary WHERE "
			+ "attendanceSummary.attendanceDate BETWEEN ?2 AND ?3 AND attendanceSummary.empId = ?1 and attendanceSummary.orgId= ?4 "
			+ "group by attendanceSummary.empId,attendanceSummary.employeeAcn,attendanceSummary.empName,attendanceSummary.designation,attendanceSummary.department," 
			+ " attendanceSummary.division, attendanceSummary.orgName, attendanceSummary.branch order by attendanceSummary.empId")
	List<Object[]> findByempIdDate(long employeeId, Date parse, Date parse2,long orgId);

	
	/*@Query("select attendanceSummary.empId AS empId,attendanceSummary.employeeAcn  AS employeeAcn,attendanceSummary.empName as empName,sum(attendanceSummary.present) as present,sum(attendanceSummary.absent) AS absent,"
			+ "sum(attendanceSummary.weeklyoff) as weeklyoff,sum(attendanceSummary.halfday) as halfday,sum(attendanceSummary.holiday) as holiday,attendanceSummary.designation,attendanceSummary.department,"
			+ " attendanceSummary.branch,attendanceSummary.division, attendanceSummary.orgName,sum(attendanceSummary.overTime),sum(attendanceSummary.lopCount) as lopCount from AttendanceSummary attendanceSummary WHERE "
				+ "attendanceSummary.attendanceDate BETWEEN ?1 AND ?2  and attendanceSummary.orgId= ?3 "
			+ "group by attendanceSummary.empId,attendanceSummary.employeeAcn,attendanceSummary.empName,attendanceSummary.designation,attendanceSummary.department," 
			+ " attendanceSummary.division, attendanceSummary.orgName, attendanceSummary.branch order by attendanceSummary.empId")
	List<Object[]> findByDate(Date parse, Date parse2, long orgId);
	*/
	// To find unique records in attendance report
	@Query(value = "SELECT emp.id AS empId," + "		empacn.emp_acn," + "		s.actual_relieving_date,"
			+ "		cand.first_name," + "       cand.last_name," + "       dept.department_name,"
			+ "       desg.designation_name," + "       branch.branch_name," + "       division.division_name,"
			+ "       org.ORG_NAME," + "       Sum(attendancesummary.present)   AS present,"
			+ "       Sum(attendancesummary.absent)    AS absent,"
			+ "       Sum(attendancesummary.weeklyoff) AS weeklyoff,"
			+ "       Sum(attendancesummary.halfday)   AS halfday,"
			+ "       Sum(attendancesummary.holiday)   AS holiday," + "       Sum(attendancesummary.over_time),"
			+ "       Sum(attendancesummary.lop_count)  AS lopCount" + " FROM   tbl_employee emp"
			+ "		LEFT JOIN vw_employee_attendance_summary attendanceSummary  on attendanceSummary.emp_id = emp.id"
			+ "        AND  attendancesummary.attendance_date BETWEEN ?1 AND ?2"
			+ "       AND attendancesummary.org_id = ?3"
			+ "	    join tbl_candidate cand on emp.candidate_id = cand.id"
			+ "       join tbl_candidate_professional_detail cpro on cand.id=cpro.candidate_id"
			+ "       join tbl_mst_designation desg on  cpro.designation_id=desg.id"
			+ "       join tbl_mst_department dept on cpro.department_id=dept.id"
			+ " 		join tbl_attendance_employee_acn empacn on empacn.employee_id=emp.id"
			+ "		join tbl_mst_branch branch on cpro.branch_id=branch.id"
			+ " 		join tbl_mst_division division on cpro.division_id =division.id"
			+ "		join tbl_login_entity le on cand.login_entity_id=le.id"
			+ "		join tbl_organization org on le.organization_id=org.id"
			+ "		LEFT Join tbl_trn_employee_separation_details s on s.employee_id=emp.id"
			+ "   		AND s.is_active = 'Y' "
			// + " -- AND s.actual_relieving_date >= '2020-01-26' " +
			+ " WHERE emp.is_active='Y' AND (s.actual_relieving_date >= ?1 OR s.actual_relieving_date is NULL) "
			+ " GROUP  BY emp.id," + "		cand.first_name," + "       cand.last_name,"
			+ "       dept.department_name," + "       desg.designation_name,"
			+ "		s.actual_relieving_date,empacn.emp_acn,branch.branch_name,division.division_name,org.ORG_NAME"
			+ " ORDER  BY emp.id ", nativeQuery = true)
	List<Object[]> findByDate(Date parse, Date parse2, long orgId);

	}
