package com.vinsys.hrms.traveldesk.vo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vinsys.hrms.traveldesk.entity.TDExpenceSummaryReport;
import com.vinsys.hrms.util.EModeOfTravel;
import com.vinsys.hrms.util.ETravelDocumentFlow;
import com.vinsys.hrms.util.HRMSHelper;

/**
 * @author Onkar A
 *
 * 
 */

public class XLSGenerator {

	public XLSGenerator() {

	}

	public Workbook generateXlS(List<TDExpenceSummaryReport> travelRequest) throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("ExpenseSummary");
		XSSFCellStyle cellStyle = createCellStyle(workbook);

		// set date formate Style
		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		// dateFormateStyle = cellStyle;
		XSSFCellStyle dateFormateStyle = createDateStyle(workbook, createHelper);

		// border
		XSSFCellStyle border = createBorderStyle(workbook);

		createXLS(travelRequest, sheet, cellStyle, dateFormateStyle, border);
		createMergeCell(sheet, cellStyle);

		return workbook;
	}

	private void createMergeCell(XSSFSheet sheet, XSSFCellStyle cellStyle) {
	
		CellRangeAddress cellRangeAddress = null;
		List<CellRangeAddress> mergedRegions = new ArrayList<>();
		for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {

			if (j == 2) {
				continue; // Skip merging for column C1
			}

			int firstVal = 1;
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				if (sheet.getLastRowNum() > i
						&& sheet.getRow(i).getCell(j).toString().equals(sheet.getRow(i + 1).getCell(j).toString())) {
					// Cells match, update the range
					if (sheet.getRow(i + 1).getCell(0).toString().equals(sheet.getRow(i).getCell(0).toString())) {
						cellRangeAddress = new CellRangeAddress(firstVal, i + 1, j, j);
					} else {
						if (!HRMSHelper.isNullOrEmpty(cellRangeAddress)) {
							mergedRegions.add(cellRangeAddress);
							cellRangeAddress = null;
							firstVal = i + 1;
						}

					}
				} else if (sheet.getLastRowNum() >= i) {
					// Cells don't match, add the range to the list
					if (!HRMSHelper.isNullOrEmpty(cellRangeAddress)) {
						mergedRegions.add(cellRangeAddress);
						cellRangeAddress = null;
					}
					firstVal = i + 1;

				}
			}
		}

		// Add merged regions to the sheet
		for (CellRangeAddress mergedRegion : mergedRegions) {
			sheet.addMergedRegion(mergedRegion);

			// Set alignment for each cell within the merged region
			for (int rowIndex = mergedRegion.getFirstRow(); rowIndex <= mergedRegion.getLastRow(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row != null) {
					for (int columnIndex = mergedRegion.getFirstColumn(); columnIndex <= mergedRegion
							.getLastColumn(); columnIndex++) {
						Cell cell = row.getCell(columnIndex);
						if (cell == null) {
							cell = row.createCell(columnIndex);
						}
						// Set vertical alignment to middle
						CellStyle cellStyles = cell.getCellStyle();
						if (cellStyles == null) {
							cellStyles = sheet.getWorkbook().createCellStyle();
						}
						cellStyles.setVerticalAlignment(VerticalAlignment.CENTER);
						cellStyle.setAlignment(HorizontalAlignment.CENTER);
						cell.setCellStyle(cellStyles);
					}
				}
			}
		}
	}

	private XSSFCellStyle createBorderStyle(XSSFWorkbook workbook) {
		XSSFCellStyle border = workbook.createCellStyle();
		border.setBorderBottom(BorderStyle.THIN);
		border.setBorderTop(BorderStyle.THIN);
		border.setBorderRight(BorderStyle.THIN);
		border.setBorderLeft(BorderStyle.THIN);
		border.setVerticalAlignment(VerticalAlignment.CENTER);
		// Set alignment to center
		border.setAlignment(HorizontalAlignment.CENTER);
		return border;
	}

	private XSSFCellStyle createDateStyle(XSSFWorkbook workbook, XSSFCreationHelper createHelper) {
		XSSFCellStyle dateFormateStyle = workbook.createCellStyle();
		dateFormateStyle.setBorderBottom(BorderStyle.THIN);
		dateFormateStyle.setBorderTop(BorderStyle.THIN);
		dateFormateStyle.setBorderRight(BorderStyle.THIN);
		dateFormateStyle.setBorderLeft(BorderStyle.THIN);
		dateFormateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyy"));
		dateFormateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// Set alignment to center
		dateFormateStyle.setAlignment(HorizontalAlignment.CENTER);
		return dateFormateStyle;
	}

	private XSSFCellStyle createCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// Set alignment to center
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		Font boldFont = workbook.createFont();
		cellStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		boldFont.setBold(true);
		cellStyle.setFont(boldFont);
		return cellStyle;
	}

	private void createXLS(List<TDExpenceSummaryReport> travelRequest, XSSFSheet sheet, XSSFCellStyle cellStyle,
			XSSFCellStyle dateFormateStyle, XSSFCellStyle border) {
		int rownum = 0;
		Row header = sheet.createRow(rownum++);
		setHeader(cellStyle, header);
		setRowData(travelRequest, sheet, dateFormateStyle, border, rownum);
	}

	private void setRowData(List<TDExpenceSummaryReport> travelRequest, XSSFSheet sheet,
			XSSFCellStyle dateFormateStyle, XSSFCellStyle border, int rownum) {
		for (TDExpenceSummaryReport travellerRequest : travelRequest) {
			Row row = sheet.createRow(rownum++);

			Cell requestId = row.createCell(0);
			requestId.setCellStyle(border);
			requestId.setCellValue(travellerRequest.getTravelRequestId());

			Cell requesterName = row.createCell(1);
			requesterName.setCellStyle(border);
			requesterName.setCellValue(travellerRequest.getRequesterName());

			Cell createdDate = row.createCell(2);
			createdDate.setCellStyle(dateFormateStyle);
			createdDate.setCellValue(travellerRequest.getRequestCreatedDate());

			Cell requestType = row.createCell(3);
			requestType.setCellStyle(border);
			requestType.setCellValue(getRequestType(travellerRequest));

			Cell requestMode = row.createCell(4);
			requestMode.setCellStyle(border);
			requestMode.setCellValue(getRequestMode(travellerRequest));

			Cell travelerName = row.createCell(5);
			travelerName.setCellStyle(border);
			travelerName.setCellValue(travellerRequest.getTravellerName());

			Cell drivrName = row.createCell(6);
			drivrName.setCellStyle(border);
			drivrName.setCellValue(getDriverName(travellerRequest));

			Cell fromLocation = row.createCell(7);
			fromLocation.setCellStyle(border);
			fromLocation.setCellValue(getFromLocation(travellerRequest));

			Cell toLocation = row.createCell(8);
			toLocation.setCellStyle(border);
			toLocation.setCellValue(getToLocation(travellerRequest));

			Cell fromDate = row.createCell(9);
			fromDate.setCellStyle(dateFormateStyle);
			fromDate.setCellValue(getFromDate(travellerRequest));

			Cell toDate = row.createCell(10);
			toDate.setCellStyle(dateFormateStyle);
			toDate.setCellValue(getToDate(travellerRequest));

			Cell noOfRooms = row.createCell(11);
			noOfRooms.setCellStyle(border);
			noOfRooms.setCellValue(getNoOfRoom(travellerRequest));

			Cell bdName = row.createCell(12);
			bdName.setCellStyle(border);
			bdName.setCellValue(travellerRequest.getBdName());

			Cell bpmNo = row.createCell(13);
			bpmNo.setCellStyle(border);
			bpmNo.setCellValue(travellerRequest.getBpmNumber());

			Cell salesInvoice = row.createCell(14);
			salesInvoice.setCellStyle(border);
			salesInvoice.setCellValue(travellerRequest.getInvoiceNumber());

			Cell approxCost = row.createCell(15);
			approxCost.setCellStyle(border);
			approxCost.setCellValue(getApproximateCost(travellerRequest));

			Cell totalApproxCost = row.createCell(16);
			totalApproxCost.setCellStyle(border);
			totalApproxCost.setCellValue(!HRMSHelper.isNullOrEmpty(travellerRequest.getTotalApproximateCost())
					? travellerRequest.getTotalApproximateCost()
					: 0);

			Cell finalCost = row.createCell(17);
			finalCost.setCellStyle(border);
			finalCost.setCellValue(getFinalCost(travellerRequest));

			Cell totalFinalCost = row.createCell(18);
			totalFinalCost.setCellStyle(border);
			totalFinalCost.setCellValue(!HRMSHelper.isNullOrEmpty(travellerRequest.getTotalFinalCost())
					? travellerRequest.getTotalFinalCost()
					: 0);

			Cell refundCost = row.createCell(19);
			refundCost.setCellStyle(border);
			refundCost.setCellValue(!HRMSHelper.isNullOrEmpty(travellerRequest.getTotalRefundCost())
					? travellerRequest.getTotalRefundCost()
					: 0);

			Cell settledCost = row.createCell(20);
			settledCost.setCellStyle(border);
			settledCost.setCellValue(!HRMSHelper.isNullOrEmpty(travellerRequest.getTotalSettledCost())
					? travellerRequest.getTotalSettledCost()
					: 0);

			Cell currency = row.createCell(21);
			currency.setCellStyle(border);
			currency.setCellValue(travellerRequest.getCurrency());

			Cell approverName = row.createCell(22);
			approverName.setCellStyle(border);
			approverName.setCellValue(getRequestApproverName(travellerRequest));

			Cell status = row.createCell(23);
			status.setCellStyle(border);
			status.setCellValue(travellerRequest.getStatus());

		}
		for (int i = 0; i < 24; i++) {
			sheet.autoSizeColumn(i);
		}
	}

	private void setHeader(XSSFCellStyle cellStyle, Row header) {
		Cell requestIdHeader = header.createCell(0);
		requestIdHeader.setCellStyle(cellStyle);
		requestIdHeader.setCellValue("Request Id");

		Cell requesterNameHeader = header.createCell(1);
		requesterNameHeader.setCellStyle(cellStyle);
		requesterNameHeader.setCellValue("Requester Name");

		Cell createdDateHeader = header.createCell(2);
		createdDateHeader.setCellStyle(cellStyle);
		createdDateHeader.setCellValue("Request Date");

		Cell reqType = header.createCell(3);
		reqType.setCellStyle(cellStyle);
		reqType.setCellValue("Request Type");

		Cell reqMode = header.createCell(4);
		reqMode.setCellStyle(cellStyle);
		reqMode.setCellValue("Request Mode");

		Cell travellerName = header.createCell(5);
		travellerName.setCellStyle(cellStyle);
		travellerName.setCellValue("Traveller Name");

		Cell driverName = header.createCell(6);
		driverName.setCellStyle(cellStyle);
		driverName.setCellValue("Driver Name");

		Cell fromLocationHeader = header.createCell(7);
		fromLocationHeader.setCellStyle(cellStyle);
		fromLocationHeader.setCellValue("From Location");

		Cell toLocationHeader = header.createCell(8);
		toLocationHeader.setCellStyle(cellStyle);
		toLocationHeader.setCellValue("To Location");

		Cell fromDateHeader = header.createCell(9);
		fromDateHeader.setCellStyle(cellStyle);
		fromDateHeader.setCellValue("From Date");

		Cell toDateHeader = header.createCell(10);
		toDateHeader.setCellStyle(cellStyle);
		toDateHeader.setCellValue("To Date");

		Cell noOfRoomsHeader = header.createCell(11);
		noOfRoomsHeader.setCellStyle(cellStyle);
		noOfRoomsHeader.setCellValue("No of Rooms");

		Cell bdNameHeader = header.createCell(12);
		bdNameHeader.setCellStyle(cellStyle);
		bdNameHeader.setCellValue("BD Name");

		Cell bpmNumberHeader = header.createCell(13);
		bpmNumberHeader.setCellStyle(cellStyle);
		bpmNumberHeader.setCellValue("BPM No");

		Cell salesInvioceNoHeader = header.createCell(14);
		salesInvioceNoHeader.setCellStyle(cellStyle);
		salesInvioceNoHeader.setCellValue("Sales Invoice No");

		Cell approxCostHeader = header.createCell(15);
		approxCostHeader.setCellStyle(cellStyle);
		approxCostHeader.setCellValue("Approx Cost");

		Cell totalApproxCostHeader = header.createCell(16);
		totalApproxCostHeader.setCellStyle(cellStyle);
		totalApproxCostHeader.setCellValue("Total Approx Cost");

		Cell finalCostHeader = header.createCell(17);
		finalCostHeader.setCellStyle(cellStyle);
		finalCostHeader.setCellValue("Final Cost");

		Cell totalFinalCostHeader = header.createCell(18);
		totalFinalCostHeader.setCellStyle(cellStyle);
		totalFinalCostHeader.setCellValue("Total Final Cost");

		Cell refundCostHeader = header.createCell(19);
		refundCostHeader.setCellStyle(cellStyle);
		refundCostHeader.setCellValue("Refund Cost");

		Cell settledCostHeader = header.createCell(20);
		settledCostHeader.setCellStyle(cellStyle);
		settledCostHeader.setCellValue("Settled Cost");

		Cell currancy = header.createCell(21);
		currancy.setCellStyle(cellStyle);
		currancy.setCellValue("Currancy");

		Cell approvedByHeader = header.createCell(22);
		approvedByHeader.setCellStyle(cellStyle);
		approvedByHeader.setCellValue("Approved By");

		Cell statusHeader = header.createCell(23);
		statusHeader.setCellStyle(cellStyle);
		statusHeader.setCellValue("status");
	}

	private String getDriverName(TDExpenceSummaryReport travellerRequest) {

		if (!HRMSHelper.isNullOrEmpty(travellerRequest.getCabDriverFirstName())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getCabDriverLastName())) {
			return ((travellerRequest.getCabDriverFirstName()) + " " + (travellerRequest.getCabDriverLastName()));
		}

		return "NA";
	}

	private Long getFinalCost(TDExpenceSummaryReport travellerRequest) {

		if (!HRMSHelper.isNullOrEmpty(travellerRequest.getCabId())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getCabFinalCost())) {
			return (travellerRequest.getCabFinalCost());
		} else if (!HRMSHelper.isNullOrEmpty(travellerRequest.getTicketId())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getTicketFinalCost())) {
			return (travellerRequest.getTicketFinalCost());
		} else if (!HRMSHelper.isNullOrEmpty(travellerRequest.getAccId())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getAccFinalCost())) {
			return (travellerRequest.getAccFinalCost());
		} else {
			return 0L;
		}

	}

	private Long getApproximateCost(TDExpenceSummaryReport travellerRequest) {
		if (!HRMSHelper.isNullOrEmpty(travellerRequest.getCabId())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getCabApproxCost())) {
			return (travellerRequest.getCabApproxCost());
		} else if (!HRMSHelper.isNullOrEmpty(travellerRequest.getTicketId())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getTicketApproxCost())) {
			return (travellerRequest.getTicketApproxCost());
		} else if (!HRMSHelper.isNullOrEmpty(travellerRequest.getAccId())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getAccApproxCost())) {
			return (travellerRequest.getAccApproxCost());
		}
		return 0L;
	}

	private Long getNoOfRoom(TDExpenceSummaryReport travellerRequest) {
		if (!HRMSHelper.isNullOrEmpty(travellerRequest.getAccNoOfRooms())) {
			return (travellerRequest.getAccNoOfRooms());
		}
		return 0L;
	}

	private Date getToDate(TDExpenceSummaryReport travellerRequest) {
		if (Boolean.TRUE.equals(travellerRequest.getBookCab())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getCabReturnDate())) {
			return (travellerRequest.getCabReturnDate());
		} else if (Boolean.TRUE.equals(travellerRequest.getBookAccommodation())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getAccToDate())) {
			return (travellerRequest.getAccToDate());
		} else if (Boolean.TRUE.equals(travellerRequest.getBookTicket())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getTiketReturnDate())) {
			return (travellerRequest.getTiketReturnDate());
		}
		return null;
	}

	private Date getFromDate(TDExpenceSummaryReport travellerRequest) {
		if (Boolean.TRUE.equals(travellerRequest.getBookCab())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getCabDateOfJourney())) {
			return (travellerRequest.getCabDateOfJourney());
		} else if (Boolean.TRUE.equals(travellerRequest.getBookAccommodation())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getAccFromDate())) {
			return (travellerRequest.getAccFromDate());
		} else if (Boolean.TRUE.equals(travellerRequest.getBookTicket())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getTiketDateOfJourney())) {
			return (travellerRequest.getTiketDateOfJourney());
		}
		return null;
	}

	private String getToLocation(TDExpenceSummaryReport travellerRequest) {

		if (Boolean.TRUE.equals(travellerRequest.getBookCab())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getCabToLocation())) {
			return (travellerRequest.getCabToLocation());
		} else if (Boolean.TRUE.equals(travellerRequest.getBookAccommodation())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getAccLocation())) {
			return (travellerRequest.getAccLocation());
		} else if (Boolean.TRUE.equals(travellerRequest.getBookTicket())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getTiketToLocation())) {
			return (travellerRequest.getTiketToLocation());
		}

		return null;
	}

	private String getFromLocation(TDExpenceSummaryReport travellerRequest) {
		if (Boolean.TRUE.equals(travellerRequest.getBookCab())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getCabFromLocation())) {
			return (travellerRequest.getCabFromLocation());
		} else if (Boolean.TRUE.equals(travellerRequest.getBookAccommodation())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getAccLocation())) {
			return (travellerRequest.getAccLocation());
		} else if (Boolean.TRUE.equals(travellerRequest.getBookTicket())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getTiketFromLocation())) {
			return (travellerRequest.getTiketFromLocation());
		}
		return null;
	}

	private String getRequestApproverName(TDExpenceSummaryReport travellerRequest) {
		if (!HRMSHelper.isNullOrEmpty(travellerRequest.getApproverFirstName())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getApproverLastName())) {
			return (travellerRequest.getApproverFirstName() + " " + travellerRequest.getApproverLastName());
		}
		return null;
	}

	private String getRequestMode(TDExpenceSummaryReport travellerRequest) {
		if (!HRMSHelper.isNullOrEmpty(travellerRequest.getBookTicket())) {
			if (!HRMSHelper.isNullOrEmpty(travellerRequest.getModeOfTravel())
					&& travellerRequest.getModeOfTravel().equalsIgnoreCase(EModeOfTravel.Air.name())) {
				return EModeOfTravel.Air.name();
			} else if (!HRMSHelper.isNullOrEmpty(travellerRequest.getModeOfTravel())
					&& travellerRequest.getModeOfTravel().equalsIgnoreCase(EModeOfTravel.Bus.name())) {
				return EModeOfTravel.Bus.name();
			} else if (!HRMSHelper.isNullOrEmpty(travellerRequest.getModeOfTravel())
					&& travellerRequest.getModeOfTravel().equalsIgnoreCase(EModeOfTravel.Train.name())) {
				return EModeOfTravel.Train.name();
			} else if (!HRMSHelper.isNullOrEmpty(travellerRequest.getCabMode())) {
				return travellerRequest.getCabMode();
			}
		} else {
			return null;
		}

		return null;
	}

	private String getRequestType(TDExpenceSummaryReport travellerRequest) {
		String result = "";
		if (Boolean.TRUE.equals(travellerRequest.getBookCab())
				&& !HRMSHelper.isNullOrEmpty(travellerRequest.getCabId())) {
			result = ETravelDocumentFlow.Cab.name();
		} else if (travellerRequest.getBookAccommodation() && !HRMSHelper.isNullOrEmpty(travellerRequest.getAccId())) {
			result = ETravelDocumentFlow.Accommodation.name();
		} else if (travellerRequest.getBookTicket() && !HRMSHelper.isNullOrEmpty(travellerRequest.getTicketId())) {
			result = ETravelDocumentFlow.Ticket.name();
		}
		return result;
	}

}
