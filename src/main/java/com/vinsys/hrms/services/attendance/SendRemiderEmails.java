package com.vinsys.hrms.services.attendance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.datamodel.attendance.VOEmployeeAttendanceReport;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.services.CandidateOverseasDetailsService;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/attendance")

public class SendRemiderEmails {
	private static final Logger logger = LoggerFactory.getLogger(CandidateOverseasDetailsService.class);

	@Autowired
	IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSEmployeeAcnDAO employeeAcnDAO;
	@Autowired
	EmailSender emailsender;

	@RequestMapping(method = RequestMethod.POST, value = "/sendreminders", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String sendReminders(@RequestBody VOEmployeeAttendanceReport employeeAttendanceReportParams)
			throws JsonGenerationException, JsonMappingException, IOException {

		List<AttendanceProcessedData> attendanceProcessedDataList = new ArrayList<>();
		try {

			List<String> status = new ArrayList<>();
			status.add("A");
			status.add("HD");
			if (!HRMSHelper.isLongZero(employeeAttendanceReportParams.getEmployeeId())) {
				attendanceProcessedDataList = attendanceProcessedDataDAO.findByMonthEmpAndStatus(
						employeeAttendanceReportParams.getEmployeeId(),
						new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getFromDate()),
						new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getToDate()), status,
						employeeAttendanceReportParams.getOrgId());
			} else {
				attendanceProcessedDataList = attendanceProcessedDataDAO.findByMonthAndStatus(
						new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getFromDate()),
						new SimpleDateFormat("yyyy-MM-dd").parse(employeeAttendanceReportParams.getToDate()), status,
						employeeAttendanceReportParams.getOrgId());
			}

