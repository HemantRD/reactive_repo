package com.vinsys.hrms.idp.trainingcatalog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchTopicsVo {
    private String trainingName;
    private String trainingCode;
    private String competencyTypeName;
    private String competencySubTypeName;
    private Long totalCount;
}
