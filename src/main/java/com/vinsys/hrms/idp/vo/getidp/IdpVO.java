package com.vinsys.hrms.idp.vo.getidp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

/**
 * Individual Development Plan master VO containing metadata,
 * workflow state, allowed actions, and detailed competency plans.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpVO {

    private Long id;

    private Long activeFlowId;

    private Long employeeId;

    private String employeeName;

    private String lineManagerName;

    private String designation;

    private String function;

    private String department;

    private String grade;

    private Long departmentId;

    private Long divId;

    private Instant createdDate;

    private String status;

    private List<String> allowedActions;

    @Valid
    private List<IdpFlowDetailVO> flowDetails;

    @Valid
    private List<IdpDetailVO> idpDetails;

    @Valid
    private IdpFlowDetailVO activeFlowDetails;

    @Valid
    private List<IdpProgressDetailVO> progressDetails;
}
