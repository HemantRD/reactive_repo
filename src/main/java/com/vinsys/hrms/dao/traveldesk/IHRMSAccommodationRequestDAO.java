package com.vinsys.hrms.dao.traveldesk;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.AccommodationRequest;

public interface IHRMSAccommodationRequestDAO extends JpaRepository<AccommodationRequest, Long> {

	@Query(" SELECT accReq FROM AccommodationRequest accReq WHERE accReq.id = ?1 "
			+ " AND accReq.isActive = ?2 ")
	public AccommodationRequest findAccommodationRequestById(long childReqId, String isActive);
	
	//@Query("Select accomodReq from AccomodationDetails accomodReq"
	//		+ " JOIN fetch accomodReq.baseRequest travReq where travReq.stayRequired = true and ((accomodReq.stayFrom >= ?1 and accomodReq.stayFrom <= ?2) or (accomodReq.stayTo >= ?1 and accomodReq.stayTo <= ?2))")
	@Query("Select accomodReq from AccommodationRequest accomodReq"
			+ " JOIN fetch accomodReq.travelRequest travReq where travReq.bookAccommodation = true and DATE(accomodReq.createdDate) >= ?1 and DATE(accomodReq.createdDate) <= ?2"
			+ " and accomodReq.isActive = ?3 and travReq.isActive = ?3")
	public List<AccommodationRequest> getAccomodationRequestByDateFilter(Date fromDate, Date toDate, String isActive);
}
