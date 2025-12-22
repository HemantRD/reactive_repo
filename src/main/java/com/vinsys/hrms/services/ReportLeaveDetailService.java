package com.vinsys.hrms.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.vinsys.hrms.dao.IHRMSReportLeaveDetailDAO;
import com.vinsys.hrms.datamodel.VOLeaveReportParam;
import com.vinsys.hrms.entity.ReportLeaveDetail;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/reportLeaveDetails")

public class ReportLeaveDetailService {

	private static final Logger logger = LoggerFactory.getLogger(ReportLeaveDetailService.class);

	@Autowired
	IHRMSReportLeaveDetailDAO LeaveDetailReportDao;

	@RequestMapping(method = { RequestMethod.GET })
	@ResponseBody
	public void demoLeave(HttpServletResponse res) {
		try {
			List<Long> listOfLeaveType = new ArrayList<Long>();
			res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			res.setHeader("Content-Disposition", "attachment; filename=deployment-definitions.xlsx");
			VOLeaveReportParam param = new VOLeaveReportParam();
			param.setLeaveTypesStr("12,7,6,5,11,9,4,2,");
			List<ReportLeaveDetail> leaveDetails = LeaveDetailReportDao
					.findAll(SpecificationFactory.extendedLeaveReport(param));
			String[] leavetypes = param.getLeaveTypesStr().split(",");
			for (String lvTypeId : leavetypes) {
				listOfLeaveType.add(new Long(lvTypeId));
			}
			Workbook book = writeXLSXFile(leaveDetails, listOfLeaveType, param);
			book.write(res.getOutputStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	@ResponseBody
	public void fetchLeaveDetailReport(@RequestBody VOLeaveReportParam leaveReportParam, HttpServletResponse res) {
		logger.info("******************* Leave Details Report START *********************");
		List<ReportLeaveDetail> leaveDetails = LeaveDetailReportDao
				.findAll(SpecificationFactory.extendedLeaveReport(leaveReportParam));
		try {

			if (HRMSHelper.isNullOrEmpty(leaveDetails) || leaveDetails.size() == 0) {
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
					listOfLeaveType.add(new Long(lvTypeId));
				}
				Workbook workbook = writeXLSXFile(leaveDetails, listOfLeaveType, leaveReportParam);

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

	public static Workbook writeXLSXFile(List<ReportLeaveDetail> leaveDetails, List<Long> listOfLeaveType,
			VOLeaveReportParam param) throws IOException {

		// String excelFileName = "E:/input/Test.xlsx";// name of excel file

		String sheetName = "LeaveDetailReport";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		ArrayList<String> listOfMainHeader = new ArrayList<String>();
		for (int i = 1; i <= 11; i++) {
			listOfMainHeader.add("");
		}
		if (listOfLeaveType.contains(new Long(9))) {
			listOfMainHeader.add("Privilege Leaves");
			for (int j = 2; j <= 6; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(4))) {
			listOfMainHeader.add("Probationary Leaves");
			for (int j = 2; j <= 6; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(2))) {
			listOfMainHeader.add("Compensatory Leaves");
			for (int j = 2; j <= 6; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(7))) {
			listOfMainHeader.add("On Duty");
			for (int j = 2; j <= 6; j++) {
				listOfMainHeader.add("");
			}
		}
//		******************************************Update Sick Leave to Emergency Leave********************************************************
		if (listOfLeaveType.contains(new Long(11))) {
			listOfMainHeader.add("Emergency Leave");
			for (int j = 2; j <= 6; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(5))) {
			listOfMainHeader.add("LOP");
			for (int j = 2; j <= 6; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(12))) {
			listOfMainHeader.add("Work From Home");
			for (int j = 2; j <= 6; j++) {
				listOfMainHeader.add("");
			}
		}
		if (listOfLeaveType.contains(new Long(6))) {
			listOfMainHeader.add("Maternity Leave");
			for (int j = 2; j <= 6; j++) {
				listOfMainHeader.add("");
			}
			listOfMainHeader.add("");
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 20));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 20));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 10));
		sheet.addMergedRegion(new CellRangeAddress(2, // mention first row here
				2, // mention last row here, it is 1 as we are doing a column wise merging
				11, // mention first column of merging
				16 // mention last column to include in merge
		));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 17, 22));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 23, 28));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 29, 34));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 35, 40));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 41, 46));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 47, 52));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 53, 58));
		/*
		 * String[] subHeader = new String[] {
		 * "EmployeeID","EmployeeName","Designation","Branch","LeaveAppliedDate",
		 * "FromDate","ToDate",
		 * "ApproverNameId","CancelRequest","NoAction","Applied","Cancelled",
		 * "RO-Approved","Rejected","Approved",
		 * "ApproverNameId","CancelRequest","NoAction","Applied","Cancelled",
		 * "RO-Approved","Rejected","Approved",
		 * "ApproverNameId","CancelRequest","NoAction","Applied","Cancelled",
		 * "RO-Approved","Rejected","Approved",
		 * "ApproverNameId","CancelRequest","NoAction","Applied","Cancelled",
		 * "RO-Approved","Rejected","Approved",
		 * "ApproverNameId","CancelRequest","NoAction","Applied","Cancelled",
		 * "RO-Approved","Rejected","Approved",
		 * "ApproverNameId","CancelRequest","NoAction","Applied","Cancelled",
		 * "RO-Approved","Rejected","Approved",
		 * "ApproverNameId","CancelRequest","NoAction","Applied","Cancelled",
		 * "RO-Approved","Rejected","Approved"};
		 */

		List<String> tempListOfSubHeader = new ArrayList<String>();
		tempListOfSubHeader.add("   Approver Name - Id   ");
		tempListOfSubHeader.add("Cancel Request");
		// tempListOfSubHeader.add("NoAction");
		tempListOfSubHeader.add("Applied");
		tempListOfSubHeader.add("Cancelled");
		// tempListOfSubHeader.add("RO-Approved");
		tempListOfSubHeader.add("Rejected");
		tempListOfSubHeader.add("Approved");

		List<String> listOfsubHeader = new ArrayList<String>();
		listOfsubHeader.add("Employee ID");
		listOfsubHeader.add("  Employee Name   ");
		listOfsubHeader.add("  Designation   ");
		listOfsubHeader.add("  Department  ");
		listOfsubHeader.add("     Branch     ");
		listOfsubHeader.add("            Division            ");
		listOfsubHeader.add("Leave Applied Date");
		listOfsubHeader.add("  From Date  ");
		listOfsubHeader.add("   To Date   ");
		listOfsubHeader.add(" From Session ");
		listOfsubHeader.add("  To Session  ");

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
		// listOfsubHeader.add(" Total ");

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
		cell1.setCellValue("Employee Leave Application Status Report for the period From  " + param.getFromDate()
				+ " TO " + param.getToDate());
		cell1.setCellStyle(detailsStyle);

		row = sheet.createRow(1);
		cell1 = row.createCell(0);
		cell1.setCellValue("Report generated date : "
				+ HRMSDateUtil.format(HRMSDateUtil.getToday(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		cell1.setCellStyle(detailsStyle);

		row = sheet.createRow(2);

		for (int i = 0; i < listOfMainHeader.size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(listOfMainHeader.get(i));
			cell.setCellStyle(headerStyle);
		}
		row = sheet.createRow(3);
		for (int j = 0; j < listOfsubHeader.size(); j++) {
			Cell cell = row.createCell(j);
			cell.setCellValue(listOfsubHeader.get(j));
			cell.setCellStyle(headerStyle);
			sheet.setHorizontallyCenter(true);
			sheet.autoSizeColumn(j);
		}

		int rowNo = 3;
		for (int r = 0; r < leaveDetails.size(); r++) {

			if (!listOfLeaveType.contains(leaveDetails.get(r).getLeaveTypeId())) {
				continue;
			}
			++rowNo;
			row = sheet.createRow(rowNo);
			int cellNo = 0;
			Cell cell = row.createCell(cellNo++);
			cell.setCellValue(leaveDetails.get(r).getEmployeeId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(leaveDetails.get(r).getEmployeeName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(leaveDetails.get(r).getDesignationName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(leaveDetails.get(r).getDepartmentName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(leaveDetails.get(r).getBranchName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(leaveDetails.get(r).getDivisionName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(HRMSDateUtil.format(leaveDetails.get(r).getDateOfApplied(), "dd-MM-yyyy"));
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(HRMSDateUtil.format(leaveDetails.get(r).getFromDate(), "dd-MM-yyyy"));
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(HRMSDateUtil.format(leaveDetails.get(r).getToDate(), "dd-MM-yyyy"));
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(leaveDetails.get(r).getFromSession());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(leaveDetails.get(r).getToSession());
			cell.setCellStyle(cellStyle);

			// row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getEmployeeId());
			// row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getEmployeeName());
			// row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getDesignationName());
			// row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getDepartmentName());
			// row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getBranchName());
			// row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getDivisionName());
			// row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getDateOfApplied());
			// row.createCell(cellNo++).setCellValue(HRMSDateUtil.format(leaveDetails.get(r).getFromDate(),
			// "dd-MM-yyyy"));
			// row.createCell(cellNo++).setCellValue(HRMSDateUtil.format(leaveDetails.get(r).getToDate(),
			// "dd-MM-yyyy"));
			if (listOfLeaveType.contains(new Long(9))) {
				if (leaveDetails.get(r).getLeaveTypeId() == 9) {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					if (leaveDetails.get(r).getCancelRequest() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getCancelRequest()));
					}
					if (leaveDetails.get(r).getApplied() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getApplied()));
					}
					if (leaveDetails.get(r).getCancelled() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getCancelled());
					}
					if (leaveDetails.get(r).getRejected() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getRejected());
					}
					if (leaveDetails.get(r).getApproved() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getApproved());
					}
				} else {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
				}
			}
			if (listOfLeaveType.contains(new Long(4))) {
				if (leaveDetails.get(r).getLeaveTypeId() == 4) {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					if (leaveDetails.get(r).getCancelRequest() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getCancelRequest()));
					}
					if (leaveDetails.get(r).getApplied() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getApplied()));
					}
					if (leaveDetails.get(r).getCancelled() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getCancelled());
					}
					if (leaveDetails.get(r).getRejected() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getRejected());
					}
					if (leaveDetails.get(r).getApproved() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getApproved());
					}
				} else {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
				}
			}
			if (listOfLeaveType.contains(new Long(2))) {
				if (leaveDetails.get(r).getLeaveTypeId() == 2) {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					if (leaveDetails.get(r).getCancelRequest() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getCancelRequest()));
					}
					if (leaveDetails.get(r).getApplied() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getApplied()));
					}
					if (leaveDetails.get(r).getCancelled() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getCancelled());
					}
					if (leaveDetails.get(r).getRejected() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getRejected());
					}
					if (leaveDetails.get(r).getApproved() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getApproved());
					}
				} else {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
				}
			}
			if (listOfLeaveType.contains(new Long(7))) {
				if (leaveDetails.get(r).getLeaveTypeId() == 7) {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					if (leaveDetails.get(r).getCancelRequest() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getCancelRequest()));
					}
					if (leaveDetails.get(r).getApplied() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getApplied()));
					}
					if (leaveDetails.get(r).getCancelled() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getCancelled());
					}
					if (leaveDetails.get(r).getRejected() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getRejected());
					}
					if (leaveDetails.get(r).getApproved() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getApproved());
					}
				} else {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
				}
			}
			if (listOfLeaveType.contains(new Long(11))) {
				if (leaveDetails.get(r).getLeaveTypeId() == 11) {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					if (leaveDetails.get(r).getCancelRequest() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getCancelRequest()));
					}
					if (leaveDetails.get(r).getApplied() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getApplied()));
					}
					if (leaveDetails.get(r).getCancelled() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getCancelled());
					}
					if (leaveDetails.get(r).getRejected() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getRejected());
					}
					if (leaveDetails.get(r).getApproved() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getApproved());
					}
				} else {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
				}
			}
			if (listOfLeaveType.contains(new Long(5))) {
				if (leaveDetails.get(r).getLeaveTypeId() == 5) {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					if (leaveDetails.get(r).getCancelRequest() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getCancelRequest()));
					}
					if (leaveDetails.get(r).getApplied() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getApplied()));
					}
					if (leaveDetails.get(r).getCancelled() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getCancelled());
					}
					if (leaveDetails.get(r).getRejected() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getRejected());
					}
					if (leaveDetails.get(r).getApproved() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getApproved());
					}
				} else {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
				}
			}
			if (listOfLeaveType.contains(new Long(12))) {
				if (leaveDetails.get(r).getLeaveTypeId() == 12) {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					if (leaveDetails.get(r).getCancelRequest() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getCancelRequest()));
					}
					if (leaveDetails.get(r).getApplied() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getApplied()));
					}
					if (leaveDetails.get(r).getCancelled() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getCancelled());
					}
					if (leaveDetails.get(r).getRejected() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getRejected());
					}
					if (leaveDetails.get(r).getApproved() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getApproved());
					}
				} else {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
				}
			}
			if (listOfLeaveType.contains(new Long(6))) {
				if (leaveDetails.get(r).getLeaveTypeId() == 6) {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					if (leaveDetails.get(r).getCancelRequest() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getCancelRequest()));
					}
					if (leaveDetails.get(r).getApplied() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue((leaveDetails.get(r).getApplied()));
					}
					if (leaveDetails.get(r).getCancelled() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getCancelled());
					}
					if (leaveDetails.get(r).getRejected() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getRejected());
					}
					if (leaveDetails.get(r).getApproved() == 0) {
						row.createCell(cellNo++).setCellValue("");
					} else {
						row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getApproved());
					}
				} else {
					row.createCell(cellNo++).setCellValue(leaveDetails.get(r).getReportingManager());
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
					// row.createCell(cellNo++).setCellValue("");
				}
			}
			/*
			 * double rowiseCount = leaveDetails.get(r).getCancelRequest() +
			 * leaveDetails.get(r).getApplied() + leaveDetails.get(r).getCancelled() +
			 * leaveDetails.get(r).getRejected() + leaveDetails.get(r).getApproved();
			 * row.createCell(cellNo++).setCellValue(rowiseCount);
			 */
		}

		// FileOutputStream fileOut = new FileOutputStream(excelFileName);

		// write this workbook to an Outputstream.
		// wb.write(fileOut);
		// fileOut.flush();
		// fileOut.close();
		return wb;
	}

}
