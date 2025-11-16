package com.vinsys.hrms.employee.service;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.vo.AddSwipesVO;
import com.vinsys.hrms.employee.vo.AttendanceRequestVO;
import com.vinsys.hrms.employee.vo.AttendanceResponseVO;
import com.vinsys.hrms.employee.vo.EmployeeSwipeVO;
import com.vinsys.hrms.employee.vo.InOutStatusVO;
import com.vinsys.hrms.employee.vo.TeamAttendanceVO;
import com.vinsys.hrms.exception.HRMSException;

public interface IAttendanceService {

	HRMSBaseResponse<EmployeeSwipeVO> employeeSwipeDetails(AttendanceRequestVO requestVO, Pageable pageable)
			throws ParseException, HRMSException;

	HRMSBaseResponse teamSwipeDetails(TeamAttendanceVO requestVO, Pageable pageable)
			throws HRMSException, ParseException;

	void empAttendanceReport(TeamAttendanceVO employeeAttendanceReportParams, HttpServletResponse res)
			throws ParseException, IOException, HRMSException;

	HRMSBaseResponse<AttendanceResponseVO> empTeamAttendanceDetails(int month, Long empId)
			throws HRMSException, ParseException;

	HRMSBaseResponse<AttendanceResponseVO> empAttendanceDetails(Integer month) throws HRMSException, ParseException;

	/************* API to Add IN/Out swipes *********************/
	HRMSBaseResponse<?> addswipes(AddSwipesVO request) throws ParseException, HRMSException;

	HRMSBaseResponse<InOutStatusVO> getSwipeInOutStatus() throws HRMSException;

	HRMSBaseResponse teamSwipeDetailsForHR(TeamAttendanceVO requestVO, Pageable pageable)
			throws HRMSException, ParseException;

	HRMSBaseResponse reProcessAttendance(String date,Long division)throws HRMSException, ParseException;
}
