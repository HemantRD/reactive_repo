package com.vinsys.hrms.idp.vo.submit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Wrapper request class for workflow-related actions on an IDP.
 * <p>
 * Expected request structure:
 * <pre>
 * {
 *   "idp": {
 *     "id": 1001,
 *     "activeFlowId": 5001,
 *     "action": "SUBMIT"
 *   }
 * }
 * </pre>
 * This wrapper ensures consistent validation and encapsulation
 * for workflow submissions (e.g., Submit, Approve, Reject).
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpSubmitRequestVO {

    /** Nested IDP action object containing identifiers and workflow action type. */
    @NotNull(message = "idp object must not be null")
    @Valid
    private IdpSubmitVO idp;
}