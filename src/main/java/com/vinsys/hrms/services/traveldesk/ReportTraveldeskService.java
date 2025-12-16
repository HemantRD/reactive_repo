package com.vinsys.hrms.services.traveldesk;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.criteriaBuilder.SpecificationFactory;
import com.vinsys.hrms.dao.traveldesk.IHRMSAccommodationGuestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSAccommodationRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRequestPassengerDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSReportAccommodationRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSReportCabRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSReportTicketRequestDao;
import com.vinsys.hrms.dao.traveldesk.IHRMSReportTraveldeskDetailDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSReportWonToCostDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTicketRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTicketRequestPassengerDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTravelRequestDAO;
import com.vinsys.hrms.dao.traveldesk.VOReportWonToCostResponse;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.traveldesk.VOReportAccommodationRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOReportCabRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOReportFilterTraveldesk;
import com.vinsys.hrms.datamodel.traveldesk.VOReportTicketRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOReportTraveldeskDetail;
import com.vinsys.hrms.datamodel.traveldesk.VOReportTraveldeskSummary;
import com.vinsys.hrms.datamodel.traveldesk.VOReportWonToCost;
import com.vinsys.hrms.entity.traveldesk.AccommodationGuest;
import com.vinsys.hrms.entity.traveldesk.AccommodationRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequest;
import com.vinsys.hrms.entity.traveldesk.ReportAccommodationRequest;
import com.vinsys.hrms.entity.traveldesk.ReportCabRequest;
import com.vinsys.hrms.entity.traveldesk.ReportTicketRequest;
import com.vinsys.hrms.entity.traveldesk.ReportTraveldeskDetail;
import com.vinsys.hrms.entity.traveldesk.ReportWonToCost;
import com.vinsys.hrms.entity.traveldesk.TicketRequest;
import com.vinsys.hrms.entity.traveldesk.TicketRequestPassenger;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/reportTravelDesk")

public class ReportTraveldeskService {

	private static final Logger logger = LoggerFactory.getLogger(ReportTraveldeskService.class);

	@Autowired
	IHRMSReportTicketRequestDao reportTicketRequestDAO;

	@Autowired
	IHRMSTicketRequestPassengerDAO ticketPassengerDetailDAO;

	@Autowired
	IHRMSReportAccommodationRequestDAO reportAccommodationRequestDAO;

	@Autowired
	IHRMSAccommodationGuestDAO accommodationGuestDetailDAO;

	@Autowired
	IHRMSReportCabRequestDAO reportCabRequestDAO;

	@Autowired
	IHRMSCabRequestPassengerDAO cabPassengerDetailDAO;

	@Autowired
	IHRMSCabRequestDAO cabRequestDAO;

	@Autowired
	IHRMSTicketRequestDAO ticketRequestDAO;

	@Autowired
	IHRMSAccommodationRequestDAO accomodationRequestDAO;

	@Autowired
	IHRMSTravelRequestDAO travelRequestDao;

	@Autowired
	IHRMSReportWonToCostDAO wonToCostDAO;

	@Autowired
	IHRMSReportTraveldeskDetailDAO traveldeskDetailDAO;