			Map<Long, List<AttendanceProcessedData>> attendanceByEmpMap = new HashMap<>();
			logger.info("Sending Attendance Reminder to: " + attendanceProcessedDataList.size());
			for (AttendanceProcessedData attendanceProcessedData : attendanceProcessedDataList) {

				if (!attendanceByEmpMap.containsKey(attendanceProcessedData.getCompositePrimaryKey().getEmpId())) {
					List<AttendanceProcessedData> list = new ArrayList<AttendanceProcessedData>();
					list.add(attendanceProcessedData);

					attendanceByEmpMap.put(attendanceProcessedData.getCompositePrimaryKey().getEmpId(), list);
				} else {
					attendanceByEmpMap.get(attendanceProcessedData.getCompositePrimaryKey().getEmpId()).add(attendanceProcessedData);
				}
			}
			for (long empID : attendanceByEmpMap.keySet()) {
				List<AttendanceProcessedData> empAttendanceList = new ArrayList<>();
				empAttendanceList = attendanceByEmpMap.get(empID);
				Employee empObj = employeeDAO.findById(empID).get();

				if (!HRMSHelper.isNullOrEmpty(empObj) && !empObj.getEmployeeACN().getIsManagement()) {
					Map<String, String> placeHolderMapping = HRMSRequestTranslator
							.createPlaceHolderMapAttendanceDefaulters(empAttendanceList, empObj);

					String mailContent = HRMSHelper.replaceString(placeHolderMapping,
							IHRMSEmailTemplateConstants.EMAIL_TEMPLATE_ATTENDANCE_DEFAULTERS);
					// String ccEmailId =
					// empAcnMap.get(attendanceProcessedData.getCompositePrimaryKey().getEmployeeACN()).getEmployeeReportingManager().getId();
					// String ccEmailId = empObj.getEmployeeReportingManager().get;

					// if (empID == 1200) {

					emailsender.toPersistEmail(empObj.getOfficialEmailId(), "", mailContent,
							"Attendance : " + new SimpleDateFormat("MMM y").format(new SimpleDateFormat("yyyy-MM-dd")
									.parse(employeeAttendanceReportParams.getToDate())),
							empObj.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							empObj.getCandidate().getLoginEntity().getOrganization().getId());

					// }
					logger.info("Attendance Reminder Email Sent to " + empObj.getOfficialEmailId());
				}

			}

		} catch (Exception e) {
			return HRMSHelper.sendSuccessResponse(e.getMessage(), IHRMSConstants.EmailSendingFailedCode);
		}
		return HRMSHelper.sendSuccessResponse("Success", IHRMSConstants.successCode);
	}

	public String sendAttendanceReminders(long orgId)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<AttendanceProcessedData> attendanceProcessedDataList = new ArrayList<>();
		try {
			List<String> status = new ArrayList<>();
			status.add("A");
			status.add("HD");

			LocalDate fDate = LocalDate.now();
			LocalDate tDate = LocalDate.now();

			// change in attendance cycle 28/12/2020 by SK
			fDate = LocalDate.of(fDate.getYear(), fDate.getMonth(), 1);
			/*
			 * if (fDate.getDayOfMonth() <= 25) { if(fDate.getMonthValue()==1) { fDate =
			 * LocalDate.of(fDate.minusYears(1).getYear(), fDate.minusMonths(1).getMonth(),
			 * 26); } else { fDate = LocalDate.of(fDate.getYear(),
			 * fDate.minusMonths(1).getMonth(), 26); } } else { fDate =
			 * LocalDate.of(fDate.getYear(), fDate.getMonth(), 26); }
			 */

			attendanceProcessedDataList = attendanceProcessedDataDAO.findByMonthAndStatus(
					Date.from(fDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
					Date.from(tDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), status, orgId);
			Map<Long, List<AttendanceProcessedData>> attendanceByEmpMap = new HashMap<>();
			logger.info("Sending Attendance Reminder to: " + attendanceProcessedDataList.size());

			for (AttendanceProcessedData attendanceProcessedData : attendanceProcessedDataList) {

				if (!attendanceByEmpMap.containsKey(attendanceProcessedData.getCompositePrimaryKey().getEmpId())) {
					List<AttendanceProcessedData> list = new ArrayList<AttendanceProcessedData>();
					list.add(attendanceProcessedData);

					attendanceByEmpMap.put(attendanceProcessedData.getCompositePrimaryKey().getEmpId(), list);
				} else {
					attendanceByEmpMap.get(attendanceProcessedData.getCompositePrimaryKey().getEmpId()).add(attendanceProcessedData);
				}
			}
			for (long empID : attendanceByEmpMap.keySet()) {
				List<AttendanceProcessedData> empAttendanceList = new ArrayList<>();
				empAttendanceList = attendanceByEmpMap.get(empID);
				Employee empObj = employeeDAO.findById(empID).get();

				if (!HRMSHelper.isNullOrEmpty(empObj) && !empObj.getEmployeeACN().getIsManagement()) {
					Map<String, String> placeHolderMapping = HRMSRequestTranslator
							.createPlaceHolderMapAttendanceDefaulters(empAttendanceList, empObj);

					String mailContent = HRMSHelper.replaceString(placeHolderMapping,
							IHRMSEmailTemplateConstants.EMAIL_TEMPLATE_ATTENDANCE_DEFAULTERS);
					// String ccEmailId =
					// empAcnMap.get(attendanceProcessedData.getCompositePrimaryKey().getEmployeeACN()).getEmployeeReportingManager().getId();
					// String ccEmailId = empObj.getEmployeeReportingManager().get;

					// if (empID == 1200) {

					emailsender.toPersistEmail(empObj.getOfficialEmailId(), "", mailContent, "Attendance Reminder",
							empObj.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							empObj.getCandidate().getLoginEntity().getOrganization().getId());

					// }
					logger.info("Attendance Reminder Email Sent to " + empObj.getOfficialEmailId());
				}

			}
		} catch (Exception e) {
			return HRMSHelper.sendSuccessResponse(e.getMessage(), IHRMSConstants.EmailSendingFailedCode);
		}
		return HRMSHelper.sendSuccessResponse("Success", IHRMSConstants.successCode);
	}

	public String sendAttendanceDubaiReminders(long orgId)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<AttendanceProcessedData> attendanceProcessedDataList = new ArrayList<>();
		try {
			List<String> status = new ArrayList<>();
			status.add("A");
			status.add("HD");

			LocalDate fDate = LocalDate.now();
			LocalDate tDate = LocalDate.now();

			fDate = LocalDate.of(fDate.getYear(), fDate.getMonth(), 1);

			attendanceProcessedDataList = attendanceProcessedDataDAO.findByMonthAndStatusDubai(
					Date.from(fDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
					Date.from(tDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), status, orgId);
			Map<Long, List<AttendanceProcessedData>> attendanceByEmpMap = new HashMap<>();
			logger.info("Sending Dub Attendance Reminder to: " + attendanceProcessedDataList.size());

			for (AttendanceProcessedData attendanceProcessedData : attendanceProcessedDataList) {

				if (!attendanceByEmpMap.containsKey(attendanceProcessedData.getCompositePrimaryKey().getEmpId())) {
					List<AttendanceProcessedData> list = new ArrayList<AttendanceProcessedData>();
					list.add(attendanceProcessedData);

					attendanceByEmpMap.put(attendanceProcessedData.getCompositePrimaryKey().getEmpId(), list);
				} else {
					attendanceByEmpMap.get(attendanceProcessedData.getCompositePrimaryKey().getEmpId()).add(attendanceProcessedData);
				}
			}
			for (long empID : attendanceByEmpMap.keySet()) {
				List<AttendanceProcessedData> empAttendanceList = new ArrayList<>();
				empAttendanceList = attendanceByEmpMap.get(empID);
				Employee empObj = employeeDAO.findById(empID).get();

				if (!HRMSHelper.isNullOrEmpty(empObj) && !empObj.getEmployeeACN().getIsManagement()) {
					Map<String, String> placeHolderMapping = HRMSRequestTranslator
							.createPlaceHolderMapAttendanceDefaulters(empAttendanceList, empObj);

					String mailContent = HRMSHelper.replaceString(placeHolderMapping,
							IHRMSEmailTemplateConstants.EMAIL_TEMPLATE_ATTENDANCE_DEFAULTERS);
					// String ccEmailId =
					// empAcnMap.get(attendanceProcessedData.getCompositePrimaryKey().getEmployeeACN()).getEmployeeReportingManager().getId();
					// String ccEmailId = empObj.getEmployeeReportingManager().get;

					// if (empID == 1200) {

					emailsender.toPersistEmail(empObj.getOfficialEmailId(), "", mailContent, "Attendance Reminder",
							empObj.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							empObj.getCandidate().getLoginEntity().getOrganization().getId());

					// }
					logger.info("Attendance Reminder Email Sent to " + empObj.getOfficialEmailId());
				}

			}
		} catch (Exception e) {
			return HRMSHelper.sendSuccessResponse(e.getMessage(), IHRMSConstants.EmailSendingFailedCode);
		}
		return HRMSHelper.sendSuccessResponse("Success", IHRMSConstants.successCode);
	}

}
