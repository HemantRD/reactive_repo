package com.vinsys.hrms.idp.vo.employeeidpstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdpStatusHistoryRequestVO {
    private String status;
    private String remarks;
    private Instant effectiveDate;
}