	@RequestMapping(value = "/ticketReport", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String fetchTicketReport(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk)
			throws IOException, URISyntaxException {

		logger.info("*******************  Ticket Report UI START *********************");

		try {
			if (!HRMSHelper.isNullOrEmpty(reportFilterTraveldesk)) {

				List<VOReportTicketRequest> voTicketRequestList = new ArrayList<>();

				voTicketRequestList = getDataForTicketReport(reportFilterTraveldesk);
				if (!HRMSHelper.isNullOrEmpty(voTicketRequestList)) {

					HRMSListResponseObject responseObject = new HRMSListResponseObject();
					List<Object> objectList = new ArrayList<Object>(voTicketRequestList);
					responseObject.setResponseCode(IHRMSConstants.successCode);
					responseObject.setResponseMessage(IHRMSConstants.successMessage);
					responseObject.setListResponse(objectList);

					return HRMSHelper.createJsonString(responseObject);

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		logger.info("******************* Ticket UI Report END *********************");
		return null;
	}

	@RequestMapping(value = "/ticketReportExcel", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void fetchTicketReportExcel(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk,
			HttpServletResponse res) throws IOException, URISyntaxException {

		logger.info("******************* Ticket Report Excel START *********************");

		try {
			List<VOReportTicketRequest> voReportTicketRequestList = new ArrayList<>();
			voReportTicketRequestList = getDataForTicketReport(reportFilterTraveldesk);
			if (!HRMSHelper.isNullOrEmpty(voReportTicketRequestList)) {
				Workbook workbook = writeXLSXFileTicketRequestReport(voReportTicketRequestList);
				res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				res.setHeader("Content-Disposition", "attachment; filename=Leave_Details_Report.xlsx");
				workbook.write(res.getOutputStream());

			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
		logger.info("******************* Ticket Report Excel END *********************");
	}

	public static Workbook writeXLSXFileTicketRequestReport(List<VOReportTicketRequest> reportTicketRequestList)
			throws IOException {

		String sheetName = "Ticket_Request_Report";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		ArrayList<String> listOfMainHeader = new ArrayList<String>();

		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 30 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 30 * 256);
		sheet.setColumnWidth(5, 30 * 256);
		sheet.setColumnWidth(6, 30 * 256);
		sheet.setColumnWidth(7, 30 * 256);
		sheet.setColumnWidth(8, 30 * 256);
		sheet.setColumnWidth(9, 30 * 256);
		sheet.setColumnWidth(10, 30 * 256);
		sheet.setColumnWidth(11, 30 * 256);
		sheet.setColumnWidth(12, 30 * 256);
		sheet.setColumnWidth(13, 30 * 256);
		sheet.setColumnWidth(14, 30 * 256);
		sheet.setColumnWidth(15, 30 * 256);
		sheet.setColumnWidth(16, 30 * 256);
		sheet.setColumnWidth(17, 30 * 256);
		sheet.setColumnWidth(18, 30 * 256);

		listOfMainHeader.add("SR.NO.");
		listOfMainHeader.add("Requested By");
		listOfMainHeader.add("Requested Date");
		listOfMainHeader.add("Travel Request Id");
		listOfMainHeader.add("Ticket Request Id");
		listOfMainHeader.add("WON");
		listOfMainHeader.add("Name");
		listOfMainHeader.add("From Location");
		listOfMainHeader.add("To Location");
		listOfMainHeader.add("Approvel Required");
		listOfMainHeader.add("Approver");
		listOfMainHeader.add("Approver Status");
		listOfMainHeader.add("Chargeable To Client");
		listOfMainHeader.add("Mode Of Travel");
		listOfMainHeader.add("No Of Traveller");
		listOfMainHeader.add("Round Trip");
		listOfMainHeader.add("Travel Request Status");
		listOfMainHeader.add("Ticket Request Status");
		listOfMainHeader.add("Total Ticket Fare");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

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
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font foorterFont = wb.createFont();
		foorterFont.setBold(true);
		foorterFont.setFontHeightInPoints((short) 11);
		CellStyle footerStyle = wb.createCellStyle();
		// footerStyle.setBorderLeft(BorderStyle.THIN);
		// footerStyle.setBorderRight(BorderStyle.THIN);
		footerStyle.setBorderTop(BorderStyle.THIN);
		// footerStyle.setBorderBottom(BorderStyle.THIN);
		footerStyle.setAlignment(HorizontalAlignment.CENTER);
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = wb.createCellStyle();
		// cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		// cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
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

		int rowNo = 0;
		int cellNo = 0;
		Cell cell = null;
		int totalRsTaken = 0;
		int totalRsReturn = 0;

		for (VOReportTicketRequest ticketReq : reportTicketRequestList) {

			++rowNo;
			cellNo = 0;
			row = sheet.createRow(rowNo + 1);

			cell = row.createCell(cellNo++);
			cell.setCellValue(rowNo);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getRequestedBy());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getCreatedDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getTravelRequestId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getTicketRequestId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getWon());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getPassengerName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getFromLocation());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getToLocation());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getApprovalRequired());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getApprover());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getApproverStatus());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getChargeableToClient());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getModeOfTravel());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getNoOfTraveller());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getRoundTrip());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getTravelStatus());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getTicketRequestStatus());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(ticketReq.getTotalTicketFare());
			cell.setCellStyle(cellStyle);
		}
		return wb;
	}

	public List<VOReportTicketRequest> getDataForTicketReport(VOReportFilterTraveldesk filter) {

		List<VOReportTicketRequest> voTicketRequestList = new ArrayList<>();
		List<ReportTicketRequest> reportTicketRequestList = reportTicketRequestDAO
				.findAll(SpecificationFactory.extendedTicketRequestReport(filter));

		ListIterator<ReportTicketRequest> iter = reportTicketRequestList.listIterator();
		while (iter.hasNext()) {
			ReportTicketRequest ticketReq = iter.next();
			List<TicketRequestPassenger> passengerDetailList = ticketPassengerDetailDAO
					.findByTicketRequestId(ticketReq.getTicketRequestId(), IHRMSConstants.isActive);
			String passengerNames = "";
			for (TicketRequestPassenger ticketPassenger : passengerDetailList) {
				passengerNames += ticketPassenger.getPassengerName() + " / ";
			}
			passengerNames = passengerNames.substring(0, passengerNames.length() - 3);
			ticketReq.setPassengerName(passengerNames);
		}

		if (!HRMSHelper.isNullOrEmpty(reportTicketRequestList)) {
			for (ReportTicketRequest reportTicketRequest : reportTicketRequestList) {
				voTicketRequestList.add(HRMSResponseTranslator.translateToReportTicketRequestVo(reportTicketRequest));
			}
		}
		return voTicketRequestList;
	}

