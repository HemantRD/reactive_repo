package com.vinsys.hrms.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.criteriaBuilder.SpecificationFactory;
import com.vinsys.hrms.dao.IHRMSReportLeaveSummaryDAO;
import com.vinsys.hrms.datamodel.VOLeaveReportParam;
import com.vinsys.hrms.entity.ReportLeaveSummary;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/reportSummaryDetails")

public class ReportLeaveSummaryService {

	private static final Logger logger = LoggerFactory.getLogger(ReportLeaveSummaryService.class);

	@Autowired
	IHRMSReportLeaveSummaryDAO leaveSummaryReportDao;

	@RequestMapping(method = { RequestMethod.GET })
	@ResponseBody
	public void demoLeaveSummaryReport(HttpServletResponse res) {
		try {
			List<Long> listOfLeaveType = new ArrayList<Long>();
			res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			res.setHeader("Content-Disposition", "attachment; filename=deployment-definitions.xlsx");
			VOLeaveReportParam param = new VOLeaveReportParam();
			param.setLeaveTypesStr("12,7,6,5,11,9,4,2,16,");
			// param.setLeaveTypesStr("7,");
			// param.setOrgId(1);
			List<ReportLeaveSummary> leaveSummary = leaveSummaryReportDao
					.findAll(SpecificationFactory.extendedLeaveSummaryReport(param));
			String[] leavetypes = param.getLeaveTypesStr().split(",");
			for (String lvTypeId : leavetypes) {
				listOfLeaveType.add(new Long(lvTypeId));
			}
			Workbook book = writeXLSXFile(leaveSummary, listOfLeaveType, param);
			book.write(res.getOutputStream());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	@ResponseBody
	public void fetchLeaveSummaryReport(@RequestBody VOLeaveReportParam leaveReportParam, HttpServletResponse res) {
		logger.info("******************* Leave Summary Report START *********************");
		List<ReportLeaveSummary> leaveSummary = leaveSummaryReportDao
				.findAll(SpecificationFactory.extendedLeaveSummaryReport(leaveReportParam));
		try {

			if (HRMSHelper.isNullOrEmpty(leaveSummary) || leaveSummary.size() == 0) {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
			/*
			 * 2 == Compensatory Leave' 4 == Leaves for Probationary Employees' 5 == LOP' 6
			 * == Maternity Leave' 7 == On Duty' 9 == Privilege Leaves' 11 == Sick Leaves'
			 * 12 == Work From Home'
			 */
			List<Long> listOfLeaveType = new ArrayList<Long>();

			if (!HRMSHelper.isNullOrEmpty(leaveReportParam.getLeaveTypesStr())) {
				String[] leavetypes = leaveReportParam.getLeaveTypesStr().split(",");
				for (String lvTypeId : leavetypes) {
					if (lvTypeId.equalsIgnoreCase("2") || lvTypeId.equalsIgnoreCase("4")
							|| lvTypeId.equalsIgnoreCase("16") || lvTypeId.equalsIgnoreCase("9")
							|| lvTypeId.equalsIgnoreCase("11")) {
						listOfLeaveType.add(new Long(lvTypeId));
					}

				}
				Workbook workbook = writeXLSXFile(leaveSummary, listOfLeaveType, leaveReportParam);

				res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				res.setHeader("Content-Disposition", "attachment; filename=Leave_Details_Report.xlsx");
				workbook.write(res.getOutputStream());

			} else {
				throw new HRMSException(IHRMSConstants.LeaveNotselectedCode, IHRMSConstants.LeaveNotselectedMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				// return HRMSHelper.sendErrorResponse(e.getResponseMessage(),
				// e.getResponseCode());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				// return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage,
				// IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		logger.info("******************* Leave Details Report END *********************");
		// return null;
	}

	public static Workbook writeXLSXFile(List<ReportLeaveSummary> leaveSummary, List<Long> listOfLeaveType,
			VOLeaveReportParam param) throws IOException {

		// String excelFileName = "E:/input/Test.xlsx";// name of excel file

		String sheetName = "LeaveSummaryReport";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		ArrayList<String> listOfMainHeader = new ArrayList<String>();
		for (int i = 1; i <= 6; i++) {
			listOfMainHeader.add("");
		}
		if (listOfLeaveType.contains(new Long(9))) {
			listOfMainHeader.add("Privilege Leaves");
			for (int j = 2; j <= 8; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(4))) {
			listOfMainHeader.add("Probationary Leaves");
			for (int j = 2; j <= 8; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(2))) {
			listOfMainHeader.add("Compensatory Leaves");
			for (int j = 2; j <= 8; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(7))) {
			listOfMainHeader.add("On Duty");
			for (int j = 2; j <= 8; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(11))) {
			listOfMainHeader.add("Sick Leave");
			for (int j = 2; j <= 8; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(5))) {
			listOfMainHeader.add("LOP");
			for (int j = 2; j <= 8; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(12))) {
			listOfMainHeader.add("Work From Home");
			for (int j = 2; j <= 8; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(6))) {
			listOfMainHeader.add("Maternity Leave");
			for (int j = 2; j <= 8; j++) {
				listOfMainHeader.add("");
			}

		}
		if (listOfLeaveType.contains(new Long(16))) {
			listOfMainHeader.add("Service Completion Leave");
			for (int j = 2; j <= 8; j++) {
				listOfMainHeader.add("");
			}

		}
		listOfMainHeader.add("Grand Total");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 20));

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(1, // mention first row here
				1, // mention last row here, it is 1 as we are doing a column wise merging
				6, // mention first column of merging
				13 // mention last column to include in merge
		));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 14, 21));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 22, 29));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 30, 37));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 38, 42));
		// sheet.addMergedRegion(new CellRangeAddress(1, 1, 46, 53));
		// sheet.addMergedRegion(new CellRangeAddress(1, 1, 54, 61));
		// sheet.addMergedRegion(new CellRangeAddress(1, 1, 62, 69));
		// sheet.addMergedRegion(new CellRangeAddress(1, 1, 70, 74));

		List<String> tempListOfSubHeader = new ArrayList<String>();
		tempListOfSubHeader.add("Closing Balance");
		tempListOfSubHeader.add("PYLeave Encashment");
		tempListOfSubHeader.add("Leave Carried Over");
		tempListOfSubHeader.add("Leaves Earned");
		tempListOfSubHeader.add("FYLeave Encashment");
		tempListOfSubHeader.add("Total Eligibility");
		tempListOfSubHeader.add("Number of days Availed");
		tempListOfSubHeader.add("Leave Available");

		List<String> listOfsubHeader = new ArrayList<String>();
		listOfsubHeader.add("Employee ID");
		listOfsubHeader.add("  Employee Name   ");
		listOfsubHeader.add("  Designation   ");
		listOfsubHeader.add("  Department  ");
		listOfsubHeader.add("     Branch     ");
		listOfsubHeader.add("            Division            ");
		// listOfsubHeader.add("Leave Applied Date");
		// listOfsubHeader.add(" From Date ");
		// listOfsubHeader.add(" To Date ");
		// listOfsubHeader.add(" From Session ");
		// listOfsubHeader.add(" To Session ");

		if (listOfLeaveType.contains(new Long(9))) {
			listOfsubHeader.addAll(tempListOfSubHeader);
		}
		if (listOfLeaveType.contains(new Long(4))) {
			listOfsubHeader.addAll(tempListOfSubHeader);
		}
		if (listOfLeaveType.contains(new Long(2))) {
			listOfsubHeader.addAll(tempListOfSubHeader);
		}
		if (listOfLeaveType.contains(new Long(7))) {
			listOfsubHeader.addAll(tempListOfSubHeader);
		}
		if (listOfLeaveType.contains(new Long(11))) {
			listOfsubHeader.addAll(tempListOfSubHeader);
		}
		if (listOfLeaveType.contains(new Long(5))) {
			listOfsubHeader.addAll(tempListOfSubHeader);
		}
		if (listOfLeaveType.contains(new Long(12))) {
			listOfsubHeader.addAll(tempListOfSubHeader);
		}
		if (listOfLeaveType.contains(new Long(6))) {
			listOfsubHeader.addAll(tempListOfSubHeader);
		}
		if (listOfLeaveType.contains(new Long(16))) {
			listOfsubHeader.addAll(tempListOfSubHeader);
		}
		listOfsubHeader.add("Leave Carried Over");
		listOfsubHeader.add("Leaves Earned");
		listOfsubHeader.add("Total Eligibility");
		listOfsubHeader.add("Number of days Availed");
		listOfsubHeader.add("Leave Available");

		Font detailsFont = wb.createFont();
		detailsFont.setBold(true);
		detailsFont.setFontHeightInPoints((short) 11);
		CellStyle detailsStyle = wb.createCellStyle();
		detailsStyle.setAlignment(HorizontalAlignment.LEFT);
		detailsStyle.setFont(detailsFont);

		Font font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 11);
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFont(font);

		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = wb.createCellStyle();
		// cellStyle.setBorderLeft(BorderStyle.THIN);
		// cellStyle.setBorderRight(BorderStyle.THIN);
		// cellStyle.setBorderTop(BorderStyle.THIN);
		// cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setFont(cellFont);

		XSSFRow row = sheet.createRow(0);
		Cell cell1 = row.createCell(0);
		cell1.setCellValue("Report generated date : "
				+ HRMSDateUtil.format(HRMSDateUtil.getToday(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		cell1.setCellStyle(detailsStyle);

		row = sheet.createRow(1);

		for (int i = 0; i < listOfMainHeader.size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(listOfMainHeader.get(i));
			cell.setCellStyle(headerStyle);
		}
		row = sheet.createRow(2);
		for (int j = 0; j < listOfsubHeader.size(); j++) {
			Cell cell = row.createCell(j);
			cell.setCellValue(listOfsubHeader.get(j));
			cell.setCellStyle(headerStyle);
			sheet.setHorizontallyCenter(true);
			sheet.autoSizeColumn(j);
		}

		Map<Long, ReportLeaveSummary> mapOfEmpBasicDetail = new HashMap<Long, ReportLeaveSummary>();
		Map<Long, ReportLeaveSummary> mapOfLeaveSummaryRow = new HashMap<Long, ReportLeaveSummary>();
		Map<Long, Map<Long, ReportLeaveSummary>> mapOfLeaveSummary = new HashMap<Long, Map<Long, ReportLeaveSummary>>();

		for (ReportLeaveSummary element : leaveSummary) {
			// mapOfLeaveSummaryRow.put(element.getLeaveTypeId(),element);
			mapOfLeaveSummary.put(element.getEmployeeId(), null);
			mapOfEmpBasicDetail.put(element.getEmployeeId(), element);
		}

		Set<Long> keysOfEmpIds = mapOfLeaveSummary.keySet();
		for (Long keyEmpId : keysOfEmpIds) {
			mapOfLeaveSummaryRow = new HashMap<Long, ReportLeaveSummary>();
			for (ReportLeaveSummary element : leaveSummary) {
				if (keyEmpId.longValue() == element.getEmployeeId()) {
					mapOfLeaveSummaryRow.put(element.getLeaveTypeId(), element);
				}
			}
			mapOfLeaveSummary.put(keyEmpId, mapOfLeaveSummaryRow);
		}

		int rowNo = 2;

		Set<Long> keysOfEmpIds1 = mapOfEmpBasicDetail.keySet();
		for (Long keyEmpId1 : keysOfEmpIds1) {

			/*********************
			 * Employee Basic Details Columns START
			 *********************/
			++rowNo;
			row = sheet.createRow(rowNo);
			int cellNo = 0;
			Cell cell = row.createCell(cellNo++);
			cell.setCellValue(mapOfEmpBasicDetail.get(keyEmpId1).getEmployeeId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(mapOfEmpBasicDetail.get(keyEmpId1).getEmployeeName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(mapOfEmpBasicDetail.get(keyEmpId1).getDesignationName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(mapOfEmpBasicDetail.get(keyEmpId1).getDepartmentName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(mapOfEmpBasicDetail.get(keyEmpId1).getBranchName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(mapOfEmpBasicDetail.get(keyEmpId1).getDivisionName());
			cell.setCellStyle(cellStyle);

			/********************* Employee Basic Details Columns END *********************/

			Map<Long, ReportLeaveSummary> mapOfEmpLvSumm = mapOfLeaveSummary.get(keyEmpId1);
			Set<Long> keysOfLvTypeIds1 = mapOfEmpLvSumm.keySet();

			float totalLeaveCarriedOver = 0;
			float totalLeavesEarned = 0;
			float totalTotalEligibility = 0;
			float totalNumberOfDaysAvailed = 0;
			float totalLeaveAvailable = 0;

			for (Long keyLvTypeId1 : keysOfLvTypeIds1) {

				if (listOfLeaveType.contains(new Long(9))) {
					if (mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveTypeId() == 9) {
						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver());
							totalLeaveCarriedOver += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned());
							totalLeavesEarned += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned();

						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility());
							totalTotalEligibility += mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed());
							totalNumberOfDaysAvailed += mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable());
							totalLeaveAvailable += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable();
						}
						cell.setCellStyle(cellStyle);
						break;
					}
					if (!mapOfEmpLvSumm.containsKey(new Long(9))) {
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						break;
					}
				}
			}
			for (Long keyLvTypeId1 : keysOfLvTypeIds1) {

				if (listOfLeaveType.contains(new Long(4))) {
					if (mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveTypeId() == 4) {
						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver());
							totalLeaveCarriedOver += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned());
							totalLeavesEarned += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned();

						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility());
							totalTotalEligibility += mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed());
							totalNumberOfDaysAvailed += mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable());
							totalLeaveAvailable += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable();
						}
						cell.setCellStyle(cellStyle);
						break;
					}
					if (!mapOfEmpLvSumm.containsKey(new Long(4))) {
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						break;
					}
				}

			}
			// start of service completion code
			for (Long keyLvTypeId1 : keysOfLvTypeIds1) {

				if (listOfLeaveType.contains(new Long(16))) {
					if (mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveTypeId() == 16) {
						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver());
							totalLeaveCarriedOver += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned());
							totalLeavesEarned += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned();

						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility());
							totalTotalEligibility += mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed());
							totalNumberOfDaysAvailed += mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable());
							totalLeaveAvailable += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable();
						}
						cell.setCellStyle(cellStyle);
						break;
					}
					if (!mapOfEmpLvSumm.containsKey(new Long(16))) {
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						break;
					}
				}

			}
			// end of service completion loop
			for (Long keyLvTypeId1 : keysOfLvTypeIds1) {

				if (listOfLeaveType.contains(new Long(2))) {
					if (mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveTypeId() == 2) {
						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver());
							totalLeaveCarriedOver += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned());
							totalLeavesEarned += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned();

						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility());
							totalTotalEligibility += mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed());
							totalNumberOfDaysAvailed += mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable());
							totalLeaveAvailable += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable();
						}
						cell.setCellStyle(cellStyle);
						break;
					}
					if (!mapOfEmpLvSumm.containsKey(new Long(2))) {
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						break;
					}
				}
			}
			/*
			 * for (Long keyLvTypeId1 : keysOfLvTypeIds1) {
			 * 
			 * if (listOfLeaveType.contains(new Long(7))) { if
			 * (mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveTypeId() == 7) { cell =
			 * row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getClosingBalance())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getPyLeaveEncashment())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getLeaveCarriedOver())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver());
			 * totalLeaveCarriedOver +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned()
			 * )) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned());
			 * totalLeavesEarned += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned();
			 * 
			 * } cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getFyLeaveEncashment())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getTotalEligibility())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility());
			 * totalTotalEligibility +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getNumberOfDaysAvailed())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed());
			 * totalNumberOfDaysAvailed +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getLeaveAvailable())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable());
			 * totalLeaveAvailable += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable();
			 * } cell.setCellStyle(cellStyle); break; } if(!mapOfEmpLvSumm.containsKey(new
			 * Long(7))) { cell = row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); break; } } }
			 */
			for (Long keyLvTypeId1 : keysOfLvTypeIds1) {
				if (listOfLeaveType.contains(new Long(11))) {
					if (mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveTypeId() == 11) {
						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver());
							totalLeaveCarriedOver += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned());
							totalLeavesEarned += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned();

						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment());
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility());
							totalTotalEligibility += mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed());
							totalNumberOfDaysAvailed += mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed();
						}
						cell.setCellStyle(cellStyle);

						cell = row.createCell(cellNo++);
						if (HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable())) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable());
							totalLeaveAvailable += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable();
						}
						cell.setCellStyle(cellStyle);
						break;
					}
					if (!mapOfEmpLvSumm.containsKey(new Long(11))) {
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						cell = row.createCell(cellNo++);
						cell.setCellValue("");
						break;
					}
				}
			}
			/*
			 * for (Long keyLvTypeId1 : keysOfLvTypeIds1) { if (listOfLeaveType.contains(new
			 * Long(5))) { if (mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveTypeId() == 5) {
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getClosingBalance())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getPyLeaveEncashment())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getLeaveCarriedOver())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver());
			 * totalLeaveCarriedOver +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned()
			 * )) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned());
			 * totalLeavesEarned += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned();
			 * 
			 * } cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getFyLeaveEncashment())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getTotalEligibility())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility());
			 * totalTotalEligibility +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getNumberOfDaysAvailed())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed());
			 * totalNumberOfDaysAvailed +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getLeaveAvailable())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable());
			 * totalLeaveAvailable += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable();
			 * } cell.setCellStyle(cellStyle); break; } if(!mapOfEmpLvSumm.containsKey(new
			 * Long(5))) { cell = row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); break; } } }
			 * 
			 * for (Long keyLvTypeId1 : keysOfLvTypeIds1) { if (listOfLeaveType.contains(new
			 * Long(12))) { if (mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveTypeId() == 12) {
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getClosingBalance())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getPyLeaveEncashment())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getLeaveCarriedOver())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver());
			 * totalLeaveCarriedOver +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned()
			 * )) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned());
			 * totalLeavesEarned += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned();
			 * 
			 * } cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getFyLeaveEncashment())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getTotalEligibility())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility());
			 * totalTotalEligibility +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getNumberOfDaysAvailed())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed());
			 * totalNumberOfDaysAvailed +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getLeaveAvailable())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable());
			 * totalLeaveAvailable += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable();
			 * } cell.setCellStyle(cellStyle); break; } if(!mapOfEmpLvSumm.containsKey(new
			 * Long(12))) { cell = row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); break; } } } for (Long
			 * keyLvTypeId1 : keysOfLvTypeIds1) { if (listOfLeaveType.contains(new Long(6)))
			 * { if (mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveTypeId() == 6) { cell =
			 * row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getClosingBalance())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getClosingBalance()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getPyLeaveEncashment())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getPyLeaveEncashment()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getLeaveCarriedOver())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver());
			 * totalLeaveCarriedOver +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveCarriedOver(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned()
			 * )) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned());
			 * totalLeavesEarned += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveEarned();
			 * 
			 * } cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getFyLeaveEncashment())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getFyLeaveEncashment()); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getTotalEligibility())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility());
			 * totalTotalEligibility +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getTotalEligibility(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getNumberOfDaysAvailed())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed());
			 * totalNumberOfDaysAvailed +=
			 * mapOfEmpLvSumm.get(keyLvTypeId1).getNumberOfDaysAvailed(); }
			 * cell.setCellStyle(cellStyle);
			 * 
			 * cell = row.createCell(cellNo++);
			 * if(HRMSHelper.isNullOrEmpty(mapOfEmpLvSumm.get(keyLvTypeId1).
			 * getLeaveAvailable())) { cell.setCellValue(""); }else{
			 * cell.setCellValue(mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable());
			 * totalLeaveAvailable += mapOfEmpLvSumm.get(keyLvTypeId1).getLeaveAvailable();
			 * } cell.setCellStyle(cellStyle); break; } if(!mapOfEmpLvSumm.containsKey(new
			 * Long(6))) { cell = row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); cell =
			 * row.createCell(cellNo++); cell.setCellValue(""); break; } } }
			 */
			cell = row.createCell(cellNo++);
			cell.setCellValue(totalLeaveCarriedOver);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(totalLeavesEarned);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(totalTotalEligibility);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(totalNumberOfDaysAvailed);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(totalLeaveAvailable);
			cell.setCellStyle(cellStyle);

		}

		// FileOutputStream fileOut = new FileOutputStream(excelFileName);

		// write this workbook to an Outputstream.
		// wb.write(fileOut);
		// fileOut.flush();
		// fileOut.close();
		return wb;
	}

}
