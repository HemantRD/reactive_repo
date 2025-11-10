package com.vinsys.hrms.dashboard;

import java.text.ParseException;

import com.vinsys.hrms.dashboard.vo.AttendanceGraphVO;
import com.vinsys.hrms.dashboard.vo.BirthdayVO;
import com.vinsys.hrms.dashboard.vo.GalleryVO;
import com.vinsys.hrms.dashboard.vo.LeaveSumarryDetailsResponseVO;
import com.vinsys.hrms.dashboard.vo.ServiceCompletionVO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;

public interface IDashboardService {

	HRMSBaseResponse<BirthdayVO> getBirthdayEvents() throws HRMSException, ParseException;

	HRMSBaseResponse<ServiceCompletionVO> getServiceCompletions() throws HRMSException, ParseException;

	HRMSBaseResponse<GalleryVO> getGalleryImages() throws HRMSException;

	HRMSBaseResponse<LeaveSumarryDetailsResponseVO> getEmployeeLeaveSummary() throws HRMSException;

	HRMSBaseResponse<AttendanceGraphVO> empAttendanceGraphDetails(String fromDate, String toDate)
			throws HRMSException, ParseException;

}
