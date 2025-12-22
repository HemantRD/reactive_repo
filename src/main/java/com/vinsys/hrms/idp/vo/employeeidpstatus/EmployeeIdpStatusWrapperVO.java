package com.vinsys.hrms.idp.vo.employeeidpstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeIdpStatusWrapperVO {

    @NotNull(message = "employeeIdpStatus is required")
    private EmployeeIdpStatusRequestVO employeeIdpStatus;
}
