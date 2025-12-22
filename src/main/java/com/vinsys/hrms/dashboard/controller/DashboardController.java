package com.vinsys.hrms.dashboard.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dashboard.IDashboardService;
import com.vinsys.hrms.dashboard.vo.AttendanceGraphVO;
import com.vinsys.hrms.dashboard.vo.BirthdayVO;
import com.vinsys.hrms.dashboard.vo.GalleryVO;
import com.vinsys.hrms.dashboard.vo.LeaveSumarryDetailsResponseVO;
import com.vinsys.hrms.dashboard.vo.ServiceCompletionVO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/dashboardv2")
public class DashboardController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IDashboardService dashboardService;

	@Operation(summary = "API for employee birthday", description = "This API used for Birthday display on dashborad")
	@GetMapping(value = "birthdays", produces = "application/json")
	public HRMSBaseResponse<BirthdayVO> getBirthdayEvents() {
		HRMSBaseResponse<BirthdayVO> response = new HRMSBaseResponse<>();
		try {
			response = dashboardService.getBirthdayEvents();
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping(value = "servicecompletion", produces = "application/json")
	public HRMSBaseResponse<ServiceCompletionVO> getServiceCompletions() {
		HRMSBaseResponse<ServiceCompletionVO> response = new HRMSBaseResponse<>();
		try {
			response = dashboardService.getServiceCompletions();
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping(value = "gallery", produces = "application/json")
	public HRMSBaseResponse<GalleryVO> getGalleryImages() {
		HRMSBaseResponse<GalleryVO> response = new HRMSBaseResponse<>();
		try {
			response = dashboardService.getGalleryImages();
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping(value = "getAttendanceGraph", produces = "application/json")
	public HRMSBaseResponse<AttendanceGraphVO> empAttendanceGraphDetails(@RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate) {
		HRMSBaseResponse<AttendanceGraphVO> response = new HRMSBaseResponse<>();
		try {
			response = dashboardService.empAttendanceGraphDetails(fromDate, toDate);
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

	@Operation(summary = "EmployeeLeaveSummary", description = "This Api Use for Employee Leave Summarry Display On Dashborad")
	@GetMapping(value = "dashboardLeaveSummary")
	public HRMSBaseResponse<LeaveSumarryDetailsResponseVO> getEmployeeLeaveSummary() {
		HRMSBaseResponse<LeaveSumarryDetailsResponseVO> response = new HRMSBaseResponse<>();
		try {
			return dashboardService.getEmployeeLeaveSummary();
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
}
