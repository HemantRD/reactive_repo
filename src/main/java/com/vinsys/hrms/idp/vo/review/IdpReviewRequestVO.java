package com.vinsys.hrms.idp.vo.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Wrapper request class for updating IDP details.
 *
 * Expected JSON structure:
 * <pre>
 * {
 *   "idp": {
 *     "id": 123456,
 *     "activeFlowId": 123456,
 *     "idpDetails": [
 *       { "id": 123456, "comment": "optional" },
 *       { "id": 123457, "comment": "optional" }
 *     ]
 *   }
 * }
 * </pre>
 *
 * This wrapper ensures clean structure and validation for IDP detail updates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpReviewRequestVO {

    /** Nested object containing IDP metadata and detail list. */
    @NotNull(message = "idp object must not be null")
    @Valid
    private IdpDetailsUpdateVO idp;
}