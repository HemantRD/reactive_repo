package com.vinsys.hrms.dao.attendance;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedDataKey;

public interface IHRMSAttendanceProcessedDataDAO
		extends JpaRepository<AttendanceProcessedData, AttendanceProcessedDataKey> {

	public AttendanceProcessedData findBycompositePrimaryKey(AttendanceProcessedDataKey compKey);

	// ATTENDANCE REMINDER EMAILS TO EMPLOYEES OTHER THAN DUBAI
	@Query("select attendanceProcessedData from AttendanceProcessedData attendanceProcessedData WHERE "
			+ "attendanceProcessedData.compositePrimaryKey.attendanceDate BETWEEN ?1 AND ?2 "
			+ "AND attendanceProcessedData.status IN ?3 AND attendanceProcessedData.divisionId!=3 AND attendanceProcessedData.compositePrimaryKey.orgId= ?4 ")
	public List<AttendanceProcessedData> findByMonthAndStatus(Date fDate, Date tDate, List<String> status, long orgId);

	// ATTENDANCE REMINDER EMAILS TO DUBAI EMPLOYEES
	@Query("select attendanceProcessedData from AttendanceProcessedData attendanceProcessedData WHERE "
			+ "attendanceProcessedData.compositePrimaryKey.attendanceDate BETWEEN ?1 AND ?2 "
			+ "AND attendanceProcessedData.status IN ?3 AND attendanceProcessedData.divisionId=3 AND attendanceProcessedData.compositePrimaryKey.orgId= ?4 ")
	public List<AttendanceProcessedData> findByMonthAndStatusDubai(Date fDate, Date tDate, List<String> status,
			long orgId);

	// public List<AttendanceProcessedData> findByempId(long empId);

	@Query("select attendanceProcessedData from AttendanceProcessedData attendanceProcessedData WHERE "
			+ "attendanceProcessedData.compositePrimaryKey.attendanceDate BETWEEN ?2 AND ?3 "
			+ "AND attendanceProcessedData.compositePrimaryKey.empId = ?1 AND attendanceProcessedData.compositePrimaryKey.orgId= ?4 ")
	public List<AttendanceProcessedData> findByDateOrgIdempId(long empId, Date parse, Date parse2, long orgId);

	//old query
//	@Query("select attendanceProcessedData from AttendanceProcessedData attendanceProcessedData WHERE "
//			+ "attendanceProcessedData.compositePrimaryKey.attendanceDate BETWEEN ?1 AND ?2 "
//			+ "AND attendanceProcessedData.compositePrimaryKey.orgId= ?3 "
//			+ "order by attendanceProcessedData.empId,attendanceProcessedData.compositePrimaryKey.attendanceDate")
//	public List<AttendanceProcessedData> findByDateOrgId(Date parse, Date parse2, long orgId);
	
	@Query("select attendanceProcessedData from AttendanceProcessedData attendanceProcessedData WHERE "
			+ "attendanceProcessedData.compositePrimaryKey.attendanceDate BETWEEN ?1 AND ?2 "
			+ "AND attendanceProcessedData.compositePrimaryKey.orgId= ?3 "
			+ "order by attendanceProcessedData.compositePrimaryKey.empId,attendanceProcessedData.compositePrimaryKey.attendanceDate")
	public List<AttendanceProcessedData> findByDateOrgId(Date parse, Date parse2, long orgId);

	@Query("select attendanceProcessedData from AttendanceProcessedData attendanceProcessedData WHERE "
			+ "attendanceProcessedData.compositePrimaryKey.attendanceDate BETWEEN ?1 AND ?2 "
			+ "AND attendanceProcessedData.compositePrimaryKey.orgId= ?3 AND attendanceProcessedData.status not IN ?4 "
			+ "order by attendanceProcessedData.compositePrimaryKey.empId,attendanceProcessedData.compositePrimaryKey.attendanceDate")
	public List<AttendanceProcessedData> findByDateOrgIDStatus(Date fDate, Date tDate, long orgId, List<String> status);

	@Query("select attendanceProcessedData from AttendanceProcessedData attendanceProcessedData WHERE "
			+ "attendanceProcessedData.compositePrimaryKey.attendanceDate BETWEEN ?1 AND ?2 "
			+ "AND attendanceProcessedData.compositePrimaryKey.orgId= ?3 AND attendanceProcessedData.status not IN ?4 "
			+ "AND attendanceProcessedData.compositePrimaryKey.empId = ?5 "
			+ "order by attendanceProcessedData.compositePrimaryKey.empId,attendanceProcessedData.compositePrimaryKey.attendanceDate")
	public List<AttendanceProcessedData> findByDateOrgIDStatusEmpId(Date fDate, Date tDate, long orgId,
			List<String> status, long empId);

	@Query("select attendanceProcessedData from AttendanceProcessedData attendanceProcessedData WHERE "
			+ "attendanceProcessedData.compositePrimaryKey.attendanceDate BETWEEN ?2 AND ?3 "
			+ "AND attendanceProcessedData.compositePrimaryKey.empId = ?1 "
			+ "AND attendanceProcessedData.status IN ?4  AND attendanceProcessedData.compositePrimaryKey.orgId= ?5 ")
	public List<AttendanceProcessedData> findByMonthEmpAndStatus(long employeeId, Date parse, Date parse2,
			List<String> status, long orgId);

	@Query("select attendanceProcessedData from AttendanceProcessedData attendanceProcessedData WHERE "
			+ "attendanceProcessedData.compositePrimaryKey.attendanceDate=?2 "
			+ "AND attendanceProcessedData.compositePrimaryKey.empId = ?1")
	public List<AttendanceProcessedData> findByEmpIdAndDate(long empId);

	@Query(value = "SELECT * FROM tbl_attendance_processed_data " + "WHERE emp_id = ?1 "
			+ "AND attendance_date <= CURRENT_DATE " + "ORDER BY attendance_date DESC " + "LIMIT 7", nativeQuery = true)
	List<AttendanceProcessedData> findLatestFiveAttendanceForEmployee(long empId);

	@Query(value = "SELECT * FROM tbl_attendance_processed_data " + "WHERE emp_id = ?1 "
			+ "AND attendance_date BETWEEN ?2 AND ?3", nativeQuery = true)
	List<AttendanceProcessedData> findWeekWiseAttendanceForEmployee(long empId, Date startDate, Date endDate);

	@Query(value = "select * from " + "	tbl_attendance_processed_data " + "where "
			+ "	(attendance_date between '2024-02-01' and '2024-02-29') " + "	and emp_Id =1547 "
			+ "	and org_id=1 ", nativeQuery = true)
	public List<AttendanceProcessedData> findByDateOrgIdempId1(long empId, Date parse, Date parse2, long orgId);

}
