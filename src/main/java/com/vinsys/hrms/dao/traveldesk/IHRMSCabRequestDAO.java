package com.vinsys.hrms.dao.traveldesk;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.CabRequest;

public interface IHRMSCabRequestDAO extends JpaRepository<CabRequest, Long> {

	@Query(" SELECT cabReq FROM CabRequest cabReq WHERE cabReq.id = ?1 AND cabReq.isActive = ?2 ")
	public CabRequest findCabRequestById(long childReqId, String isActive);
	
	@Query("Select cabReq from CabRequest cabReq"
			+ " JOIN fetch cabReq.travelRequest travReq where travReq.bookCab = true and DATE(cabReq.createdDate) >= ?1 and DATE(cabReq.createdDate) <= ?2"
			+ " and cabReq.isActive = ?3 and travReq.isActive = ?3")
	public List<CabRequest> getCabRequestByDateFilter(Date fromDate, Date toDate, String isActive);
}
