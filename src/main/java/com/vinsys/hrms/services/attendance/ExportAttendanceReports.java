package com.vinsys.hrms.services.attendance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.datamodel.attendance.VOEmployeeAttendanceReport;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.util.HRMSAttendanceHelper;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/attendance")


public class ExportAttendanceReports {

	private static final Logger logger = LoggerFactory.getLogger(ExportAttendanceReports.class);

	@Autowired
	IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO;
	@Autowired
	HRMSAttendanceHelper attendanceHelper;
	
	
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, value="/empreport", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	@ResponseBody
	public void empAttendanceReport(@RequestBody VOEmployeeAttendanceReport employeeAttendanceReportParams, HttpServletResponse res) {
		try {
			
			/****************************Reprocess Attendance Data*****************************/
			try {
				attendanceHelper.UpdateProcessedData(new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getFromDate()),	
						new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getToDate()),
						employeeAttendanceReportParams.getOrgId(),employeeAttendanceReportParams.getEmployeeId());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			/*****************************************END**************************************/
			
			List<AttendanceProcessedData> attendanceProcessedData = new ArrayList<>();
			try {
				if (!HRMSHelper.isLongZero(employeeAttendanceReportParams.getEmployeeId()))
					attendanceProcessedData = attendanceProcessedDataDAO.findByDateOrgIdempId(employeeAttendanceReportParams.getEmployeeId(),
							new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getFromDate()),
							new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getToDate()),
							employeeAttendanceReportParams.getOrgId());
				else
					attendanceProcessedData = attendanceProcessedDataDAO.findByDateOrgId(
							new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getFromDate()),
							new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getToDate()),
							employeeAttendanceReportParams.getOrgId());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			res.setHeader("Content-Disposition", "attachment; filename=EmployeeAttendanceReport.xlsx");

			Workbook book = writeXLSXFile(attendanceProcessedData);
			book.write(res.getOutputStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
	//	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));

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

		/*row = sheet.createRow(2);
		cell1 = row.createCell(0);
		cell1.setCellValue("Employee ID: " + attendanceProcessedData.get(0).getEmpId());
		cell1.setCellStyle(detailsStyle);

		row = sheet.createRow(3);
		cell1 = row.createCell(0);
		cell1.setCellValue("Employee Name: " + attendanceProcessedData.get(0).getEmpName());
		cell1.setCellStyle(detailsStyle);*/

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
					&& !attendanceProcessedData.get(r).getStatus().trim().equals("WO") && !attendanceProcessedData.get(r).getStatus().trim().equals("H"))
				cell.setCellStyle(cellRedStyle);
			else
				cell.setCellStyle(cellStyle);
			
			
			

			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
		}
		return wb;
	}
}
