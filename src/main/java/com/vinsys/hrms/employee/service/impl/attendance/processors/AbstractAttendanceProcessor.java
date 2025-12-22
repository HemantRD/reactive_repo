package com.vinsys.hrms.employee.service.impl.attendance.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.vo.AddSwipesVO;
import com.vinsys.hrms.employee.vo.AttendanceRequestVO;
import com.vinsys.hrms.employee.vo.AttendanceResponseVO;
import com.vinsys.hrms.employee.vo.AttendanceSwipeResponseVO;
import com.vinsys.hrms.employee.vo.EmployeeSwipeVO;
import com.vinsys.hrms.employee.vo.InOutStatusVO;
import com.vinsys.hrms.employee.vo.TeamAttendanceVO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.entity.attendance.AttendanceCSVData;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

public abstract class AbstractAttendanceProcessor implements IAttendanceProcessor {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private AttendanceProcessorDependencies dependencies;

	protected AbstractAttendanceProcessor(AttendanceProcessorDependencies detailsProcessorDependencies) {
		this.dependencies = detailsProcessorDependencies;
	}

	@Override
	public HRMSBaseResponse<AttendanceResponseVO> empAttendanceDetails(Integer month)
			throws HRMSException, ParseException {
		HRMSBaseResponse<AttendanceResponseVO> response = new HRMSBaseResponse<AttendanceResponseVO>();

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		Employee emp = dependencies.getEmployeeDAO().findActiveEmployeeById(Long.valueOf(empId), "Y");
		long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
		long branchId = !HRMSHelper
				.isNullOrEmpty(emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation())
						? emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation().getId()
						: emp.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
		long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
		long deptId = emp.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
		int currentYear = Year.now().getValue();

		Calendar calendar = Calendar.getInstance();
		// calendar.set(Calendar.WEEK_OF_MONTH, weekNumber);
		List<OrganizationHoliday> holiday = null;
		List<AttendanceProcessedData> attendanceProcessedData = new ArrayList<>();

		if (!HRMSHelper.isLongZero(empId))
			if (HRMSHelper.isLongZero(month)) {
				calendar.setTime(new Date());
				calendar.set(Calendar.DAY_OF_MONTH, 1); // Set day of the month to 1 first
				int currentMonth = calendar.get(Calendar.MONTH) + 1;
				Date startDate = calendar.getTime();

				calendar.add(Calendar.MONTH, 1);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date endDate = calendar.getTime();
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

				holiday = dependencies.getHolidayDAO().getHolidayListByOrgBranchDivId(orgId, divId, branchId, startDate,
						endDate);
				attendanceProcessedData = dependencies.getAttendanceProcessedDataDAO().findByDateOrgIdempId(empId,
						formatter.parse(formatter.format(startDate)), formatter.parse(formatter.format(endDate)),
						orgId);
			} else {
				if (month >= 1 && month <= 12) {
					calendar.setTime(new Date());
					calendar.set(Calendar.YEAR, currentYear);
					calendar.set(Calendar.MONTH, month - 1);
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					Date startDate = calendar.getTime();

					calendar.add(Calendar.MONTH, 1);
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					Date endDate = calendar.getTime();
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

					holiday = dependencies.getHolidayDAO().getHolidayListByOrgBranchDivId(orgId, divId, branchId,
							startDate, endDate);
					attendanceProcessedData = dependencies.getAttendanceProcessedDataDAO().findByDateOrgIdempId(empId,
							formatter.parse(formatter.format(startDate)), formatter.parse(formatter.format(endDate)),
							orgId);
				}
			}
		if (HRMSHelper.isNullOrEmpty(attendanceProcessedData) && HRMSHelper.isLongZero(month)) {

			calendar.setTime(new Date());
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			Date startDateOfPreviousMonth = calendar.getTime();

			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date endDateOfPreviousMonth = calendar.getTime();

			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			holiday = dependencies.getHolidayDAO().getHolidayListByOrgBranchDivId(orgId, divId, branchId,
					startDateOfPreviousMonth, endDateOfPreviousMonth);
			attendanceProcessedData = dependencies.getAttendanceProcessedDataDAO().findByDateOrgIdempId(empId,
					formatter.parse(formatter.format(startDateOfPreviousMonth)),
					formatter.parse(formatter.format(endDateOfPreviousMonth)), orgId);
		}

		List<OrganizationHoliday> holidayEntityList = dependencies.getHolidayDAO()
				.getHolidayListByOrgBranchDivIdYear(orgId, divId, branchId, currentYear);

		List<OrganizationWeekoff> weekoffList = dependencies.getOrganizationWeekoffDAO()
				.getWeekoffByOrgBranchDivDeptId(orgId, divId, branchId, deptId);

		// get week days

		List<String> weekDays = new ArrayList<String>();

		String weekOffDays = weekoffList.get(0).getWeekoffDays();
		String[] weekday = weekOffDays.split(",");

		int index = getIndex(weekday[weekday.length - 1]);
		int startingDay = index + 1;

		calendar.setTime(new Date());

		// Set the calendar's day of the week to the starting day
		calendar.set(Calendar.DAY_OF_WEEK, startingDay);

		// Print the days of the week
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		for (int i = 0; i < 7; i++) {
			weekDays.add(sdf.format(calendar.getTime()));
			calendar.add(Calendar.DAY_OF_WEEK, 1);
		}

		AttendanceResponseVO attendanceVO = new AttendanceResponseVO();

		if (!HRMSHelper.isNullOrEmpty(attendanceProcessedData)) {
			attendanceVO = dependencies.getAttendanceDetailsTransformUtils()
					.transalteToAttendanceVO(attendanceProcessedData, holidayEntityList, weekoffList, holiday);
			attendanceVO.setWeekDays(weekDays);
			response.setResponseBody(attendanceVO);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(dependencies.getApplicationVersion());

		} else {

			throw new HRMSException(IHRMSConstants.DataNotFoundMessage);
		}

		return response;

	}

