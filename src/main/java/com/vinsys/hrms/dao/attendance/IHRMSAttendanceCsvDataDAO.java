package com.vinsys.hrms.dao.attendance;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.vinsys.hrms.entity.attendance.AttendanceCSVData;

public interface IHRMSAttendanceCsvDataDAO extends JpaRepository<AttendanceCSVData, Long> {

	/*
	 * List<AttendanceCSVData> findByOrgId(long orgId);
	 * 
	 * List<AttendanceCSVData> findBySwapTimeBetween(Date parse, Date parse2);
	 * 
	 * List<AttendanceCSVData> findBySwapTimeBetweenAndCardNoAndOrgId(Date parse,
	 * Date parse2, long l, long m);
	 * 
	 * List<AttendanceCSVData>
	 * findBySwapTimeGreaterThanEqualAndSwapTimeLessThanEqualAndCardNoAndOrgId(Date
	 * parse, Date date, long accessCardNo, long orgId);
	 * 
	 * @Query(value = "SELECT * FROM tbl_attendance_csv_data t1 " + "JOIN  " + "( "
	 * + "   SELECT card_no, MAX(swap_date) AS MAXDATE " +
	 * "   FROM tbl_attendance_csv_data " + "   GROUP BY card_no " + ") t2 " +
	 * "ON T1.card_no = t2.card_no " + "AND t1.swap_date < t2.MAXDATE " +
	 * "AND t1.is_processed=?2 " + "AND t1.org_id = ?1 " + "ORDER BY swap_date",
	 * nativeQuery = true) List<AttendanceCSVData> findByOrgIdIsProcessed(long
	 * orgId, boolean b);
	 * 
	 */

	List<AttendanceCSVData> findBySwapDateBetweenAndCardNoAndOrgIdAndIsActiveOrderBySwapTimeDesc(Date parse, Date date,
			long accessCardNo, long orgId, String isActive);

	/* order by swipe date asc */
	List<AttendanceCSVData> findBySwapDateBetweenAndCardNoAndOrgIdAndIsActiveOrderBySwapDateAsc(Date parse, Date date,
			long accessCardNo, long orgId, String isActive);

	List<AttendanceCSVData> findBySwapDateBetweenAndCardNoAndOrgIdAndIsActiveOrderBySwapDateAscSwapTimeAsc(
			Date startDate, Date endDate, long accessCardNo, long orgId, String isActive, Pageable pageable);

	@Transactional
	@Modifying
	@Query("update AttendanceCSVData attendanceCSVData set attendanceCSVData.isProcessed=true where "
			+ "attendanceCSVData.swapDate = ?1 and attendanceCSVData.cardNo = ?2 and attendanceCSVData.orgId= ?3 ")
	void updateStatus(Date attendanceDate, long employeeACN, long orgId);

	int countByCardNoAndSwapDate(long empACN, java.sql.Date date);

	@Query(" SELECT acd FROM AttendanceCSVData acd WHERE acd.isActive = ?1 AND acd.cardNo = ?2 "
			+ " AND acd.swapDate = CURRENT_DATE AND acd.description = ?3 ")
	public List<AttendanceCSVData> getCurrDateEmployeeMobileAttendanceRecords(String isActive, long cardNo,
			String description);

	@Transactional
	@Modifying
	@Query("update AttendanceCSVData attendanceCSVData set attendanceCSVData.isProcessed=false where "
			+ "attendanceCSVData.swapDate > ?1 and attendanceCSVData.orgId= ?2 ")
	void updateStatusForProcessing(Date swipeDate, long orgId);

	@Query(" SELECT max(acd.transactionId) FROM AttendanceCSVData acd ")
	public Long getMaxTransactionId();

	@Query(value = " SELECT count(*) FROM tbl_attendance_csv_data tacd WHERE card_no =?1 AND org_id =?2 AND is_active =?3 AND swap_date BETWEEN (?4) AND (?5) ", nativeQuery = true)
	public long countBySwapDateBetweenAndCardNoAndOrgIdAndIsActive(long accessCardNo, long orgId, String isActive,
			Date startDate, Date endDate);

//	@Query(value = "select * from tbl_attendance_csv_data  where hash_id = ?1", nativeQuery = true)
	public AttendanceCSVData findByHashId(long hashId);

//	@Query(" SELECT max(acd.transactionIdV2) FROM AttendanceCSVData acd ")
//	public Long getMaxTransactionIdV2();

	@Query(value = "select count(*)  from tbl_attendance_csv_data where org_id =?1 and is_active =?2 and swap_date = ?3 and employee_id =?4 ", nativeQuery = true)
	public int getSwipesCountByOrgIdAndDateAndEmpId(Long orgId, String isActive, Date date, Long empId);

	public List<AttendanceCSVData> findBySwapDateBetweenAndOrgIdAndIsActiveAndEmployeeIdOrderBySwapDateAscSwapTimeAsc(Date startDate,
			Date endDate, long orgId, String isActive,Long empId, Pageable pageable);

	public int countBySwapDateBetweenAndOrgIdAndIsActiveAndEmployeeId(Date startDate, Date endDate, long orgId,
			String isActive,Long empId);
	
	@Query(value="select * from tbl_attendance_csv_data tacd join tbl_employee te on te.id =tacd.employee_id  join tbl_candidate tc on te.candidate_id =tc.id "
			+ "join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =tc.id where tacd.employee_id =?1 and tacd.org_id =?2 and tacd.is_active =?3 and tcpd.division_id =?4 "
			+ "AND swap_date BETWEEN (?5) AND (?6) order by tacd.swap_date asc ,tacd.swap_time asc",nativeQuery = true)
	public List<AttendanceCSVData> findSwipeByCustomQuery(Long empId,Long orgId,String isActive,Long divId ,Date startDate, Date endDate,Pageable pageable);

	@Query(value="select count(*) from tbl_attendance_csv_data tacd join tbl_employee te on te.id =tacd.employee_id  join tbl_candidate tc on te.candidate_id =tc.id "
			+ "join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =tc.id where tacd.employee_id =?1 and tacd.org_id =?2 and tacd.is_active =?3 and tcpd.division_id =?4 "
			+ "AND swap_date BETWEEN (?5) AND (?6)",nativeQuery = true)
    public int countByCustomQuery(Long empId,Long orgId,String isActive,Long divId ,Date startDate, Date endDate);
	
	@Query(value="select attendance.* from tbl_attendance_csv_data attendance "
			+ "join tbl_attendance_employee_acn acn on acn.emp_acn =attendance.card_no "
			+ "join tbl_employee te on te.id = acn.employee_id "
			+ "join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "where attendance.org_id = ?2 and attendance.swap_date = ?1 and tcpd.division_id = ?3",nativeQuery = true)
	public List<AttendanceCSVData> findAttendanceListByDateAndOrgId(Date date ,Long orgId,Long divId);
}
