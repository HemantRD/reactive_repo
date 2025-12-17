package com.vinsys.hrms.idp.controller;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.service.IEmployeeIdpStatusService;
import com.vinsys.hrms.idp.service.IIdpService;
import com.vinsys.hrms.idp.vo.employeeidpstatus.*;
import com.vinsys.hrms.idp.vo.getidp.IdpListResponseVO;
import com.vinsys.hrms.idp.vo.getidp.IdpVO;
import com.vinsys.hrms.idp.vo.getidp.IdpWrapperRequestVO;
import com.vinsys.hrms.idp.vo.save.IdpSaveResponseVO;
import com.vinsys.hrms.idp.vo.submit.IdpSubmitRequestVO;
import com.vinsys.hrms.idp.vo.submit.IdpSubmitVO;
import com.vinsys.hrms.util.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/idp")
public class IdpController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final IIdpService idpService;
    private final String applicationVersion;
    private final IEmployeeIdpStatusService employeeIdpStatusService;

    /**
     * Constructor injection for dependencies
     */
    @Autowired
    public IdpController(IIdpService idpService, @Value("${app_version}") String applicationVersion, IEmployeeIdpStatusService employeeIdpStatusService) {
        this.idpService = idpService;
        this.applicationVersion = applicationVersion;
        this.employeeIdpStatusService = employeeIdpStatusService;
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

        } catch (HRMSException e) {
            log.error(e.getMessage(), e);

            response.setResponseCode(e.getResponseCode());
            response.setResponseMessage(e.getResponseMessage());
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

        }catch (HRMSException e) {
            log.error(e.getMessage(), e);

            response.setResponseCode(e.getResponseCode());
            response.setResponseMessage(e.getResponseMessage());
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

        }catch (HRMSException e) {
            log.error(e.getMessage(), e);

            response.setResponseCode(e.getResponseCode());
            response.setResponseMessage(e.getResponseMessage());
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

    @Operation(summary = "Get IDP by Flow ID", description = "Retrieve IDP by activeFlowId")
    @GetMapping("/get")
    public HRMSBaseResponse<IdpVO> getIdpById(
            @RequestParam Long id) {

        HRMSBaseResponse<IdpVO> response = new HRMSBaseResponse<>();

        IdpVO idp = idpService.getIdpById(id);

        response.setResponseBody(idp);
        response.setResponseCode(200);
        response.setResponseMessage(idp != null ? "IDP found" : "No IDP found");
        response.setApplicationVersion(applicationVersion);
        response.setTotalRecord(idp != null ? 1 : 0);
        return response;
    }

    @Operation(summary = "Save Employee IDP Status", description = "Create a new employee IDP status record")
    @PostMapping("/employee-status/update")
    @Deprecated
    public HRMSBaseResponse<List<EmployeeIdpStatusResponseVO>> createEmployeeIdpStatus(
            @Valid @RequestBody EmployeeIdpStatusWrapperVO request) {

        HRMSBaseResponse<List<EmployeeIdpStatusResponseVO>> response = new HRMSBaseResponse<>();
        List<EmployeeIdpStatusResponseVO> saved = employeeIdpStatusService.createIdpStatus(request.getEmployeeIdpStatus());

        response.setResponseBody(saved);
        response.setResponseCode(200);
        response.setResponseMessage("Employee IDP status saved successfully");
        response.setApplicationVersion(applicationVersion);
        response.setTotalRecord(saved.size());

        return response;
    }

    @Operation(summary = "Get Employee IDP Status", description = "Retrieve employee IDP status by ID or by Employee ID")
    @GetMapping("/employee-status/get")
    public HRMSBaseResponse<EmployeeIdpStatusFullResponseVO> getEmployeeIdpStatus(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long employeeId) {

        HRMSBaseResponse<EmployeeIdpStatusFullResponseVO> response = new HRMSBaseResponse<>();

        if (id != null) {
            log.info("Fetching employee IDP status with id: {}", id);
            EmployeeIdpStatusFullResponseVO status = employeeIdpStatusService.getIdpStatus(id);

            response.setResponseBody(status);
        } else if (employeeId != null) {
            log.info("Fetching employee IDP status for employeeId: {}", employeeId);
            EmployeeIdpStatusFullResponseVO status = employeeIdpStatusService.getByEmployeeId(employeeId);

            response.setResponseBody(status);
        } else {
            throw new IllegalArgumentException("Either 'id' or 'employeeId' must be provided");
        }

        response.setResponseCode(200);
        response.setResponseMessage("Employee IDP status retrieved successfully");
        response.setApplicationVersion(applicationVersion);
        response.setTotalRecord(1);


        return response;
    }

    @Operation(summary = "Get All Employee IDP Statuses", description = "Retrieve all employee IDP status records")
    @GetMapping("/employee-status/getAll")
    public HRMSBaseResponse<List<EmployeeIdpStatusFullResponseVO>> getAllEmployeeIdpStatuses() {

        HRMSBaseResponse<List<EmployeeIdpStatusFullResponseVO>> response = new HRMSBaseResponse<>();

        log.info("Fetching all employee IDP statuses");
        List<EmployeeIdpStatusFullResponseVO> statuses = employeeIdpStatusService.getAllIdpStatuses();

        response.setResponseBody(statuses);
        response.setResponseCode(200);
        response.setResponseMessage("All employee IDP statuses retrieved successfully");
        response.setApplicationVersion(applicationVersion);
        response.setTotalRecord(statuses.size());

        return response;
    }

    @Operation(summary = "API for All Organzation Employee Details", description = "This API used to all employee deatils shows on Organzation details tab like empid,Name,Offical mail,location..,etc.")
    @GetMapping("/employee-status/getOrganizationEmployeesForIdp")
    HRMSBaseResponse<List<EmployeeIdpStatusVO>> getOrganizationEmployeesForIdp(
            @RequestBody EmpStatusListingReq request,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
        HRMSBaseResponse<List<EmployeeIdpStatusVO>> response = new HRMSBaseResponse<>();
        try {
            response = employeeIdpStatusService.getOrganizationEmployeesForIdp(request, pageable);
        } catch (HRMSException e) {
            log.error(e.getMessage(), e);
            response.setResponseCode(e.getResponseCode());
            response.setResponseMessage(e.getResponseMessage());
        }
        return response;
    }

    @Operation(summary = "API for All Organzation Employee Details", description = "This API used to all employee deatils shows on Organzation details tab like empid,Name,Offical mail,location..,etc.")
    @GetMapping("/employee-status/getOrganizationEmployeesForIdp/download")
    ResponseEntity<byte[]> getOrganizationEmployeesForIdpExcel(
            @RequestBody EmpStatusListingReq request,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
        try {
            byte[] response = employeeIdpStatusService.getOrganizationEmployeesForIdpExcel(request, pageable);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String fileName = "TrainingCatalogList_Report.xlsx";
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment").filename(fileName).build());

            return ResponseEntity.ok().headers(headers).body(response);
        } catch (HRMSException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error: " + e.getResponseMessage()).getBytes());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
        }
    }
}
