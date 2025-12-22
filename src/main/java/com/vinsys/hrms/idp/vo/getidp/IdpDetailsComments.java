package com.vinsys.hrms.idp.vo.getidp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpDetailsComments {

    /** Role of the person who added the comment (Employee, Manager, Reviewer, etc.) */
    private String role;

    /** Actual comment text */
    private String comment;

    /** Action taken by role (e.g., Submitted, Reviewed, Updated) */
    private String action;

    /** When the action/comment was made */
    private Instant actionDate;
}

