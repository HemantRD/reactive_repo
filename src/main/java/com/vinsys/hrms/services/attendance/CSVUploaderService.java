package com.vinsys.hrms.services.attendance;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceCSVDataSummaryDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceCsvDataDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceSwipeDataDAO;
import com.vinsys.hrms.datamodel.attendance.AttendanceStatusDetails;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.attendance.AttendanceCSVData;
import com.vinsys.hrms.entity.attendance.AttendanceCSVDataSummary;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedDataKey;
import com.vinsys.hrms.entity.attendance.AttendanceSwipeData;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.services.CandidateOverseasDetailsService;
import com.vinsys.hrms.util.HRMSAttendanceHelper;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/attendance")

public class CSVUploaderService {
	private static final Logger logger = LoggerFactory.getLogger(CandidateOverseasDetailsService.class);

	@Autowired
	IHRMSAttendanceCsvDataDAO attendanceCsvDataDAO;
	@Autowired
	IHRMSAttendanceCSVDataSummaryDAO attendanceCSVDataSummaryDAO;
	@Autowired
	IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO;
	@Autowired
	IHRMSEmployeeAcnDAO employeeAcnDAO;
	@Autowired
	HRMSAttendanceHelper attendanceHelper;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSEmployeeSeparationDAO employeeSeparationDAO;
	@Autowired
	IHRMSAttendanceSwipeDataDAO attendanceSwipeDataDAO;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("dataSourceSQLServer")
	private DataSource dataSourceSQLServer;

	@RequestMapping(value = "/csvuploader/{orgId}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String uploadCSV(@RequestParam("file") MultipartFile uploadedInputStream, @PathVariable("orgId") long orgId)
			throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();

		String extension = FilenameUtils.getExtension(uploadedInputStream.getOriginalFilename());

		if (extension.equals("csv")) {
			logger.info("UPLOADING CSV::" + uploadedInputStream.getOriginalFilename());

			readAndUploadCSV(uploadedInputStream, orgId);
			// workbook = csvToEXCEL(uploadedInputStream);
			// processUploadedData(orgId);

			return "SUCCESS";
		} else if (extension.equals("xls") || extension.equals("xlsx")) {
			logger.info("UPLOADING EXCEL::" + uploadedInputStream.getOriginalFilename());

			readAndUploadExcel(uploadedInputStream, orgId);
			return "SUCCESS";
		} else if (extension.equals("txt")) {
			logger.info("UPLOADING TXT::" + uploadedInputStream.getOriginalFilename());
			readAndUploadTXT(uploadedInputStream, orgId);
			return "SUCCESS";
		} else
			return IHRMSConstants.INVALID_FILE_FORMAT;
	}

	private void readAndUploadTXT(MultipartFile uploadedInputStream, long orgId) {
		InputStream is = null;
		BufferedReader bfReader = null;
		try {
			is = new ByteArrayInputStream(uploadedInputStream.getBytes());
			bfReader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = bfReader.readLine()) != null && line.trim().length() > 0) {
				logger.info(line);

				String[] data;
				data = line.split("\\|");
				/*
				 * LINE FORMAT SHOULD BE::::::::: {Pin}|{FirstName}{LastName}|{Date}
				 * {Time}|{Type}|{Area}|{DeviceName}|{DeviceSN}\r\n
				 */
				AttendanceCSVData attendanceCSVData = new AttendanceCSVData();
				try {
					attendanceCSVData.setEventId(null != data[0] ? Long.parseLong(data[0].trim()) : 0);

					List<SimpleDateFormat> formatters = new ArrayList<>();
					formatters.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
					formatters.add(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

					for (SimpleDateFormat formatter : formatters) {
						try {
							attendanceCSVData.setSwapTime(null != data[2] ? formatter.parse(data[2].trim()) : null);
							attendanceCSVData.setSwapDate(null != data[2] ? formatter.parse(data[2].trim()) : null);
							break;
						} catch (Exception e) {

						}
					}

					/*
					 * attendanceCSVData.setSwapTime( null != data[2] ? new
					 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data[2].trim()) : null);
					 * attendanceCSVData.setSwapDate( null != data[2] ? new
					 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data[2].trim()) : null);
					 */

					attendanceCSVData.setPanelID(null != data[6] ? data[6].trim() : "");
					attendanceCSVData.setEventPoint(null != data[6] ? data[6].trim() : "");
					// attendanceCSVData.setPanelID(null != data[6] ? Long.parseLong(data[6].trim())
					// : 0);
					// attendanceCSVData.setEventPoint(null != data[6] ?
					// Long.parseLong(data[6].trim()) : 0);

					attendanceCSVData.setEventPointName(null != data[5] ? data[5].trim() : "");
					attendanceCSVData.setDescription(null != data[4] ? data[4].trim() : "");
					attendanceCSVData.setCardNo(null != data[0] ? Long.parseLong(data[0].trim()) : 0);
					attendanceCSVData.setCardHolderName(null != data[1] ? data[1].trim() : "");

					attendanceCSVData.setOrgId(orgId);
					attendanceCSVData.setProcessed(false);
					attendanceCSVData.setHashId(getTxtHashCode(line.trim()));

					attendanceCSVData.setCreatedBy("System");
					attendanceCSVData.setCreatedDate(new Date());
					attendanceCSVData.setIsActive("Y");

					attendanceCsvDataDAO.save(attendanceCSVData);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception ex) {

			}
		}

	}