// ====================================================================================================================//

	@RequestMapping(value = "/accommodationReport", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String fetchAccommodationReport(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk)
			throws IOException, URISyntaxException {

		logger.info("*******************  Accommodation Report UI START *********************");

		try {
			if (!HRMSHelper.isNullOrEmpty(reportFilterTraveldesk)) {

				List<VOReportAccommodationRequest> voAccommodationRequestList = new ArrayList<>();

				voAccommodationRequestList = getDataForAccommodationReport(reportFilterTraveldesk);
				if (!HRMSHelper.isNullOrEmpty(voAccommodationRequestList)) {

					HRMSListResponseObject responseObject = new HRMSListResponseObject();
					List<Object> objectList = new ArrayList<Object>(voAccommodationRequestList);
					responseObject.setResponseCode(IHRMSConstants.successCode);
					responseObject.setResponseMessage(IHRMSConstants.successMessage);
					responseObject.setListResponse(objectList);

					return HRMSHelper.createJsonString(responseObject);

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		logger.info("******************* Accommodation Report UI END *********************");
		return null;
	}

	@RequestMapping(value = "/accommodationReportExcel", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void fetchAccommodationReportExcel(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk,
			HttpServletResponse res) throws IOException, URISyntaxException {

		logger.info("******************* Accommodation Report Excel START *********************");

		try {
			List<VOReportAccommodationRequest> voReportAccommodationRequestList = new ArrayList<>();
			voReportAccommodationRequestList = getDataForAccommodationReport(reportFilterTraveldesk);
			if (!HRMSHelper.isNullOrEmpty(voReportAccommodationRequestList)) {
				Workbook workbook = writeXLSXFileAccommodationRequestReport(voReportAccommodationRequestList);
				res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				res.setHeader("Content-Disposition", "attachment; filename=Leave_Details_Report.xlsx");
				workbook.write(res.getOutputStream());

			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
		logger.info("******************* Ticket Report Excel END *********************");
	}

	public static Workbook writeXLSXFileAccommodationRequestReport(
			List<VOReportAccommodationRequest> reportAccommodationRequestList) throws IOException {

		String sheetName = "Accommodation_Request_Report";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		ArrayList<String> listOfMainHeader = new ArrayList<String>();

		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 30 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 30 * 256);
		sheet.setColumnWidth(5, 30 * 256);
		sheet.setColumnWidth(6, 30 * 256);
		sheet.setColumnWidth(7, 30 * 256);
		sheet.setColumnWidth(8, 30 * 256);
		sheet.setColumnWidth(9, 30 * 256);
		sheet.setColumnWidth(10, 30 * 256);
		sheet.setColumnWidth(11, 30 * 256);
		sheet.setColumnWidth(12, 30 * 256);
		sheet.setColumnWidth(13, 30 * 256);
		sheet.setColumnWidth(14, 30 * 256);
		sheet.setColumnWidth(15, 30 * 256);
		sheet.setColumnWidth(16, 30 * 256);

		listOfMainHeader.add("SR.NO.");
		listOfMainHeader.add("Requested By");
		listOfMainHeader.add("Requested Date");
		listOfMainHeader.add("Travel Request Id");
		listOfMainHeader.add("Accommodation Request Id");
		listOfMainHeader.add("WON");
		listOfMainHeader.add("Name");
		listOfMainHeader.add("From Date");
		listOfMainHeader.add("To Date");
		listOfMainHeader.add("Approval Required");
		listOfMainHeader.add("Approver");
		listOfMainHeader.add("Approver Status");
		listOfMainHeader.add("Chargeable To Client");
		listOfMainHeader.add("No Of People");
		listOfMainHeader.add("Travel Request Status");
		listOfMainHeader.add("Accommodation Request Status");
		listOfMainHeader.add("Total Accommodation Cost");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

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
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font foorterFont = wb.createFont();
		foorterFont.setBold(true);
		foorterFont.setFontHeightInPoints((short) 11);
		CellStyle footerStyle = wb.createCellStyle();
		// footerStyle.setBorderLeft(BorderStyle.THIN);
		// footerStyle.setBorderRight(BorderStyle.THIN);
		footerStyle.setBorderTop(BorderStyle.THIN);
		// footerStyle.setBorderBottom(BorderStyle.THIN);
		footerStyle.setAlignment(HorizontalAlignment.CENTER);
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = wb.createCellStyle();
		// cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		// cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
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

		int rowNo = 0;
		int cellNo = 0;
		Cell cell = null;
		int totalRsTaken = 0;
		int totalRsReturn = 0;

		for (VOReportAccommodationRequest accoReq : reportAccommodationRequestList) {

			++rowNo;
			cellNo = 0;
			row = sheet.createRow(rowNo + 1);

			cell = row.createCell(cellNo++);
			cell.setCellValue(rowNo);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getRequestedBy());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getCreatedDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getTravelRequestId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getAccommodationRequestId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getWon());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getGuestName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getFromDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getToDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getApprovalRequired());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getApprover());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getApproverStatus());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getChargeableToClient());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getNoOfPeople());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getTravelRequestStatus());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getAccommodationRequestStatus());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(accoReq.getTotalAccommodationCost());
			cell.setCellStyle(cellStyle);

		}
		return wb;
	}

	public List<VOReportAccommodationRequest> getDataForAccommodationReport(VOReportFilterTraveldesk filter) {

		List<VOReportAccommodationRequest> voAccommodationRequestList = new ArrayList<>();
		// List<ReportAccommodationRequest> reportAccommodationRequestList =
		// reportAccommodationRequestDAO.findAll(Specifications.where(SpecificationFactory.extendedAccomodationRequestReportFromDate(filter)).or(Specifications.where(SpecificationFactory.extendedAccomodationRequestReportToDate(filter))));
		List<ReportAccommodationRequest> reportAccommodationRequestList = reportAccommodationRequestDAO
				.findAll(Specification.where(SpecificationFactory.extendedAccomodationRequestReport(filter)));
		// List<ReportAccommodationRequest> reportAccommodationRequestList =
		// reportAccommodationRequestDAO.findAll();

		ListIterator<ReportAccommodationRequest> iter = reportAccommodationRequestList.listIterator();
		while (iter.hasNext()) {
			ReportAccommodationRequest accoReq = iter.next();
			List<AccommodationGuest> guestDetailList = accommodationGuestDetailDAO
					.findByAccomodationReq(accoReq.getAccommodationRequestId(), IHRMSConstants.isActive);
			String guestNames = "";
			for (AccommodationGuest guest : guestDetailList) {
				guestNames += guest.getPassengerName() + " / ";
			}
			guestNames = guestNames.substring(0, guestNames.length() - 3);
			accoReq.setGuestName(guestNames);
		}

		if (!HRMSHelper.isNullOrEmpty(reportAccommodationRequestList)) {
			for (ReportAccommodationRequest reportAccomodatioRequest : reportAccommodationRequestList) {
				voAccommodationRequestList
						.add(HRMSResponseTranslator.translateToReportAccommodationRequestVo(reportAccomodatioRequest));
			}
		}
		return voAccommodationRequestList;
	}

	// ====================================================================================================================//

	@RequestMapping(value = "/cabReport", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String fetchCabReport(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk)
			throws IOException, URISyntaxException {

		logger.info("******************* Cab Report UI START *********************");

		try {
			if (!HRMSHelper.isNullOrEmpty(reportFilterTraveldesk)) {

				List<VOReportCabRequest> voCabRequestList = new ArrayList<>();

				voCabRequestList = getDataForCabReport(reportFilterTraveldesk);
				if (!HRMSHelper.isNullOrEmpty(voCabRequestList)) {

					HRMSListResponseObject responseObject = new HRMSListResponseObject();
					List<Object> objectList = new ArrayList<Object>(voCabRequestList);
					responseObject.setResponseCode(IHRMSConstants.successCode);
					responseObject.setResponseMessage(IHRMSConstants.successMessage);
					responseObject.setListResponse(objectList);

					return HRMSHelper.createJsonString(responseObject);

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		logger.info("******************* Cab UI Report END *********************");
		return null;
	}

	@RequestMapping(value = "/cabReportExcel", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void fetchCabReportExcel(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk,
			HttpServletResponse res) throws IOException, URISyntaxException {

		logger.info("******************* Cab Report Excel START *********************");

		try {
			List<VOReportCabRequest> voReportCabRequestList = new ArrayList<>();
			voReportCabRequestList = getDataForCabReport(reportFilterTraveldesk);
			if (!HRMSHelper.isNullOrEmpty(voReportCabRequestList)) {
				Workbook workbook = writeXLSXFileCabRequestReport(voReportCabRequestList);
				res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				res.setHeader("Content-Disposition", "attachment; filename=Leave_Details_Report.xlsx");
				workbook.write(res.getOutputStream());

			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
		logger.info("******************* Cab Report Excel END *********************");
	}

	public static Workbook writeXLSXFileCabRequestReport(List<VOReportCabRequest> reportCabRequestList)
			throws IOException {

		String sheetName = "Cab_Request_Report";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		ArrayList<String> listOfMainHeader = new ArrayList<String>();

		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 30 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 30 * 256);
		sheet.setColumnWidth(5, 30 * 256);
		sheet.setColumnWidth(6, 30 * 256);
		sheet.setColumnWidth(7, 30 * 256);
		sheet.setColumnWidth(8, 30 * 256);
		sheet.setColumnWidth(9, 30 * 256);
		sheet.setColumnWidth(10, 30 * 256);
		sheet.setColumnWidth(11, 30 * 256);
		sheet.setColumnWidth(12, 30 * 256);
		sheet.setColumnWidth(13, 30 * 256);
		sheet.setColumnWidth(14, 30 * 256);
		sheet.setColumnWidth(15, 30 * 256);
		sheet.setColumnWidth(16, 30 * 256);
		sheet.setColumnWidth(17, 30 * 256);
		sheet.setColumnWidth(18, 30 * 256);
		sheet.setColumnWidth(19, 30 * 256);
		sheet.setColumnWidth(20, 30 * 256);
		sheet.setColumnWidth(21, 30 * 256);
		sheet.setColumnWidth(22, 30 * 256);
		sheet.setColumnWidth(23, 30 * 256);
		sheet.setColumnWidth(24, 30 * 256);

		listOfMainHeader.add("SR.NO.");
		listOfMainHeader.add("Requested By");
		listOfMainHeader.add("Requested Date");
		listOfMainHeader.add("Travel Request Id");
		listOfMainHeader.add("Cab Request Id");
		listOfMainHeader.add("WON");
		listOfMainHeader.add("Passenger Name");
		listOfMainHeader.add("Email Id");
		listOfMainHeader.add("Contact Number");
		listOfMainHeader.add("Pick Up Location");
		listOfMainHeader.add("Drop Location");
		listOfMainHeader.add("Pick Up Date");
		listOfMainHeader.add("Pick Up Time");
		listOfMainHeader.add("Return Date");
		listOfMainHeader.add("Return Time");
		listOfMainHeader.add("Is Drop Only");
		listOfMainHeader.add("Is Recurring");
		listOfMainHeader.add("Is Self Managed");
		listOfMainHeader.add("Approval Required");
		listOfMainHeader.add("Approver");
		listOfMainHeader.add("Approver Status");
		listOfMainHeader.add("Chargeable To Client");
		listOfMainHeader.add("Travel Request Status");
		listOfMainHeader.add("Cab Request Status");
		listOfMainHeader.add("Total Cab Cost");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

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
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font foorterFont = wb.createFont();
		foorterFont.setBold(true);
		foorterFont.setFontHeightInPoints((short) 11);
		CellStyle footerStyle = wb.createCellStyle();
		// footerStyle.setBorderLeft(BorderStyle.THIN);
		// footerStyle.setBorderRight(BorderStyle.THIN);
		footerStyle.setBorderTop(BorderStyle.THIN);
		// footerStyle.setBorderBottom(BorderStyle.THIN);
		footerStyle.setAlignment(HorizontalAlignment.CENTER);
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = wb.createCellStyle();
		// cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		// cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
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

		int rowNo = 0;
		int cellNo = 0;
		Cell cell = null;

		for (VOReportCabRequest cabReq : reportCabRequestList) {

			++rowNo;
			cellNo = 0;
			row = sheet.createRow(rowNo + 1);

			cell = row.createCell(cellNo++);
			cell.setCellValue(rowNo);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getRequestedBy());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getCreatedDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getTravelRequestId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getCabRequestId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getWon());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getPassengerName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getEmailId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getContactNumber());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getPickupAt());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getDropLocation());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getPickupDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getPickupTime());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getReturnDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getReturnTime());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getIsDropOnly());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getIsRecurring());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getIsSelfManaged());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getApprovalRequired());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getApprover());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getApproverStatus());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getChargeableToClient());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getTravelStatus());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getCabRequestStatus());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(cabReq.getTotalCabCost());
			cell.setCellStyle(cellStyle);
		}
		return wb;
	}

	public List<VOReportCabRequest> getDataForCabReport(VOReportFilterTraveldesk filter) {

		List<VOReportCabRequest> voCabRequestList = new ArrayList<>();
		List<ReportCabRequest> reportCabRequestList = reportCabRequestDAO
				.findAll(SpecificationFactory.extendedCabRequestReport(filter));
		// List<ReportCabRequest> reportCabRequestList = reportCabRequestDAO.findAll();

		/*
		 * ListIterator<ReportCabRequest> iter = reportCabRequestList.listIterator();
		 * while (iter.hasNext()) { ReportCabRequest cabReq = iter.next();
		 * List<CabRequestPassenger> passengerDetailList =
		 * cabPassengerDetailDAO.findByCabRequestId(cabReq.getCabRequestId()); String
		 * passengerNames = ""; for (CabRequestPassenger cabPassenger :
		 * passengerDetailList) { passengerNames += cabPassenger.getPassengerName() +
		 * " / "; } passengerNames = passengerNames.substring(0, passengerNames.length()
		 * - 3); cabReq.setPassengerName(passengerNames); }
		 */

		if (!HRMSHelper.isNullOrEmpty(reportCabRequestList)) {
			for (ReportCabRequest reportCabRequest : reportCabRequestList) {
				voCabRequestList.add(HRMSResponseTranslator.translateToReportCabRequestVo(reportCabRequest));
			}
		}
		return voCabRequestList;
	}

