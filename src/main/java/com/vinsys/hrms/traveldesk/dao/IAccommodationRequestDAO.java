package com.vinsys.hrms.traveldesk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.traveldesk.entity.AccommodationRequestV2;

public interface IAccommodationRequestDAO extends JpaRepository<AccommodationRequestV2, Long> {

	@Query(value="select * from tbl_trn_accommodation_request_v2 tmmot where id=?1 and travel_request_id=?2",nativeQuery = true)
	public AccommodationRequestV2 findByIdAndTravelRequestId(Long id, Long reqId);
	
	@Query(value="select * from tbl_trn_accommodation_request_v2 tmmot where travel_request_id=?1",nativeQuery = true)
	public AccommodationRequestV2 findByRequestId(Long reqId);
	AccommodationRequestV2 findByTravelRequestIdAndIsActive(Long id, String name);
	
	 @Query(value = "SELECT * FROM tbl_trn_accommodation_request_v2 WHERE travel_request_id = ?1 AND is_active = ?2", nativeQuery = true)
	 AccommodationRequestV2 findIdByIsActive(long travelRequestId, String isActive);
	 
	 
	 @Query(value = "SELECT * FROM tbl_trn_accommodation_request_v2 WHERE travel_request_id = ?1 AND is_active = ?2 AND id = ?3", nativeQuery = true)
	 AccommodationRequestV2 findIdByIsActive(long travelRequestId, String isActive,Long id);

}
