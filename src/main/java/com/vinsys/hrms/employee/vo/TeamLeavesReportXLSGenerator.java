package com.vinsys.hrms.employee.vo;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

/**
 * @author Onkar A
 *
 * 
 */

public class TeamLeavesReportXLSGenerator {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFCellStyle headerStyle;
	private XSSFCellStyle border;

	public TeamLeavesReportXLSGenerator() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("TeamLeaveReport");
		Font detailsFont = workbook.createFont();
		detailsFont.setBold(true);
		detailsFont.setFontHeightInPoints((short) 11);

		CellStyle detailsStyle = workbook.createCellStyle();
		detailsStyle.setAlignment(HorizontalAlignment.LEFT);
		detailsStyle.setFont(detailsFont);

		Font font = workbook.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontHeightInPoints((short) 11);
		headerStyle = workbook.createCellStyle();
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.MAROON.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFont(font);

		Font cellFont = workbook.createFont();
		cellFont.setFontHeightInPoints((short) 10);

		// border
		border = workbook.createCellStyle();
		border.setBorderBottom(BorderStyle.THIN);
		border.setBorderTop(BorderStyle.THIN);
		border.setBorderRight(BorderStyle.THIN);
		border.setBorderLeft(BorderStyle.THIN);
		border.setFont(cellFont);

	}

	public Workbook generateXlS(List<EmployeeLeaveApplied> appliedLeaves) throws IOException {
		int rownum = 0;
		int srNo = 1;

		Row header = sheet.createRow(rownum++);

		setHeaderName(header);
		setRowValue(appliedLeaves, rownum, srNo);

		for (int i = 0; i < 13; i++) {
			sheet.autoSizeColumn(i);
		}

		return workbook;
	}

	private void setRowValue(List<EmployeeLeaveApplied> appliedLeaves, int rownum, int srNo) {
		for (EmployeeLeaveApplied appliedLeave : appliedLeaves) {
			Row row = sheet.createRow(rownum++);

			Cell srNO = row.createCell(0);
			srNO.setCellValue(srNo);
			srNO.setCellStyle(border);

			Cell employeeId = row.createCell(1);
			employeeId.setCellValue(appliedLeave.getEmployee().getId());
			employeeId.setCellStyle(border);

			Cell name = row.createCell(2);
			name.setCellValue(appliedLeave.getEmployee().getCandidate().getFirstName() + " "
					+ appliedLeave.getEmployee().getCandidate().getLastName());
			name.setCellStyle(border);

			Cell leaveType = row.createCell(3);
			leaveType.setCellValue(appliedLeave.getMasterLeaveType().getLeaveTypeName());
			leaveType.setCellStyle(border);

			Cell appliedDate = row.createCell(4);
			if (!HRMSHelper.isNullOrEmpty(appliedLeave.getDateOfApplied())) {
				appliedDate.setCellValue(
						HRMSDateUtil.format(appliedLeave.getDateOfApplied(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			}
			appliedDate.setCellStyle(border);

			Cell fromDate = row.createCell(5);
			if (!HRMSHelper.isNullOrEmpty(appliedLeave.getFromDate())) {
				fromDate.setCellValue(
						HRMSDateUtil.format(appliedLeave.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			}
			fromDate.setCellStyle(border);

			Cell toDate = row.createCell(6);
			if (!HRMSHelper.isNullOrEmpty(appliedLeave.getToDate())) {
				toDate.setCellValue(
						HRMSDateUtil.format(appliedLeave.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			}
			toDate.setCellStyle(border);

			Cell noOfdays = row.createCell(7);
			noOfdays.setCellValue(
					!HRMSHelper.isNullOrEmpty(appliedLeave.getNoOfDays()) ? appliedLeave.getNoOfDays() : 0.0f);
			noOfdays.setCellStyle(border);

			Cell reason = row.createCell(8);
			reason.setCellValue(
					!HRMSHelper.isNullOrEmpty(appliedLeave.getReasonForApply()) ? appliedLeave.getReasonForApply()
							: "");
			reason.setCellStyle(border);

			Cell status = row.createCell(9);
			status.setCellValue(
					!HRMSHelper.isNullOrEmpty(appliedLeave.getLeaveStatus()) ? appliedLeave.getLeaveStatus() : "");
			status.setCellStyle(border);

			srNo++;
		}
	}

	private void setHeaderName(Row row) {
		Cell srNO = row.createCell(0);
		srNO.setCellValue("Sr No");
		srNO.setCellStyle(headerStyle);

		Cell empId = row.createCell(1);
		empId.setCellValue("Employee Id");
		empId.setCellStyle(headerStyle);

		Cell empName = row.createCell(2);
		empName.setCellValue("Employee Name");
		empName.setCellStyle(headerStyle);

		Cell leaveType = row.createCell(3);
		leaveType.setCellValue("Leave Type");
		leaveType.setCellStyle(headerStyle);

		Cell appliedDate = row.createCell(4);
		appliedDate.setCellValue("Applied Date");
		appliedDate.setCellStyle(headerStyle);

		Cell fromDate = row.createCell(5);
		fromDate.setCellValue("From Date");
		fromDate.setCellStyle(headerStyle);

		Cell toDate = row.createCell(6);
		toDate.setCellValue("To Date");
		toDate.setCellStyle(headerStyle);

		Cell days = row.createCell(7);
		days.setCellValue("Days");
		days.setCellStyle(headerStyle);

		Cell reason = row.createCell(8);
		reason.setCellValue("Reason");
		reason.setCellStyle(headerStyle);

		Cell status = row.createCell(9);
		status.setCellValue("Status");
		status.setCellStyle(headerStyle);
	}

}
