package com.vinsys.hrms.idp.service;

import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.vo.getidp.IdpListResponseVO;
import com.vinsys.hrms.idp.vo.getidp.IdpVO;
import com.vinsys.hrms.idp.vo.save.IdpSaveResponseVO;
import com.vinsys.hrms.idp.vo.submit.IdpSubmitVO;

/**
 * Service interface for handling IDP business logic, persistence,
 * and workflow-based state transitions.
 */
public interface IIdpService {

    /**
     * Creates or updates an IDP draft.
     */
    IdpSaveResponseVO saveIdp(IdpVO idp) throws HRMSException;

    /**
     * Executes the standard submit action (legacy use, if needed).
     * Use {@link (IdpSubmitVO)} for workflow-aware transitions.
     */
    void submitIdp(IdpSubmitVO idp) throws HRMSException;

    /**
     * Adds or updates review comments on an IDP.
     */
    void reviewIdp(IdpVO idp) throws HRMSException;

    /**
     * Retrieves all IDPs, optionally filtered by reporting manager,
     * department, or designation.
     */
    IdpListResponseVO getAllIdp(String role, Long departmentId) throws HRMSException;

    /**
     * Retrieves a single IDP by its active flow ID.
     */
    IdpVO getIdpById(Long activeFlowId);
}
