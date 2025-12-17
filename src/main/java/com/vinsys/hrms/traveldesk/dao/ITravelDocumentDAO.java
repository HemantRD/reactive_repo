package com.vinsys.hrms.traveldesk.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.traveldesk.entity.TravelDocument;

public interface ITravelDocumentDAO extends JpaRepository<TravelDocument,Long> {
	
	TravelDocument findByIdAndIsActive(Long entityId,String isActive);

	List<TravelDocument> findByTravelRequestIdAndTicketRequestIdAndIsActive(Long travelRequestId,Long ticketRequestId,String isActive);
	
	List<TravelDocument> findByTravelRequestIdAndCabRequestIdAndIsActive(Long travelRequestId,Long cabRequestId,String isActive);
	
	List<TravelDocument> findByTravelRequestIdAndAccommodationRequestIdAndIsActive(Long travelRequestId,Long accommodationRequestId,String isActive);
	
	TravelDocument findByTravelRequestIdAndIsActive(Long entityId,String isActive);
	
TravelDocument findByTravelRequestIdAndTicketRequestIdAndIsActiveAndTicketType(Long travelRequestId,Long ticketRequestId,String isActive,String ticketType);
	
	TravelDocument findByTravelRequestIdAndCabRequestIdAndIsActiveAndTicketType(Long travelRequestId,Long cabRequestId,String isActive,String ticketType);
	
	TravelDocument findByTravelRequestIdAndAccommodationRequestIdAndIsActiveAndTicketType(Long travelRequestId,Long accommodationRequestId,String isActive,String ticketType);
}
