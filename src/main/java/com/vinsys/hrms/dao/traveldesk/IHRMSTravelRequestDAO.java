package com.vinsys.hrms.dao.traveldesk;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.TravelRequest;

public interface IHRMSTravelRequestDAO extends JpaRepository<TravelRequest, Long> {

	@Query(" SELECT tr FROM TravelRequest tr LEFT JOIN FETCH tr.accommodationRequest ar "
			+ " LEFT JOIN FETCH tr.cabRequest cr LEFT JOIN FETCH tr.ticketRequest tickReq WHERE tr.id = ?1 AND tr.isActive = ?2 ")
	public TravelRequest findTravelRequestById(long travelRequestId, String isActive);

	@Query(" SELECT tr FROM TravelRequest tr WHERE tr.employeeId.id = ?1 AND tr.isActive = ?5 AND tr.organization.id = ?6 "
			+ " ORDER BY CASE tr.travelStatus WHEN ?2 THEN 1 WHEN ?3 THEN 2 WHEN ?4 THEN 3  ELSE 4 END, tr.createdDate DESC")
	public List<TravelRequest> findTravelRequestByEmployeeId(long employeeId, String TR_Pending, String TR_WIP,
			String TR_Rejected, String isActive, long organizationId/*, Pageable pageable*/);

	@Query(" SELECT tr from TravelRequest tr WHERE tr.isActive = ?1 AND tr.organization.id = ?5 "
			+ " ORDER BY CASE tr.travelStatus WHEN ?2 THEN 1 WHEN ?3 THEN 2 WHEN ?4 THEN 3  ELSE 4 END, tr.createdDate DESC")
	public List<TravelRequest> findAllTravelRequest(String isActive, String TR_Pending, String TR_WIP,
			String TR_Rejected, long organizationId/*, Pageable pageable*/);

	@Query(" SELECT MAX(tr.seqId) FROM TravelRequest tr WHERE tr.organization.id = ?1 ")
	public long getTravelRequestSeqOrgwise(long orgId);

	@Query(" SELECT COUNT(*) FROM TravelRequest tr WHERE tr.organization.id = ?1 ")
	public long getCountTravelRequestOrgwise(long orgId);

	@Query(" SELECT tr FROM TravelRequest tr WHERE tr.id = ?1 AND tr.isActive = ?2")
	public TravelRequest findByRequestId(long travelRequestId, String isActive);
	
	/*@Query(" SELECT tr FROM CancelTravelRequest cancelTR JOIN  cancelTR.travelRequest tr WHERE "
			+ " tr.organization.id = ?1 AND tr.isActive = ?2 AND cancelTR.isActive = ?2")
	public List<TravelRequest> findAllCancelledTravelRequest(long orgId, String isActive, Pageable pageable);
	*/
	@Query(" SELECT tr FROM TravelRequest tr WHERE tr.id in (select ctr.travelRequest.id from CancelTravelRequest ctr WHERE ctr.isActive = ?2) "
			+ " AND tr.organization.id = ?1 AND tr.isActive = ?2 ")
	public List<TravelRequest> findAllCancelledTravelRequest(long orgId, String isActive, Pageable pageable);
	
	
	
	@Query(" SELECT tr FROM CancelTravelRequest cancelTR JOIN  cancelTR.travelRequest tr WHERE "
			+ "tr.employeeId.id = ?1 AND tr.organization.id = ?2 AND tr.isActive = ?3 AND cancelTR.isActive = ?3 GROUP BY tr.id")
	public List<TravelRequest> findMyCancelledTravelRequest(long employeeId , long orgId, String isActive, Pageable pageable);
	
	/*@Query("SELECT travelReq FROM TravelRequest travelReq "
			+ " join fetch travelReq.ticketRequest tickReq"
			//+ " join fetch travelReq.traveldeskComment comm"
			+ " where tickReq.isActive =?1 and tickReq.approvalRequired = ?2"
			+ " and tickReq.approverId.id = ?3 and tickReq.approverStatus = ?4 "
			+ " and travelReq.traveldeskComment.childId = tickReq.id and travelReq.traveldeskComment.childType = ?5" )*/
	@Query("SELECT travelReq FROM TravelRequest travelReq "
			+ " join fetch travelReq.ticketRequest tickReq"
			+ " where tickReq.isActive =?1 and tickReq.approvalRequired = ?2"
			+ " and tickReq.masterApprover.employee.id = ?3 and tickReq.approverStatus = ?4 ")
	public List<TravelRequest> getPendingTicketRequetsWithCommentsForApprover(String isActive, boolean approvalRequired , long approverId, String status);
	
