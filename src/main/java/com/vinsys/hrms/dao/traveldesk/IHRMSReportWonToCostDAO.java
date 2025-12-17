package com.vinsys.hrms.dao.traveldesk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vinsys.hrms.entity.traveldesk.ReportTicketRequest;
import com.vinsys.hrms.entity.traveldesk.ReportWonToCost;

public interface IHRMSReportWonToCostDAO
		extends JpaRepository<ReportWonToCost, String>, JpaSpecificationExecutor<ReportTicketRequest> {

}
