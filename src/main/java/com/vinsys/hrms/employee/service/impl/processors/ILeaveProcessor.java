package com.vinsys.hrms.employee.service.impl.processors;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

public interface ILeaveProcessor {

	public HRMSBaseResponse<?> applyLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException, ParseException;

	public HRMSBaseResponse<?> cancelLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException;

	public HRMSBaseResponse<AvailableLeavesVO> getAvailableLeaveProcess(Long leaveTypeId) throws HRMSException;

	public HRMSBaseResponse<?> withdrawLeaveLeaveProcess(ApplyLeaveRequestVO request)
			throws HRMSException, ParseException;

	public HRMSBaseResponse<?> approveLeaveProcess(List<ApplyLeaveRequestVO> request) throws HRMSException;

	public HRMSBaseResponse<VOAppliedLeaveCount> acalculateLeaveProcess(LeaveCalculationRequestVO requestVo)
			throws HRMSException;

	public HRMSBaseResponse<?> rejectLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException;

	public HRMSBaseResponse<?> withdrawApproveLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException;

	public HRMSBaseResponse<?> withdrawRejectLeaveProcess(ApplyLeaveRequestVO request) throws HRMSException;

	public HRMSBaseResponse<?> applyGrantLeaveProcess(ApplyGrantLeaveRequestVO request)
			throws HRMSException, ParseException;

	public HRMSBaseResponse<VOAppliedLeaveCount> calculateGrantLeaveProcess(LeaveCalculationRequestVO request)
			throws HRMSException;

	public HRMSBaseResponse<?> cancelGrantLeaveProcess(ApplyGrantLeaveRequestVO request) throws HRMSException;

	public HRMSBaseResponse<?> withdrawGrantLeaveProcess(ApplyGrantLeaveRequestVO request) throws HRMSException;

	public HRMSBaseResponse<?> approveGrantLeaveProcess(ApplyGrantLeaveRequestVO request) throws HRMSException;

	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getLeaveDetailsProcess(Long employeeId, Pageable pageable)
			throws HRMSException;

	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getGrantLeaveDetailsProcess(Long employeeId, Pageable pageable)
			throws HRMSException;

	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetailsProcess(Long filterEmployeeId, Integer year,
			Pageable pageable) throws HRMSException;

	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetailsProcess(Pageable pageable, Long employeeId)
			throws HRMSException;

	public HRMSBaseResponse<List<VOHolidayResponse>> holidayListProcess(Long year) throws HRMSException;

	public HRMSBaseResponse<SubOrdinateListVO> findSubordinate() throws HRMSException;

	public HRMSBaseResponse<LeaveSumarryDetailsResponseVO> getEmployeeLeaveSummary(long empId, Integer year)
			throws HRMSException;

	public void clearExpiredCompOffs();

	public HRMSBaseResponse rejectGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException;

	byte[] downloadTeamLeavesReport(String fromDate, String toDate,
			HttpServletResponse httpServletResponse) throws HRMSException, IOException, ParseException;
	
	public String creditEmpYearlyCasualLeavesMethod(Long orgId, List<Long> divList, float clCount) throws HRMSException;
	public String creditEarnedLeaves(Long orgId,List<Long> divsionList,float elCount) throws HRMSException, ParseException;

	public byte[] downloadAllEmployeeLeavesReport(String fromDate, String toDate,
			HttpServletResponse httpServletResponse)throws HRMSException, IOException, ParseException;

	
	

	HRMSBaseResponse<EmployeeLeaveDetailsVO> getHrLeaveDetailsProcess(Long filterEmployeeId, Integer year,
			Pageable pageable) throws HRMSException;
}
