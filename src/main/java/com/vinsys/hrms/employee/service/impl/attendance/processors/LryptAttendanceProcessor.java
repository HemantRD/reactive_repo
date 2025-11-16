package com.vinsys.hrms.employee.service.impl.attendance.processors;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
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

import com.vinsys.hrms.constants.EOrganizationId;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.vo.AddSwipesVO;
import com.vinsys.hrms.employee.vo.AttendanceRequestVO;
import com.vinsys.hrms.employee.vo.AttendanceSwipeResponseVO;
import com.vinsys.hrms.employee.vo.EmployeeSwipeVO;
import com.vinsys.hrms.employee.vo.InOutStatusVO;
import com.vinsys.hrms.employee.vo.TeamAttendanceVO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.attendance.AttendanceCSVData;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

public class LryptAttendanceProcessor extends AbstractAttendanceProcessor {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private AttendanceProcessorDependencies dependencies;

	public LryptAttendanceProcessor(AttendanceProcessorDependencies detailsProcessorDependencies) {
		super(detailsProcessorDependencies);
		this.dependencies = detailsProcessorDependencies;
	}

	@Override
	public HRMSBaseResponse<?> addswipes(AddSwipesVO request) throws ParseException, HRMSException {
		log.info("Inside addswipes method");
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		saveSwipeData(empId, orgId);
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(dependencies.getApplicationVersion());
		log.info("Exit from addswipes method");
		return response;
	}

