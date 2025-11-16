package com.vinsys.hrms.idp.vo.request;

import com.vinsys.hrms.idp.vo.IdpVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Wrapper request class for IDP operations such as create, update, or review.
 * <p>
 * Expected request structure:
 * <pre>
 * {
 *   "idp": {
 *     "id": 123456,
 *     "activeFlowId": 123456,
 *     "idpDetails": [ ... ]
 *   }
 * }
 * </pre>
 * This class serves as a request envelope to ensure the payload is always
 * validated and structured consistently.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpWrapperRequestVO {

    /** Nested IDP object containing plan details and metadata. */
    @NotNull(message = "idp object must not be null")
    @Valid
    private IdpVO idp;
}