package com.vinsys.hrms.traveldesk.vo;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.entity.DashboardRatingView;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraDetailsLite;
import com.vinsys.hrms.kra.entity.PercentageRatingView;
import com.vinsys.hrms.kra.util.EKraStatus;
import com.vinsys.hrms.kra.util.KraTransformUtil;
import com.vinsys.hrms.traveldesk.entity.TravelRequestV2;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

/**
 * @author Onkar A
 *
 * 
 */
@Component
public class ExpenseSummaryXLSGenerator {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFCellStyle cellStyle;
	private XSSFCellStyle dateFormateStyle;
	private XSSFCellStyle border;
	private XSSFDataFormat format;

	@Autowired
	private KraTransformUtil transformUtil;

	public ExpenseSummaryXLSGenerator() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("KPI Report");
		cellStyle = workbook.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);

		Font boldFont = workbook.createFont();
		cellStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());

		boldFont.setBold(true);
		cellStyle.setFont(boldFont);
		format = workbook.createDataFormat();

		// set date formate Style
		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		// dateFormateStyle = cellStyle;
		dateFormateStyle = workbook.createCellStyle();
		dateFormateStyle.setBorderBottom(BorderStyle.THIN);
		dateFormateStyle.setBorderTop(BorderStyle.THIN);
		dateFormateStyle.setBorderRight(BorderStyle.THIN);
		dateFormateStyle.setBorderLeft(BorderStyle.THIN);
		dateFormateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyy"));

		// border
		border = workbook.createCellStyle();
		border.setBorderBottom(BorderStyle.THIN);
		border.setBorderTop(BorderStyle.THIN);
		border.setBorderRight(BorderStyle.THIN);
		border.setBorderLeft(BorderStyle.THIN);
	}

	public Workbook generateXlS(List<TravelRequestV2> travelRequests) throws IOException {
		int rownum = 0;

		Row header = sheet.createRow(rownum++);

		Cell requestIdHeader = header.createCell(0);
		requestIdHeader.setCellStyle(cellStyle);
		requestIdHeader.setCellValue("Request Id");

		Cell requesterNameHeader = header.createCell(1);
		requesterNameHeader.setCellStyle(cellStyle);
		requesterNameHeader.setCellValue("Requester Name");

		Cell bpmNumberHeader = header.createCell(2);
		bpmNumberHeader.setCellStyle(cellStyle);
		bpmNumberHeader.setCellValue("BPM No");

		Cell divisionHeader = header.createCell(3);
		divisionHeader.setCellStyle(cellStyle);
		divisionHeader.setCellValue("Division");

		Cell departMentHeader = header.createCell(4);
		departMentHeader.setCellStyle(cellStyle);
		departMentHeader.setCellValue("Department");

		Cell createdDateHeader = header.createCell(5);
		createdDateHeader.setCellStyle(cellStyle);
		createdDateHeader.setCellValue("Created Date");

		Cell approxCostHeader = header.createCell(6);
		approxCostHeader.setCellStyle(cellStyle);
		approxCostHeader.setCellValue("Approx Cost");

		Cell finalCostHeader = header.createCell(7);
		finalCostHeader.setCellStyle(cellStyle);
		finalCostHeader.setCellValue("Final Cost");

		Cell refundCostHeader = header.createCell(8);
		refundCostHeader.setCellStyle(cellStyle);
		refundCostHeader.setCellValue("Refund Cost");

		Cell settledCostHeader = header.createCell(9);
		settledCostHeader.setCellStyle(cellStyle);
		settledCostHeader.setCellValue("Settled Cost");

		Cell approvedByHeader = header.createCell(10);
		approvedByHeader.setCellStyle(cellStyle);
		approvedByHeader.setCellValue("Approved By");

		Cell statusHeader = header.createCell(11);
		statusHeader.setCellStyle(cellStyle);
		statusHeader.setCellValue("status");

		for (TravelRequestV2 travelRequest : travelRequests) {
			Row row = sheet.createRow(rownum++);

			Cell requestId = row.createCell(0);
			requestId.setCellStyle(border);
			requestId.setCellValue(travelRequest.getId());

			Cell requesterName = row.createCell(1);
			requesterName.setCellStyle(border);
			requesterName.setCellValue(travelRequest.getName());

			Cell bpmNumber = row.createCell(2);
			bpmNumber.setCellStyle(border);
			bpmNumber.setCellValue(travelRequest.getBpmNumber());

			Cell division = row.createCell(3);
			division.setCellStyle(border);
			division.setCellValue(travelRequest.getDivision().getDivisionName());

			Cell departMent = row.createCell(4);
			departMent.setCellStyle(border);
			departMent.setCellValue(travelRequest.getDepartment().getDepartmentName());

			Cell createdDate = row.createCell(5);
			createdDate.setCellStyle(dateFormateStyle);
			createdDate.setCellValue(travelRequest.getCreatedDate());

			Cell approxCost = row.createCell(6);
			approxCost.setCellStyle(border);
			approxCost.setCellValue(!HRMSHelper.isNullOrEmpty(travelRequest.getTotalApproximateCost())
					? travelRequest.getTotalApproximateCost()
					: 0);

			Cell finalCost = row.createCell(7);
			finalCost.setCellStyle(border);
			finalCost.setCellValue(
					!HRMSHelper.isNullOrEmpty(travelRequest.getTotalFinalCost()) ? travelRequest.getTotalFinalCost()
							: 0);

			Cell refundCost = row.createCell(8);
			refundCost.setCellStyle(border);
			refundCost.setCellValue(
					!HRMSHelper.isNullOrEmpty(travelRequest.getTotalRefundCost()) ? travelRequest.getTotalRefundCost()
							: 0);

			Cell settledCost = row.createCell(9);
			settledCost.setCellStyle(border);
			settledCost.setCellValue(
					!HRMSHelper.isNullOrEmpty(travelRequest.getTotalSettledCost()) ? travelRequest.getTotalSettledCost()
							: 0);

			Cell approvedBy = row.createCell(10);
			approvedBy.setCellStyle(border);
			if (!HRMSHelper.isNullOrEmpty(travelRequest.getApprover())) {
				approvedBy.setCellValue(!HRMSHelper.isNullOrEmpty(travelRequest.getApprover().getCandidate())
						? travelRequest.getApprover().getCandidate().getFirstName() + " "
								+ travelRequest.getApprover().getCandidate().getLastName()
						: "");
			}

			Cell status = row.createCell(11);
			status.setCellStyle(border);
			status.setCellValue(travelRequest.getRequestWF().getStatus());

		}
		for (int i = 0; i < 13; i++) {
			sheet.autoSizeColumn(i);
		}

		return workbook;
	}

