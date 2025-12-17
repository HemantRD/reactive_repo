package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.CabRecurringRequest;

public interface IHRMSCabRecurringRequestDAO extends JpaRepository<CabRecurringRequest, Long> {

	@Query(" SELECT cabRecReq FROM CabRecurringRequest cabRecReq WHERE cabRecReq.id = ?1 "
			+ " AND cabRecReq.isActive = ?2 ")
	public CabRecurringRequest findCabRecurringRequestById(long childReqId, String isActive);
	
	@Query(" SELECT cabRecReq FROM CabRecurringRequest cabRecReq WHERE cabRequestPassenger.id = ?1 "
			+ " AND cabRecReq.isActive = ?2 ")
	public List<CabRecurringRequest> findCabRecurringRequestByPassenger(long passengerId, String isActive);
	
	
	@Query(" SELECT cabRecReq FROM CabRecurringRequest cabRecReq WHERE cabRecReq.id = ?1 "
			+ " AND cabRecReq.returnTripStatus = ?2 "
			+ " AND cabRecReq.isActive = ?3 "
			//+ " AND DATE(cabRecReq.pickupDate) >= (current_date - 1) "
			+ " AND DATE(cabRecReq.pickupDate) <=  (current_date + 10) ")
	public List<CabRecurringRequest> findCabRcrReqByReturnTripStatusRowId(long cabRecurringRowId,String status,String isActive);
	
	@Query(" SELECT cabRecReq FROM CabRecurringRequest cabRecReq WHERE cabRecReq.id = ?1 "
			+ " AND cabRecReq.oneWayTripStatus = ?2 "
			+ " AND cabRecReq.isActive = ?3 "
			//+ " AND DATE(cabRecReq.pickupDate) >= (current_date - 1) "
			+ " AND DATE(cabRecReq.pickupDate)  <=  (current_date + 10) ")
	public List<CabRecurringRequest> findCabRcrReqByOneWayRowId(long cabRecurringRowId,String status,String isActive);
	
}