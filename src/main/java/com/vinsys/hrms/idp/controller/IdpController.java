package com.vinsys.hrms.idp.controller;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.service.IIdpService;
import com.vinsys.hrms.idp.vo.IdpVO;
import com.vinsys.hrms.idp.vo.IdpSubmitVO;
import com.vinsys.hrms.idp.vo.request.IdpWrapperRequestVO;
import com.vinsys.hrms.idp.vo.request.IdpSubmitRequestVO;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/idp")
public class IdpController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IIdpService idpService;

    @Value("${app_version}")
    private String applicationVersion;

    @Operation(summary = "Save IDP", description = "Create or update an IDP record")
    @PostMapping("/save")
    public HRMSBaseResponse<IdpVO> saveIdp(@Valid @RequestBody IdpWrapperRequestVO request) {

        HRMSBaseResponse<IdpVO> response = new HRMSBaseResponse<>();

        try {
            IdpVO saved = idpService.saveIdp(request.getIdp());

            response.setResponseBody(saved);
            response.setResponseCode(200);
            response.setResponseMessage("IDP saved successfully");
            response.setApplicationVersion(applicationVersion);
            response.setTotalRecord(1);

        } catch (Exception e) {
            log.error("Unexpected error saving IDP", e);
            response.setResponseCode(1500);
            response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
            response.setApplicationVersion(applicationVersion);
        }

        return response;
    }

    @Operation(summary = "Submit IDP", description = "Submit, Approve or Reject an IDP workflow")
    @PostMapping("/submit")
    public HRMSBaseResponse<String> submitIdp(@Valid @RequestBody IdpSubmitRequestVO request) {

        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

        try {
            IdpSubmitVO dto = request.getIdp();
            String result = idpService.handleWorkflowAction(dto);

            response.setResponseBody(result);
            response.setResponseCode(200);
            response.setResponseMessage("Success");
            response.setApplicationVersion(applicationVersion);

        } catch (Exception e) {
            log.error("Unexpected workflow error", e);
            response.setResponseCode(1500);
            response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
            response.setApplicationVersion(applicationVersion);
        }

        return response;
    }

    @Operation(summary = "Review IDP", description = "Add reviewer comments")
    @PostMapping("/review")
    public HRMSBaseResponse<String> reviewIdp(@Valid @RequestBody IdpWrapperRequestVO request) {

        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

        try {
            idpService.reviewIdp(request.getIdp());

            response.setResponseBody("Review submitted successfully");
            response.setResponseCode(200);
            response.setResponseMessage("Success");
            response.setApplicationVersion(applicationVersion);

        } catch (Exception e) {
            log.error("Unexpected review error", e);
            response.setResponseCode(1500);
            response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
            response.setApplicationVersion(applicationVersion);
        }

        return response;
    }

    @Operation(summary = "List IDPs", description = "Fetch all IDPs with optional filters")
    @GetMapping("/list")
    public HRMSBaseResponse<List<IdpVO>> getAllIdp(
            @RequestParam(required = false) String reportingManager,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation) {

        HRMSBaseResponse<List<IdpVO>> response = new HRMSBaseResponse<>();

        try {
            List<IdpVO> list = idpService.getAllIdp(reportingManager, department, designation);

            response.setResponseBody(list);
            response.setResponseCode(200);
            response.setResponseMessage("Fetched all IDPs successfully");
            response.setApplicationVersion(applicationVersion);
            response.setTotalRecord(list != null ? list.size() : 0);

        } catch (Exception e) {
            log.error("Error fetching IDPs", e);
            response.setResponseCode(1500);
            response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
            response.setApplicationVersion(applicationVersion);
        }

        return response;
    }

    @Operation(summary = "Get IDP by Flow ID", description = "Retrieve IDP by activeFlowId")
    @GetMapping
    public HRMSBaseResponse<IdpVO> getIdpByFlowId(@RequestParam Long activeFlowId) {

        HRMSBaseResponse<IdpVO> response = new HRMSBaseResponse<>();

        try {
            IdpVO idp = idpService.getIdpByFlowId(activeFlowId);

            response.setResponseBody(idp);
            response.setResponseCode(200);
            response.setResponseMessage(idp != null ? "IDP found" : "No IDP found");
            response.setApplicationVersion(applicationVersion);
            response.setTotalRecord(idp != null ? 1 : 0);

        } catch (Exception e) {
            log.error("Error retrieving IDP by flowId", e);
            response.setResponseCode(1500);
            response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
            response.setApplicationVersion(applicationVersion);
        }
        return response;
    }
}
