package com.vinsys.hrms.idp.controller;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.idp.dto.IdpDto;
import com.vinsys.hrms.idp.dto.IdpSubmitDto;
import com.vinsys.hrms.idp.dto.request.IdpSubmitRequest;
import com.vinsys.hrms.idp.dto.request.IdpWrapperRequest;
import com.vinsys.hrms.idp.service.IdpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for handling Individual Development Plan (IDP) operations.
 * <p>
 * Exposes endpoints for creating, updating, submitting, reviewing,
 * and retrieving IDP records. Each endpoint wraps its response
 * in a standardized {@link HRMSBaseResponse} object for consistent
 * API response formatting.
 * </p>
 *
 * <h3>Key Endpoints:</h3>
 * <ul>
 *     <li>{@code POST /save} - Create or update an IDP record.</li>
 *     <li>{@code POST /submit} - Trigger a workflow action (Submit / Approve / Reject).</li>
 *     <li>{@code POST /review} - Add review comments or feedback.</li>
 *     <li>{@code GET /list} - Retrieve all IDPs with optional filters.</li>
 *     <li>{@code GET} - Fetch a specific IDP by its workflow ID.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/idp")
public class IdpController {

    private final IdpService idpService;

    public IdpController(IdpService idpService) {
        this.idpService = idpService;
    }

    /**
     * Creates or updates an IDP record in the system.
     * <p>
     * If the {@code id} field is absent, a new record is created with DRAFT status.
     * Otherwise, existing details are updated while maintaining workflow state.
     * </p>
     *
     * @param request wrapper containing a validated {@link IdpDto} object
     * @return {@link HRMSBaseResponse} containing the saved IDP data
     */
    @PostMapping("/save")
    public ResponseEntity<HRMSBaseResponse<IdpDto>> saveIdp(@Valid @RequestBody IdpWrapperRequest request) {
        IdpDto saved = idpService.saveIdp(request.getIdp());

        HRMSBaseResponse<IdpDto> response = new HRMSBaseResponse<>();
        response.setResponseBody(saved);
        response.setResponseCode(HttpStatus.OK.value());
        response.setResponseMessage("IDP saved successfully");
        response.setApplicationVersion("v1.0");
        response.setTotalRecord(1);

        return ResponseEntity.ok(response);
    }

    /**
     * Applies a workflow action (e.g., SUBMIT, APPROVE, REJECT) to an existing IDP.
     * <p>
     * This operation invokes the workflow state machine implemented via
     * the State Design Pattern — validating transitions before applying changes.
     * </p>
     *
     * @param request wrapper containing {@link IdpSubmitDto} with ID and action
     * @return {@link HRMSBaseResponse} containing the workflow transition result
     */
    @PostMapping("/submit")
    public ResponseEntity<HRMSBaseResponse<String>> submitIdp(@Valid @RequestBody IdpSubmitRequest request) {
        IdpSubmitDto dto = request.getIdp();
        String resultMessage = idpService.handleWorkflowAction(dto);

        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        response.setResponseBody(resultMessage);
        response.setResponseCode(HttpStatus.OK.value());
        response.setResponseMessage("Success");
        response.setApplicationVersion("v1.0");
        response.setTotalRecord(0);

        return ResponseEntity.ok(response);
    }

    /**
     * Updates review comments or feedback on an existing IDP.
     * <p>
     * Typically used by reviewers, line managers, or approvers to provide
     * feedback before the approval stage.
     * </p>
     *
     * @param request wrapper containing the IDP details and comments
     * @return generic success response
     */
    @PostMapping("/review")
    public ResponseEntity<HRMSBaseResponse<String>> reviewIdp(@Valid @RequestBody IdpWrapperRequest request) {
        idpService.reviewIdp(request.getIdp());

        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        response.setResponseBody("Review submitted successfully");
        response.setResponseCode(HttpStatus.OK.value());
        response.setResponseMessage("Success");
        response.setApplicationVersion("v1.0");
        response.setTotalRecord(0);

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a list of all IDPs, optionally filtered by:
     * <ul>
     *     <li>Reporting Manager</li>
     *     <li>Department</li>
     *     <li>Designation</li>
     * </ul>
     *
     * @param reportingManager optional filter for manager name
     * @param department optional filter for department name
     * @param designation optional filter for designation
     * @return {@link HRMSBaseResponse} containing a list of matching IDPs
     */
    @GetMapping("/list")
    public ResponseEntity<HRMSBaseResponse<List<IdpDto>>> getAllIdp(
            @RequestParam(required = false) String reportingManager,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation) {

        List<IdpDto> list = idpService.getAllIdp(reportingManager, department, designation);

        HRMSBaseResponse<List<IdpDto>> response = new HRMSBaseResponse<>();
        response.setResponseBody(list);
        response.setResponseCode(HttpStatus.OK.value());
        response.setResponseMessage("Fetched all IDPs successfully");
        response.setApplicationVersion("v1.0");
        response.setTotalRecord(list != null ? list.size() : 0);

        return ResponseEntity.ok(response);
    }

    /**
     * Fetches a specific IDP record using its active workflow ID.
     *
     * @param activeFlowId unique identifier of the IDP workflow
     * @return {@link HRMSBaseResponse} containing the requested IDP details
     */
    @GetMapping
    public ResponseEntity<HRMSBaseResponse<IdpDto>> getIdpByFlowId(@RequestParam Long activeFlowId) {
        IdpDto idp = idpService.getIdpByFlowId(activeFlowId);

        HRMSBaseResponse<IdpDto> response = new HRMSBaseResponse<>();
        response.setResponseBody(idp);
        response.setResponseCode(HttpStatus.OK.value());
        response.setResponseMessage(idp != null ? "IDP found" : "No IDP found for given flowId");
        response.setApplicationVersion("v1.0");
        response.setTotalRecord(idp != null ? 1 : 0);

        return ResponseEntity.ok(response);
    }
}