package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.vinsys.hrms.entity.traveldesk.TicketRequestPassenger;

public interface IHRMSTicketRequestPassengerDAO extends JpaRepository<TicketRequestPassenger, Long> {

	@Query("SELECT ticketPassenger from TicketRequestPassenger ticketPassenger "
			+ " WHERE ticketPassenger.ticketRequest.id = ?1 and ticketPassenger.isActive = ?2 and ticketPassenger.ticketRequest.isActive = ?2")
	public List<TicketRequestPassenger> findByTicketRequestId(long ticketRequestId,String isActive);
	
	@Query("SELECT passenger from TicketRequestPassenger passenger WHERE passenger.ticketRequest.id = ?1 AND passenger.isActive = ?2")
	public List<TicketRequestPassenger> findTicketPassengerByTicketId(long ticketReqId, String isActive);

	@Transactional
	@Modifying
	@Query("UPDATE TicketRequestPassenger passenger set passenger.isActive='N' , passenger.updatedDate = NOW() WHERE passenger.id in ?1")
	public void updatePassengerIdsToInActive(List<Long> dbGuestIds);
}
