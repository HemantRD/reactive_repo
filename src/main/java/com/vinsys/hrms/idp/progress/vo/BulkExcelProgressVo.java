package com.vinsys.hrms.idp.progress.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkExcelProgressVo {
    private String memberEmail;
    private String trainingCode;
    private LocalDate progressDate;
    private Integer progressValue;
    private String progressUnit;
    private String remark;
    private String status;
}
