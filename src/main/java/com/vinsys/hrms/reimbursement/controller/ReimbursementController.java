package com.vinsys.hrms.reimbursement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Onkar A.
 * @Date 26-Apr-2023
 */

import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.reimbursement.service.IReimbursementService;
import com.vinsys.hrms.reimbursement.vo.AddClaimRequestVO;
import com.vinsys.hrms.reimbursement.vo.AddClaimResponseVO;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class ReimbursementController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IReimbursementService reimbursementService;

	@Value("${app_version}")
	private String applicationVersion;

	@Operation(summary = "API for add claim", description = "This API used to add claim.")
	@PostMapping("emp/addclaim")
	HRMSBaseResponse<AddClaimResponseVO> addClaim(@RequestBody AddClaimRequestVO request) {
		HRMSBaseResponse<AddClaimResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = reimbursementService.addClaim(request);
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