// ==========================================================================================================================//	

	@RequestMapping(value = "/travelDeskSummaryReport", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String fetchTravelDeskSummaryReport(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk)
			throws IOException, URISyntaxException {

		logger.info("******************* Traveldesk summary Report START *********************");

		VOReportTraveldeskSummary traveldeskSummary = new VOReportTraveldeskSummary();
		List<VOReportTraveldeskSummary> list = new ArrayList<VOReportTraveldeskSummary>();

		try {
			if (!HRMSHelper.isNullOrEmpty(reportFilterTraveldesk)
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getFromDate())
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getToDate())) {

				traveldeskSummary = getDataForTraveldeskSummaryReport(reportFilterTraveldesk);

				list.add(traveldeskSummary);

				if (!HRMSHelper.isNullOrEmpty(list)) {
					HRMSListResponseObject responseObject = new HRMSListResponseObject();
					List<Object> objectList = new ArrayList<Object>(list);
					responseObject.setResponseCode(IHRMSConstants.successCode);
					responseObject.setResponseMessage(IHRMSConstants.successMessage);
					responseObject.setListResponse(objectList);

					return HRMSHelper.createJsonString(responseObject);

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		logger.info("******************* Leave CAB Report END *********************");
		return null;

	}

	@RequestMapping(value = "/travelDeskSummaryReportExcel", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void fetchTravelDeskSummaryReportExcel(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk,
			HttpServletResponse res) throws IOException, URISyntaxException {

		logger.info("******************* Traveldesk Summary Excel START *********************");
		VOReportTraveldeskSummary traveldeskSummary = new VOReportTraveldeskSummary();
		List<VOReportTraveldeskSummary> list = new ArrayList<VOReportTraveldeskSummary>();
		try {
			if (!HRMSHelper.isNullOrEmpty(reportFilterTraveldesk)
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getFromDate())
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getToDate())) {

				traveldeskSummary = getDataForTraveldeskSummaryReport(reportFilterTraveldesk);

				list.add(traveldeskSummary);

				if (!HRMSHelper.isNullOrEmpty(list)) {

					Workbook workbook = writeXLSXFileTraveldeskSummaryReport(list);
					res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					res.setHeader("Content-Disposition", "attachment; filename=Leave_Details_Report.xlsx");
					workbook.write(res.getOutputStream());

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
		logger.info("******************* Traveldesk Summary Excel END *********************");
	}

	public static Workbook writeXLSXFileTraveldeskSummaryReport(List<VOReportTraveldeskSummary> traveldeskSummaryList)
			throws IOException {

		String sheetName = "Traveldesk_Summary_Report";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		ArrayList<String> listOfMainHeader = new ArrayList<String>();

		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 30 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 30 * 256);

		listOfMainHeader.add("SR.NO.");
		listOfMainHeader.add("Cab Request Count");
		listOfMainHeader.add("Ticket Request Count");
		listOfMainHeader.add("Accommodation Request Count");
		listOfMainHeader.add("Total Count");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

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
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font foorterFont = wb.createFont();
		foorterFont.setBold(true);
		foorterFont.setFontHeightInPoints((short) 11);
		CellStyle footerStyle = wb.createCellStyle();
		// footerStyle.setBorderLeft(BorderStyle.THIN);
		// footerStyle.setBorderRight(BorderStyle.THIN);
		footerStyle.setBorderTop(BorderStyle.THIN);
		// footerStyle.setBorderBottom(BorderStyle.THIN);
		footerStyle.setAlignment(HorizontalAlignment.CENTER);
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = wb.createCellStyle();
		// cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		// cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
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

		int rowNo = 0;
		int cellNo = 0;
		Cell cell = null;
		int totalRsTaken = 0;
		int totalRsReturn = 0;

		for (VOReportTraveldeskSummary summary : traveldeskSummaryList) {

			++rowNo;
			cellNo = 0;
			row = sheet.createRow(rowNo + 1);
			cell = row.createCell(cellNo++);
			cell.setCellValue(rowNo);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(summary.getCabRequestCount());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(summary.getTicketRequestCount());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(summary.getAccomodationRequestCount());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(summary.getTotalRequestCount());
			cell.setCellStyle(cellStyle);

		}
		return wb;
	}

	public VOReportTraveldeskSummary getDataForTraveldeskSummaryReport(VOReportFilterTraveldesk filter) {

		VOReportTraveldeskSummary summary = new VOReportTraveldeskSummary();
		filter.setFromDate(HRMSDateUtil.setTimeStampToZero(filter.getFromDate()));
		filter.setToDate(HRMSDateUtil.setTimeStampToZero(filter.getToDate()));
		List<CabRequest> cabRequestList = cabRequestDAO.getCabRequestByDateFilter(filter.getFromDate(),
				filter.getToDate(), IHRMSConstants.isActive);
		List<TicketRequest> ticketRequestList = ticketRequestDAO.getTicketRequestByDateFilter(filter.getFromDate(),
				filter.getToDate(), IHRMSConstants.isActive);
		List<AccommodationRequest> accomodationRequestList = accomodationRequestDAO
				.getAccomodationRequestByDateFilter(filter.getFromDate(), filter.getToDate(), IHRMSConstants.isActive);

		summary.setCabRequestCount(cabRequestList.size());
		summary.setTicketRequestCount(ticketRequestList.size());
		summary.setAccomodationRequestCount(accomodationRequestList.size());
		summary.setTotalRequestCount(cabRequestList.size() + ticketRequestList.size() + accomodationRequestList.size());

		return summary;
	}

	@RequestMapping(value = "/wonToCostReport", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String fetchWonToCostReport(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk)
			throws IOException, URISyntaxException {

		logger.info("******************* Won To Cost Report START *********************");

		List<VOReportWonToCostResponse> wonToCostResponseList = new ArrayList<>();
		List<ReportWonToCost> wonToCostEntityList = new ArrayList<>();
		Set<String> wonSet = new HashSet<>();

		try {
			if (!HRMSHelper.isNullOrEmpty(reportFilterTraveldesk)
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getFromDate())
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getToDate())) {

				wonToCostEntityList = getDataForWonToCostReport(reportFilterTraveldesk);

				for (ReportWonToCost entity : wonToCostEntityList) {
					wonSet.add(entity.getWon());
				}

				for (String won : wonSet) {
					VOReportWonToCostResponse w2cResponseObj = new VOReportWonToCostResponse();
					List<VOReportWonToCost> innelList = new ArrayList<>();
					double totalExpenditure = 0.0;
					for (ReportWonToCost w2cEntity : wonToCostEntityList) {

						if (w2cEntity.getWon().equals(won)) {
							VOReportWonToCost innnerObj = new VOReportWonToCost();
							innnerObj.setChargeableAmount(w2cEntity.getChargeableAmount());
							innnerObj.setCreatedDate(HRMSDateUtil.format(w2cEntity.getCreatedDate(),
									IHRMSConstants.FRONT_END_DATE_FORMAT));
							innnerObj.setRefundAmount(w2cEntity.getRefundAmount());
							innnerObj.setTravelRequestId(w2cEntity.getTravelRequestId());
							innnerObj.setSubRequestId(w2cEntity.getSubRequestId());
							innnerObj.setRequestType(w2cEntity.getRequestType());
							innnerObj.setTotalCost(w2cEntity.getTotalCost());
							innnerObj.setWon(w2cEntity.getWon());

							innelList.add(innnerObj);

							totalExpenditure += w2cEntity.getTotalCost();
							w2cResponseObj.setDepartmentName(w2cEntity.getDepartmentName());
							w2cResponseObj.setBdName(w2cEntity.getBdName());
							w2cResponseObj.setWon(won);
						}
						w2cResponseObj.setWonToCostList(innelList);

						w2cResponseObj.setTotalExpenditure(totalExpenditure);

					}
					wonToCostResponseList.add(w2cResponseObj);
				}

				// list.add(traveldeskSummary);

				if (!HRMSHelper.isNullOrEmpty(wonToCostResponseList)) {
					HRMSListResponseObject responseObject = new HRMSListResponseObject();
					List<Object> objectList = new ArrayList<Object>(wonToCostResponseList);
					responseObject.setResponseCode(IHRMSConstants.successCode);
					responseObject.setResponseMessage(IHRMSConstants.successMessage);
					responseObject.setListResponse(objectList);

					return HRMSHelper.createJsonString(responseObject);

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		logger.info("******************* Won To Cost Report END *********************");
		return null;

	}

	@RequestMapping(value = "/wonToCostReportExcel", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void fetchWonToCostReportExcel(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk,
			HttpServletResponse res) throws IOException, URISyntaxException {

		logger.info("******************* Won to cost Excel START *********************");
		List<VOReportWonToCostResponse> wonToCostResponseList = new ArrayList<>();
		List<ReportWonToCost> wonToCostEntityList = new ArrayList<>();
		Set<String> wonSet = new HashSet<>();
		try {
			if (!HRMSHelper.isNullOrEmpty(reportFilterTraveldesk)
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getFromDate())
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getToDate())) {

				wonToCostEntityList = getDataForWonToCostReport(reportFilterTraveldesk);

				/*
				 * for (ReportWonToCost entity : wonToCostEntityList) {
				 * wonSet.add(entity.getWon()); }
				 * 
				 * 
				 * for (String won : wonSet) { VOReportWonToCostResponse w2cResponseObj = new
				 * VOReportWonToCostResponse(); List<VOReportWonToCost> innelList = new
				 * ArrayList<>(); double totalExpenditure= 0.0; for (ReportWonToCost w2cEntity :
				 * wonToCostEntityList) {
				 * 
				 * if(w2cEntity.getWon().equals(won)) { VOReportWonToCost innnerObj = new
				 * VOReportWonToCost();
				 * innnerObj.setChargeableAmount(w2cEntity.getChargeableAmount());
				 * innnerObj.setCreatedDate(HRMSDateUtil.format(w2cEntity.getCreatedDate(),
				 * IHRMSConstants.FRONT_END_DATE_FORMAT));
				 * innnerObj.setRefundAmount(w2cEntity.getRefundAmount());
				 * innnerObj.setTravelRequestId(w2cEntity.getTravelRequestId());
				 * innnerObj.setSubRequestId(w2cEntity.getSubRequestId());
				 * innnerObj.setRequestType(w2cEntity.getRequestType());
				 * innnerObj.setTotalCost(w2cEntity.getTotalCost());
				 * innnerObj.setWon(w2cEntity.getWon());
				 * 
				 * innelList.add(innnerObj);
				 * 
				 * totalExpenditure += w2cEntity.getTotalCost(); //
				 * w2cResponseObj.setDepartmentName(w2cEntity.getDepartmentName());
				 * w2cResponseObj.setBdName(w2cEntity.getBdName()); w2cResponseObj.setWon(won);
				 * } w2cResponseObj.setWonToCostList(innelList);
				 * 
				 * w2cResponseObj.setTotalExpenditure(totalExpenditure);
				 * 
				 * 
				 * 
				 * } wonToCostResponseList.add(w2cResponseObj); }
				 * 
				 * 
				 * 
				 * //list.add(traveldeskSummary);
				 * 
				 * if(!HRMSHelper.isNullOrEmpty(wonToCostResponseList)) { HRMSListResponseObject
				 * responseObject = new HRMSListResponseObject(); List<Object> objectList = new
				 * ArrayList<Object>(wonToCostResponseList);
				 * responseObject.setResponseCode(IHRMSConstants.successCode);
				 * responseObject.setResponseMessage(IHRMSConstants.successMessage);
				 * responseObject.setListResponse(objectList);
				 * 
				 * return HRMSHelper.createJsonString(responseObject);
				 * 
				 * }else { throw new HRMSException(IHRMSConstants.DataNotFoundCode,
				 * IHRMSConstants.DataNotFoundMessage); }
				 */

				if (!HRMSHelper.isNullOrEmpty(wonToCostEntityList)) {
					Workbook workbook = writeXLSXFileWonToCostReport(wonToCostEntityList);
					res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					res.setHeader("Content-Disposition", "attachment; filename=Leave_Details_Report.xlsx");
					workbook.write(res.getOutputStream());

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
		logger.info("******************* Won to cost Excel END *********************");
	}

	public static Workbook writeXLSXFileWonToCostReport(List<ReportWonToCost> wonToCostList) throws IOException {

		String sheetName = "Won_To_Cost_Report";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		ArrayList<String> listOfMainHeader = new ArrayList<String>();

		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 30 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 30 * 256);
		sheet.setColumnWidth(5, 30 * 256);
		sheet.setColumnWidth(6, 30 * 256);
		sheet.setColumnWidth(7, 30 * 256);
		sheet.setColumnWidth(8, 30 * 256);

		listOfMainHeader.add("SR.NO.");
		listOfMainHeader.add("Created Date");
		listOfMainHeader.add("Travel ID");
		listOfMainHeader.add("Sub Request ID");
		listOfMainHeader.add("WON");
		listOfMainHeader.add("Request Type");
		listOfMainHeader.add("Chargeable Amount");
		listOfMainHeader.add("Refund Amout");
		listOfMainHeader.add("Total Cost");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

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
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font foorterFont = wb.createFont();
		foorterFont.setBold(true);
		foorterFont.setFontHeightInPoints((short) 11);
		CellStyle footerStyle = wb.createCellStyle();
		// footerStyle.setBorderLeft(BorderStyle.THIN);
		// footerStyle.setBorderRight(BorderStyle.THIN);
		footerStyle.setBorderTop(BorderStyle.THIN);
		// footerStyle.setBorderBottom(BorderStyle.THIN);
		footerStyle.setAlignment(HorizontalAlignment.CENTER);
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = wb.createCellStyle();
		// cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		// cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
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

		int rowNo = 0;
		int cellNo = 0;
		Cell cell = null;
		int totalRsTaken = 0;
		int totalRsReturn = 0;

		for (ReportWonToCost w2c : wonToCostList) {

			++rowNo;
			cellNo = 0;
			row = sheet.createRow(rowNo + 1);
			cell = row.createCell(cellNo++);
			cell.setCellValue(rowNo);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(HRMSDateUtil.format(w2c.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(w2c.getTravelRequestId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(w2c.getSubRequestId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(w2c.getWon());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(w2c.getRequestType());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(w2c.getChargeableAmount());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(w2c.getRefundAmount());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(w2c.getTotalCost());
			cell.setCellStyle(cellStyle);

		}
		return wb;
	}

	public List<ReportWonToCost> getDataForWonToCostReport(VOReportFilterTraveldesk filter) {

		List<ReportWonToCost> entityList = wonToCostDAO.findAll(SpecificationFactory.extendedWonTocostReport(filter));
		return entityList;
	}

	@RequestMapping(value = "/traveldeskDetailReport", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String fetchTraveldeskDetailReport(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk)
			throws IOException, URISyntaxException {

		logger.info("******************* Traveldesk detail Report START *********************");

		List<ReportTraveldeskDetail> reportTraveldeskDetailEntityList = new ArrayList<>();
		List<VOReportTraveldeskDetail> reportTraveldeskDetailVOList = new ArrayList<>();

		try {
			if (!HRMSHelper.isNullOrEmpty(reportFilterTraveldesk)
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getFromDate())
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getToDate())) {

				reportTraveldeskDetailEntityList = getDataForTravelDeskdetailReport(reportFilterTraveldesk);

				for (ReportTraveldeskDetail entity : reportTraveldeskDetailEntityList) {
					VOReportTraveldeskDetail vo = HRMSEntityToModelMapper.convertToReportTraveldeskDetailModel(entity);
					reportTraveldeskDetailVOList.add(vo);
				}

				if (!HRMSHelper.isNullOrEmpty(reportTraveldeskDetailVOList)) {
					HRMSListResponseObject responseObject = new HRMSListResponseObject();
					List<Object> objectList = new ArrayList<Object>(reportTraveldeskDetailVOList);
					responseObject.setResponseCode(IHRMSConstants.successCode);
					responseObject.setResponseMessage(IHRMSConstants.successMessage);
					responseObject.setListResponse(objectList);

					return HRMSHelper.createJsonString(responseObject);

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		logger.info("******************* Traveldesk detail Report END *********************");
		return null;

	}

	@RequestMapping(value = "/traveldeskDetailReportExcel", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void fetchTraveldeskDetailExcel(@RequestBody VOReportFilterTraveldesk reportFilterTraveldesk,
			HttpServletResponse res) throws IOException, URISyntaxException {

		logger.info("******************* Traveldesk Report Excel START *********************");
		List<ReportTraveldeskDetail> reportTraveldeskDetailEntityList = new ArrayList<>();
		List<VOReportTraveldeskDetail> reportTraveldeskDetailVOList = new ArrayList<>();

		try {
			if (!HRMSHelper.isNullOrEmpty(reportFilterTraveldesk)
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getFromDate())
					&& !HRMSHelper.isNullOrEmpty(reportFilterTraveldesk.getToDate())) {

				reportTraveldeskDetailEntityList = getDataForTravelDeskdetailReport(reportFilterTraveldesk);

				for (ReportTraveldeskDetail entity : reportTraveldeskDetailEntityList) {
					VOReportTraveldeskDetail vo = HRMSEntityToModelMapper.convertToReportTraveldeskDetailModel(entity);
					reportTraveldeskDetailVOList.add(vo);
				}

				if (!HRMSHelper.isNullOrEmpty(reportTraveldeskDetailVOList)) {
					Workbook workbook = writeXLSXFileTravelDeskDetailReport(reportTraveldeskDetailVOList);
					res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					res.setHeader("Content-Disposition", "attachment; filename=Leave_Details_Report.xlsx");
					workbook.write(res.getOutputStream());

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
		logger.info("******************* Traveldesk Report Excel END *********************");
	}

	public static Workbook writeXLSXFileTravelDeskDetailReport(List<VOReportTraveldeskDetail> tdDetailList)
			throws IOException {

		String sheetName = "Traveldesk_Detail_Report";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		ArrayList<String> listOfMainHeader = new ArrayList<String>();

		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 30 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 30 * 256);
		sheet.setColumnWidth(5, 30 * 256);
		sheet.setColumnWidth(6, 30 * 256);
		sheet.setColumnWidth(7, 30 * 256);
		sheet.setColumnWidth(8, 30 * 256);
		sheet.setColumnWidth(9, 30 * 256);
		sheet.setColumnWidth(10, 30 * 256);
		sheet.setColumnWidth(11, 30 * 256);
		sheet.setColumnWidth(12, 30 * 256);
		sheet.setColumnWidth(13, 30 * 256);
		sheet.setColumnWidth(14, 30 * 256);
		sheet.setColumnWidth(15, 30 * 256);
		sheet.setColumnWidth(16, 30 * 256);

		listOfMainHeader.add("SR.NO.");
		listOfMainHeader.add("Requested By");
		listOfMainHeader.add("WON");
		listOfMainHeader.add("Requested Date");
		listOfMainHeader.add("Travel ID");
		listOfMainHeader.add("Sub Request ID");
		listOfMainHeader.add("Request Type");
		listOfMainHeader.add("Round trip");
		listOfMainHeader.add("Travelling Mode");
		listOfMainHeader.add("passenger Name");
		listOfMainHeader.add("From Location");
		listOfMainHeader.add("To Location");
		listOfMainHeader.add("From Date");
		listOfMainHeader.add("To Date");
		listOfMainHeader.add("Approval Required");
		listOfMainHeader.add("Approver Name");
		listOfMainHeader.add("Chargeable To Client");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

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
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font foorterFont = wb.createFont();
		foorterFont.setBold(true);
		foorterFont.setFontHeightInPoints((short) 11);
		CellStyle footerStyle = wb.createCellStyle();
		// footerStyle.setBorderLeft(BorderStyle.THIN);
		// footerStyle.setBorderRight(BorderStyle.THIN);
		footerStyle.setBorderTop(BorderStyle.THIN);
		// footerStyle.setBorderBottom(BorderStyle.THIN);
		footerStyle.setAlignment(HorizontalAlignment.CENTER);
		// headerStyle.setWrapText(true);
		headerStyle.setFont(font);

		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = wb.createCellStyle();
		// cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		// cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
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

		int rowNo = 0;
		int cellNo = 0;
		Cell cell = null;

		for (VOReportTraveldeskDetail tdDet : tdDetailList) {

			++rowNo;
			cellNo = 0;
			row = sheet.createRow(rowNo + 1);
			cell = row.createCell(cellNo++);
			cell.setCellValue(rowNo);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getRequestedBy());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getWon());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getCreatedDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getTravelRequestId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getSubRequestId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getRequestType());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getRoundTrip());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getTravellingMode());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getPassengerName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getFromLocation());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getToLocation());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getFromDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getToDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getApprovalRequired());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getApproverName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(tdDet.getChargeableToClient());
			cell.setCellStyle(cellStyle);

		}
		return wb;
	}

	public List<ReportTraveldeskDetail> getDataForTravelDeskdetailReport(VOReportFilterTraveldesk filter) {

		// List<ReportTraveldeskDetail> entityList =
		// traveldeskDetailDAO.findAll(SpecificationFactory.extendedTraveldeskDetailReport(filter));
		List<ReportTraveldeskDetail> entityList = traveldeskDetailDAO.findAll(Specification
				.where(SpecificationFactory.extendedTraveldeskDetailReportForDate(filter))
				.and(Specification.where(SpecificationFactory.extendedTraveldeskDetailReportForRequestType(filter))
						.and(Specification
								.where(SpecificationFactory.extendedTraveldeskDetailReportForTravellingMode(filter)))));
		// List<ReportAccommodationRequest> reportAccommodationRequestList =
		// reportAccommodationRequestDAO.findAll(Specifications.where(SpecificationFactory.extendedAccomodationRequestReportFromDate(filter)).or(Specifications.where(SpecificationFactory.extendedAccomodationRequestReportToDate(filter))));

		return entityList;
	}
}
