package com.vinsys.hrms.employee.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.constants.EResponse;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.service.IEmploymentDetailsService;
import com.vinsys.hrms.employee.vo.BankDetailsVO;
import com.vinsys.hrms.employee.vo.EmployeeCurrentDetailMainVO;
import com.vinsys.hrms.employee.vo.EmployeeDocumentVO;
import com.vinsys.hrms.employee.vo.PreviousEmploymentVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author Akanksha.
 */
@RestController
@RequestMapping("/employmentdetailsv2")
public class EmploymentDetailsController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IEmploymentDetailsService employmentDetailsService;
	@Value("${app_version}")
	private String applicationVersion;

	@Operation(summary = "API for get bank details", description = "This API used to get bank details of employee required for salary processing.")
	@GetMapping("getEmployeeBankDetails")
	HRMSBaseResponse<BankDetailsVO> getEmployeeBankDetails(@RequestParam(required = false) Long candidateId) {
		HRMSBaseResponse<BankDetailsVO> response = new HRMSBaseResponse<>();
		try {
			response = employmentDetailsService.getEmployeeBankDetails(candidateId);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API for previous employment details", description = "This API used to get previous employment details of employee.")
	@GetMapping("getPreviousEmploymentDetails")
	HRMSBaseResponse<PreviousEmploymentVO> getPreviousEmploymentDetails(@RequestParam(required = false) Long candidateId) {
		HRMSBaseResponse<PreviousEmploymentVO> response = new HRMSBaseResponse<>();
		try {
			response = employmentDetailsService.getPreviousEmploymentDetails(candidateId);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API to get All candidate documents", description = "This API used to get all docments of candidate of employee.")
	@GetMapping("getallcandidatedocuments")
	HRMSBaseResponse<List<EmployeeDocumentVO>> getAllCandidateDocuments() {
		HRMSBaseResponse<List<EmployeeDocumentVO>> response = new HRMSBaseResponse<>();
		try {
			response = employmentDetailsService.getAllCandidateDocuments();
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API to preview All candidate documents", description = "This API used to preview all docments of candidate of employee.")
	@GetMapping("previewdocument")
	ResponseEntity<String> previewAllDocuments(@RequestParam(required = true) String documentName ,
			@RequestParam(required=false) Long candidateId) {
		ResponseEntity<String> response;
		try {
			response = employmentDetailsService.previewAllDocuments(documentName , candidateId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response = new ResponseEntity<>(EResponse.FAILED.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return response;
	}

	@Operation(summary = "API for current employment details", description = "This API used to get current employment details of employee.")
	@GetMapping("currentorganization")
	HRMSBaseResponse<EmployeeCurrentDetailMainVO> getEmployeeCurrentOrganizationDetails(Long candidateId) {
		HRMSBaseResponse<EmployeeCurrentDetailMainVO> response = new HRMSBaseResponse<>();
		try {
			response = employmentDetailsService.getEmployeeCurrentOrganizationDetails(candidateId);
			response.setApplicationVersion(applicationVersion);
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
