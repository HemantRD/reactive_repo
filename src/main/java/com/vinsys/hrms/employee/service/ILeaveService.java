package com.vinsys.hrms.employee.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.cookie.DateParseException;
import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.dashboard.vo.LeaveSumarryDetailsResponseVO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.VOAppliedLeaveCount;
import com.vinsys.hrms.datamodel.VOHolidayResponse;
import com.vinsys.hrms.employee.vo.ApplyGrantLeaveRequestVO;
import com.vinsys.hrms.employee.vo.ApplyLeaveRequestVO;
import com.vinsys.hrms.employee.vo.AvailableLeavesVO;
import com.vinsys.hrms.employee.vo.EmployeeLeaveDetailsVO;
import com.vinsys.hrms.employee.vo.LeaveCalculationRequestVO;
import com.vinsys.hrms.employee.vo.SubOrdinateListVO;
import com.vinsys.hrms.exception.HRMSException;

public interface ILeaveService {

	HRMSBaseResponse<?> applyLeave(ApplyLeaveRequestVO request)
			throws HRMSException, ParseException, DateParseException;

	HRMSBaseResponse<?> cancelLeave(ApplyLeaveRequestVO request) throws HRMSException, IOException;

	HRMSBaseResponse<AvailableLeavesVO> getAvailableLeave(Long leaveTypeId) throws HRMSException;

	HRMSBaseResponse<?> withdrawLeave(ApplyLeaveRequestVO request) throws HRMSException, ParseException;

	HRMSBaseResponse<?> approveLeave(List<ApplyLeaveRequestVO> request) throws HRMSException;

	HRMSBaseResponse<VOAppliedLeaveCount> calculateLeave(LeaveCalculationRequestVO calculationRequestVO)
			throws HRMSException, ParseException;

	HRMSBaseResponse<?> rejectLeave(ApplyLeaveRequestVO request) throws HRMSException;

	HRMSBaseResponse<?> withdrawApproveLeave(ApplyLeaveRequestVO request) throws HRMSException;

	HRMSBaseResponse<?> withdrawRejectLeave(ApplyLeaveRequestVO request) throws HRMSException;

	HRMSBaseResponse<?> applyGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException, ParseException;

	HRMSBaseResponse<?> cancelGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException;

	HRMSBaseResponse<?> withdrawGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException;

	HRMSBaseResponse<?> approveGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException;

	HRMSBaseResponse<?> rejectGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException;

	HRMSBaseResponse<VOAppliedLeaveCount> calculateGrantLeave(LeaveCalculationRequestVO calculationRequestVO)
			throws HRMSException;

	HRMSBaseResponse<EmployeeLeaveDetailsVO> getLeaveDetails(Long employeeId, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<EmployeeLeaveDetailsVO> getGrantLeaveDetails(Long employeeId, Pageable pageable)
			throws HRMSException;

	// HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetails(Long
	// filterEmployeeId,Pageable pageable) throws HRMSException;

	HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamGrantLeaveDetails(Pageable pageable, Long employeeId)
			throws HRMSException;

	HRMSBaseResponse<List<VOHolidayResponse>> holidayList(Long year) throws HRMSException;

	HRMSBaseResponse<SubOrdinateListVO> findSubordinate() throws HRMSException;

	HRMSBaseResponse<LeaveSumarryDetailsResponseVO> getEmployeeLeaveSummary(long empId, Integer year)
			throws HRMSException;

	HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetails(Long filterEmployeeId, Integer year, Pageable pageable)
			throws HRMSException;

	byte[] downloadTeamLeavesReport(String fromDate, String toDate, HttpServletResponse httpServletResponse)
			throws IOException, HRMSException, ParseException;

//	HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetails(Long filterEmployeeId, Pageable pageable)
//			throws HRMSException;

	public String creditEmpYearlyCasualLeavesMethod(Long orgId, List<Long> divList, float clCount)
			throws HRMSException, ParseException;

	public String creditEarnedLeaves(Long orgId, List<Long> divsionList, float elCount)
			throws HRMSException, ParseException;

	byte[] downloadAllEmployeeLeavesReport(String fromDate, String toDate, HttpServletResponse httpServletResponse)
			throws IOException, HRMSException, ParseException;


	HRMSBaseResponse<EmployeeLeaveDetailsVO> getLeaveDetailsForHr(Long filterEmployeeId, Integer year,
			Pageable pageable) throws HRMSException;
}