	private int getIndex(String string) {
		switch (string) {
		case "SUNDAY":
			return Calendar.SUNDAY;
		case "MONDAY":
			return Calendar.MONDAY;
		case "TUESDAY":
			return Calendar.TUESDAY;
		case "WEDNESDAY":
			return Calendar.WEDNESDAY;
		case "THURSDAY":
			return Calendar.THURSDAY;
		case "FRIDAY":
			return Calendar.FRIDAY;
		case "SATURDAY":
			return Calendar.SATURDAY;
		default:
			throw new IllegalArgumentException("Invalid day: " + string);
		}
	}

	public HRMSBaseResponse<EmployeeSwipeVO> employeeSwipeDetails(AttendanceRequestVO employeeAttendanceReportParams,
			Pageable pageable) throws ParseException, HRMSException {
		log.info("GETTING SWIPE DETAILS...");

		HRMSBaseResponse<EmployeeSwipeVO> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		Employee emp = dependencies.getEmployeeDAO().findActiveEmployeeById(Long.valueOf(empId), "Y");

		EmployeeACN empAcn = dependencies.getEmployeeACNDAO().getEmployeeACNDetailsByEmpId(IHRMSConstants.isActive,
				empId);
		long empAcnNo = empAcn.getEmpACN();
		long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();

		List<AttendanceCSVData> attendanceCSVData = new ArrayList<>();
		Long totalSwipeCount = 0L;
		if (!HRMSHelper.isLongZero(empAcnNo)) {
			if (HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getFromDate())
					&& HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getToDate())) {
				attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
						.findBySwapDateBetweenAndCardNoAndOrgIdAndIsActiveOrderBySwapDateAscSwapTimeAsc(new Date(),
								new Date(), empAcnNo, orgId, "Y", pageable);
				totalSwipeCount = dependencies.getAttendanceCsvDataDAO()
						.countBySwapDateBetweenAndCardNoAndOrgIdAndIsActive(empAcnNo, orgId, "Y", new Date(),
								new Date());
			} else {

				dependencies.getEmployeeAttendanceHelper()
						.employeeGetSwipesInputValidation(employeeAttendanceReportParams);

				Date fromDate = HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getFromDate()) ? new Date()
						: new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getFromDate());
				Date toDate = HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getToDate()) ? new Date()
						: new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getToDate());

				attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
						.findBySwapDateBetweenAndCardNoAndOrgIdAndIsActiveOrderBySwapDateAscSwapTimeAsc(fromDate,
								toDate, empAcnNo, orgId, "Y", pageable);

				totalSwipeCount = dependencies.getAttendanceCsvDataDAO()
						.countBySwapDateBetweenAndCardNoAndOrgIdAndIsActive(empAcnNo, orgId, "Y", fromDate, toDate);

			}

		} else {

			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1200));
		}
		List<AttendanceSwipeResponseVO> voAttendanceSwapDataList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(attendanceCSVData)) {
			voAttendanceSwapDataList = dependencies.getAttendanceDetailsTransformUtils()
					.transalteToAttendanceSwapDataListVO(attendanceCSVData);

			EmployeeSwipeVO swipeDetails = new EmployeeSwipeVO();
			swipeDetails.setSwipeDetails(voAttendanceSwapDataList);

			response.setTotalRecord(totalSwipeCount);
			response.setResponseBody(swipeDetails);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(dependencies.getApplicationVersion());

		} else {

			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		log.info("Exit from swipe details service...");
		return response;
	}

	/*****************
	 * My Team APIs for attendance
	 * 
	 * @throws ParseException
	 ***************************************/

	@Override
	public HRMSBaseResponse<AttendanceResponseVO> empTeamAttendanceDetails(int month, Long empId)
			throws HRMSException, ParseException {
		log.info("Inside empTeamAttendanceDetails method...");
		HRMSBaseResponse<AttendanceResponseVO> response = new HRMSBaseResponse<>();

		Long managerId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		Employee emp = dependencies.getEmployeeDAO().findActiveEmployeeById(Long.valueOf(empId), "Y");
		long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
		long branchId = !HRMSHelper
				.isNullOrEmpty(emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation())
						? emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation().getId()
						: emp.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
		long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
		long deptId = emp.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
		int currentYear = Year.now().getValue();
		long empManagerId = emp.getEmployeeReportingManager().getReporingManager().getId();

		Calendar calendar = Calendar.getInstance();

		List<OrganizationHoliday> holiday = null;

		if (managerId == empManagerId) {
			List<AttendanceProcessedData> attendanceProcessedData = new ArrayList<>();

			if (!HRMSHelper.isLongZero(empId))

				if (HRMSHelper.isLongZero(month)) {
					calendar.setTime(new Date());
					calendar.set(Calendar.DAY_OF_MONTH, 1); // Set day of the month to 1 first
					int currentMonth = calendar.get(Calendar.MONTH) + 1;
					Date startDate = calendar.getTime();

					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					Date endDate = calendar.getTime();

					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

					holiday = dependencies.getHolidayDAO().getHolidayListByOrgBranchDivId(orgId, divId, branchId,
							startDate, endDate);

					attendanceProcessedData = dependencies.getAttendanceProcessedDataDAO().findByDateOrgIdempId(empId,
							formatter.parse(formatter.format(startDate)), formatter.parse(formatter.format(endDate)),
							orgId);
				} else {
					if (month >= 1 && month <= 12) {
						calendar.setTime(new Date());
						calendar.set(Calendar.YEAR, currentYear);
						calendar.set(Calendar.MONTH, month - 1);
						calendar.set(Calendar.DAY_OF_MONTH, 1);
						Date startDate = calendar.getTime();

						calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
						Date endDate = calendar.getTime();

						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						formatter.parse(formatter.format(startDate));

						holiday = dependencies.getHolidayDAO().getHolidayListByOrgBranchDivId(orgId, divId, branchId,
								startDate, endDate);
						attendanceProcessedData = dependencies.getAttendanceProcessedDataDAO().findByDateOrgIdempId(
								empId, formatter.parse(formatter.format(startDate)),
								formatter.parse(formatter.format(endDate)), orgId);
					}
				}

			List<OrganizationHoliday> holidayEntityList = dependencies.getHolidayDAO()
					.getHolidayListByOrgBranchDivIdYear(orgId, divId, branchId, currentYear);

			List<OrganizationWeekoff> weekoffList = dependencies.getOrganizationWeekoffDAO()
					.getWeekoffByOrgBranchDivDeptId(orgId, divId, branchId, deptId);

			// get week days

			List<String> weekDays = new ArrayList<String>();

			String weekOffDays = weekoffList.get(0).getWeekoffDays();
			String[] weekday = weekOffDays.split(",");

			int index = getIndex(weekday[weekday.length - 1]);
			int startingDay = index + 1;

			calendar.setTime(new Date());

			// Set the calendar's day of the week to the starting day
			calendar.set(Calendar.DAY_OF_WEEK, startingDay);

			SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
			for (int i = 0; i < 7; i++) {
				weekDays.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DAY_OF_WEEK, 1);
			}

			AttendanceResponseVO attendanceVO = new AttendanceResponseVO();

			if (!HRMSHelper.isNullOrEmpty(attendanceProcessedData)) {
				attendanceVO = dependencies.getAttendanceDetailsTransformUtils()
						.transalteToAttendanceVO(attendanceProcessedData, holidayEntityList, weekoffList, holiday);
				attendanceVO.setWeekDays(weekDays);
				response.setResponseBody(attendanceVO);
				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setApplicationVersion(dependencies.getApplicationVersion());
				log.info("Exit empTeamAttendanceDetails method...");

			} else {

				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		return response;

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
		// HRMSBaseResponse response = new HRMSBaseResponse();

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		long empId = 0;
		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {

			if (!HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getEmpId())
					&& !HRMSHelper.isLongZero(employeeAttendanceReportParams.getEmpId())) {
				empId = employeeAttendanceReportParams.getEmpId();
			} else {

				empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			}

		} else {

			empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		}

		if (!HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getEmpId())
				&& !HRMSHelper.isLongZero(employeeAttendanceReportParams.getEmpId())) {
			Employee emp = dependencies.getEmployeeDAO()
					.findActiveEmployeeById(Long.valueOf(employeeAttendanceReportParams.getEmpId()), "Y");
			long managerId = emp.getEmployeeReportingManager().getReporingManager().getId();
			if (managerId != SecurityFilter.TL_CLAIMS.get().getEmployeeId()) {
				throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
			}

		}

		Employee emp = dependencies.getEmployeeDAO().findActiveEmployeeById(Long.valueOf(empId), "Y");
		long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();

		dependencies.getEmployeeAttendanceHelper()
				.employeeAttendanceReportInputValidation(employeeAttendanceReportParams);

		try {
			dependencies.getAttendanceHelper().UpdateProcessedData(
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getFromDate()),
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getToDate()), orgId, empId);
		} catch (ParseException e1) {

			e1.printStackTrace();
		}

		List<AttendanceProcessedData> attendanceProcessedData = new ArrayList<>();

		if (!HRMSHelper.isLongZero(empId))
			attendanceProcessedData = dependencies.getAttendanceProcessedDataDAO().findByDateOrgIdempId(empId,
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getFromDate()),
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getToDate()), orgId);
		else
			attendanceProcessedData = dependencies.getAttendanceProcessedDataDAO().findByDateOrgId(
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getFromDate()),
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getToDate()), orgId);

		res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		res.setHeader("Content-Disposition", "attachment; filename=EmployeeAttendanceReport.xlsx");

		Workbook book = writeXLSXFile(attendanceProcessedData);
		book.write(res.getOutputStream());
		// response.setApplicationVersion(applicationVersion);
		// return response;

	}

	private Workbook writeXLSXFile(List<AttendanceProcessedData> attendanceProcessedData) {
		// TODO Auto-generated method stub
		String sheetName = "EmployeeAttendanceReport";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		Font detailsFont = wb.createFont();
		detailsFont.setBold(true);
		detailsFont.setFontHeightInPoints((short) 11);

		CellStyle detailsStyle = wb.createCellStyle();
		detailsStyle.setAlignment(HorizontalAlignment.LEFT);
		detailsStyle.setFont(detailsFont);

		Font font = wb.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontHeightInPoints((short) 11);
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.MAROON.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		// style.setFillPattern(CellStyle.BIG_SPOTS);
		headerStyle.setFont(font);

		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFont(cellFont);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);

		CellStyle cellRedStyle = wb.createCellStyle();
		cellRedStyle.setAlignment(HorizontalAlignment.CENTER);
		cellRedStyle.setFont(cellFont);
		cellRedStyle.setBorderBottom(BorderStyle.THIN);
		cellRedStyle.setBorderTop(BorderStyle.THIN);
		cellRedStyle.setBorderLeft(BorderStyle.THIN);
		cellRedStyle.setBorderRight(BorderStyle.THIN);
		cellRedStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		cellRedStyle.setFillPattern(FillPatternType.FINE_DOTS);

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 8));
		// sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));

		XSSFRow row = sheet.createRow(0);
		Cell cell1 = row.createCell(0);
		cell1.setCellValue("Vinsys IT Services India Limited.");
		cell1.setCellStyle(detailsStyle);

		row = sheet.createRow(1);
		cell1 = row.createCell(0);
		cell1.setCellValue("Employee Attendance Report for the period From "
				+ HRMSDateUtil.format(attendanceProcessedData.get(0).getCompositePrimaryKey().getAttendanceDate(),
						"dd-MM-yyyy")
				+ "" + " TO " + HRMSDateUtil.format(attendanceProcessedData.get(attendanceProcessedData.size() - 1)
						.getCompositePrimaryKey().getAttendanceDate(), "dd-MM-yyyy"));
		cell1.setCellStyle(detailsStyle);

		row = sheet.createRow(2);
		cell1 = row.createCell(0);
		cell1.setCellValue("Report generated date : "
				+ HRMSDateUtil.format(HRMSDateUtil.getToday(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		cell1.setCellStyle(detailsStyle);

		/*
		 * row = sheet.createRow(2); cell1 = row.createCell(0);
		 * cell1.setCellValue("Employee ID: " +
		 * attendanceProcessedData.get(0).getEmpId()); cell1.setCellStyle(detailsStyle);
		 * 
		 * row = sheet.createRow(3); cell1 = row.createCell(0);
		 * cell1.setCellValue("Employee Name: " +
		 * attendanceProcessedData.get(0).getEmpName());
		 * cell1.setCellStyle(detailsStyle);
		 */

		row = sheet.createRow(4);

		Cell cell21 = row.createCell(1);
		cell21.setCellValue(" Sr. No. ");
		cell21.setCellStyle(headerStyle);

		Cell cell22 = row.createCell(2);
		cell22.setCellValue("    Date     ");
		cell22.setCellStyle(headerStyle);

		Cell cell23 = row.createCell(3);
		cell23.setCellValue(" Employee Name ");
		cell23.setCellStyle(headerStyle);

		Cell cell26 = row.createCell(4);
		cell26.setCellValue(" Designation ");
		cell26.setCellStyle(headerStyle);

		Cell cell27 = row.createCell(5);
		cell27.setCellValue(" Department ");
		cell27.setCellStyle(headerStyle);

		Cell cell24 = row.createCell(6);
		cell24.setCellValue(" Employee ID ");
		cell24.setCellStyle(headerStyle);

		Cell cell25 = row.createCell(7);
		cell25.setCellValue(" Status ");
		cell25.setCellStyle(headerStyle);

		Cell cell28 = row.createCell(8);
		cell28.setCellValue(" Leave Applied ");
		cell28.setCellStyle(headerStyle);

		int rowNo = 4;
		for (int r = 0; r < attendanceProcessedData.size(); r++) {

			++rowNo;
			row = sheet.createRow(rowNo);
			int cellNo = 1;

			Cell cell = row.createCell(cellNo++);
			cell.setCellValue(r + 1);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(HRMSDateUtil
					.format(attendanceProcessedData.get(r).getCompositePrimaryKey().getAttendanceDate(), "dd-MM-yyyy"));
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceProcessedData.get(r).getEmpName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceProcessedData.get(r).getDesignationName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceProcessedData.get(r).getDepartmentName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceProcessedData.get(r).getCompositePrimaryKey().getEmpId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceProcessedData.get(r).getStatus());
			if (!attendanceProcessedData.get(r).getStatus().trim().equals("P")
					&& !attendanceProcessedData.get(r).getStatus().trim().equals("WO")
					&& !attendanceProcessedData.get(r).getStatus().trim().equals("H"))
				cell.setCellStyle(cellRedStyle);
			else
				cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(!HRMSHelper.isNullOrEmpty(attendanceProcessedData.get(r).getLeaveType())
					? attendanceProcessedData.get(r).getLeaveType()
					: "-");
			cell.setCellStyle(cellStyle);

			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
		}
		return wb;
	}

	@Override
	public HRMSBaseResponse teamSwipeDetails(TeamAttendanceVO requestVO, Pageable pageable) throws HRMSException {
		log.info("Inside Team Attendance method");

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		HRMSBaseResponse response = new HRMSBaseResponse(); // Assuming response is a local variable.

		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
			// Retrieve the manager's ID
			Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			// Find all employee IDs under this manager
			List<EmployeeReportingManager> employeesUnderManager = dependencies.getReportingManagerDAO()
					.findByreporingManager(empId);

			Employee emp = dependencies.getEmployeeDAO().findActiveEmployeeById(requestVO.getEmpId(), "Y");

			EmployeeACN empAcn = dependencies.getEmployeeACNDAO().getEmployeeACNDetailsByEmpId(IHRMSConstants.isActive,
					requestVO.getEmpId());
			long empAcnNo = empAcn.getEmpACN();
			long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
			Long totalSwipeCount = 0L;

			long matchCount = employeesUnderManager.stream()
					.filter(e -> e.getEmployee().getId().equals(requestVO.getEmpId())).count();

			if (matchCount > 0) {
				List<AttendanceCSVData> attendanceCSVData = new ArrayList<>();

				if (!HRMSHelper.isLongZero(empAcnNo)) {
					Date currentDate = new Date();
					String formattedCurrentDate = new SimpleDateFormat("dd-MM-yyyy").format(currentDate);

					if (HRMSHelper.isNullOrEmpty(requestVO.getFromDate())
							&& HRMSHelper.isNullOrEmpty(requestVO.getToDate())) {
						attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
								.findBySwapDateBetweenAndCardNoAndOrgIdAndIsActiveOrderBySwapDateAscSwapTimeAsc(
										new Date(), new Date(), empAcnNo, orgId, "Y", pageable);
						totalSwipeCount = dependencies.getAttendanceCsvDataDAO()
								.countBySwapDateBetweenAndCardNoAndOrgIdAndIsActive(empAcnNo, orgId, "Y", new Date(),
										new Date());

					} else {
						try {
							dependencies.getEmployeeAttendanceHelper().teamGetSwipesInputValidation(requestVO);
							Date fromDate = HRMSHelper.isNullOrEmpty(requestVO.getFromDate()) ? new Date()
									: new SimpleDateFormat("dd-MM-yyyy").parse(requestVO.getFromDate());
							Date toDate = HRMSHelper.isNullOrEmpty(requestVO.getToDate()) ? new Date()
									: new SimpleDateFormat("dd-MM-yyyy").parse(requestVO.getToDate());

							attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
									.findBySwapDateBetweenAndCardNoAndOrgIdAndIsActiveOrderBySwapDateAscSwapTimeAsc(
											fromDate, toDate, empAcnNo, orgId, "Y", pageable);
							totalSwipeCount = dependencies.getAttendanceCsvDataDAO()
									.countBySwapDateBetweenAndCardNoAndOrgIdAndIsActive(empAcnNo, orgId, "Y", fromDate,
											toDate);
						} catch (HRMSException e1) {

							response.setResponseCode(e1.getResponseCode());
							response.setResponseMessage(e1.getResponseMessage());

						} catch (ParseException e1) {

							throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
						}

					}

				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}

				List<AttendanceSwipeResponseVO> voAttendanceSwapDataList = new ArrayList<>();
				if (!HRMSHelper.isNullOrEmpty(attendanceCSVData)) {
					voAttendanceSwapDataList = dependencies.getAttendanceDetailsTransformUtils()
							.transalteToAttendanceSwapDataListVO(attendanceCSVData);

					EmployeeSwipeVO swipeDetails = new EmployeeSwipeVO();
					swipeDetails.setSwipeDetails(voAttendanceSwapDataList);
					response.setTotalRecord(totalSwipeCount);
					response.setResponseBody(swipeDetails);
					response.setResponseCode(1200);
					response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
					response.setApplicationVersion(dependencies.getApplicationVersion());
				}
			} else {
				throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		log.info("Exit from swipe details service...");
		return response;
	}

	/************* API to Add IN/Out swipes *********************/

	@Override
	public HRMSBaseResponse<?> addswipes(AddSwipesVO request) throws ParseException, HRMSException {
		log.info("ADDING SWIPE DETAILS...");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee emp = dependencies.getEmployeeDAO().findActiveEmployeeById(Long.valueOf(empId), "Y");
		EmployeeACN empAcn = dependencies.getEmployeeACNDAO().getEmployeeACNDetailsByEmpId(IHRMSConstants.isActive,
				empId);
		long empAcnNo = empAcn.getEmpACN();
		long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
		long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
		long branchId = !HRMSHelper
				.isNullOrEmpty(emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation())
						? emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation().getId()
						: emp.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
		AttendanceCSVData attendanceCSVData = new AttendanceCSVData();
		InputStream is = null;
		BufferedReader bfReader = null;
//		
//		is = new ByteArrayInputStream(uploadedInputStream.getBytes());
//		bfReader = new BufferedReader(new InputStreamReader(is));
//		String line = null;
//		while ((line = bfReader.readLine()) != null && line.trim().length() > 0) {
//			log.info(line);
//
//			String[] data;
//			data = line.split("\\|");

		if (!HRMSHelper.isNullOrEmpty(request)) {

			attendanceCSVData.setCreatedBy(empId.toString());
			attendanceCSVData.setCreatedDate(new Date());
			attendanceCSVData.setIsActive(IHRMSConstants.isActive);
			String Name = emp.getCandidate().getFirstName() + " " + emp.getCandidate().getLastName();
			attendanceCSVData.setCardHolderName(Name);
			attendanceCSVData.setCardNo(empAcnNo);
			attendanceCSVData.setDescription("Trace Card");
			attendanceCSVData.setOrgId(orgId);
			attendanceCSVData.setSwapDate(new Date());
			attendanceCSVData.setEmployeeId(empId);
			attendanceCSVData.setProcessed(false);

//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			if (!HRMSHelper.isNullOrEmpty(request.getSwipe())) {

//				Date currentTime = new Date();

				// Get the current date and time in the Asia/Kolkata time zone
				ZoneId asiaSingapore = ZoneId.of("Asia/Kolkata");
				ZonedDateTime currentDateTime = ZonedDateTime.now(asiaSingapore);

				// Format the current date and time
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedTime = formatter.format(currentDateTime);

				// Parse the formatted time string to Date object
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date swipetime = dateFormat.parse(formattedTime);

//				String formattedTime = dateFormat.format(currentDateTime);
//				Date swipetime = dateFormat.parse(formattedTime);
				attendanceCSVData.setSwapTime(swipetime);

				String hashId = formattedTime + empId;

				String numericPart = hashId.replaceAll("[^\\d]", "");
				long hashIdAsLong = Long.parseLong(numericPart);
				attendanceCSVData.setHashId(hashIdAsLong);

			}

//			if (!HRMSHelper.isNullOrEmpty(request.getSwipe())) {
//				Date inTime = dateFormat.parse(request.getInTime());
//				attendanceCSVData.setSwapTime(inTime);
//			} else {
//				if (!HRMSHelper.isNullOrEmpty(request.getOutTime())) {
//					Date outTime = dateFormat.parse(request.getOutTime());
//				attendanceCSVData.setSwapTime(outTime);
//				}
//			}

			dependencies.getAttendanceCsvDataDAO().save(attendanceCSVData);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(dependencies.getApplicationVersion());

			log.info("Exit from add swipe details service...");
		} else {

			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1200));

		}
		// }
		return response;
	}

	private long getTxtHashCode(String line) {
		return line.hashCode();
	}

	@Override
	public HRMSBaseResponse<InOutStatusVO> getSwipeInOutStatus() throws HRMSException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public HRMSBaseResponse teamSwipeDetailsForHR(TeamAttendanceVO requestVO, Pageable pageable)
			throws HRMSException, ParseException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @author vidya.chandane
	 * Description : re-process the attendance for particular date and division 
	 */
	@Override
	public HRMSBaseResponse<?> reProcessAttendance(String date,Long division) throws HRMSException, ParseException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		log.info("Inside reProcessAttendace Method");
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
		}
		validateDate(date);
		List<AttendanceCSVData> attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
				.findAttendanceListByDateAndOrgId(new SimpleDateFormat("dd-MM-yyyy").parse(date), orgId,division);

		if (!HRMSHelper.isNullOrEmpty(attendanceCSVData)) {
			for (AttendanceCSVData csvData : attendanceCSVData) {
				csvData.setProcessed(false);
				csvData.setUpdatedDate(new Date());
				csvData.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
				dependencies.getAttendanceCsvDataDAO().save(csvData);
				log.info(" reProcessAttendace : Attendance has been reprocessed for employeeId : "
						+ csvData.getEmployeeId()+ " ACN : " + csvData.getCardNo());
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1625));
		log.info("Exit from reProcessAttendace Method");
		return response;
	}

	private void validateDate(String date) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(date)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " date");
		}

		if (!HRMSHelper.validateDateFormate(date)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " date");
		}
	}

}