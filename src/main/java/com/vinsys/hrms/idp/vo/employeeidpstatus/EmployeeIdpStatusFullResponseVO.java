package com.vinsys.hrms.idp.vo.employeeidpstatus;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeIdpStatusFullResponseVO {
    private Long id;
    private Long employeeId;
    private String idpSubmissionStatus;
    private String recordStatus;
    private Instant updatedOn;
    private String updatedBy;
}
