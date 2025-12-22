package com.vinsys.hrms.idp.vo.employeeidpstatus;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeIdpStatusRequestVO {

    @NotEmpty(message = "Employee IDs cannot be empty")
    private List<Long> employeeIds;

    @NotNull(message = "IDP Submission Status is required")
    @Pattern(regexp = "Open|Close", message = "IDP Submission Status must be Open or Close")
    private String idpSubmissionStatus;

    @NotBlank(message = "Record status is required")
    @Pattern(regexp = "Active|Inactive", message = "Record status must be Active or Inactive")
    private String recordStatus;
}