package com.vinsys.hrms.idp.reports.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
public class BudgetUtilization {
    private Double totalBudgetAmount;
    private Double totalRequestedAmount;
    private Double consumedAmount;
    private String currencySymbol;

}
