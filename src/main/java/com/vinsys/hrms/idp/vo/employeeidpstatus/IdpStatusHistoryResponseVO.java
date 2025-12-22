package com.vinsys.hrms.idp.vo.employeeidpstatus;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpStatusHistoryResponseVO {
    private Long id;
    private String status;
    private String remarks;
    private Instant effectiveDate;
}
