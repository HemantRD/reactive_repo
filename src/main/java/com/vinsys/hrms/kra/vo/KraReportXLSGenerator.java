package com.vinsys.hrms.kra.vo;//package com.vinsys.hrms.kra.vo;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.apache.poi.ss.usermodel.BorderStyle;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.FillPatternType;
//import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;
//import org.apache.poi.ss.usermodel.IndexedColors;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.VerticalAlignment;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//
///**
// * @author Onkar A
// *
// * 
// */
//
//public class KraReportXLSGenerator {
//
//	private XSSFWorkbook workbook;
//	private XSSFSheet sheet;
//	private XSSFCellStyle headerCellStyle;
//	private XSSFCellStyle border;
//
//	public KraReportXLSGenerator() {
//		workbook = new XSSFWorkbook();
//		sheet = workbook.createSheet("ExpenseSummary");
//		headerCellStyle = workbook.createCellStyle();
//		headerCellStyle.setBorderBottom(BorderStyle.THIN);
//		headerCellStyle.setBorderTop(BorderStyle.THIN);
//		headerCellStyle.setBorderRight(BorderStyle.THIN);
//		headerCellStyle.setBorderLeft(BorderStyle.THIN);
//		headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
//
//		Font boldFont = workbook.createFont();
//		headerCellStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());
//		headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
//		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//		boldFont.setBold(true);
//		headerCellStyle.setFont(boldFont);
//
//		// border
//		border = workbook.createCellStyle();
//		border.setBorderBottom(BorderStyle.THIN);
//		border.setBorderTop(BorderStyle.THIN);
//		border.setBorderRight(BorderStyle.THIN);
//		border.setBorderLeft(BorderStyle.THIN);
//		border.setVerticalAlignment(VerticalAlignment.CENTER);
//		border.setAlignment(HorizontalAlignment.CENTER);
//
//	}
//
//	public Workbook generateXlS(List<KraReportView> kraReports) throws IOException {
//		int rownum = 0;
//
//		Row header = sheet.createRow(rownum++);
//		setHeaderName(header);
//		setRowValue(kraReports, rownum);
//		for (int i = 0; i < 13; i++) {
//			sheet.autoSizeColumn(i);
//		}
//		return workbook;
//	}
//
//	private void setRowValue(List<KraReportView> kraReports, int rownum) {
//		CellStyle combinedStyle = workbook.createCellStyle();
//		combinedStyle.cloneStyleFrom(border);
//		combinedStyle.setWrapText(true);
//		int srNo = 1;
//		for (KraReportView kraReport : kraReports) {
//			Row row = sheet.createRow(rownum++);
//
//			Cell srNumber = row.createCell(0);
//			srNumber.setCellStyle(border);
//			srNumber.setCellValue(srNo);
//
//			Cell kra = row.createCell(1);
//			kra.setCellStyle(border);
//			kra.setCellValue(kraReport.getKraDetail());
//
//			Cell detailedDescription = row.createCell(2);
//			detailedDescription.setCellStyle(combinedStyle);
//			detailedDescription.setCellValue(kraReport.getDescription());
//			
//
//			Cell weightage = row.createCell(3);
//			weightage.setCellStyle(border);
//			weightage.setCellValue(kraReport.getWeightage());
//
//			Cell measurementCriteria = row.createCell(4);
//			measurementCriteria.setCellStyle(combinedStyle);
//			measurementCriteria.setCellValue(kraReport.getMeasurementCriteria());
//			
//			
//			Cell achievementPlan = row.createCell(5);
//			achievementPlan.setCellStyle(combinedStyle);
//			achievementPlan.setCellValue(kraReport.getAchievementPlan());
//			
//
//
//			Cell rmComment = row.createCell(6);
//			rmComment.setCellStyle(border);
//			rmComment.setCellValue(kraReport.getRmComment());
//
//			srNo++;
//		}
//	}
//
//	private CellStyle setWrapTextStyle(Cell cell, int textLength) {
//
//		CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
//		if (textLength > 200) {
//			//to wrap how many line
//			cell.getRow().setHeightInPoints(cell.getSheet().getDefaultRowHeightInPoints() * 2);
//			cellStyle.setWrapText(true);
//		}
//		return cellStyle;
//	}
//
//	private void setHeaderName(Row header) {
//		Cell requestIdHeader = header.createCell(0);
//		requestIdHeader.setCellStyle(headerCellStyle);
//		requestIdHeader.setCellValue("Sr.No");
//
//		Cell requesterNameHeader = header.createCell(1);
//		requesterNameHeader.setCellStyle(headerCellStyle);
//		requesterNameHeader.setCellValue("KRA");
//
//		Cell bpmNumberHeader = header.createCell(2);
//		bpmNumberHeader.setCellStyle(headerCellStyle);
//		bpmNumberHeader.setCellValue("Detailed Description");
//
//		Cell divisionHeader = header.createCell(3);
//		divisionHeader.setCellStyle(headerCellStyle);
//		divisionHeader.setCellValue("Weightage in %");
//
//		Cell departMentHeader = header.createCell(4);
//		departMentHeader.setCellStyle(headerCellStyle);
//		departMentHeader.setCellValue("Measurement Criteria");
//
//		Cell createdDateHeader = header.createCell(5);
//		createdDateHeader.setCellStyle(headerCellStyle);
//		createdDateHeader.setCellValue("Achievement Plan");
//
//		Cell approxCostHeader = header.createCell(6);
//		approxCostHeader.setCellStyle(headerCellStyle);
//		approxCostHeader.setCellValue("RM Commet");
//	}
//
//}
