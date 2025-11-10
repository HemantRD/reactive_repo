package com.vinsys.hrms.idp.service;

import com.vinsys.hrms.idp.dto.IdpDto;
import com.vinsys.hrms.idp.dto.IdpSubmitDto;

import java.util.List;

/**
 * Service interface for handling IDP business logic, persistence,
 * and workflow-based state transitions.
 */
public interface IdpService {

    /**
     * Creates or updates an IDP draft.
     */
    IdpDto saveIdp(IdpDto idp);

    /**
     * Executes the standard submit action (legacy use, if needed).
     * Use {@link #handleWorkflowAction(IdpSubmitDto)} for workflow-aware transitions.
     */
    void submitIdp(IdpSubmitDto idp);

    /**
     * Adds or updates review comments on an IDP.
     */
    void reviewIdp(IdpDto idp);

    /**
     * Retrieves all IDPs, optionally filtered by reporting manager,
     * department, or designation.
     */
    List<IdpDto> getAllIdp(String reportingManager, String department, String designation);

    /**
     * Retrieves a single IDP by its active flow ID.
     */
    IdpDto getIdpByFlowId(Long activeFlowId);

    /**
     * Handles workflow-based actions (SUBMIT, APPROVE, REJECT)
     * following the state design pattern.
     * <p>
     * Uses IdpContext → IdpPlan → IdpState to validate and execute transitions.
     *
     * @param idp the submit request payload containing ID and action
     * @return a success message describing the transition performed
     */
    String handleWorkflowAction(IdpSubmitDto idp);
}
