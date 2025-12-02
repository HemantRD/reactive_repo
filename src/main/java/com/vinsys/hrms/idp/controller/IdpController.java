package com.vinsys.hrms.idp.controller;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.idp.service.IIdpService;
import com.vinsys.hrms.idp.vo.getidp.IdpListResponseVO;
import com.vinsys.hrms.idp.vo.getidp.IdpVO;
import com.vinsys.hrms.idp.vo.save.IdpSaveResponseVO;
import com.vinsys.hrms.idp.vo.submit.IdpSubmitVO;
import com.vinsys.hrms.idp.vo.getidp.IdpWrapperRequestVO;
import com.vinsys.hrms.idp.vo.submit.IdpSubmitRequestVO;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/idp")
public class IdpController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final IIdpService idpService;
    private final String applicationVersion;

    /**
     * Constructor injection for dependencies
     */
    @Autowired
    public IdpController(IIdpService idpService, @Value("${app_version}") String applicationVersion) {
        this.idpService = idpService;
        this.applicationVersion = applicationVersion;
    }

    @Operation(summary = "Save IDP", description = "Create or update an IDP record")
    @PostMapping("/save")
    public HRMSBaseResponse<IdpSaveResponseVO> saveIdp(@Valid @RequestBody IdpWrapperRequestVO request) {

        HRMSBaseResponse<IdpSaveResponseVO> response = new HRMSBaseResponse<>();

        try {
            IdpSaveResponseVO saved = idpService.saveIdp(request.getIdp());

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
            idpService.submitIdp(dto);
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
    @GetMapping("/getAll")
    public HRMSBaseResponse<IdpListResponseVO> getAllIdp(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Long departmentId) {

        HRMSBaseResponse<IdpListResponseVO> response = new HRMSBaseResponse<>();

        try {
            IdpListResponseVO list = idpService.getAllIdp(role, departmentId);

            response.setResponseBody(list);
            response.setResponseCode(200);
            response.setResponseMessage("Fetched all IDPs successfully");
            response.setApplicationVersion(applicationVersion);

        } catch (Exception e) {
            log.error("Error fetching IDPs", e);
            response.setResponseCode(1500);
            response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
            response.setApplicationVersion(applicationVersion);
        }

        return response;
    }

    @Operation(summary = "Get IDP by Flow ID", description = "Retrieve IDP by activeFlowId")
    @GetMapping("/get")
    public HRMSBaseResponse<IdpVO> getIdpById(
            @RequestParam Long id) {

        HRMSBaseResponse<IdpVO> response = new HRMSBaseResponse<>();

        try {
            IdpVO idp = idpService.getIdpById(id);

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
