package com.vinsys.hrms.employee.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.service.IOrganazationService;
import com.vinsys.hrms.employee.vo.EmployeeAddressVO;
import com.vinsys.hrms.employee.vo.EmployeeProfileVO;
import com.vinsys.hrms.employee.vo.EmployeeVO;
import com.vinsys.hrms.employee.vo.HRListResponseVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/organazationv2")
public class OrganazationController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IOrganazationService organazationService;

	@Operation(summary = "API for All Organzation Employee Details", description = "This API used to all employee deatils shows on Organzation details tab like empid,Name,Offical mail,location..,etc.")
	@GetMapping("getorganazationdetails")
	HRMSBaseResponse<List<EmployeeAddressVO>> findAllEmployeeAddressBook(@RequestParam(required = false) Long branchId,
			@RequestParam(required = false) String branch, @RequestParam(required = false) String keyword,@RequestParam(required = false) Long gradeId,@RequestParam(required = false) Long deptId,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		HRMSBaseResponse<List<EmployeeAddressVO>> response = new HRMSBaseResponse<>();
		try {

			response = organazationService.findAllEmployeeAddressBook(branchId, branch, keyword, gradeId, deptId,
					pageable);
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

	@GetMapping("detailsbydepartment")
	HRMSBaseResponse<List<EmployeeAddressVO>> findAllEmployeeAddressBookByDepartment() {
		HRMSBaseResponse<List<EmployeeAddressVO>> response = new HRMSBaseResponse<>();
		try {
			response = organazationService.findAllEmployeeAddressBookByDepartment();
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

	@GetMapping("/activeemployee")
	public HRMSBaseResponse<List<EmployeeVO>> getActiveEmployeeList() {
		HRMSBaseResponse<List<EmployeeVO>> response = new HRMSBaseResponse<>();
		try {
			response = organazationService.getActiveEmployeeList();
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

	@Operation(summary = "API to fetch profile details of an employee", description = "This API returns complete profile details of a single employee including name, contact, department, grade, etc.")
	@GetMapping("/employee/profile")
	public HRMSBaseResponse<EmployeeProfileVO> getEmployeeProfileDetails(@RequestParam Long employeeId) {
		HRMSBaseResponse<EmployeeProfileVO> response = new HRMSBaseResponse<>();
		try {
			response = organazationService.getEmployeeProfileDetails(employeeId);
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

	@Operation(summary = "Download Employee Report", description = "This API is used to download the Employee Details report in Excel format based on optional keyword filter.")
	@PostMapping("/orgnizationreport/download-excel")
	public ResponseEntity<byte[]> downloadEmployeeDetailsReport(@RequestParam(required = false) String keyword) {
		try {
			byte[] response = organazationService.downloadEmployeeDetailsReport(keyword);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(ContentDisposition.builder("attachment")
					.filename("Employee_OrganizationDetails_Report.xlsx").build());

			return ResponseEntity.ok().headers(headers).body(response);

		} catch (HRMSException e) {
			log.error("HRMSException: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Error: " + e.getResponseMessage()).getBytes());
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
		}
	}
	
	
	@Operation(
		    summary = "API to fetch list of active HCD Dept employees",
		    description = "This API returns a list of all active employees from HCD Deprtment"
		)
		@GetMapping("/active-hr-list")
		public HRMSBaseResponse<List<HRListResponseVO>> getActiveHRList() {
		    HRMSBaseResponse<List<HRListResponseVO>> response = new HRMSBaseResponse<>();
		    try {
		        response = organazationService.getActiveHRList();
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
