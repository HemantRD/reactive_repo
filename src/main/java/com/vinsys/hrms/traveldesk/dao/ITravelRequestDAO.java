package com.vinsys.hrms.traveldesk.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.traveldesk.entity.TravelRequestV2;

public interface ITravelRequestDAO extends JpaRepository<TravelRequestV2, Long> {

	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id  join tbl_mst_travel_status status on tttw.status = status.travel_status WHERE tbl.requester_id =?1 AND tbl.is_active =?2 AND tttw.status not in (?3) order by status.sequence, tbl.created_date DESC", nativeQuery = true)
	public List<TravelRequestV2> findByRequesterIdAndIsActive(Long empId, String isActive, Object[] status,
			Pageable pageable);

	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id =tttw.travel_request_id WHERE tbl.travel_request_id =?1 AND tbl.is_active =?2 AND tttw.status !=?3", nativeQuery = true)
	public List<TravelRequestV2> findByRequesterIdAndIsActiveAnsStatus(Long empId, String isActive, String status,
			Pageable pageable);

	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl WHERE tbl.travel_request_id =?1 AND tbl.is_active =?2", nativeQuery = true)
	public TravelRequestV2 findByIdAndIsActive(Long reqId, String isActive);

	@Query(value = "SELECT COUNT(*) FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.requester_id =?1 AND tbl.is_active =?2 AND tttw.status not in (?3)", nativeQuery = true)
	public long countByIdAndIsActive(Long empId, String isActive, Object[] status);

	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 WHERE travel_request_id = ?1 AND is_active = ?2", nativeQuery = true)
	TravelRequestV2 findIdByIsActive(long travelRequestId, String isActive);

	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl WHERE tbl.requester_id =?1 AND tbl.is_active =?2 AND tbl.travel_request_id=?3", nativeQuery = true)
	public TravelRequestV2 findEmployeeIdAndTravelRequestIdAndIsActive(Long empId, String isActive, Long reqId);

	/*
	 * @Query(value =
	 * "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id in (?1) AND tbl.is_active =?2 and tttw.status not in (?3) order by tbl.created_date DESC"
	 * , nativeQuery = true) public List<TravelRequestV2>
	 * findDivisionIdAndIsActive(Object[] divId, String isActive, Object[] status,
	 * Pageable pageable);
	 */

	@Query(value = "select * from tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id join tbl_mst_travel_status status on tttw.status =status.travel_status where tbl.division_id in (?1) and tbl.is_active =?2 and tttw.status not in (?3) order by status.sequence ,tbl.created_date desc", nativeQuery = true)
	public List<TravelRequestV2> findDivisionIdAndIsActive(Object[] divId, String isActive, Object[] status,
			Pageable pageable);

//	@Query(value = "SELECT COUNT(*) FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id in (?1) AND tbl.is_active =?2 and tttw.status not in (?3)", nativeQuery = true)
//	public long countDivisionIdIdAndIsActive(Object[] divId, String isActive, Object[] status);

