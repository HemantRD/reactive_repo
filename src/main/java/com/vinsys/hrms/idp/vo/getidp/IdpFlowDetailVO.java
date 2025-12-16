package com.vinsys.hrms.idp.vo.getidp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpFlowDetailVO {

    private Long id;

    private Long ownerEmployeeId;

    private String ownerEmployeeName;

    private String ownerEmployeeRole;

    private Instant startDate;

    private String action;

    private Instant actionDate;

    private String status;

    private String recordStatus;
}