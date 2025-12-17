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
public class ProgressDetailVO {

    private Long id;

    private Instant logDate;

    private Integer progressValue;

    private String progressUnit;

    private String comment;

    private String status;

    private String recordStatus;
}