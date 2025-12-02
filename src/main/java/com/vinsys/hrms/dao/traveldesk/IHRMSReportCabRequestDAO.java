package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.ReportCabRequest;

public interface IHRMSReportCabRequestDAO extends JpaRepository<ReportCabRequest, Long>,JpaSpecificationExecutor<ReportCabRequest> {

	@Query("Select cabReqReport from ReportCabRequest cabReqReport")
	public List<ReportCabRequest> getCabRequestReportQuery();
}