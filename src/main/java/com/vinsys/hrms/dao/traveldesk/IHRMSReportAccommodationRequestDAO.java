package com.vinsys.hrms.dao.traveldesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vinsys.hrms.entity.traveldesk.ReportAccommodationRequest;


public interface IHRMSReportAccommodationRequestDAO  extends JpaRepository<ReportAccommodationRequest, Long>,JpaSpecificationExecutor<ReportAccommodationRequest> {

}
