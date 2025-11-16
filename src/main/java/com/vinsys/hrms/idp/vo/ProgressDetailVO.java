package com.vinsys.hrms.idp.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO representing progress tracking information for a specific
 * development goal or training activity within an IDP.
 * <p>
 * Captures incremental progress updates, remarks, and current status.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressDetailVO {

    /** Unique identifier of the progress record. */
    private Long id;

    /** Timestamp of the progress entry log. */
    private Instant logDate;

    /** Numeric progress value (e.g., 50 representing 50%). */
    private Integer progressValue;

    /** Measurement unit (e.g., %, hours, points). */
    private String progressUnit;

    /** Optional comment or remark added by the user or reviewer. */
    private String comment;

    /** Current status (e.g., Pending, In Progress, Completed). */
    private String status;

    /** Record lifecycle indicator (Active / Inactive). */
    private String recordStatus;
}