//	public Workbook generateKRAXlS(List<KraDetailsLite> kraDetails) throws IOException {
//		XSSFCellStyle cellStyle = workbook.createCellStyle();
//
//		Font boldFont = workbook.createFont();
//		cellStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());
//		boldFont.setBold(true);
//		cellStyle.setFont(boldFont);
//		cellStyle.setBorderBottom(BorderStyle.THIN);
//		cellStyle.setBorderTop(BorderStyle.THIN);
//		cellStyle.setBorderRight(BorderStyle.THIN);
//		cellStyle.setBorderLeft(BorderStyle.THIN);
//
//		int rownum = 0;
//
//		Row titleRow = sheet.createRow(rownum++);
//		Cell titleCell = titleRow.createCell(0);
//		titleCell.setCellValue("KPI Report");
//		// Merge cells for the title
////	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7)); 
//
//		CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, 11);
//		sheet.addMergedRegion(mergedRegion);
//
//		RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegion, sheet);
//		RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegion, sheet);
//		RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegion, sheet);
//		RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegion, sheet);
//
//		titleCell.setCellStyle(cellStyle);
//
//		XSSFCellStyle titleStyle = workbook.createCellStyle();
//		titleStyle.cloneStyleFrom(cellStyle);
//		titleStyle.setAlignment(HorizontalAlignment.CENTER);
//		titleCell.setCellStyle(titleStyle);
//
//		Row header = sheet.createRow(rownum++);
//
//		Cell requestIdHeader = header.createCell(0);
//		requestIdHeader.setCellStyle(cellStyle);
//		requestIdHeader.setCellValue("Sr.No");
//
//		Cell requesterNameHeader = header.createCell(1);
//		requesterNameHeader.setCellStyle(cellStyle);
//		requesterNameHeader.setCellValue("Employee Code");
//
//		Cell bpmNumberHeader = header.createCell(2);
//		bpmNumberHeader.setCellStyle(cellStyle);
//		bpmNumberHeader.setCellValue("Employee Name");
//
//		Cell divisionHeader = header.createCell(3);
//		divisionHeader.setCellStyle(cellStyle);
//		divisionHeader.setCellValue("Employee Email-id");
//
//		Cell departMentHeader = header.createCell(4);
//		departMentHeader.setCellStyle(cellStyle);
//		departMentHeader.setCellValue("Department");
//
//		Cell createdDateHeader = header.createCell(5);
//		createdDateHeader.setCellStyle(cellStyle);
//		createdDateHeader.setCellValue("Reporting Manager");
//
//		Cell approxCostHeader = header.createCell(6);
//		approxCostHeader.setCellStyle(cellStyle);
//		approxCostHeader.setCellValue("KPI status");
//
//		Cell pendingWithHeader = header.createCell(7);
//		pendingWithHeader.setCellStyle(cellStyle);
//		pendingWithHeader.setCellValue("Pending With");
//
//		Cell finalCostHeader = header.createCell(8);
//		finalCostHeader.setCellStyle(cellStyle);
//		finalCostHeader.setCellValue("KPI year");
//
//		Cell selfRateHeader = header.createCell(9);
//		selfRateHeader.setCellStyle(cellStyle);
//		selfRateHeader.setCellValue("Self Rating");
//
//		Cell managerRateHeader = header.createCell(10);
//		managerRateHeader.setCellStyle(cellStyle);
//		managerRateHeader.setCellValue("Manager Rating");
//
//		Cell calibrationHeader = header.createCell(11);
//		calibrationHeader.setCellStyle(cellStyle);
//		calibrationHeader.setCellValue("Calibrated Rating");
//
//		int index = 1;
//		for (KraDetailsLite kraDetail : kraDetails) {
//
//			String status = transformUtil.setKraReportStatus(kraDetail);
//			Row row = sheet.createRow(rownum++);
//
//			Cell srNo = row.createCell(0);
//			srNo.setCellStyle(border);
//			srNo.setCellValue(index++);
//
//			Cell empCode = row.createCell(1);
//			empCode.setCellStyle(border);
//			empCode.setCellValue(kraDetail.getEmployeeId());
//
//			Cell empName = row.createCell(2);
//			empName.setCellStyle(border);
//			empName.setCellValue(kraDetail.getFirstName() + " " + kraDetail.getLastName());
//
//			Cell empEmailId = row.createCell(3);
//			empEmailId.setCellStyle(border);
//			empEmailId.setCellValue(kraDetail.getEmpEmailId());
//
//			Cell empDept = row.createCell(4);
//			empDept.setCellStyle(border);
//			empDept.setCellValue(kraDetail.getDepartmentName());
//
//			Cell empRm = row.createCell(5);
//			empRm.setCellStyle(border);
//			empRm.setCellValue(!HRMSHelper.isNullOrEmpty(kraDetail.getRmFirstName())
//					&& !HRMSHelper.isNullOrEmpty(kraDetail.getRmLastName())
//							? kraDetail.getRmFirstName() + " " + kraDetail.getRmLastName()
//							: "NA");
//
//			Cell kraStaus = row.createCell(6);
//			kraStaus.setCellStyle(border);
//			kraStaus.setCellValue((status));
//
//			String PendingWith = null;
//			if (kraDetail.getStatus().equalsIgnoreCase(EKraStatus.COMPLETED.name())) {
//				PendingWith = "";
//			} else {
//				PendingWith = kraDetail.getPendingWith();
//			}
//
//			Cell pendingWith = row.createCell(7);
//			pendingWith.setCellStyle(border);
//			pendingWith.setCellValue((PendingWith));
//
//			Cell kraYear = row.createCell(8);
//			kraYear.setCellStyle(border);
//			kraYear.setCellValue((kraDetail.getYear()));
//
//			Cell selfRating = row.createCell(9);
//			selfRating.setCellStyle(border);
//			selfRating.setCellValue((kraDetail.getTotalSelfRating()));
//
//			Cell managerRating = row.createCell(10);
//			managerRating.setCellStyle(border);
//			managerRating.setCellValue((kraDetail.getRmFinalRating()));
//
//			Cell calibratedRating = row.createCell(11);
//			calibratedRating.setCellStyle(border);
//			calibratedRating.setCellValue((kraDetail.getCalibratedRating()));
//
//		}
//		for (int i = 0; i < 10; i++) {
//			sheet.autoSizeColumn(i);
//		}
//
//		return workbook;
//	}

	public Workbook generateKRAXlS(List<KraDetailsLite> kraDetails) throws IOException, HRMSException {
		Workbook workbook = new XSSFWorkbook(); // Create a new Workbook each time
		Sheet sheet = workbook.createSheet("KRA Report");
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

		Font boldFont = workbook.createFont();
		boldFont.setBold(true);
		cellStyle.setFont(boldFont);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);

		int rownum = 0;

		Row titleRow = sheet.createRow(rownum++);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("KPI Report");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));
		titleCell.setCellStyle(cellStyle);

		Row header = sheet.createRow(rownum++);
		String[] headers = { "Sr.No", "Employee Code", "Employee Name", "Employee Email-id", "Department",
				"Reporting Manager", "KPI status", "Pending With", "KPI year", "Self Rating", "Manager Rating",
				"Calibrated Rating", "Cycle" };

		for (int i = 0; i < headers.length; i++) {
			Cell headerCell = header.createCell(i);
			headerCell.setCellStyle(cellStyle);
			headerCell.setCellValue(headers[i]);
		}

		int index = 1;
		for (KraDetailsLite kraDetail : kraDetails) {
			Row row = sheet.createRow(rownum++);
			String value = null;
			String flag = transformUtil.setCheckRatingAccess(kraDetail.getKraId());
			if (flag.equalsIgnoreCase(IHRMSConstants.True)) {
				row.createCell(0).setCellValue(index++);
				row.createCell(1).setCellValue(kraDetail.getEmployeeId());
				row.createCell(2).setCellValue(kraDetail.getFirstName() + " " + kraDetail.getLastName());
				row.createCell(3).setCellValue(kraDetail.getEmpEmailId());
				row.createCell(4).setCellValue(kraDetail.getDepartmentName());
				row.createCell(5).setCellValue(kraDetail.getRmFirstName() + " " + kraDetail.getRmLastName());
				row.createCell(6).setCellValue(transformUtil.setKraReportStatus(kraDetail));
				row.createCell(7).setCellValue(kraDetail.getPendingWith());
				row.createCell(8).setCellValue(kraDetail.getYear());
				row.createCell(9).setCellValue(value);
				row.createCell(10).setCellValue(value);
				row.createCell(11).setCellValue(value);
				String cycleName = transformUtil.setCycleName(kraDetail.getCycle_id());
				row.createCell(12).setCellValue(cycleName);
			} else {

				row.createCell(0).setCellValue(index++);
				row.createCell(1).setCellValue(kraDetail.getEmployeeId());
				row.createCell(2).setCellValue(kraDetail.getFirstName() + " " + kraDetail.getLastName());
				row.createCell(3).setCellValue(kraDetail.getEmpEmailId());
				row.createCell(4).setCellValue(kraDetail.getDepartmentName());
				row.createCell(5).setCellValue(kraDetail.getRmFirstName() + " " + kraDetail.getRmLastName());
				row.createCell(6).setCellValue(transformUtil.setKraReportStatus(kraDetail));
				row.createCell(7).setCellValue(kraDetail.getPendingWith());
				row.createCell(8).setCellValue(kraDetail.getYear());
				row.createCell(9).setCellValue(kraDetail.getTotalSelfRating());
				row.createCell(10).setCellValue(kraDetail.getRmFinalRating());
				row.createCell(11).setCellValue(kraDetail.getCalibratedRating());
				String cycleName = transformUtil.setCycleName(kraDetail.getCycle_id());
				row.createCell(12).setCellValue(cycleName);
			}
		}

		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		return workbook;
	}

	// **************HOD Report**********************

	public Workbook generateKRAXlSForHod(List<Kra> kraDetails) throws IOException {
		XSSFCellStyle cellStyle = workbook.createCellStyle();

		Font boldFont = workbook.createFont();
		cellStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());
		boldFont.setBold(true);
		cellStyle.setFont(boldFont);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);

		int rownum = 0;

		Row titleRow = sheet.createRow(rownum++);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("KPI Report");
		// Merge cells for the title
