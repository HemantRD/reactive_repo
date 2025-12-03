package com.vinsys.hrms.idp.reports.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class DashboardVo {
    private String currencySymbol;
    private List<TopTrainingCourses> topRequestedTopics;
    private GroupVsIndividualCostSummary groupVsIndividualCostSummary;
    private BudgetUtilization budgetUtilization;
    private GroupVsIndividualTrainingCountSummary groupVsIndividualTrainingCountSummary;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetUtilization {
        private Double totalBudgetAmount;
        private Double consumedAmount;
        private Double totalRequestedAmount;
        private String currencySymbol;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupVsIndividualCostSummary {
        private Double group;
        private Double individual;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupVsIndividualTrainingCountSummary {
        private int totalTrainingCount;
        private int groupTrainingCount;
        private int individualTrainingCount;
    }
}