	private long getTxtHashCode(String line) {
		return line.hashCode();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/processCSVData", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Transactional
	// @Scheduled(fixedDelay = 6000, initialDelay = 5000)
	// @Scheduled(cron = "0 0 6,12,15,16,18,21 * * *")
	// @Scheduled(cron = "0 20 15,23 * * *")
	//@Scheduled(cron = "0 20 1,9,15,21 * * *")
	private void processUploadedData() {
		long orgId = 1;
		logger.info("PROCSSING ATTENDACNE::::");

		// attendanceCsvDataDAO.updateStatusForProcessing(java.sql.Date.valueOf(LocalDate.now().minusDays(3)),
		// orgId);
		List<AttendanceCSVDataSummary> csvDataList = new ArrayList<>();
		csvDataList = attendanceCSVDataSummaryDAO.findByOrgIdIsProcessedIsActive(orgId, false);

		for (AttendanceCSVDataSummary attendanceCSVData : csvDataList) {
			AttendanceCSVData attendanceCsvData = new AttendanceCSVData();
			AttendanceProcessedData csvProcessedData = new AttendanceProcessedData();

			try {
				String manHours = "";
				double NewOvertime = 0;
				long timeDiff = 0;
				if (!HRMSHelper.isNullOrEmpty(attendanceCSVData.getEndTime())
						&& !HRMSHelper.isNullOrEmpty(attendanceCSVData.getStartTime())) {
					timeDiff = attendanceCSVData.getEndTime().getTime() - attendanceCSVData.getStartTime().getTime();
					NewOvertime = TimeUnit.MILLISECONDS.toMinutes(timeDiff) - (9 * 60);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(timeDiff);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff)
							- TimeUnit.HOURS.toMinutes(diffInHours);
					manHours = String.format("%02d.%02d", diffInHours, diffInMinutes);
				} else
					manHours = "0";
				AttendanceProcessedDataKey compositePrimaryKey = new AttendanceProcessedDataKey();
				compositePrimaryKey.setEmployeeACN(attendanceCSVData.getCardNo());
				compositePrimaryKey.setAttendanceDate(attendanceCSVData.getAttendanceDate());
//				compositePrimaryKey.setOrgId(orgId);

				csvProcessedData.setCompositePrimaryKey(compositePrimaryKey);
				csvProcessedData.setStartTime(attendanceCSVData.getStartTime());
				csvProcessedData.setEndTime(attendanceCSVData.getEndTime());
				csvProcessedData.setManHours(Double.parseDouble(manHours));
				// csvProcessedData.setHashId(getProcessedCSVHashId(csvProcessedData));
				csvProcessedData.setCreatedBy("System");
				csvProcessedData.setCreatedDate(new Date());
				csvProcessedData.setIsActive("Y");

				/*
				 * csvProcessedData.setOverTime( csvProcessedData.getManHours() > 9 ?
				 * csvProcessedData.getManHours() - 9 : 0);
				 */

				csvProcessedData.setOverTime(NewOvertime > 0 ? NewOvertime : 0);

				/*
				 * LocalTime mHours =
				 * LocalTime.parse(String.valueOf(csvProcessedData.getManHours()),
				 * DateTimeFormatter.ofPattern("H.mm")); int hour =
				 * mHours.get(ChronoField.CLOCK_HOUR_OF_DAY); int minute =
				 * mHours.get(ChronoField.MINUTE_OF_HOUR); int second =
				 * mHours.get(ChronoField.SECOND_OF_MINUTE);
				 * csvProcessedData.setOver(csvProcessedData.getManHours() > 9 ?
				 * mHours.minusHours(9):null);
				 */

				// TODO
				Employee empObj = employeeAcnDAO.findByACNEmpObj(attendanceCSVData.getCardNo(), orgId);

				// Added new logic as per new Attendance system Machine on 23rd Feb 21
				// Employee empObj =
				// employeeDAO.findActiveEmployeeById(attendanceCSVData.getEmployeeId(),
				// IHRMSConstants.isActive);

				if (!HRMSHelper.isNullOrEmpty(empObj)) {
					csvProcessedData.getCompositePrimaryKey().setEmpId(empObj.getId());
					csvProcessedData.setEmpName(
							empObj.getCandidate().getFirstName() + " " + empObj.getCandidate().getLastName());

					csvProcessedData
							.setBranchId(empObj.getCandidate().getCandidateProfessionalDetail().getBranch().getId());
					csvProcessedData.setDivisionId(
							empObj.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
					csvProcessedData.setDepartmentId(
							empObj.getCandidate().getCandidateProfessionalDetail().getDepartment().getId());
					csvProcessedData.setDesignationId(
							empObj.getCandidate().getCandidateProfessionalDetail().getDesignation().getId());

					csvProcessedData.setBranchName(
							empObj.getCandidate().getCandidateProfessionalDetail().getBranch().getBranchName());
					csvProcessedData.setDivisionName(
							empObj.getCandidate().getCandidateProfessionalDetail().getDivision().getDivisionName());
					csvProcessedData.setDepartmentName(
							empObj.getCandidate().getCandidateProfessionalDetail().getDepartment().getDepartmentName());
					csvProcessedData.setDesignationName(empObj.getCandidate().getCandidateProfessionalDetail()
							.getDesignation().getDesignationName());

					csvProcessedData.setRemark("System Auto Processed");
				} else
					csvProcessedData.setRemark("Card Not Assigned");

				AttendanceStatusDetails statusDetails = new AttendanceStatusDetails();
				statusDetails = attendanceHelper.getAttendanceStatus(csvProcessedData, true);

				csvProcessedData.setStatus(statusDetails.getStatus());
				csvProcessedData.setLeaveType(statusDetails.getLeaveType());
				csvProcessedData.setLop(statusDetails.getLopCount());

				// csvProcessedData.setStatus(attendanceHelper.getAttendanceStatus(csvProcessedData,
				// true));

				if (csvProcessedData.getStatus().equals("H") || csvProcessedData.getStatus().equals("WO")) {
					// csvProcessedData.setOverTime(csvProcessedData.getManHours());
					csvProcessedData.setOverTime(TimeUnit.MILLISECONDS.toMinutes(timeDiff));
				}

				int isResignedEmp = employeeSeparationDAO.countByEmployeeActualRelievingDateIsActive(empObj.getId(),
						attendanceCSVData.getAttendanceDate(), "Y");

				if (isResignedEmp == 0) {
					attendanceProcessedDataDAO.save(csvProcessedData);
				}

				attendanceCsvDataDAO.updateStatus(csvProcessedData.getCompositePrimaryKey().getAttendanceDate(),
						csvProcessedData.getCompositePrimaryKey().getEmployeeACN(),
						csvProcessedData.getCompositePrimaryKey().getOrgId());

			} catch (NumberFormatException e) {

				e.printStackTrace();

			} catch (IncorrectResultSizeDataAccessException nonUniqueExp) {
				csvProcessedData.setRemark("Duplicate Card Found");

				attendanceProcessedDataDAO.save(csvProcessedData);

				attendanceCsvDataDAO.updateStatus(csvProcessedData.getCompositePrimaryKey().getAttendanceDate(),
						csvProcessedData.getCompositePrimaryKey().getEmployeeACN(),
						csvProcessedData.getCompositePrimaryKey().getOrgId());

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	}

	private void readAndUploadCSV(MultipartFile uploadedInputStream, long orgId) {
		try {
			CSVReader csvReader = new CSVReader(new InputStreamReader(uploadedInputStream.getInputStream()));
			String[] nextRecord;

			while ((nextRecord = csvReader.readNext()) != null) {

				AttendanceCSVData attendanceCSVData = new AttendanceCSVData();
				try {
					attendanceCSVData.setEventId(null != nextRecord[0] ? Long.parseLong(nextRecord[0].trim()) : 0);
					attendanceCSVData.setSwapTime(null != nextRecord[1]
							? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nextRecord[1].trim())
							: null);
					attendanceCSVData.setSwapDate(null != nextRecord[1]
							? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nextRecord[1].trim())
							: null);
					attendanceCSVData.setPanelID(null != nextRecord[2] ? nextRecord[2].trim() : "");
					attendanceCSVData.setEventPoint(null != nextRecord[3] ? nextRecord[3].trim() : "");
					// attendanceCSVData.setPanelID(null != nextRecord[2] ?
					// Long.parseLong(nextRecord[2].trim()) : 0);
					// attendanceCSVData.setEventPoint(null != nextRecord[3] ?
					// Long.parseLong(nextRecord[3].trim()) : 0);
					attendanceCSVData.setEventPointName(null != nextRecord[4] ? nextRecord[4].trim() : "");
					attendanceCSVData.setDescription(null != nextRecord[5] ? nextRecord[5].trim() : "");
					attendanceCSVData.setCardNo(null != nextRecord[6] ? Long.parseLong(nextRecord[6].trim()) : 0);
					attendanceCSVData.setCardHolderName(null != nextRecord[7] ? nextRecord[7].trim() : "");

					attendanceCSVData.setOrgId(orgId);
					attendanceCSVData.setProcessed(false);
					attendanceCSVData.setHashId(getCsvHashCode(nextRecord));

					attendanceCSVData.setCreatedBy("System");
					attendanceCSVData.setCreatedDate(new Date());
					attendanceCSVData.setIsActive("Y");

					AttendanceCSVData existingSwapData = attendanceCsvDataDAO.findById(attendanceCSVData.getHashId())
							.get();

					if (HRMSHelper.isNullOrEmpty(existingSwapData)) {
						attendanceCsvDataDAO.save(attendanceCSVData);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private long getCsvHashCode(String[] nextRecord) {
		return (nextRecord[0] + nextRecord[1] + nextRecord[2] + nextRecord[3] + nextRecord[4] + nextRecord[5]
				+ nextRecord[6] + nextRecord[7]).hashCode();
	}

	private void readAndUploadExcel(MultipartFile uploadedInputStream, long orgId) throws IOException {
		XSSFWorkbook workbook;
		workbook = new XSSFWorkbook(uploadedInputStream.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		// Reads the data in excel file until last row is encountered
		int totalCount = worksheet.getLastRowNum() - worksheet.getFirstRowNum();
		int i = 1;
		while (i <= worksheet.getLastRowNum()) {
			AttendanceCSVData attendanceCSVData = new AttendanceCSVData();
			try {
				XSSFRow row = worksheet.getRow(i++);

				attendanceCSVData.setEventId(getLongValue(row.getCell(0)));
				attendanceCSVData.setSwapTime(getDateValue(row.getCell(1)));
				attendanceCSVData.setSwapDate(getDateValue(row.getCell(1)));
				attendanceCSVData.setPanelID(getStringValue(row.getCell(2)));
				attendanceCSVData.setEventPoint(getStringValue(row.getCell(3)));
				// attendanceCSVData.setPanelID(getLongValue(row.getCell(2)));
				// attendanceCSVData.setEventPoint(getLongValue(row.getCell(3)));
				attendanceCSVData.setEventPointName(getStringValue(row.getCell(4)));
				attendanceCSVData.setDescription(getStringValue(row.getCell(5)));
				attendanceCSVData.setCardNo(getLongValue(row.getCell(6)));
				attendanceCSVData.setCardHolderName(getStringValue(row.getCell(7)));
				attendanceCSVData.setOrgId(orgId);
				attendanceCSVData.setProcessed(false);
				attendanceCSVData.setHashId(getExcelHashCode(attendanceCSVData));

				attendanceCSVData.setCreatedBy("System");
				attendanceCSVData.setCreatedDate(new Date());
				attendanceCSVData.setIsActive("Y");

				AttendanceCSVData existingSwapData = attendanceCsvDataDAO.findById(attendanceCSVData.getHashId()).get();

				if (HRMSHelper.isNullOrEmpty(existingSwapData)) {
					attendanceCsvDataDAO.save(attendanceCSVData);
				}

				// attendanceCsvDataDAO.save(attendanceCSVData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		workbook.close();
	}

	private Date getDateValue(XSSFCell cell) throws ParseException {

		Date aDate;
		if (cell.getCellTypeEnum() == CellType.STRING)
			aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cell.getStringCellValue().trim());
		else
			aDate = cell.getDateCellValue();
		return aDate;
	}

	private XSSFWorkbook csvToEXCEL(MultipartFile uploadedInputStream) {
		try {
			String csvFileAddress = "test.csv"; // csv file address
			String xlsxFileAddress = "test.xlsx"; // xlsx file address
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet1");
			String currentLine = null;
			int RowNum = 0;
			BufferedReader br = new BufferedReader(
					new InputStreamReader(uploadedInputStream.getInputStream(), "UTF-8"));
			while ((currentLine = br.readLine()) != null) {
				String str[] = currentLine.split(",");
				RowNum++;
				XSSFRow currentRow = sheet.createRow(RowNum);
				for (int i = 0; i < str.length; i++) {
					currentRow.createCell(i).setCellValue(str[i]);
				}
			}

			return workBook;

		} catch (Exception ex) {
			logger.error("Error :::: ", ex);
		}
		return null;
	}

	private long getExcelHashCode(AttendanceCSVData attendanceCSVData) {
		// TODO Auto-generated method stub
		if (!HRMSHelper.isNullOrEmpty(attendanceCSVData)) {

			try {
				StringBuffer strBuff = new StringBuffer();
				strBuff.append(!HRMSHelper.isLongZero(attendanceCSVData.getEventId())
						? String.valueOf(attendanceCSVData.getEventId())
						: "");
				strBuff.append(attendanceCSVData.getSwapTime() != null
						? HRMSDateUtil.format(attendanceCSVData.getSwapTime(), "yyyy-MM-dd HH:mm:ss")
						: "");

				strBuff.append(attendanceCSVData.getPanelID() != null ? attendanceCSVData.getPanelID() : "");
				strBuff.append(attendanceCSVData.getEventPoint() != null ? attendanceCSVData.getEventPoint() : "");

				/*
				 * 
				 * strBuff.append(!HRMSHelper.isLongZero(attendanceCSVData.getPanelID()) ?
				 * String.valueOf(attendanceCSVData.getPanelID()) : "");
				 * strBuff.append(!HRMSHelper.isLongZero(attendanceCSVData.getEventPoint()) ?
				 * String.valueOf(attendanceCSVData.getEventPoint()) : "");
				 */
				strBuff.append(
						attendanceCSVData.getEventPointName() != null ? attendanceCSVData.getEventPointName() : "");
				strBuff.append(attendanceCSVData.getDescription() != null ? attendanceCSVData.getDescription() : "");
				strBuff.append(!HRMSHelper.isLongZero(attendanceCSVData.getCardNo())
						? String.valueOf(attendanceCSVData.getCardNo())
						: "");
				strBuff.append(
						attendanceCSVData.getCardHolderName() != null ? attendanceCSVData.getCardHolderName() : "");
				strBuff.append(!HRMSHelper.isLongZero(attendanceCSVData.getOrgId())
						? String.valueOf(attendanceCSVData.getOrgId())
						: "");
				return strBuff.toString().hashCode();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return 0;
	}

	private long getLongValue(XSSFCell cell) {
		// TODO Auto-generated method stub
		long x;
		if (cell.getCellTypeEnum() == CellType.STRING)
			x = Long.valueOf(cell.getStringCellValue());
		else
			x = (long) cell.getNumericCellValue();
		return x;
	}

	private String getStringValue(XSSFCell cell) {
		// TODO Auto-generated method stub
		String x;
		if (cell.getCellTypeEnum() == CellType.NUMERIC)
			x = String.valueOf(cell.getNumericCellValue());
		else
			x = cell.getStringCellValue();
		return x;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/addMissingData", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	// @Scheduled(fixedDelay = 60000*60*12, initialDelay = 5000)
	// @Scheduled(cron = "0 0 3 * * *")
	@Scheduled(cron = "0 5 19,22 * * *")
	public String addMissingSwipeData() {
		long orgId = 1;
		logger.info(":::::::::Adding Missing Entries:::::::");
		try {
			List<EmployeeACN> employeeACNList = new ArrayList<>();
			employeeACNList = employeeAcnDAO.findByIsActive("Y");

//			LocalDate currDate = LocalDate.now();
			LocalDate currDate = LocalDate.now().plusDays(1);

			// LocalDate currDate = LocalDate.now().minusDays(1);

			for (LocalDate date = currDate.minusDays(3); date.isBefore(currDate); date = date.plusDays(1)) {
				logger.info(":::::::::Adding Missing Entries inside for Date:::::::" + date);
				for (EmployeeACN empAcn : employeeACNList) {
					// if(empAcn.getEmpACN()==14904) {
					try {
						int i = attendanceCsvDataDAO.countByCardNoAndSwapDate(empAcn.getEmpACN(),
								java.sql.Date.valueOf(date));

						List<Employee> empList = employeeDAO.findByEmployeeDOJ("Y", empAcn.getEmployee().getId(),
								java.sql.Date.valueOf(date));

						int isResigned = employeeSeparationDAO.countByEmployeeRelievingDateMissingData(
								empAcn.getEmployee().getId(),
								Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), "Y");

						/*
						 * if(empAcn.getEmployee().getId()==1200 &&
						 * date.isAfter(currDate.minusDays(15))) { //System.out.println("RESIGNED"); }
						 */
						if (i == 0 && empList.size() != 0 && isResigned == 0) {
							AttendanceCSVData attendanceCsvData = new AttendanceCSVData();
							attendanceCsvData.setCardNo(empAcn.getEmpACN());
							attendanceCsvData.setSwapDate(java.sql.Date.valueOf(date));
							attendanceCsvData.setIsActive("N");
							attendanceCsvData.setCreatedBy("System Schedular");
							attendanceCsvData.setCreatedDate(new Date());
							attendanceCsvData.setRemark("Entry for Missing Data");
							attendanceCsvData.setOrgId(orgId);
							attendanceCsvData.setProcessed(false);
							attendanceCsvData.setSwapTime(null);
							attendanceCsvData.setHashId(getHashForMissedData(attendanceCsvData));

							logger.info(":::::::::Adding Missing Entries inside for Card:::::::" + empAcn.getEmpACN());

							attendanceCsvDataDAO.save(attendanceCsvData);

						}

					} catch (Exception e) {
						logger.error("ERROR::", e);
					}
					// }//END OF IF CONDITION
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Fail";
		}

		return "Success";

	}

	private long getHashForMissedData(AttendanceCSVData attendanceCsvData) {
		if (!HRMSHelper.isNullOrEmpty(attendanceCsvData)) {
			try {
				StringBuffer strBuff = new StringBuffer();

				strBuff.append(attendanceCsvData.getSwapDate() != null
						? HRMSDateUtil.format(attendanceCsvData.getSwapDate(), "yyyy-MM-dd")
						: "");
				strBuff.append(!HRMSHelper.isLongZero(attendanceCsvData.getCardNo())
						? String.valueOf(attendanceCsvData.getCardNo())
						: "");
				strBuff.append(!HRMSHelper.isLongZero(attendanceCsvData.getOrgId())
						? String.valueOf(attendanceCsvData.getOrgId())
						: "");
				return strBuff.toString().hashCode();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return 0;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/updateAttendanceData", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Scheduled(cron = "0 45 9,10,11,12,13,14,15,16,17,18,19,20 * * *")
	// @Scheduled(cron = "0 0 2 * * *")
	public void UpdateProcessedData() {
		LocalDate currDate = LocalDate.now();

		long orgId = 1;
		Date fDate = java.sql.Date.valueOf(currDate.minusDays(31));
		Date tDate = java.sql.Date.valueOf(currDate);

		List<AttendanceProcessedData> attendanceProcessedDataList = new ArrayList<>();
		List<String> status = new ArrayList<>();
		status.add("H");
		status.add("WO");
		status.add("P");
		attendanceProcessedDataList = attendanceProcessedDataDAO.findByDateOrgIDStatus(fDate, tDate, orgId, status);

		for (AttendanceProcessedData attendanceProcessedData : attendanceProcessedDataList) {

			try {
				AttendanceStatusDetails statusDetails = new AttendanceStatusDetails();
				statusDetails = attendanceHelper.getAttendanceStatus(attendanceProcessedData, false);
				if (attendanceProcessedData != null) {
					attendanceProcessedData.setStatus(statusDetails.getStatus());
					attendanceProcessedData.setLeaveType(statusDetails.getLeaveType());
					attendanceProcessedData.setLop(statusDetails.getLopCount());

					// attendanceProcessedData.setStatus(attendanceHelper.getAttendanceStatus(attendanceProcessedData,false));
					attendanceProcessedDataDAO.save(attendanceProcessedData);
				} else {
					attendanceProcessedDataList.add(attendanceProcessedData);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("Attendance Data Updated");
	}

	public void readAndUploadSwipes() {
		int recordInsertedCount = 0;
		Connection conn = null;
		try {

//			String dbURL = "jdbc:sqlserver://localhost\\sqlexpress";
//            String user = "";
//            String pass = "";
//            conn = DriverManager.getConnection(dbURL, user, pass);
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM SmartAlarmWithAccess_BlankNew.dbo.vw_Transactions_Swipes WHERE transaction_id > "+attendanceCsvDataDAO.getMaxTransactionId());
//            while(rs.next()) {
//				AttendanceCSVData attendanceCSVData = new AttendanceCSVData();
//				attendanceCSVData.setCardHolderName(rs.getString("card_holder_name"));
//				attendanceCSVData.setTransactionId(rs.getLong("transaction_id"));
//				attendanceCSVData.setEmployeeId(rs.getString("emp_code"));
//				attendanceCSVData.setCardNo(rs.getLong("card_no"));
//				attendanceCSVData.setChannelNo(rs.getLong("channel_no"));
//				java.sql.Timestamp timestamp = rs.getTimestamp("swipe_date");
//				Date date = new Date(timestamp.getTime());
//				attendanceCSVData.setSwapDate(date.toString());
//				attendanceCSVData.setControllerNo(rs.getLong("controller_no"));
//				attendanceCSVData.setCardHolderName(rs.getString("card_holder_name"));
//			
//
//            }
//            

			final Long maxTransId = getMaxTransactionId();

			String sql = "SELECT * FROM SmartAlarmWithAccess_BlankNew.dbo.vw_Transactions_Swipes WHERE transaction_id > ?";

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceSQLServer);

			List<AttendanceSwipeData> swipeDetails = jdbcTemplate.query(sql, new Object[] { maxTransId },
					new RowMapper<AttendanceSwipeData>() {
						public AttendanceSwipeData mapRow(ResultSet rs, int rowNum) throws SQLException {
							AttendanceSwipeData swipe = new AttendanceSwipeData();
							swipe.setTransactionId(rs.getLong("transaction_id"));
							swipe.setEmployeeCode(rs.getString("emp_code"));
							swipe.setCardNo(rs.getLong("card_no"));
							swipe.setChannelNo(rs.getLong("channel_no"));
							// swipe.setSwapDate((rs.getDate("swipe_date").toString()));
							java.sql.Timestamp timestamp = rs.getTimestamp("swipe_date");
							Date date = new Date(timestamp.getTime());
							swipe.setSwapDate(date.toString());
							swipe.setControllerNo(rs.getLong("controller_no"));
							swipe.setCardHolderName(rs.getString("card_holder_name"));
							return swipe;
						}
					});

			SimpleDateFormat f = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			for (AttendanceSwipeData swiperecord : swipeDetails) {
				AttendanceCSVData attendanceCSVData = new AttendanceCSVData();
				try {

					// swiperecord.getTransactionId());

					attendanceCSVData
							.setTransactionId(0 != swiperecord.getTransactionId() ? swiperecord.getTransactionId() : 0);

					Date swapTime = f.parse(swiperecord.getSwapDate());

					attendanceCSVData.setSwapTime(null != swapTime
							? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(format1.format(swapTime))
							: null);
					attendanceCSVData.setSwapDate(null != swiperecord.getSwapDate()
							? new SimpleDateFormat("yyyy-MM-dd").parse(format1.format(swapTime))
							: null);

					attendanceCSVData.setPanelID(Long.toString(swiperecord.getControllerNo()));

					attendanceCSVData.setDescription("Trace Card");
					attendanceCSVData.setCardNo(0 != swiperecord.getCardNo() ? swiperecord.getCardNo() : 0);
					attendanceCSVData.setCardHolderName(
							null != swiperecord.getCardHolderName() ? swiperecord.getCardHolderName().trim() : "");

					if (HRMSHelper.isDigit(
							null != swiperecord.getEmployeeCode() ? swiperecord.getEmployeeCode().trim() : "")) {
						attendanceCSVData.setEmployeeId(0 != Long.parseLong(swiperecord.getEmployeeCode())
								? Long.parseLong(swiperecord.getEmployeeCode())
								: 0);
					} else {
						attendanceCSVData.setEmployeeId(0);
					}
					attendanceCSVData.setOrgId(1);
					attendanceCSVData.setProcessed(false);
					// attendanceCSVData.setHashId(getSwipesHashCode(swiperecord));
					attendanceCSVData.setHashId(swiperecord.getTransactionId());

					attendanceCSVData.setCreatedBy("System");
					attendanceCSVData.setCreatedDate(new Date());
					attendanceCSVData.setIsActive("Y");
					// attendanceCSVData.getEmployeeId());
					AttendanceCSVData existingSwapData = attendanceCsvDataDAO.findById(attendanceCSVData.getHashId())
							.get();

					if (HRMSHelper.isNullOrEmpty(existingSwapData) && attendanceCSVData.getEmployeeId() != 0) {
						if (employeeDAO.existsById(attendanceCSVData.getEmployeeId())) {
							recordInsertedCount++;
							attendanceCsvDataDAO.save(attendanceCSVData);
						} else {
							logger.info(
									"Employee not present in HRMS:Employee Id-->" + attendanceCSVData.getEmployeeId());
						}
					} else {
						logger.info("Already processed or invalid :Employee Id-->" + attendanceCSVData.getEmployeeId());
					}

				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception in readAndUploadSwipes for loop:: ", e);
				}
			} // END OF FOR
			logger.info("Total Swipe records Fetch From Smart Access DB are " + swipeDetails.size());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in readAndUploadSwipes method ", e);
		}
		logger.info("Total Swipe records Inserted into HRMS DB are " + recordInsertedCount);
	}

	private long getSwipesHashCode(AttendanceSwipeData data) {
		String swipeHashStr = data.getTransactionId() + data.getEmployeeCode() + data.getCardNo() + data.getChannelNo()
				+ data.getSwapDate().toString() + data.getControllerNo() + data.getCardHolderName();
		return swipeHashStr.hashCode();
	}

	public Long getMaxTransactionId() {
		Long maxId = attendanceCsvDataDAO.getMaxTransactionId();
		return maxId;
	}
}