	@Query("SELECT travelReq FROM TravelRequest travelReq "
			+ " join fetch travelReq.accommodationRequest accoReq"
			+ " where accoReq.isActive =?1 and accoReq.approvalRequired = ?2"
			+ " and accoReq.masterApprover.employee.id = ?3 and accoReq.approverStatus = ?4 ")
	public List<TravelRequest> getPendingAccommodationRequetsWithCommentsForApprover(String isActive, boolean approvalRequired , long approverId, String status);
	
	@Query("SELECT travelReq FROM TravelRequest travelReq "
			+ " join fetch travelReq.cabRequest cabReq"
			+ " where cabReq.isActive =?1 and cabReq.approvalRequired = ?2"
			+ " and cabReq.masterApprover.employee.id = ?3 and cabReq.approverStatus = ?4 ")
	public List<TravelRequest> getPendingCabRequetsWithCommentsForApprover(String isActive, boolean approvalRequired , long approverId, String status);

	
	@Query("SELECT travelRequest from TravelRequest travelRequest "
			+ "JOIN FETCH travelRequest.ticketRequest ticketRequest WHERE "
			+ "DATE( ticketRequest.preferredDate)  >= current_date "
			+ "AND DATE( ticketRequest.preferredDate) <= ?2 "
			+ "AND travelRequest.isActive = ?1 "
			+ "AND (travelRequest.travelStatus = ?3  OR travelRequest.travelStatus = ?4) "
			+ "AND ticketRequest.isActive = ?1 "
			+ "AND ( ticketRequest.ticketRequestStatus=?3 OR ticketRequest.ticketRequestStatus=?4 ) "
			+ "AND travelRequest.organization.id = ?5")
	public List<TravelRequest> getUpcomingTicketRequest(String isActive,Date date,String pending,String wip,long organizationId);
	
	@Query("SELECT travelRequest from TravelRequest travelRequest "
			+ "JOIN FETCH travelRequest.cabRequest cabRequest "
			+ "JOIN FETCH cabRequest.cabRequestPassengers cabRequestPassengers "
			+ "JOIN FETCH cabRequestPassengers.cabRecurringRequests cabRecurringRequests "
			+ "WHERE "
			+ "( ( DATE(cabRequestPassengers.pickupDate)  >=  current_date AND DATE( cabRequestPassengers.pickupDate)  <=  ?2 "
			+ "AND ( cabRequestPassengers.oneWayTripStatus =  ?3 OR cabRequestPassengers.oneWayTripStatus =?4 ) ) "
			
			+ "OR ( DATE(cabRequestPassengers.returnDate)  >=  current_date AND DATE( cabRequestPassengers.returnDate) <=  ?2 "
			+ "AND ( cabRequestPassengers.returnTripStatus =  ?3 OR cabRequestPassengers.returnTripStatus =?4 ) ) "
			
			+ "OR ( DATE( cabRecurringRequests.pickupDate)  >=  current_date AND DATE( cabRecurringRequests.pickupDate)  <=  ?2 "
			+ "AND ( cabRecurringRequests.oneWayTripStatus =  ?3 OR cabRequestPassengers.oneWayTripStatus =?4 ) ) "
			
			+ "OR ( DATE( cabRecurringRequests.returnDate)  >=  current_date AND DATE( cabRecurringRequests.returnDate)  <=  ?2 "
			+ "AND ( cabRecurringRequests.returnTripStatus =  ?3 OR cabRecurringRequests.returnTripStatus =?4 ) ) )"
			
			+ "AND travelRequest.isActive = ?1 "
			+ "AND ( travelRequest.travelStatus = ?3  OR travelRequest.travelStatus = ?4  ) "
			+ "AND cabRequest.isActive = ?1 "
			//+ "AND (cabRequestPassengers.cabRequestPasssengerStatus =?3 OR cabRequestPassengers.cabRequestPasssengerStatus =?4 ) "
			+ "AND  travelRequest.organization.id = ?5")
	public List<TravelRequest> getUpcomingCabRequest(String isActive,Date date,String pending,String wip,long organizationId);
	
	
	@Query("SELECT travelRequest from TravelRequest travelRequest "
			+ "JOIN FETCH travelRequest.accommodationRequest accommodationRequest WHERE "
			+ "DATE( accommodationRequest.fromDate)  >=  current_date "
			+ "AND DATE( accommodationRequest.fromDate)  <=  ?2 "
			+ "AND travelRequest.isActive = ?1 "
			+ "AND ( travelRequest.travelStatus = ?3  OR travelRequest.travelStatus = ?4 ) "
			+ "AND accommodationRequest.isActive = ?1 "
			+ "AND (accommodationRequest.accommodationRequestStatus = ?3  OR accommodationRequest.accommodationRequestStatus = ?4 ) "
			+ "AND travelRequest.organization.id = ?5")
	public List<TravelRequest> getUpcomingAccommodation(String isActive,Date date,String pending,String wip,long organizationId);
	
}
