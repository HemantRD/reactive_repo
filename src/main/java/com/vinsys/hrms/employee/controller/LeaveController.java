package com.vinsys.hrms.employee.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dashboard.vo.LeaveSumarryDetailsResponseVO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.VOAppliedLeaveCount;
import com.vinsys.hrms.datamodel.VOHolidayResponse;
import com.vinsys.hrms.employee.service.ILeaveService;
import com.vinsys.hrms.employee.vo.ApplyGrantLeaveRequestVO;
import com.vinsys.hrms.employee.vo.ApplyLeaveRequestVO;
import com.vinsys.hrms.employee.vo.AvailableLeavesVO;
import com.vinsys.hrms.employee.vo.EmployeeLeaveDetailsVO;
import com.vinsys.hrms.employee.vo.LeaveCalculationRequestVO;
import com.vinsys.hrms.employee.vo.SubOrdinateListVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/leavev2")
public class LeaveController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app_version}")
	private String applicationVersion;

	@Autowired
	ILeaveService leaveService;

	@Operation(summary = "API for apply leave", description = "This API used for apply all type of leave like PL,EL,OD,..,etc.")
	@PostMapping("apply")
	HRMSBaseResponse<?> applyLeave(@RequestBody ApplyLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.applyLeave(request);
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

	@Operation(summary = "API for cancel leave", description = "This API used for cancel all type of leave like PL,EL,OD,..,etc.")
	@PostMapping("cancel")
	HRMSBaseResponse<?> cancelLeave(@RequestBody ApplyLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.cancelLeave(request);
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

	@Operation(summary = "API for available leaves", description = "This API used to get available leave balance of all type of leave like PL,EL,OD,..,etc.")
	@GetMapping("availableleave")
	HRMSBaseResponse<AvailableLeavesVO> getAvailableLeave(@RequestParam Long leaveTypeId) {
		HRMSBaseResponse<AvailableLeavesVO> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.getAvailableLeave(leaveTypeId);
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

	@Operation(summary = "API for withdraw leave", description = "This API used to withdraw all type of leave like PL,EL,OD,..,etc.")
	@PostMapping("withdraw")
	HRMSBaseResponse<?> withdrawLeave(@RequestBody ApplyLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.withdrawLeave(request);
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

	@Operation(summary = "API for approve leave", description = "This API used to approve all type of leave like PL,EL,OD,..,etc.")
	@PostMapping("approve")
	HRMSBaseResponse<?> approveLeave(@RequestBody List<ApplyLeaveRequestVO> request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.approveLeave(request);
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

	@Operation(summary = "API for calculate leave", description = "This API used to calculate total leaves for all type of leave like PL,EL,OD,..,etc.")
	@PostMapping("calculateLeave")
	HRMSBaseResponse<VOAppliedLeaveCount> calculateLeave(@RequestBody LeaveCalculationRequestVO calculationRequestVO) {
		HRMSBaseResponse<VOAppliedLeaveCount> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.calculateLeave(calculationRequestVO);
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

	@Operation(summary = "API for reject leave", description = "This API used to reject all type of leave like PL,EL,OD,..,etc. In this API only Id is mandatory")
	@PostMapping("reject")
	HRMSBaseResponse<?> rejectLeave(@RequestBody ApplyLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.rejectLeave(request);
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

	/*
	 * if employee withdraw leave then manager have rights to approve/reject
	 */
	@Operation(summary = "API for withdraw approve leave", description = "This API used to approve leaves withdrawn by employee for all type of leave like PL,EL,OD,..,etc.")
	@PostMapping("withdrawapprove")
	HRMSBaseResponse<?> withdrawApproveLeave(@RequestBody ApplyLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = leaveService.withdrawApproveLeave(request);
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

	@Operation(summary = "API for withdra reject leave", description = "This API used to reject leaves withdrawn by employee for all type of leave like PL,EL,OD,..,etc.")
	@PostMapping("withdrawreject")
	HRMSBaseResponse<?> withdrawRejectLeave(@RequestBody ApplyLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = leaveService.withdrawRejectLeave(request);
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

	@Operation(summary = "API for get leave details for employee", description = "This API used to get all leaves applied by employee for all type of leave like PL,EL,OD,..,etc.")
	@GetMapping("getleavedetails")
	HRMSBaseResponse<EmployeeLeaveDetailsVO> getLeaveDetails(@RequestParam(required = false) Long employeeId,
			Pageable pageable) {
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse();
		try {

			response = leaveService.getLeaveDetails(employeeId, pageable);
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

	/*
	 * Grant leave API code start
	 */

	/*
	 * Apply Grant leave compOff
	 * 
	 */
	@Operation(summary = "API for apply grant leave", description = "This API used to apply grant leave.")
	@PostMapping("grantleave/apply")
	HRMSBaseResponse<?> grantLeaveApply(@RequestBody ApplyGrantLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = leaveService.applyGrantLeave(request);
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

	@Operation(summary = "API for cancel leave", description = "This API used to cancel grant leave.")
	@PostMapping("grantleave/cancel")
	HRMSBaseResponse<?> cancelGrantLeave(@RequestBody ApplyGrantLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = leaveService.cancelGrantLeave(request);
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

	/*
	 * : employee withdraw leave grant
	 */
	@Operation(summary = "API for withdraw grant leave", description = "This API used to withdraw grant leave.")
	@PostMapping("grantleave/withdraw")
	HRMSBaseResponse<?> withdrawGrantLeave(@RequestBody ApplyGrantLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = leaveService.withdrawGrantLeave(request);
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

	@Operation(summary = "API for approve grant leave", description = "This API used to approve grant leave. For approve grant leave only id is mandatory field.")
	@PostMapping("grantleave/approve")
	HRMSBaseResponse<?> approveGrantLeave(@RequestBody ApplyGrantLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.approveGrantLeave(request);
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

	@Operation(summary = "API for reject grant leave", description = "This API used to reject grant leave.")
	@PostMapping("grantleave/reject")
	HRMSBaseResponse<?> rejectGrantLeave(@RequestBody ApplyGrantLeaveRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = leaveService.rejectGrantLeave(request);
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

	@Operation(summary = "API to calculate grant leave", description = "This API used to calculate grant leave.")
	@PostMapping("calculateGrantLeave")
	HRMSBaseResponse<VOAppliedLeaveCount> calculateGrantLeave(
			@RequestBody LeaveCalculationRequestVO calculationRequestVO) {
		HRMSBaseResponse<VOAppliedLeaveCount> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.calculateGrantLeave(calculationRequestVO);
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

	@Operation(summary = "API to get grant leave details", description = "This API used to get grant leaves applied by employee.")
	@GetMapping("grantleave/getleavedetails")
	HRMSBaseResponse<EmployeeLeaveDetailsVO> getGrantLeaveDetails(@RequestParam(required = false) Long employeeId,
			Pageable pageable) {
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse<EmployeeLeaveDetailsVO>();
		try {
			response = leaveService.getGrantLeaveDetails(employeeId, pageable);
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

	@Operation(summary = "API to get all team leaves for manager", description = "This API used to get all team leaves for manager for all type of leave like PL,EL,OD,..,etc.")
	@GetMapping("getteamleavedetails")
	HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetails(
			@RequestParam(value = "employeeId", required = false) Long filterEmployeeId,
			@RequestParam(value = "year", required = false) Integer year, Pageable pageable) {
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse<EmployeeLeaveDetailsVO>();
		try {
			year = HRMSHelper.isNullOrEmpty(year) ? null : year;
			response = leaveService.getTeamLeaveDetails(filterEmployeeId, year, pageable);
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

	/*
	 * Grant leave code start
	 */

	@Operation(summary = "API to get grant leave details for manager", description = "This API used to get grant leaves for manager.")
	@GetMapping("grantleave/getteamleavedetails")
	HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamGrantLeaveDetails(Pageable pageable,
			@RequestParam(required = false) Long employeeId) {
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.getTeamGrantLeaveDetails(pageable, employeeId);
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

	@Operation(summary = "API to get holiday list", description = "This API used to get holiday list.")
	@PostMapping("holidaylist")
	HRMSBaseResponse<List<VOHolidayResponse>> holidayList(@RequestParam(required = false) Long year) {
		HRMSBaseResponse<List<VOHolidayResponse>> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.holidayList(year);
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

	@Operation(summary = "API to get employee list for manager", description = "This API used to get employee list for manager.")
	@GetMapping("findsubordinate")
	HRMSBaseResponse<SubOrdinateListVO> findSubordinate() {
		HRMSBaseResponse<SubOrdinateListVO> response = new HRMSBaseResponse<>();
		try {
			response = leaveService.findSubordinate();
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

	@Operation(summary = "get employee Leave Summary for manager ", description = "This Api Use to get Employee Leave Summarry in My teams")
	@GetMapping(value = "teamleavesummary")
	public HRMSBaseResponse<LeaveSumarryDetailsResponseVO> getEmployeeLeaveSummary(@RequestParam long empId,
			@RequestParam(required = false) Integer year) {
		HRMSBaseResponse<LeaveSumarryDetailsResponseVO> response = new HRMSBaseResponse<>();
		try {
			year = HRMSHelper.isNullOrEmpty(year) ? null : year;
			response = leaveService.getEmployeeLeaveSummary(empId, year);
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

	@Operation(summary = "download leave report", description = "This API will use to download self team leave report in Excel format")
	@GetMapping("/download/teamleavesreport")
	public ResponseEntity<?> downloadTeamLeavesReport(
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate,
			HttpServletResponse httpServletResponse) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			byte[] report= leaveService.downloadTeamLeavesReport(fromDate, toDate, httpServletResponse);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentLength(report.length);

			return ResponseEntity.ok(report);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

		}catch (IOException e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	@Operation(summary = "download leave report", description = "This API will use to download all employee from organization")
	@GetMapping("/download/allemployeeleavereport")
	public ResponseEntity<?> downloadAllEmployeeLeavesReport(
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate,
			HttpServletResponse httpServletResponse) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			byte[] report= leaveService.downloadAllEmployeeLeavesReport(fromDate, toDate, httpServletResponse);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentLength(report.length);
			
			return ResponseEntity.ok(report);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			
		}catch (IOException e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	@Operation(summary = "API to get all leaves for HR", description = "This API used to get all  leaves for HR for all type of leave like PL,EL,OD,..,etc.")
	@GetMapping("gethrleavedetails")
	HRMSBaseResponse<EmployeeLeaveDetailsVO> getLeaveDetailsForHr(
			@RequestParam(value = "employeeId", required = false) Long filterEmployeeId,
			@RequestParam(value = "year", required = false) Integer year, Pageable pageable) {
		HRMSBaseResponse<EmployeeLeaveDetailsVO> response = new HRMSBaseResponse<EmployeeLeaveDetailsVO>();
		try {
			year = HRMSHelper.isNullOrEmpty(year) ? null : year;
			response = leaveService.getLeaveDetailsForHr(filterEmployeeId, year, pageable);
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

}
