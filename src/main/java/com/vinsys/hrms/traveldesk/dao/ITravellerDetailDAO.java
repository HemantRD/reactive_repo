package com.vinsys.hrms.traveldesk.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.traveldesk.entity.TravellerDetailsV2;

public interface ITravellerDetailDAO extends JpaRepository<TravellerDetailsV2, Long>{
	
	@Query(value = "SELECT * FROM tbl_trn_traveller_details ttd WHERE ttd.cab_request = ?1 AND is_active = ?2", nativeQuery = true)
	public List<TravellerDetailsV2> findByCabRequestAndIsActive(Long id,String IsActive);
	
	@Query(value = "SELECT * FROM tbl_trn_traveller_details ttd WHERE ttd.ticket_request_id = ?1 AND is_active = ?2", nativeQuery = true)
	public List<TravellerDetailsV2> findByTicketRequestAndIsActive(Long ticketRequestId,String IsActive);
	
	@Query(value = "SELECT * FROM tbl_trn_traveller_details ttd WHERE ttd.accommodation_request = ?1 AND is_active = ?2", nativeQuery = true)
	public List<TravellerDetailsV2> findByAccommodationRequestAndIsActive(Long accommodationRequestId,String IsActive);

    @Query(value = "SELECT * FROM tbl_trn_traveller_details WHERE id = ?1 AND is_active = ?2", nativeQuery = true)
	TravellerDetailsV2 findIdByIsActive(long id, String isActive);
    
    @Query(value = "SELECT * FROM tbl_trn_traveller_details ttd WHERE ttd.ticket_request_id = ?1 AND is_active = ?2 AND ttd.id=?3", nativeQuery = true)
    TravellerDetailsV2 findByTicketRequestAndIsActiveAndId(Long ticketRequestId,String IsActive,Long id);
    
    
    @Query(value = "SELECT * FROM tbl_trn_traveller_details ttd WHERE ttd.cab_request = ?1 AND is_active = ?2 AND ttd.id=?3", nativeQuery = true)
    TravellerDetailsV2 findByCabRequestAndIsActiveAndId(Long cabRequestId,String IsActive,Long id);
    
    
    @Query(value = "SELECT * FROM tbl_trn_traveller_details ttd WHERE ttd.accommodation_request = ?1 AND is_active = ?2 AND ttd.id=?3", nativeQuery = true)
    TravellerDetailsV2 findByAccomadationRequestAndIsActiveAndId(Long accommodation,String IsActive,Long id);
    
    

}
