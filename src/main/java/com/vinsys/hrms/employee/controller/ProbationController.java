package com.vinsys.hrms.employee.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.service.IProbationService;
import com.vinsys.hrms.employee.vo.ProbationFeedbackDetailsVO;
import com.vinsys.hrms.employee.vo.ProbationFeedbackVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author Onkar A
 *
 * 
 */

@RestController
@RequestMapping("probationv2")
public class ProbationController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IProbationService probationService;

	@Value("${app_version}")
	private String applicationVersion;

	@Operation(summary = "API for get probation feedback", description = "")
	@GetMapping("getfeedback")
	HRMSBaseResponse<ProbationFeedbackVO> getProbationFeedback(@RequestParam(required = false) Long empid,@RequestParam(required = false) Long feedbackId) {
		HRMSBaseResponse<ProbationFeedbackVO> response = new HRMSBaseResponse<>();
		try {
			response = probationService.getProbationFeedback(empid,feedbackId);
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

	@Operation(summary = "API used for save feedback for all role", description = "This API will use for save feedback form for probation confirmation. *For MANAGER and HR before submit you  have "
			+ "call first /getfeedback API.")
	@PostMapping("submitfeedback")
	HRMSBaseResponse<?> submitProbationFeedback(@RequestBody ProbationFeedbackVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = probationService.submitProbationFeedback(request);
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
	
	@Operation(summary = "Manager will get all feedback list", description = "")
	@GetMapping("manager/allfeedback")
	HRMSBaseResponse<List<ProbationFeedbackVO>> getProbationFeedbackList(Pageable pageable) {
		HRMSBaseResponse<List<ProbationFeedbackVO>> response = new HRMSBaseResponse<>();
		try {
			response = probationService.getProbationFeedbackList(pageable);
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
	
	@Operation(summary = "API for get probation feedback list", description = "")
	@GetMapping("getfeedbacklist")
	HRMSBaseResponse<ProbationFeedbackDetailsVO> getProbationFeedbackList(@RequestParam(required = false) Long empid) {
		HRMSBaseResponse<ProbationFeedbackDetailsVO> response = new HRMSBaseResponse<>();
		try {
			response = probationService.getProbationFeedbackListForEmployee(empid);
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
