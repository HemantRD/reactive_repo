package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vinsys.hrms.entity.ReportLeaveSummary;

public interface IHRMSReportLeaveSummaryDAO
		extends JpaRepository<ReportLeaveSummary, String>, JpaSpecificationExecutor<ReportLeaveSummary> {

}
