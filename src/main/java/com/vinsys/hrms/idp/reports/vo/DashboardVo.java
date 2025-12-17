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
}

