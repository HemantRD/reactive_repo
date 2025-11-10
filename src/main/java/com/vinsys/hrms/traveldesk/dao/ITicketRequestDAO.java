package com.vinsys.hrms.traveldesk.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.traveldesk.entity.TicketRequestV2;

public interface ITicketRequestDAO extends JpaRepository<TicketRequestV2, Long> {
	@Query(value = "select * from tbl_trn_ticket_request_v2 tmmot where id=?1", nativeQuery = true)
	public TicketRequestV2 findByTicketId(Long id);

	@Query(value = "select * from tbl_trn_ticket_request_v2 tmmot join tbl_trn_traveller_details td on tmmot.id =td.ticket_request_id "
			+ " join tbl_mst_mode_of_travel mt on mt.id=tmmot.mode_of_travel "
			+ "  where  tmmot.travel_request_id=?1 and tmmot.is_active=?2 ",nativeQuery = true)
	public List<TicketRequestV2> findByTravelRequestIdAndIsActive(Long id, String name);

	@Query(value = "select * from tbl_trn_ticket_request_v2 tmmot where id=?1 and travel_request_id=?2", nativeQuery = true)
	public TicketRequestV2 findByIdAndTravelRequestId(Long id, Long reqId);
	
	
	 @Query(value = "SELECT * FROM tbl_trn_ticket_request_v2 WHERE travel_request_id = ?1 AND is_active = ?2", nativeQuery = true)
	 List<TicketRequestV2> findIdByIsActive(long travelRequestId, String isActive);
	 
	 @Query(value = "SELECT * FROM tbl_trn_ticket_request_v2 WHERE travel_request_id = ?1 AND is_active = ?2 AND id = ?3", nativeQuery = true)
	 TicketRequestV2 findIdByIsActive(long travelRequestId, String isActive, Long id);

	
}
