package com.vinsys.hrms.idp.trainingcatalog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingBudgetVo {
    private Long id;
    private Long year;
    private Double budgetAmount;
    private String currencySymbol;
    private String remark;
}
