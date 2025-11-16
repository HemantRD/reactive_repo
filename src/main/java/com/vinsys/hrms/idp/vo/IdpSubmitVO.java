package com.vinsys.hrms.idp.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a workflow action request on an IDP record.
 * <p>
 * Used to perform state transitions such as <b>Submit</b>, <b>Approve</b>, or <b>Reject</b>
 * within the IDP workflow process.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpSubmitVO {

    /** Identifier of the target IDP record. */
    private Long id;

    /** Active workflow (flow history) reference for this IDP. */
    private Long activeFlowId;

    /** Action to be performed (e.g., SUBMIT, APPROVE, REJECT). */
    private String action;
}