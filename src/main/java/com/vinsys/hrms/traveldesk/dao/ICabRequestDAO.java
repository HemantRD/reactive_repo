package com.vinsys.hrms.traveldesk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.traveldesk.entity.CabRequestV2;

public interface ICabRequestDAO extends JpaRepository<CabRequestV2, Long> {
	
	@Query(value="select * from tbl_trn_cab_request_v2 tmmot where id=?1 and travel_request_id=?2",nativeQuery = true)
	public CabRequestV2 findByIdAndTravelRequestId(Long id, Long reqId);
	

	@Query(value="select * from tbl_trn_cab_request_v2 tmmot where travel_request_id=?1",nativeQuery = true)
	public CabRequestV2 findByRequestId(Long reqId);
	
	
	CabRequestV2 findByTravelRequestIdAndIsActive(Long travelRequestId, String isActive);
	
	
	 @Query(value = "SELECT * FROM tbl_trn_cab_request_v2 WHERE travel_request_id = ?1 AND is_active = ?2", nativeQuery = true)
	 CabRequestV2 findIdByIsActive(long travelRequestId, String isActive);
	 
	 @Query(value = "SELECT * FROM tbl_trn_cab_request_v2 WHERE travel_request_id = ?1 AND is_active = ?2 AND id = ?3", nativeQuery = true)
	 CabRequestV2 findIdByIsActive(long travelRequestId, String isActive,Long id);
}
