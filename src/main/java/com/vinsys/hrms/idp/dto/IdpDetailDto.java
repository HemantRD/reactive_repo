package com.vinsys.hrms.idp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object representing detailed information
 * within an Individual Development Plan (IDP).
 * <p>
 * Includes competency details, development goals/actions,
 * optional training references, comments, and progress tracking.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpDetailDto {

    /** Unique identifier for this IDP detail record. */
    private Long id;

    /** Foreign key reference to competency type. */
    private Integer competencyTypeId;

    /** Foreign key reference to competency sub-type. */
    private Integer competencySubTypeId;

    /** Display name for competency type. */
    private String competencyTypeName;

    /** Display name for competency sub-type. */
    private String competencySubTypeName;

    /** Development goal description provided by employee or manager. */
    private String devGoal;

    /** Specific actions or activities planned to achieve the development goal. */
    private String devActions;

    /** Optional reference to a training catalog entry. */
    private Long trainingId;

    /** Name of the training (can be custom if not in catalog). */
    private String trainingName;

    /** Additional object for external or flexible training metadata. */
    private Object trainingObj;

    /** Reviewer or manager comments on this detail entry. */
    private String comment;

    /** List of progress updates or milestones under this development goal. */
    private List<ProgressDetailDto> progressDetails;
}