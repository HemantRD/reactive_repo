package com.vinsys.hrms.services.attendance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceRawDataDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.entity.attendance.AttendanceRawData;
import com.vinsys.hrms.entity.attendance.FileUpload;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/attendance")

public class UploadAttendanceService {

	@Autowired
	IHRMSAttendanceRawDataDAO attendanceRawDataDAO;

	@RequestMapping(value = "/uploadExcel", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String uploadExcel(@RequestBody FileUpload af) {
		if (af.getAttendanceFile() != null) {
			boolean errorFlag = false;
			int sucCount = 0;
			int errCount = 0;
			int totalCount = 0;
			List<Object> colHeadersMapList = getHeaderRows();

			List<AttendanceRawData> attendanceRawDataList = new ArrayList<>();
			try {
				attendanceRawDataDAO.deleteAll();
				byte[] xls = Base64.getDecoder().decode(af.getAttendanceFile().getValue().getBytes());
				ByteArrayInputStream bis = new ByteArrayInputStream(xls);

				int i = 1;
				XSSFWorkbook workbook = new XSSFWorkbook(bis);
				XSSFSheet worksheet = workbook.getSheetAt(0);
				// Reads the data in excel file until last row is encountered
				totalCount = worksheet.getLastRowNum() - worksheet.getFirstRowNum();
				while (i <= worksheet.getLastRowNum()) {
					AttendanceRawData attendanceRawData = new AttendanceRawData();
					try {

						XSSFRow row = worksheet.getRow(i++);
						attendanceRawData.setEmpName(getEmpName(row.getCell(2)));
						attendanceRawData.setEmpId(getCellValue(row.getCell(0)));
						attendanceRawData.setEmployeeACN(getCellValue(row.getCell(1)));
						attendanceRawData.setDepartment(row.getCell(3).getStringCellValue());
						attendanceRawData.setDesignation(row.getCell(4).getStringCellValue());
						attendanceRawData.setAttendanceDate(getAttendanceDate(row.getCell(5)));
						attendanceRawData.setStatus(row.getCell(6).getStringCellValue());
						attendanceRawData.setStartTime(row.getCell(7).getStringCellValue().length() == 0 ? null
								: new SimpleDateFormat("hh:mma").parse(row.getCell(7).getStringCellValue()));
						attendanceRawData.setEndTime(row.getCell(8).getStringCellValue().length() == 0 ? null
								: new SimpleDateFormat("hh:mma").parse(row.getCell(8).getStringCellValue()));
						attendanceRawData.setManHours(getManHours(row.getCell(9)));
						attendanceRawData.setOrgId(af.getAttendanceFile().getOrgId()); // Currently Hardcoded
						attendanceRawData.setUploadStatus("Success");
						attendanceRawData.setCreatedBy("System");
						attendanceRawData.setCreatedDate(new Date());
						attendanceRawData.setRemark("HRMS UI Upload");

						sucCount++;
					} catch (Exception e) {
						errorFlag = true;
						errCount++;
						attendanceRawData.setUploadStatus("Failed at Excel Row No: " + i);
						e.printStackTrace();
					}
					attendanceRawDataList.add(attendanceRawData);
					attendanceRawDataDAO.save(attendanceRawData);
				}
				workbook.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<Object> voAttendanceRawInfoList = new ArrayList<>();
			HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
			try {
				// attendanceInfoList = attendanceInfoDAO.findAll();
				if (!HRMSHelper.isNullOrEmpty(attendanceRawDataList)) {
					voAttendanceRawInfoList = HRMSResponseTranslator.transalteToAttendanceListVO(attendanceRawDataList,
							voAttendanceRawInfoList);
					hrmsListResponseObject.setListResponse((List<Object>) voAttendanceRawInfoList);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setSuccessCount(sucCount);
					hrmsListResponseObject.setErrorCount(errCount);
					hrmsListResponseObject.setTotalCount(totalCount);
					if (!errorFlag)
						hrmsListResponseObject.setResponseMessage("File Uploaded Successfully!");
					else
						hrmsListResponseObject.setResponseMessage("File Uploaded with some errors");

					hrmsListResponseObject.setColHeaders(colHeadersMapList);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
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
					return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage,
							IHRMSConstants.UnknowErrorCode);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			return null;
		} else {
			try {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.NO_FILE_EXISTS_MESSAGE,
						IHRMSConstants.NO_FILE_EXISTS_CODE);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private List<Object> getHeaderRows() {
		Map<String, Object> colHeadersMap = new HashMap<String, Object>();
		List<Object> colHeadersMapList = new ArrayList<>();

		colHeadersMap.put("property", "empId");
		colHeadersMap.put("header", "Employee Id");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "employeeACN");
		colHeadersMap.put("header", "Employee ACN");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "empName");
		colHeadersMap.put("header", "Employee Name");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "department");
		colHeadersMap.put("header", "Department");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "designation");
		colHeadersMap.put("header", "Designation");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "attendanceDate");
		colHeadersMap.put("header", "Date");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "status");
		colHeadersMap.put("header", "Status");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "startTime");
		colHeadersMap.put("header", "Start Time");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "endTime");
		colHeadersMap.put("header", "End Time");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "manHours");
		colHeadersMap.put("header", "Man Hours");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);

		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "uploadStatus");
		colHeadersMap.put("header", "Upload Status");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		return colHeadersMapList;
	}

	private double getManHours(XSSFCell cell) {
		double manHours;
		if (cell.getCellTypeEnum() == CellType.STRING)
			manHours = Double.valueOf((cell.getStringCellValue().trim()));
		else
			manHours = cell.getNumericCellValue();

		return manHours;
	}

	private Date getAttendanceDate(XSSFCell cell) throws ParseException {
		Date aDate;
		if (cell.getCellTypeEnum() == CellType.STRING)
			aDate = new SimpleDateFormat("MMM dd, yy").parse(cell.getStringCellValue().trim());
		else
			aDate = cell.getDateCellValue();
		return aDate;
	}

	private long getCellValue(XSSFCell cell) {
		long x;
		if (cell.getCellType() == 0)
			x = (long) cell.getNumericCellValue();
		else if (cell.getCellType() == 1)
			x = Long.parseLong(cell.getStringCellValue());
		else
			x = -1;
		return x;
	}

	private String getEmpName(XSSFCell cell) {
		String x;
		if (cell.getCellTypeEnum() == CellType.NUMERIC)
			x = String.valueOf(cell.getNumericCellValue());
		else
			x = cell.getStringCellValue();
		return x;
	}
}