	@Query(value = "SELECT COUNT(*) FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id in (?1) AND tbl.is_active =?2 and tttw.status not in (?3)", nativeQuery = true)
	public long countDivisionIdIdAndIsActive(Object[] divId, String isActive, Object[] status);

	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id =?1 AND tbl.department_id =?2 AND tbl.is_active =?3 and tttw.status not in (?4) order by tbl.created_date", nativeQuery = true)
	public List<TravelRequestV2> findDivisionIdAndDepartmentIdAndIsActive(Long divId, Long depId, String isActive,
			Object[] status, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id =?1 AND tbl.department_id =?2 AND tbl.is_active =?3 and tttw.status not in (?4)", nativeQuery = true)
	public long countDivisionIdAndDepartmentIdAndIsActive(Long divId, Long depId, String isActive, Object[] status);

	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 WHERE travel_request_id = ?1 AND is_active = ?2 AND  requester_id = ?3 ", nativeQuery = true)
	TravelRequestV2 findIdByIsActive(long travelRequestId, String isActive, long requesterId);

	@Query(value = "select 	* from tbl_trn_travel_request_v2 tttrv join tbl_trn_travel_wf tttw on tttrv.travel_request_id =tttw.travel_request_id where tttrv.created_date >= ?1"
			+ "	and tttrv.created_date <= ?2 and tttrv.division_id in (?3) and tttw.status not in('INCOMPLETE','CANCELLED') and tttw.pending_with !='EMPLOYEE'  ", nativeQuery = true)
	public List<TravelRequestV2> getTravelRequestByCreatedDate(Date fromDate, Date toDate, Object[] divIds);

	@Query(value = "SELECT COUNT(*) FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id "
			+ " WHERE tbl.division_id =?1 AND tbl.department_id =?2 AND tbl.is_active =?3 and tttw.status not in (?4) and tbl.approver_id =?5 ", nativeQuery = true)
	public long countDivisionIdAndDeptIdAndIsActiveAndApproverId(Long divId, Long depId, String isActive,
			Object[] status, Long approverId);

	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id =?1 AND tbl.department_id =?2 AND tbl.is_active =?3 and tttw.status not in (?4) and tbl.approver_id =?5 order by tbl.created_date DESC", nativeQuery = true)
	public List<TravelRequestV2> findDivisionIdAndDeptIdAndIsActiveAndApproverId(Long divId, Long depId,
			String isActive, Object[] status, Long approverId, Pageable pageable);

	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 WHERE travel_request_id = ?1 AND is_active = ?2 ", nativeQuery = true)
	TravelRequestV2 findIdByIsActiveAndId(long travelRequestId, String isActive);

//	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id in (?1) AND tbl.is_active =?2 and tttw.status not in (?3) and tbl.department_id =?4 order by tbl.created_date DESC", nativeQuery = true)
//	public List<TravelRequestV2> findDivisionIdAndIsActiveAndDepartmentId(Object[] divId, String isActive, Object[] status,Long departmentId,
//			Pageable pageable);

	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id  join tbl_mst_travel_status status on tttw.status = status.travel_status WHERE tbl.division_id in (?1) AND tbl.is_active =?2 and tttw.status not in (?3) and tbl.department_id =?4 order by status.sequence ,tbl.created_date desc", nativeQuery = true)
	public List<TravelRequestV2> findDivisionIdAndIsActiveAndDepartmentId(Object[] divId, String isActive,
			Object[] status, Long departmentId, Pageable pageable);

//	@Query(value = "SELECT COUNT(*) FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id in (?1) AND tbl.is_active =?2 and tttw.status not in (?3) and tbl.department_id =?4", nativeQuery = true)
//	public long countDivisionIdIdAndIsActiveAndDepartmentId(Object[] divId, String isActive, Object[] status,Long departmentId);

	@Query(value = "SELECT COUNT(*) FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id in (?1) AND tbl.is_active =?2 and tttw.status not in (?3) and tbl.department_id =?4", nativeQuery = true)
	public long countDivisionIdIdAndIsActiveAndDepartmentId(Object[] divId, String isActive, Object[] status,
			Long departmentId);

	@Query(value = "SELECT DISTINCT req.approver_id " + "FROM tbl_trn_travel_request_v2 req "
			+ "JOIN tbl_trn_travel_wf tttw ON req.travel_request_id = tttw.travel_request_id "
			+ "WHERE tttw.pending_with = ?1 " + "AND tttw.status = ?2 ", nativeQuery = true)
	List<Long> findPendingWithTD(String pendingWith, String status);

	@Query(value = "select DISTINCT req.division_id " + "from tbl_trn_travel_request_v2 req "
			+ "join tbl_trn_travel_wf tttw on req.travel_request_id = tttw.travel_request_id "
			+ "	where  tttw.pending_with = ?1 and tttw.status = ?2 ", nativeQuery = true)
	List<Long> findDivisionIdsPendingWithTravelDeskAndCompleted(String pendingWith, String status);

	@Query(value = "SELECT COUNT(*) FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id "
			+ " WHERE tbl.division_id =?1 AND tbl.department_id in (?2) AND tbl.is_active =?3 and tttw.status not in (?4) and tbl.approver_id =?5 ", nativeQuery = true)
	public long countDivisionIdAndDeptIdAndIsActiveAndApproverId(Long divId, Object[] depId, String isActive,
			Object[] status, Long approverId);

//	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id =?1 AND tbl.department_id in (?2) AND tbl.is_active =?3 and tttw.status not in (?4) and tbl.approver_id =?5 order by tbl.created_date DESC", nativeQuery = true)
//	public List<TravelRequestV2> findDivisionIdAndDeptIdAndIsActiveAndApproverId(Long divId, Object[] depId,
//			String isActive, Object[] status, Long approverId, Pageable pageable);
	
	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id join tbl_mst_travel_status status on tttw.status = status.travel_status WHERE tbl.division_id =?1 AND tbl.department_id in (?2) AND tbl.is_active =?3 and tttw.status not in (?4) and tbl.approver_id =?5 order by status.sequence, tbl.created_date DESC", nativeQuery = true)
	public List<TravelRequestV2> findDivisionIdAndDeptIdAndIsActiveAndApproverId(Long divId, Object[] depId,
			String isActive, Object[] status, Long approverId, Pageable pageable);
	
	
	@Query(value = "SELECT COUNT(*) FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id WHERE tbl.division_id =?1 AND tbl.is_active =?2 and tttw.status in (?3)", nativeQuery = true)
	public long countDivisionIdIdAndIsActive(Long divId, String isActive,Object[] status);
	
	
	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id  join tbl_mst_travel_status status on tttw.status = status.travel_status WHERE tbl.division_id =?1 AND tbl.is_active =?2 and tttw.status in (?3) order by status.sequence ,tbl.created_date desc", nativeQuery = true)
	public List<TravelRequestV2> findDivisionIdAndIsActive(Long divId, String isActive,Object[] status, Pageable pageable);
	
	
	@Query(value = "SELECT COUNT(*) FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id "
			+ " WHERE  tbl.department_id in (?1) AND tbl.is_active =?2 and tttw.status not in (?3) and tbl.approver_id =?4 ", nativeQuery = true)
	public long countDeptIdAndIsActiveAndApproverId( Object[] depId, String isActive,
			Object[] status, Long approverId);
	
	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id join tbl_mst_travel_status status on tttw.status = status.travel_status WHERE  tbl.department_id in (?1) AND tbl.is_active =?2 and tttw.status not in (?3) and tbl.approver_id =?4 order by status.sequence, tbl.created_date DESC", nativeQuery = true)
	public List<TravelRequestV2> findDeptIdAndIsActiveAndApproverId( Object[] depId,
			String isActive, Object[] status, Long approverId, Pageable pageable);
	
	@Query(value = "select * from tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id join tbl_mst_travel_status status on tttw.status =status.travel_status where tbl.division_id in (?1) and tbl.is_active =?2 and tttw.status not in (?3) and tbl.branch_id in (?4) order by status.sequence ,tbl.created_date desc", nativeQuery = true)
	public List<TravelRequestV2> findDivisionIdAndIsActiveAndBranchId(Object[] divId, String isActive, Object[] status,Object[] branchId,
			Pageable pageable);
	
	@Query(value = "SELECT * FROM tbl_trn_travel_request_v2 tbl join tbl_trn_travel_wf tttw on tbl.travel_request_id = tttw.travel_request_id  join tbl_mst_travel_status status on tttw.status = status.travel_status WHERE tbl.division_id in (?1) AND tbl.is_active =?2 and tttw.status not in (?3) and tbl.department_id =?4 and tbl.branch_id in (?5) order by status.sequence ,tbl.created_date desc", nativeQuery = true)
	public List<TravelRequestV2> findDivisionIdAndIsActiveAndDepartmentIdAndBranchId(Object[] divId, String isActive,
			Object[] status, Long departmentId, Object[] branchId, Pageable pageable);

	@Query(value = "select * from tbl_trn_traveller_details traveler"
			+ " join tbl_trn_travel_request_v2 request on traveler.ticket_request_id = request.travel_request_id"
			+ " join tbl_trn_travel_wf workflow on request.travel_request_id = workflow.travel_request_id"
			+ " join tbl_mst_travel_status status on workflow.status = status.travel_status"
			+ " where"
			+ " traveler.is_active = ?2 and request.is_active = ?2 and request.division_id in (?1) and"
			+ " traveler.name ILIKE concat(?3,'%')"
			+ " order by status.sequence ,workflow.created_date desc", nativeQuery = true)
	public List<TravelRequestV2> findDivisionIdAndIsActiveAndTravelerName(Object[] divId, String isActive,
			  String travelerName, Pageable pageable);
	
}