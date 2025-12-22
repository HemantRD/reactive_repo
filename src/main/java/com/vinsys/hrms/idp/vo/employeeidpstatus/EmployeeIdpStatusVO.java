package com.vinsys.hrms.idp.vo.employeeidpstatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeIdpStatusVO {
    private long id;
    private String officialEmailId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String department;
    private String branch;
    private String division;
    private String dateOfBirth;
    private long contactNo;
    private String officialContactNo;
    private String employeeCode;
    private String name;
    private String designation;
    private String ReportingManager;
    private String Grade;
    private Long branchId;
    private Long departmentId;
    private Long divisionId;
    private String idpStatus;
}
