package com.vinsys.hrms.dao.traveldesk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.CancelTravelRequest;

public interface IHRMSCancelTravelRequestDAO extends JpaRepository<CancelTravelRequest, Long>{

	@Query("SELECT cancelReq FROM CancelTravelRequest cancelReq WHERE "
			+ " cancelReq.childId =?1 AND cancelReq.travelRequest.id =?2 AND cancelReq.childType =?3 AND cancelReq.isActive = ?4")
	public CancelTravelRequest findCancelRequestByChildReq(long childId, long parentId, String childType,
			String isActive);
}
