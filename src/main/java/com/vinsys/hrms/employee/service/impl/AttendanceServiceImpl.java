package com.vinsys.hrms.employee.service.impl;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendaceEmployeeACNDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceCsvDataDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.service.IAttendanceService;
import com.vinsys.hrms.employee.service.impl.attendance.processors.AttendanceProcessorDependencies;
import com.vinsys.hrms.employee.service.impl.attendance.processors.AttendanceProcessorFactory;
import com.vinsys.hrms.employee.service.impl.attendance.processors.IAttendanceProcessor;
import com.vinsys.hrms.employee.vo.AddSwipesVO;
import com.vinsys.hrms.employee.vo.AttendanceRequestVO;
import com.vinsys.hrms.employee.vo.AttendanceResponseVO;
import com.vinsys.hrms.employee.vo.EmployeeSwipeVO;
import com.vinsys.hrms.employee.vo.InOutStatusVO;
import com.vinsys.hrms.employee.vo.TeamAttendanceVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.AttendanceDetailsTransformUtils;
import com.vinsys.hrms.util.EmployeeAttendanceHelper;
import com.vinsys.hrms.util.HRMSAttendanceHelper;

@Service
public class AttendanceServiceImpl implements IAttendanceService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app_version}")
	private String applicationVersion;
	@Autowired
	HRMSAttendanceHelper attendanceHelper;
	@Autowired
	IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO;
	@Autowired
	IHRMSEmployeeAcnDAO employeeAcnDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO holidayDAO;
	@Autowired
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	@Autowired
	IHRMSAttendanceCsvDataDAO attendanceCsvDataDAO;
	@Autowired
	IHRMSAttendaceEmployeeACNDAO employeeACNDAO;
	@Autowired
	EmployeeAttendanceHelper employeeAttendanceHelper;
	@Autowired
	IHRMSEmployeeReportingManager reportingManagerDAO;
	@Autowired
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;
	@Autowired
	AttendanceDetailsTransformUtils attendanceDetailsTransformUtils;

	protected AttendanceProcessorDependencies getDependencies() {
		AttendanceProcessorDependencies dependencies = new AttendanceProcessorDependencies(applicationVersion,
				attendanceHelper, attendanceProcessedDataDAO, employeeAcnDAO, employeeDAO, holidayDAO,
				organizationWeekoffDAO, attendanceCsvDataDAO, employeeACNDAO, employeeAttendanceHelper,
				reportingManagerDAO, employeeLeaveAppliedDAO, attendanceDetailsTransformUtils);
		return dependencies;
	}

	public IAttendanceProcessor getProcessor() {
		AttendanceProcessorDependencies dependencies = getDependencies();
		AttendanceProcessorFactory factory = new AttendanceProcessorFactory();
		return factory.getAttendanceProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
	}

	@Override
	public HRMSBaseResponse<AttendanceResponseVO> empAttendanceDetails(Integer month)
			throws HRMSException, ParseException {
		IAttendanceProcessor processor = getProcessor();
		return processor.empAttendanceDetails(month);
	}

	public HRMSBaseResponse<EmployeeSwipeVO> employeeSwipeDetails(AttendanceRequestVO employeeAttendanceReportParams,
			Pageable pageable) throws ParseException, HRMSException {
		IAttendanceProcessor processor = getProcessor();
		return processor.employeeSwipeDetails(employeeAttendanceReportParams, pageable);
	}

	/*****************
	 * My Team APIs for attendance
	 * 
	 * @throws ParseException
	 ***************************************/

	@Override
	public HRMSBaseResponse<AttendanceResponseVO> empTeamAttendanceDetails(int month, Long empId)
			throws HRMSException, ParseException {
		IAttendanceProcessor processor = getProcessor();
		return processor.empTeamAttendanceDetails(month, empId);
	}

	/*****************************
	 * Report
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws HRMSException
	 ********************************/
	@Override
	public void empAttendanceReport(TeamAttendanceVO employeeAttendanceReportParams, HttpServletResponse res)
			throws ParseException, IOException, HRMSException {
		IAttendanceProcessor processor = getProcessor();
		processor.empAttendanceReport(employeeAttendanceReportParams, res);
	}

	@Override
	public HRMSBaseResponse teamSwipeDetails(TeamAttendanceVO requestVO, Pageable pageable)
			throws HRMSException, ParseException {
		IAttendanceProcessor processor = getProcessor();
		return processor.teamSwipeDetails(requestVO, pageable);
	}

	/************* API to Add IN/Out swipes *********************/

	@Override
	public HRMSBaseResponse<?> addswipes(AddSwipesVO request) throws ParseException, HRMSException {
		IAttendanceProcessor processor = getProcessor();
		return processor.addswipes(request);
	}

	@Override
	public HRMSBaseResponse<InOutStatusVO> getSwipeInOutStatus() throws HRMSException {
		IAttendanceProcessor processor = getProcessor();
		return processor.getSwipeInOutStatus();
	}

	@Override
	public HRMSBaseResponse teamSwipeDetailsForHR(TeamAttendanceVO requestVO, Pageable pageable)
			throws HRMSException, ParseException {
		IAttendanceProcessor processor = getProcessor();
		return processor.teamSwipeDetailsForHR(requestVO, pageable);
	}

	@Override
	public HRMSBaseResponse reProcessAttendance(String date,Long divisionId) throws HRMSException, ParseException {
		IAttendanceProcessor processor =getProcessor();
		return processor.reProcessAttendance(date,divisionId);
	}
}
