package com.vinsys.hrms.traveldesk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.traveldesk.entity.TravelRequestWf;

public interface ITravelRequestWfDAO extends JpaRepository<TravelRequestWf, Long> {
	
	 @Query(value = "SELECT * FROM tbl_trn_travel_wf WHERE travel_request_id = ?1 AND is_active = ?2", nativeQuery = true)
	 TravelRequestWf findTravelRequestIdByIsActive(Long travelRequestId, String isActive);
}
