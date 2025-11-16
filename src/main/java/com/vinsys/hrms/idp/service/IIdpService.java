package com.vinsys.hrms.idp.service;

import com.vinsys.hrms.idp.vo.IdpVO;
import com.vinsys.hrms.idp.vo.IdpSubmitVO;

import java.util.List;

/**
 * Service interface for handling IDP business logic, persistence,
 * and workflow-based state transitions.
 */
public interface IIdpService {

    /**
     * Creates or updates an IDP draft.
     */
    IdpVO saveIdp(IdpVO idp);

    /**
     * Executes the standard submit action (legacy use, if needed).
     * Use {@link #handleWorkflowAction(IdpSubmitVO)} for workflow-aware transitions.
     */
    void submitIdp(IdpSubmitVO idp);

    /**
     * Adds or updates review comments on an IDP.
     */
    void reviewIdp(IdpVO idp);

    /**
     * Retrieves all IDPs, optionally filtered by reporting manager,
     * department, or designation.
     */
    List<IdpVO> getAllIdp(String reportingManager, String department, String designation);

    /**
     * Retrieves a single IDP by its active flow ID.
     */
    IdpVO getIdpByFlowId(Long activeFlowId);

    /**
     * Handles workflow-based actions (SUBMIT, APPROVE, REJECT)
     * following the state design pattern.
     * <p>
     * Uses IdpContext → IdpPlan → IdpState to validate and execute transitions.
     *
     * @param idp the submit request payload containing ID and action
     * @return a success message describing the transition performed
     */
    String handleWorkflowAction(IdpSubmitVO idp);
}
