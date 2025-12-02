package com.vinsys.hrms.idp.vo.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents the inner 'idp' object from the update request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpDetailsUpdateVO {

    /** Primary IDP identifier. */
    @NotNull(message = "id must not be null")
    private Long id;

    /** Active workflow instance reference. */
    private Long activeFlowId;

    /** List of IDP detail objects to update. */
    @Valid
    private List<IdpDetailUpdateVO> idpDetails;
}