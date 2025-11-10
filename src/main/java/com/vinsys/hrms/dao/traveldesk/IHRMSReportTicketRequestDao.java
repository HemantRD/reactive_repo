package com.vinsys.hrms.dao.traveldesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vinsys.hrms.entity.traveldesk.ReportTicketRequest;


public interface IHRMSReportTicketRequestDao extends JpaRepository<ReportTicketRequest, Long>,JpaSpecificationExecutor<ReportTicketRequest> {

}