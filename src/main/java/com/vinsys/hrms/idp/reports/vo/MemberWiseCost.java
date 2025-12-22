package com.vinsys.hrms.idp.reports.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberWiseCost {
    private String memberName;
    private String trainingName;
    private String trainingCode;
    private String competencyTypeName;
    private String competencySubTypeName;
    private Long coursesCount;
    private Double totalCost;
}
