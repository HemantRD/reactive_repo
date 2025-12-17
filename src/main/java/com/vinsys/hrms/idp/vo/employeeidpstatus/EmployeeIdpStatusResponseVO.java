package com.vinsys.hrms.idp.vo.employeeidpstatus;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeIdpStatusResponseVO {
    private Long employeeId;
    private String status;
}
