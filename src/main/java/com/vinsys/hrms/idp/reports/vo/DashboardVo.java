package com.vinsys.hrms.idp.reports.vo;

import lombok.Data;

import java.util.List;

@Data
public class DashboardVo {
    private String currencySymbol;
    private List<TopTrainingCourses> topRequestedTopics;
    private GroupVsIndividualCostSummary groupVsIndividualCostSummary;
    private BudgetUtilization budgetUtilization;
    private GroupVsIndividualTrainingCountSummary groupVsIndividualTrainingCountSummary;

    @Data
    public static class BudgetUtilization {
        private int totalBudgetAmount;
        private int consumedAmount;
        private int totalRequestedAmount;
    }

    @Data
    public static class GroupVsIndividualCostSummary {
        private int group;
        private int individual;
    }

    @Data
    public static class GroupVsIndividualTrainingCountSummary {
        private int totalTrainingCount;
        private int groupTrainingCount;
        private int individualTrainingCount;
    }
}

