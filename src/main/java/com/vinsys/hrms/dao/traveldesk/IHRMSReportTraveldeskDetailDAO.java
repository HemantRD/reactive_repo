package com.vinsys.hrms.dao.traveldesk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vinsys.hrms.entity.traveldesk.ReportTraveldeskDetail;

public interface IHRMSReportTraveldeskDetailDAO
		extends JpaRepository<ReportTraveldeskDetail, String>, JpaSpecificationExecutor<ReportTraveldeskDetail> {

}
