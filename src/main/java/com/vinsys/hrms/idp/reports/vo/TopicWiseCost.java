package com.vinsys.hrms.idp.reports.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicWiseCost {
    private String trainingName;
    private String trainingCode;
    private String competencyTypeName;
    private String competencySubTypeName;
    private String memberName;
    private Long memberCount;
    private Double totalCost;
}
