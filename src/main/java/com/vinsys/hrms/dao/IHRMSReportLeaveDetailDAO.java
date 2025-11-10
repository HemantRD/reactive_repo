package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.ReportLeaveDetail;

public interface IHRMSReportLeaveDetailDAO
		extends JpaRepository<ReportLeaveDetail, String>, JpaSpecificationExecutor<ReportLeaveDetail> {

	@Query("Select lvDetail from ReportLeaveDetail lvDetail where lvDetail.employeeName like %?1%")
	// @Query(value="Select * from view_leave_details_report lvDetail where
	// lvDetail.employee_name like %?1%",nativeQuery = true)
	public List<ReportLeaveDetail> getLeaveDetailReportQuery(String empName);
}
