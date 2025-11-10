package com.vinsys.hrms.dao.attendance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.attendance.AttendanceCSVDataSummary;

public interface IHRMSAttendanceCSVDataSummaryDAO extends JpaRepository<AttendanceCSVDataSummary, String> {

	// List<AttendanceCSVDataSummary> findByOrgId(long orgId);

	@Query(value = "SELECT row_number() OVER (ORDER BY t1.swap_date) AS id,t1.card_no,t1.org_id, t1.swap_date attendance_date,\r\n"
			+ "min(t1.swap_time) start_time,MAX(t1.swap_time) end_time,\r\n"
			+ "DATE_PART('hour',(MAX(swap_time) - min(swap_time))) hours,\r\n"
			+ "DATE_PART('minute',(MAX(swap_time) - min(swap_time))) minutes,\r\n" + "t1.employee_id\r\n"
			+ "FROM tbl_attendance_csv_data t1\r\n" + "JOIN \r\n" + "(\r\n"
			+ "   SELECT card_no, MAX(swap_date) AS MAXDATE\r\n"
			+ "   FROM tbl_attendance_csv_data where org_id= ?1  \r\n" + "   GROUP BY card_no\r\n" + ") t2\r\n"
			+ "ON T1.card_no = t2.card_no\r\n" + "AND t1.swap_date < current_date \r\n" + "AND t1.is_processed= ?2 \r\n"
			+ "AND t1.org_id = ?1 \r\n" + "group by t1.card_no, t1.swap_date,t1.org_id,t1.employee_id\r\n"
			+ "order by t1.swap_date;", nativeQuery = true)
	List<AttendanceCSVDataSummary> findByOrgIdIsProcessedIsActive(long orgId, boolean b);
	
	@Query(value = "select row_number() over (order by	t1.swap_date) as id,t1.card_no,t1.org_id,t1.swap_date attendance_date,\r\n"
			+ "   min(t1.swap_time) start_time,\r\n"
			+ "	MAX(t1.swap_time) end_time,\r\n"
			+ "	DATE_PART('hour',(MAX(swap_time) - min(swap_time))) hours,DATE_PART('minute',(MAX(swap_time) - min(swap_time))) minutes,\r\n"
			+ "	t1.employee_id\r\n"
			+ " from tbl_attendance_csv_data t1 where\r\n"
			+ "	t1.swap_date < current_date\r\n"
			+ "	and t1.is_processed =?2\r\n"
			+ "	and t1.org_id =?1"
			+ " group by t1.card_no,t1.swap_date,t1.org_id,	t1.employee_id\r\n"
			+ " order by t1.swap_date", nativeQuery = true)
	List<AttendanceCSVDataSummary> findByOrgIdIsProcessedIsActiveAndNoACN(long orgId, boolean b);
	
	

	// and is_active= ?3 removed from inner query
}