	private void saveSwipeData(Long empId, Long orgId) throws HRMSException {
		if (getSwipesCount(empId, orgId) >= 2) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee emp = dependencies.getEmployeeDAO().findActiveEmployeeByIdAndOrgId(empId, ERecordStatus.Y.name(),
				orgId);
		if (HRMSHelper.isNullOrEmpty(emp)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		AttendanceCSVData attendanceCSVData = new AttendanceCSVData();
		attendanceCSVData.setCreatedBy(empId.toString());
		attendanceCSVData.setCreatedDate(new Date());
		attendanceCSVData.setIsActive(IHRMSConstants.isActive);
		attendanceCSVData.setCardHolderName(emp.getCandidate().getFirstName() + " " + emp.getCandidate().getLastName());
		attendanceCSVData.setCardNo(0);
		attendanceCSVData.setDescription("From Portal IN/OUT");
		attendanceCSVData.setOrgId(orgId);
		attendanceCSVData.setSwapDate(new Date());
		attendanceCSVData.setEmployeeId(empId);
		attendanceCSVData.setProcessed(false);
		attendanceCSVData.setSwapTime(new Date());
		attendanceCSVData.setHashId(getHashCode(attendanceCSVData));
		dependencies.getAttendanceCsvDataDAO().save(attendanceCSVData);
		log.info("Exit from add swipe details service...");

	}

	/**
	 * 
	 * @param empId
	 * @param orgId This will return today date swipe count by date.
	 * @throws HRMSException
	 */
	protected int getSwipesCount(Long empId, Long orgId) throws HRMSException {
		return dependencies.getAttendanceCsvDataDAO().getSwipesCountByOrgIdAndDateAndEmpId(orgId,
				ERecordStatus.Y.name(), new Date(), empId);

	}

	private long getHashCode(AttendanceCSVData attendanceCSVData) {
		String data = String.valueOf(attendanceCSVData.getSwapDate()) + attendanceCSVData.getCardHolderName();
		return data.hashCode();
	}

	@Override
	public HRMSBaseResponse<EmployeeSwipeVO> employeeSwipeDetails(AttendanceRequestVO request, Pageable pageable)
			throws ParseException, HRMSException {
		log.info("Inside employeeSwipeDetails method");
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		return handleEmployeeSwipeDetailsResponse(request, empId, orgId, pageable);
	}

	private HRMSBaseResponse<EmployeeSwipeVO> handleEmployeeSwipeDetailsResponse(AttendanceRequestVO request,
			Long empId, Long orgId, Pageable pageable) throws HRMSException, ParseException {
		List<AttendanceCSVData> attendanceCSVData = new ArrayList<>();
		int totalRecord = 0;
		if (HRMSHelper.isNullOrEmpty(request.getFromDate()) && HRMSHelper.isNullOrEmpty(request.getToDate())) {
			attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
					.findBySwapDateBetweenAndOrgIdAndIsActiveAndEmployeeIdOrderBySwapDateAscSwapTimeAsc(new Date(),
							new Date(), orgId, ERecordStatus.Y.name(), empId, pageable);
			totalRecord = dependencies.getAttendanceCsvDataDAO().countBySwapDateBetweenAndOrgIdAndIsActiveAndEmployeeId(
					new Date(), new Date(), orgId, ERecordStatus.Y.name(), empId);
		} else {
			dependencies.getEmployeeAttendanceHelper().employeeGetSwipesInputValidation(request);
			Date fromDate = HRMSHelper.isNullOrEmpty(request.getFromDate()) ? new Date()
					: new SimpleDateFormat("dd-MM-yyyy").parse(request.getFromDate());
			Date toDate = HRMSHelper.isNullOrEmpty(request.getToDate()) ? new Date()
					: new SimpleDateFormat("dd-MM-yyyy").parse(request.getToDate());
			attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
					.findBySwapDateBetweenAndOrgIdAndIsActiveAndEmployeeIdOrderBySwapDateAscSwapTimeAsc(fromDate,
							toDate, orgId, ERecordStatus.Y.name(), empId, pageable);
			totalRecord = dependencies.getAttendanceCsvDataDAO().countBySwapDateBetweenAndOrgIdAndIsActiveAndEmployeeId(
					fromDate, toDate, orgId, ERecordStatus.Y.name(), empId);
		}

		List<AttendanceSwipeResponseVO> voAttendanceSwapDataList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(attendanceCSVData)) {
			voAttendanceSwapDataList = dependencies.getAttendanceDetailsTransformUtils()
					.transalteToAttendanceSwapDataListVO(attendanceCSVData);
		}
		return getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200, voAttendanceSwapDataList,
				totalRecord);
	}

	HRMSBaseResponse getBaseResponse(String responseMessage, int responseCode, Object responseBody, long totalRecord) {
		HRMSBaseResponse response = new HRMSBaseResponse<>();
		response.setResponseBody(responseBody);
		response.setResponseCode(responseCode);
		response.setResponseMessage(responseMessage);
		response.setTotalRecord(totalRecord);
		return response;
	}

	@Override
	public HRMSBaseResponse<InOutStatusVO> getSwipeInOutStatus() throws HRMSException {
		log.info("Inside getSwipeInOutStatus method");
		InOutStatusVO inOutStatusVO = new InOutStatusVO();
		inOutStatusVO.setIsInEnable(ERecordStatus.N.name());
		inOutStatusVO.setIsOutEnable(ERecordStatus.N.name());
		int todaySwipeCount = getSwipesCount(SecurityFilter.TL_CLAIMS.get().getEmployeeId(),
				Long.valueOf(EOrganizationId.LRYPT.toString()));
		if (HRMSHelper.isLongZero(todaySwipeCount)) {
			inOutStatusVO.setIsInEnable(ERecordStatus.Y.name());
		} else if (todaySwipeCount == 1) {
			inOutStatusVO.setIsOutEnable(ERecordStatus.Y.name());
		}
		log.info("Exit from getSwipeInOutStatus method");
		return getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200, inOutStatusVO, 0);
	}

	@Override
	public void empAttendanceReport(TeamAttendanceVO employeeAttendanceReportParams, HttpServletResponse response)
			throws ParseException, IOException, HRMSException {
		log.info("Inside empAttendanceReport method");
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		long empId = getEmpIdForGenerateReport(employeeAttendanceReportParams, roles);
	
		if(!HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
		validateEmployeeAndManagerMapping(employeeAttendanceReportParams);
		}
		dependencies.getEmployeeAttendanceHelper()
				.employeeAttendanceReportInputValidation(employeeAttendanceReportParams);
		reprocessAttendanceData(employeeAttendanceReportParams, orgId, empId);
		List<AttendanceProcessedData> attendanceProcessedData = getProcessedAttendanceDataByEmpIdAndDate(
				employeeAttendanceReportParams, orgId, empId);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=EmployeeAttendanceReport.xlsx");
		Workbook book = generateAttendanceReport(attendanceProcessedData);
		book.write(response.getOutputStream());

	}

	private List<AttendanceProcessedData> getProcessedAttendanceDataByEmpIdAndDate(
			TeamAttendanceVO employeeAttendanceReportParams, long orgId, long empId) throws ParseException {
		List<AttendanceProcessedData> attendanceProcessedData = new ArrayList<>();
		if (!HRMSHelper.isLongZero(empId)) {
			attendanceProcessedData = dependencies.getAttendanceProcessedDataDAO().findByDateOrgIdempId(empId,
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getFromDate()),
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getToDate()), orgId);
		} else {
			attendanceProcessedData = dependencies.getAttendanceProcessedDataDAO().findByDateOrgId(
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getFromDate()),
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getToDate()), orgId);
		}
		return attendanceProcessedData;
	}

	private void reprocessAttendanceData(TeamAttendanceVO employeeAttendanceReportParams, long orgId, long empId) {
		try {
			dependencies.getAttendanceHelper().UpdateProcessedData(
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getFromDate()),
					new SimpleDateFormat("dd-MM-yyyy").parse(employeeAttendanceReportParams.getToDate()), orgId, empId);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	private void validateEmployeeAndManagerMapping(TeamAttendanceVO employeeAttendanceReportParams)
			throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getEmpId())
				&& !HRMSHelper.isLongZero(employeeAttendanceReportParams.getEmpId())) {
			Employee emp = dependencies.getEmployeeDAO()
					.findActiveEmployeeById(Long.valueOf(employeeAttendanceReportParams.getEmpId()), "Y");
			long managerId = emp.getEmployeeReportingManager().getReporingManager().getId();
			if (managerId != SecurityFilter.TL_CLAIMS.get().getEmployeeId()) {
				throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
			}

		}
	}


	private long getEmpIdForGenerateReport(TeamAttendanceVO employeeAttendanceReportParams, List<String> roles) {
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
		return empId;
	}

	private Workbook generateAttendanceReport(List<AttendanceProcessedData> attendanceProcessedData) {
		XSSFWorkbook wb = new XSSFWorkbook();
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
		CellStyle headerStyle = getHeaderCellStle(wb, font);
		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = getCellStyleForBorder(wb, cellFont);
		CellStyle cellRedStyle = getRedColorCellStyle(wb, cellFont);
		XSSFSheet sheet = wb.createSheet("EmployeeAttendanceReport");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 8));
		setAttendanceReportHeaderData(attendanceProcessedData, detailsStyle, headerStyle, sheet);
		CellStyle dateTimeStyle = getDateTimeStyle(wb, font);
		setAttendanceReportRowData(attendanceProcessedData, cellStyle, cellRedStyle, sheet,dateTimeStyle);
		return wb;
	}

	private CellStyle getHeaderCellStle(XSSFWorkbook wb, Font font) {
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.MAROON.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFont(font);
		return headerStyle;
	}

	private CellStyle getCellStyleForBorder(XSSFWorkbook wb, Font cellFont) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFont(cellFont);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		return cellStyle;
	}

	private CellStyle getRedColorCellStyle(XSSFWorkbook wb, Font cellFont) {
		CellStyle cellRedStyle = wb.createCellStyle();
		cellRedStyle.setAlignment(HorizontalAlignment.CENTER);
		cellRedStyle.setFont(cellFont);
		cellRedStyle.setBorderBottom(BorderStyle.THIN);
		cellRedStyle.setBorderTop(BorderStyle.THIN);
		cellRedStyle.setBorderLeft(BorderStyle.THIN);
		cellRedStyle.setBorderRight(BorderStyle.THIN);
		cellRedStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		cellRedStyle.setFillPattern(FillPatternType.FINE_DOTS);
		return cellRedStyle;
	}
	
	private CellStyle getDateTimeStyle(XSSFWorkbook wb, Font cellFont) {
		CellStyle dateTimeStyle = wb.createCellStyle();
		dateTimeStyle.setAlignment(HorizontalAlignment.CENTER);
		dateTimeStyle.setBorderBottom(BorderStyle.THIN);
		dateTimeStyle.setBorderTop(BorderStyle.THIN);
		dateTimeStyle.setBorderLeft(BorderStyle.THIN);
		dateTimeStyle.setBorderRight(BorderStyle.THIN);
		CreationHelper createHelper = wb.getCreationHelper();
		short format = createHelper.createDataFormat().getFormat("h:mm AM/PM");
		dateTimeStyle.setDataFormat(format);
		return dateTimeStyle;
	}

	private void setAttendanceReportRowData(List<AttendanceProcessedData> attendanceProcessedData, CellStyle cellStyle,
			CellStyle cellRedStyle, XSSFSheet sheet, CellStyle dateTimeStyle) {
		XSSFRow row;
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
			cell.setCellValue(attendanceProcessedData.get(r).getStartTime());
			cell.setCellStyle(dateTimeStyle);
			
			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceProcessedData.get(r).getEndTime());
			cell.setCellStyle(dateTimeStyle);
			
			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceProcessedData.get(r).getManHours());
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
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);
			sheet.autoSizeColumn(11);
		}
	}

	private void setAttendanceReportHeaderData(List<AttendanceProcessedData> attendanceProcessedData,
			CellStyle detailsStyle, CellStyle headerStyle, XSSFSheet sheet) {
		XSSFRow row = sheet.createRow(0);
		Cell cell1 = row.createCell(0);
		cell1.setCellValue("LRYPT Technologies Pvt. Ltd");
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

		row = sheet.createRow(4);

		Cell srNo = row.createCell(1);
		srNo.setCellValue(" Sr. No. ");
		srNo.setCellStyle(headerStyle);

		Cell date = row.createCell(2);
		date.setCellValue("    Date     ");
		date.setCellStyle(headerStyle);

		Cell empName = row.createCell(3);
		empName.setCellValue(" Employee Name ");
		empName.setCellStyle(headerStyle);

		Cell designation = row.createCell(4);
		designation.setCellValue(" Designation ");
		designation.setCellStyle(headerStyle);

		Cell department = row.createCell(5);
		department.setCellValue(" Department ");
		department.setCellStyle(headerStyle);

		Cell empId = row.createCell(6);
		empId.setCellValue(" Employee ID ");
		empId.setCellStyle(headerStyle);
		
		Cell inTime = row.createCell(7);
		inTime.setCellValue(" In Time ");
		inTime.setCellStyle(headerStyle);
		
		Cell outTime = row.createCell(8);
		outTime.setCellValue(" Out Time ");
		outTime.setCellStyle(headerStyle);
		
		Cell manHour = row.createCell(9);
		manHour.setCellValue(" Total Hours ");
		manHour.setCellStyle(headerStyle);

		Cell status = row.createCell(10);
		status.setCellValue(" Status ");
		status.setCellStyle(headerStyle);

		Cell leaveApplied = row.createCell(11);
		leaveApplied.setCellValue(" Leave Applied ");
		leaveApplied.setCellStyle(headerStyle);
	}
	
	
	@Override
	public HRMSBaseResponse teamSwipeDetails(TeamAttendanceVO requestVO, Pageable pageable) throws HRMSException {
		log.info("Inside Team Attendance method");

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgID = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee emp = dependencies.getEmployeeDAO().findByEmpIdAndOrgId(requestVO.getEmpId(), orgID);
		HRMSBaseResponse response = new HRMSBaseResponse();

		if (HRMSHelper.isNullOrEmpty(emp)) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		List<AttendanceCSVData> attendanceCSVData = new ArrayList<>();
		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {

			teamSwipeDetailForManager(requestVO, pageable, orgID, empId, response, attendanceCSVData, emp);
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		log.info("Exit from swipe details service...");
		return response;
	}

	private void teamSwipeDetailForManager(TeamAttendanceVO requestVO, Pageable pageable, Long orgID, Long empId,
			HRMSBaseResponse response, List<AttendanceCSVData> attendanceCSVData,Employee emp) throws HRMSException {
		// Find all employee IDs under this manager
		List<EmployeeReportingManager> employeesUnderManager = dependencies.getReportingManagerDAO()
				.findByreporingManager(empId);


		long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
		int totalSwipeCount = 0;

		long matchCount = employeesUnderManager.stream()
				.filter(e -> e.getEmployee().getId().equals(requestVO.getEmpId())).count();

		if (matchCount > 0) {
			

				Date currentDate = new Date();
				String formattedCurrentDate = new SimpleDateFormat("dd-MM-yyyy").format(currentDate);

				if (HRMSHelper.isNullOrEmpty(requestVO.getFromDate())
						&& HRMSHelper.isNullOrEmpty(requestVO.getToDate())) {
				
					attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
							.findBySwapDateBetweenAndOrgIdAndIsActiveAndEmployeeIdOrderBySwapDateAscSwapTimeAsc(
									new Date(), new Date(),orgId, IHRMSConstants.isActive,emp.getId(), pageable);
					totalSwipeCount = dependencies.getAttendanceCsvDataDAO()
							.countBySwapDateBetweenAndOrgIdAndIsActiveAndEmployeeId(
									new Date(), new Date(),orgId, IHRMSConstants.isActive,emp.getId());

				} else {
					try {
						dependencies.getEmployeeAttendanceHelper().teamGetSwipesInputValidation(requestVO);
						Date fromDate = HRMSHelper.isNullOrEmpty(requestVO.getFromDate()) ? new Date()
								: new SimpleDateFormat("dd-MM-yyyy").parse(requestVO.getFromDate());
						Date toDate = HRMSHelper.isNullOrEmpty(requestVO.getToDate()) ? new Date()
								: new SimpleDateFormat("dd-MM-yyyy").parse(requestVO.getToDate());
					
						attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
								.findBySwapDateBetweenAndOrgIdAndIsActiveAndEmployeeIdOrderBySwapDateAscSwapTimeAsc(
										fromDate, toDate, orgId, IHRMSConstants.isActive,emp.getId(), pageable);
						totalSwipeCount = dependencies.getAttendanceCsvDataDAO()
								.countBySwapDateBetweenAndOrgIdAndIsActiveAndEmployeeId(
										fromDate, toDate,orgId, IHRMSConstants.isActive,emp.getId());
					} catch (HRMSException e1) {

						response.setResponseCode(e1.getResponseCode());
						response.setResponseMessage(e1.getResponseMessage());

					} catch (ParseException e1) {

						throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
					}

				}

			setTeamSwipeResponse(response, attendanceCSVData, totalSwipeCount);
		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	private void setTeamSwipeResponse(HRMSBaseResponse response, List<AttendanceCSVData> attendanceCSVData,
			int totalSwipeCount) {
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
	}

	@Override
	public HRMSBaseResponse teamSwipeDetailsForHR(TeamAttendanceVO requestVO, Pageable pageable)
			throws HRMSException, ParseException {
		log.info("Inside Team Attendance method for HR");

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgID=SecurityFilter.TL_CLAIMS.get().getOrgId();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
	    Employee emp = dependencies.getEmployeeDAO().findByEmpIdAndOrgId(requestVO.getEmpId(),orgID);
		HRMSBaseResponse response = new HRMSBaseResponse(); 

		if(HRMSHelper.isNullOrEmpty(emp)) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		
		List<AttendanceCSVData> attendanceCSVData = new ArrayList<>();
		
		 if(HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
			setSwipeDetailForHR(requestVO, pageable, emp, response, attendanceCSVData);
		}
		 else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		log.info("Exit from Inside Team Attendance method for HR");
		return response;
	}

	private void setSwipeDetailForHR(TeamAttendanceVO requestVO, Pageable pageable, Employee emp,
			HRMSBaseResponse response, List<AttendanceCSVData> attendanceCSVData) throws HRMSException {
		int totalSwipeCount = 0;
		Date currentDate = new Date();
		String formattedCurrentDate = new SimpleDateFormat("dd-MM-yyyy").format(currentDate);

		if (HRMSHelper.isNullOrEmpty(requestVO.getFromDate())
				&& HRMSHelper.isNullOrEmpty(requestVO.getToDate())) {
		
			attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
					.findSwipeByCustomQuery(emp.getId(),emp.getOrgId(),IHRMSConstants.isActive,
							emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							new Date(), new Date(), pageable);
			totalSwipeCount = dependencies.getAttendanceCsvDataDAO()
					.countByCustomQuery(emp.getId(),emp.getOrgId(),IHRMSConstants.isActive,
							emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							new Date(), new Date());

		} else {
			try {
				dependencies.getEmployeeAttendanceHelper().teamGetSwipesInputValidation(requestVO);
				Date fromDate = HRMSHelper.isNullOrEmpty(requestVO.getFromDate()) ? new Date()
						: new SimpleDateFormat("dd-MM-yyyy").parse(requestVO.getFromDate());
				Date toDate = HRMSHelper.isNullOrEmpty(requestVO.getToDate()) ? new Date()
						: new SimpleDateFormat("dd-MM-yyyy").parse(requestVO.getToDate());
			
				attendanceCSVData = dependencies.getAttendanceCsvDataDAO()
						.findSwipeByCustomQuery(emp.getId(),emp.getOrgId(),IHRMSConstants.isActive,
								emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
								fromDate, toDate, pageable);
				totalSwipeCount = dependencies.getAttendanceCsvDataDAO()
						.countByCustomQuery(emp.getId(),emp.getOrgId(),IHRMSConstants.isActive,
								emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
								fromDate,toDate);
			} catch (HRMSException e1) {

				response.setResponseCode(e1.getResponseCode());
				response.setResponseMessage(e1.getResponseMessage());

			} catch (ParseException e1) {

				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

		}
		setTeamSwipeResponse(response, attendanceCSVData, totalSwipeCount);
	}

	@Override
	public HRMSBaseResponse reProcessAttendance(String date,Long division) throws HRMSException{
		// TODO Auto-generated method stub
		return null;
	}



}