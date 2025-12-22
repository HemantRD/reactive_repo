package com.vinsys.hrms.idp.trainingcatalog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingBudgetVo {
    private Long id;
    private Long year;
    private Double budgetAmount;
    private Double idpRequestAmount;
    private Double consumedAmount;
    private String currencySymbol;
    private String remark;
    private Date createdDate;
    private Date updatedDate;
}
