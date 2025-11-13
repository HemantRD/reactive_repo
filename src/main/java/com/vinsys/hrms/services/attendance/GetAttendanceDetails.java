package com.vinsys.hrms.services.attendance;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.attendance.VOEmployeeAttendanceReport;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/attendance")

public class GetAttendanceDetails {

	private static final Logger logger = LoggerFactory.getLogger(GetAttendanceDetails.class);
	private Map<Long, Employee> empAcnMap = new HashMap<Long, Employee>();

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

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, value = "/getdetails", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String empAttendanceReport(@RequestBody VOEmployeeAttendanceReport employeeAttendanceReportParams,
			HttpServletResponse res) {

		List<EmployeeACN> employeeACNList = new ArrayList<>();
		List<Object> colHeadersMapList = getHeaderRows();

		try {
			employeeACNList = employeeAcnDAO.findAll();
			for (EmployeeACN employeeACN : employeeACNList) {
				empAcnMap.put(employeeACN.getEmpACN(), employeeACN.getEmployee());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			List<AttendanceProcessedData> attendanceProcessedData = new ArrayList<>();
			try {
				if (!HRMSHelper.isLongZero(employeeAttendanceReportParams.getEmployeeId()))

					if (HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getFromDate())
							&& HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getToDate())) {

						Calendar calendar = Calendar.getInstance();
						Date currentDate = calendar.getTime();

						calendar.set(Calendar.DAY_OF_MONTH, 1);
						Date startDate = calendar.getTime();

						calendar.add(Calendar.MONTH, 1);
						calendar.add(Calendar.DAY_OF_MONTH, -1);
						Date endDate = calendar.getTime();

						attendanceProcessedData = attendanceProcessedDataDAO.findByDateOrgIdempId(
								employeeAttendanceReportParams.getEmployeeId(), startDate, endDate,
								employeeAttendanceReportParams.getOrgId());
					} else {
						attendanceProcessedData = attendanceProcessedDataDAO.findByDateOrgIdempId(
								employeeAttendanceReportParams.getEmployeeId(),
								new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getFromDate()),
								new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getToDate()),
								employeeAttendanceReportParams.getOrgId());
					}

				else
					attendanceProcessedData = attendanceProcessedDataDAO.findByDateOrgId(
							new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getFromDate()),
							new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getToDate()),
							employeeAttendanceReportParams.getOrgId());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
			List<Object> voAttendanceProcessedDataList = new ArrayList<>();

			if (!HRMSHelper.isNullOrEmpty(attendanceProcessedData)) {
				voAttendanceProcessedDataList = HRMSResponseTranslator
						.transalteToAttendanceProcessListVO(attendanceProcessedData, voAttendanceProcessedDataList);
				hrmsListResponseObject.setListResponse((List<Object>) voAttendanceProcessedDataList);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				hrmsListResponseObject.setColHeaders(colHeadersMapList);
				return HRMSHelper.createJsonString(hrmsListResponseObject);
			} else {

				return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
						IHRMSConstants.DataNotFoundCode);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	private List<Object> getHeaderRows() {

		Map<String, Object> colHeadersMap = new HashMap<String, Object>();
		List<Object> colHeadersMapList = new ArrayList<>();

		colHeadersMap.put("property", "srNo");
		colHeadersMap.put("header", "Sr. No.");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "attendanceDate");
		colHeadersMap.put("header", "Date");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "empName");
		colHeadersMap.put("header", "Employee Name");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "empId");
		colHeadersMap.put("header", "Employee ID");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "status");
		colHeadersMap.put("header", "Status");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		return colHeadersMapList;
	}

	@RequestMapping(value = "getAttendanceGraph/{empId}/{weekNo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HRMSBaseResponse empAttendanceGraphDetails(@PathVariable("empId") long empId,
			@PathVariable("weekNo") Integer weekNo, HttpServletResponse res)
			throws JsonGenerationException, JsonMappingException, IOException, HRMSException {

		try {

			HRMSBaseResponse response = new HRMSBaseResponse();
			if (!HRMSHelper.isLongZero(empId)) {

				Employee emp = employeeDAO.findActiveEmployeeById(empId, "Y");
				long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
				long branchId = emp.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
				long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
				long deptId = emp.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
				long currentYear = Year.now().getValue();
				
				
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.WEEK_OF_MONTH, weekNo);

				Date startOfWeek = HRMSDateUtil.getStartOfWeekInCurrentMonth(calendar);
				Date endOfWeek = HRMSDateUtil.getEndOfWeekInCurrentMonth(calendar);

				List<Object> voAttendanceProcessedDataList = new ArrayList<>();
				List<Object> voHolidayList = new ArrayList<>();
				
				List<AttendanceProcessedData> attendanceProcessedData = attendanceProcessedDataDAO
						.findWeekWiseAttendanceForEmployee(empId, startOfWeek, endOfWeek);

				HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();

				if (!HRMSHelper.isNullOrEmpty(attendanceProcessedData)) {
					voAttendanceProcessedDataList = HRMSResponseTranslator
							.transalteToAttendanceProcessListVO(attendanceProcessedData, voAttendanceProcessedDataList);

					List<OrganizationHoliday> holidayEntityList = holidayDAO.getHolidayListByOrgBranchDivIdYear(orgId,
							divId, branchId, currentYear);

					if (!HRMSHelper.isNullOrEmpty(holidayEntityList)) {

						voHolidayList = HRMSResponseTranslator.transalteToHolidayListVO(holidayEntityList,
								voHolidayList);

					}

					List<OrganizationWeekoff> weekoffList = organizationWeekoffDAO.getWeekoffByOrgBranchDivDeptId(orgId,
							divId, branchId, deptId);
					

					if (!HRMSHelper.isNullOrEmpty(weekoffList)) {

						voHolidayList = HRMSResponseTranslator.transalteToHolidayListVO(holidayEntityList,
								voHolidayList);

					}

					hrmsListResponseObject.setListResponse(voAttendanceProcessedDataList);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);

					response.setResponseBody(hrmsListResponseObject);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);

					HRMSListResponseObject holidayResponseObject = new HRMSListResponseObject();
					holidayResponseObject.setListResponse(voHolidayList);
					holidayResponseObject.setResponseCode(IHRMSConstants.successCode);
					holidayResponseObject.setResponseMessage(IHRMSConstants.successMessage);

					response.setResponseBody(holidayResponseObject);
					return response;

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);

				}

			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();

			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
		}

	}

}
