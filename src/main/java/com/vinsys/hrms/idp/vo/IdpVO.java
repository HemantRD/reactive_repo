package com.vinsys.hrms.idp.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object representing an Individual Development Plan (IDP).
 * <p>
 * Contains key employee information, plan metadata, workflow status,
 * and detailed competency/development goals.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpVO {

    /** Primary identifier of the IDP record. */
    private Long id;

    /** Foreign key reference to active workflow instance (nullable). */
    private Long activeFlowId;

    /** Associated employee identifier. */
    private Long employeeId;

    /** Human-readable employee name (optional for display). */
    private String employeeName;

    /** Employee’s current job position. */
    private String position;

    /** Employee’s function (business or organizational function). */
    private String function;

    /** Employee’s grade or level within the organization. */
    private String grade;

    /** Record lifecycle status (e.g., Active / Inactive). */
    private String recordStatus;

    /** Current workflow or business status (e.g., Draft, Review, Approved). */
    private String status;

    /** Timestamp indicating when the IDP was initially created. */
    private Instant createdDate;

    /** Timestamp indicating last update or modification. */
    private Instant updatedDate;

    /** Actions allowed from current state (populated dynamically by workflow engine). */
    private List<String> allowedActions;

    /** Nested collection of IDP details representing development goals, actions, and progress. */
    @Valid
    private List<IdpDetailVO> idpDetails;
}