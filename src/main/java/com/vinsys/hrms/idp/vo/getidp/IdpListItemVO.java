package com.vinsys.hrms.idp.vo.getidp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

/**
 * VO representing a single IDP item in the list response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpListItemVO {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private String designation;

    private String function;

    private String department;

    private String grade;

    private String lineManagerName;

    private Instant createdDate;

    private String status;

    private Long activeFlowId;

    @Valid
    private List<IdpFlowDetailVO> flowDetails;

    private IdpFlowDetailVO activeFlowDetails;

    private List<String> allowedActions;
}
