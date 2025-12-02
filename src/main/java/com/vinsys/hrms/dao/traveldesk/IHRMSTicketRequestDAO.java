package com.vinsys.hrms.dao.traveldesk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.TicketRequest;

public interface IHRMSTicketRequestDAO extends JpaRepository<TicketRequest, Long> {

	@Query(" SELECT tickReq FROM TicketRequest tickReq WHERE tickReq.id = ?1 AND tickReq.isActive = ?2")
	public TicketRequest findTicketRequestById(long childReqId, String isActive);

	@Query(" SELECT tickReq FROM TicketRequest tickReq " + " join fetch tickReq.travelRequest"
			+ " WHERE tickReq.isActive = ?1 and tickReq.approvalRequired = ?2 "
			+ " and tickReq.masterApprover.employee.id = ?3 and tickReq.approverStatus = ?4 ")
	public ArrayList<TicketRequest> findTicketRequestForApprover(String isActive, boolean approverRequired,
			long approverId, String approverStatus);

	@Query("Select ticketReq from TicketRequest ticketReq"
			+ " JOIN fetch ticketReq.travelRequest travReq where travReq.bookTicket = true and DATE(ticketReq.createdDate) >= ?1 and DATE(ticketReq.createdDate) <= ?2"
			+ " and ticketReq.isActive = ?3 and travReq.isActive = ?3")
	public List<TicketRequest> getTicketRequestByDateFilter(Date fromDate, Date toDate, String isActive);
}
