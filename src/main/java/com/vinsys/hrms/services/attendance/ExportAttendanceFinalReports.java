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

import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceSummaryDAO;
import com.vinsys.hrms.datamodel.attendance.VOEmployeeAttendanceReport;
import com.vinsys.hrms.entity.attendance.AttendanceSummary;
import com.vinsys.hrms.util.HRMSAttendanceHelper;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/attendance")

public class ExportAttendanceFinalReports {

	private static final Logger logger = LoggerFactory.getLogger(ExportAttendanceFinalReports.class);
//	private Map<Long, Employee> empAcnMap = new HashMap<Long, Employee>();

	@Autowired
	IHRMSAttendanceSummaryDAO attendanceSummaryDAO;

	@Autowired
	IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired	
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO holidayDAO;
	@Autowired
	IHRMSEmployeeAcnDAO employeeAcnDAO;
	@Autowired
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;
	@Autowired
	HRMSAttendanceHelper attendanceHelper;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, value = "/finalreport", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	@ResponseBody
	public void empAttendanceReport(@RequestBody VOEmployeeAttendanceReport employeeAttendanceReportParams,
			HttpServletResponse res)throws ParseException {
		try {
			try {
				attendanceHelper.UpdateProcessedData(
						new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getFromDate()),
						new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getToDate()),
						employeeAttendanceReportParams.getOrgId(), employeeAttendanceReportParams.getEmployeeId());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List<Object[]> attendanceSummaryObjectDataList = new ArrayList<>();
			try {
				if (!HRMSHelper.isLongZero(employeeAttendanceReportParams.getEmployeeId())) {
					attendanceSummaryObjectDataList = attendanceSummaryDAO.findByempIdDate(
							employeeAttendanceReportParams.getEmployeeId(),
							new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getFromDate()),
							new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getToDate()),
							employeeAttendanceReportParams.getOrgId());
				} else
					attendanceSummaryObjectDataList = attendanceSummaryDAO.findByDate(
							new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getFromDate()),
							new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getToDate()),
							employeeAttendanceReportParams.getOrgId());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		/*	List<Employee> employeeList = new ArrayList<>();
			employeeList = employeeDAO.findByIsActive("Y");

			List<Long> empDataList = new ArrayList<Long>();*/
			
			List<AttendanceSummary> attendanceSummaryList = new ArrayList<>();

			if (attendanceSummaryObjectDataList != null && !attendanceSummaryObjectDataList.isEmpty()) {
				for (Object[] resultSet : attendanceSummaryObjectDataList) {
					AttendanceSummary attendanceSummary = new AttendanceSummary();

					attendanceSummary.setEmpId(Long.parseLong(String.valueOf(resultSet[0])));
					attendanceSummary.setEmployeeAcn(String.valueOf(resultSet[1])!=null?String.valueOf(resultSet[1]):null);
					attendanceSummary.setEmpName(String.valueOf(resultSet[3])+" "+String.valueOf(resultSet[4]));
					attendanceSummary.setPresent(resultSet[10]!=null?resultSet[10].toString():null);
					attendanceSummary.setAbsent(resultSet[11]!=null?resultSet[11].toString():null);
					attendanceSummary.setWeeklyoff(resultSet[12]!=null?resultSet[12].toString():null);
					attendanceSummary.setHalfday(resultSet[13]!=null?resultSet[13].toString():null);
					attendanceSummary.setHoliday(resultSet[14]!=null?resultSet[14].toString():null);
					attendanceSummary.setDesignation(resultSet[6].toString());
					attendanceSummary.setDepartment(resultSet[5].toString());
					attendanceSummary.setBranch(resultSet[7].toString());
					attendanceSummary.setDivision(resultSet[8].toString());
					attendanceSummary.setOrgName(resultSet[9].toString());
					attendanceSummary.setOverTime(resultSet[15]!=null?resultSet[15].toString():null);
					attendanceSummary.setLopCount(resultSet[16]!=null?(Double.parseDouble(resultSet[16].toString())):0.0);
					//empDataList.add(attendanceSummary.getEmpId());
					attendanceSummaryList.add(attendanceSummary);
				}
			}

	/*		for (Employee emp : employeeList) {
				
				try {
					if(!empDataList.contains(emp.getId())) {
						logger.info("Missing Emp ID: "+emp.getId());
						
						AttendanceSummary attendanceSummary = new AttendanceSummary();

						attendanceSummary.setEmpId(emp.getId());
						attendanceSummary.setEmployeeAcn(emp.getEmployeeACN()!=null?emp.getEmployeeACN().toString():null);
						attendanceSummary.setEmpName(emp.getCandidate().getFirstName()+" "+emp.getCandidate().getLastName());
						attendanceSummary.setDesignation(emp.getCandidate().getCandidateProfessionalDetail().getDesignation().getDesignationName());
						attendanceSummary.setDepartment(emp.getCandidate().getCandidateProfessionalDetail().getDepartment().getDepartmentName());
						attendanceSummary.setBranch(emp.getCandidate().getCandidateProfessionalDetail().getBranch().getBranchName());
						attendanceSummary.setDivision(emp.getCandidate().getCandidateProfessionalDetail().getDivision().getDivisionName());
						attendanceSummary.setOrgName(emp.getCandidate().getLoginEntity().getOrganization().getOrganizationName());
					attendanceSummary.setPresent("NA");
						attendanceSummary.setAbsent("NA");
						attendanceSummary.setWeeklyoff("NA");
						attendanceSummary.setHalfday("NA");
						attendanceSummary.setHoliday("NA");
						attendanceSummary.setOverTime("NA");
						attendanceSummary.setLopCount("NA");

						attendanceSummaryList.add(attendanceSummary);
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			res.setHeader("Content-Disposition", "attachment; filename=FinalAttendanceReport.xlsx");

			Workbook book = writeXLSXFile(attendanceSummaryList, employeeAttendanceReportParams.getFromDate(),
					employeeAttendanceReportParams.getToDate());
			book.write(res.getOutputStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Workbook writeXLSXFile(List<AttendanceSummary> attendanceSummaryDataList, String fDate, String tDate) {
		// TODO Auto-generated method stub
		String sheetName = "FinalAttendanceReport";// name of sheet

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
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setFont(cellFont);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);

		CellStyle cellRedStyle = wb.createCellStyle();
		cellRedStyle.setAlignment(HorizontalAlignment.LEFT);
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
		// sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));

		XSSFRow row = sheet.createRow(0);
		Cell cell1 = row.createCell(0);
		cell1.setCellValue("Vinsys IT Services India Limited");
		cell1.setCellStyle(detailsStyle);

		row = sheet.createRow(1);
		cell1 = row.createCell(0);
		cell1.setCellValue("Attendance Summary Report from " + fDate + " to " + tDate);
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
		cell21.setCellValue("Sr.No.");
		cell21.setCellStyle(headerStyle);

		Cell cell22 = row.createCell(2);
		cell22.setCellValue("Emp Code");
		cell22.setCellStyle(headerStyle);

		Cell cell23 = row.createCell(3);
		cell23.setCellValue("Employee Name");
		cell23.setCellStyle(headerStyle);

		Cell cell24 = row.createCell(4);
		cell24.setCellValue("Division");
		cell24.setCellStyle(headerStyle);

		Cell cell25 = row.createCell(5);
		cell25.setCellValue(" Branch");
		cell25.setCellStyle(headerStyle);

		Cell cell26 = row.createCell(6);
		cell26.setCellValue(" Department");
		cell26.setCellStyle(headerStyle);

		Cell cell27 = row.createCell(7);
		cell27.setCellValue(" Absent(days)");
		cell27.setCellStyle(headerStyle);

		Cell cell28 = row.createCell(8);
		cell28.setCellValue("LOP Leaves");
		cell28.setCellStyle(headerStyle);

		Cell cell29 = row.createCell(9);
		cell29.setCellValue("Present Days(P+H+WO)");
		cell29.setCellStyle(headerStyle);
		
		Cell cell30 = row.createCell(10);
		cell30.setCellValue("Overtime(Days)");
		cell30.setCellStyle(headerStyle);
		
	/*	Cell cell31 = row.createCell(11);
		cell31.setCellValue("Overtime(DAYS:HOURS:MIN)");
		cell31.setCellStyle(headerStyle);*/

		/*
		 * Cell cell210 = row.createCell(10); cell210.setCellValue("Total Days");
		 * cell210.setCellStyle(headerStyle);
		 */

		int rowNo = 4;
		for (int r = 0; r < attendanceSummaryDataList.size(); r++) {

			float p, a, hd, wo, h, qd, qd3, ot, totalAttendance, totalAbsent;
			p = attendanceSummaryDataList.get(r).getPresent() != null
					? Float.parseFloat(attendanceSummaryDataList.get(r).getPresent())
					: 0;
			a = attendanceSummaryDataList.get(r).getAbsent() != null
					? Float.parseFloat(attendanceSummaryDataList.get(r).getAbsent())
					: 0;
			hd = attendanceSummaryDataList.get(r).getHalfday() != null
					? Float.parseFloat(attendanceSummaryDataList.get(r).getHalfday())
					: 0;
			wo = attendanceSummaryDataList.get(r).getWeeklyoff() != null
					? Float.parseFloat(attendanceSummaryDataList.get(r).getWeeklyoff())
					: 0;
			qd = attendanceSummaryDataList.get(r).getQuarterDay() != null
					? Float.parseFloat(attendanceSummaryDataList.get(r).getQuarterDay())
					: 0;
			qd3 = attendanceSummaryDataList.get(r).getOneThirdDay() != null
					? Float.parseFloat(attendanceSummaryDataList.get(r).getOneThirdDay())
					: 0;
			ot = attendanceSummaryDataList.get(r).getOverTime() != null
					? Float.parseFloat(attendanceSummaryDataList.get(r).getOverTime())
					: 0;
			h = attendanceSummaryDataList.get(r).getHoliday() != null
					? Float.parseFloat(attendanceSummaryDataList.get(r).getHoliday())
					: 0;

			p = p + (hd * 0.5f);
			p = p + (qd * 0.75f);
			p = p + (qd3 * 0.25f);

			hd = hd * 0.5f;
			qd3 = qd3 * .75f;
			qd = qd * 0.25f + qd3;

			totalAttendance = p + wo + hd + a + qd + h;
			totalAbsent = a + hd + qd;

			/*
			 * int totalDays=30; try { totalDays = (int) (((new
			 * SimpleDateFormat("yyyy-MM-dd").parse(tDate)).getTime() - (new
			 * SimpleDateFormat("yyyy-MM-dd").parse(fDate)).getTime())/(1000*60*60*24)); }
			 * catch (ParseException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */

			++rowNo;
			row = sheet.createRow(rowNo);
			int cellNo = 1;

			Cell cell = row.createCell(cellNo++);
			cell.setCellValue(r + 1);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(String.format("%04d", attendanceSummaryDataList.get(r).getEmpId()));
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceSummaryDataList.get(r).getEmpName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceSummaryDataList.get(r).getDivision());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceSummaryDataList.get(r).getBranch());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceSummaryDataList.get(r).getDepartment());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(cellNo++);
			cell.setCellValue(totalAbsent);
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(cellNo++);
			cell.setCellValue(attendanceSummaryDataList.get(r).getLopCount());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(totalAttendance - totalAbsent);
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(cellNo++);
			//cell.setCellValue(String.format("%.2f", ot));
			cell.setCellValue(String.format("%.1f",Double.parseDouble(String.format("%d.%d", (int)(ot/60),(int)ot%60))/9));
			cell.setCellStyle(cellStyle); 
			
			/*
			cell = row.createCell(cellNo++);
			cell.setCellValue(String.format("%d:%02d", (int)(ot/60),(int)ot%60));
			cell.setCellStyle(cellStyle); 
			*/
		/*	cell = row.createCell(cellNo++);
			cell.setCellValue(String.format("%02d:%02d:%02d",(int)ot/9/60 ,(int)ot/60%9, (int) ot%60));
			cell.setCellStyle(cellStyle); */
			
			/*
			 * cell = row.createCell(cellNo++); cell.setCellValue(totalDays);
			 * cell.setCellStyle(cellStyle);
			 */

		/*	sheet.setColumnWidth(1, 1500);
			sheet.setColumnWidth(2, 1500);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 100);
			sheet.setColumnWidth(7, 100);
			sheet.setColumnWidth(8, 100);*/
		}
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
	//	sheet.autoSizeColumn(11);
		return wb;
	}

}
