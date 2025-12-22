package com.vinsys.hrms.employee.controller;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.service.IAttendanceService;
import com.vinsys.hrms.employee.vo.AddSwipesVO;
import com.vinsys.hrms.employee.vo.AttendanceRequestVO;
import com.vinsys.hrms.employee.vo.AttendanceResponseVO;
import com.vinsys.hrms.employee.vo.EmployeeSwipeVO;
import com.vinsys.hrms.employee.vo.InOutStatusVO;
import com.vinsys.hrms.employee.vo.TeamAttendanceVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/attendancev2")
public class AttendanceController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IAttendanceService attendanceService;
	@Value("${app_version}")
	private String applicationVersion;

	@GetMapping("getattendancedetails")
	HRMSBaseResponse<AttendanceResponseVO> empAttendanceDetails(
			@RequestParam(value = "month", required = false) Integer month) {
		HRMSBaseResponse<AttendanceResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = attendanceService.empAttendanceDetails(month);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@PostMapping("getswipedetails")
	HRMSBaseResponse<EmployeeSwipeVO> employeeSwipeDetails(@RequestBody AttendanceRequestVO requestVO,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		HRMSBaseResponse<EmployeeSwipeVO> response = new HRMSBaseResponse<>();
		try {
			response = attendanceService.employeeSwipeDetails(requestVO, pageable);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for get Team Attendance", description = "This API used to get Team Attendance for manager")
	@GetMapping("getteamattendance")
	HRMSBaseResponse<AttendanceResponseVO> empTeamAttendanceDetails(
			@RequestParam(value = "month", required = false) Integer month, Long empId) {
		HRMSBaseResponse<AttendanceResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = attendanceService.empTeamAttendanceDetails(month, empId);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for download Employee Attendance report", description = "This API used download Employee Attendance report")
	@PostMapping("attendancereport")
	void empAttendanceReport(@RequestBody TeamAttendanceVO requestVO, HttpServletResponse res)
			throws JsonProcessingException, IOException {

		HRMSBaseResponse<?> baseResponse = new HRMSBaseResponse<>();
		try {
			attendanceService.empAttendanceReport(requestVO, res);
		} catch (ParseException | IOException | HRMSException e) {
			e.printStackTrace();
			baseResponse.setApplicationVersion(applicationVersion);
			baseResponse.setResponseMessage(e.getMessage());
			baseResponse.setResponseCode(1500);
		}
		ObjectMapper mapper = new ObjectMapper();
		res.getWriter().write(mapper.writeValueAsString(baseResponse));

	}

	@PostMapping("getteamswipedetails")
	HRMSBaseResponse teamSwipeDetails(@RequestBody TeamAttendanceVO requestVO,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			response = attendanceService.teamSwipeDetails(requestVO, pageable);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setResponseBody(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setResponseBody(e.getMessage());
		}
		return response;
	}

	@Operation(summary = "API to store In and out swipe details", description = "This API used to store In Out swipe time.")
	@PostMapping("addswipes")
	HRMSBaseResponse<EmployeeSwipeVO> addswipes(@RequestBody(required = false) AddSwipesVO request) {
		HRMSBaseResponse<EmployeeSwipeVO> response = new HRMSBaseResponse<>();
		try {
			attendanceService.addswipes(request);
			response = attendanceService.employeeSwipeDetails(new AttendanceRequestVO(), PageRequest.of(0, 10));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API will use for to check today in out status ", description = "This API used to store In Out swipe time.")
	@GetMapping("todayinoutstatus")
	HRMSBaseResponse<InOutStatusVO> getSwipeInOutStatus() {
		HRMSBaseResponse<InOutStatusVO> response = new HRMSBaseResponse<>();
		try {
			response = attendanceService.getSwipeInOutStatus();
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}
	
	@PostMapping("hr/getteamswipedetails")
	@Operation(summary = "This API is to get swipe detail of employee for HR ", description = "This API is to get swipe detail of employee for HR")
	HRMSBaseResponse teamSwipeDetailsForHR(@RequestBody TeamAttendanceVO requestVO,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			response = attendanceService.teamSwipeDetailsForHR(requestVO, pageable);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setResponseBody(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setResponseBody(e.getMessage());
		}
		return response;
	}
	
	@PostMapping("hr/reprocessattendance")
	@Operation(summary = "This API is to reprocess the attendance ", description = "you have to pass 'Date' as a parameter")
	public HRMSBaseResponse<?> reProcessAttendance(@RequestParam(value = "date", required = true) String date,@RequestParam(value = "division", required = true) Long divisionId) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = attendanceService.reProcessAttendance(date,divisionId);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setApplicationVersion(applicationVersion);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setApplicationVersion(applicationVersion);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));

		}
		return response;
	}
}
