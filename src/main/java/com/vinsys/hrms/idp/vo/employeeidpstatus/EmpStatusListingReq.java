package com.vinsys.hrms.idp.vo.employeeidpstatus;

import lombok.Data;

@Data
public class EmpStatusListingReq {
    private Long branchId;
    private String branch;
    private String keyword;
    private Long gradeId;
    private Long deptId;
    private String idpSubmissionStatus;
}