//	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7)); 

		CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, 10);
		sheet.addMergedRegion(mergedRegion);

		RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegion, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegion, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegion, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegion, sheet);

		titleCell.setCellStyle(cellStyle);

		XSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.cloneStyleFrom(cellStyle);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCell.setCellStyle(titleStyle);

		Row header = sheet.createRow(rownum++);

		Cell requestIdHeader = header.createCell(0);
		requestIdHeader.setCellStyle(cellStyle);
		requestIdHeader.setCellValue("Sr.No");

		Cell requesterNameHeader = header.createCell(1);
		requesterNameHeader.setCellStyle(cellStyle);
		requesterNameHeader.setCellValue("Employee Code");

		Cell bpmNumberHeader = header.createCell(2);
		bpmNumberHeader.setCellStyle(cellStyle);
		bpmNumberHeader.setCellValue("Employee Name");

		Cell divisionHeader = header.createCell(3);
		divisionHeader.setCellStyle(cellStyle);
		divisionHeader.setCellValue("Employee Email-id");

		Cell departMentHeader = header.createCell(4);
		departMentHeader.setCellStyle(cellStyle);
		departMentHeader.setCellValue("Department");

		Cell createdDateHeader = header.createCell(5);
		createdDateHeader.setCellStyle(cellStyle);
		createdDateHeader.setCellValue("Reporting Manager");

		Cell approxCostHeader = header.createCell(6);
		approxCostHeader.setCellStyle(cellStyle);
		approxCostHeader.setCellValue("KPI status");

		Cell pendingWithHeader = header.createCell(7);
		pendingWithHeader.setCellStyle(cellStyle);
		pendingWithHeader.setCellValue("Pending With");

		Cell finalCostHeader = header.createCell(8);
		finalCostHeader.setCellStyle(cellStyle);
		finalCostHeader.setCellValue("KPI year");

		Cell selfRateHeader = header.createCell(9);
		selfRateHeader.setCellStyle(cellStyle);
		selfRateHeader.setCellValue("Self Rating");

		Cell managerRateHeader = header.createCell(10);
		managerRateHeader.setCellStyle(cellStyle);
		managerRateHeader.setCellValue("Manager Rating");

		Cell calibrationHeader = header.createCell(11);
		calibrationHeader.setCellStyle(cellStyle);
		calibrationHeader.setCellValue("Calibrated Rating");

		int index = 1;
		for (Kra kraDetail : kraDetails) {

			String status = transformUtil.setKraReportStatus(kraDetail);
			String CalibratedRating = transformUtil.getCalibratedRating(kraDetail);
			Row row = sheet.createRow(rownum++);

			Cell srNo = row.createCell(0);
			srNo.setCellStyle(border);
			srNo.setCellValue(index++);

			Cell empCode = row.createCell(1);
			empCode.setCellStyle(border);
			empCode.setCellValue(kraDetail.getEmployeeId());

			Cell empName = row.createCell(2);
			empName.setCellStyle(border);
			empName.setCellValue(kraDetail.getEmployee().getCandidate().getFirstName() + " "
					+ kraDetail.getEmployee().getCandidate().getLastName());

			Cell empEmailId = row.createCell(3);
			empEmailId.setCellStyle(border);
			empEmailId.setCellValue(kraDetail.getEmployee().getOfficialEmailId());

			Cell empDept = row.createCell(4);
			empDept.setCellStyle(border);
			empDept.setCellValue(kraDetail.getEmployee().getCandidate().getCandidateProfessionalDetail().getDepartment()
					.getDepartmentName());

			Cell empRm = row.createCell(5);
			empRm.setCellStyle(border);
			empRm.setCellValue(kraDetail.getEmployee().getEmployeeReportingManager().getReporingManager().getCandidate()
					.getFirstName() + " "
					+ kraDetail.getEmployee().getEmployeeReportingManager().getReporingManager().getCandidate()
							.getLastName());

			Cell kraStaus = row.createCell(6);
			kraStaus.setCellStyle(border);
			kraStaus.setCellValue((status));

			String PendingWith = null;
			if (kraDetail.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.COMPLETED.name())) {
				PendingWith = "";
			} else {
				PendingWith = kraDetail.getKraWf().getPendingWith();
			}

			Cell pendingWith = row.createCell(7);
			pendingWith.setCellStyle(border);
			pendingWith.setCellValue((PendingWith));

			Cell kraYear = row.createCell(8);
			kraYear.setCellStyle(border);
			kraYear.setCellValue((kraDetail.getKraYear().getYear()));

			Cell selfRating = row.createCell(9);
			selfRating.setCellStyle(border);
			selfRating.setCellValue((kraDetail.getTotalSelfRating()));

			Cell managerRating = row.createCell(10);
			managerRating.setCellStyle(border);
			managerRating.setCellValue((kraDetail.getFinalRating()));

			Cell calibratedRating = row.createCell(11);
			calibratedRating.setCellStyle(border);
			calibratedRating.setCellValue((CalibratedRating));

		}
		for (int i = 0; i < 10; i++) {
			sheet.autoSizeColumn(i);
		}

		return workbook;
	}

	public Workbook generateDashboardRatingXlS(List<MasterDepartment> departmentsList,
			List<DashboardRatingView> dashboardratinglist) {
		Workbook workbook = new XSSFWorkbook(); 
		Sheet sheet = workbook.createSheet("DashboardRating Report");
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

		Font boldFont = workbook.createFont();
		boldFont.setBold(true);
		cellStyle.setFont(boldFont);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);

		int rownum = 0;

		Row titleRow = sheet.createRow(rownum++);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Dashboard Rating Report");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
		titleCell.setCellStyle(cellStyle);

		Row header = sheet.createRow(rownum++);
		String[] headers = { "Rating", "LEG", "HCD", "PRT_FIN", "CDO",
				"NBD", "CSM", "AUD", "TOTAL", "TOTAL_ACTUAL", "TARGET"};

		for (int i = 0; i < headers.length; i++) {
			Cell headerCell = header.createCell(i);
			headerCell.setCellStyle(cellStyle);
			headerCell.setCellValue(headers[i]);
		}
	
	    String currentGroupPrefix = null;
	    int mergeStartRow = -1;
	   
		if (dashboardratinglist != null && !dashboardratinglist.isEmpty()) {
			
		for (DashboardRatingView ratingDetail : dashboardratinglist) {
           if(ratingDetail.getRating().equalsIgnoreCase("Fail")||ratingDetail.getRating().equalsIgnoreCase("Pass")) {
				continue;
			}
 
           String[] ratingParts = ratingDetail.getRating().split(" ");
           String groupPrefix = ratingParts[0]; 
			   Row row = sheet.createRow(rownum++);
		
				row.createCell(0).setCellValue(ratingDetail.getRating());
				row.createCell(1).setCellValue(ratingDetail.getLeg());
				row.createCell(2).setCellValue(ratingDetail.getHcd());
				row.createCell(3).setCellValue(ratingDetail.getPrtFin());
				row.createCell(4).setCellValue(ratingDetail.getCdo());
				row.createCell(5).setCellValue(ratingDetail.getNbd());
				row.createCell(6).setCellValue(ratingDetail.getCsm());
				row.createCell(7).setCellValue(ratingDetail.getAud());
				row.createCell(8).setCellValue(ratingDetail.getTotal());
				row.createCell(9).setCellValue(ratingDetail.getTotalActual());
                row.createCell(10).setCellValue(ratingDetail.getTarget());
                
                if (currentGroupPrefix != null && currentGroupPrefix.equals(groupPrefix)) {
                   
                    if (mergeStartRow == -1) {
                        mergeStartRow = rownum - 2;
                    }
                } else {
                    
                    if (mergeStartRow != -1) {
                        sheet.addMergedRegion(new CellRangeAddress(mergeStartRow, rownum - 2, 9, 9)); 
                        sheet.addMergedRegion(new CellRangeAddress(mergeStartRow, rownum - 2, 10, 10)); 
                        mergeStartRow = -1;
                    }
                   
                    currentGroupPrefix = groupPrefix;
                    mergeStartRow = rownum - 1;
                }
            }

            
            if (mergeStartRow != -1) {
                sheet.addMergedRegion(new CellRangeAddress(mergeStartRow, rownum - 1, 9, 9)); // Merge TotalActual
                sheet.addMergedRegion(new CellRangeAddress(mergeStartRow, rownum - 1, 10, 10)); // Merge Target
            }
        }
		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		return workbook;
	}

	public Workbook generatePercentageRatingXlS(List<MasterDepartment> departmentsList,
			List<PercentageRatingView> percenatgeratinglist) {
		
		Workbook workbook = new XSSFWorkbook(); 
		Sheet sheet = workbook.createSheet("Percentage Rating Report");
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

		Font boldFont = workbook.createFont();
		boldFont.setBold(true);
		cellStyle.setFont(boldFont);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);

		int rownum = 0;

		Row titleRow = sheet.createRow(rownum++);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Percentage Rating Report");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
		titleCell.setCellStyle(cellStyle);

		Row header = sheet.createRow(rownum++);
		String[] headers = { "Rating", "LEG", "HCD", "PRT_FIN", "CDO",
				"NBD", "CSM", "AUD", "TOTAL", "TOTAL_ACTUAL", "TARGET"};

		for (int i = 0; i < headers.length; i++) {
			Cell headerCell = header.createCell(i);
			headerCell.setCellStyle(cellStyle);
			headerCell.setCellValue(headers[i]);
		}
	
	    String currentGroupPrefix = null;
	    int mergeStartRow = -1;
	   
		if (percenatgeratinglist != null && !percenatgeratinglist.isEmpty()) {
			
		for (PercentageRatingView ratingDetail : percenatgeratinglist) {
           if(ratingDetail.getRating().equalsIgnoreCase("Fail")||ratingDetail.getRating().equalsIgnoreCase("Pass")) {
				continue;
			}
 
           String[] ratingParts = ratingDetail.getRating().split(" ");
           String groupPrefix = ratingParts[0]; 
			   Row row = sheet.createRow(rownum++);
		
				row.createCell(0).setCellValue(ratingDetail.getRating());
				row.createCell(1).setCellValue(ratingDetail.getLeg());
				row.createCell(2).setCellValue(ratingDetail.getHcd());
				row.createCell(3).setCellValue(ratingDetail.getPrtFin());
				row.createCell(4).setCellValue(ratingDetail.getCdo());
				row.createCell(5).setCellValue(ratingDetail.getNbd());
				row.createCell(6).setCellValue(ratingDetail.getCsm());
				row.createCell(7).setCellValue(ratingDetail.getAud());
				row.createCell(8).setCellValue(ratingDetail.getTotal());
				row.createCell(9).setCellValue(ratingDetail.getTotalActual());
                row.createCell(10).setCellValue(ratingDetail.getTarget());
                
                if (currentGroupPrefix != null && currentGroupPrefix.equals(groupPrefix)) {
                   
                    if (mergeStartRow == -1) {
                        mergeStartRow = rownum - 2;
                    }
                } else {
                    
                    if (mergeStartRow != -1) {
                        sheet.addMergedRegion(new CellRangeAddress(mergeStartRow, rownum - 2, 9, 9)); 
                        sheet.addMergedRegion(new CellRangeAddress(mergeStartRow, rownum - 2, 10, 10)); 
                        mergeStartRow = -1;
                    }
                   
                    currentGroupPrefix = groupPrefix;
                    mergeStartRow = rownum - 1;
                }
            }

            
            if (mergeStartRow != -1) {
                sheet.addMergedRegion(new CellRangeAddress(mergeStartRow, rownum - 1, 9, 9)); // Merge TotalActual
                sheet.addMergedRegion(new CellRangeAddress(mergeStartRow, rownum - 1, 10, 10)); // Merge Target
            }
        }
		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		return workbook;
	}
			
}